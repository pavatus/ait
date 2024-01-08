package mdteam.ait.tardis;

import com.google.gson.*;
import mdteam.ait.registry.DesktopRegistry;
import mdteam.ait.tardis.control.impl.DimensionControl;
import mdteam.ait.tardis.desktops.textures.DesktopPreviewTexture;
import mdteam.ait.tardis.util.TardisUtil;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.Identifier;

import java.lang.reflect.Type;
import java.util.Optional;

public abstract class TardisDesktopSchema {
    private final Identifier id;
    private final DesktopPreviewTexture preview;

    public TardisDesktopSchema(Identifier id, DesktopPreviewTexture texture) {
        this.id = id;
        this.preview = texture;
    }
    public TardisDesktopSchema(Identifier id) {
        this(id, new DesktopPreviewTexture(id));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() == null) return false;

        TardisDesktopSchema that = (TardisDesktopSchema) o;

        return id.equals(that.id);
    }

    public Identifier id() {
        return id;
    }
    public String name() { return DimensionControl.convertWorldValueToModified(id().getPath());} // temp ish
    public DesktopPreviewTexture previewTexture() { return this.preview; }

    /**
     * Decides whether this desktop should be auto-unlocked on creation.
     * aka - freebee, freeby
     */
    public boolean freebie() { return true; }

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
            return DesktopRegistry.get(new Identifier(json.getAsJsonPrimitive().getAsString()));
        }

        @Override
        public JsonElement serialize(TardisDesktopSchema src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.id().toString());
        }
    }
}
