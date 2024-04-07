package loqor.ait.tardis.data.permissions;

public record Permission(String name, Permission... children) {

    public PermissionNode node(PermissionNode parent) {
        return new PermissionNode(name, parent, null);
    }
}
