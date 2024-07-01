package loqor.ait.tardis;

import com.google.gson.*;
import loqor.ait.core.data.BasicSchema;
import loqor.ait.registry.impl.DesktopRegistry;
import loqor.ait.registry.unlockable.Unlockable;
import loqor.ait.tardis.data.loyalty.Loyalty;
import loqor.ait.tardis.desktops.textures.DesktopPreviewTexture;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.Identifier;

import java.lang.reflect.Type;
import java.util.Optional;

public abstract class TardisDesktopSchema extends BasicSchema implements Unlockable {

	private final Identifier id;
	private final DesktopPreviewTexture preview;
	private final Loyalty loyalty;

	protected TardisDesktopSchema(Identifier id, DesktopPreviewTexture texture, Loyalty loyalty) {
		this.id = id;
		this.preview = texture;
		this.loyalty = loyalty;
	}

	@Override
	public Identifier id() {
		return id;
	}

	@Override
	public Loyalty getRequirement() {
		return loyalty;
	}

	@Override
	public UnlockType unlockType() {
		return UnlockType.DESKTOP;
	}

	public DesktopPreviewTexture previewTexture() {
		return this.preview;
	}

	@Override
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

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (o instanceof TardisDesktopSchema that)
			return id.equals(that.id);

		return false;
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
