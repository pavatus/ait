package loqor.ait.tardis.data.permissions;

import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.TardisLink;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PermissionHandler extends TardisLink {
    private final Map<UUID, PermissionMap> data;

    public PermissionHandler(Tardis tardis, Map<UUID, PermissionMap> map) {
        super(tardis, TypeId.PERMISSIONS);
        this.data = map;
    }

    public PermissionHandler(Tardis tardis) {
        this(tardis, new HashMap<>());
    }

    public boolean check(ServerPlayerEntity player, Permission permission) {
        return this.getPermissionMap(player).contains(permission);
    }

    public void set(ServerPlayerEntity player, Permission permission, boolean value) {
        PermissionMap map = this.getPermissionMap(player);
        System.out.println("got perm map");

        if (value)
            map.add(permission);
        else
            map.remove(permission);

        System.out.println("aaaa");
        this.sync(); //@TODO theo, with this, you dont have to sync anymore - if you use the
        // PropertiesHandler, it can *sync a boolean value*. you do it above ^ with the value, so just do this
        // logic somewhere else, and use the PropertiesHandler to sync the boolean value you set here, and then get it.
        // The small problem, is that you'll need to store different keys for each permission, so, whether you like it or not, you will
        // need to make it like that. Sorry not sorry.
        System.out.println("aaaaaaahhhh");
    }

    private PermissionMap getPermissionMap(ServerPlayerEntity player) {
        PermissionMap result = data.get(player.getUuid());

        if (result != null)
            return result;

        result = new PermissionMap();
        data.put(player.getUuid(), result);
        return result;
    }
}
