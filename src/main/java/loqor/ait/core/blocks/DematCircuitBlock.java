package loqor.ait.core.blocks;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import loqor.ait.core.AITBlockEntityTypes;
import loqor.ait.core.AITBlocks;
import loqor.ait.core.AITSounds;
import loqor.ait.core.engine.SubSystem;
import loqor.ait.core.engine.block.SubSystemBlock;
import loqor.ait.core.engine.block.SubSystemBlockEntity;
import loqor.ait.core.engine.block.multi.MultiBlockStructure;
import loqor.ait.core.engine.block.multi.StructureSystemBlockEntity;
import loqor.ait.core.engine.impl.DematCircuit;


public class DematCircuitBlock extends SubSystemBlock {
    public DematCircuitBlock(Settings settings) {
        super(settings, SubSystem.Id.DEMAT);
    }

    @Override
    protected BlockEntityType<? extends SubSystemBlockEntity> getType() {
        return AITBlockEntityTypes.DEMAT_CIRCUIT_BLOCK_TYPE;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntity(pos, state);
    }

    public static class BlockEntity extends StructureSystemBlockEntity {
        private static final MultiBlockStructure STRUCTURE = new MultiBlockStructure(
                MultiBlockStructure.BlockOffset.volume(AITBlocks.ZEITON_BLOCK, 3, 1, 3)
        ).offset(-1, -1, -1);

        public BlockEntity(BlockPos pos, BlockState state) {
            super(AITBlockEntityTypes.DEMAT_CIRCUIT_BLOCK_TYPE, pos, state, SubSystem.Id.DEMAT);
        }

        @Override
        protected MultiBlockStructure getStructure() {
            return STRUCTURE;
        }

        @Override
        public void useOn(BlockState state, World world, boolean sneaking, PlayerEntity player, ItemStack hand) {
            super.useOn(state, world, sneaking, player, hand);

            if (world.isClient() || this.tardis().isEmpty())
                return;

            DematCircuit system = this.tardis().get().subsystems().demat();

            if (system.isRepairItem(hand) && system.durability() < 100) {
                system.addDurability(5);
                hand.decrement(1);
                world.playSound(null, this.getPos(), AITSounds.BWEEP, SoundCategory.BLOCKS, 0.5f, 1f);
            }
        }
    }
}
