package dev.pavatus.christmas.core;

import dev.pavatus.christmas.ChristmasModule;
import io.wispforest.owo.itemgroup.OwoItemSettings;

import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import loqor.ait.AITMod;
import loqor.ait.core.item.KeyItem;

public class ChristmasItems {
    public static void init() {
        if (ChristmasModule.Feature.FESTIVE_KEY.isUnlocked()) {
            register(new KeyItem(new OwoItemSettings().group(ChristmasModule.instance().getItemGroup()).rarity(Rarity.EPIC)), "festive_key");
        }
    }
    public static <T extends Item> T register(T item, Identifier id) {
        return Registry.register(Registries.ITEM, id, item);
    }
    public static <T extends Item> T register(T item, String name) {
        return register(item, AITMod.id(name));
    }
}
