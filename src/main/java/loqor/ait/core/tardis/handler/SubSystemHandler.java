package loqor.ait.core.tardis.handler;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import com.google.gson.*;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.server.MinecraftServer;

import loqor.ait.AITMod;
import loqor.ait.api.KeyedTardisComponent;
import loqor.ait.api.TardisEvents;
import loqor.ait.api.TardisTickable;
import loqor.ait.core.engine.DurableSubSystem;
import loqor.ait.core.engine.SubSystem;
import loqor.ait.core.engine.impl.*;
import loqor.ait.core.engine.registry.SubSystemRegistry;
import loqor.ait.data.Exclude;
import loqor.ait.data.enummap.EnumMap;

public class SubSystemHandler extends KeyedTardisComponent implements TardisTickable {
    @Exclude
    private final EnumMap<SubSystem.IdLike, SubSystem> systems = new EnumMap<>(SubSystemRegistry::values,
            SubSystem[]::new);

    static {
        TardisEvents.OUT_OF_FUEL.register(tardis -> tardis.fuel().disablePower());
    }

    public SubSystemHandler() {
        super(Id.SUBSYSTEM);
    }


    @Override
    public void onCreate() {
        super.onCreate();

        SubSystemRegistry.getInstance().fill(this::create);
    }

    @Override
    protected void onInit(InitContext ctx) {
        super.onInit(ctx);

        this.forEach(component -> SubSystem.init(component, this.tardis, ctx));
    }

    public <T extends SubSystem> T get(SubSystem.IdLike id) {
        if (!(this.systems.containsKey(id))) {
            AITMod.LOGGER.info("Creating subsystem: {} | {}", id, tardis());
            this.add(this.create(id));
        }

        return (T) this.systems.get(id);
    }

    public SubSystem add(SubSystem system) {
        this.systems.put(system.getId(), system);
        this.sync();
        return system;
    }
    private SubSystem remove(SubSystem.IdLike id) {
        SubSystem found = this.systems.remove(id);
        this.sync();
        return found;
    }
    private Iterator<SubSystem> iterator() {
        return Arrays.stream(this.systems.getValues()).iterator();
    }
    private void forEach(Consumer<SubSystem> consumer) {
        for (SubSystem system : this.systems.getValues()) {
            if (system == null)
                continue;

            consumer.accept(system);
        }
    }

    private SubSystem create(SubSystem.IdLike id) {
        SubSystem system = id.create();
        if (tardis != null)
            SubSystem.init(system, this.tardis, InitContext.createdAt(this.tardis.travel().position()));
        return system;
    }
    private void create(SubSystem subSystem) {
        this.systems.put(subSystem.getId(), subSystem);
        if (this.tardis != null)
            SubSystem.init(subSystem, this.tardis, InitContext.createdAt(this.tardis.travel().position()));
    }


    /**
     * @return true if all subsystems are enabled
     */
    public boolean isEnabled() {
        for (Iterator<SubSystem> it = this.iterator(); it.hasNext(); ) {
            SubSystem i = it.next();

            if (!i.isEnabled())
                return false;
        }

        return true;
    }
    public int count() {
        return this.systems.size();
    }
    public int countEnabled() {
        int count = 0;

        for (Iterator<SubSystem> it = this.iterator(); it.hasNext(); ) {
            if (it.next().isEnabled())
                count++;
        }

        return count;
    }

    @Override
    public void tick(MinecraftServer server) {
        for (Iterator<SubSystem> it = this.iterator(); it.hasNext(); ) {
            SubSystem next = it.next();
            if (next == null) return;
            next.tick();
        }
    }

    public Optional<DurableSubSystem> findBrokenSubsystem() {
        for (Iterator<SubSystem> it = this.iterator(); it.hasNext(); ) {
            SubSystem next = it.next();
            if (next instanceof DurableSubSystem && next.isEnabled() && ((DurableSubSystem) next).durability() <= 5)
                return Optional.of((DurableSubSystem) next);
        }

        return Optional.empty();
    }

    public void repairAll() {
        AITMod.LOGGER.info("Repairing all subsystems for {}", this.tardis);
        for (Iterator<SubSystem> it = this.iterator(); it.hasNext(); ) {
            SubSystem next = it.next();
            if (next == null) continue;
            if (next instanceof DurableSubSystem)
                ((DurableSubSystem) next).addDurability(100);
            next.setEnabled(true);
        }
    }

    public EngineSystem engine() {
        return this.get(SubSystem.Id.ENGINE);
    }
    public DematCircuit demat() {
        return this.get(SubSystem.Id.DEMAT);
    }

    public LifeSupportCircuit lifeSupport() {
        return this.get(SubSystem.Id.LIFE_SUPPORT);
    }

    public ShieldsCircuit shields() {
        return this.get(SubSystem.Id.SHIELDS);
    }
    public EmergencyPower emergency() {
        return this.get(SubSystem.Id.EMERGENCY_POWER);
    }
    public Stabilisers stabilisers() {
        return this.get(SubSystem.Id.STABILISERS);
    }
    public ChameleonCircuit chameleon() {
        return this.get(SubSystem.Id.CHAMELEON);
    }

    @ApiStatus.Internal
    public <T extends SubSystem> void set(T t) {
        this.systems.put(t.getId(), t);
    }

    public static Object serializer() {
        return new Serializer();
    }

    static class Serializer implements JsonSerializer<SubSystemHandler>, JsonDeserializer<SubSystemHandler> {

        @Override
        public SubSystemHandler deserialize(JsonElement json, java.lang.reflect.Type type,
                                                 JsonDeserializationContext context) throws JsonParseException {
            SubSystemHandler manager = new SubSystemHandler();
            Map<String, JsonElement> map = json.getAsJsonObject().asMap();

            SubSystemRegistry registry = SubSystemRegistry.getInstance();

            for (Map.Entry<String, JsonElement> entry : map.entrySet()) {
                String key = entry.getKey();
                JsonElement element = entry.getValue();

                SubSystem.IdLike id = registry.get(key);

                if (id == null) {
                    AITMod.LOGGER.error("Can't find a subsystem id with name '{}'!", key);
                    continue;
                }

                manager.set(context.deserialize(element, id.clazz()));
            }

            for (int i = 0; i < manager.systems.size(); i++) {
                if (manager.systems.get(i) != null)
                    continue;

                SubSystem.IdLike id = registry.get(i);
                AITMod.LOGGER.debug("Appending new subsystem {}", id);

                manager.get(id);
            }

            return manager;
        }

        @Override
        public JsonElement serialize(SubSystemHandler manager, java.lang.reflect.Type type,
                                     JsonSerializationContext context) {
            JsonObject result = new JsonObject();

            manager.forEach(component -> {
                SubSystem.IdLike idLike = component.getId();

                if (idLike == null) {
                    AITMod.LOGGER.error("Id was null for {}", component.getClass());
                    return;
                }

                result.add(idLike.name(), context.serialize(component));
            });

            return result;
        }
    }
}