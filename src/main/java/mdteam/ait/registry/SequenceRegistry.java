package mdteam.ait.registry;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.control.impl.*;
import mdteam.ait.tardis.control.impl.pos.IncrementControl;
import mdteam.ait.tardis.control.impl.pos.XControl;
import mdteam.ait.tardis.control.impl.pos.YControl;
import mdteam.ait.tardis.control.impl.pos.ZControl;
import mdteam.ait.tardis.control.sequences.Sequence;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.util.TardisUtil;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.Items;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

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
    public static Sequence FORCED_MAT;
    public static Sequence CLOAK_TO_AVOID_VORTEX_TRAPPED_MOBS;
    public static Sequence ANTI_GRAVITY_ERROR;
    public static Sequence POWER_DRAIN_IMMINENT;


    public static void init() {

        /*SEQUENCE = register(Sequence.Builder.create(new Identifier(AITMod.MOD_ID, SOME_SEQUENCE), (finishedTardis -> {
        }), (missedTardis -> {
        }), 100L, Text.literal("<> detected!").formatting(Formatted.ITALIC, Formatted.YELLOW), new Control()),*/

        AVOID_DEBRIS = register(Sequence.Builder.create(new Identifier(AITMod.MOD_ID, "avoid_debris"), (finishedTardis -> {
            finishedTardis.getHandlers().getFlight().decreaseFlightTime(30);
        }), (missedTardis -> {
            missedTardis.removeFuel(-random.nextBetween(45, 125));
            missedTardis.getDoor().openDoors();
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
        }),  80L, Text.literal("Artron drain detected!").formatted(Formatting.ITALIC, Formatting.YELLOW),
                new RefuelerControl()));

        POWER_DRAIN_IMMINENT = register(Sequence.Builder.create(new Identifier(AITMod.MOD_ID, "power_drain_imminent"), (finishedTardis -> {
            finishedTardis.getHandlers().getFlight().decreaseFlightTime(120);
            finishedTardis.addFuel(random.nextBetween(45, 125));
        }), (missedTardis -> {
            missedTardis.removeFuel(random.nextBetween(45, 125));
            missedTardis.disablePower();
        }),  110L, Text.literal("Power drain imminent!").formatted(Formatting.ITALIC, Formatting.YELLOW),
                new PowerControl(), new RefuelerControl(), new RandomiserControl()));

        ANTI_GRAVITY_ERROR = register(Sequence.Builder.create(new Identifier(AITMod.MOD_ID, "anti_gravity_error"), (finishedTardis -> {
            finishedTardis.getHandlers().getFlight().decreaseFlightTime(30);
        }), (missedTardis -> {
            missedTardis.removeFuel(random.nextBetween(45, 125));
            PropertiesHandler.set(missedTardis, PropertiesHandler.ANTIGRAVS_ENABLED, false);
        }),  80L, Text.literal("Anti-gravity error detected!").formatted(Formatting.ITALIC, Formatting.YELLOW),
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
        }),  100L, Text.literal("Drifting off course X!").formatted(Formatting.ITALIC, Formatting.YELLOW),
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
        }),  100L, Text.literal("Drifting off course Y!").formatted(Formatting.ITALIC, Formatting.YELLOW),
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
        }),  100L, Text.literal("Drifting off course Z!").formatted(Formatting.ITALIC, Formatting.YELLOW),
                new DimensionControl(), new ZControl()));

        FORCED_MAT = register(Sequence.Builder.create(new Identifier(AITMod.MOD_ID, "forced_materialisation"), (finishedTardis -> {
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
                new DirectionControl(), new XControl(), new ZControl()));

        CLOAK_TO_AVOID_VORTEX_TRAPPED_MOBS = register(Sequence.Builder.create(new Identifier(AITMod.MOD_ID, "cloak_to_avoid_vortex_trapped_mobs"), (finishedTardis -> {
            finishedTardis.getHandlers().getFlight().decreaseFlightTime(30);
            // @TODO fix the entity spawning stuff, idk if i should do it though so - Loqor
            /*if(finishedTardis.getDoor().getDoorPos() == null) return;
            if(finishedTardis.getDoor().isOpen()) return;
            ItemEntity rewardForCloaking = new ItemEntity(EntityType.ITEM, TardisUtil.getTardisDimension());
            rewardForCloaking.setStack(random.nextBoolean() ? Items.GOLD_NUGGET.getDefaultStack() : Items.POPPY.getDefaultStack());
            TardisUtil.getTardisDimension().spawnEntity(rewardForCloaking);*/
        }), (missedTardis -> {
            missedTardis.getHandlers().getFlight().increaseFlightTime(30);
            /*if(missedTardis.getDoor().getDoorPos() == null) return;
            if(missedTardis.getDoor().isOpen()) return;
            ZombieEntity zombieEntity = new ZombieEntity(EntityType.ZOMBIE, TardisUtil.getTardisDimension());
            PhantomEntity phantomEntity = new PhantomEntity(EntityType.PHANTOM, TardisUtil.getTardisDimension());
            TardisUtil.getTardisDimension().spawnEntity(random.nextBoolean() ? zombieEntity : phantomEntity);*/
        }), 80L, Text.literal("Immediate cloaking necessary!").formatted(Formatting.ITALIC, Formatting.YELLOW),
                new CloakControl(), new RandomiserControl()));
    }
}
