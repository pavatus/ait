package loqor.ait.core.engine.impl;


import loqor.ait.api.TardisEvents;
import loqor.ait.core.engine.StructureHolder;
import loqor.ait.core.engine.SubSystem;
import loqor.ait.core.engine.block.multi.MultiBlockStructure;

public class Stabilisers extends SubSystem implements StructureHolder {
    static {
        TardisEvents.DEMAT.register(tdis -> {
            Stabilisers stabilisers = tdis.subsystems().stabilisers();

            // i dont like this feature, so i commented it out :p
            /*
            if (!stabilisers.isEnabled()) return TardisEvents.Interaction.FAIL;
            ServerPlayerEntity player = tdis.loyalty().getLoyalPlayerInside();
            if (!tdis.loyalty().get(player).isOf(Loyalty.Type.PILOT)) return TardisEvents.Interaction.FAIL;
             */
            return TardisEvents.Interaction.PASS;
        });
    }

    public Stabilisers() {
        super(Id.STABILISERS);
    }

    @Override
    public MultiBlockStructure getStructure() {
        return MultiBlockStructure.EMPTY;
    }

    @Override
    protected void onDisable() {
        super.onDisable();

        this.tardis().travel().autopilot(false);
    }
}
