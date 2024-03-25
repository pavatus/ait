package loqor.ait.tardis;

import loqor.ait.api.tardis.TardisEvents;
import loqor.ait.client.util.ClientShakeUtil;
import loqor.ait.client.util.ClientTardisUtil;
import loqor.ait.core.item.ChargedZeitonCrystalItem;
import loqor.ait.core.item.TardisItemBuilder;
import loqor.ait.core.util.DeltaTimeManager;
import loqor.ait.core.util.TimeUtil;
import loqor.ait.registry.DesktopRegistry;
import loqor.ait.tardis.data.FuelData;
import loqor.ait.tardis.data.TardisHandlersManager;
import loqor.ait.AITMod;
import loqor.ait.core.AITSounds;
import loqor.ait.registry.ExteriorVariantRegistry;
import loqor.ait.tardis.data.DoorData;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.exterior.category.ExteriorCategorySchema;
import loqor.ait.tardis.exterior.variant.ExteriorVariantSchema;
import loqor.ait.tardis.util.AbsoluteBlockPos;
import loqor.ait.tardis.util.TardisChunkUtil;
import loqor.ait.tardis.util.TardisUtil;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;

public class Tardis {
	// this is starting to get a little bloated.. - you're tellin' me fam

	protected TardisTravel travel;
	private final UUID uuid;
	protected TardisDesktop desktop;
	protected TardisExterior exterior;
	//protected TardisDoor door;
	protected TardisHandlersManager handlers;
	private boolean dirty = false;

	public int tardisHammerAnnoyance = 0; // todo move :(

	public Tardis(UUID uuid, AbsoluteBlockPos.Directed pos, TardisDesktopSchema schema, ExteriorCategorySchema exteriorType, ExteriorVariantSchema variant) {
		this(uuid, tardis -> new TardisTravel(tardis, pos), tardis -> new TardisDesktop(tardis, schema), (tardis) -> new TardisExterior(tardis, exteriorType, variant), false);
		tardisHammerAnnoyance = 0;
	}

