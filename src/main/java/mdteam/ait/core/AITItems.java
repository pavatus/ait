package mdteam.ait.core;

import io.wispforest.owo.itemgroup.OwoItemSettings;
import io.wispforest.owo.registration.reflect.ItemRegistryContainer;
import mdteam.ait.AITMod;
import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.core.components.block.interior_door.InteriorDataComponent;
import mdteam.ait.core.item.*;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryOps;
import the.mdteam.ait.TardisDesktopSchema;

import java.util.List;

public class AITItems implements ItemRegistryContainer {
    public static final Item TARDIS_ITEM = new TardisItemBuilder(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP));
    // public static final Item TOYOTA_ITEM = new TardisItemBuilder(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP), ExteriorEnum.TOYOTA);
    public static final Item REMOTE_ITEM = new RemoteItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP).maxCount(1));
    public static final Item GOLDEN_TARDIS_KEY = new KeyItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP));

    // This is only temporary for testing until proper interior/exterior changing is done fixme todo whjatever
    public static void createExteriorItems() {
        for (ExteriorEnum exterior : ExteriorEnum.values()) {
            Registry.register(Registries.ITEM, exterior.name().toLowerCase() + "_item", new ExteriorSelectItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP),exterior));
        }
    }
    public static void createInteriorItems() {
        for (TardisDesktopSchema interior : AITDesktops.iterator()) {
            Registry.register(Registries.ITEM, interior.id().toString().toLowerCase() + "_item", new InteriorSelectItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP),interior.id()));
        }
    }
}
