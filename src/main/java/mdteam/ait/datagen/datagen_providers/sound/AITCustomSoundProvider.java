package mdteam.ait.datagen.datagen_providers.sound;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.sound.SoundEvent;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public abstract class AITCustomSoundProvider implements DataProvider {
	protected final FabricDataOutput dataOutput;

	protected AITCustomSoundProvider(FabricDataOutput dataOutput) {
		this.dataOutput = dataOutput;
	}

	public abstract void generateSoundsData(AITCustomSoundBuilder aitCustomSoundBuilder);

	@Override
	public CompletableFuture<?> run(DataWriter writer) {
		HashMap<String, SoundEvent[]> soundEventsHashMap = new HashMap<>();

		generateSoundsData(((soundName, soundEvents) -> {
			if (soundEventsHashMap.containsKey(soundName)) {
				throw new RuntimeException("Duplicate sound event: " + soundName + " - Duplicate will be ignored!");
			} else if (soundName.contains(" ")) {
				throw new RuntimeException("Sound event name cannot contain spaces: " + soundName);
			} else {
				for (Character character : soundName.toCharArray()) {
					if (Character.isTitleCase(character)) {
						throw new RuntimeException("Sound event name cannot contain capital letters: " + soundName);
					} else if (Character.isUpperCase(character)) {
						throw new RuntimeException("Sound event name cannot contain capital letters: " + soundName);
					}
				}
				soundEventsHashMap.put(soundName, soundEvents);
			}
		}));
		JsonObject soundJsonObject = new JsonObject();

		soundEventsHashMap.forEach((soundName, soundEvents) -> {
			soundJsonObject.add(soundName, createJsonObjectForSoundEvent(soundEvents));
		});


		return DataProvider.writeToPath(writer, soundJsonObject, getOutputPath());
	}

	public Path getOutputPath() {
		return dataOutput.resolvePath(DataOutput.OutputType.RESOURCE_PACK).resolve(dataOutput.getModId()).resolve("sounds.json");
	}

	@Override
	public String getName() {
		return "Sound Definitions";
	}

	public JsonObject createJsonObjectForSoundEvent(SoundEvent[] soundEvents) {
		JsonObject soundEventJsonObject = new JsonObject();
		JsonArray soundsJsonObject = new JsonArray();

		for (SoundEvent soundEvent : soundEvents) {
			soundsJsonObject.add(soundEvent.getId().toString());
		}

		soundEventJsonObject.add("sounds", soundsJsonObject);
		return soundEventJsonObject;
	}
}
