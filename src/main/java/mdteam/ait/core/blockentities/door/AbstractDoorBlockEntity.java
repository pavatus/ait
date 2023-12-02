package mdteam.ait.core.blockentities.door;

import mdteam.ait.core.blocks.types.HorizontalDirectionalBlock;
import mdteam.ait.tardis.ITardis;
import mdteam.ait.tardis.TardisDoor;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.linkable.LinkableBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public abstract class AbstractDoorBlockEntity extends LinkableBlockEntity {

    private TardisDoor door;

    public AbstractDoorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void useOn(World world, PlayerEntity player) {
        if (this.getTravel().getState() != TardisTravel.State.LANDED)
            return;

        this.door.next();
        if (this.door.isLocked()) {
            world.playSound(null, pos, SoundEvents.BLOCK_CHAIN_STEP, SoundCategory.BLOCKS, 0.6F, 1F);
            player.sendMessage(Text.literal("TARDIS is locked!"), true);
        } else {
            world.playSound(null, pos, SoundEvents.BLOCK_IRON_DOOR_OPEN, SoundCategory.BLOCKS, 0.6f, 1f);
        }
    }

    public Direction getFacing() {
        return this.getCachedState().get(HorizontalDirectionalBlock.FACING);
    }

    public void onEntityCollision(Entity entity) {
        if (!(entity instanceof ServerPlayerEntity player))
            return;

        if (this.door.getState() != TardisDoor.State.CLOSED) {
            this.teleport(player);
        }
    }

    protected abstract void teleport(Entity entity);

    @Override
    public void setTardis(ITardis tardis) {
        super.setTardis(tardis);
        this.linkDoor();
    }

    @Override
    public void setDoor(TardisDoor door) {
        this.door = door;
    }
}
