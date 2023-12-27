package mdteam.ait.tardis;

import mdteam.ait.core.AITSounds;
import mdteam.ait.core.sounds.MatSound;

public enum ExteriorEnum {
    CAPSULE() {
    },
    POLICE_BOX() {
    },
    CLASSIC() {
    },
    TARDIM() {
    },
    BOOTH {
    },
    CUBE() {
    }
    ;

    public MatSound getSound(TardisTravel.State state) {
        return switch (state) {
            case LANDED, CRASH -> AITSounds.LANDED_ANIM;
            case FLIGHT -> AITSounds.FLIGHT_ANIM;
            case DEMAT -> AITSounds.DEMAT_ANIM;
            case MAT -> AITSounds.MAT_ANIM;
        };
    }
}
