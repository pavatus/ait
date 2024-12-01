package dev.pavatus.planet.core;

import dev.pavatus.planet.PlanetModule;
import dev.pavatus.planet.core.item.PlanetToolMaterial;
import dev.pavatus.planet.core.item.SpacesuitItem;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import io.wispforest.owo.registration.reflect.ItemRegistryContainer;

import net.minecraft.item.*;

public class PlanetItems implements ItemRegistryContainer {
    public static final Item SPACESUIT_HELMET = new SpacesuitItem(ArmorMaterials.IRON, ArmorItem.Type.HELMET,
            new OwoItemSettings().group(PlanetModule.ITEM_GROUP).maxDamage(240), true);
    public static final Item SPACESUIT_CHESTPLATE = new SpacesuitItem(ArmorMaterials.IRON, ArmorItem.Type.CHESTPLATE,
            new OwoItemSettings().group(PlanetModule.ITEM_GROUP).maxDamage(240), true);
    public static final Item SPACESUIT_LEGGINGS = new SpacesuitItem(ArmorMaterials.IRON, ArmorItem.Type.LEGGINGS,
            new OwoItemSettings().group(PlanetModule.ITEM_GROUP).maxDamage(240), true);
    public static final Item SPACESUIT_BOOTS = new SpacesuitItem(ArmorMaterials.IRON, ArmorItem.Type.BOOTS,
            new OwoItemSettings().group(PlanetModule.ITEM_GROUP).maxDamage(240), true);

    // Martian Stone Tools
    public static final Item MARTIAN_STONE_SWORD = new SwordItem(PlanetToolMaterial.MARTIAN_STONE, 5, 3f, new OwoItemSettings().group(PlanetModule.ITEM_GROUP));
    public static final Item ANORTHOSITE_SWORD = new SwordItem(PlanetToolMaterial.ANORTHOSITE, 5, 3f, new OwoItemSettings().group(PlanetModule.ITEM_GROUP));

    public static final Item MARTIAN_STONE_SHOVEL = new ShovelItem(PlanetToolMaterial.MARTIAN_STONE, 0, 0f, new OwoItemSettings().group(PlanetModule.ITEM_GROUP));
    public static final Item ANORTHOSITE_SHOVEL = new ShovelItem(PlanetToolMaterial.ANORTHOSITE, 0, 0f, new OwoItemSettings().group(PlanetModule.ITEM_GROUP));

    public static final Item MARTIAN_STONE_PICKAXE = new PickaxeItem(PlanetToolMaterial.MARTIAN_STONE, 2, 2f, new OwoItemSettings().group(PlanetModule.ITEM_GROUP));
    public static final Item ANORTHOSITE_PICKAXE = new PickaxeItem(PlanetToolMaterial.ANORTHOSITE, 2, 2f, new OwoItemSettings().group(PlanetModule.ITEM_GROUP));

    public static final Item MARTIAN_STONE_AXE = new AxeItem(PlanetToolMaterial.MARTIAN_STONE, 3, 1f, new OwoItemSettings().group(PlanetModule.ITEM_GROUP));
    public static final Item ANORTHOSITE_AXE = new AxeItem(PlanetToolMaterial.ANORTHOSITE, 3, 1f, new OwoItemSettings().group(PlanetModule.ITEM_GROUP));

    public static final Item MARTIAN_STONE_HOE = new HoeItem(PlanetToolMaterial.MARTIAN_STONE, 1, 2f, new OwoItemSettings().group(PlanetModule.ITEM_GROUP));
    public static final Item ANORTHOSITE_HOE = new HoeItem(PlanetToolMaterial.ANORTHOSITE, 1, 2f, new OwoItemSettings().group(PlanetModule.ITEM_GROUP));

}
