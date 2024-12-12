package dev.pavatus.gun.core.item;

import dev.pavatus.gun.GunModule;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import io.wispforest.owo.registration.reflect.ItemRegistryContainer;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.util.Rarity;

import static loqor.ait.core.AITItems.DRIFTING_MUSIC_DISC;
import static loqor.ait.core.AITItems.MERCURY_MUSIC_DISC;

public class GunItems implements ItemRegistryContainer {
    public static final Item CULT_STASER = new BaseGunItem(new OwoItemSettings().maxCount(1).rarity(Rarity.RARE));
    public static final Item CULT_STASER_RIFLE = new StaserRifleItem(new OwoItemSettings().maxCount(1).rarity(Rarity.RARE));
    public static final Item STASER_BOLT_MAGAZINE = new StaserBoltMagazine(new OwoItemSettings().maxCount(1).rarity(Rarity.RARE));

    static {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.addAfter(Items.CROSSBOW, CULT_STASER);
            entries.addAfter(CULT_STASER, CULT_STASER_RIFLE);
            entries.addAfter(Items.TIPPED_ARROW, STASER_BOLT_MAGAZINE);
        });
    }
}
