package loqor.ait.core.engine.block;

import net.minecraft.block.BlockWithEntity;

import loqor.ait.core.engine.SubSystem;

public abstract class SubSystemBlock extends BlockWithEntity {
    private final SubSystem.IdLike id;

    protected SubSystemBlock(Settings settings, SubSystem.IdLike system) {
        super(settings);

        this.id = system;
    }

    public SubSystem.IdLike getSystemId() {
        return this.id;
    }
}
