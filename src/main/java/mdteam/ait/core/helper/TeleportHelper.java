package mdteam.ait.core.helper;

import com.mojang.logging.LogUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.slf4j.Logger;

import java.util.UUID;

public class TeleportHelper {
    private static final Logger LOGGER = LogUtils.getLogger();
    public final UUID entityUUID;
    public final AbsoluteBlockPos destination;

    public TeleportHelper(UUID uuid, AbsoluteBlockPos destination) {
        this.entityUUID = uuid;
        this.destination = destination;
    }

    public TeleportHelper(UUID uuid, World level, Vec3i destination) {
        this.entityUUID = uuid;
        this.destination = new AbsoluteBlockPos(new BlockPos(destination), level);
    }

    public void teleport(ServerWorld origin) {
        Entity entity = origin.getEntity(this.entityUUID);
        this.destination.getDimension().getChunk(this.destination.toBlockPos());

        if (entity instanceof ServerPlayerEntity player) {
            player.teleport((ServerWorld) this.destination.getDimension(), this.destination.getX() + 0.5, this.destination.getY(), this.destination.getZ() + 0.5,this.destination.getDirection().getHorizontal(),0);
        }
        else if (!(entity instanceof PlayerEntity)) {
            entity.moveToWorld((ServerWorld) this.destination.getDimension());
        }
        LOGGER.info("Teleported " + entity + " to " + this.destination);
    }
}
