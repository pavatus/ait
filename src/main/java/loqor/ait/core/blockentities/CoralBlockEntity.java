package loqor.ait.core.blockentities;

import loqor.ait.core.AITBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public class CoralBlockEntity extends BlockEntity {

	public String CREATOR_NAME = "creator_name";

	public CoralBlockEntity(BlockPos pos, BlockState state) {
		super(AITBlockEntityTypes.CORAL_BLOCK_ENTITY_TYPE, pos, state);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.CREATOR_NAME = nbt.getString("permission");
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.putString("permission", this.CREATOR_NAME);
	}
}
