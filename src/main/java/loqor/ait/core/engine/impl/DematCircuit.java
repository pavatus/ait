package loqor.ait.core.engine.impl;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

import loqor.ait.api.TardisEvents;
import loqor.ait.core.AITBlocks;
import loqor.ait.core.engine.DurableSubSystem;
import loqor.ait.core.engine.StructureHolder;
import loqor.ait.core.engine.block.multi.MultiBlockStructure;

public class DematCircuit extends DurableSubSystem implements StructureHolder {
    private static final MultiBlockStructure STRUCTURE = createStructure();
    private static MultiBlockStructure createStructure() {
        MultiBlockStructure made = new MultiBlockStructure(MultiBlockStructure.BlockOffset.volume(Blocks.WATER, 3, 3, 3)).offset(-1, -1 ,-1); // 3x3x3 water volume

        // 5 adjacent faces allow zeiton
        made.put(new MultiBlockStructure.BlockOffset(AITBlocks.ZEITON_COBBLE, 1, 0, 0).allow(AITBlocks.COMPACT_ZEITON));
        made.put(new MultiBlockStructure.BlockOffset(AITBlocks.ZEITON_COBBLE, -1, 0, 0).allow(AITBlocks.COMPACT_ZEITON));
        made.put(new MultiBlockStructure.BlockOffset(AITBlocks.ZEITON_COBBLE, 0, 1, 0).allow(AITBlocks.COMPACT_ZEITON));
        made.put(new MultiBlockStructure.BlockOffset(AITBlocks.ZEITON_COBBLE, 0, 0, 1).allow(AITBlocks.COMPACT_ZEITON));
        made.put(new MultiBlockStructure.BlockOffset(AITBlocks.ZEITON_COBBLE, 0, 0, -1).allow(AITBlocks.COMPACT_ZEITON));
        made.remove(new BlockPos(0, -1, 0)); // remove bottom
        made.remove(new BlockPos(0, 0, 0)); // ignore centre

        return made;
    }

    static {
        TardisEvents.DEMAT.register(tardis -> {
            DematCircuit circuit = tardis.subsystems().demat();

            return (circuit.isEnabled() && !circuit.isBroken()) ? TardisEvents.Interaction.PASS : TardisEvents.Interaction.FAIL;
        });
    }

    public DematCircuit() {
        super(Id.DEMAT);
    }

    @Override
    protected float cost() {
        return 1f;
    }

    @Override
    protected boolean shouldDurabilityChange() {
        return this.tardis.travel().inFlight();
    }

    @Override
    protected void onRepair() {

    }

    @Override
    public MultiBlockStructure getStructure() {
        return STRUCTURE;
    }
}
