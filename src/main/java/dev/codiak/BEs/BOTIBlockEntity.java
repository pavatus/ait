package dev.codiak.BEs;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

import dev.amble.ait.core.AITBlockEntityTypes;
import dev.amble.ait.core.AITBlocks;

public class BOTIBlockEntity extends BlockEntity {
    public BOTIBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.PORTAL_BLOCK_ENTITY_TYPE, pos, state);
    }

    public static BlockEntityType<?> createBlockEntityType() {
        return BlockEntityType.Builder.create(BOTIBlockEntity::new, AITBlocks.PORTAL_BLOCK).build(null);
    }
}
