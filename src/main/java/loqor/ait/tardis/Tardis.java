package loqor.ait.tardis;

import loqor.ait.AITMod;
import loqor.ait.api.tardis.TardisEvents;
import loqor.ait.core.item.ChargedZeitonCrystalItem;
import loqor.ait.core.util.DeltaTimeManager;
import loqor.ait.core.util.TimeUtil;
import loqor.ait.registry.impl.DesktopRegistry;
import loqor.ait.registry.impl.exterior.ExteriorVariantRegistry;
import loqor.ait.registry.unlockable.Unlockable;
import loqor.ait.tardis.base.AbstractTardisComponent;
import loqor.ait.tardis.data.DoorData;
import loqor.ait.tardis.data.FuelData;
import loqor.ait.tardis.data.SonicHandler;
import loqor.ait.tardis.data.TardisHandlersManager;
import loqor.ait.tardis.data.loyalty.Loyalty;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.core.data.schema.exterior.ExteriorCategorySchema;
import loqor.ait.core.data.schema.exterior.ExteriorVariantSchema;
import loqor.ait.core.data.AbsoluteBlockPos;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;

public class Tardis {

	protected TardisTravel travel;
	private final UUID uuid;
	protected TardisDesktop desktop;
	protected TardisExterior exterior;
	//protected TardisDoor door;
	protected TardisHandlersManager handlers;
	protected boolean dirty = false;

	public int tardisHammerAnnoyance = 0; // todo move :(

	public Tardis(UUID uuid, AbsoluteBlockPos.Directed pos, TardisDesktopSchema schema, ExteriorCategorySchema exteriorType, ExteriorVariantSchema variant) {
		this(uuid, tardis -> new TardisTravel(tardis, pos), tardis -> new TardisDesktop(tardis, schema), (tardis) -> new TardisExterior(tardis, exteriorType, variant));
		tardisHammerAnnoyance = 0;
	}

	protected Tardis(UUID uuid, Function<Tardis, TardisTravel> travel, Function<Tardis, TardisDesktop> desktop, Function<Tardis, TardisExterior> exterior) {
		this.uuid = uuid;
		this.travel = travel.apply(this);
		this.desktop = desktop.apply(this);
		this.exterior = exterior.apply(this);
		tardisHammerAnnoyance = 0;
	}

	public UUID getUuid() {
		return uuid;
	}

	public TardisDesktop getDesktop() {
		return desktop;
	}

	public TardisExterior getExterior() {
		return exterior;
	}

	public DoorData getDoor() {
		return this.getHandlers().getDoor();
	}

	public SonicHandler getSonic() {
		return this.getHandlers().getSonic();
	}

	// dont use this
	public void setLockedTardis(boolean bool) {
		this.getDoor().setLocked(bool);
	}

	public boolean getLockedTardis() {
		return this.getDoor().locked();
	}

	public TardisTravel getTravel() {
		return travel;
	}

	/**
	 * Retrieves the TardisHandlersManager instance associated with the given UUID.
	 *
	 * @return TardisHandlersManager instance or null if it doesn't exist
	 */
	public TardisHandlersManager getHandlers() {
		if (handlers == null)
			handlers = new TardisHandlersManager(this);

		return handlers;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		return o instanceof Tardis other
				&& uuid.equals(other.uuid);
	}

	@Override
	public int hashCode() {
		return Objects.hash(uuid);
	}

	public void init() {
		this.init(false);
	}

	public void init(boolean dirty) {

	}

	private void init(AbstractTardisComponent component, boolean dirty) {
		AbstractTardisComponent.Init mode = component.getInitMode();
		component.setTardis(this);

		switch (mode) {
			case NO_INIT -> {
			}
			case ALWAYS -> component.init();
			case FIRST -> {
				if (!dirty) component.init();
			}
			case DESERIALIZE -> {
				if (dirty) component.init();
			}
			default -> throw new IllegalArgumentException("Unimplemented init mode " + mode);
		}
	}

	// todo clean up all this
	// fuel - because getHandlers() blah blah is annoying me
	public double addFuel(double fuel) {
		return this.getHandlers().getFuel().addFuel(fuel);
	}

	public void removeFuel(double fuel) {
		this.getHandlers().getFuel().removeFuel(fuel);
	}

	public double getFuel() {
		return this.getHandlers().getFuel().getCurrentFuel();
	}

	public void setFuelCount(double i) {
		this.getHandlers().getFuel().setCurrentFuel(i);
	}

	public boolean isRefueling() {
		return this.getHandlers().getFuel().isRefueling();
	}

	public void setRefueling(boolean b) {
		this.getHandlers().getFuel().setRefueling(b);
	}

	public void setIsInDanger(boolean danger) {
		this.getHandlers().getHADS().setIsInDanger(danger);
	}

