package loqor.ait.core.tardis.handler;

import org.joml.Vector2i;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;

import loqor.ait.api.KeyedTardisComponent;
import loqor.ait.api.TardisEvents;
import loqor.ait.core.AITBlocks;
import loqor.ait.core.AITSounds;
import loqor.ait.core.blocks.ExteriorBlock;
import loqor.ait.core.engine.impl.EngineSystem;
import loqor.ait.core.tardis.handler.travel.TravelHandler;
import loqor.ait.core.util.StackUtil;
import loqor.ait.data.DirectedGlobalPos;
import loqor.ait.data.properties.Property;
import loqor.ait.data.properties.Value;
import loqor.ait.data.properties.bool.BoolProperty;
import loqor.ait.data.properties.bool.BoolValue;
@Deprecated(forRemoval = true, since = "1.2.0") // todo to be moved to SubSystemHandler
public class EngineHandler extends KeyedTardisComponent {

    private static final Vector2i ZERO = new Vector2i();

    private static final BoolProperty POWER = new BoolProperty("power", false);
    private static final Property<Vector2i> ENGINE_CORE_POS = new Property<>(Property.Type.VEC2I, "engine_core_pos",
            (Vector2i) null);

    private final BoolValue power = POWER.create(this);
    private final Value<Vector2i> engineCorePos = ENGINE_CORE_POS.create(this);

    public EngineHandler() {
        super(Id.ENGINE);
    }

    static {
        TardisEvents.OUT_OF_FUEL.register(tardis -> tardis.engine().disablePower());

        TardisEvents.RECONFIGURE_DESKTOP.register(tardis -> {
            if (!tardis.engine().hasEngineCore())
                return;

            DirectedGlobalPos.Cached cached = tardis.travel().position();

            StackUtil.spawn(cached.getWorld(), cached.getPos().up(), new ItemStack(AITBlocks.ENGINE_CORE_BLOCK));
        });
    }

    @Override
    public void onLoaded() {
        power.of(this, POWER);
        engineCorePos.of(this, ENGINE_CORE_POS);
    }

    @Deprecated
    public boolean hasEngineCore() {
        return engineCorePos.get() != null;
    }

    @Deprecated
    public Vector2i getCorePos() {
        Vector2i result = engineCorePos.get();
        return result != null ? result : ZERO;
    }

    @Deprecated
    public void linkEngine(int x, int z) {
        engineCorePos.set(new Vector2i(x, z));
    }

    @Deprecated
    public void unlinkEngine() {
        engineCorePos.set((Vector2i) null);
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

    public void enablePower() {
        if (this.power.get())
            return;

        if (this.tardis.getFuel() <= (0.01 * FuelHandler.TARDIS_MAX_FUEL))
            return; // cant enable power if not enough fuel
        if (this.tardis.siege().isActive()) return;
        if (!EngineSystem.hasEngine(tardis)) return;

        this.power.set(true);
        this.updateExteriorState();

        this.tardis.getDesktop().playSoundAtEveryConsole(AITSounds.POWERUP, SoundCategory.AMBIENT, 10f, 1f);
        TardisEvents.REGAIN_POWER.invoker().onRegainPower(this.tardis);
    }

    // why is this in the engine handler? - Loqor
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
}
