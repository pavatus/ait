package loqor.ait.core.item;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import loqor.ait.core.AITItems;
import loqor.ait.tardis.data.distress.DistressCall;

public class DistressCallItem extends Item { // todo needs rename
    public DistressCallItem(Settings settings) {
        super(settings.maxDamageIfAbsent(100));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        if (world.isClient()) return;

        DistressCall call = getCall(stack, world.getServer().getTicks(), world);
        if (call == null) return;

        stack.setDamage((int) ((1f - (((float) call.getTimeLeft() / (call.lifetime())))) * stack.getMaxDamage()));

        if (call.isValid()) return;

        stack.setCount(0);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        if (world == null) return;

        DistressCall call = getCall(stack, 0, world);
        if (call == null) return;

        tooltip.add(Text.literal(call.message()).formatted(Formatting.ITALIC, Formatting.RED));
    }

    public static DistressCall getCall(ItemStack stack, int ticks, World world) {
        NbtCompound data = stack.getOrCreateNbt();
        if (!data.contains("DistressCall")) return null;

        return DistressCall.create(data.getCompound("DistressCall"), ticks, world);
    }
    public static void setCall(ItemStack stack, DistressCall call) {
        stack.getOrCreateNbt().put("DistressCall", call.toNbt());
    }

    public static ItemStack create(DistressCall call) {
        ItemStack stack = new ItemStack(AITItems.HYPERCUBE);
        setCall(stack, call);
        return stack;
    }
}
