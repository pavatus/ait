package mdteam.ait.core;

import io.wispforest.owo.Owo;
import mdteam.ait.AITMod;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import io.wispforest.owo.registration.reflect.ItemRegistryContainer;
import mdteam.ait.core.item.TardisItemBuilder;
import net.minecraft.item.Item;

public class AITItems implements ItemRegistryContainer {
    public static final Item AITMODCREATIVETAB = new Item(new OwoItemSettings());

    public static final Item TARDIS_ITEM = new TardisItemBuilder(new OwoItemSettings());
}
