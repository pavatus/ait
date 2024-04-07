package loqor.ait.tardis.data.permissions;

import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.TardisLink;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.*;

public class PermissionHandler extends TardisLink {

    private final Map<UUID, PermissionMap> permissions = new HashMap<>();

    public PermissionHandler(Tardis tardis) {
        super(tardis, "permissions");
    }

    public boolean check(ServerPlayerEntity player, PermissionNode permission) {
        return permissions.get(player.getUuid()).get(permission);
    }

    public void set(ServerPlayerEntity player, PermissionNode permission, boolean value) {
        permissions.getOrDefault(player.getUuid(), new PermissionMap())
                .set(permission, value);
    }
}
