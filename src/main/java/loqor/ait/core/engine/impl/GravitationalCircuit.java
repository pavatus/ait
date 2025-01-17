package loqor.ait.core.engine.impl;


import loqor.ait.AITMod;
import loqor.ait.core.engine.DurableSubSystem;
import loqor.ait.core.engine.StructureHolder;
import loqor.ait.core.engine.block.multi.MultiBlockStructure;

public class GravitationalCircuit extends DurableSubSystem implements StructureHolder {
    private static MultiBlockStructure STRUCTURE;

    public GravitationalCircuit() {
        super(Id.GRAVITATIONAL);
    }

    @Override
    protected float cost() {
        return 0.25f;
    }

    @Override
    protected boolean shouldDurabilityChange() {
        return this.tardis().travel().antigravs().get();
    }

    @Override
    public MultiBlockStructure getStructure() {
        if (STRUCTURE == null) {
            STRUCTURE = MultiBlockStructure.from(AITMod.id("multiblock/gravity"));
        }

        return STRUCTURE;
    }

    @Override
    protected void onDisable() {
        super.onDisable();

        this.tardis().travel().antigravs().set(false);
    }
}
