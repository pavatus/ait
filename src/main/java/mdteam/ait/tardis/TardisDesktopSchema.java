package mdteam.ait.tardis;

import com.google.gson.*;
import mdteam.ait.core.AITDesktops;
import mdteam.ait.tardis.util.TardisUtil;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.Identifier;

import java.lang.reflect.Type;
import java.util.Optional;

public abstract class TardisDesktopSchema {

    private final Identifier id;

    public TardisDesktopSchema(Identifier id) {
        this.id = id;
    }

    public Identifier id() {
        return id;
    }

    public Optional<StructureTemplate> findTemplate() {
        return TardisUtil.getTardisDimension().getStructureTemplateManager().getTemplate(this.getStructureLocation());
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
            return AITDesktops.get(new Identifier(json.getAsJsonPrimitive().getAsString()));
        }

        @Override
        public JsonElement serialize(TardisDesktopSchema src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.id().toString());
        }
    }
}
