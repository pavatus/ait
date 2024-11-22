package loqor.ait.core.engine;

import java.lang.reflect.Type;
import java.util.function.Supplier;

import com.google.gson.*;

import loqor.ait.api.Disposable;
import loqor.ait.api.Initializable;
import loqor.ait.api.TardisComponent;
import loqor.ait.client.tardis.ClientTardis;
import loqor.ait.core.engine.impl.DematCircuit;
import loqor.ait.core.engine.impl.EngineSystem;
import loqor.ait.core.engine.impl.LifeSupportCircuit;
import loqor.ait.core.engine.impl.ShieldsCircuit;
import loqor.ait.core.tardis.ServerTardis;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.core.tardis.manager.ServerTardisManager;
import loqor.ait.data.Exclude;
import loqor.ait.data.enummap.Ordered;

public abstract class SubSystem extends Initializable<TardisComponent.InitContext> implements Disposable {
    @Exclude protected Tardis tardis;

    @Exclude(strategy = Exclude.Strategy.NETWORK) private final IdLike id;
    private boolean enabled = false;

    protected SubSystem(IdLike id) {
        this.id = id;
    }

    public IdLike getId() {
        return id;
    }
    public Tardis tardis() {
        return this.tardis;
    }

    public void setTardis(Tardis tardis) {
        this.tardis = tardis;
    }

    public boolean isClient() {
        return this.tardis() instanceof ClientTardis;
    }

    public boolean isServer() {
        return this.tardis() instanceof ServerTardis;
    }

    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

        if (enabled) {
            this.onEnable();
        } else {
            this.onDisable();
        }
    }
    protected void onEnable() {

    }
    protected void onDisable() {

    }

    public void tick() {

    }
    protected void sync() {
        ServerTardisManager.getInstance().markComponentDirty(this.tardis.subsystems());
    }

    @Override
    public void dispose() {
        this.tardis = null;
    }

    public static void init(SubSystem system, Tardis tardis, TardisComponent.InitContext context) {
        if (system == null) return;

        system.setTardis(tardis);
        Initializable.init(system, context);
    }

    public enum Id implements SubSystem.IdLike {
        ENGINE(EngineSystem.class, EngineSystem::new),
        DEMAT(DematCircuit.class, DematCircuit::new),
        LIFE_SUPPORT(LifeSupportCircuit.class, LifeSupportCircuit::new),
        SHIELDS(ShieldsCircuit.class, ShieldsCircuit::new);
        private final Supplier<SubSystem> creator;

        private final Class<? extends SubSystem> clazz;

        private Integer index = null;

        @SuppressWarnings("unchecked")
        <T extends SubSystem> Id(Class<T> clazz, Supplier<T> creator) {
            this.clazz = clazz;
            this.creator = (Supplier<SubSystem>) creator;
        }

        @Override
        public Class<? extends SubSystem> clazz() {
            return clazz;
        }

        @Override
        public SubSystem create() {
            return this.creator.get();
        }

        @Override
        public boolean creatable() {
            return this.creator != null;
        }

        @Override
        public int index() {
            return index;
        }

        @Override
        public void index(int i) {
            this.index = i;
        }

        public static SubSystem.IdLike[] ids() {
            return SubSystem.Id.values();
        }
    }

    public interface IdLike extends Ordered {
        default void set(ClientTardis tardis, SubSystem component) {
            tardis.subsystems().add(component);
        }

        default SubSystem get(ClientTardis tardis) {
            return tardis.subsystems().get(this);
        }

        Class<? extends SubSystem> clazz();

        SubSystem create();

        boolean creatable();

        String name();

        int index();

        void index(int i);
    }

    public static Object serializer() {
        return new Adapter();
    }

    private static class Adapter implements JsonSerializer<SubSystem>, JsonDeserializer<SubSystem> {

        @Override
        public SubSystem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();

            IdLike id = context.deserialize(jsonObject.get("id"), IdLike.class);
            JsonElement element = jsonObject.get("data");

            return context.deserialize(element, id.clazz());
        }

        @Override
        public JsonElement serialize(SubSystem src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject result = new JsonObject();

            result.add("id", context.serialize(src.getId()));
            result.add("data", context.serialize(src, src.getClass()));

            return result;
        }
    }
}
