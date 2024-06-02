package loqor.ait.registry.impl;

import loqor.ait.registry.Registry;
import loqor.ait.tardis.base.TardisComponent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class TardisComponentRegistry implements Registry {

    private static final TardisComponentRegistry instance = new TardisComponentRegistry();

    private static final Map<String, TardisComponent.IdLike> REGISTRY = new HashMap<>();

    public static void register(TardisComponent.IdLike id) {
        if (!id.creatable())
            return;

        id.index(REGISTRY.size());
        REGISTRY.put(id.name(), id);
    }

    public static void register(TardisComponent.IdLike[] idLikes) {
        for (TardisComponent.IdLike idLike : idLikes) {
            register(idLike);
        }
    }

    @Override
    public void onCommonInit() {
        register(TardisComponent.Id.ids());
    }

    public void fill(Consumer<TardisComponent> consumer) {
        for (TardisComponent.IdLike id : REGISTRY.values()) {
            consumer.accept(id.create());
        }
    }

    public static TardisComponent.IdLike[] values() {
        return REGISTRY.values().toArray(new TardisComponent.IdLike[0]);
    }

    public static TardisComponentRegistry getInstance() {
        return instance;
    }
}
