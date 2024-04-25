package loqor.ait.tardis;

import com.google.gson.*;
import loqor.ait.registry.DesktopRegistry;
import loqor.ait.registry.datapack.Identifiable;
import loqor.ait.registry.datapack.Nameable;
import loqor.ait.tardis.control.impl.DimensionControl;
import loqor.ait.tardis.data.loyalty.Loyalty;
import loqor.ait.tardis.desktops.textures.DesktopPreviewTexture;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.Identifier;

import java.lang.reflect.Type;
import java.util.Optional;

public abstract class TardisDesktopSchema implements Identifiable, Nameable {

	private final Identifier id;
	private final DesktopPreviewTexture preview;
	private final Loyalty loyalty;

	protected TardisDesktopSchema(Identifier id, DesktopPreviewTexture texture, Loyalty loyalty) {
		this.id = id;
		this.preview = texture;
		this.loyalty = loyalty;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (o instanceof TardisDesktopSchema that)
			return id.equals(that.id);

		return false;
	}

	public Identifier id() {
		return id;
	}

	@Override
	public String name() {
		return DimensionControl.convertWorldValueToModified(id().getPath());
	}

	public DesktopPreviewTexture previewTexture() {
		return this.preview;
	}

	public Loyalty getRequirement() {
		return loyalty;
	}

	/**
	 * Decides whether this desktop should be auto-unlocked on creation.
	 * aka - freebee, freeby
	 */
	public boolean freebie() {
		return true;
	}

	public Optional<StructureTemplate> findTemplate() {
		return ((ServerWorld) TardisUtil.getTardisDimension()).getStructureTemplateManager().getTemplate(this.getStructureLocation());
	}

	private Identifier getStructureLocation() {
		Identifier id = this.id();

		return new Identifier(
				id.getNamespace(), "interiors/" + id.getPath()
		);
	}

	public static Object serializer() {
		return new Serializer();
	}

	private static class Serializer implements JsonSerializer<TardisDesktopSchema>, JsonDeserializer<TardisDesktopSchema> {

		@Override
		public TardisDesktopSchema deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			return DesktopRegistry.getInstance().get(new Identifier(json.getAsJsonPrimitive().getAsString()));
		}

		@Override
		public JsonElement serialize(TardisDesktopSchema src, Type typeOfSrc, JsonSerializationContext context) {
			return new JsonPrimitive(src.id().toString());
		}
	}
}
