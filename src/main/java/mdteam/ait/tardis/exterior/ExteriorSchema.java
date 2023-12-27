package mdteam.ait.tardis.exterior;

import mdteam.ait.core.AITSounds;
import mdteam.ait.core.sounds.MatSound;
import mdteam.ait.tardis.TardisTravel;
import net.minecraft.util.Identifier;

public abstract class ExteriorSchema {
    private final Identifier id;

    protected ExteriorSchema(Identifier id) {
        this.id = id;
    }

    public Identifier id() {
        return this.id;
    }

    public MatSound getSound(TardisTravel.State state) {
        return switch (state) {
            case LANDED, CRASH -> AITSounds.LANDED_ANIM;
            case FLIGHT -> AITSounds.FLIGHT_ANIM;
            case DEMAT -> AITSounds.DEMAT_ANIM;
            case MAT -> AITSounds.MAT_ANIM;
        };
    }
}
