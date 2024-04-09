package loqor.ait.tardis.data.permissions;

import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.TardisLink;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PermissionHandler extends TardisLink {
    private final Map<UUID, PermissionMap> permissions;

    public PermissionHandler(Tardis tardis, Map<UUID, PermissionMap> map) {
        super(tardis, "permissions");
        this.permissions = map;
    }

    public PermissionHandler(Tardis tardis) {
        this(tardis, new HashMap<>());
    }

    public boolean check(ServerPlayerEntity player, Permission permission) {
        return permissions.get(player.getUuid()).get(permission);
    }

    public void set(ServerPlayerEntity player, Permission permission, boolean value) {
        permissions.getOrDefault(player.getUuid(), new PermissionMap())
                .set(permission, value);
    }
}
