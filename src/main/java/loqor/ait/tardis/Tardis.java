package loqor.ait.tardis;

import loqor.ait.core.data.AbsoluteBlockPos;
import loqor.ait.core.data.SerialDimension;
import loqor.ait.core.item.ChargedZeitonCrystalItem;
import loqor.ait.registry.impl.DesktopRegistry;
import loqor.ait.registry.impl.exterior.ExteriorVariantRegistry;
import loqor.ait.registry.unlockable.Unlockable;
import loqor.ait.tardis.base.Initializable;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.control.sequences.SequenceHandler;
import loqor.ait.tardis.data.*;
import loqor.ait.tardis.data.loyalty.Loyalty;
import loqor.ait.tardis.data.loyalty.LoyaltyHandler;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.data.properties.PropertiesHolder;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public abstract class Tardis extends Initializable<TardisComponent.InitContext> {

	private UUID uuid;
	protected TardisTravel travel;
	protected TardisDesktop desktop;
	protected TardisExterior exterior;
	protected TardisHandlersManager handlers;

	protected boolean dirty = false;
	public int tardisHammerAnnoyance = 0; // todo move :(

	protected Tardis(UUID uuid, TardisTravel travel, TardisDesktop desktop, TardisExterior exterior) {
		this.uuid = uuid;
		this.travel = travel;
		this.desktop = desktop;
		this.exterior = exterior;
		this.handlers = new TardisHandlersManager();

		tardisHammerAnnoyance = 0;
	}

	protected Tardis() { }

	@Override
	protected void onInit(TardisComponent.InitContext ctx) {
		TardisComponent.init(travel, this, ctx);
		TardisComponent.init(desktop, this, ctx);
		TardisComponent.init(exterior, this, ctx);
		TardisComponent.init(handlers, this, ctx);
	}

	public static void init(Tardis tardis, boolean deserialized) {
		Initializable.init(tardis, new TardisComponent.InitContext(deserialized));
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
		return this.getHandlers().get(TardisComponent.Id.DOOR);
	}

	public SonicHandler sonic() {
		return this.getHandlers().get(TardisComponent.Id.SONIC);
	}

	@Deprecated
	public void setLockedTardis(boolean bool) {
		this.getDoor().setLocked(bool);
	}

	public boolean getLockedTardis() {
		return this.getDoor().locked();
	}

	public TardisTravel travel() {
		return travel;
	}

	public LoyaltyHandler loyalty() {
		return this.handler(TardisComponent.Id.LOYALTY);
	}

	public ServerAlarmHandler alarm() {
		return this.handler(TardisComponent.Id.ALARMS);
	}

	public StatsData stats() {
		return this.handler(TardisComponent.Id.STATS);
	}

	public EngineHandler engine() {
		return this.handler(TardisComponent.Id.ENGINE);
	}

	/**
	 * Retrieves the TardisHandlersManager instance associated with the given UUID.
	 *
	 * @return TardisHandlersManager instance or null if it doesn't exist
	 */
	public TardisHandlersManager getHandlers() {
		return handlers;
	}

	public @NotNull <T extends TardisComponent> T handler(TardisComponent.Id type) {
		return this.handlers.get(type);
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
		return uuid.hashCode();
	}

	// todo clean up all this
	// fuel - because getHandlers() blah blah is annoying me
	public double addFuel(double fuel) {
		return this.<FuelData>handler(TardisComponent.Id.FUEL).addFuel(fuel);
	}

	public void removeFuel(double fuel) {
		this.<FuelData>handler(TardisComponent.Id.FUEL).removeFuel(fuel);
	}

	public double getFuel() {
		return this.<FuelData>handler(TardisComponent.Id.FUEL).getCurrentFuel();
	}

	public void setFuelCount(double i) {
		this.<FuelData>handler(TardisComponent.Id.FUEL).setCurrentFuel(i);
	}

	public boolean isRefueling() {
		return this.<FuelData>handler(TardisComponent.Id.FUEL).isRefueling();
	}

	public void setRefueling(boolean b) {
		this.<FuelData>handler(TardisComponent.Id.FUEL).setRefueling(b);
	}

	public boolean isInDanger() {
		return this.<HADSData>handler(TardisComponent.Id.HADS).isInDanger();
	}

	public FuelData fuel() {
		return this.handler(TardisComponent.Id.FUEL);
	}

	public PropertiesHolder properties() {
		return this.handler(TardisComponent.Id.PROPERTIES);
	}

	public FlightData flight() {
		return this.handler(TardisComponent.Id.FLIGHT);
	}

	public TardisCrashData crash() {
		return this.handler(TardisComponent.Id.CRASH_DATA);
	}

	public SequenceHandler sequence() {
		return this.handler(TardisComponent.Id.SEQUENCE);
	}

	public WaypointHandler waypoint() {
		return this.handler(TardisComponent.Id.WAYPOINTS);
	}

	public boolean inFlight() {
		return this.travel().getState() != TardisTravel.State.LANDED;
	}

	public boolean isUnlocked(Unlockable unlockable) {
		return unlockable.getRequirement() == Loyalty.MIN
				|| PropertiesHandler.isUnlocked(this, unlockable);
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

	public boolean areShieldsActive() {
		return this.<ShieldData>handler(TardisComponent.Id.SHIELDS).areShieldsActive();
	}

	public boolean areVisualShieldsActive() {
		return this.<ShieldData>handler(TardisComponent.Id.SHIELDS).areVisualShieldsActive();
	}

	public SiegeData siege() {
		return this.handler(TardisComponent.Id.SIEGE);
	}

	public boolean isSiegeBeingHeld() {
		return this.<SiegeData>handler(TardisComponent.Id.SIEGE).isSiegeBeingHeld();
	}

	public void setSiegeBeingHeld(UUID b) {
		this.<SiegeData>handler(TardisComponent.Id.SIEGE).setSiegeBeingHeld(b);
	}

	public AbsoluteBlockPos.Directed position() {
		return this.travel().getPosition();
	}

	public AbsoluteBlockPos.Directed destination() {
		return this.travel().getDestination();
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
					this.engine().enablePower();
					entity.getWorld().playSound(null, entity.getBlockPos(), SoundEvents.BLOCK_BEACON_POWER_SELECT, SoundCategory.BLOCKS, 10.0F, 0.75F);
					entity.getWorld().playSound(null, this.getExterior().getExteriorPos(), SoundEvents.BLOCK_BEACON_POWER_SELECT, SoundCategory.BLOCKS, 10.0F, 0.75F);

					InteriorChangingHandler interior = this.handler(TardisComponent.Id.INTERIOR);
					interior.queueInteriorChange(DesktopRegistry.getInstance().getRandom(this));

					if (interior.isGenerating()) {
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

	public AbsoluteBlockPos.Directed getDoorPos() {
		return this.desktop.getInteriorDoorPos() != null ? this.desktop.getInteriorDoorPos() :
				new AbsoluteBlockPos.Directed(0, 0, 0, new SerialDimension(World.OVERWORLD.getValue().toString()), 0);
	}

	public AbsoluteBlockPos.Directed getExteriorPos() {
		return this.travel != null ? this.travel.getPosition() :
				new AbsoluteBlockPos.Directed(0, 0, 0, new SerialDimension(World.OVERWORLD.getValue().toString()), 0);
	}

	public boolean isDirty() {
		return dirty;
	}

	@Deprecated
	public void markDirty() {
		this.dirty = false;
	}
}