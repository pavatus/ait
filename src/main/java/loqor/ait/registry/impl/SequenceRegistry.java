package loqor.ait.registry.impl;

import java.util.ArrayList;
import java.util.List;

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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import loqor.ait.AITMod;
import loqor.ait.core.data.DirectedBlockPos;
import loqor.ait.core.util.WorldUtil;
import loqor.ait.tardis.control.impl.*;
import loqor.ait.tardis.control.impl.pos.IncrementControl;
import loqor.ait.tardis.control.impl.pos.XControl;
import loqor.ait.tardis.control.impl.pos.YControl;
import loqor.ait.tardis.control.impl.pos.ZControl;
import loqor.ait.tardis.control.impl.waypoint.SetWaypointControl;
import loqor.ait.tardis.control.sequences.Sequence;
import loqor.ait.tardis.util.TardisUtil;

public class SequenceRegistry {
    public static final SimpleRegistry<Sequence> REGISTRY = FabricRegistryBuilder
            .createSimple(RegistryKey.<Sequence>ofRegistry(new Identifier(AITMod.MOD_ID, "sequence")))
            .buildAndRegister();
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
    // public static Sequence FORCED_MAT;
    public static Sequence CLOAK_TO_AVOID_VORTEX_TRAPPED_MOBS;
    public static Sequence ANTI_GRAVITY_ERROR;
    public static Sequence POWER_DRAIN_IMMINENT;
    public static Sequence SHIP_COMPUTER_OFFLINE;
    // public static Sequence VORTEX_COLLISION;
    public static Sequence DIRECTIONAL_ERROR;
    // public static Sequence RANDOM_LOCATION_IDENTIFIED;
    public static Sequence SPEED_UP_TO_AVOID_DRIFTING_OUT_OF_VORTEX;
    public static Sequence COURSE_CORRECT;
    public static Sequence GROUND_UNSTABLE;
    public static Sequence INCREMENT_SCALE_RECALCULATION_NECESSARY;
    public static Sequence SMALL_DEBRIS_FIELD;

