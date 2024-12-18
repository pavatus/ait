package dev.pavatus.christmas.core.item;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import loqor.ait.core.item.HypercubeItem;

public class ChristmasCardHypercube extends HypercubeItem {
    public ChristmasCardHypercube(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.literal("MERRY CHRISTMAS!").formatted(Formatting.GREEN)); // todo translatable

        super.appendTooltip(stack, world, tooltip, context);
    }
}
