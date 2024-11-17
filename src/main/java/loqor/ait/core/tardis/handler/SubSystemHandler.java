package loqor.ait.core.tardis.handler;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;

import net.minecraft.block.BlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;

import loqor.ait.AITMod;
import loqor.ait.api.KeyedTardisComponent;
import loqor.ait.api.TardisEvents;
import loqor.ait.api.TardisTickable;
import loqor.ait.core.AITSounds;
import loqor.ait.core.blocks.ExteriorBlock;
import loqor.ait.core.engine.DurableSubSystem;
import loqor.ait.core.engine.SubSystem;
import loqor.ait.core.engine.impl.EngineSystem;
import loqor.ait.core.engine.registry.SubSystemRegistry;
import loqor.ait.core.tardis.handler.travel.TravelHandler;
import loqor.ait.data.DirectedGlobalPos;
import loqor.ait.data.enummap.EnumMap;
import loqor.ait.data.properties.bool.BoolProperty;
import loqor.ait.data.properties.bool.BoolValue;

public class SubSystemHandler extends KeyedTardisComponent implements TardisTickable {
    private static final BoolProperty POWER = new BoolProperty("power", false);
    private final BoolValue power = POWER.create(this);

    private final EnumMap<SubSystem.IdLike, SubSystem> systems = new EnumMap<>(SubSystemRegistry::values,
            SubSystem[]::new);

    static {
        TardisEvents.OUT_OF_FUEL.register(tardis -> tardis.subsystems().disablePower());
    }

    public SubSystemHandler() {
        super(Id.SUBSYSTEM);
    }

    @Override
    protected void onInit(InitContext ctx) {
        super.onInit(ctx);

        this.iterator().forEachRemaining(i -> SubSystem.init(i, this.tardis, ctx));
    }

    @Override
    public void onLoaded() {
        super.onLoaded();

        power.of(this, POWER);
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
            if (next instanceof DurableSubSystem && ((DurableSubSystem) next).durability() <= 5)
                return Optional.of((DurableSubSystem) next);
        }

        return Optional.empty();
    }

    public boolean hasPower() {
        return power.get();
    }

    public void togglePower() {
        if (this.power.get()) {
            this.disablePower();
        } else {
            this.enablePower();
        }
    }

    public void disablePower() {
        if (!this.power.get())
            return;

        this.power.set(false);
        this.updateExteriorState();

        TardisEvents.LOSE_POWER.invoker().onLosePower(this.tardis);
        this.disableProtocols();
    }

    private void disableProtocols() {
        tardis.getDesktop().playSoundAtEveryConsole(AITSounds.SHUTDOWN, SoundCategory.AMBIENT, 10f, 1f);

        // disabling protocols
        tardis.travel().antigravs().set(false);
        tardis.stats().hailMary().set(false);
        tardis.<HadsHandler>handler(Id.HADS).enabled().set(false);
    }

    public void enablePower(boolean requiresEngine) {
        if (this.power.get())
            return;

        if (this.tardis.getFuel() <= (0.01 * FuelHandler.TARDIS_MAX_FUEL))
            return; // cant enable power if not enough fuel
        if (this.tardis.siege().isActive()) return;
        if (requiresEngine && !EngineSystem.hasEngine(tardis)) return;

        this.power.set(true);
        this.updateExteriorState();

        this.tardis.getDesktop().playSoundAtEveryConsole(AITSounds.POWERUP, SoundCategory.AMBIENT, 10f, 1f);
        TardisEvents.REGAIN_POWER.invoker().onRegainPower(this.tardis);
    }
    public void enablePower() {
        this.enablePower(true);
    }

    // why is this in the engine handler? - Loqor
    // idk, but i moved it here still - duzo
    private void updateExteriorState() {
        TravelHandler travel = this.tardis.travel();

        if (travel.getState() != TravelHandler.State.LANDED)
            return;

        DirectedGlobalPos.Cached pos = travel.position();
        World world = pos.getWorld();

        if (world == null)
            return;
        BlockState state = world.getBlockState(pos.getPos());
        if (!(state.getBlock() instanceof ExteriorBlock))
            return;

        world.setBlockState(pos.getPos(),
                state.with(ExteriorBlock.LEVEL_9, this.power.get() ? 9 : 0));
    }

    public EngineSystem engine() {
        return this.get(SubSystem.Id.ENGINE);
    }
}
