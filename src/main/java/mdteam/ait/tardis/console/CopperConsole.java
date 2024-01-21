package mdteam.ait.tardis.console;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.control.ControlTypes;
import mdteam.ait.tardis.control.impl.*;
import mdteam.ait.tardis.control.impl.pos.IncrementControl;
import mdteam.ait.tardis.control.impl.pos.XControl;
import mdteam.ait.tardis.control.impl.pos.YControl;
import mdteam.ait.tardis.control.impl.pos.ZControl;
import mdteam.ait.tardis.control.impl.waypoint.InsertWaypointControl;
import mdteam.ait.tardis.control.impl.waypoint.LoadWaypointControl;
import mdteam.ait.tardis.control.impl.waypoint.SetWaypointControl;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;

public class CopperConsole extends ConsoleSchema {
    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "console/copper");
    private static final ControlTypes[] TYPES = new ControlTypes[]{
            new ControlTypes(new ThrottleControl(), EntityDimensions.changing(0.16249998f, 0.17500001f), new Vector3f(-0.35937500558793545f, 0.6499999985098839f, -1.026562505401671f)),
            new ControlTypes(new HandBrakeControl(), EntityDimensions.changing(0.099999994f, 0.22500002f), new Vector3f(-0.6757812760770321f, 0.29999980982393026f, -1.2625000057742f)),
            new ControlTypes(new AutoPilotControl(), EntityDimensions.changing(0.1125f, 0.1375f), new Vector3f(0.30390626564621925f, 0.6250001918524504f, -1.0468750186264515f)),
            new ControlTypes(new FastReturnControl(), EntityDimensions.changing(0.075f, 0.075f), new Vector3f(0.2742187436670065f, 0.7500001937150955f, -0.8656250098720193f)),
            new ControlTypes(new DoorControl(), EntityDimensions.changing(0.06249999f, 0.11249998f), new Vector3f(0.6101562408730388f, 0.29999980982393026f, -1.310937530361116f)),
            new ControlTypes(new DoorLockControl(), EntityDimensions.changing(0.07499999f, 0.14999999f), new Vector3f(0.8484375290572643f, 0.26249923277646303f, 1.1281250081956387f)),
            new ControlTypes(new AntiGravsControl(), EntityDimensions.changing(0.125f, 0.16250001f), new Vector3f(0.7023437451571226f, 0.5625f, 0.8812500042840838f)),
            new ControlTypes(new MonitorControl(), EntityDimensions.changing(0.3749999f, 0.36249992f), new Vector3f(-0.5257812635973096f, 1.5750017277896404f, 0.7093750014901161f)),
            new ControlTypes(new TelepathicControl(), EntityDimensions.changing(0.27500004f, 0.20000002f), new Vector3f(0.2000000076368451f, 0.46249980945140123f, 1.1390625294297934f)),
            new ControlTypes(new LandTypeControl(), EntityDimensions.changing(0.15f, 0.17500001f), new Vector3f(-0.48671876825392246f, 1.065000157803297f, -0.26874999795109034f)),
            new ControlTypes(new IncrementControl(), EntityDimensions.changing(0.06249999f, 0.125f), new Vector3f(1.1125000165775418f, 0.4875005707144737f, -0.385937524959445f)),
            new ControlTypes(new XControl(), EntityDimensions.changing(0.06249999f, 0.08749999f), new Vector3f(0.9132812488824129f, 0.5374998105689883f, -0.570312513038516f)),
            new ControlTypes(new YControl(), EntityDimensions.changing(0.06249999f, 0.0875f), new Vector3f(0.8781249979510903f, 0.5249998103827238f, -0.6609375113621354f)),
            new ControlTypes(new ZControl(),  EntityDimensions.changing(0.062499996f, 0.0875f), new Vector3f(0.9249999979510903f, 0.47500000055879354f, -0.7359375096857548f)),
            new ControlTypes(new RandomiserControl(), EntityDimensions.changing(0.1125f, 0.1125f), new Vector3f(-1.3023437727242708f, 0.25000018533319235f, -0.3499999977648258f)),
            new ControlTypes(new DirectionControl(), EntityDimensions.changing(0.15f, 0.15f), new Vector3f(-0.7125000106170774f, 0.42500000074505806f, 1.2359374985098839f)),
            new ControlTypes(new HailMaryControl(), EntityDimensions.changing(0.1125f, 0.1125f), new Vector3f(-1.2210937505587935f, 0.5124996202066541f, -0.18750001303851604f)),
            new ControlTypes(new DimensionControl(), EntityDimensions.changing(0.31249997f, 0.125f), new Vector3f(-1.1757812583819032f, 0.17000007256865501f, -0.6765625225380063f)),
            new ControlTypes(new RefuelerControl(), EntityDimensions.changing(0.08749999f, 0.099999994f), new Vector3f(-0.4875000072643161f, 1.2999998200684786f, -0.24218748603016138f)),
            new ControlTypes(new PowerControl(), EntityDimensions.changing(0.15f, 0.16250001f), new Vector3f(-1.1609375020489097f, 0.5500001907348633f, 0.1640625111758709f)),
            new ControlTypes(new SiegeModeControl(), EntityDimensions.changing(0.08749999f, 0.099999994f), new Vector3f(-0.1703124837949872f, 0.8250000085681677f, -0.634375018067658f)),
            new ControlTypes(new HADSControl(), EntityDimensions.changing(0.08749999f, 0.099999994f), new Vector3f(-0.020312494598329067f, 0.824999812990427f, -0.6312500014901161f)),
            new ControlTypes(new SetWaypointControl(), EntityDimensions.changing(0.125f, 0.1f), new Vector3f(-0.32421876676380634f, 0.6500003831461072f, 0.903125012293458f)),
            new ControlTypes(new LoadWaypointControl(), EntityDimensions.changing(0.099999994f, 0.074999996f), new Vector3f(-0.4492187686264515f, 0.5500003816559911f, 1.0890625165775418f)),
            new ControlTypes(new InsertWaypointControl(), EntityDimensions.changing(0.15f, 0.1125f), new Vector3f(-0.01093751098960638f, 0.6125005725771189f, 0.8796875094994903f)),
            new ControlTypes(new CloakControl(), EntityDimensions.changing(0.099999994f, 0.1375f), new Vector3f(-0.4976562522351742f, 0.9625003868713975f, 0.2890624972060323f)),
    };
    public CopperConsole() {
        super(REFERENCE, "copper");
    }

    @Override
    public ControlTypes[] getControlTypes() {
        return TYPES;
    }
}
