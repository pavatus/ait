package loqor.ait.core.engine.impl;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import loqor.ait.core.AITBlocks;
import loqor.ait.core.engine.DurableSubSystem;
import loqor.ait.core.engine.StructureHolder;
import loqor.ait.core.engine.block.multi.MultiBlockStructure;

public class ShieldsCircuit extends DurableSubSystem implements StructureHolder {
    private static final MultiBlockStructure STRUCTURE = createStructure();
    private static MultiBlockStructure createStructure() {
        MultiBlockStructure made = new MultiBlockStructure();

        made.addAll(MultiBlockStructure.BlockOffset.square(Blocks.QUARTZ_BLOCK, Direction.NORTH, 2, AITBlocks.ZEITON_COBBLE, AITBlocks.COMPACT_ZEITON));
        made.addAll(MultiBlockStructure.BlockOffset.square(Blocks.QUARTZ_BLOCK, Direction.EAST, 2, AITBlocks.ZEITON_COBBLE, AITBlocks.COMPACT_ZEITON));
        made.addAll(MultiBlockStructure.BlockOffset.square(Blocks.QUARTZ_BLOCK, Direction.UP, 2, AITBlocks.ZEITON_COBBLE, AITBlocks.COMPACT_ZEITON));

        // the blocks above and below the center can be cables
        made.at(new BlockPos(0, 2, 0)).orElseThrow().allow(AITBlocks.CABLE_BLOCK);
        made.at(new BlockPos(0,-2, 0)).orElseThrow().allow(AITBlocks.CABLE_BLOCK);
        made.remove(new BlockPos(0, 0, 0));

        return made;
    }

    public ShieldsCircuit() {
        super(Id.SHIELDS);
    }

    @Override
    protected float cost() {
        return 1f;
    }

    @Override
    protected boolean shouldDurabilityChange() {
        return this.tardis.areShieldsActive() || tardis.areVisualShieldsActive();
    }

    @Override
    protected void onRepair() {

    }

    @Override
    public MultiBlockStructure getStructure() {
        return STRUCTURE;
    }
}
