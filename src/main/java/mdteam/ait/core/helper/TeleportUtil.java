package mdteam.ait.core.helper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;

public class TeleportUtil {

    public static void teleport(Entity entity, ServerWorld targetWorld, double x, double y, double z, float yaw, float pitch) {
        yaw = MathHelper.wrapDegrees(yaw);
        pitch = MathHelper.wrapDegrees(pitch);

        if ((entity instanceof ServerPlayerEntity player)) {
            handlePlayerTeleport(player, targetWorld, x, y, z, yaw, pitch);
        } else {
            handleNonPlayerTeleport(entity, targetWorld, x, y, z, yaw, pitch);
        }

        if (!(entity instanceof LivingEntity living) || !living.isFallFlying()) {
            entity.setVelocity(entity.getVelocity().multiply(1.0D, 0.0D, 1.0D));
            entity.setOnGround(true);
        }

        if (entity instanceof PathAwareEntity pathAware) {
            pathAware.getNavigation().stop();
        }
    }

    private static void handlePlayerTeleport(ServerPlayerEntity player, ServerWorld targetWorld, double x, double y, double z, float yaw, float pitch) {
        BlockPos pos = new BlockPos((int) x, (int) y, (int) z);

        System.out.println(pos);

        targetWorld.getChunkManager().addTicket(ChunkTicketType.POST_TELEPORT,
                new ChunkPos(pos), 1, player.getId()
        );

        System.out.println(pos);

        player.stopRiding();
        if (player.isSleeping()) {
            player.wakeUp(true, true);
        }

        if (player.getWorld() == targetWorld) {
            player.networkHandler.requestTeleport(x, y, z, yaw, pitch);
        } else {
            player.teleport(targetWorld, x, y, z, yaw, pitch);
        }

        player.setYaw(yaw);
    }

    private static void handleNonPlayerTeleport(Entity entity, ServerWorld targetWorld, double x, double y, double z, float yaw, float pitch) {
        pitch = MathHelper.clamp(pitch, -90.0F, 90.0F);

        if (entity.getWorld() == targetWorld) {
            entity.refreshPositionAndAngles(x, y, z, yaw, pitch);
            entity.setYaw(yaw);
            return;
        }

        entity.stopRiding();

        Entity originalEntity = entity;
        entity = entity.getType().create(targetWorld);

        if (entity == null) {
            return;
        }

        entity.copyFrom(originalEntity);
        entity.refreshPositionAndAngles(x, y, z, yaw, pitch);
        entity.setYaw(yaw);

        originalEntity.setRemoved(Entity.RemovalReason.CHANGED_DIMENSION);
        targetWorld.spawnEntity(originalEntity);
    }
}