    public static void init() {
        AVOID_DEBRIS = register(Sequence.Builder.create(new Identifier(AITMod.MOD_ID, "avoid_debris"),
                finishedTardis -> finishedTardis.travel().decreaseFlightTime(120), missedTardis -> {
                    missedTardis.removeFuel(-random.nextBetween(45, 125));
                    missedTardis.door().openDoors();
                    List<Explosion> explosions = new ArrayList<>();

                    missedTardis.getDesktop().getConsolePos().forEach(console -> {
                        Explosion explosion = WorldUtil.getTardisDimension().createExplosion(null, null, null,
                                console.toCenterPos(), 3f * 2, false, World.ExplosionSourceType.BLOCK);

                        explosions.add(explosion);
                    });

                    for (ServerPlayerEntity player : TardisUtil.getPlayersInsideInterior(missedTardis.asServer())) {
                        float xVel = AITMod.RANDOM.nextFloat(-2f, 3f);
                        float yVel = AITMod.RANDOM.nextFloat(-1f, 2f);
                        float zVel = AITMod.RANDOM.nextFloat(-2f, 3f);

                        player.setVelocity(xVel * 2, yVel * 2, zVel * 2);

                        if (!explosions.isEmpty()) {
                            player.damage(
                                    WorldUtil.getTardisDimension().getDamageSources().explosion(explosions.get(0)), 0);
                        } else {
                            player.damage(WorldUtil.getTardisDimension().getDamageSources().generic(), 0);
                        }
                    }
                }, 100L, Text.literal("Debris incoming!").formatted(Formatting.ITALIC, Formatting.YELLOW),
                new DirectionControl(), new RandomiserControl()));

        DIMENSIONAL_BREACH = register(
                Sequence.Builder.create(new Identifier(AITMod.MOD_ID, "dimensional_breach"), (finishedTardis -> {
                    finishedTardis.travel().decreaseFlightTime(60);
                }), (missedTardis -> {
                    missedTardis.door().openDoors();
                }), 80L, Text.literal("DIMENSION BREACH: SECURE DOORS").formatted(Formatting.ITALIC, Formatting.YELLOW),
                        new DimensionControl(), new DoorControl()));

        ENERGY_DRAIN = register(
                Sequence.Builder.create(new Identifier(AITMod.MOD_ID, "energy_drain"), (finishedTardis -> {
                    finishedTardis.travel().decreaseFlightTime(140);
                    finishedTardis.addFuel(random.nextBetween(45, 125));
                }), (missedTardis -> missedTardis.removeFuel(random.nextBetween(45, 125))), 80L,
                        Text.literal("Artron drain detected!").formatted(Formatting.ITALIC, Formatting.YELLOW),
                        new RefuelerControl()));

        POWER_DRAIN_IMMINENT = register(
                Sequence.Builder.create(new Identifier(AITMod.MOD_ID, "power_drain_imminent"), (finishedTardis -> {
                    finishedTardis.travel().decreaseFlightTime(120);
                    finishedTardis.addFuel(random.nextBetween(45, 125));
                }), (missedTardis -> {
                    missedTardis.removeFuel(random.nextBetween(45, 125));
                    missedTardis.engine().disablePower();
                }), 110L, Text.literal("Power drain imminent!").formatted(Formatting.ITALIC, Formatting.YELLOW),
                        new PowerControl(), new RefuelerControl(), new RandomiserControl()));

        SHIP_COMPUTER_OFFLINE = register(
                Sequence.Builder.create(new Identifier(AITMod.MOD_ID, "ship_computer_offline"), (finishedTardis -> {
                    finishedTardis.travel().decreaseFlightTime(240);
                    finishedTardis.addFuel(random.nextBetween(45, 125));
                }), (missedTardis -> {
                    missedTardis.removeFuel(random.nextBetween(45, 125));
                    missedTardis.engine().disablePower();
                }), 110L, Text.literal("Ship computer offline! Crash imminent!").formatted(Formatting.ITALIC,
                        Formatting.YELLOW), new AutoPilotControl()));

        ANTI_GRAVITY_ERROR = register(
                Sequence.Builder.create(new Identifier(AITMod.MOD_ID, "anti_gravity_error"), (finishedTardis -> {
                    finishedTardis.travel().decreaseFlightTime(120);
                }), (missedTardis -> {
                    missedTardis.removeFuel(random.nextBetween(45, 125));
                    missedTardis.travel().antigravs().set(false);
                }), 80L, Text.literal("Gravity miscalculation!").formatted(Formatting.ITALIC, Formatting.YELLOW),
                        new AntiGravsControl()));

        DIMENSIONAL_DRIFT_X = register(
                Sequence.Builder.create(new Identifier(AITMod.MOD_ID, "dimensional_drift_x"), (finishedTardis -> {
                    finishedTardis.travel().decreaseFlightTime(120);
                }), (missedTardis -> missedTardis.travel().forceDestination(cached -> {
                    BlockPos pos = cached.getPos();

                    return cached.pos(random.nextBetween(pos.getX() - 8, pos.getX() + 8), pos.getY(),
                            random.nextBetween(pos.getZ() - 8, pos.getZ() + 8));
                })), 100L, Text.literal("Drifting off course X!").formatted(Formatting.ITALIC, Formatting.YELLOW),
                        new DimensionControl(), new XControl()));

        DIMENSIONAL_DRIFT_Y = register(
                Sequence.Builder.create(new Identifier(AITMod.MOD_ID, "dimensional_drift_y"), (finishedTardis -> {
                    finishedTardis.travel().decreaseFlightTime(120);
                }), (missedTardis -> missedTardis.travel().forceDestination(cached -> {
                    BlockPos pos = cached.getPos();

                    return cached.pos(random.nextBetween(pos.getX() - 8, pos.getX() + 8), pos.getY(),
                            random.nextBetween(pos.getZ() - 8, pos.getZ() + 8));
                })), 100L, Text.literal("Drifting off course Y!").formatted(Formatting.ITALIC, Formatting.YELLOW),
                        new DimensionControl(), new YControl()));

        DIMENSIONAL_DRIFT_Z = register(
                Sequence.Builder.create(new Identifier(AITMod.MOD_ID, "dimensional_drift_z"), (finishedTardis -> {
                    finishedTardis.travel().decreaseFlightTime(120);
                }), (missedTardis -> missedTardis.travel().forceDestination(cached -> {
                    BlockPos pos = cached.getPos();

                    return cached.pos(random.nextBetween(pos.getX() - 8, pos.getX() + 8), pos.getY(),
                            random.nextBetween(pos.getZ() - 8, pos.getZ() + 8));
                })), 100L, Text.literal("Drifting off course Z!").formatted(Formatting.ITALIC, Formatting.YELLOW),
                        new DimensionControl(), new ZControl()));

        CLOAK_TO_AVOID_VORTEX_TRAPPED_MOBS = register(Sequence.Builder
                .create(new Identifier(AITMod.MOD_ID, "cloak_to_avoid_vortex_trapped_mobs"), (finishedTardis -> {
                    finishedTardis.travel().decreaseFlightTime(180);
                    DirectedBlockPos directedDoorPos = finishedTardis.getDesktop().doorPos();

                    if (directedDoorPos == null)
                        return;

                    BlockPos doorPos = directedDoorPos.getPos();

                    if (finishedTardis.door().isOpen() || WorldUtil.getTardisDimension().isClient())
                        return;

                    ItemEntity rewardForCloaking = new ItemEntity(EntityType.ITEM, WorldUtil.getTardisDimension());
                    rewardForCloaking.setPosition(doorPos.toCenterPos());

                    rewardForCloaking.setStack(
                            random.nextBoolean() ? Items.GOLD_NUGGET.getDefaultStack() : Items.POPPY.getDefaultStack());
                            WorldUtil.getTardisDimension().spawnEntity(rewardForCloaking);
                }), (missedTardis -> {
                    DirectedBlockPos directedDoorPos = missedTardis.getDesktop().doorPos();

                    if (directedDoorPos == null)
                        return;

                    BlockPos doorPos = directedDoorPos.getPos();
                    missedTardis.travel().increaseFlightTime(120);

                    if (missedTardis.door().isOpen() || WorldUtil.getTardisDimension().isClient())
                        return;

                    Vec3d centered = doorPos.toCenterPos();

                    ZombieEntity zombieEntity = new ZombieEntity(EntityType.ZOMBIE, WorldUtil.getTardisDimension());
                    zombieEntity.setPosition(centered);

                    DrownedEntity drownedEntity = new DrownedEntity(EntityType.DROWNED,
                            WorldUtil.getTardisDimension());
                    drownedEntity.setPosition(centered);

                    PhantomEntity phantomEntity = new PhantomEntity(EntityType.PHANTOM,
                            WorldUtil.getTardisDimension());
                    phantomEntity.setPosition(centered);

                            WorldUtil.getTardisDimension().spawnEntity(
                            random.nextBoolean() ? random.nextBoolean() ? drownedEntity : zombieEntity : phantomEntity);
                }), 80L, Text.literal("Immediate cloaking necessary!").formatted(Formatting.ITALIC, Formatting.YELLOW),
                        new CloakControl(), new RandomiserControl()));

        DIRECTIONAL_ERROR = register(
                Sequence.Builder.create(new Identifier(AITMod.MOD_ID, "directional_error"), (finishedTardis -> {
                    finishedTardis.travel().decreaseFlightTime(80);
                }), (missedTardis -> {
                    missedTardis.travel().increaseFlightTime(120);
                }), 80L, Text.literal("Directional error!").formatted(Formatting.ITALIC, Formatting.YELLOW),
                        new DirectionControl()));

        SPEED_UP_TO_AVOID_DRIFTING_OUT_OF_VORTEX = register(Sequence.Builder
                .create(new Identifier(AITMod.MOD_ID, "speed_up_to_avoid_drifting_out_of_vortex"), (finishedTardis -> {
                    finishedTardis.travel().decreaseFlightTime(180);
                }), (missedTardis -> {
                    missedTardis.removeFuel(random.nextBetween(45, 125));
                }), 80L, Text.literal("Vortex drift: acceleration necessary!").formatted(Formatting.ITALIC,
                        Formatting.YELLOW), new IncrementControl(), new ThrottleControl()));

        COURSE_CORRECT = register(
                Sequence.Builder.create(new Identifier(AITMod.MOD_ID, "course_correct"), (finishedTardis -> {
                    finishedTardis.travel().decreaseFlightTime(240);
                }), (missedTardis -> {
                    missedTardis.removeFuel(random.nextBetween(65, 250));

                    missedTardis.travel().forceDestination(cached -> {
                        BlockPos pos = cached.getPos();

                        return cached.pos(random.nextBetween(pos.getX() - 24, pos.getX() + 24), pos.getY(),
                                random.nextBetween(pos.getZ() - 24, pos.getZ() + 24));
                    });
                }), 110L, Text.literal("TARDIS off course!").formatted(Formatting.ITALIC, Formatting.YELLOW),
                        new HandBrakeControl(), new ThrottleControl(), new RandomiserControl()));

        GROUND_UNSTABLE = register(
                Sequence.Builder.create(new Identifier(AITMod.MOD_ID, "ground_unstable"), (finishedTardis -> {
                    finishedTardis.travel().decreaseFlightTime(120);
                    finishedTardis.addFuel(random.nextBetween(45, 125));
                }), (missedTardis -> {
                    missedTardis.removeFuel(random.nextBetween(45, 125));
                }), 110L, Text.literal("Unstable landing position!").formatted(Formatting.ITALIC, Formatting.YELLOW),
                        new LandTypeControl(), new YControl(), new SetWaypointControl()));

        INCREMENT_SCALE_RECALCULATION_NECESSARY = register(Sequence.Builder
                .create(new Identifier(AITMod.MOD_ID, "increment_scale_recalculation_necessary"), (finishedTardis -> {
                    finishedTardis.travel().decreaseFlightTime(100);
                    finishedTardis.addFuel(random.nextBetween(45, 125));
                }), (missedTardis -> {
                    missedTardis.removeFuel(random.nextBetween(45, 125));
                }), 80L, Text.literal("Increment scale error! Recalculation necessary!").formatted(Formatting.ITALIC,
                        Formatting.YELLOW), new IncrementControl()));

        SMALL_DEBRIS_FIELD = register(
                Sequence.Builder.create(new Identifier(AITMod.MOD_ID, "small_debris_field"), (finishedTardis -> {
                    finishedTardis.travel().decreaseFlightTime(110);
                    finishedTardis.addFuel(random.nextBetween(45, 125));
                }), (missedTardis -> {
                    missedTardis.removeFuel(random.nextBetween(45, 125));
                }), 80L, Text.literal("Small debris field!").formatted(Formatting.ITALIC, Formatting.YELLOW),
                        new IncrementControl(), new ShieldsControl()));
    }
}
