package mdteam.ait.core.item;

import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.core.blockentities.DoorBlockEntity;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class KeyItem extends Item {

    public KeyItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();
        ItemStack itemStack = context.getStack();

        if (world.isClient() || player == null)
            return ActionResult.PASS;

        NbtCompound nbt = itemStack.getOrCreateNbt();

        if (player.isSneaking()) {
            if (world.getBlockEntity(pos) instanceof ConsoleBlockEntity consoleBlock) {
                if (consoleBlock.getTardis() == null)
                    return ActionResult.FAIL;

                nbt.putUuid("tardis", consoleBlock.getTardis().getUuid());
                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.PASS;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (!Screen.hasShiftDown()) {
            tooltip.add(Text.of("Hold shift for more info"));
            return;
        }

        NbtCompound tag = stack.getOrCreateNbt();
        String text = tag.contains("tardis") ? tag.getUuid("tardis").toString().substring(0, 8)
                : "Key does not identify with any TARDIS";

        tooltip.add(Text.literal("→ " + text).formatted(Formatting.DARK_AQUA));
    }
}
