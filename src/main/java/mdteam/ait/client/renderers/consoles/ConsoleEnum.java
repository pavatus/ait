package mdteam.ait.client.renderers.consoles;

import mdteam.ait.client.models.consoles.BorealisConsoleModel;
import mdteam.ait.client.models.consoles.ConsoleModel;
import mdteam.ait.client.models.consoles.TempConsoleModel;
import mdteam.ait.core.AITSounds;
import mdteam.ait.tardis.control.ControlTypes;
import mdteam.ait.tardis.control.impl.*;
import mdteam.ait.tardis.control.impl.pos.IncrementControl;
import mdteam.ait.tardis.control.impl.pos.XControl;
import mdteam.ait.tardis.control.impl.pos.YControl;
import mdteam.ait.tardis.control.impl.pos.ZControl;
import mdteam.ait.core.sounds.MatSound;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityDimensions;
import org.joml.Vector3f;
import mdteam.ait.tardis.TardisTravel;

public enum ConsoleEnum {
    TEMP() {
        @Override
        @Environment(value = EnvType.CLIENT)
        public ConsoleModel createModel() {
            return new TempConsoleModel(TempConsoleModel.getTexturedModelData().createModel());
        }

        @Override
        public Class<? extends ConsoleModel> getModelClass() {
            return TempConsoleModel.class;
        }

        @Override
        public ControlTypes[] getControlTypesList() {
            return new ControlTypes[]{
                    new ControlTypes(new MonitorControl(), EntityDimensions.changing(0.275f, 0.275f), new Vector3f(0.625f, 0.5250015258789062f, 0.34999999962747097f)), //0
                    new ControlTypes(new HandBrakeControl(), EntityDimensions.changing(0.175f, 0.175f), new Vector3f(-0.5750000085681677f, 0.4749999772757292f, -0.5750000234693289f)), //1
                    new ControlTypes(new ThrottleControl(), EntityDimensions.changing(0.175f, 0.175f), new Vector3f(-0.8000000156462193f, 0.4749999772757292f, -0.20000000298023224f)), //2
                    new ControlTypes(new DoorControl(), EntityDimensions.changing(0.175f, 0.175f), new Vector3f(0.0f, 0.4749999772757292f, 0.7750000078231096f)), //3
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
        }
    },
    BOREALIS() {
        @Override
        @Environment(value = EnvType.CLIENT)
        public ConsoleModel createModel() {
            return new BorealisConsoleModel(BorealisConsoleModel.getTexturedModelData().createModel());
        }

        @Override
        public Class<? extends ConsoleModel> getModelClass() {
            return BorealisConsoleModel.class;
        }

        //@TODO SEQUENCE REGISTRY, AKA, CONTROLS/STATIC FEATURES BEING REPLACED WITH SEQUENCES; REFERENCES BELOW.
        //- Instead of a chameleon circuit, you do a sequence with the visualiser and use the increment control to change the current exterior.
        //- Instead of just doing flight events, you will have to set certain controls to continue on their own,
        // AKA like auto control; like making the randomiser and direction set to auto so it can spin freely in the vortex.
        //- Instead of having the handbrake as a necessary condition for flight, if left active, the rotor AND default (the scraping noise) TARDIS demat/remat sounds are played;
        // if it is left off, it will be entirely silent except for the rotor noises.
        //- Instead of "boo hoo player locator lame it locate me :<" the sequence will be like so in this example:
        // 1. Set the dimension to the correct one you're currently in.
        // 1a. Make sure your handbrake is active during this time or the TARDIS will go awry.
        // 2. Set the refueler to "rooted but unfueling"
        // 3. Set the increment to whatever the sequence asks of you, it will ask it of you differently for each XYZ.
        // 4. Use the locator, and it will set the coordinates. Take off.

        private static final ControlTypes[] CONTROL_TYPES = new ControlTypes[]{
                new ControlTypes(new MonitorControl(), EntityDimensions.changing(0.275f, 0.275f), new Vector3f(0.625f, 0.5250015258789062f, 0.34999999962747097f)), //0
                new ControlTypes(new HandBrakeControl(), EntityDimensions.changing(0.175f, 0.175f), new Vector3f(-0.5750000085681677f, 0.4749999772757292f, -0.5750000234693289f)), //1
                new ControlTypes(new ThrottleControl(), EntityDimensions.changing(0.175f, 0.175f), new Vector3f(-0.8000000156462193f, 0.4749999772757292f, -0.20000000298023224f)), //2
                new ControlTypes(new DoorControl(), EntityDimensions.changing(0.175f, 0.175f), new Vector3f(0.0f, 0.4749999772757292f, 0.7750000078231096f)), //3
                new ControlTypes(new XControl(), EntityDimensions.changing(0.1f, 0.1f), new Vector3f(0.12500001676380634f, 0.6499999798834324f, -0.5250000078231096f)), //4
                new ControlTypes(new YControl(), EntityDimensions.changing(0.1f, 0.1f), new Vector3f(0.0f, 0.6499999798834324f, -0.5249999929219484f)), //5
                new ControlTypes(new ZControl(), EntityDimensions.changing(0.1f, 0.1f), new Vector3f(-0.12499999813735485f, 0.6499999798834324f, -0.5250000040978193f)), //6
                new ControlTypes(new IncrementControl(), EntityDimensions.changing(0.15f, 0.15f), new Vector3f(-1.862645149230957E-9f, 0.5249999780207872f, -0.6984375026077032f)), //7
                new ControlTypes(new RandomiserControl(), EntityDimensions.changing(0.125f, 0.125f), new Vector3f(0.23500000406056643f, 0.48499997798353434f, -0.8034375039860606f)), //8
                new ControlTypes(new LandTypeControl(), EntityDimensions.changing(0.125f, 0.125f), new Vector3f(-0.23500000312924385f, 0.4899999788030982f, -0.8050000118091702f)), //9
                new ControlTypes(new DirectionControl(), EntityDimensions.changing(0.1f, 0.1f), new Vector3f(-0.8000000212341547f, 0.5249999780207872f, 0.14999999664723873f)), //10
                new ControlTypes(new DimensionControl(), EntityDimensions.changing(0.125f, 0.125f), new Vector3f(0.4850000077858567f, 0.6449999799951911f, -0.28500001039355993f)), //11
                new ControlTypes(new TelepathicControl(), EntityDimensions.changing(0.325f, 0.125f), new Vector3f(-0.5507812462747097f, 0.5499999932944775f, 0.3234375026077032f)), //12
                new ControlTypes(new AutoPilotControl(), EntityDimensions.changing(0.1f, 0.1f), new Vector3f(-0.421875f, 0.6750001907348633f, 0.25f)), //13
                new ControlTypes(new HailMaryControl(), EntityDimensions.changing(0.074999996f, 0.074999996f), new Vector3f(-0.5468749962747097f, 0.6749999076128006f, -0.17421874962747097f)), //2
                new ControlTypes(new FastReturnControl(), EntityDimensions.changing(0.075f, 0.075f), new Vector3f(-0.4000000134110451f, 0.674999987706542f, -0.37500001303851604f)), //idk
                // @TODO new ControlTypes(new HADSControl(), EntityDimensions.changing(NaN), new Vector3f(NaN)),
                new ControlTypes(new AntiGravsControl(), EntityDimensions.changing(0.1f, 0.1f), new Vector3f(0.8750000037252903f, 0.5249999780207872f, 0.15156249701976776f)), //14
                new ControlTypes(new RefuelerControl(), EntityDimensions.changing(0.1f, 0.1f), new Vector3f(0.6593750026077032f, 0.5499999783933163f, -0.3812500014901161f)), //15
        };

        @Override
        public ControlTypes[] getControlTypesList() {
            return CONTROL_TYPES;
        }
    };

    @Environment(value = EnvType.CLIENT)
    public abstract ConsoleModel createModel();

    public abstract Class<? extends ConsoleModel> getModelClass();

    public abstract ControlTypes[] getControlTypesList();

    public MatSound getSound(TardisTravel.State state) {
        return switch (state) {
            case LANDED, CRASH -> AITSounds.LANDED_ANIM;
            case FLIGHT -> AITSounds.FLIGHT_ANIM;
            case DEMAT -> AITSounds.DEMAT_ANIM;
            case MAT -> AITSounds.MAT_ANIM;
        };
    }
}
