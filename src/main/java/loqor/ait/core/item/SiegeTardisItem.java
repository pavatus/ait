package loqor.ait.core.item;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import dev.pavatus.lib.data.CachedDirectedGlobalPos;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import loqor.ait.api.link.LinkableItem;
import loqor.ait.core.AITItems;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.core.tardis.handler.SiegeHandler;

// todo fix so many issues with having more than one of this item
public class SiegeTardisItem extends Item {

    public static final String CURRENT_TEXTURE_KEY = "siege_current_texture";

    public SiegeTardisItem(Settings settings) {
        super(settings.maxCount(1));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        if (world.isClient())
            return;

        Tardis tardis = SiegeTardisItem.getTardis(world, stack);

        if (tardis == null) {
            stack.setCount(0);
            return;
        }

        if (!tardis.siege().isActive()) {
            tardis.setSiegeBeingHeld(null);
            stack.setCount(0);
            return;
        }

        UUID heldId = tardis.siege().getHeldPlayerUUID();

        // todo this might be laggy
        if (entity instanceof ServerPlayerEntity player) {
            if (tardis.getExterior().findExteriorBlock().isEmpty()) {
                if (heldId == null) {
                    tardis.siege().setSiegeBeingHeld(player.getUuid());
                    return;
                }
            }

            if (!(Objects.equals(player.getUuid(), heldId))) {
                int found = findSlot(player, tardis);
                player.getInventory().setStack(found, ItemStack.EMPTY);
                return;
            }

            if (getSiegeCount(player, tardis) > 1) {
                int foundSlot = findSlot(player, tardis);
                if (foundSlot == slot) {
                    player.getInventory().setStack(slot, ItemStack.EMPTY);
                }
            }
        }

        tardis.travel().forcePosition(fromEntity(entity));

        if (!tardis.isSiegeBeingHeld()) {
            tardis.setSiegeBeingHeld(entity.getUuid());
        }
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getHand() != Hand.MAIN_HAND)
            return ActionResult.FAIL; // bc i cba

        if (context.getWorld().isClient())
            return ActionResult.SUCCESS;

        ItemStack stack = context.getStack();
        Tardis tardis = SiegeTardisItem.getTardis(context.getWorld(), stack);

        ServerPlayerEntity player = (ServerPlayerEntity) context.getPlayer();
        player.getMainHandStack().setCount(0);

        if (tardis == null) {
            player.getInventory().markDirty();
            return ActionResult.FAIL;
        }

        if (!tardis.siege().isActive()) {
            tardis.setSiegeBeingHeld(null);
            player.getInventory().markDirty();
            return ActionResult.FAIL;
        }

        placeTardis(tardis, fromItemContext(context));

        if (player.isCreative()) {
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
        String text = tag.contains("tardis-uuid")
                ? tag.getUuid("tardis-uuid").toString().substring(0, 8)
                : Text.translatable("tooltip.ait.remoteitem.notardis").getString();

        tooltip.add(Text.literal("→ " + text).formatted(Formatting.BLUE));
    }

    public static CachedDirectedGlobalPos fromItemContext(ItemUsageContext context) {
        return CachedDirectedGlobalPos.create((ServerWorld) context.getWorld(),
                context.getBlockPos().offset(context.getSide()), (byte) 0);
    }

    public static CachedDirectedGlobalPos fromEntity(Entity entity) {
        return CachedDirectedGlobalPos.create((ServerWorld) entity.getWorld(), BlockPos.ofFloored(entity.getPos()),
                (byte) 0);
    }

    public static int getSiegeCount(ServerPlayerEntity player, Tardis tardis) {
        int count = 0;

        for (int i = 0; i < 36; i++) {
            Tardis other = SiegeTardisItem.getTardis(player.getWorld(), player.getInventory().getStack(i));

            if (other == null)
                continue;

            if (other == tardis)
                count++;
        }

        return count;
    }

    public static int findSlot(ServerPlayerEntity player, Tardis tardis) {
        Tardis found;

        for (ItemStack stack : player.getInventory().main) {
            found = SiegeTardisItem.getTardis(player.getWorld(), stack);

            if (found == null)
                continue;

            if (found.equals(tardis)) {
                return player.getInventory().indexOf(stack);
            }
        }

        return -1;
    }

    public static void pickupTardis(Tardis tardis, ServerPlayerEntity player) {
        if (tardis.travel().handbrake())
            return;

        tardis.travel().deleteExterior();
        tardis.siege().setSiegeBeingHeld(player.getUuid());
        player.getInventory().insertStack(create(tardis));
        player.getInventory().markDirty();
    }

    public static void placeTardis(Tardis tardis, CachedDirectedGlobalPos pos) {
        tardis.travel().forcePosition(pos);
        tardis.travel().placeExterior(false);
        tardis.setSiegeBeingHeld(null);
    }

    public static ItemStack create(Tardis tardis) {
        ItemStack stack = new ItemStack(AITItems.SIEGE_ITEM);
        stack.setCount(1);

        setTardis(stack, tardis);
        return stack;
    }

    public static Tardis getTardis(World world, ItemStack stack) {
        return LinkableItem.getTardisFromUuid(world, stack, "tardis-uuid");
    }

    public static void setTardis(ItemStack stack, Tardis tardis) {
        NbtCompound data = stack.getOrCreateNbt();
        data.putUuid("tardis-uuid", tardis.getUuid());
        data.putInt(CURRENT_TEXTURE_KEY, tardis.siege().texture().get().equals(SiegeHandler.BRICK_TEXTURE) ? 1 : 0);
    }
}
