package mdteam.ait.core.item;

import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.tardis.Tardis;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static mdteam.ait.tardis.control.impl.DimensionControl.convertWorldValueToModified;

public class WaypointItem extends Item {
    public static final String BLOCK_POS_KEY = "blockpos";
    public static final String DIRECTION_KEY = "direction";
    public static final String DIMENSION_KEY = "dimension";
    /*public static final String LOCKED_KEY = "locked";*/
    // fixme ehhhhh should we have a locked variable for the tardis waypoints? maybe it could be helpful?

    public WaypointItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();
        ItemStack itemStack = context.getStack();

        if (player == null)
            return ActionResult.FAIL;
        if (world.isClient()) return ActionResult.SUCCESS;

        NbtCompound nbt = itemStack.getOrCreateNbt();

        if (player.isSneaking()) {
            if (world.getBlockEntity(pos) instanceof ConsoleBlockEntity consoleBlock) {
                if (consoleBlock.getTardis() == null || consoleBlock.getTardis().getTravel().getPosition() == null)
                    return ActionResult.PASS;

                if(!nbt.contains(BLOCK_POS_KEY))
                    nbt.put(BLOCK_POS_KEY, NbtHelper.fromBlockPos(consoleBlock.getTardis().getTravel().getPosition()));
                if(!nbt.contains(DIRECTION_KEY))
                    nbt.putInt(DIRECTION_KEY, consoleBlock.getTardis().getTravel().getPosition().getDirection().ordinal());
                if(!nbt.contains(DIMENSION_KEY))
                    nbt.putString(DIMENSION_KEY, consoleBlock.getTardis().getTravel().getPosition().getDimension().getValue());

                world.playSound(null, pos, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 6f, 1);

                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (!Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("tooltip.ait.remoteitem.holdformoreinfo").formatted(Formatting.GRAY).formatted(Formatting.ITALIC));
            return;
        }

        NbtCompound tag = stack.getOrCreateNbt();

        if (tag.contains(BLOCK_POS_KEY)) {
            tooltip.add(Text.translatable("Block Position > " +
                    NbtHelper.toBlockPos(tag.getCompound(BLOCK_POS_KEY)).getX() + ", " +
                    NbtHelper.toBlockPos(tag.getCompound(BLOCK_POS_KEY)).getY() + ", " +
                    NbtHelper.toBlockPos(tag.getCompound(BLOCK_POS_KEY)).getZ()).formatted(Formatting.GREEN));
        } else {
            tooltip.add(Text.literal("Block Position > ").formatted(Formatting.BLUE));
        }
        if (tag.contains(DIRECTION_KEY)) {
            tooltip.add(Text.translatable("Direction > " + Direction.byId(tag.getInt(DIRECTION_KEY)).asString().toUpperCase()).formatted(Formatting.GREEN));
        } else {
            tooltip.add(Text.literal("Direction > ").formatted(Formatting.BLUE));
        }
        if (tag.contains(DIMENSION_KEY)) {
            tooltip.add(Text.translatable("Dimension > " + convertWorldValueToModified(tag.getString(DIMENSION_KEY))).formatted(Formatting.GREEN));
        } else {
            tooltip.add(Text.literal("Dimension > ").formatted(Formatting.BLUE));
        }
    }
}
