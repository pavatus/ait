package loqor.ait.core.item.part;

import loqor.ait.core.item.SonicItem;
import loqor.ait.core.util.AITModTags;
import loqor.ait.registry.MachineRecipeRegistry;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ClickType;

public class MachineItem extends Item {

    public MachineItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
        if (clickType != ClickType.RIGHT)
            return false;

        ItemStack sonic = slot.getStack();

        // Should this be in SonicItem.Mode.INTERACTION?
        if (!sonic.getRegistryEntry().isIn(AITModTags.Items.SONIC_ITEM))
            return false;

        if (SonicItem.findMode(sonic) != SonicItem.Mode.INTERACTION)
            return false;

        MachineRecipeRegistry.getInstance().findMatching(stack).ifPresent(recipe -> {
            SonicItem.playSonicSounds(player);
            stack.decrement(1);

            for (ItemStack input : recipe.input()) {
                player.getWorld().spawnEntity(new ItemEntity(player.getWorld(), player.getX(), player.getY(), player.getZ(), input));
            }

            player.playSound(SoundEvents.ENTITY_ITEM_BREAK, 0.8F, 0.8F + player.getWorld().getRandom().nextFloat() * 0.4F);
        });

        return true;
    }
}
