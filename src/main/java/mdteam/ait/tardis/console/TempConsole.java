package mdteam.ait.tardis.console;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.control.ControlTypes;
import mdteam.ait.tardis.control.impl.*;
import mdteam.ait.tardis.control.impl.pos.IncrementControl;
import mdteam.ait.tardis.control.impl.pos.XControl;
import mdteam.ait.tardis.control.impl.pos.YControl;
import mdteam.ait.tardis.control.impl.pos.ZControl;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;

public class TempConsole extends ConsoleSchema {
    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "console/temp");
    private static final ControlTypes[] CONTROL_TYPES = new ControlTypes[]{
            new ControlTypes(new MonitorControl(), EntityDimensions.changing(0.275f, 0.275f), new Vector3f(0.625f, 0.5250015258789062f, 0.34999999962747097f)), //0
            new ControlTypes(new HandBrakeControl(), EntityDimensions.changing(0.175f, 0.175f), new Vector3f(-0.5750000085681677f, 0.4749999772757292f, -0.5750000234693289f)), //1
            new ControlTypes(new ThrottleControl(), EntityDimensions.changing(0.175f, 0.175f), new Vector3f(-0.8000000156462193f, 0.4749999772757292f, -0.20000000298023224f)), //2
            new ControlTypes(new DoorControl(), EntityDimensions.changing(0.175f, 0.175f), new Vector3f(0.0f, 0.4749999772757292f, 0.7750000078231096f)), //3
            new ControlTypes(new DoorLockControl(), EntityDimensions.changing(0.175f, 0.175f), new Vector3f(0.0f, 0.9749999772757292f, 0.7750000078231096f)),
            new ControlTypes(new XControl(), EntityDimensions.changing(0.1f, 0.1f), new Vector3f(0.12500001676380634f, 0.6499999798834324f, -0.5250000078231096f)), //4
            new ControlTypes(new YControl(), EntityDimensions.changing(0.1f, 0.1f), new Vector3f(0.0f, 0.6499999798834324f, -0.5249999929219484f)), //5
            new ControlTypes(new ZControl(), EntityDimensions.changing(0.1f, 0.1f), new Vector3f(-0.12499999813735485f, 0.6499999798834324f, -0.5250000040978193f)), //6
            new ControlTypes(new IncrementControl(), EntityDimensions.changing(0.15f, 0.15f), new Vector3f(-1.862645149230957E-9f, 0.5249999780207872f, -0.6984375026077032f)), //7
            new ControlTypes(new RandomiserControl(), EntityDimensions.changing(0.125f, 0.125f), new Vector3f(0.23500000406056643f, 0.48499997798353434f, -0.8034375039860606f)), //8
            new ControlTypes(new LandTypeControl(), EntityDimensions.changing(0.125f, 0.125f), new Vector3f(-0.23500000312924385f, 0.4899999788030982f, -0.8050000118091702f)), //9
            new ControlTypes(new DirectionControl(), EntityDimensions.changing(0.1f, 0.1f), new Vector3f(-0.8000000212341547f, 0.5249999780207872f, 0.14999999664723873f)), //10
            new ControlTypes(new DimensionControl(), EntityDimensions.changing(0.125f, 0.125f), new Vector3f(0.4850000077858567f, 0.6449999799951911f, -0.28500001039355993f)), //11
            new ControlTypes(new TelepathicControl(), EntityDimensions.changing(0.325f, 0.125f), new Vector3f(-0.5507812462747097f, 0.5499999932944775f, 0.3234375026077032f)), //12
            // new ControlTypes(new AutoPilotControl(),   EntityDimensions.changing(0.1f, 0.1f), new Vector3f(-0.5507812462747097f, 0.5499999932944775f, 0.3234375026077032f)), //12
            new ControlTypes(new VisualiserControl(), EntityDimensions.changing(0.1f, 0.1f), new Vector3f(0.8750000037252903f, 0.5249999780207872f, 0.15156249701976776f)), //13
            new ControlTypes(new RefuelerControl(), EntityDimensions.changing(0.1f, 0.1f), new Vector3f(0.6593750026077032f, 0.5499999783933163f, -0.3812500014901161f)), //14
    };

    public TempConsole() {
        super(REFERENCE, "temp");
    }

    @Override
    public ControlTypes[] getControlTypes() {
        return CONTROL_TYPES;
    }
}
