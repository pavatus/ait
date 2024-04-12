package loqor.ait.tardis.data.permissions;

import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.TardisLink;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PermissionHandler extends TardisLink {
    private final Map<UUID, PermissionMap> permissions;
    private static final String KEY = "permissions";

    public PermissionHandler(Tardis tardis, Map<UUID, PermissionMap> map) {
        super(tardis, "permissions");
        this.permissions = map;
    }

    public PermissionHandler(Tardis tardis) {
        this(tardis, new HashMap<>());
    }

    public boolean check(ServerPlayerEntity player, Permission permission) {
        return this.getPermissionMap(player).get(permission);
    }

    public void set(ServerPlayerEntity player, Permission permission, boolean value) {
        this.getPermissionMap(player).put(permission, value);
        this.sync();
    }

    private PermissionMap getPermissionMap(ServerPlayerEntity player) {
        PermissionMap result = permissions.get(player.getUuid());

        if (result != null)
            return result;

        permissions.put(player.getUuid(), new PermissionMap());
        return this.getPermissionMap(player);
    }

    @Override
    protected void sync() {
        PropertiesHandler.set(this.findTardis().get(), KEY, permissions);
    }
}
