package loqor.ait.core.item;

import loqor.ait.core.AITItems;
import loqor.ait.core.data.AbsoluteBlockPos;
import loqor.ait.core.data.Waypoint;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static loqor.ait.tardis.control.impl.DimensionControl.convertWorldValueToModified;

public class WaypointItem extends Item implements DyeableItem {
	public static final String POS_KEY = "pos";

	public WaypointItem(Settings settings) {
		super(settings);
	}

	@Override
	public ItemStack getDefaultStack() {
		return super.getDefaultStack();
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		if (!Screen.hasShiftDown()) {
			tooltip.add(Text.translatable("tooltip.ait.remoteitem.holdformoreinfo").formatted(Formatting.GRAY).formatted(Formatting.ITALIC));
			return;
		}

		NbtCompound main = stack.getOrCreateNbt();

		if (!(main.contains(POS_KEY)))
			return;

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

	@Override
	public int getColor(ItemStack stack) {
		NbtCompound nbt = stack.getSubNbt(DISPLAY_KEY);

		if (nbt != null && nbt.contains(COLOR_KEY, NbtElement.NUMBER_TYPE))
			return nbt.getInt(COLOR_KEY);

		return 16777215; // white
	}

	public static ItemStack create(Waypoint pos) {
		ItemStack stack = new ItemStack(AITItems.WAYPOINT_CARTRIDGE);
		setPos(stack, pos);

		if (pos.hasName())
			stack.setCustomName(Text.literal(pos.name()));

		return stack;
	}

	public static AbsoluteBlockPos.Directed getPos(ItemStack stack) {
		NbtCompound nbt = stack.getOrCreateNbt();

		if (!nbt.contains(POS_KEY))
			return null;

		return AbsoluteBlockPos.Directed.fromNbt(nbt.getCompound(POS_KEY));
	}

	public static void setPos(ItemStack stack, AbsoluteBlockPos.Directed pos) {
		NbtCompound nbt = stack.getOrCreateNbt();
		nbt.put(POS_KEY, pos.toNbt());
	}
}
