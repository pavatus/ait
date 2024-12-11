package loqor.ait.core.sounds.flight;

import dev.pavatus.register.datapack.SimpleDatapackRegistry;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.core.AITSounds;

public class FlightSoundRegistry extends SimpleDatapackRegistry<FlightSound> {
    private static final FlightSoundRegistry instance = new FlightSoundRegistry();

    public FlightSoundRegistry() {
        super(FlightSound::fromInputStream, FlightSound.CODEC, "fx/flight", true);
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
