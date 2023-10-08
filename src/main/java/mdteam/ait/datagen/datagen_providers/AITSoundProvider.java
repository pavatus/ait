package mdteam.ait.datagen.datagen_providers;

import mdteam.ait.datagen.datagen_providers.special.AITCustomSoundBuilder;
import mdteam.ait.datagen.datagen_providers.special.AITCustomSoundProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.sound.SoundEvent;

import java.util.HashMap;

public class AITSoundProvider extends AITCustomSoundProvider {
    private final FabricDataOutput dataGenerator;

    private HashMap<String, SoundEvent[]> soundEventList = new HashMap<>();
    public AITSoundProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
        dataGenerator = dataOutput;
    }

    @Override
    public void generateSoundsData(AITCustomSoundBuilder falloutCustomSoundBuilder) {
        soundEventList.forEach(falloutCustomSoundBuilder::add);
    }

    public void addSound(String soundName, SoundEvent sound) {
        soundEventList.put(soundName, new SoundEvent[]{sound});
    }
    public void addSound(String soundName, SoundEvent[] soundEvents) {
        soundEventList.put(soundName, soundEvents);
    }
}
