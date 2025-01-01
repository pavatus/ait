package dev.pavatus.lib.item;

import net.fabricmc.fabric.api.item.v1.CustomDamageHandler;
import net.fabricmc.fabric.api.item.v1.EquipmentSlotProvider;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;

import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.resource.featuretoggle.FeatureFlag;
import net.minecraft.util.Rarity;

public class AItemSettings extends FabricItemSettings {

    private ItemGroup group;

    public AItemSettings group(ItemGroup group) {
        this.group = group;
        return this;
    }

    @Override
    public AItemSettings equipmentSlot(EquipmentSlotProvider equipmentSlotProvider) {
        return (AItemSettings) super.equipmentSlot(equipmentSlotProvider);
    }

    @Override
    public AItemSettings customDamage(CustomDamageHandler handler) {
        return (AItemSettings) super.customDamage(handler);
    }

    @Override
    public AItemSettings food(FoodComponent foodComponent) {
        return (AItemSettings) super.food(foodComponent);
    }

    @Override
    public AItemSettings maxCount(int maxCount) {
        return (AItemSettings) super.maxCount(maxCount);
    }

    @Override
    public AItemSettings maxDamageIfAbsent(int maxDamage) {
        return (AItemSettings) super.maxDamageIfAbsent(maxDamage);
    }

    @Override
    public AItemSettings maxDamage(int maxDamage) {
        return (AItemSettings) super.maxDamage(maxDamage);
    }

    @Override
    public AItemSettings recipeRemainder(Item recipeRemainder) {
        return (AItemSettings) super.recipeRemainder(recipeRemainder);
    }

    @Override
    public AItemSettings rarity(Rarity rarity) {
        return (AItemSettings) super.rarity(rarity);
    }

    @Override
    public AItemSettings fireproof() {
        return (AItemSettings) super.fireproof();
    }

    @Override
    public AItemSettings requires(FeatureFlag... features) {
        return (AItemSettings) super.requires(features);
    }

    public ItemGroup group() {
        return group;
    }
}
