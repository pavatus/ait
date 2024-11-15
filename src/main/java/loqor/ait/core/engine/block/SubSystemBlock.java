package loqor.ait.core.engine.block;

import loqor.ait.core.engine.SubSystem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWithEntity;

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
