package mdteam.ait.core.item;

import mdteam.ait.core.AITItems;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.handler.properties.PropertiesHandler;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.tardis.wrapper.client.ClientTardis;
import mdteam.ait.tardis.wrapper.client.manager.ClientTardisManager;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.apache.logging.log4j.core.jmx.Server;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;


// todo fix so many issues with having more than one of this item
public class SiegeTardisItem extends Item {
    public SiegeTardisItem(Settings settings) {
        super(settings.maxCount(1));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        if (world.isClient()) return;

         if (getTardis(stack) == null) {
             stack.setCount(0);
             return;
         }

        Tardis tardis = getTardis(stack);
        assert tardis != null;

        if (!tardis.isSiegeMode()) {
            tardis.setSiegeBeingHeld(false);
            stack.setCount(0);
            return;
        }

        if (entity instanceof ServerPlayerEntity player) {
            if (getSiegeCount(player, tardis) > 1) {
                int foundSlot = findSlot(player, tardis);
                if (foundSlot == slot) {
                    player.getInventory().setStack(slot, ItemStack.EMPTY);
                }
            }
        }

        tardis.getTravel().setPosition(fromEntity(entity));
        if (!tardis.isSiegeBeingHeld()) {
            tardis.setSiegeBeingHeld(true);
        }
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getHand() != Hand.MAIN_HAND) return ActionResult.FAIL; // bc i cba
        if (context.getWorld().isClient()) return ActionResult.SUCCESS;

        ItemStack stack = context.getStack();
        ServerPlayerEntity player = (ServerPlayerEntity) context.getPlayer();

        if (getTardis(stack) == null) {
            player.getMainHandStack().setCount(0);
            player.getInventory().markDirty();
            return ActionResult.FAIL;
        }

        Tardis tardis = getTardis(stack);
        assert tardis != null;

        if (!tardis.isSiegeMode()) {
            tardis.setSiegeBeingHeld(false);
            player.getMainHandStack().setCount(0);
            player.getInventory().markDirty();
            return ActionResult.FAIL;
        }

        placeTardis(tardis, fromItemContext(context));
        player.getMainHandStack().setCount(0);

        if (player.isCreative()) {
            // annoying shit
            int slot = findSlot(player, tardis);
            if (slot == -1) {
                return ActionResult.SUCCESS; // how
            }
            player.getInventory().setStack(slot, ItemStack.EMPTY);
        }

        player.getInventory().markDirty();

        return ActionResult.SUCCESS;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound tag = stack.getOrCreateNbt();
        String text = tag.contains("tardis-uuid") ? tag.getUuid("tardis-uuid").toString().substring(0, 8)
                : Text.translatable("tooltip.ait.remoteitem.notardis").getString();

        tooltip.add(Text.literal("→ " + text).formatted(Formatting.BLUE));
    }

    public static AbsoluteBlockPos.Directed fromItemContext(ItemUsageContext context) {
        return new AbsoluteBlockPos.Directed(context.getBlockPos().offset(context.getSide()), context.getWorld(), context.getHorizontalPlayerFacing().getOpposite());
    }

    public static AbsoluteBlockPos.Directed fromEntity(Entity entity) {
        return new AbsoluteBlockPos.Directed(BlockPos.ofFloored(entity.getPos()), entity.getWorld(), entity.getMovementDirection());
    }

    public static boolean hasSiegeInInventory(ServerPlayerEntity player, Tardis tardis) {
        return getSiegeCount(player, tardis) > 0;
    }

    public static int getSiegeCount(ServerPlayerEntity player, Tardis tardis) {
        int count = 0;

        for (int i = 0; i < 36; i++) {
            if (getTardis(player.getInventory().getStack(i)) == null) continue;
            if (getTardis(player.getInventory().getStack(i)).equals(tardis)) {
                count++;
            }
        }
        return count;
    }

    public static int findSlot(ServerPlayerEntity player, Tardis tardis) {
        Tardis found;

        for (int i = 0; i < 36; i++) {
            found = getTardis(player.getInventory().getStack(i));

            if (found == null) continue;
            if (found.equals(tardis)) {
                return i;
            }
        }
        return -1;
    }

    public static void pickupTardis(Tardis tardis, ServerPlayerEntity player) {
        if(PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.HANDBRAKE))
            return;
        tardis.getTravel().deleteExterior();
        player.getInventory().insertStack(create(tardis));
        player.getInventory().markDirty();
    }
    public static void placeTardis(Tardis tardis, AbsoluteBlockPos.Directed pos) {
        tardis.getTravel().setPosition(pos);
        tardis.getTravel().placeExterior();
        tardis.setSiegeBeingHeld(false);
    }

    public static ItemStack create(Tardis tardis) {
        ItemStack stack = new ItemStack(AITItems.SIEGE_ITEM);
        stack.setCount(1);
        setTardis(stack,tardis);
        return stack;
    }

    public static Tardis getTardis(ItemStack stack) {
        NbtCompound data = stack.getOrCreateNbt();

        if (!data.contains("tardis-uuid")) {
            return null;
        }

        UUID uuid = data.getUuid("tardis-uuid");
        if (TardisUtil.isClient())
            return ClientTardisManager.getInstance().getLookup().get(uuid);

        return ServerTardisManager.getInstance().getTardis(uuid);
    }
    public static void setTardis(ItemStack stack, Tardis tardis) {
        NbtCompound data = stack.getOrCreateNbt();
        data.putUuid("tardis-uuid", tardis.getUuid());
    }
}
