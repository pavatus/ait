package loqor.ait.core.item.blueprint;

import loqor.ait.AITMod;
import loqor.ait.registry.impl.BlueprintRegistry;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlueprintItem extends Item {

    public BlueprintItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack stack = new ItemStack(this);
        NbtCompound nbt = stack.getOrCreateNbt();

        nbt.putString("id", BlueprintRegistry.getRandomEntry().id().toString());
        return stack;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        NbtCompound nbt = stack.getOrCreateNbt();
        NbtElement element = nbt.get("id");

        if (element == null)
            return;

        Identifier id = Identifier.tryParse(element.asString());

        if (id == null) {
            AITMod.LOGGER.warn("Couldn't parse blueprint id: '{}'", element.asString());
            return;
        }

        BlueprintType blueprint = BlueprintRegistry.REGISTRY.get(new Identifier(element.asString()));

        if (blueprint == null) {
            AITMod.LOGGER.warn("Couldn't find blueprint with id: '{}'!", id);
            return;
        }

        tooltip.add(Text.translatable("ait.blueprint.tooltip").formatted(Formatting.BLUE)
                .append(blueprint.text().copy().formatted(Formatting.GRAY)));
    }
}
