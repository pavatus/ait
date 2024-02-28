package mdteam.ait.core.item;

import mdteam.ait.api.tardis.ArtronHolder;
import mdteam.ait.api.tardis.ArtronHolderItem;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.wrapper.client.manager.ClientTardisManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

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
        tooltip.add(
                Text.literal("AU: ").formatted(Formatting.BLUE)
                        .append(Text.literal(
                                        String.valueOf(
                                                Math.round(this.getCurrentFuel(stack)))
                                ).formatted(Formatting.GREEN).append(Text.literal("/" + MAX_FUEL).formatted(Formatting.GRAY))
                        )
        ); // todo translatable + changing of colour based off fuel
        super.appendTooltip(stack, world, tooltip, context);
    }
}
