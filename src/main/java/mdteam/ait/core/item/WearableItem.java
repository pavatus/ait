package mdteam.ait.core.item;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Equipment;
import net.minecraft.item.Item;

public class WearableItem extends Item implements Equipment {
    private final EquipmentSlot slot;
    private final boolean hasCustomRenderer;

    public WearableItem(EquipmentSlot slot, boolean hasCustomRenderer, Settings settings) {
        super(settings);

        this.slot = slot;
        this.hasCustomRenderer = hasCustomRenderer;
    }
    public WearableItem(EquipmentSlot slot, Settings settings) {
        this(slot, false, settings);
    }

    @Override
    public EquipmentSlot getSlotType() {
        return this.slot;
    }

    public boolean hasCustomRenderer() {
        return this.hasCustomRenderer;
    }
}