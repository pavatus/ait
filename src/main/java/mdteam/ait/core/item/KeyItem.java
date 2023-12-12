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
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.security.Key;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class KeyItem extends Item {
    private final List<Protocols> protocols;


    public KeyItem(Settings settings, Protocols... abs) {
        super(settings.maxCount(1));
        protocols = new ArrayList<>(List.of(abs));
    }

    public enum Protocols {
        SNAP,
        HAIL,
        PERCEPTION
    }

    public boolean hasProtocol(Protocols var) {
        return this.protocols.contains(var);
    }

    private void link(ItemStack stack,Tardis tardis) {
        this.link(stack,tardis.getUuid());
    }
    private void link(ItemStack stack,UUID uuid) {
        NbtCompound nbt = stack.getOrCreateNbt();

        nbt.putUuid("tardis", uuid);
    }

    public static boolean isKeyInInventory(PlayerEntity player) {
        return getFirstKeyStackInInventory(player) != null;
    }
    public static KeyItem getFirstKeyInInventory(PlayerEntity player) {
        return (KeyItem) getFirstKeyStackInInventory(player).getItem();
    }
    public static ItemStack getFirstKeyStackInInventory(PlayerEntity player) {
        // from playerinventory

        Iterator it = player.getInventory().main.iterator();

        while (it.hasNext()) {
            ItemStack itemStack = (ItemStack) it.next();
            if (!itemStack.isEmpty() && itemStack.getItem() instanceof KeyItem key) {
                return itemStack;
            }
        }
        return null;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();
        ItemStack itemStack = context.getStack();

        if (world.isClient() || player == null)
            return ActionResult.PASS;

        if (player.isSneaking()) {
            if (world.getBlockEntity(pos) instanceof ConsoleBlockEntity consoleBlock) {
                if (consoleBlock.getTardis() == null)
                    return ActionResult.FAIL;

                this.link(itemStack, consoleBlock.getTardis());
                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.PASS;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (!Screen.hasShiftDown()) {
            tooltip.add(Text.literal("Hold shift for more info").formatted(Formatting.GRAY).formatted(Formatting.ITALIC));
            return;
        }

        NbtCompound tag = stack.getOrCreateNbt();
        String text = tag.contains("tardis") ? tag.getUuid("tardis").toString().substring(0, 8)
                : "Key does not identify with any TARDIS";

        tooltip.add(Text.literal("→ " + text).formatted(Formatting.BLUE));
    }
}
