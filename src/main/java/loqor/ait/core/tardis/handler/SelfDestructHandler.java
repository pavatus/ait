package loqor.ait.core.tardis.handler;

import dev.drtheo.blockqueue.data.TimeUnit;
import dev.drtheo.scheduler.Scheduler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import loqor.ait.AITMod;
import loqor.ait.api.KeyedTardisComponent;
import loqor.ait.api.TardisTickable;
import loqor.ait.core.AITSounds;
import loqor.ait.core.tardis.manager.ServerTardisManager;
import loqor.ait.core.tardis.util.TardisUtil;
import loqor.ait.core.util.ServerLifecycleHooks;
import loqor.ait.data.DirectedGlobalPos;
import loqor.ait.data.properties.bool.BoolProperty;
import loqor.ait.data.properties.bool.BoolValue;


public class SelfDestructHandler extends KeyedTardisComponent implements TardisTickable {

    private static final BoolProperty QUEUED = new BoolProperty("queued");
    private final BoolValue queued = QUEUED.create(this);
    private static final BoolProperty REGENERATING = new BoolProperty("regenerating");
    private final BoolValue regenerating = REGENERATING.create(this);

    public SelfDestructHandler() {
        super(Id.SELF_DESTRUCT);
    }

    @Override
    public void onLoaded() {
        queued.of(this, QUEUED);
        regenerating.of(this, REGENERATING);
    }

    public void boom() {
        if ((this.isQueued()) || !this.canSelfDestruct()) return;

        this.queued.set(true);
        this.tardis.alarm().enabled().set(true);
    }

    private void complete() {
        DirectedGlobalPos.Cached exterior = tardis.travel().position();
        ServerWorld world = exterior.getWorld();
        BlockPos pos = exterior.getPos();

        this.queued.set(false);

        world.spawnParticles(ParticleTypes.EXPLOSION_EMITTER, pos.getX(), pos.getY(), pos.getZ(), 10, 1, 1, 1, 1);
        world.spawnParticles(ParticleTypes.CLOUD, pos.getX(), pos.getY(), pos.getZ(), 100, 1, 1, 1, 1);
        world.spawnParticles(ParticleTypes.LARGE_SMOKE, pos.getX(), pos.getY(), pos.getZ(), 250, 1, 1, 1, 1);
        world.spawnParticles(ParticleTypes.FLAME, pos.getX(), pos.getY(), pos.getZ(), 50, 1, 1, 1, 1);
        world.spawnParticles(ParticleTypes.SOUL_FIRE_FLAME, pos.getX(), pos.getY(), pos.getZ(), 25, 1, 1, 1, 1);
        world.spawnParticles(ParticleTypes.SMALL_FLAME, pos.getX(), pos.getY(), pos.getZ(), 10, 1, 1, 1, 1);
        world.spawnParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, pos.getX(), pos.getY(), pos.getZ(), 500, 1, 1, 1, 1);
        world.playSound(null, pos, AITSounds.GROAN, SoundCategory.BLOCKS, 10f, 0.7f);

        ServerTardisManager.getInstance().remove(world.getServer(), tardis.asServer());
    }

    public boolean isQueued() {
        return queued.get();
    }

    private boolean canSelfDestruct() {
        return tardis.travel().isLanded();
    }

    private void warnPlayers() {
        for (PlayerEntity player : TardisUtil.getPlayersInsideInterior(this.tardis.asServer())) {
            player.sendMessage(Text.translatable("tardis.message.self_destruct.warning").formatted(Formatting.RED),
                    true);
        }
    }

    @Override
    public void tick(MinecraftServer server) {
        if (!this.isQueued())
            return;

        if (!this.canSelfDestruct()) {
            this.queued.set(false);

            tardis.alarm().enabled().set(false);
            return;
        }

        if (!TardisUtil.isInteriorEmpty(tardis.asServer())) {
            warnPlayers();
            return;
        }

        if (!tardis.door().locked())
            DoorHandler.lockTardis(true, this.tardis, null, true);

        if (tardis.asServer().isRemoved())
            return;

        if (!(this.regenerating.get())) {
            Scheduler.get().runAsyncTaskLater(this::complete, TimeUnit.SECONDS, 5);

            this.regenerating.set(true);
        }
    }
}
