package mdteam.ait.tardis;

import io.wispforest.owo.ops.WorldOps;
import mdteam.ait.AITMod;
import mdteam.ait.api.tardis.TardisEvents;
import mdteam.ait.core.AITBlocks;
import mdteam.ait.core.AITMessages;
import mdteam.ait.core.AITSounds;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.blocks.ExteriorBlock;
import mdteam.ait.core.sounds.MatSound;
import mdteam.ait.core.util.ForcedChunkUtil;
import mdteam.ait.registry.CategoryRegistry;
import mdteam.ait.registry.ExteriorVariantRegistry;
import mdteam.ait.tardis.control.impl.SecurityControl;
import mdteam.ait.tardis.control.impl.pos.PosType;
import mdteam.ait.tardis.control.sequences.Sequence;
import mdteam.ait.tardis.control.sequences.SequenceHandler;
import mdteam.ait.tardis.data.DoorData;
import mdteam.ait.tardis.data.SonicHandler;
import mdteam.ait.tardis.data.TardisCrashData;
import mdteam.ait.tardis.data.TardisLink;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.util.FlightUtil;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.tardis.wrapper.server.ServerTardis;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static mdteam.ait.AITMod.AIT_CONFIG;

// todo this class is like a monopoly, im gonna slash it into little corporate pieces
public class TardisTravel extends TardisLink {
	private static final String MAX_SPEED_KEY = "max_speed";
	private static final int DEFAULT_MAX_SPEED = 7;
	private State state = State.LANDED;
	private AbsoluteBlockPos.Directed position;
	private AbsoluteBlockPos.Directed destination;
	private AbsoluteBlockPos.Directed lastPosition;
	private boolean crashing = false;
	private static final int CHECK_LIMIT = AIT_CONFIG.SEARCH_HEIGHT();
	private static final Random random = new Random();

	public TardisTravel(Tardis tardis, AbsoluteBlockPos.Directed pos) {
		super(tardis, "travel");
		this.position = pos;
		if (this.lastPosition == null) this.lastPosition = pos;
	}

	public TardisTravel(Tardis tardis, AbsoluteBlockPos.Directed pos, AbsoluteBlockPos.Directed dest, State state) {
		super(tardis, "travel");
		this.position = pos;
		if (this.lastPosition == null) this.lastPosition = pos;
		this.destination = dest;
		this.state = state;
	}

	public boolean isCrashing() {
		return this.crashing;
	}

	public void setPosition(AbsoluteBlockPos.Directed pos) {
		this.position = pos;
	}

	public void setLastPosition(AbsoluteBlockPos.Directed position) {
		this.lastPosition = position;
	}

	public AbsoluteBlockPos.Directed getLastPosition() {
		return lastPosition;
	}

	public AbsoluteBlockPos.Directed getPosition() {
		return position;
	}

