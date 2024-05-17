package loqor.ait.core.data.schema.exterior;

import com.google.gson.*;
import loqor.ait.AITMod;
import loqor.ait.client.models.exteriors.ExteriorModel;
import loqor.ait.registry.impl.exterior.ClientExteriorVariantRegistry;
import loqor.ait.registry.impl.exterior.ExteriorVariantRegistry;
import loqor.ait.core.data.base.Identifiable;
import loqor.ait.tardis.data.BiomeHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import org.joml.Vector3f;

import java.lang.reflect.Type;

@Environment(EnvType.CLIENT)
public abstract class ClientExteriorVariantSchema implements Identifiable {
	private final Identifier parent;
	private final Identifier id;

	protected ClientExteriorVariantSchema(Identifier parent, Identifier id) {
		this.parent = parent;
		this.id = id;
	}

	protected ClientExteriorVariantSchema(Identifier parent) {
		this.id = parent;
		this.parent = parent;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null) return false;
		if (!(o instanceof ClientExteriorVariantSchema that)) return false;

		return id.equals(that.id);
	}

	public ExteriorVariantSchema parent() {
		return ExteriorVariantRegistry.getInstance().get(this.parent);
	}

	public abstract Identifier getBiomeTexture(BiomeHandler.BiomeType biomeType);

	public Identifier id() {
		return id;
	}

	public abstract Identifier texture();

	public abstract Identifier emission();

	public abstract ExteriorModel model();

	public abstract Vector3f sonicItemTranslations();

	public float[] sonicItemRotations() {
		return new float[]{0f, 45f};
	}

	/**
	 * The default exterior for this category
	 */
	public ExteriorVariantSchema getDefaultVariant() {
		return ExteriorVariantRegistry.getInstance().get(this.parent().id()).category().getDefaultVariant();
	}

	public static Object serializer() {
		return new Serializer();
	}

	private static class Serializer implements JsonSerializer<ClientExteriorVariantSchema>, JsonDeserializer<ClientExteriorVariantSchema> {

		@Override
		public ClientExteriorVariantSchema deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			Identifier id;

			try {
				id = new Identifier(json.getAsJsonPrimitive().getAsString());
			} catch (InvalidIdentifierException e) {
				id = new Identifier(AITMod.MOD_ID, "capsule_default");
			}

			return ClientExteriorVariantRegistry.getInstance().get(id);
		}

		@Override
		public JsonElement serialize(ClientExteriorVariantSchema src, Type typeOfSrc, JsonSerializationContext context) {
			return new JsonPrimitive(src.id().toString());
		}
	}
}