package mdteam.ait.registry;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.control.impl.*;
import mdteam.ait.tardis.control.impl.pos.IncrementControl;
import mdteam.ait.tardis.control.impl.pos.XControl;
import mdteam.ait.tardis.control.impl.pos.YControl;
import mdteam.ait.tardis.control.impl.pos.ZControl;
import mdteam.ait.tardis.control.impl.waypoint.SetWaypointControl;
import mdteam.ait.tardis.control.sequences.Sequence;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.util.TardisUtil;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.Items;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.ArrayList;
import java.util.List;

public class SequenceRegistry {
	public static final SimpleRegistry<Sequence> REGISTRY = FabricRegistryBuilder.createSimple(RegistryKey.<Sequence>ofRegistry(new Identifier(AITMod.MOD_ID, "sequence"))).buildAndRegister();
	private static final Random random = Random.create();

	public static Sequence register(Sequence schema) {
		return Registry.register(REGISTRY, schema.id(), schema);
	}

	public static Sequence AVOID_DEBRIS;
	public static Sequence DIMENSIONAL_BREACH;
	public static Sequence ENERGY_DRAIN;
	public static Sequence DIMENSIONAL_DRIFT_X;
	public static Sequence DIMENSIONAL_DRIFT_Y;
	public static Sequence DIMENSIONAL_DRIFT_Z;
	//public static Sequence FORCED_MAT;
	public static Sequence CLOAK_TO_AVOID_VORTEX_TRAPPED_MOBS;
	public static Sequence ANTI_GRAVITY_ERROR;
	public static Sequence POWER_DRAIN_IMMINENT;
	//public static Sequence VORTEX_COLLISION;
	public static Sequence DIRECTIONAL_ERROR;
	//public static Sequence RANDOM_LOCATION_IDENTIFIED;
	public static Sequence SPEED_UP_TO_AVOID_DRIFTING_OUT_OF_VORTEX;
	public static Sequence COURSE_CORRECT;
	public static Sequence GROUND_UNSTABLE;
	public static Sequence INCREMENT_SCALE_RECALCULATION_NECESSARY;

	// TODO flight staging
	public static Sequence TAKE_OFF;
	public static Sequence ENTER_VORTEX;
	public static Sequence EXIT_VORTEX;
	public static Sequence LANDING;


