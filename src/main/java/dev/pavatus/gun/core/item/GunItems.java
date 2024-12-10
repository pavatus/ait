package dev.pavatus.gun.core.item;

import dev.pavatus.gun.GunModule;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import io.wispforest.owo.registration.reflect.ItemRegistryContainer;

import net.minecraft.item.Item;
import net.minecraft.util.Rarity;

public class GunItems implements ItemRegistryContainer {
    public static final Item CULT_STASER = new BaseGunItem(new OwoItemSettings().maxCount(1).rarity(Rarity.RARE).group(GunModule.instance().getItemGroup()));
    public static final Item CULT_STASER_RIFLE = new StaserRifleItem(new OwoItemSettings().maxCount(1).rarity(Rarity.RARE).group(GunModule.instance().getItemGroup()));
    public static final Item STASER_BOLT_MAGAZINE = new StaserBoltMagazine(new OwoItemSettings().maxCount(1).rarity(Rarity.RARE).group(GunModule.instance().getItemGroup()));
}
