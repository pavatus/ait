package loqor.ait.core.engine.block.multi;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MultiBlockStructure extends ArrayList<MultiBlockStructure.BlockOffset> {
    public MultiBlockStructure(BlockOffset... offsets) {
        this(List.of(offsets));
    }
    public MultiBlockStructure(List<BlockOffset> offsets) {
        super(offsets);
    }

    public boolean check(World world, BlockPos center) {
        return check(world, center, this);
    }

    public static boolean check(World world, BlockPos center, List<BlockOffset> blockOffsets) {
        for (BlockOffset blockOffset : blockOffsets) {
            BlockPos targetPos = center.add(blockOffset.offset);
            if (!blockOffset.block.contains(world.getBlockState(targetPos))) {
                return false;
            }
        }
        return true;
    }

    public MultiBlockStructure offset(BlockPos offset) {
        List<BlockOffset> newOffsets = new ArrayList<>();
        for (BlockOffset blockOffset : this) {
            newOffsets.add(blockOffset.offset(offset));
        }
        return new MultiBlockStructure(newOffsets);
    }
    public MultiBlockStructure offset(int x, int y, int z) {
        return offset(new BlockPos(x, y, z));
    }
    public Optional<BlockOffset> remove(BlockPos pos) {
        Optional<BlockOffset> found = this.at(pos);

	    found.ifPresent(this::remove);
        return found;
    }
    public Optional<BlockOffset> at(BlockPos pos) {
        return this.stream().filter(blockOffset -> blockOffset.offset.equals(pos)).findFirst();
    }
    public BlockOffset atOrDefault(BlockPos pos, BlockOffset def) {
        return this.at(pos).orElse(def);
    }
    public Optional<BlockOffset> put(BlockOffset offset) {
        Optional<BlockOffset> prev = this.remove(offset.offset);
        this.add(offset);
        return prev;
    }

    public record BlockOffset(AllowedBlocks block, BlockPos offset) {
        public BlockOffset(Block block, int x, int y, int z) {
            this(new AllowedBlocks(block), new BlockPos(x, y, z));
        }
        public BlockOffset(Block block) {
            this(new AllowedBlocks(block), BlockPos.ORIGIN);
        }

        public BlockOffset offset(int x, int y, int z) {
            return new BlockOffset(this.block, this.offset.add(x, y, z));
        }
        public BlockOffset offset(BlockPos offset) {
            return new BlockOffset(this.block, this.offset.add(offset));
        }
        public BlockOffset allow(Block... blocks) {
            this.block.addAll(List.of(blocks));
            return this;
        }

        public static List<BlockOffset> corners(Block block, int x, int y, int z) {
            return List.of(
                    new BlockOffset(block, x, y, z),
                    new BlockOffset(block, -x, y, z),
                    new BlockOffset(block, x, -y, z),
                    new BlockOffset(block, x, y, -z),
                    new BlockOffset(block, -x, -y, z),
                    new BlockOffset(block, x, -y, -z),
                    new BlockOffset(block, -x, y, -z),
                    new BlockOffset(block, -x, -y, -z)
            );
        }
        public static List<BlockOffset> volume(Block block, int width, int height, int depth) {
            List<BlockOffset> offsets = new ArrayList<>();
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    for (int z = 0; z < depth; z++) {
                        offsets.add(new BlockOffset(block, x, y, z));
                    }
                }
            }
            return offsets;
        }
    }

    public static class AllowedBlocks extends ArrayList<Block> {
        public AllowedBlocks(Block... blocks) {
            super(List.of(blocks));
        }

        public boolean contains(BlockState state) {
            return this.contains(state.getBlock());
        }
    }
}
