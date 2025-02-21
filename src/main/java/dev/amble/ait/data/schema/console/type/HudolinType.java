package dev.amble.ait.data.schema.console.type;

import org.joml.Vector3f;

import net.minecraft.entity.EntityDimensions;
import net.minecraft.util.Identifier;

import dev.amble.ait.AITMod;
import dev.amble.ait.core.tardis.control.ControlTypes;
import dev.amble.ait.core.tardis.control.impl.*;
import dev.amble.ait.core.tardis.control.impl.pos.IncrementControl;
import dev.amble.ait.core.tardis.control.impl.pos.XControl;
import dev.amble.ait.core.tardis.control.impl.pos.YControl;
import dev.amble.ait.core.tardis.control.impl.pos.ZControl;
import dev.amble.ait.core.tardis.control.impl.waypoint.ConsolePortControl;
import dev.amble.ait.core.tardis.control.impl.waypoint.MarkWaypointControl;
import dev.amble.ait.core.tardis.control.impl.waypoint.SetWaypointControl;
import dev.amble.ait.data.schema.console.ConsoleTypeSchema;
import dev.amble.ait.data.schema.console.ConsoleVariantSchema;
import dev.amble.ait.registry.impl.console.variant.ConsoleVariantRegistry;

public class HudolinType extends ConsoleTypeSchema {
    public static final Identifier REFERENCE = AITMod.id("console/hudolin");
    private static final ControlTypes[] TYPES = new ControlTypes[]{
            new ControlTypes(new ThrottleControl(), EntityDimensions.changing(0.16249998f, 0.31249997f),
                    new Vector3f(-0.48203126061707735f, 0.48750004917383194f, 0.5136718945577741f)),
            new ControlTypes(new HandBrakeControl(), EntityDimensions.changing(0.15f, 0.18750001f),
                    new Vector3f(0.5609371401369572f, 0.5000000968575478f, -0.6234378945082426f)),
            new ControlTypes(new AutoPilotControl(), EntityDimensions.changing(0.125f, 0.16250001f),
                    new Vector3f(-0.6453125048428774f, 0.47500033490359783f, 0.37578145880252123f)),
            new ControlTypes(new FastReturnControl(), EntityDimensions.changing(0.12500001f, 0.10000001f),
                    new Vector3f(-0.162500006146729f, 0.3749999962747097f, 0.9621093794703484f)),
            new ControlTypes(new DoorControl(), EntityDimensions.changing(0.1125f, 0.1625f),
                    new Vector3f(-0.6148439403623343f, 0.5999997165054083f, 0.13554669544100761f)),
            new ControlTypes(new DoorLockControl(), EntityDimensions.changing(0.33749995f, 0.2125f),
                    new Vector3f(-0.5523443222045898f, 0.46249966602772474f, -0.33671894017606974f)),
            new ControlTypes(new AntiGravsControl(), EntityDimensions.changing(0.1f, 0.125f),
                    new Vector3f(0.32656249310821295f, 0.5000002393499017f, -0.7976558804512024f)),
            new ControlTypes(new MonitorControl(), EntityDimensions.changing(0.29999998f, 0.3749999f),
                    new Vector3f(-0.0015630722045898438f, 0.40001535415649414f, 0.6378898620605469f)),
            new ControlTypes(new SecurityControl(), EntityDimensions.changing(0.07499999f, 0.1375f),
                    new Vector3f(0.778906268067658f, 0.4624997554346919f, 0.08945311885327101f)),
            new ControlTypes(new TelepathicControl(), EntityDimensions.changing(0.15000004f, 0.099999994f),
                    new Vector3f(-0.7499998146668077f, 0.46250023879110813f, 0.7859376929700375f)),
            new ControlTypes(new LandTypeControl(), EntityDimensions.changing(0.1125f, 0.15f),
                    new Vector3f(0.6273437645286322f, 0.5025001531466842f, -0.005078110843896866f)),
            new ControlTypes(new IncrementControl(), EntityDimensions.changing(0.275f, 0.15f),
                    new Vector3f(0.399218556471169f, 0.7000002423301339f, 0.22656270302832127f)),
            new ControlTypes(new XControl(), EntityDimensions.changing(0.074999996f, 0.099999994f),
                    new Vector3f(0.35234376322478056f, 0.5749996183440089f, -0.40859375055879354f)),
            new ControlTypes(new YControl(), EntityDimensions.changing(0.074999996f, 0.1f),
                    new Vector3f(0.2640625135973096f, 0.5750001911073923f, -0.46054687537252903f)),
            new ControlTypes(new ZControl(), EntityDimensions.changing(0.074999996f, 0.1f),
                    new Vector3f(0.17656251415610313f, 0.5749998101964593f, -0.51171875f)),
            new ControlTypes(new RandomiserControl(), EntityDimensions.changing(0.1375f, 0.16250001f),
                    new Vector3f(-1.0531250098720193f, 0.39999942295253277f, 0.23867206927388906f)),
            new ControlTypes(new DirectionControl(), EntityDimensions.changing(0.1375f, 0.1875f),
                    new Vector3f(0.5476560769602656f, 0.4624997144564986f, 0.5855467021465302f)),
            new ControlTypes(new HailMaryControl(),EntityDimensions.changing(0.31249997f, 0.06249999f),
                    new Vector3f(0.0023437608033418655f, 0.5124999051913619f, 0.7390627004206181f)),
            new ControlTypes(new CloakControl(), EntityDimensions.changing(0.06249999f, 0.18750001f),
                    new Vector3f(-0.18515625223517418f, 0.4000003803521395f, -0.9226562483236194f)),
            new ControlTypes(new SiegeModeControl(), EntityDimensions.changing(0.15f, 0.125f),
                    new Vector3f(0.79140645544976f, 0.5374998087063432f, -0.14804707467556f)),
            new ControlTypes(new DimensionControl(), EntityDimensions.changing(0.125f, 0.1125f),
                    new Vector3f(-0.7640624977648258f, 0.3700000746175647f, 0.6269531277939677f)),
            new ControlTypes(new RefuelerControl(), EntityDimensions.changing(0.1125f, 0.17500001f),
                    new Vector3f(0.6757814390584826f, 0.47500028647482395f, 0.3320316392928362f)),
            new ControlTypes(new HADSControl(), EntityDimensions.changing(0.049999997f, 0.16250001f),
                    new Vector3f(-0.5468752104789019f, 0.46249980945140123f, 0.6648441385477781f)),
            new ControlTypes(new PowerControl(), EntityDimensions.changing(0.20000002f, 0.16250001f),
                    new Vector3f(0.4890621304512024f, 0.6125006694346666f, -0.2843749960884452f)),
            new ControlTypes(new ConsolePortControl(), EntityDimensions.changing(0.32499996f, 0.15f),
                    new Vector3f(-7.816189900040627E-4f, 0.7500003390014172f, -0.3578126961365342f)),
            new ControlTypes(new MarkWaypointControl(), EntityDimensions.changing(0.074999996f, 0.1f),
                    new Vector3f(-0.7125000134110451f, 0.47500038146972656f, 0.1789062386378646f)),
            new ControlTypes(new SetWaypointControl(), EntityDimensions.changing(0.07499999f, 0.1f),
                    new Vector3f(-0.7109374962747097f, 0.47500057239085436f, -0.17265624925494194f)),
            new ControlTypes(new SonicPortControl(),EntityDimensions.changing(0.16250001f, 0.15f),
                    new Vector3f(-0.3476564511656761f, 0.7500000996515155f, -0.19921836256980896f)),
            new ControlTypes(new ShieldsControl(), EntityDimensions.changing(0.08749999f, 0.16250001f),
                    new Vector3f(0.7640625275671482f, 0.4374997578561306f, -0.09999999217689037f)),};

    public HudolinType() {
        super(REFERENCE, "hudolin");
    }

    @Override
    public ControlTypes[] getControlTypes() {
        return TYPES;
    }

    @Override
    public ConsoleVariantSchema getDefaultVariant() {
        return ConsoleVariantRegistry.HUDOLIN;
    }
}
