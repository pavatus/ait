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
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class SequenceRegistry {
    public static final SimpleRegistry<Sequence> REGISTRY = FabricRegistryBuilder.createSimple(RegistryKey.<Sequence>ofRegistry(new Identifier(AITMod.MOD_ID, "sequence"))).buildAndRegister();
    private static final Random random = Random.create();
    public static Sequence register(Sequence schema) {
        return Registry.register(REGISTRY, schema.id(), schema);
    }

    public static final String AVOID_DEBRIS = "avoid_debris";
    public static final String DIMENSIONAL_BREACH = "dimensional_breach";
    public static final String ENERGY_DRAIN = "energy_drain";
    public static final String DIMENSIONAL_DRIFT_X = "dimensional_drift_x";
    public static final String DIMENSIONAL_DRIFT_Y = "dimensional_drift_y";
    public static final String DIMENSIONAL_DRIFT_Z = "dimensional_drift_z";
    public static final String FORCED_MAT = "forced_materialisation";
    public static final String CLOAK_TO_AVOID_VORTEX_TRAPPED_MOBS = "cloak_to_avoid_vortex_trapped_mobs";
    public static final String ANTI_GRAVITY_ERROR = "anti_gravity_error";
    public static final String POWER_DRAIN_IMMINENT = "power_drain_imminent";


    public static void init() {

        /*register(Sequence.Builder.create(new Identifier(AITMod.MOD_ID, SOME_SEQUENCE), (finishedTardis -> {
        }), (missedTardis -> {
        }), 100L, new Control()),*/

        register(Sequence.Builder.create(new Identifier(AITMod.MOD_ID, AVOID_DEBRIS), (finishedTardis -> {
            finishedTardis.getHandlers().getFlight().decreaseFlightTime(30);
        }), (missedTardis -> {
            missedTardis.removeFuel(-random.nextBetween(45, 125));
            missedTardis.getDoor().openDoors();
            //missedTardis.getTravel().crash();
        }), 100L, new DirectionControl(), new RandomiserControl()));

        register(Sequence.Builder.create(new Identifier(AITMod.MOD_ID, DIMENSIONAL_BREACH), (finishedTardis -> {
            finishedTardis.getHandlers().getFlight().decreaseFlightTime(30);
        }), (missedTardis -> {
            missedTardis.getDoor().openDoors();
        }), 80L, new DimensionControl(), new DoorControl()));

        register(Sequence.Builder.create(new Identifier(AITMod.MOD_ID, ENERGY_DRAIN), (finishedTardis -> {
            finishedTardis.getHandlers().getFlight().decreaseFlightTime(30);
            finishedTardis.addFuel(random.nextBetween(45, 125));
        }), (missedTardis -> {
            missedTardis.removeFuel(random.nextBetween(45, 125));
        }),  80L, new RefuelerControl()));

        register(Sequence.Builder.create(new Identifier(AITMod.MOD_ID, POWER_DRAIN_IMMINENT), (finishedTardis -> {
            finishedTardis.getHandlers().getFlight().decreaseFlightTime(120);
            finishedTardis.addFuel(random.nextBetween(45, 125));
        }), (missedTardis -> {
            missedTardis.removeFuel(random.nextBetween(45, 125));
            missedTardis.disablePower();
        }),  110L, new PowerControl(), new RefuelerControl(), new RandomiserControl()));

        register(Sequence.Builder.create(new Identifier(AITMod.MOD_ID, ANTI_GRAVITY_ERROR), (finishedTardis -> {
            finishedTardis.getHandlers().getFlight().decreaseFlightTime(30);
        }), (missedTardis -> {
            missedTardis.removeFuel(random.nextBetween(45, 125));
            PropertiesHandler.set(missedTardis, PropertiesHandler.ANTIGRAVS_ENABLED, false);
        }),  80L, new AntiGravsControl()));

        register(Sequence.Builder.create(new Identifier(AITMod.MOD_ID, DIMENSIONAL_DRIFT_X), (finishedTardis -> {
            finishedTardis.getHandlers().getFlight().decreaseFlightTime(30);
        }), (missedTardis -> {
            AbsoluteBlockPos.Directed pos = missedTardis.destination();
            missedTardis.getTravel().setDestination(new AbsoluteBlockPos.Directed(
                    random.nextBetween(pos.getX() - 8, pos.getX() + 8),
                    pos.getY(),
                    random.nextBetween(pos.getZ() - 8, pos.getZ() + 8), pos.getWorld(),
                    pos.getDirection()));
        }),  100L, new DimensionControl(), new XControl()));

        register(Sequence.Builder.create(new Identifier(AITMod.MOD_ID, DIMENSIONAL_DRIFT_Y), (finishedTardis -> {
            finishedTardis.getHandlers().getFlight().decreaseFlightTime(30);
        }), (missedTardis -> {
            AbsoluteBlockPos.Directed pos = missedTardis.destination();
            missedTardis.getTravel().setDestination(new AbsoluteBlockPos.Directed(
                    random.nextBetween(pos.getX() - 8, pos.getX() + 8),
                    pos.getY(),
                    random.nextBetween(pos.getZ() - 8, pos.getZ() + 8), pos.getWorld(),
                    pos.getDirection()));
        }),  100L, new DimensionControl(), new YControl()));

        register(Sequence.Builder.create(new Identifier(AITMod.MOD_ID, DIMENSIONAL_DRIFT_Z), (finishedTardis -> {
            finishedTardis.getHandlers().getFlight().decreaseFlightTime(30);
        }), (missedTardis -> {
            AbsoluteBlockPos.Directed pos = missedTardis.destination();
            missedTardis.getTravel().setDestination(new AbsoluteBlockPos.Directed(
                    random.nextBetween(pos.getX() - 8, pos.getX() + 8),
                    pos.getY(),
                    random.nextBetween(pos.getZ() - 8, pos.getZ() + 8), pos.getWorld(),
                    pos.getDirection()));
        }),  100L, new DimensionControl(), new ZControl()));

        register(Sequence.Builder.create(new Identifier(AITMod.MOD_ID, FORCED_MAT), (finishedTardis -> {
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
        }),  80L, new DirectionControl(), new XControl(), new ZControl()));

        register(Sequence.Builder.create(new Identifier(AITMod.MOD_ID, CLOAK_TO_AVOID_VORTEX_TRAPPED_MOBS), (finishedTardis -> {
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
        }), 80L, new CloakControl(), new RandomiserControl()));
    }

    public static String getSequenceMessage(Identifier id) {
        return switch(id.getPath()) {
            case SequenceRegistry.AVOID_DEBRIS -> "Debris incoming!";
            case SequenceRegistry.CLOAK_TO_AVOID_VORTEX_TRAPPED_MOBS -> "Immediate cloaking necessary!";
            case SequenceRegistry.DIMENSIONAL_BREACH -> "DIMENSION BREACH: SECURE DOORS";
            case SequenceRegistry.DIMENSIONAL_DRIFT_X -> "Drifting off course X!";
            case SequenceRegistry.DIMENSIONAL_DRIFT_Y -> "Drifting off course Y!";
            case SequenceRegistry.DIMENSIONAL_DRIFT_Z -> "Drifting off course Z!";
            case SequenceRegistry.ANTI_GRAVITY_ERROR -> "Anti-gravity error detected!";
            case SequenceRegistry.ENERGY_DRAIN -> "Artron drain detected!";
            case SequenceRegistry.POWER_DRAIN_IMMINENT -> "Power drain imminent!";
            case SequenceRegistry.FORCED_MAT -> "Materialisation forced!";
            default -> "Sequence started";
        };
    }
}
