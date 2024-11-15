package loqor.ait.core.engine.block;

import loqor.ait.api.link.v2.block.InteriorLinkableBlockEntity;
import loqor.ait.core.engine.SubSystem;
import loqor.ait.core.engine.link.IFluidLink;
import loqor.ait.core.engine.link.IFluidSource;
import loqor.ait.core.engine.link.block.FluidLinkBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class SubSystemBlockEntity extends FluidLinkBlockEntity implements IFluidLink {
	private SubSystem.IdLike id;

	public SubSystemBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public SubSystem system() {
		if (this.tardis() == null || this.tardis().isEmpty()) return null;

		return this.tardis().get().subsystems().get(this.id);
	}
	protected SubSystem.IdLike id() {
		if (this.id == null) {
			this.id = ((SubSystemBlock) this.getCachedState().getBlock()).getSystemId();
		}

		return this.id;
	}

	@Override
	public void onGainFluid() {
		this.system().setEnabled(true);
	}

	@Override
	public void onLoseFluid() {
		this.system().setEnabled(false);
	}
}
