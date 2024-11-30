package loqor.ait.core.engine.impl;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.api.TardisComponent;
import loqor.ait.core.engine.DurableSubSystem;
import loqor.ait.core.engine.StructureHolder;
import loqor.ait.core.engine.block.multi.MultiBlockStructure;
import loqor.ait.core.tardis.handler.ShieldHandler;

public class ShieldsCircuit extends DurableSubSystem implements StructureHolder {
    private static final MultiBlockStructure STRUCTURE = MultiBlockStructure.from(new Identifier(AITMod.MOD_ID, "multiblock/shield"));

    public ShieldsCircuit() {
        super(Id.SHIELDS);
    }

    @Override
    protected float cost() {
        return 1f;
    }

    @Override
    protected boolean shouldDurabilityChange() {
        return this.tardis.areShieldsActive() || tardis.areVisualShieldsActive();
    }

    @Override
    public MultiBlockStructure getStructure() {
        return STRUCTURE;
    }

    @Override
    protected void onDisable() {
        super.onDisable();

        this.tardis.<ShieldHandler>handler(TardisComponent.Id.SHIELDS).disableAll();
    }
}
