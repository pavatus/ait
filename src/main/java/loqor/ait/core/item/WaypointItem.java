package loqor.ait.core.item;

import loqor.ait.core.AITItems;
import loqor.ait.core.blockentities.ConsoleBlockEntity;
import loqor.ait.core.data.AbsoluteBlockPos;
import loqor.ait.core.data.Waypoint;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static loqor.ait.tardis.control.impl.DimensionControl.convertWorldValueToModified;

public class WaypointItem extends Item {
	public static final String POS_KEY = "pos";

	public WaypointItem(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		PlayerEntity player = context.getPlayer();
		ItemStack itemStack = context.getStack();
		Hand hand = context.getHand();

		if (player == null)
			return ActionResult.FAIL;
		if (world.isClient()) return ActionResult.SUCCESS;

		if (!player.isSneaking()) return ActionResult.FAIL;
		if (hand != Hand.MAIN_HAND) return ActionResult.FAIL;
		if (!(world.getBlockEntity(pos) instanceof ConsoleBlockEntity console)) return ActionResult.FAIL;

		if (console.findTardis().isEmpty() || console.findTardis().get().getTravel().getPosition() == null)
			return ActionResult.PASS;

		if (getPos(itemStack) == null) setPos(itemStack, console.findTardis().get().getTravel().getPosition());

		console.findTardis().get().getHandlers().getWaypoints().markHasCartridge();
		console.findTardis().get().getHandlers().getWaypoints().set(Waypoint.fromDirected(getPos(itemStack)).setName(itemStack.getName().getString()), true);
		player.setStackInHand(hand, ItemStack.EMPTY);

		world.playSound(null, pos, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 6f, 1);

		return ActionResult.SUCCESS;
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		if (!Screen.hasShiftDown()) {
			tooltip.add(Text.translatable("tooltip.ait.remoteitem.holdformoreinfo").formatted(Formatting.GRAY).formatted(Formatting.ITALIC));
			return;
		}

		NbtCompound main = stack.getOrCreateNbt();

		if (!(main.contains(POS_KEY))) return;

		NbtCompound nbt = main.getCompound(POS_KEY);

		BlockPos pos = NbtHelper.toBlockPos(nbt.getCompound("pos"));
		String dimension = nbt.getString("dimension");
		Direction dir = Direction.byId(nbt.getInt("direction"));

		tooltip.add(Text.translatable("waypoint.position.tooltip").append(Text.literal(
						" > " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ()))
				.formatted(Formatting.BLUE));
		tooltip.add(Text.translatable("waypoint.direction.tooltip").append(Text.literal(
						" > " + dir.asString().toUpperCase()))
				.formatted(Formatting.BLUE));
		tooltip.add(Text.translatable("waypoint.dimension.tooltip").append(Text.literal(
						" > " + convertWorldValueToModified(dimension)))
				.formatted(Formatting.BLUE));
	}

	public static ItemStack create(Waypoint pos) {
		ItemStack stack = new ItemStack(AITItems.WAYPOINT_CARTRIDGE);
		setPos(stack, pos);
		if (pos.hasName()) {
			stack.setCustomName(Text.literal(pos.name()));
		}
		return stack;
	}

	public static AbsoluteBlockPos.Directed getPos(ItemStack stack) {
		NbtCompound nbt = stack.getOrCreateNbt();

		if (!nbt.contains(POS_KEY)) return null;

		return AbsoluteBlockPos.Directed.fromNbt(nbt.getCompound(POS_KEY));
	}

	public static void setPos(ItemStack stack, AbsoluteBlockPos.Directed pos) {
		NbtCompound nbt = stack.getOrCreateNbt();

		nbt.put(POS_KEY, pos.toNbt());
	}

	public static boolean hasPos(ItemStack stack) {
		return stack.getOrCreateNbt().contains(POS_KEY);
	}
}
