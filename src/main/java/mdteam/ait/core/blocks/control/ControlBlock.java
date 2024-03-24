package mdteam.ait.core.blocks.control;

import mdteam.ait.core.blockentities.control.ControlBlockEntity;
import mdteam.ait.core.blocks.types.HorizontalDirectionalBlock;
import mdteam.ait.core.item.control.ControlBlockItem;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class ControlBlock extends HorizontalDirectionalBlock implements BlockEntityProvider {

	public ControlBlock(Settings settings) {
		super(settings);
	}

	@Override
	public Item asItem() {
		return this.getItem();
	}

	protected abstract ControlBlockItem getItem();

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		Optional<String> id = ControlBlockItem.findControlId(itemStack);

		if (id.isEmpty()) return;

		BlockEntity be = world.getBlockEntity(pos);
		if (!(be instanceof ControlBlockEntity)) return;

		((ControlBlockEntity) be).setControl(id.get());
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient()) {
			BlockEntity be = world.getBlockEntity(pos);
			if (be instanceof ControlBlockEntity) {
				boolean success = ((ControlBlockEntity) be).run((ServerPlayerEntity) player, false);

				return (success) ? ActionResult.SUCCESS : ActionResult.FAIL;
			}
		}

		return ActionResult.SUCCESS;
	}

	@Override
	public void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
		if (!world.isClient()) {
			BlockEntity be = world.getBlockEntity(pos);
			if (be instanceof ControlBlockEntity) {
				((ControlBlockEntity) be).run((ServerPlayerEntity) player, true);
			}
		}

		super.onBlockBreakStart(state, world, pos, player);
	}

}
