package loqor.ait.core.screen_handlers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

import loqor.ait.AITMod;

@Deprecated(forRemoval = true)
public class EngineScreenHandler extends ScreenHandler {
    private final Inventory inventory;

    public EngineScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(8));
    }

    public EngineScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(AITMod.ENGINE_SCREEN_HANDLER, syncId);
        checkSize(inventory, 8);
        this.inventory = inventory;
        // some inventories do custom logic when a player opens it.
        inventory.onOpen(playerInventory.player);
        // This will place the slot in the correct locations for a 3x3 Grid. The slots
        // exist on both
        // server and client!
        // This will not render the background of the slots however, this is the Screens
        // job
        int m;
        int l;
        // Our inventory
        /*
         * for (m = 0; m < 3; ++m) { for (l = 0; l < 3; ++l) { this.addSlot(new
         * Slot(inventory, l + m * 3, 62 + l * 18, 17 + m * 18)); } }
         */
        for (m = 0; m < 2; ++m) {
            for (l = 0; l < 4; ++l) {
                this.addSlot(new Slot(inventory, (l + m * 4), 26 + l * 36, 24 + m * 23));
            }
        }
        // The player inventory
        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
            }
        }
        // The player Hotbar
        for (m = 0; m < 9; ++m) {
            this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 142));
        }
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }
}
