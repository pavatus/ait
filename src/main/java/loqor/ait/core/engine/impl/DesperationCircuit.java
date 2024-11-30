package loqor.ait.core.engine.impl;

import loqor.ait.core.engine.StructureHolder;
import loqor.ait.core.engine.SubSystem;
import loqor.ait.core.engine.block.multi.MultiBlockStructure;

public class DesperationCircuit extends SubSystem implements StructureHolder {
    public DesperationCircuit() {
        super(Id.DESPERATION);
    }

    @Override
    public MultiBlockStructure getStructure() {
        return MultiBlockStructure.EMPTY;
    }

    @Override
    protected void onDisable() {
        super.onDisable();

        this.tardis().stats().hailMary().set(false);
        this.tardis().siege().setActive(false);
    }
}
