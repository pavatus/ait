package loqor.ait.core.engine.impl;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.core.engine.DurableSubSystem;
import loqor.ait.core.engine.StructureHolder;
import loqor.ait.core.engine.block.multi.MultiBlockStructure;

public class GravitationalCircuit extends DurableSubSystem implements StructureHolder {
    private static final MultiBlockStructure STRUCTURE = MultiBlockStructure.from(new Identifier(AITMod.MOD_ID, "multiblock/gravity"));

    public GravitationalCircuit() {
        super(Id.GRAVITATIONAL);
    }

    @Override
    protected float cost() {
        return 1f;
    }

    @Override
    protected boolean shouldDurabilityChange() {
        return this.tardis().travel().antigravs().get();
    }

    @Override
    public MultiBlockStructure getStructure() {
        return STRUCTURE;
    }

    @Override
    protected void onDisable() {
        super.onDisable();

        this.tardis().travel().antigravs().set(false);
    }
}
