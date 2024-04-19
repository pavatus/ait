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
        return this.getPermissionMap(player).get(permission);
    }

    /**
     *
     * @param player
     * @param permission
     * @param value
     * @return The value set.
     */
    public boolean set(ServerPlayerEntity player, Permission permission, boolean value) {
        PermissionMap map = this.getPermissionMap(player);
        map.put(permission, value);

        this.sync();
        return value;
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
