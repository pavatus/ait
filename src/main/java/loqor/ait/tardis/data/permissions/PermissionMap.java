package loqor.ait.tardis.data.permissions;

import java.util.HashMap;
import java.util.Map;

public class PermissionMap {

    private final Map<PermissionLike, Boolean> map = new HashMap<>();

    public void set(PermissionLike permission, boolean value) {
        this.map.put(permission, value);
    }

    public boolean get(PermissionLike permission) {
        return map.get(permission);
    }
}
