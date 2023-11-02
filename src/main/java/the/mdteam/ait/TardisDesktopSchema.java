package the.mdteam.ait;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import mdteam.ait.core.AITDesktops;
import mdteam.ait.core.helper.TardisUtil;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonSerializer;

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

    public static class Serializer implements JsonDeserializer<TardisDesktopSchema> {

        @Override
        public TardisDesktopSchema deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return AITDesktops.get(context.deserialize(
                    json.getAsJsonObject().getAsJsonObject("id"), Identifier.class)
            );
        }
    }
}
