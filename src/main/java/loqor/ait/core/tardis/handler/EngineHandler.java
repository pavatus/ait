package loqor.ait.tardis.handler;

import java.util.Random;

import org.joml.Vector2i;

import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;

import loqor.ait.AITMod;
import loqor.ait.api.KeyedTardisComponent;
import loqor.ait.api.TardisEvents;
import loqor.ait.api.TardisTickable;
import loqor.ait.core.AITBlocks;
import loqor.ait.core.AITSounds;
import loqor.ait.core.blocks.ExteriorBlock;
import loqor.ait.core.util.DeltaTimeManager;
import loqor.ait.core.util.StackUtil;
import loqor.ait.core.util.TimeUtil;
import loqor.ait.data.DirectedGlobalPos;
import loqor.ait.tardis.handler.properties.Property;
import loqor.ait.tardis.handler.properties.Value;
import loqor.ait.tardis.handler.properties.bool.BoolProperty;
import loqor.ait.tardis.handler.properties.bool.BoolValue;
import loqor.ait.tardis.handler.travel.TravelHandler;
import loqor.ait.tardis.handler.travel.TravelHandlerBase;
import loqor.ait.tardis.util.TardisUtil;

public class EngineHandler extends KeyedTardisComponent implements TardisTickable {

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

    public boolean hasEngineCore() {
        return engineCorePos.get() != null;
    }

    public Vector2i getCorePos() {
        Vector2i result = engineCorePos.get();
        return result != null ? result : ZERO;
    }

    public void linkEngine(int x, int z) {
        engineCorePos.set(new Vector2i(x, z));
    }

    public void unlinkEngine() {
        engineCorePos.set((Vector2i) null);
        this.disablePower();
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

        DeltaTimeManager.createDelay(AITMod.MOD_ID + "-driftingmusicdelay",
                (long) TimeUtil.secondsToMilliseconds(new Random().nextInt(1, 360)));

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

        this.tardis.siege().setActive(false);

        if (!this.hasEngineCore())
            return;

        this.power.set(true);
        this.updateExteriorState();

        this.tardis.getDesktop().playSoundAtEveryConsole(AITSounds.POWERUP, SoundCategory.AMBIENT, 10f, 1f);
        TardisEvents.REGAIN_POWER.invoker().onRegainPower(this.tardis);
    }

    private void updateExteriorState() {
        TravelHandler travel = this.tardis.travel();

        if (travel.getState() != TravelHandlerBase.State.LANDED)
            return;

        DirectedGlobalPos.Cached pos = travel.position();
        World world = pos.getWorld();

        if (world == null)
            return;

        world.setBlockState(pos.getPos(),
                world.getBlockState(pos.getPos()).with(ExteriorBlock.LEVEL_9, this.power.get() ? 9 : 0));
    }

    @Override
    public void tick(MinecraftServer server) {
        // most of the logic is in the handlers, so we can just disable them if we're a
        // growth
        if (!this.hasPower() && !DeltaTimeManager.isStillWaitingOnDelay(AITMod.MOD_ID + "-driftingmusicdelay")) {
            for (ServerPlayerEntity player : TardisUtil.getPlayersInsideInterior(this.tardis.asServer())) {
                player.playSound(AITSounds.DRIFTING_MUSIC, SoundCategory.MUSIC, 1, 1);
            }

            DeltaTimeManager.createDelay(AITMod.MOD_ID + "-driftingmusicdelay",
                    (long) TimeUtil.minutesToMilliseconds(new Random().nextInt(7, 9)));
        }
    }
}
