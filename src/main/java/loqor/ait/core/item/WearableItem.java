package loqor.ait.core.item;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Equipment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

/**
 * A WearableItem is an Item that can be equipped.
 * Allows for custom rendering using own models + feature renderer instead of the default one
 * <p>
 * From Duzo's Persona Mod
 *
 * @author duzo
 */
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

	@Override
	public TypedActionResult<ItemStack> equipAndSwap(Item item, World world, PlayerEntity user, Hand hand) {
		return Equipment.super.equipAndSwap(item, world, user, hand);
	}
}