	protected Tardis(UUID uuid, Function<Tardis, TardisTravel> travel, Function<Tardis, TardisDesktop> desktop, Function<Tardis, TardisExterior> exterior, boolean locked) {
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
		if (handlers == null) {
			handlers = new TardisHandlersManager(this);
		}

		return handlers;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() == null) return false;
		Tardis tardis = (Tardis) o;
		return uuid.equals(tardis.uuid);
	}

	@Override
	public int hashCode() {
		return Objects.hash(uuid);
	}

	public void init() {
		this.init(false);
	}

	public void init(boolean dirty) {
		// this.init(this.getTravel(), dirty);
		// this.init(this.getDesktop(), dirty);
		// this.init(this.getExterior(), dirty);
		// this.init(this.getDoor(), dirty);
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

	// unlock destop stuff
	// kill me.
	public boolean isDesktopUnlocked(TardisDesktopSchema schema) {
		return PropertiesHandler.isSchemaUnlocked(getHandlers().getProperties(), schema);
	}

	public void unlockDesktop(TardisDesktopSchema schema) {
		PropertiesHandler.setSchemaUnlocked(getHandlers().getProperties(), schema, true);
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
		if (!hasPower()) return;
		DeltaTimeManager.createDelay(AITMod.MOD_ID + "-driftingmusicdelay", (long) TimeUtil.secondsToMilliseconds(new Random().nextInt(1, 360)));

		//PropertiesHandler.set(this, PropertiesHandler.POWER_DELTA, MAX_POWER_DELTA_TICKS);
		PropertiesHandler.set(this, PropertiesHandler.HAS_POWER, false);
		TardisEvents.LOSE_POWER.invoker().onLosePower(this);
	}

	public void enablePower() {
		if (getFuel() <= (0.01 * FuelData.TARDIS_MAX_FUEL)) return; // cant enable power if not enough fuel
		// if (isSiegeBeingHeld()) return; // cant re-enable while being held, this may become OP tho
		if (isSiegeMode()) setSiegeMode(false);
		if (hasPower()) return;

		//PropertiesHandler.set(this, PropertiesHandler.POWER_DELTA, 0);
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

	private void generateInteriorWithItem() {
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
					this.getHandlers().getInteriorChanger().queueInteriorChange(TardisItemBuilder.findRandomDesktop(this));
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

	/**
	 * Called at the end of a servers tick
	 *
	 * @param server the server being ticked
	 */
	public void tick(MinecraftServer server) {
		// most of the logic is in the handlers, so we can just disable them if we're a growth
		// if (!isGrowth())
		//     this.getHandlers().tick(server);
		if (!this.hasPower() && !DeltaTimeManager.isStillWaitingOnDelay(AITMod.MOD_ID + "-driftingmusicdelay")) {
			List<PlayerEntity> playerEntities = TardisUtil.getPlayersInsideInterior(this);
			for (PlayerEntity player : playerEntities) {
				player.playSound(AITSounds.DRIFTING_MUSIC, SoundCategory.MUSIC, 1, 1);
			}
			DeltaTimeManager.createDelay(AITMod.MOD_ID + "-driftingmusicdelay", (long) TimeUtil.minutesToMilliseconds(new Random().nextInt(7, 9)));
		}
		if (tardisHammerAnnoyance > 0 && !DeltaTimeManager.isStillWaitingOnDelay(AITMod.MOD_ID + "-tardisHammerAnnoyanceDecay")) {
			tardisHammerAnnoyance--;
			DeltaTimeManager.createDelay(AITMod.MOD_ID + "-tardisHammerAnnoyanceDecay", (long) TimeUtil.secondsToMilliseconds(10));
		}
		// @TODO if tnt explodes in the interior (near the console), then it should crash
		if (this.isGrowth()) {
            /*if (this.getHandlers().getInteriorChanger().isGenerating()) {
                for (PlayerEntity player : TardisUtil.getPlayersInInterior(this)) {
                    TardisUtil.teleportOutside(this, player);
                }
            } else {
                this.generateInteriorWithNetherStar();
            }*/
			this.generateInteriorWithItem();
		}
		if (isGrowth() && getDoor().isBothClosed() && !getHandlers().getInteriorChanger().isGenerating())
			getDoor().openDoors();
		if (isGrowth() && getDoor().locked() && !getHandlers().getInteriorChanger().isGenerating())
			getDoor().setLocked(false);

		if (isSiegeMode() && !getDoor().locked())
			getDoor().setLocked(true);

		this.getHandlers().tick(server);

		// im sure this is great for your server performace
		if (TardisChunkUtil.shouldExteriorChunkBeForced(this) && !TardisChunkUtil.isExteriorChunkForced(this)) {
			TardisChunkUtil.forceLoadExteriorChunk(this);
		} else if (!TardisChunkUtil.shouldExteriorChunkBeForced(this) && TardisChunkUtil.isExteriorChunkForced(this)) {
			TardisChunkUtil.stopForceExteriorChunk(this);
		}

		// autoland stuff
		// if (getTravel().getState() == TardisTravel.State.FLIGHT && PropertiesHandler.getBool(getHandlers().getProperties(), PropertiesHandler.AUTO_LAND)) {
		//     getTravel().materialise();
		// }

		// fixme nuh uh i dont like it when it locks on land it makes me sadge, instead lock if it was locked - Loqor

        /*if (PropertiesHandler.getBool(getHandlers().getProperties(), PropertiesHandler.IS_FALLING) && !getHandlers().getDoor().locked()) {
            DoorData.lockTardis(true, this, null, true);
        }*/
		if (PropertiesHandler.getBool(getHandlers().getProperties(), PropertiesHandler.IS_FALLING)) {
			DoorData.lockTardis(true, this, null, true);
		}

		AbsoluteBlockPos.Directed pos = this.getExterior().getExteriorPos();
		// If we're falling nearly out of the world, freak out.
		if (PropertiesHandler.getBool(this.getHandlers().getProperties(), PropertiesHandler.IS_FALLING)
				&& pos.getY() <= pos.getWorld().getBottomY() + 2) {
			PropertiesHandler.set(this, PropertiesHandler.ANTIGRAVS_ENABLED, true);
			PropertiesHandler.set(this, PropertiesHandler.IS_FALLING, false);
		}

		this.getTravel().tick(server);
		this.getDesktop().tick(server);
	}

	/**
	 * Called at the end of a worlds tick
	 *
	 * @param world the world being ticked
	 */
	public void tick(ServerWorld world) {
	}

	/**
	 * Called at the end of a clients tick, ONLY FOR CLIENT STUFF!!
	 *
	 * @param client the remote being ticked
	 */
	public void tick(MinecraftClient client) { // fixme should likely be in ClientTardis instead, same with  other server-only things should be in ServerTardis
		// referencing client stuff where it COULD be server causes problems
		if (ClientShakeUtil.shouldShake(this)) {
			ClientShakeUtil.shakeFromConsole();
		}

		if (this.equals(ClientTardisUtil.getCurrentTardis())) {
			ClientTardisUtil.tickPowerDelta();
			ClientTardisUtil.tickAlarmDelta();
		}
	}

	public boolean isDirty() {
		return dirty;
	}

	// todo replace all instances of "markDirty" with eg "this.sync()" or a properties sync (the set method with the tardis variable)
	@Deprecated
	public void markDirty() {
		dirty = false;
	}

	/**
	 * Called at the START of a servers tick, ONLY to be used for syncing data to avoid comodification errors
	 *
	 * @param server the current minecraft server
	 */
	public void startTick(MinecraftServer server) {
		if (this instanceof ServerTardis && isDirty()) {
			((ServerTardis) this).sync();
			dirty = false;
		}
	}

	// todo move these up

	public boolean isInDanger() {
		return this.getHandlers().getHADS().isInDanger();
	}

	public boolean inFlight() {
		return this.getTravel().getState() != TardisTravel.State.LANDED;
	}
}