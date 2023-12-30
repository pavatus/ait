package mdteam.ait.core.item;

import mdteam.ait.core.interfaces.RiftChunk;
import mdteam.ait.core.managers.DeltaTimeManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.UUID;

public class RiftScannerItem extends Item {
    public RiftScannerItem(Settings settings) {
        super(settings);
    }

    public BlockPos targetBlock = null;
    UUID uuid = UUID.randomUUID();


    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            BlockPos currentBlockPos = player.getBlockPos();
            if (targetBlock != null) {
                if (DeltaTimeManager.isStillWaitingOnDelay("riftscanner-" + uuid.toString() + "-checkingdelay")) {
                    DeltaTimeManager.createDelay("riftscanner-" + uuid.toString() + "-checkingdelay", 60000L);
                }
            }

        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack stack = new ItemStack(this);
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putLong("targetBlock", 0L);
        return super.getDefaultStack();
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(world.isClient()) return TypedActionResult.pass(user.getStackInHand(hand));
        if (user.isSneaking()) {
            // reset target
            // @TODO: Readd limit
            user.sendMessage(Text.literal("Target has been reset and updated, the device is now pointing towards your new target"));
            createNewTarget(world, user.getBlockPos());
            ItemStack stack = user.getStackInHand(hand);
            NbtCompound nbt = stack.getOrCreateNbt();
            nbt.putLong("targetBlock", targetBlock.asLong());

        } else {
            RiftChunk riftChunk = (RiftChunk) world.getChunk(user.getBlockPos());
            if (riftChunk.isRiftChunk()) {
                user.sendMessage(Text.literal("Artron Chunk Info: "));
                user.sendMessage(Text.literal("Artron left in chunk: " + riftChunk.getArtronLevels() + "au"));
            } else {
                user.sendMessage(Text.literal("This is not a rift chunk"));
            }
            // do something
        }
        return super.use(world, user, hand);
    }

    /**
     * Searches for a target block with artron levels > 0 within a range.
     * @param world The world object.
     * @param currentBlockPos The current block position.
     */
    private void createNewTarget(World world, BlockPos currentBlockPos) {
        Chunk chunk = world.getChunk(currentBlockPos);
        RiftChunk riftChunk = (RiftChunk) chunk;
        if (riftChunk.isRiftChunk() && riftChunk.getArtronLevels() > 0) {
            targetBlock = chunk.getPos().getBlockPos(0, 0,0);
            return;
        }
        int max_iterations = 50000;
        int i = 0;
        int a_x = chunk.getPos().x;
        int a_z = chunk.getPos().z;
        int b_x = a_x;
        int b_z = a_z;
        int c_x = a_x;
        int c_z = a_z;
        int d_x = a_x;
        int d_z = a_z;
        while (true) {
            i ++;
            a_x ++;
            a_z++;
            b_x--;
            b_z--;
            c_x++;
            c_z--;
            d_x--;
            d_z--;

            BlockPos a_block_pos = new BlockPos(a_x * 16, 0, a_z * 16);
            BlockPos b_block_pos = new BlockPos(b_x * 16, 0, b_z * 16);
            BlockPos c_block_pos = new BlockPos(c_x * 16, 0, c_z * 16);
            BlockPos d_block_pos = new BlockPos(d_x * 16, 0, d_z * 16);
            RiftChunk a_rift_chunk = (RiftChunk) world.getChunk(a_block_pos);
            RiftChunk b_rift_chunk = (RiftChunk) world.getChunk(b_block_pos);
            RiftChunk c_rift_chunk = (RiftChunk) world.getChunk(c_block_pos);
            RiftChunk d_rift_chunk = (RiftChunk) world.getChunk(d_block_pos);
            if (a_rift_chunk.isRiftChunk() && a_rift_chunk.getArtronLevels() > 0) {
                targetBlock = a_block_pos;
                break;
            }
            if (b_rift_chunk.isRiftChunk() && b_rift_chunk.getArtronLevels() > 0) {
                targetBlock = b_block_pos;
                break;
            }
            if (c_rift_chunk.isRiftChunk() && c_rift_chunk.getArtronLevels() > 0) {
                targetBlock = c_block_pos;
                break;
            }
            if (d_rift_chunk.isRiftChunk() && d_rift_chunk.getArtronLevels() > 0) {
                targetBlock = d_block_pos;
                break;
            }
            if (i == max_iterations) {
                break;
            }

        }

    }
}
