package loqor.ait.core.engine.impl;

import loqor.ait.api.TardisEvents;
import loqor.ait.core.engine.DurableSubSystem;
import loqor.ait.core.engine.StructureHolder;
import loqor.ait.core.engine.block.multi.MultiBlockStructure;
import loqor.ait.core.tardis.TardisExterior;
import loqor.ait.registry.impl.CategoryRegistry;

public class ChameleonCircuit extends DurableSubSystem implements StructureHolder {
    static {
        TardisEvents.EXTERIOR_CHANGE.register(tardis -> {
            if (tardis.subsystems().chameleon().isUsable()) return;

            TardisExterior exterior = tardis.getExterior();
            if (exterior.getCategory().equals(CategoryRegistry.CAPSULE)) return;

            exterior.setType(CategoryRegistry.CAPSULE);
        });
    }

    public ChameleonCircuit() {
        super(Id.CHAMELEON);
    }

    @Override
    protected float cost() {
        return 0.2f;
    }

    @Override
    protected boolean shouldDurabilityChange() {
        return this.tardis().cloak().cloaked().get();
    }
    @Override
    public MultiBlockStructure getStructure() {
        return MultiBlockStructure.EMPTY;
    }

    @Override
    protected void onDisable() {
        super.onDisable();

        this.tardis().getExterior().setType(CategoryRegistry.CAPSULE);
    }
}
