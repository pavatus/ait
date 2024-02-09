package mdteam.ait.core.item;

import com.neptunedevelopmentteam.neptunelib.utils.DeltaTimeManager;
import mdteam.ait.core.managers.RiftChunkManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class RiftScannerItem extends Item {
    private static final int MAX_ITERATIONS = 32;

    public RiftScannerItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(world.isClient()) return TypedActionResult.pass(user.getStackInHand(hand));

        if (!isSearchOnDelay((ServerPlayerEntity) user)) {
            createSearchDelay((ServerPlayerEntity) user);
            createNewTarget((ServerWorld) world, new ChunkPos(user.getBlockPos()), user.getStackInHand(hand));
            user.sendMessage(Text.literal("Tracking rift!"), true);
        } else {
            user.sendMessage(Text.literal("Rift search is on cooldown"), true);
        }

        return TypedActionResult.success(user.getStackInHand(hand));
    }

    /**
     * Searches for a target block with artron levels > 0 within a range.
     * @param world The world object.
     * @param source The current chunk
     */
    private void createNewTarget(ServerWorld world, ChunkPos source, ItemStack stack) {
        Direction dir = Direction.NORTH;
        int steps = 1, b;

        for (int i = 0; i < MAX_ITERATIONS; i++) {
            if (steps % 2 != 0) {
                for (b = 0; b < steps; b++) {
                    // going left
                    dir = Direction.EAST;

                    source = getChunkInDirection(source, dir);

                    if (isRiftChunk(source) && hasSufficientFuel(world, source)) {
                        setTarget(stack, source);
                        return;
                    }
                }
                for (b = 0; b < steps; b++) {
                    // going down
                    dir = Direction.SOUTH;

                    source = getChunkInDirection(source, dir);

                    if (isRiftChunk(source) && hasSufficientFuel(world, source)) {
                        setTarget(stack, source);
                        return;
                    }
                }
                steps++; //after "left, down" number of steps increasing by 1, making it even
            }
            else { //if even
                for (b = 0; b <= steps; b++) {
                    dir = Direction.WEST; //right

                    source = getChunkInDirection(source, dir);

                    if (isRiftChunk(source) && hasSufficientFuel(world, source)) {
                        setTarget(stack, source);
                        return;
                    }
                }
                for (b = 0; b <= steps; b++) {
                    dir = Direction.NORTH; // up

                    source = getChunkInDirection(source, dir);

                    if (isRiftChunk(source) && hasSufficientFuel(world, source)) {
                        setTarget(stack, source);
                        return;
                    }
                }
                steps++; //after "right, up" number of steps increasing by 1, making it odd
            }
        }
    }
    private static boolean isRiftChunk(ChunkPos pos) {
        return RiftChunkManager.isRiftChunk(pos);
    }
    private static boolean hasSufficientFuel(ServerWorld world, ChunkPos pos) {
        return RiftChunkManager.getArtronLevels(world, pos) >= 250;
    }
    private static ChunkPos getChunkInDirection(ChunkPos pos, Direction dir) {
        return new ChunkPos(pos.x + (dir.getOffsetX()), pos.z + (dir.getOffsetZ()));
    }
    private static void setTarget(ItemStack stack, ChunkPos pos) {
        NbtCompound nbt = stack.getOrCreateNbt();

        nbt.putInt("X", pos.x);
        nbt.putInt("Z", pos.z);
    }
    public static ChunkPos getTarget(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();

        if (!(nbt.contains("X") && nbt.contains("Z"))) {
            return ChunkPos.ORIGIN;
        }

        return new ChunkPos(nbt.getInt("X"), nbt.getInt("Z"));
    }

    private static void createSearchDelay(ServerPlayerEntity player) {
        DeltaTimeManager.createDelay(player.getUuidAsString() + "-rift-search-delay", 60 * 1000L);
    }
    private static boolean isSearchOnDelay(ServerPlayerEntity player) {
        return DeltaTimeManager.isStillWaitingOnDelay(player.getUuidAsString() + "-rift-search-delay");
    }
}
