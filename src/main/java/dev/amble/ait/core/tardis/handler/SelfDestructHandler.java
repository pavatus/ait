package dev.amble.ait.core.tardis.handler;

import dev.amble.lib.data.CachedDirectedGlobalPos;
import dev.drtheo.scheduler.api.Scheduler;
import dev.drtheo.scheduler.api.TimeUnit;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import dev.amble.ait.AITMod;
import dev.amble.ait.api.KeyedTardisComponent;
import dev.amble.ait.api.TardisTickable;
import dev.amble.ait.core.AITSounds;
import dev.amble.ait.core.tardis.manager.ServerTardisManager;
import dev.amble.ait.core.tardis.util.TardisUtil;
import dev.amble.ait.data.properties.bool.BoolProperty;
import dev.amble.ait.data.properties.bool.BoolValue;

public class SelfDestructHandler extends KeyedTardisComponent implements TardisTickable {

    private static final BoolProperty QUEUED = new BoolProperty("queued");
    private final BoolValue queued = QUEUED.create(this);

    private boolean destructing;

    public SelfDestructHandler() {
        super(Id.SELF_DESTRUCT);
    }

    @Override
    public void onLoaded() {
        queued.of(this, QUEUED);
    }

    public void boom() {
        if (this.isQueued() || !this.canSelfDestruct())
            return;

        this.queued.set(true);
        this.tardis.alarm().enabled().set(true);
    }

    private void complete() {
        CachedDirectedGlobalPos exterior = tardis.travel().position();
        ServerWorld world = exterior.getWorld();
        BlockPos pos = exterior.getPos();

        this.queued.set(false);

        AITMod.LOGGER.warn("Tardis {} has self destructed, expect lag.", tardis.getUuid());
        world.getServer().executeSync(() -> ServerTardisManager.getInstance().remove(world.getServer(), tardis.asServer()));

        world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 50, true,
                World.ExplosionSourceType.MOB);
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

        if (!this.destructing) {
            tardis.getDesktop().startQueue(true);
            Scheduler.get().runTaskLater(this::complete, TimeUnit.SECONDS, 5);

            this.destructing = true;
        }
    }
}
