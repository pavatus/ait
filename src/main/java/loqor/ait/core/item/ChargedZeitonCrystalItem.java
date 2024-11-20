package loqor.ait.core.item;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import loqor.ait.api.ArtronHolderItem;

public class ChargedZeitonCrystalItem extends Item implements ArtronHolderItem {
    public static final double MAX_FUEL = 5000;

    public ChargedZeitonCrystalItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack stack = new ItemStack(this);
        NbtCompound nbt = stack.getOrCreateNbt();

        nbt.putDouble(FUEL_KEY, getMaxFuel(stack));

        return stack;
    }

    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        super.onCraft(stack, world, player);
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putDouble(FUEL_KEY, 0);
    }

    @Override
    public double getMaxFuel(ItemStack stack) {
        return MAX_FUEL;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        int currentFuel = (int) Math.round(this.getCurrentFuel(stack));
        Formatting fuelColor = currentFuel > (MAX_FUEL / 4) ? Formatting.GREEN : Formatting.RED;

        tooltip.add(
                Text.translatable("message.ait.artron_units", currentFuel).formatted(Formatting.BLUE)
                        .formatted(fuelColor)
                        .append(Text.literal("/" + MAX_FUEL).formatted(Formatting.GRAY))
        );

        super.appendTooltip(stack, world, tooltip, context);
    }
}
