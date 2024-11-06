package loqor.ait.core.sounds.flight;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.core.AITSounds;
import loqor.ait.registry.datapack.SimpleDatapackRegistry;

public class FlightSoundRegistry extends SimpleDatapackRegistry<FlightSound> {
    private static final FlightSoundRegistry instance = new FlightSoundRegistry();

    public FlightSoundRegistry() {
        super(FlightSound::fromInputStream, FlightSound.CODEC, "sfx/flight", true);
    }

    public static FlightSoundRegistry getInstance() {
        return instance;
    }

    public static FlightSound DEFAULT;

    @Override
    protected void defaults() {
        DEFAULT = register(new FlightSound(new Identifier(AITMod.MOD_ID, "default"), AITSounds.FLIGHT_LOOP.getId(), 80));
    }

    @Override
    public FlightSound fallback() {
        return DEFAULT;
    }
}
