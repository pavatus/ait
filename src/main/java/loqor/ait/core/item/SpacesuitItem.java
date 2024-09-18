package loqor.ait.core.item;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import loqor.ait.core.planet.PlanetRegistry;

public class SpacesuitItem extends RenderableArmorItem {
    public static final String OXYGEN_KEY = "oxygen";
    public static final double MAX_OXYGEN = 4.2D; // 20 minutes worth of oxygen in LITERS.
    public SpacesuitItem(ArmorMaterial material, Type type, Settings settings, boolean hasCustomRendering) {
        super(material, type, settings, hasCustomRendering);
    }

    @Override
    public ItemStack getDefaultStack() {
        if (this.type != Type.CHESTPLATE) {
            return super.getDefaultStack();
        }
        ItemStack stack = new ItemStack(this);
        NbtCompound compound = stack.getOrCreateNbt();
        compound.putDouble(OXYGEN_KEY, 0.0D);
        return stack;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (this.type != Type.CHESTPLATE) return;
        NbtCompound compound = stack.getOrCreateNbt();

        if (!compound.contains(OXYGEN_KEY)) return;

        if (world == null || world.getServer() == null) return;

        if (world.getServer().getTicks() % 20 != 0) {
            return;
        }

        if (PlanetRegistry.getInstance().get(world).hasOxygen() && compound.getDouble(OXYGEN_KEY) < MAX_OXYGEN) {
            // compound.putDouble(OXYGEN_KEY, Math.min(4.2D, compound.getDouble(OXYGEN_KEY) + 0.0035D));
            compound.putDouble(OXYGEN_KEY, Math.min(MAX_OXYGEN, compound.getDouble(OXYGEN_KEY) + 0.2D));
        } else if (compound.getDouble(OXYGEN_KEY) > 0.0D) {
            // This is based on some probably-not-super-accurate math about how many liters of oxygen a human uses.
            // humans usually use about 0.21 liters of air per minute, so I did the math for per second - since the getTicks() % 20 != 0 is every second.
            compound.putDouble(OXYGEN_KEY, Math.max(0.0D, compound.getDouble(OXYGEN_KEY) - 0.0035D));
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        if (this.type != Type.CHESTPLATE) return;

        String oxygen = "" + stack.getOrCreateNbt().getDouble(OXYGEN_KEY);

        tooltip.add(Text.literal("Oxygen: " + oxygen.substring(0, 3) + "L / " + MAX_OXYGEN + "L").formatted(Formatting.BOLD, Formatting.BLUE));
    }
}
