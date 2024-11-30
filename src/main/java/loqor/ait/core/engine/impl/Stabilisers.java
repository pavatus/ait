package loqor.ait.core.engine.impl;

import net.minecraft.server.network.ServerPlayerEntity;

import loqor.ait.api.TardisEvents;
import loqor.ait.core.engine.StructureHolder;
import loqor.ait.core.engine.SubSystem;
import loqor.ait.core.engine.block.multi.MultiBlockStructure;
import loqor.ait.data.Loyalty;

public class Stabilisers extends SubSystem implements StructureHolder {
    static {
        TardisEvents.DEMAT.register(tdis -> {
            Stabilisers stabilisers = tdis.subsystems().stabilisers();
            ServerPlayerEntity player = tdis.loyalty().getLoyalPlayerInside();

            if (!stabilisers.isEnabled()) return TardisEvents.Interaction.FAIL;
            if (player == null) return TardisEvents.Interaction.FAIL;
            if (!tdis.loyalty().get(player).isOf(Loyalty.Type.PILOT)) return TardisEvents.Interaction.FAIL;

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
}
