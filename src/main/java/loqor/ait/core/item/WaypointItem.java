package loqor.ait.core.item;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import loqor.ait.core.AITItems;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.core.data.Waypoint;
import loqor.ait.core.util.WorldUtil;
import loqor.ait.tardis.control.impl.DirectionControl;

public class WaypointItem extends Item implements DyeableItem {

    public static final int DEFAULT_COLOR = 16777215;
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
            tooltip.add(Text.translatable("tooltip.ait.remoteitem.holdformoreinfo").formatted(Formatting.GRAY)
                    .formatted(Formatting.ITALIC));
            return;
        }

        NbtCompound main = stack.getOrCreateNbt();

        if (!(main.contains(POS_KEY)))
            return;

        NbtCompound nbt = main.getCompound(POS_KEY);
        DirectedGlobalPos globalPos = DirectedGlobalPos.fromNbt(nbt);

        BlockPos pos = globalPos.getPos();
        String dir = DirectionControl.rotationToDirection(globalPos.getRotation());
        RegistryKey<World> dimension = globalPos.getDimension();

        tooltip.add(Text.translatable("waypoint.position.tooltip")
                .append(Text.literal(" > " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ()))
                .formatted(Formatting.BLUE));

        tooltip.add(Text.translatable("waypoint.direction.tooltip").append(Text.literal(" > " + dir.toUpperCase()))
                .formatted(Formatting.BLUE));

        tooltip.add(Text.translatable("waypoint.dimension.tooltip")
                .append(Text.literal(" > ").append(WorldUtil.worldText(dimension))).formatted(Formatting.BLUE));
    }

    @Override
    public int getColor(ItemStack stack) {
        NbtCompound nbt = stack.getSubNbt(DISPLAY_KEY);

        if (nbt != null && nbt.contains(COLOR_KEY, NbtElement.NUMBER_TYPE))
            return nbt.getInt(COLOR_KEY);

        return DEFAULT_COLOR; // white
    }

    public static ItemStack create(Waypoint pos) {
        ItemStack stack = new ItemStack(AITItems.WAYPOINT_CARTRIDGE);
        setPos(stack, pos.getPos());

        if (pos.hasName())
            stack.setCustomName(Text.literal(pos.name()));

        return stack;
    }

    public static DirectedGlobalPos.Cached getPos(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();

        if (!nbt.contains(POS_KEY))
            return null;

        return DirectedGlobalPos.Cached.fromNbt(nbt.getCompound(POS_KEY));
    }

    public static void setPos(ItemStack stack, DirectedGlobalPos pos) {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.put(POS_KEY, pos.toNbt());
    }
}
