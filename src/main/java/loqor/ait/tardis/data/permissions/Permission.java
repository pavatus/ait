package loqor.ait.tardis.data.permissions;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public record Permission(String name, Permission parent, Map<String, Permission> children) implements PermissionLike {

    public interface USE {
        PermissionWrapper CONSOLE = new PermissionWrapper("console"); // Permission to use console
        PermissionWrapper TRAVEL = new PermissionWrapper("travel"); // Permission to travel inside the tardis
        PermissionWrapper ATTUNE = new PermissionWrapper("attune"); // Permission to attune sonic or key to a tardis
    }

    public interface MODIFY {
        PermissionWrapper PLACE = new PermissionWrapper("place"); // Permission to place blocks
        PermissionWrapper BREAK = new PermissionWrapper("break"); // Permission to break blocks
        PermissionWrapper INTERACT = new PermissionWrapper("break"); // Permission to interact with blocks
        PermissionWrapper CONTAINER = new PermissionWrapper("container"); // Permission to open containers
    }

    public interface SPECIAL {
        PermissionWrapper CLOAK = new PermissionWrapper("cloak"); // Permission to see through cloak
    }

    private static final Permission PERMISSIONS = withChildren(
            "tardis", null,
            base -> withChildren(
                    "use", base,
                    USE.CONSOLE, USE.TRAVEL, USE.ATTUNE
            ),
            base -> withChildren(
                    "modify", base,
                    MODIFY.PLACE, MODIFY.BREAK, MODIFY.INTERACT, MODIFY.CONTAINER
            ),
            base -> withChildren(
                    "special", base,
                    SPECIAL.CLOAK
            )
    );

    @SafeVarargs
    public static Permission withChildren(String name, Permission parent, Function<Permission, Permission>... children) {
        Permission base = new Permission(name, parent, new HashMap<>());

        for (Function<Permission, Permission> func : children) {
            Permission child = func.apply(base);
            base.children.put(child.name(), child);
        }

        return base;
    }

    public static Permission withChildren(String name, Permission parent, PermissionWrapper... children) {
        Permission base = new Permission(name, parent, new HashMap<>());

        for (PermissionWrapper wrapper : children) {
            wrapper.construct(base, null);
            base.children.put(wrapper.getName(), wrapper.get());
        }

        return base;
    }

    /**
     * @return Returns the fully qualified permission id, unlike the permission property.
     */
    public String getId() {
        String base = "";

        if (this.parent != null)
            base = parent.getId() + ".";

        return base + this.name;
    }

    // e.g. tardis.use.attune
    public static Permission from(String id) {
        String[] parts = id.split("\\.");
        Permission node = PERMISSIONS;

        for (int i = 1; i < parts.length; i++) {
            node = node.children.get(parts[i]);
        }

        return node;
    }

    @Override
    @SuppressWarnings("EqualsBetweenInconvertibleTypes") // shut up IntelliJ
    public boolean equals(Object obj) {
        if (obj instanceof PermissionWrapper wrapper)
            return wrapper.equals(this); // delegates to PermissionWrapper's equals method

        if (obj instanceof Permission other)
            return this == other; // finally checks if the permission is the same!

        return false;
    }

    @Override
    public String toString() {
        return this.getId();
    }
}
