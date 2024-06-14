package loqor.ait.tardis.link.v2.interior;

import loqor.ait.core.AITDimensions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class InteriorLinkableBlock extends Block implements BlockEntityProvider {

    public InteriorLinkableBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (world.getRegistryKey() == AITDimensions.TARDIS_DIM_WORLD && world.getBlockEntity(pos) instanceof InteriorLinkableBlockEntity linkable)
            linkable.onPlaced(world, pos, state, placer, itemStack);
    }

    @Nullable
    @Override
    public abstract InteriorLinkableBlockEntity createBlockEntity(BlockPos pos, BlockState state);
}
