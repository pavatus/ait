package loqor.ait.core.util;

import java.util.EnumMap;
import java.util.function.Supplier;

import loqor.ait.core.item.WearableArmorItem;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Lazy;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.Util;

public enum AITArmorMaterials implements StringIdentifiable, AITArmorMaterial {
    LEATHER("leather", 5, (EnumMap)Util.make(new EnumMap(WearableArmorItem.Type.class), (map) -> {
        map.put(WearableArmorItem.Type.BOOTS, 1);
        map.put(WearableArmorItem.Type.LEGGINGS, 2);
        map.put(WearableArmorItem.Type.CHESTPLATE, 3);
        map.put(WearableArmorItem.Type.HELMET, 1);
    }), 15, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, () -> {
        return Ingredient.ofItems(Items.LEATHER);
    }),
    CHAIN("chainmail", 15, (EnumMap)Util.make(new EnumMap(WearableArmorItem.Type.class), (map) -> {
        map.put(WearableArmorItem.Type.BOOTS, 1);
        map.put(WearableArmorItem.Type.LEGGINGS, 4);
        map.put(WearableArmorItem.Type.CHESTPLATE, 5);
        map.put(WearableArmorItem.Type.HELMET, 2);
    }), 12, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 0.0F, 0.0F, () -> {
        return Ingredient.ofItems(Items.IRON_INGOT);
    }),
    IRON("iron", 15, (EnumMap)Util.make(new EnumMap(WearableArmorItem.Type.class), (map) -> {
        map.put(WearableArmorItem.Type.BOOTS, 2);
        map.put(WearableArmorItem.Type.LEGGINGS, 5);
        map.put(WearableArmorItem.Type.CHESTPLATE, 6);
        map.put(WearableArmorItem.Type.HELMET, 2);
    }), 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F, 0.0F, () -> {
        return Ingredient.ofItems(Items.IRON_INGOT);
    }),
    GOLD("gold", 7, Util.make(new EnumMap(WearableArmorItem.Type.class), (map) -> {
        map.put(WearableArmorItem.Type.BOOTS, 1);
        map.put(WearableArmorItem.Type.LEGGINGS, 3);
        map.put(WearableArmorItem.Type.CHESTPLATE, 5);
        map.put(WearableArmorItem.Type.HELMET, 2);
    }), 25, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 0.0F, 0.0F, () -> {
        return Ingredient.ofItems(Items.GOLD_INGOT);
    }),
    DIAMOND("diamond", 33, Util.make(new EnumMap(WearableArmorItem.Type.class), (map) -> {
        map.put(WearableArmorItem.Type.BOOTS, 3);
        map.put(WearableArmorItem.Type.LEGGINGS, 6);
        map.put(WearableArmorItem.Type.CHESTPLATE, 8);
        map.put(WearableArmorItem.Type.HELMET, 3);
    }), 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2.0F, 0.0F, () -> {
        return Ingredient.ofItems(Items.DIAMOND);
    }),
    TURTLE("turtle", 25, Util.make(new EnumMap(WearableArmorItem.Type.class), (map) -> {
        map.put(WearableArmorItem.Type.BOOTS, 2);
        map.put(WearableArmorItem.Type.LEGGINGS, 5);
        map.put(WearableArmorItem.Type.CHESTPLATE, 6);
        map.put(WearableArmorItem.Type.HELMET, 2);
    }), 9, SoundEvents.ITEM_ARMOR_EQUIP_TURTLE, 0.0F, 0.0F, () -> {
        return Ingredient.ofItems(Items.SCUTE);
    }),
    NETHERITE("netherite", 37, Util.make(new EnumMap(WearableArmorItem.Type.class), (map) -> {
        map.put(WearableArmorItem.Type.BOOTS, 3);
        map.put(WearableArmorItem.Type.LEGGINGS, 6);
        map.put(WearableArmorItem.Type.CHESTPLATE, 8);
        map.put(WearableArmorItem.Type.HELMET, 3);
    }), 15, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, 3.0F, 0.1F, () -> {
        return Ingredient.ofItems(Items.NETHERITE_INGOT);
    });

    public static final StringIdentifiable.Codec<AITArmorMaterials> CODEC = StringIdentifiable.createCodec(AITArmorMaterials::values);
    private static final EnumMap<WearableArmorItem.Type, Integer> BASE_DURABILITY = (EnumMap)Util.make(new EnumMap(WearableArmorItem.Type.class), (map) -> {
        map.put(WearableArmorItem.Type.BOOTS, 13);
        map.put(WearableArmorItem.Type.LEGGINGS, 15);
        map.put(WearableArmorItem.Type.CHESTPLATE, 16);
        map.put(WearableArmorItem.Type.HELMET, 11);
    });
    private final String name;
    private final int durabilityMultiplier;
    private final EnumMap<WearableArmorItem.Type, Integer> protectionAmounts;
    private final int enchantability;
    private final SoundEvent equipSound;
    private final float toughness;
    private final float knockbackResistance;
    private final Lazy<Ingredient> repairIngredientSupplier;

    private AITArmorMaterials(String name, int durabilityMultiplier, EnumMap protectionAmounts, int enchantability, SoundEvent equipSound, float toughness, float knockbackResistance, Supplier repairIngredientSupplier) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.protectionAmounts = protectionAmounts;
        this.enchantability = enchantability;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredientSupplier = new Lazy(repairIngredientSupplier);
    }

    public int getDurability(WearableArmorItem.Type type) {
        return (int)BASE_DURABILITY.get(type) * this.durabilityMultiplier;
    }

    public int getProtection(WearableArmorItem.Type type) {
        return this.protectionAmounts.get(type);
    }

    public int getEnchantability() {
        return this.enchantability;
    }

    public SoundEvent getEquipSound() {
        return this.equipSound;
    }

    public Ingredient getRepairIngredient() {
        return (Ingredient)this.repairIngredientSupplier.get();
    }

    public String getName() {
        return this.name;
    }

    public float getToughness() {
        return this.toughness;
    }

    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }

    public String asString() {
        return this.name;
    }
}
