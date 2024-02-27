package mdteam.ait.core.util.gson;

import com.google.gson.*;
import com.google.gson.internal.LinkedTreeMap;
import mdteam.ait.AITMod;
import mdteam.ait.registry.DesktopRegistry;
import net.minecraft.util.Identifier;

import java.lang.reflect.Type;

public class IdentifierSerializer implements JsonDeserializer<Identifier> {
	@Override
	public Identifier deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		LinkedTreeMap<String, String> map = context.deserialize(json, LinkedTreeMap.class);

		if (map.get("namespace") == null || map.get("path") == null) {
			AITMod.LOGGER.error("namespace/path was null!");
			return null;
		}

		return Identifier.of((String) map.get("namespace"), (String) map.get("path"));
	}
}
