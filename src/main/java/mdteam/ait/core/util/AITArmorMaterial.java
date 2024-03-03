package mdteam.ait.core.util;

import mdteam.ait.core.item.WearableArmorItem;
import net.minecraft.item.ArmorItem;
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
