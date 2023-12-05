package mdteam.ait.core.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class KeySmithingTemplateItem extends Item {

    private static final Formatting TITLE_FORMATTING = Formatting.GRAY;
    private static final Formatting DESCRIPTION_FORMATTING = Formatting.BLUE;

    public KeySmithingTemplateItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(Text.literal("Upgrade").formatted(TITLE_FORMATTING));
        tooltip.add(ScreenTexts.EMPTY);
        tooltip.add(Text.literal("Gold Key").formatted(DESCRIPTION_FORMATTING));
        tooltip.add(ScreenTexts.space().append(Text.literal("Gold Key")));
        tooltip.add(Text.literal("Netherite Ingot").formatted(DESCRIPTION_FORMATTING));
        tooltip.add(ScreenTexts.space().append(Text.literal("Netherite Ingot")));
    }
}
