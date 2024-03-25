package loqor.ait.core.util;

import loqor.ait.core.item.WearableArmorItem;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;

public interface AITArmorMaterial {
    int getDurability(WearableArmorItem.Type type);

    int getProtection(WearableArmorItem.Type type);

    int getEnchantability();

    SoundEvent getEquipSound();

    Ingredient getRepairIngredient();

    String getName();

    float getToughness();

    float getKnockbackResistance();
}
