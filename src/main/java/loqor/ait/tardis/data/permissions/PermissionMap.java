package loqor.ait.tardis.data.permissions;

import java.util.HashMap;
import java.util.Map;

public class PermissionMap {

    private final Map<PermissionNode, Boolean> map = new HashMap<>();

    public void set(PermissionNode permission, boolean value) {
        this.map.put(permission, value);
    }

    public boolean get(PermissionNode permission) {
        return map.get(permission);
    }
}
