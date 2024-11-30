package loqor.ait.core.blocks;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import loqor.ait.core.AITBlockEntityTypes;
import loqor.ait.core.blockentities.LifeSupportBlockEntity;
import loqor.ait.core.engine.SubSystem;
import loqor.ait.core.engine.block.SubSystemBlock;
import loqor.ait.core.engine.block.SubSystemBlockEntity;

public class LifeSupportBlock extends SubSystemBlock implements BlockEntityProvider {

    public LifeSupportBlock(Settings settings) {
        super(settings, SubSystem.Id.LIFE_SUPPORT);
    }

    @Override
    public boolean isShapeFullCube(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }

    @Nullable @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new LifeSupportBlockEntity(pos, state);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        super.randomDisplayTick(state, world, pos, random);

        if (!(world.getBlockEntity(pos) instanceof LifeSupportBlockEntity lifeSupportBlockEntity))
            return;
        if (!lifeSupportBlockEntity.isLinked()) return;

        float durability = lifeSupportBlockEntity.tardis().get().subsystems().lifeSupport().durability();

        if (durability > 10) return;

        // smoke and spark particles & sfx when below 50%
        world.addParticle(ParticleTypes.LARGE_SMOKE, true, pos.getX() + 0.5f, pos.getY() + 0.25,
                pos.getZ() + 0.5f, 0.15, 0, 0);
        world.addParticle(ParticleTypes.CLOUD, pos.getX() + 0.5f, pos.getY() + 1.25, pos.getZ() + 0.5f, 0.1,
                0, 0.05f);

        world.addParticle(ParticleTypes.LARGE_SMOKE, true, pos.getX() + 0.5f, pos.getY() + 0.25,
                pos.getZ() + 0.5f, -0.15, 0, 0);
        world.addParticle(ParticleTypes.CLOUD, pos.getX() + 0.5f, pos.getY() + 1.25, pos.getZ() + 0.5f, -0.1,
                0, -0.05f);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    protected BlockEntityType<? extends SubSystemBlockEntity> getType() {
        return AITBlockEntityTypes.LIFE_SUPPORT_BLOCK_ENTITY_TYPE;
    }
}