	public void tick(MinecraftServer server) {
		if (this.findTardis().isEmpty()) return;

		this.tickDemat();
		this.tickMat();

		ServerTardis tardis = (ServerTardis) this.findTardis().get();
		int speed = this.getSpeed();
		State state = this.getState();
		boolean handbrake = PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.HANDBRAKE);
		boolean autopilot = PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.AUTO_LAND);

		if (speed > 0 && state == State.LANDED && !handbrake && !tardis.getHandlers().getSonic().hasSonic(SonicHandler.HAS_EXTERIOR_SONIC)) {
			this.dematerialise(autopilot);
		}
		if (speed == 0 && state == State.FLIGHT) {
			if (tardis.getHandlers().getCrashData().getState() == TardisCrashData.State.UNSTABLE) {
				int random_int = random.nextInt(0, 2);
				int up_or_down = random_int == 0 ? 1 : -1;
				int random_change = random.nextInt(1, 10) * up_or_down;
				int new_x = getDestination().getX() + random_change;
				int new_y = getDestination().getX();
				int new_z = getDestination().getZ() + random_change;
				this.setDestination(new AbsoluteBlockPos.Directed(new_x, new_y, new_z, getDestination().getWorld(), getDestination().getDirection()));
				if (getDestination().getWorld().getRegistryKey() == TardisUtil.getTardisDimension().getRegistryKey()) {
					this.setDestination(new AbsoluteBlockPos.Directed(new_x, new_y, new_z, TardisUtil.getServer().getOverworld(), getDestination().getDirection()));
				}
			}
			this.materialise();
		}
		// Should we just disable autopilot if the speed goes above 1?
		if (speed > 1 && state == State.FLIGHT && autopilot) {
			this.setSpeed(speed - 1);
		}

		// fixme this is a mess

        /*if (this.getSpeed() > 0 && this.getState() == State.FLIGHT && !this.getTardis().get().getHandlers().getFlight().hasFinishedFlight()) {
            if (random.nextFloat(1) > 0.985f && !PropertiesHandler.willAutoPilot(this.tardis().getHandlers().getProperties())) {
                if (this.getSpeed() == 1) {
                    this.crash();
                }

                this.decreaseSpeed();
            }
        }*/
	}

	public void increaseSpeed() {
		// Stop speed from going above 1 if autopilot is enabled and we're in flight
		if (this.getSpeed() + 1 > 1
				&& this.getState() == State.FLIGHT
				&& this.findTardis().isPresent()
				&& PropertiesHandler.getBool(this.findTardis().get().getHandlers().getProperties(), PropertiesHandler.AUTO_LAND)) {
			return;
		}

		this.setSpeed(MathHelper.clamp(this.getSpeed() + 1, 0, this.getMaxSpeed()));
	}

	public void decreaseSpeed() {
		if (this.findTardis().isEmpty()) return;

		if (this.getState() == State.LANDED && this.getSpeed() == 1) {
			FlightUtil.playSoundAtConsole(this.findTardis().get(), AITSounds.LAND_THUD, SoundCategory.AMBIENT);
		}

		this.setSpeed(MathHelper.clamp(this.getSpeed() - 1, 0, this.getMaxSpeed()));
	}

	public int getSpeed() {
		if (this.findTardis().isEmpty()) return 0;
		return PropertiesHandler.getInt(this.findTardis().get().getHandlers().getProperties(), PropertiesHandler.SPEED);
	}

	public void setSpeed(int speed) {
		if (this.findTardis().isEmpty()) return;

		PropertiesHandler.set(this.findTardis().get(), PropertiesHandler.SPEED, speed);
	}

	public int getMaxSpeed() {
		if (this.findTardis().isEmpty()) return DEFAULT_MAX_SPEED;

		if (!this.findTardis().get().getHandlers().getProperties().getData().containsKey(MAX_SPEED_KEY)) {
			setMaxSpeed(DEFAULT_MAX_SPEED);
		}

		return PropertiesHandler.getInt(this.findTardis().get().getHandlers().getProperties(), MAX_SPEED_KEY);
	}

	public void setMaxSpeed(int speed) {
		if (this.findTardis().isEmpty()) return;
		PropertiesHandler.set(this.findTardis().get(), MAX_SPEED_KEY, speed);
	}

	public boolean inFlight() {
		if (this.findTardis().isEmpty()) return false;
		return this.getState() == State.FLIGHT;
	}

	public boolean isMaterialising() {
		if (this.findTardis().isEmpty()) return false;
		return this.getState() == State.MAT;
	}

	public boolean isDematerialising() {
		if (this.findTardis().isEmpty()) return false;
		return this.getState() == State.DEMAT;
	}

	/**
	 * Gets the number of ticks that the Tardis has been materialising for
	 *
	 * @return ticks
	 */
	public int getMatTicks() {
		if (this.findTardis().isEmpty()) return 0;
		return PropertiesHandler.getInt(this.findTardis().get().getHandlers().getProperties(), PropertiesHandler.MAT_TICKS);
	}

	private void setMatTicks(int ticks) {
		if (this.findTardis().isEmpty()) return;

		PropertiesHandler.set(this.findTardis().get(), PropertiesHandler.MAT_TICKS, ticks, false);
	}

	private void tickMat() {
		if (this.getState() != State.MAT) {
			if (getMatTicks() != 0) setMatTicks(0);
			return;
		}

		setMatTicks(getMatTicks() + 1);

		if (getMatTicks() > (FlightUtil.getSoundLength(getMatSoundForCurrentState()) * 40)) {
			this.forceLand();
			this.setMatTicks(0);
		}
	}

	/**
	 * Gets the number of ticks that the Tardis has been dematerialising for
	 *
	 * @return ticks
	 */
	public int getDematTicks() {
		if (this.findTardis().isEmpty()) return 0;

		return PropertiesHandler.getInt(this.findTardis().get().getHandlers().getProperties(), PropertiesHandler.DEMAT_TICKS);
	}

	private void setDematTicks(int ticks) {
		if (this.findTardis().isEmpty()) return;

		PropertiesHandler.set(this.findTardis().get(), PropertiesHandler.DEMAT_TICKS, ticks, false);
	}

	private void tickDemat() {
		if (this.getState() != State.DEMAT) {
			if (getDematTicks() != 0) setDematTicks(0);
			return;
		}
		if (this.findTardis().isEmpty()) return;

		setDematTicks(getDematTicks() + 1);

		if (PropertiesHandler.getBool(this.findTardis().get().getHandlers().getProperties(), PropertiesHandler.HANDBRAKE)) {
			// cancel materialise
			this.cancelDemat();
			return;
		}

		if (getDematTicks() > (FlightUtil.getSoundLength(getMatSoundForCurrentState()) * 40)) {
			this.toFlight();
			this.setDematTicks(0);
		}
	}

	/**
	 * Stops demat while its happening - then plays a boom sound to signify
	 */
	private void cancelDemat() {
		if (this.getState() != State.DEMAT || this.findTardis().isEmpty()) return; // rip
		if (this.getPosition() == null || this.findTardis().get().getDesktop() == null) return;

		this.forceLand();
		this.playThudSound();
		AITMessages.sendToInterior(this.findTardis().get(), AITMessages.CANCEL_DEMAT_SOUND, PacketByteBufs.empty());
	}

	public void playThudSound() {
		if (this.findTardis().isEmpty()) return;
		this.getPosition().getWorld().playSound(null, this.getPosition(), AITSounds.LAND_THUD, SoundCategory.AMBIENT);

		FlightUtil.playSoundAtConsole(this.findTardis().get(), AITSounds.LAND_THUD, SoundCategory.AMBIENT);
	}

	/**
	 * Performs a crash for the Tardis.
	 * If the Tardis is not in flight state, the crash will not be executed.
	 */
	public void crash() {
		if (this.getState() != State.FLIGHT || findTardis().isEmpty() || this.isCrashing() || TardisUtil.getTardisDimension() == null || TardisUtil.isClient())
			return;
		Tardis tardis = findTardis().get();

		int crash_intensity = getSpeed() + tardis.tardisHammerAnnoyance + 1;

		FlightUtil.playSoundAtConsole(tardis,
				SoundEvents.ENTITY_GENERIC_EXPLODE,
				SoundCategory.BLOCKS,
				3f,
				1f);

		List<Explosion> explosions = new ArrayList<>();

		tardis.getDesktop().getConsoles().forEach(console -> {
			Explosion explosion = TardisUtil.getTardisDimension().createExplosion(
					null,
					null,
					null,
					console.position().toCenterPos(),
					3f * crash_intensity,
					false,
					World.ExplosionSourceType.MOB
			);
			explosions.add(explosion);
		});

		if (findTardis().get().getHandlers().getSequenceHandler().hasActiveSequence()) {
			findTardis().get().getHandlers().getSequenceHandler().setActiveSequence(null, true);
		}

		Random random = new Random();
		for (ServerPlayerEntity player : TardisUtil.getPlayersInInterior(tardis)) {
			float random_X_velocity = random.nextFloat(-2f, 3f);
			float random_Y_velocity = random.nextFloat(-1f, 2f);
			float random_Z_velocity = random.nextFloat(-2f, 3f);
			player.setVelocity(random_X_velocity * crash_intensity, random_Y_velocity * crash_intensity, random_Z_velocity * crash_intensity);
			//player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 20 * crash_intensity, (int) Math.round(0.25 * crash_intensity), true, false, false));
			player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 20 * crash_intensity, 1, true, false, false));
			player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 20 * crash_intensity, (int) Math.round(0.25 * crash_intensity), true, false, false));
			player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 20 * crash_intensity, (int) Math.round(0.25 * crash_intensity), true, false, false));
			player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20 * crash_intensity, (int) Math.round(0.25 * crash_intensity), true, false, false));
			player.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 20 * crash_intensity, (int) Math.round(0.75 * crash_intensity), true, false, false));
			int damage_to_player = (int) Math.round(0.5 * crash_intensity);
			if (!explosions.isEmpty()) {
				player.damage(TardisUtil.getTardisDimension().getDamageSources().explosion(explosions.get(0)), damage_to_player);
			} else {
				player.damage(TardisUtil.getTardisDimension().getDamageSources().generic(), damage_to_player);
			}
		}
		tardis.setLockedTardis(true);
		PropertiesHandler.set(tardis, PropertiesHandler.ALARM_ENABLED, true);
		PropertiesHandler.set(tardis, PropertiesHandler.ANTIGRAVS_ENABLED, false);
		this.setSpeed(0);
		tardis.removeFuel(500 * crash_intensity);
		tardis.tardisHammerAnnoyance = 0;
		int random_int = random.nextInt(0, 2);
		int up_or_down = random_int == 0 ? 1 : -1;
		int random_change = random.nextInt(10, 100) * crash_intensity * up_or_down;
		AbsoluteBlockPos.Directed percentageOfDestination = FlightUtil.getPositionFromPercentage(tardis.position(), tardis.destination(), tardis.getHandlers().getFlight().getDurationAsPercentage());
		int new_x = percentageOfDestination.getX() + random_change;
		int new_y = percentageOfDestination.getY();
		int new_z = percentageOfDestination.getZ() + random_change;
		this.setCrashing(true);
		this.setDestination(new AbsoluteBlockPos.Directed(new_x, new_y, new_z, getDestination().getWorld(), getDestination().getDirection()));
		if (getDestination().getWorld().getRegistryKey() == TardisUtil.getTardisDimension().getRegistryKey()) {
			this.setDestination(new AbsoluteBlockPos.Directed(new_x, new_y, new_z, TardisUtil.getServer().getOverworld(), getDestination().getDirection()));
		}
		this.crashAndMaterialise();
		int repair_ticks = 1000 * crash_intensity;
		tardis.getHandlers().getCrashData().setRepairTicks(repair_ticks);
		if (repair_ticks > TardisCrashData.UNSTABLE_TICK_START_THRESHOLD) {
			tardis.getHandlers().getCrashData().setState(TardisCrashData.State.TOXIC);
		} else {
			tardis.getHandlers().getCrashData().setState(TardisCrashData.State.UNSTABLE);
		}
		TardisEvents.CRASH.invoker().onCrash(tardis);
	}

	public void materialise() {
		this.materialise(false);
	}

	public void crashAndMaterialise() {
		if (this.getDestination().getWorld().isClient() || findTardis().isEmpty() || this.getState() != State.FLIGHT) {
			return;
		}
		this.setState(State.MAT);
		ServerWorld destWorld = (ServerWorld) this.getDestination().getWorld();
		ForcedChunkUtil.keepChunkLoaded(destWorld, this.getDestination());
		ExteriorBlock block = (ExteriorBlock) AITBlocks.EXTERIOR_BLOCK;
		BlockState state = block.getDefaultState().with(Properties.HORIZONTAL_FACING, this.getDestination().getDirection());
		destWorld.setBlockState(this.getDestination(), state);

		// Create and add the exterior block entity at the destination
		ExteriorBlockEntity blockEntity = new ExteriorBlockEntity(this.getDestination(), state);
		destWorld.addBlockEntity(blockEntity);

		// Set the position of the Tardis to the destination
		this.setPosition(this.getDestination());
		WorldOps.updateIfOnServer(destWorld, this.getDestination());
	}

	/**
	 * Materialises the Tardis, bringing it to the specified destination.
	 * This method handles the logic of materialization, including sound effects, locking the Tardis, and setting the Tardis state.
	 */
	public void materialise(boolean ignoreChecks) {
		// Check if running on the client side, and if so, return early
		if (this.getDestination().getWorld().isClient() || this.findTardis().isEmpty()) {
			return;
		}

		ServerTardis tardis = (ServerTardis) this.findTardis().get();

		if (this.getState() != State.FLIGHT) return;

		// Disable autopilot
		// PropertiesHandler.setAutoPilot(this.getTardis().get().getHandlers().getProperties(), false);

		this.setDestination(FlightUtil.getPositionFromPercentage(tardis.position(), tardis.destination(), tardis.getHandlers().getFlight().getDurationAsPercentage()), true);

		// Check if materialization is on cooldown and return if it is
		if (!ignoreChecks && FlightUtil.isMaterialiseOnCooldown(tardis)) {
			return;
		}

		// Check if the Tardis materialization is prevented by event listeners
		if (!ignoreChecks && TardisEvents.MAT.invoker().onMat(tardis)) {
			failToMaterialise();
			return;
		}

		// Lock the Tardis doors
		// DoorData.lockTardis(true, this.getTardis().get(), null, true);

		// Set the Tardis state to materialise
		this.setState(State.MAT);

        /*if(this.findTardis().get().getHandlers().getSequenceHandler().isConsoleDisabled()) {
            this.findTardis().get().getHandlers().getSequenceHandler().disableConsole(false);
        }*/

		SequenceHandler sequences = tardis.getHandlers().getSequenceHandler();

		if (sequences.hasActiveSequence()) {
			sequences.setActiveSequence(null, true);
		}

		// Get the server world of the destination
		ServerWorld destWorld = (ServerWorld) this.getDestination().getWorld();

		// Play materialize sound at the destination
		this.getDestination().getWorld().playSound(null, this.getDestination(), this.getSoundForCurrentState(), SoundCategory.BLOCKS, 1f, 1f);

		FlightUtil.playSoundAtConsole(tardis, this.getSoundForCurrentState(), SoundCategory.BLOCKS, 1f, 1f);

		// Set the destination block to the Tardis exterior block
		ExteriorBlock block = (ExteriorBlock) AITBlocks.EXTERIOR_BLOCK;
		BlockState state = block.getDefaultState().with(Properties.HORIZONTAL_FACING, this.getDestination().getDirection());
		destWorld.setBlockState(this.getDestination(), state);

		// Create and add the exterior block entity at the destination
		ExteriorBlockEntity blockEntity = new ExteriorBlockEntity(this.getDestination(), state);
		destWorld.addBlockEntity(blockEntity);

		// Set the position of the Tardis to the destination
		this.setPosition(this.getDestination());

		// Run animations on the block entity
		this.runAnimations(blockEntity);

		this.onMaterialise(tardis);
	}

	private void onMaterialise(ServerTardis tardis) {
		if (tardis.isGrowth()) {
			TardisExterior exterior = tardis.getExterior();

			exterior.setType(CategoryRegistry.CAPSULE);
			tardis.getDoor().closeDoors();
		}
	}

	public void dematerialise(boolean withRemat, boolean ignoreChecks) {
		if (this.getState() != State.LANDED) return;
		if (findTardis().isEmpty()) return;

		if (!findTardis().get().hasPower()) {
			return; // no flying for you if you have no powa :)
		}

		if (this.getPosition().getWorld().isClient())
			return;

		if (FlightUtil.isDematerialiseOnCooldown(findTardis().get()))
			return; // cancelled


		if (PropertiesHandler.willAutoPilot(findTardis().get().getHandlers().getProperties())) {
			// fulfill all the prerequisites
			// DoorData.lockTardis(true, tardis(), null, false);
			PropertiesHandler.set(findTardis().get(), PropertiesHandler.HANDBRAKE, false);
			this.findTardis().get().getDoor().closeDoors();
			findTardis().get().setRefueling(false);
			if (this.getSpeed() == 0) this.increaseSpeed();
		}

		PropertiesHandler.setAutoPilot(this.findTardis().get().getHandlers().getProperties(), withRemat);

		ServerWorld world = (ServerWorld) this.getPosition().getWorld();

		if (!ignoreChecks && TardisEvents.DEMAT.invoker().onDemat(findTardis().get())) {
			failToTakeoff();
			return;
		}

		// DoorData.lockTardis(true, this.getTardis().get(), null, true);

		this.setState(State.DEMAT);

		world.playSound(null, this.getPosition(), this.getSoundForCurrentState(), SoundCategory.BLOCKS);
		if (this.findTardis().isPresent())
			FlightUtil.playSoundAtConsole(this.findTardis().get(), this.getSoundForCurrentState(), SoundCategory.BLOCKS, 10f, 1f);

		this.runAnimations();
	}

	public void dematerialise(boolean withRemat) {
		this.dematerialise(withRemat, false);
	}

	public void dematerialise() {
		this.dematerialise(false);
	}

	private void failToMaterialise() {

		if (findTardis().isEmpty()) return;

		// Play failure sound at the current position
		this.getPosition().getWorld().playSound(null, this.getPosition(), AITSounds.FAIL_MAT, SoundCategory.BLOCKS, 1f, 1f);

		// Play failure sound at the Tardis console position if the interior is not empty
		FlightUtil.playSoundAtConsole(this.findTardis().get(), AITSounds.FAIL_MAT, SoundCategory.BLOCKS, 1f, 1f);

		// Send error message to the pilot
		// TardisUtil.sendMessageToPilot(this.getTardis().get(), Text.literal("Unable to land!"));

		// Create materialization delay and return
		FlightUtil.createMaterialiseDelay(this.findTardis().get());
	}

	private void failToTakeoff() {
		// dont do anything if out of fuel, make it sad :(
		if (findTardis().isEmpty() || !findTardis().get().hasPower()) return;

		// demat will be cancelled
		this.getPosition().getWorld().playSound(null, this.getPosition(), AITSounds.FAIL_DEMAT, SoundCategory.BLOCKS, 1f, 1f); // fixme can be spammed

		if (TardisUtil.isInteriorNotEmpty(findTardis().get()))
			FlightUtil.playSoundAtConsole(this.findTardis().get(), AITSounds.FAIL_DEMAT, SoundCategory.BLOCKS, 1f, 1f);

		// TardisUtil.sendMessageToPilot(this.getTardis().get(), Text.literal("Unable to takeoff!")); // fixme translatable
		FlightUtil.createDematerialiseDelay(this.findTardis().get());
	}

	/**
	 * Checks whether the destination is valid otherwise searches for a new one
	 *
	 * @param limit     how many times the search can happen (should stop hanging)
	 * @param fullCheck whether to search downwards or upwards
	 * @return whether its safe to land
	 */
	private boolean checkDestination(int limit, boolean fullCheck) {
		if (TardisUtil.isClient()) return true;

		ServerWorld world = (ServerWorld) this.getDestination().getWorld(); // this cast is fine, we know its server

		if (isDestinationTardisExterior()) {
			ExteriorBlockEntity target = (ExteriorBlockEntity) world.getBlockEntity(this.getDestination()); // safe

			if (findTardis().isEmpty() || target.findTardis().isEmpty() ||
					findTardis().get().getUuid().equals(target.findTardis().get().getUuid())) return false;

			setDestinationToTardisInterior(target.findTardis().get(), true, 256); // how many times should this be

			return this.checkDestination(CHECK_LIMIT, PropertiesHandler.getBool(findTardis().get().getHandlers().getProperties(), PropertiesHandler.FIND_GROUND)); // limit at a small number cus it might get too laggy
		}

		// is long line
		setDestination(new AbsoluteBlockPos.Directed(
						getDestination().getX(),
						MathHelper.clamp(getDestination().getY(), world.getBottomY(), world.getTopY() - 1),
						getDestination().getZ(),
						getDestination().getWorld(),
						getDestination().getDirection()),
				false
		);

		BlockPos.Mutable temp = this.getDestination().mutableCopy(); // loqor told me mutables were better, is this true? fixme if not

		BlockState current;
		BlockState top;
		BlockState ground;

		if (fullCheck) {
			for (int i = 0; i < limit; i++) {
				current = world.getBlockState(temp);
				top = world.getBlockState(temp.up());
				ground = world.getBlockState(temp.down());

				if (isReplaceable(current, top) && !isReplaceable(ground)) { // check two blocks cus tardis is two blocks tall yk and check for groud
					this.setDestination(new AbsoluteBlockPos.Directed(temp, world, this.getDestination().getDirection()), false);
					return true;
				}

				temp = temp.down().mutableCopy();
			}

			temp = this.getDestination().mutableCopy();

			for (int i = 0; i < limit; i++) {
				current = world.getBlockState(temp);
				top = world.getBlockState(temp.up());
				ground = world.getBlockState(temp.down());

				if (isReplaceable(current, top) && !isReplaceable(ground)) { // check two blocks cus tardis is two blocks tall yk and check for groud
					this.setDestination(new AbsoluteBlockPos.Directed(temp, world, this.getDestination().getDirection()), false);
					return true;
				}

				temp = temp.up().mutableCopy();
			}
		}

		temp = this.getDestination().mutableCopy();

		current = world.getBlockState(temp);
		top = world.getBlockState(temp.up());

		return isReplaceable(current, top);
	}

	private static boolean isReplaceable(BlockState... states) {
		for (BlockState state1 : states) {
			if (!state1.isReplaceable()) {
				return false;
			}
		}
		return true;
	}

	private static boolean isLiquid(BlockState... states) {
		for (BlockState state1 : states) {
			if (!state1.isSolid() || state1.isLiquid()) {
				return true;
			}
		}
		return false;
	}

	public boolean checkDestination() {
		if (this.findTardis().isEmpty()) return false;
		return this.checkDestination(CHECK_LIMIT, PropertiesHandler.getBool(this.findTardis().get().getHandlers().getProperties(), PropertiesHandler.FIND_GROUND));
	}

	private boolean isDestinationTardisExterior() {
		ServerWorld world = (ServerWorld) this.getDestination().getWorld();

		// bad code but its 4am i cba anymore
		if (world.getBlockEntity(this.getDestination()) instanceof ExteriorBlockEntity) {
			return true;
		}

		if (world.getBlockEntity(this.getDestination().down()) instanceof ExteriorBlockEntity) {
			this.setDestination(new AbsoluteBlockPos.Directed(this.getDestination().down(), world, this.getDestination().getDirection()), false);
			return true;
		}

		return false;
	}

	public static boolean isTardisExterior(AbsoluteBlockPos pos) {
		ServerWorld world = (ServerWorld) pos.getWorld();

		// bad code but its 4am i cba anymore
		if (world.getBlockEntity(pos) instanceof ExteriorBlockEntity) {
			return true;
		}

		return world.getBlockEntity(pos.down()) instanceof ExteriorBlockEntity;
	}

	/**
	 * Picks a random pos within the placed tardis interior and sets the destination
	 *
	 * @param target tardis to land in
	 * @param checks whether to run usual landing checks
	 * @param limit  how many times to check / rerun this
	 */
	private void setDestinationToTardisInterior(Tardis target, boolean checks, int limit) { // fixme as this causes problems sometimes
		if (target == null) return; // i hate null shit

		Random random = new Random();
		BlockPos h = TardisUtil.getPlacedInteriorCentre(target); // bad variable name
		h = PosType.X.add(h, random.nextBoolean() ? -random.nextInt(8) : random.nextInt(8));
		h = PosType.Z.add(h, random.nextBoolean() ? -random.nextInt(8) : random.nextInt(8));

		this.setDestination(new AbsoluteBlockPos.Directed(
						h,
						TardisUtil.getTardisDimension(),
						this.getDestination().getDirection()),
				checks
		);
	}

	public void toFlight() {
		this.setCrashing(false);
		this.setLastPosition(this.getPosition());
		this.setState(TardisTravel.State.FLIGHT);
		this.deleteExterior();

		if (this.findTardis().isEmpty()) return;
		boolean security = PropertiesHandler.getBool(this.findTardis().get().getHandlers().getProperties(), SecurityControl.SECURITY_KEY);
		if (security) {
			SecurityControl.runSecurityProtocols(this.findTardis().get());
		}
	}

	public void forceLand(@Nullable ExteriorBlockEntity blockEntity) {
		if (this.findTardis().isEmpty())
			return;

		if (PropertiesHandler.willAutoPilot(this.findTardis().get().getHandlers().getProperties())) {
			if (this.getSpeed() > 0) {
				this.setSpeed(0);
			}
		}

		this.setState(TardisTravel.State.LANDED);

		if (blockEntity == null) {
			ServerWorld world = (ServerWorld) this.getPosition().getWorld();
			BlockEntity found = world.getBlockEntity(this.getPosition());


			// If there is already a matching exterior at this position
			if (found instanceof ExteriorBlockEntity exterior
					&& exterior.findTardis().isPresent()
					&& Objects.equals(exterior.findTardis().get(), this.findTardis().get())) {
				blockEntity = exterior;
			} else {
				ExteriorBlock block = (ExteriorBlock) AITBlocks.EXTERIOR_BLOCK;
				BlockState state = block.getDefaultState().with(Properties.HORIZONTAL_FACING, this.getPosition().getDirection());
				world.setBlockState(this.getPosition(), state);

				ExteriorBlockEntity newEntity = new ExteriorBlockEntity(this.getPosition(), state);
				world.addBlockEntity(newEntity);
				newEntity.setTardis(this.findTardis().get());
				blockEntity = newEntity;
			}

		}

		this.runAnimations(blockEntity);

		if (DoorData.isClient()) return;
		DoorData.lockTardis(PropertiesHandler.getBool(this.findTardis().get().getHandlers().getProperties(), PropertiesHandler.PREVIOUSLY_LOCKED), this.findTardis().get(), null, false);

		TardisEvents.LANDED.invoker().onLanded(findTardis().get());
	}

	public void setCrashing(boolean crashing) {
		this.crashing = crashing;
		this.sync();
	}

	public void forceLand() {
		this.forceLand(null);
	}

	public void runAnimations() {
		ServerWorld level = (ServerWorld) this.getPosition().getWorld();
		BlockEntity entity = level.getBlockEntity(this.getPosition());
		if (entity instanceof ExteriorBlockEntity exterior) {
			if (exterior.getAnimation() == null) return;

			exterior.getAnimation().setupAnimation(this.state);
			exterior.getAnimation().tellClientsToSetup(this.state);
		}
	}

	public void runAnimations(ExteriorBlockEntity exterior) {
		if (exterior.getAnimation() == null) return;

		exterior.getAnimation().setupAnimation(this.state);
		exterior.getAnimation().tellClientsToSetup(this.state);
	}

	public void setDestination(AbsoluteBlockPos.Directed pos, boolean withChecks) {
		if (this.findTardis().isEmpty()) return;

		if (Objects.equals(this.destination, pos)) return;

		WorldBorder border = this.destination.getWorld().getWorldBorder();

		this.destination = border.contains(this.destination)
						? pos : new AbsoluteBlockPos.Directed(border.clamp(pos.getX(), pos.getY(), pos.getZ()),
						pos.getDimension(), pos.getDirection());
		this.findTardis().get().getHandlers().getFlight().recalculate();

		if (withChecks)
			this.checkDestination(CHECK_LIMIT, PropertiesHandler.getBool(this.findTardis().get().getHandlers().getProperties(), PropertiesHandler.FIND_GROUND));
		this.sync();
	}

	public void setDestination(AbsoluteBlockPos.Directed pos) {
		this.setDestination(pos, true);
	}

	/**
	 * Sets the position of the tardis based off the flight's progress to the destination
	 *
	 * @param source Where this tardis originally took off from
	 */
	public void setPositionToProgress(AbsoluteBlockPos.Directed source) {
		if (this.getState() != State.FLIGHT) return;
		if (this.findTardis().isEmpty()) return;

		AbsoluteBlockPos.Directed pos = FlightUtil.getPositionFromPercentage(
				source,
				this.getDestination(),
				this.findTardis().get().getHandlers().getFlight().getDurationAsPercentage()
		);

		this.setPosition(pos);
	}

	public void setPositionToProgress() {
		this.setPositionToProgress(this.getPosition());
	}

	public AbsoluteBlockPos.Directed getDestination() {
		if (this.destination == null) {
			if (this.getPosition() != null)
				this.destination = this.getPosition();
			else {
				AITMod.LOGGER.error("Destination error! resetting to 0 0 0 in overworld");
				this.destination = new AbsoluteBlockPos.Directed(0, 0, 0, TardisUtil.findWorld(World.OVERWORLD), Direction.NORTH);
			}
		}

		return destination;
	}


	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
		this.sync();
	}

	public void placeExterior() {

		if (this.findTardis().isEmpty()) return;

		this.position.setBlockState(AITBlocks.EXTERIOR_BLOCK.getDefaultState().with(ExteriorBlock.FACING, this.position.getDirection()));
		ExteriorBlockEntity exterior = new ExteriorBlockEntity(
				this.position, this.position.getBlockState()
		);
		exterior.setTardis(this.findTardis().get());
		this.position.addBlockEntity(exterior);
	}

	public void deleteExterior() {
		this.getPosition().getWorld().removeBlock(this.getPosition(), false);
		if (TardisUtil.isServer()) {
			ForcedChunkUtil.stopForceLoading((ServerWorld) this.getPosition().getWorld(), this.getPosition());
		}

	}

	@NotNull
	public SoundEvent getSoundForCurrentState() {
		if (this.findTardis().isPresent()) {
			if (this.isCrashing()) {
				return AITSounds.GHOST_MAT;
			}
			return this.findTardis().get().getExterior().getVariant().getSound(this.getState()).sound();
		}
		return SoundEvents.INTENTIONALLY_EMPTY;
	}

	public MatSound getMatSoundForCurrentState() {
		if (this.findTardis().isPresent()) {
			if (this.isCrashing()) {
				return AITSounds.GHOST_MAT_ANIM;
			}
			return this.findTardis().get().getExterior().getVariant().getSound(this.getState());
		}
		return AITSounds.LANDED_ANIM;
	}

	public enum State {
		LANDED(true) {
			@Override
			public void onEnable(TravelContext context) {
				AITMod.LOGGER.info("ON: LANDED");
			}

			@Override
			public void onDisable(TravelContext context) {
				AITMod.LOGGER.info("OFF: LANDED");
			}

			@Override
			public State getNext() {
				return DEMAT;
			}
		},
		DEMAT {
			@Override
			public void onEnable(TravelContext context) {
				AITMod.LOGGER.info("ON: DEMAT");
			}

			@Override
			public void onDisable(TravelContext context) {
				AITMod.LOGGER.info("OFF: DEMAT");
			}

			@Override
			public State getNext() {
				return FLIGHT;
			}
		},
		FLIGHT(true) {
			@Override
			public void onEnable(TravelContext context) {
				AITMod.LOGGER.info("ON: FLIGHT");
			}

			@Override
			public void onDisable(TravelContext context) {
				AITMod.LOGGER.info("OFF: LANDED");
			}

			@Override
			public State getNext() {
				return MAT;
			}
		},
		MAT {
			@Override
			public void onEnable(TravelContext context) {
				AITMod.LOGGER.info("ON: MAT");
			}

			@Override
			public void onDisable(TravelContext context) {
				AITMod.LOGGER.info("OFF: LANDED");
			}

			@Override
			public State getNext() {
				return LANDED;
			}
		},
		CRASH(true) {
			@Override
			public void onEnable(TravelContext context) {
				AITMod.LOGGER.info("ON: CRASH");
			}

			@Override
			public void onDisable(TravelContext context) {
				AITMod.LOGGER.info("OFF: LANDED");
			}

			@Override
			public State getNext() {
				return LANDED;
			}
		};
		private final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		private final boolean isStatic;

		State(boolean isStatic) {
			this.isStatic = isStatic;
		}

		State() {
			this(false);
		}

		public boolean isStatic() {
			return isStatic;
		}

		public abstract void onEnable(TravelContext context);

		public abstract void onDisable(TravelContext context);

		public abstract State getNext();

		public void next(TravelContext context) {
			this.service.shutdown();
			this.onDisable(context);

			State next = this.getNext();
			next.schedule(context);

			next.onEnable(context);
			context.travel().setState(next);
		}

		public void schedule(TravelContext context) {
		}
	}
}
