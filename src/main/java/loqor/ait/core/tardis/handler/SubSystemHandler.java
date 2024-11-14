package loqor.ait.core.tardis.handler;

import java.util.Arrays;
import java.util.Iterator;

import loqor.ait.AITMod;
import loqor.ait.api.KeyedTardisComponent;
import loqor.ait.core.engine.SubSystem;
import loqor.ait.core.engine.impl.EngineSystem;
import loqor.ait.core.engine.registry.SubSystemRegistry;
import loqor.ait.data.enummap.EnumMap;

public class SubSystemHandler extends KeyedTardisComponent {
    private final EnumMap<SubSystem.IdLike, SubSystem> systems = new EnumMap<>(SubSystemRegistry::values,
            SubSystem[]::new);

    public SubSystemHandler() {
        super(Id.SUBSYSTEM);
    }

    @Override
    protected void onInit(InitContext ctx) {
        super.onInit(ctx);

        this.iterator().forEachRemaining(i -> SubSystem.init(i, this.tardis, ctx));
    }

    public <T extends SubSystem> T get(SubSystem.IdLike id) {
        SubSystem found = this.systems.get(id);

        if (found == null) {
            AITMod.LOGGER.info("Creating subsystem: {} | {}", id, tardis().getUuid());
            found = this.add(this.create(id));
        }

        return (T) found;
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
    private SubSystem create(SubSystem.IdLike id) {
        SubSystem system = id.create();
        SubSystem.init(system, this.tardis, InitContext.createdAt(this.tardis.travel().position()));
        return system;
    }

    public EngineSystem engine() {
        return this.get(SubSystem.Id.ENGINE);
    }
}
