package the.mdteam.ait;

import mdteam.ait.core.helper.TeleportUtil;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public record TeleportEntry(Entity entity, ServerWorld targetWorld, Vec3d targetPos, float yaw, float pitch) {

    public TeleportEntry(Entity entity, ServerWorld targetWorld, Vec3d targetPos) {
        this(entity, targetWorld, targetPos, entity.getYaw(), entity.getPitch());
    }

    public void teleport() {
        TeleportUtil.teleport(
                this.entity, this.targetWorld, this.targetPos.getX(),
                this.targetPos.getY(), this.targetPos.getZ(), this.yaw, this.pitch
        );
    }
}
