package loqor.ait.core.sounds.travel;

import dev.pavatus.register.datapack.SimpleDatapackRegistry;

import loqor.ait.AITMod;
import loqor.ait.core.AITSounds;
import loqor.ait.core.sounds.travel.map.TravelSoundMap;
import loqor.ait.core.tardis.handler.travel.TravelHandlerBase;

public class TravelSoundRegistry extends SimpleDatapackRegistry<TravelSound> {
    private static final TravelSoundRegistry instance = new TravelSoundRegistry();

    public TravelSoundRegistry() {
        super(TravelSound::fromInputStream, TravelSound.CODEC, "fx/travel", true);
    }

    public static TravelSoundRegistry getInstance() {
        return instance;
    }

    public static TravelSoundMap DEFAULT;
    public static TravelSound DEFAULT_DEMAT;
    public static TravelSound DEFAULT_MAT;
    public static TravelSound PHASING_DEMAT;
    public static TravelSound PHASING_REMAT;
    public static TravelSound EMPTY;

    @Override
    protected void defaults() {
        EMPTY = register(new TravelSound(TravelHandlerBase.State.FLIGHT, AITMod.id("empty"), AITSounds.VORTEX_SOUND.getId(), 0, 0, 0, 0, 0, 0));
        DEFAULT_DEMAT = register(new TravelSound(TravelHandlerBase.State.DEMAT, AITMod.id("default_demat"), AITSounds.DEMAT.getId(), 240, 240, 240, 210, 0.2f, 0.4f));
        DEFAULT_MAT = register(new TravelSound(TravelHandlerBase.State.MAT, AITMod.id("default_mat"), AITSounds.MAT.getId(), 460, 240, 240, 400, 0.2f, 0.4f));
        DEFAULT = new TravelSoundMap().of(TravelHandlerBase.State.DEMAT, DEFAULT_DEMAT).of(TravelHandlerBase.State.MAT, DEFAULT_MAT);
        PHASING_DEMAT = register(new TravelSound(TravelHandlerBase.State.DEMAT, AITMod.id("phasing_demat"), AITSounds.PHASING_DEMAT.getId(), 240, 240, 240, 210, 0.2f, 0.4f));
        PHASING_DEMAT = register(new TravelSound(TravelHandlerBase.State.MAT, AITMod.id("phasing_remat"), AITSounds.PHASING_REMAT.getId(), 240, 240, 240, 210, 0.2f, 0.4f));
    }

    @Override
    public TravelSound fallback() {
        return EMPTY;
    }
}
