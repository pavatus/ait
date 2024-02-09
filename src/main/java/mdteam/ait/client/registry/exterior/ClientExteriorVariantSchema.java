package mdteam.ait.client.registry.exterior;

import com.google.gson.*;
import mdteam.ait.AITMod;
import mdteam.ait.client.models.exteriors.ExteriorModel;
import mdteam.ait.client.registry.ClientExteriorVariantRegistry;
import mdteam.ait.registry.ExteriorVariantRegistry;
import mdteam.ait.registry.datapack.Identifiable;
import mdteam.ait.tardis.exterior.variant.ExteriorVariantSchema;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;

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
        if (o == null || getClass() == null) return false;
        if (!(o instanceof ClientExteriorVariantSchema that)) return false;

        return id.equals(that.id);
    }

    public ExteriorVariantSchema parent() { return ExteriorVariantRegistry.getInstance().get(this.parent); }
    public Identifier id() { return id; }
    public abstract Identifier texture();
    public abstract Identifier emission();
    public abstract ExteriorModel model();

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