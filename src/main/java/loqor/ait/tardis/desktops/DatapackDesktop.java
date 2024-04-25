package loqor.ait.tardis.desktops;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import loqor.ait.AITMod;
import loqor.ait.tardis.data.loyalty.Loyalty;
import loqor.ait.tardis.desktops.textures.DesktopPreviewTexture;
import loqor.ait.tardis.TardisDesktopSchema;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class DatapackDesktop extends TardisDesktopSchema {
	public static final Codec<TardisDesktopSchema> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					Identifier.CODEC.fieldOf("id").forGetter(TardisDesktopSchema::id),
					Loyalty.CODEC.optionalFieldOf("loyalty")
							.forGetter(schema -> Optional.of(schema.getRequirement()))
			).apply(instance, (DatapackDesktop::new)));

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	public DatapackDesktop(Identifier id, Optional<Loyalty> loyalty) {
		super(id, new DesktopPreviewTexture(DesktopPreviewTexture.pathFromDesktopId(id)), loyalty.orElse(Loyalty.MIN));
	}

	public static TardisDesktopSchema fromInputStream(InputStream stream) {
		return fromJson(JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonObject());
	}

	public static TardisDesktopSchema fromJson(JsonObject json) {
		AtomicReference<TardisDesktopSchema> created = new AtomicReference<>();

		CODEC.decode(JsonOps.INSTANCE, json)
				.get()
				.ifLeft(desktop -> {
					created.set(desktop.getFirst());
				})
				.ifRight(err -> {
					created.set(null);
					AITMod.LOGGER.error("Error decoding datapack desktop: " + err);
				});

		return created.get();
	}
}
