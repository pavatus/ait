package mdteam.ait.core.item;

import mdteam.ait.core.blockentities.DoorBlockEntity;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.helper.TardisUtil;
import mdteam.ait.data.AbsoluteBlockPos;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import the.mdteam.ait.ServerTardisManager;
import the.mdteam.ait.Tardis;
import the.mdteam.ait.TardisTravel;

import java.util.List;

import static the.mdteam.ait.TardisTravel.State.FLIGHT;
import static the.mdteam.ait.TardisTravel.State.LANDED;

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
            if (world.getBlockEntity(pos) instanceof ExteriorBlockEntity exterior) {
                if (exterior.getTardis() == null)
                    return ActionResult.FAIL;

                nbt.putUuid("tardis", exterior.getTardis().getUuid());
                return ActionResult.SUCCESS;
            } else if (world.getBlockEntity(pos) instanceof DoorBlockEntity door) {
                if (door.getTardis() == null)
                    return ActionResult.FAIL;
                nbt.putUuid("tardis", door.getTardis().getUuid());
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

        tooltip.add(Text.literal("→ " + text));
    }
}
