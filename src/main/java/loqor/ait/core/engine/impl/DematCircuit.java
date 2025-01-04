package loqor.ait.core.engine.impl;


import loqor.ait.AITMod;
import loqor.ait.api.TardisEvents;
import loqor.ait.core.engine.DurableSubSystem;
import loqor.ait.core.engine.StructureHolder;
import loqor.ait.core.engine.block.multi.MultiBlockStructure;

public class DematCircuit extends DurableSubSystem implements StructureHolder {
    private static MultiBlockStructure STRUCTURE;

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
        return 0.05f * this.tardis().travel().speed();
    }

    @Override
    protected boolean shouldDurabilityChange() {
        return this.tardis.travel().inFlight();
    }

    @Override
    public MultiBlockStructure getStructure() {
        if (STRUCTURE == null) {
            STRUCTURE = MultiBlockStructure.from(AITMod.id("multiblock/demat"));
        }

        return STRUCTURE;
    }
}
