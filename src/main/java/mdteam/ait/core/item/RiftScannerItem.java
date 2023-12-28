package mdteam.ait.core.item;

import mdteam.ait.core.interfaces.RiftChunk;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class RiftScannerItem extends Item {
    public RiftScannerItem(Settings settings) {
        super(settings);
    }

    BlockPos targetBlock = null;

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;

        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (user.isSneaking()) {
            // reset target
            user.sendMessage(Text.literal("Target has been reset and updated, the device is now pointing towards your new target"));

        } else {
            // do something
        }
        return super.use(world, user, hand);
    }

    private void createNewTarget(World world, BlockPos currentBlockPos) {
        Chunk chunk = world.getChunk(currentBlockPos);
        RiftChunk riftChunk = (RiftChunk) chunk;
        if (riftChunk.isRiftChunk() && riftChunk.getArtronLevels() > 0) {
            targetBlock = chunk.getPos().getBlockPos(0, 0,0);
        } else {
            int max_iterations = 50000;
            int i = 0;
            while (true) {

            }
        }
    }
}
