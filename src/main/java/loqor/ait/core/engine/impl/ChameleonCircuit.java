package loqor.ait.core.engine.impl;

import loqor.ait.core.engine.DurableSubSystem;

public class ChameleonCircuit extends DurableSubSystem {
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
}
