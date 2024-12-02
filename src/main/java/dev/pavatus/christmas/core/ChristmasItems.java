package dev.pavatus.christmas.core;

import dev.pavatus.christmas.ChristmasModule;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import io.wispforest.owo.registration.reflect.ItemRegistryContainer;

import net.minecraft.item.*;
import net.minecraft.util.Rarity;

import loqor.ait.core.item.KeyItem;

public class ChristmasItems implements ItemRegistryContainer {
    public static final KeyItem FESTIVE_KEY = new KeyItem(new OwoItemSettings().group(ChristmasModule.ITEM_GROUP).rarity(Rarity.EPIC), KeyItem.Protocols.SNAP,
            KeyItem.Protocols.HAIL);
}
