package loqor.ait.core.engine.impl;

import loqor.ait.core.engine.DurableSubSystem;
import loqor.ait.core.engine.StructureHolder;
import loqor.ait.core.engine.block.multi.MultiBlockStructure;

public class ChameleonCircuit extends DurableSubSystem implements StructureHolder {
    public ChameleonCircuit() {
        super(Id.CHAMELEON);
    }

    @Override
    protected float cost() {
        return 1f;
    }

    @Override
    protected boolean shouldDurabilityChange() {
        return this.tardis().cloak().cloaked().get();
    }
    @Override
    public MultiBlockStructure getStructure() {
        return MultiBlockStructure.EMPTY;
    }
}
