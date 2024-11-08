package dev.pavatus.planet.core;

import dev.pavatus.planet.PlanetModule;
import dev.pavatus.planet.core.item.SpacesuitItem;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import io.wispforest.owo.registration.reflect.ItemRegistryContainer;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.Item;

public class PlanetItems implements ItemRegistryContainer {
    public static final Item SPACESUIT_HELMET = new SpacesuitItem(ArmorMaterials.IRON, ArmorItem.Type.HELMET,
            new OwoItemSettings().group(PlanetModule.ITEM_GROUP).maxDamage(240), false);
    public static final Item SPACESUIT_CHESTPLATE = new SpacesuitItem(ArmorMaterials.IRON, ArmorItem.Type.CHESTPLATE,
            new OwoItemSettings().group(PlanetModule.ITEM_GROUP).maxDamage(240), false);
    public static final Item SPACESUIT_LEGGINGS = new SpacesuitItem(ArmorMaterials.IRON, ArmorItem.Type.LEGGINGS,
            new OwoItemSettings().group(PlanetModule.ITEM_GROUP).maxDamage(240), false);
    public static final Item SPACESUIT_BOOTS = new SpacesuitItem(ArmorMaterials.IRON, ArmorItem.Type.BOOTS,
            new OwoItemSettings().group(PlanetModule.ITEM_GROUP).maxDamage(240), false);
}
