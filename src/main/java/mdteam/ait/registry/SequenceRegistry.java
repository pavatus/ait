package mdteam.ait.registry;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.control.impl.*;
import mdteam.ait.tardis.control.impl.pos.IncrementControl;
import mdteam.ait.tardis.control.impl.pos.XControl;
import mdteam.ait.tardis.control.impl.pos.ZControl;
import mdteam.ait.tardis.control.sequences.Sequence;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
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
    public static final String DIMENSIONAL_DRIFT = "dimensional_drift";
    public static final String FORCED_MAT = "forced_materialisation";


    public static void init() {
        register(Sequence.Builder.create(new Identifier(AITMod.MOD_ID, AVOID_DEBRIS), (finishedTardis -> {
            finishedTardis.getHandlers().getFlight().decreaseFlightTime(30);
        }), (missedTardis -> {
            //missedTardis.getTravel().crash();
        }), 500L, new DirectionControl(), new RandomiserControl()));
        register(Sequence.Builder.create(new Identifier(AITMod.MOD_ID, DIMENSIONAL_BREACH), (finishedTardis -> {
            finishedTardis.getHandlers().getFlight().decreaseFlightTime(30);
        }), (missedTardis -> {
            missedTardis.getDoor().openDoors();
        }), 200L, new DimensionControl(), new DoorControl()));
        register(Sequence.Builder.create(new Identifier(AITMod.MOD_ID, ENERGY_DRAIN), (finishedTardis -> {
            finishedTardis.getHandlers().getFlight().decreaseFlightTime(30);
        }), (missedTardis -> {
            missedTardis.removeFuel(-random.nextBetween(45, 125));
        }),  200L, new RefuelerControl()));
        register(Sequence.Builder.create(new Identifier(AITMod.MOD_ID, DIMENSIONAL_DRIFT), (finishedTardis -> {
            finishedTardis.getHandlers().getFlight().decreaseFlightTime(30);
        }), (missedTardis -> {
            AbsoluteBlockPos.Directed pos = missedTardis.destination();
            missedTardis.getTravel().setDestination(new AbsoluteBlockPos.Directed(
                    random.nextBetween(pos.getX() - 8, pos.getX() + 8),
                    pos.getY(),
                    random.nextBetween(pos.getZ() - 8, pos.getZ() + 8), pos.getWorld(),
                    pos.getDirection()));
        }),  200L, new DimensionControl(), new XControl()));
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
            //missedTardis.getHandlers().getSequenceHandler().disableConsole(true);
        }),  500L, new DirectionControl(), new XControl(), new ZControl()));
    }
}
