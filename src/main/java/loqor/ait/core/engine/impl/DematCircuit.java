package loqor.ait.core.engine.impl;

import loqor.ait.api.TardisEvents;
import loqor.ait.core.engine.DurableSubSystem;

public class DematCircuit extends DurableSubSystem {
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
}
