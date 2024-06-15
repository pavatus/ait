package loqor.ait.registry.impl;

import com.google.gson.*;
import loqor.ait.AITMod;
import loqor.ait.registry.Registry;
import loqor.ait.tardis.base.TardisComponent;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class TardisComponentRegistry implements Registry {

    private static final TardisComponentRegistry instance = new TardisComponentRegistry();

    private final Map<String, TardisComponent.IdLike> REGISTRY = new HashMap<>();
    private TardisComponent.IdLike[] LOOKUP;

    private boolean frozen = false;

    public void register(TardisComponent.IdLike id) {
        if (!id.creatable())
            return;

        id.index(REGISTRY.size());
        REGISTRY.put(id.name(), id);

        if (frozen)
            AITMod.LOGGER.error("Tried to register a component id after the registry got frozen: {}", id);
    }

    public void register(TardisComponent.IdLike[] idLikes) {
        for (TardisComponent.IdLike idLike : idLikes) {
            register(idLike);
        }
    }

    @Override
    public void onCommonInit() {
        register(TardisComponent.Id.ids());

        LOOKUP = new TardisComponent.IdLike[REGISTRY.size()];
        REGISTRY.forEach((name, idLike) -> LOOKUP[idLike.index()] = idLike);

        this.frozen = true;
    }

    public void fill(Consumer<TardisComponent> consumer) {
        for (TardisComponent.IdLike id : LOOKUP) {
            consumer.accept(id.create());
        }
    }

    public TardisComponent.IdLike get(String name) {
        return REGISTRY.get(name);
    }

    public TardisComponent.IdLike get(int index) {
        return LOOKUP[index];
    }

    public static TardisComponent.IdLike[] values() {
        return instance.LOOKUP;
    }

    public static TardisComponentRegistry getInstance() {
        return instance;
    }

    public static Object idSerializer() {
        return new Serializer();
    }

    private static class Serializer implements JsonSerializer<TardisComponent.IdLike>, JsonDeserializer<TardisComponent.IdLike> {

        @Override
        public TardisComponent.IdLike deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return TardisComponentRegistry.getInstance().get(json.getAsString());
        }

        @Override
        public JsonElement serialize(TardisComponent.IdLike src, Type typeOfSrc, JsonSerializationContext context) {
            return context.serialize(src.name());
        }
    }
}
