package loqor.ait.core.item.blueprint;

import loqor.ait.tardis.control.impl.DimensionControl;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlueprintItem extends Item {

    public static final String BLUEPRINT_TYPE = "blueprint_type";

    public BlueprintItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack stack = new ItemStack(this);
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putString(BLUEPRINT_TYPE, formatString(BlueprintRegistry.getRandomEntry().id()));
        return stack;
    }

    public static String formatString(String input) {
        // Split the string into words
        String[] words = input.split("_");

        // Capitalize the first letter of each word
        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1).toLowerCase();
        }

        // Join the words back together with spaces
        return String.join(" ", words);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        NbtCompound nbt = stack.getOrCreateNbt();

        if (!nbt.contains(BLUEPRINT_TYPE)) return;

        tooltip.add(Text.translatable("ait.blueprint.tooltip").formatted(Formatting.BLUE));
        tooltip.add(Text.literal(nbt.getString(BLUEPRINT_TYPE)).formatted(Formatting.GRAY));
    }
}
