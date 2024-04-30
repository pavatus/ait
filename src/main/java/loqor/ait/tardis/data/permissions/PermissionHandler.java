package loqor.ait.tardis.data.permissions;

import loqor.ait.tardis.data.TardisLink;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PermissionHandler extends TardisLink {
    private final Map<UUID, PermissionMap> data;

    public PermissionHandler(Map<UUID, PermissionMap> map) {
        super(Id.PERMISSIONS);
        this.data = map;
    }

    public PermissionHandler() {
        this(new HashMap<>());
    }

    public boolean check(ServerPlayerEntity player, Permission permission) {
        return this.getPermissionMap(player).get(permission);
    }

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
