package mdteam.ait.api.tardis;

import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.tardis.wrapper.client.manager.ClientTardisManager;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import java.util.UUID;

public abstract class LinkableItem extends Item {
    public LinkableItem(Settings settings) {
        super(settings);
    }

    // could b static? idk but overriding static methods isnt possible.
    public void link(ItemStack stack, Tardis tardis) {
        this.link(stack, tardis.getUuid());
    }

    public void link(ItemStack stack, UUID uuid) {
        NbtCompound nbt = stack.getOrCreateNbt();

        nbt.putString("tardis", uuid.toString());
    }

    public static Tardis getTardis(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();

        if (!(nbt.contains("tardis"))) return null;

        UUID uuid = UUID.fromString(nbt.getString("tardis"));

        if (TardisUtil.isClient()) {
            return ClientTardisManager.getInstance().getLookup().get(uuid);
        }

        return ServerTardisManager.getInstance().getTardis(uuid);
    }
}