	public static void init() {

        /*SEQUENCE = register(Sequence.Builder.create(new Identifier(AITMod.MOD_ID, SOME_SEQUENCE), (finishedTardis -> {
        }), (missedTardis -> {
        }), 100L, Text.literal("<> detected!").formatting(Formatted.ITALIC, Formatted.YELLOW), new Control()),*/

		AVOID_DEBRIS = register(Sequence.Builder.create(new Identifier(AITMod.MOD_ID, "avoid_debris"), (finishedTardis -> {
					finishedTardis.getHandlers().getFlight().decreaseFlightTime(30);
				}), (missedTardis -> {
					missedTardis.removeFuel(-random.nextBetween(45, 125));
					missedTardis.getDoor().openDoors();
					List<Explosion> explosions = new ArrayList<>();
					missedTardis.getDesktop().getConsoles().forEach(console -> {
						Explosion explosion = TardisUtil.getTardisDimension().createExplosion(
								null,
								null,
								null,
								console.position().toCenterPos(),
								3f * 2,
								false,
								World.ExplosionSourceType.MOB
						);
						explosions.add(explosion);
					});
					java.util.Random random = new java.util.Random();
					for (ServerPlayerEntity player : TardisUtil.getPlayersInInterior(missedTardis)) {
						float random_X_velocity = random.nextFloat(-2f, 3f);
						float random_Y_velocity = random.nextFloat(-1f, 2f);
						float random_Z_velocity = random.nextFloat(-2f, 3f);
						player.setVelocity(random_X_velocity * 2, random_Y_velocity * 2, random_Z_velocity * 2);
						if (!explosions.isEmpty()) {
							player.damage(TardisUtil.getTardisDimension().getDamageSources().explosion(explosions.get(0)), 0);
						} else {
							player.damage(null, 0);
						}
					}
					//missedTardis.getTravel().crash();
				}), 100L, Text.literal("Debris incoming!").formatted(Formatting.ITALIC, Formatting.YELLOW),
				new DirectionControl(), new RandomiserControl()));

		DIMENSIONAL_BREACH = register(Sequence.Builder.create(new Identifier(AITMod.MOD_ID, "dimensional_breach"), (finishedTardis -> {
					finishedTardis.getHandlers().getFlight().decreaseFlightTime(30);
				}), (missedTardis -> {
					missedTardis.getDoor().openDoors();
				}), 80L, Text.literal("DIMENSION BREACH: SECURE DOORS").formatted(Formatting.ITALIC, Formatting.YELLOW),
				new DimensionControl(), new DoorControl()));

		ENERGY_DRAIN = register(Sequence.Builder.create(new Identifier(AITMod.MOD_ID, "energy_drain"), (finishedTardis -> {
					finishedTardis.getHandlers().getFlight().decreaseFlightTime(30);
					finishedTardis.addFuel(random.nextBetween(45, 125));
				}), (missedTardis -> {
					missedTardis.removeFuel(random.nextBetween(45, 125));
				}), 80L, Text.literal("Artron drain detected!").formatted(Formatting.ITALIC, Formatting.YELLOW),
				new RefuelerControl()));

		POWER_DRAIN_IMMINENT = register(Sequence.Builder.create(new Identifier(AITMod.MOD_ID, "power_drain_imminent"), (finishedTardis -> {
					finishedTardis.getHandlers().getFlight().decreaseFlightTime(120);
					finishedTardis.addFuel(random.nextBetween(45, 125));
				}), (missedTardis -> {
					missedTardis.removeFuel(random.nextBetween(45, 125));
					missedTardis.disablePower();
				}), 110L, Text.literal("Power drain imminent!").formatted(Formatting.ITALIC, Formatting.YELLOW),
				new PowerControl(), new RefuelerControl(), new RandomiserControl()));

		ANTI_GRAVITY_ERROR = register(Sequence.Builder.create(new Identifier(AITMod.MOD_ID, "anti_gravity_error"), (finishedTardis -> {
					finishedTardis.getHandlers().getFlight().decreaseFlightTime(30);
				}), (missedTardis -> {
					missedTardis.removeFuel(random.nextBetween(45, 125));
					PropertiesHandler.set(missedTardis, PropertiesHandler.ANTIGRAVS_ENABLED, false);
				}), 80L, Text.literal("Gravity miscalculation!").formatted(Formatting.ITALIC, Formatting.YELLOW),
				new AntiGravsControl()));

		DIMENSIONAL_DRIFT_X = register(Sequence.Builder.create(new Identifier(AITMod.MOD_ID, "dimensional_drift_x"), (finishedTardis -> {
					finishedTardis.getHandlers().getFlight().decreaseFlightTime(30);
				}), (missedTardis -> {
					AbsoluteBlockPos.Directed pos = missedTardis.destination();
					missedTardis.getTravel().setDestination(new AbsoluteBlockPos.Directed(
							random.nextBetween(pos.getX() - 8, pos.getX() + 8),
							pos.getY(),
							random.nextBetween(pos.getZ() - 8, pos.getZ() + 8), pos.getWorld(),
							pos.getDirection()));
				}), 100L, Text.literal("Drifting off course X!").formatted(Formatting.ITALIC, Formatting.YELLOW),
				new DimensionControl(), new XControl()));

		DIMENSIONAL_DRIFT_Y = register(Sequence.Builder.create(new Identifier(AITMod.MOD_ID, "dimensional_drift_y"), (finishedTardis -> {
					finishedTardis.getHandlers().getFlight().decreaseFlightTime(30);
				}), (missedTardis -> {
					AbsoluteBlockPos.Directed pos = missedTardis.destination();
					missedTardis.getTravel().setDestination(new AbsoluteBlockPos.Directed(
							random.nextBetween(pos.getX() - 8, pos.getX() + 8),
							pos.getY(),
							random.nextBetween(pos.getZ() - 8, pos.getZ() + 8), pos.getWorld(),
							pos.getDirection()));
				}), 100L, Text.literal("Drifting off course Y!").formatted(Formatting.ITALIC, Formatting.YELLOW),
				new DimensionControl(), new YControl()));

		DIMENSIONAL_DRIFT_Z = register(Sequence.Builder.create(new Identifier(AITMod.MOD_ID, "dimensional_drift_z"), (finishedTardis -> {
					finishedTardis.getHandlers().getFlight().decreaseFlightTime(30);
				}), (missedTardis -> {
					AbsoluteBlockPos.Directed pos = missedTardis.destination();
					missedTardis.getTravel().setDestination(new AbsoluteBlockPos.Directed(
							random.nextBetween(pos.getX() - 8, pos.getX() + 8),
							pos.getY(),
							random.nextBetween(pos.getZ() - 8, pos.getZ() + 8), pos.getWorld(),
							pos.getDirection()));
				}), 100L, Text.literal("Drifting off course Z!").formatted(Formatting.ITALIC, Formatting.YELLOW),
				new DimensionControl(), new ZControl()));

		CLOAK_TO_AVOID_VORTEX_TRAPPED_MOBS = register(Sequence.Builder.create(new Identifier(AITMod.MOD_ID, "cloak_to_avoid_vortex_trapped_mobs"), (finishedTardis -> {
					finishedTardis.getHandlers().getFlight().decreaseFlightTime(30);
					if (finishedTardis.getDoor().getDoorPos() == null) return;
					if (finishedTardis.getDoor().isOpen() || TardisUtil.getTardisDimension().isClient()) return;
					ItemEntity rewardForCloaking = new ItemEntity(EntityType.ITEM, TardisUtil.getTardisDimension());
					rewardForCloaking.setPosition(finishedTardis.getDoor().getDoorPos().getX() + 0.5f,
							finishedTardis.getDoor().getDoorPos().getY() + 0.5f, finishedTardis.getDoor().getDoorPos().getZ() + 0.5f);
					rewardForCloaking.setStack(random.nextBoolean() ? Items.GOLD_NUGGET.getDefaultStack() : Items.POPPY.getDefaultStack());
					TardisUtil.getTardisDimension().spawnEntity(rewardForCloaking);
				}), (missedTardis -> {
					missedTardis.getHandlers().getFlight().increaseFlightTime(30);
					if (missedTardis.getDoor().getDoorPos() == null) return;
					if (missedTardis.getDoor().isOpen() || TardisUtil.getTardisDimension().isClient()) return;
					ZombieEntity zombieEntity = new ZombieEntity(EntityType.ZOMBIE, TardisUtil.getTardisDimension());
					zombieEntity.setPosition(missedTardis.getDoor().getDoorPos().getX() + 0.5f,
							missedTardis.getDoor().getDoorPos().getY() + 0.5f, missedTardis.getDoor().getDoorPos().getZ() + 0.5f);
					DrownedEntity drownedEntity = new DrownedEntity(EntityType.DROWNED, TardisUtil.getTardisDimension());
					drownedEntity.setPosition(missedTardis.getDoor().getDoorPos().getX() + 0.5f,
							missedTardis.getDoor().getDoorPos().getY() + 0.5f, missedTardis.getDoor().getDoorPos().getZ() + 0.5f);
					PhantomEntity phantomEntity = new PhantomEntity(EntityType.PHANTOM, TardisUtil.getTardisDimension());
					phantomEntity.setPosition(missedTardis.getDoor().getDoorPos().getX() + 0.5f,
							missedTardis.getDoor().getDoorPos().getY() + 0.5f, missedTardis.getDoor().getDoorPos().getZ() + 0.5f);
					TardisUtil.getTardisDimension().spawnEntity(random.nextBoolean() ? random.nextBoolean() ? drownedEntity : zombieEntity : phantomEntity);
				}), 80L, Text.literal("Immediate cloaking necessary!").formatted(Formatting.ITALIC, Formatting.YELLOW),
				new CloakControl(), new RandomiserControl()));

        /*FORCED_MAT = register(Sequence.Builder.create(new Identifier(AITMod.MOD_ID, "forced_materialisation"), (finishedTardis -> {
                    AbsoluteBlockPos.Directed pos = finishedTardis.destination();
                    if(finishedTardis.getTravel().isMaterialising() && PropertiesHandler.getBool(finishedTardis.getHandlers().getProperties(), SecurityControl.SECURITY_KEY)) {
                        finishedTardis.getTravel().setDestination(new AbsoluteBlockPos.Directed(
                                random.nextBetween(-pos.getX() - 50, pos.getX() + 50),
                                pos.getY(),
                                random.nextBetween(-pos.getZ() - 50, pos.getZ() + 50),
                                pos.getWorld(), pos.getDirection()));
                        finishedTardis.getTravel().setState(TardisTravel.State.FLIGHT);
                    }
                }), (missedTardis -> {
                    missedTardis.getHandlers().getAlarms().enable();
                }),  80L, Text.literal("Materialisation forced!").formatted(Formatting.ITALIC, Formatting.YELLOW),
                new DirectionControl(), new XControl(), new ZControl()));*/

		DIRECTIONAL_ERROR = register(Sequence.Builder.create(new Identifier(AITMod.MOD_ID, "directional_error"), (finishedTardis -> {
					finishedTardis.getHandlers().getFlight().decreaseFlightTime(30);
				}), (missedTardis -> {
					missedTardis.getHandlers().getFlight().increaseFlightTime(30);
				}), 80L, Text.literal("Directional error!").formatted(Formatting.ITALIC, Formatting.YELLOW),
				new DirectionControl()));

		SPEED_UP_TO_AVOID_DRIFTING_OUT_OF_VORTEX = register(Sequence.Builder.create(new Identifier(AITMod.MOD_ID, "speed_up_to_avoid_drifting_out_of_vortex"), (finishedTardis -> {
					finishedTardis.getHandlers().getFlight().decreaseFlightTime(60);
				}), (missedTardis -> {
					missedTardis.removeFuel(random.nextBetween(45, 125));
				}), 80L, Text.literal("Vortex drift: acceleration necessary!").formatted(Formatting.ITALIC, Formatting.YELLOW),
				new IncrementControl(), new ThrottleControl()));

		COURSE_CORRECT = register(Sequence.Builder.create(new Identifier(AITMod.MOD_ID, "course_correct"), (finishedTardis -> {
					finishedTardis.getHandlers().getFlight().decreaseFlightTime(60);
				}), (missedTardis -> {
					missedTardis.removeFuel(random.nextBetween(65, 250));
					AbsoluteBlockPos.Directed pos = missedTardis.destination();
					missedTardis.getTravel().setDestination(new AbsoluteBlockPos.Directed(
							random.nextBetween(pos.getX() - 24, pos.getX() + 24),
							pos.getY(),
							random.nextBetween(pos.getZ() - 24, pos.getZ() + 24), pos.getWorld(),
							pos.getDirection()));
				}), 110L, Text.literal("TARDIS off course!").formatted(Formatting.ITALIC, Formatting.YELLOW),
				new HandBrakeControl(), new ThrottleControl(), new RandomiserControl()));

		GROUND_UNSTABLE = register(Sequence.Builder.create(new Identifier(AITMod.MOD_ID, "ground_unstable"), (finishedTardis -> {
					finishedTardis.getHandlers().getFlight().decreaseFlightTime(30);
					finishedTardis.addFuel(random.nextBetween(45, 125));
				}), (missedTardis -> {
					missedTardis.removeFuel(random.nextBetween(45, 125));
				}), 110L, Text.literal("Unstable landing position!").formatted(Formatting.ITALIC, Formatting.YELLOW),
				new LandTypeControl(), new YControl(), new SetWaypointControl()));

		INCREMENT_SCALE_RECALCULATION_NECESSARY = register(Sequence.Builder.create(new Identifier(AITMod.MOD_ID, "increment_scale_recalculation_necessary"), (finishedTardis -> {
					finishedTardis.getHandlers().getFlight().decreaseFlightTime(30);
					finishedTardis.addFuel(random.nextBetween(45, 125));
				}), (missedTardis -> {
					missedTardis.removeFuel(random.nextBetween(45, 125));
				}), 80L, Text.literal("Increment scale error! Recalculation necessary!").formatted(Formatting.ITALIC, Formatting.YELLOW),
				new IncrementControl()));

		TAKE_OFF = register(Sequence.Builder.create(new Identifier(AITMod.MOD_ID, "take_off"), (finishedTardis -> {
					finishedTardis.getHandlers().getFlight().decreaseFlightTime(30);
					finishedTardis.addFuel(random.nextBetween(45, 125));
				}), (missedTardis -> {
					missedTardis.removeFuel(random.nextBetween(45, 125));
				}), 80L, Text.literal("Takeoff sequence initiated...").formatted(Formatting.ITALIC, Formatting.YELLOW),
				new IncrementControl()));
		ENTER_VORTEX = register(Sequence.Builder.create(new Identifier(AITMod.MOD_ID, "enter_vortex"), (finishedTardis -> {
					finishedTardis.getHandlers().getFlight().decreaseFlightTime(30);
					finishedTardis.addFuel(random.nextBetween(45, 125));
				}), (missedTardis -> {
					missedTardis.removeFuel(random.nextBetween(45, 125));
				}), 80L, Text.literal("Vortex entrance found!").formatted(Formatting.ITALIC, Formatting.YELLOW),
				new IncrementControl()));
		EXIT_VORTEX = register(Sequence.Builder.create(new Identifier(AITMod.MOD_ID, "exit_vortex"), (finishedTardis -> {
					finishedTardis.getHandlers().getFlight().decreaseFlightTime(30);
					finishedTardis.addFuel(random.nextBetween(45, 125));
				}), (missedTardis -> {
					missedTardis.removeFuel(random.nextBetween(45, 125));
				}), 80L, Text.literal("Vortex exit found!").formatted(Formatting.ITALIC, Formatting.YELLOW),
				new IncrementControl()));
		LANDING = register(Sequence.Builder.create(new Identifier(AITMod.MOD_ID, "landing"), (finishedTardis -> {
					finishedTardis.getHandlers().getFlight().decreaseFlightTime(30);
					finishedTardis.addFuel(random.nextBetween(45, 125));
				}), (missedTardis -> {
					missedTardis.removeFuel(random.nextBetween(45, 125));
				}), 80L, Text.literal("Landing sequence initiated...").formatted(Formatting.ITALIC, Formatting.YELLOW),
				new IncrementControl()));
	}
}
