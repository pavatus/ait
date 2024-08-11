package loqor.ait.core.util;

import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;

import loqor.ait.core.item.WearableArmorItem;

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