	public boolean isUnlocked(Unlockable unlockable) {
		return unlockable.getRequirement() == Loyalty.MIN || PropertiesHandler.isUnlocked(this, unlockable);
	}

	// for now this just checks that the exterior is the coral growth, which is bad. but its fine for first beta
	// this should stop basic features of the tardis from happening
	public boolean isGrowth() {
		return hasGrowthExterior() || hasGrowthDesktop();
	}

	public boolean hasGrowthExterior() {
		return Objects.equals(getExterior().getVariant(), ExteriorVariantRegistry.CORAL_GROWTH);
	}

	public boolean hasGrowthDesktop() {
		return Objects.equals(getDesktop().getSchema(), DesktopRegistry.DEFAULT_CAVE);
	}

	public boolean hasPower() {
		return PropertiesHandler.getBool(this.getHandlers().getProperties(), PropertiesHandler.HAS_POWER);
	}

	public void disablePower() {
		if (!this.hasPower())
			return;

		DeltaTimeManager.createDelay(AITMod.MOD_ID + "-driftingmusicdelay", (long) TimeUtil.secondsToMilliseconds(new Random().nextInt(1, 360)));

		PropertiesHandler.set(this, PropertiesHandler.HAS_POWER, false);
		TardisEvents.LOSE_POWER.invoker().onLosePower(this);
	}

	public void enablePower() {
		if (this.getFuel() <= (0.01 * FuelData.TARDIS_MAX_FUEL))
			return; // cant enable power if not enough fuel

		if (this.isSiegeMode())
			this.setSiegeMode(false);

		if (this.hasPower())
			return;

		PropertiesHandler.set(this, PropertiesHandler.HAS_POWER, true);
		TardisEvents.REGAIN_POWER.invoker().onRegainPower(this);
	}

	public void togglePower() {
		if (hasPower())
			disablePower();
		else
			enablePower();
	}

	public boolean areShieldsActive() {
		return this.getHandlers().getShields().areShieldsActive();
	}

	public boolean areVisualShieldsActive() {
		return this.getHandlers().getShields().areVisualShieldsActive();
	}


	public boolean isSiegeMode() {
		return this.getHandlers().getSiege().isSiegeMode();
	}

	public void setSiegeMode(boolean b) {
		this.getHandlers().getSiege().setSiegeMode(b);
	}

	public boolean isSiegeBeingHeld() {
		return this.getHandlers().getSiege().isSiegeBeingHeld();
	}

	public void setSiegeBeingHeld(UUID b) {
		this.getHandlers().getSiege().setSiegeBeingHeld(b);
	}

	public int getTimeInSiegeMode() {
		return this.getHandlers().getSiege().getTimeInSiegeMode();
	}

	public AbsoluteBlockPos.Directed position() {
		return this.getTravel().getPosition();
	}

	public AbsoluteBlockPos.Directed destination() {
		return this.getTravel().getDestination();
	}

	protected void generateInteriorWithItem() {
		TardisUtil.getEntitiesInInterior(this, 50)
				.stream()
				.filter(entity -> (entity instanceof ItemEntity) &&
						(((ItemEntity) entity).getStack().getItem() == Items.NETHER_STAR ||
								isChargedCrystal(((ItemEntity) entity).getStack())) &&
						entity.isTouchingWater()).forEach(entity -> {
					if (this.getExterior().getExteriorPos() == null) return;
					this.setFuelCount(8000);
					this.enablePower();
					entity.getWorld().playSound(null, entity.getBlockPos(), SoundEvents.BLOCK_BEACON_POWER_SELECT, SoundCategory.BLOCKS, 10.0F, 0.75F);
					entity.getWorld().playSound(null, this.getExterior().getExteriorPos(), SoundEvents.BLOCK_BEACON_POWER_SELECT, SoundCategory.BLOCKS, 10.0F, 0.75F);
					this.getHandlers().getInteriorChanger().queueInteriorChange(DesktopRegistry.getInstance().getRandom(this));
					if (this.getHandlers().getInteriorChanger().isGenerating()) {
						entity.discard();
					}
				});
	}

	private boolean isChargedCrystal(ItemStack stack) {
		if(stack.getItem() instanceof ChargedZeitonCrystalItem) {
			NbtCompound nbt = stack.getOrCreateNbt();
			if(nbt.contains(ChargedZeitonCrystalItem.FUEL_KEY)) {
				return nbt.getDouble(ChargedZeitonCrystalItem.FUEL_KEY) >= ChargedZeitonCrystalItem.MAX_FUEL;
			}
		}
		return false;
	}

	public boolean isDirty() {
		return dirty;
	}

	@Deprecated
	public void markDirty() {
		this.dirty = false;
	}

	public boolean isInDanger() {
		return this.getHandlers().getHADS().isInDanger();
	}

	public boolean inFlight() {
		return this.getTravel().getState() != TardisTravel.State.LANDED;
	}
}