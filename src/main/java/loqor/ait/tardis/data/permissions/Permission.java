package loqor.ait.tardis.data.permissions;

import java.util.Arrays;

public record Permission(String name, Permission... children) {

    public PermissionNode node(PermissionNode parent) {
        return PermissionNode.create(name, parent, children);
    }
}
