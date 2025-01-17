package loqor.ait.core.engine.impl;


import loqor.ait.AITMod;
import loqor.ait.core.engine.DurableSubSystem;
import loqor.ait.core.engine.StructureHolder;
import loqor.ait.core.engine.block.multi.MultiBlockStructure;

public class ShieldsCircuit extends DurableSubSystem implements StructureHolder {
    private static MultiBlockStructure STRUCTURE;

    public ShieldsCircuit() {
        super(Id.SHIELDS);
    }

    @Override
    protected float cost() {
        return 0.1f;
    }

    @Override
    protected boolean shouldDurabilityChange() {
        return this.tardis.areShieldsActive() || tardis.areVisualShieldsActive();
    }

    @Override
    public MultiBlockStructure getStructure() {
        if (STRUCTURE == null) {
            STRUCTURE = MultiBlockStructure.from(AITMod.id("multiblock/shield"));
        }

        return STRUCTURE;
    }

    @Override
    protected void onDisable() {
        super.onDisable();

        this.tardis.shields().disableAll();
    }
}
