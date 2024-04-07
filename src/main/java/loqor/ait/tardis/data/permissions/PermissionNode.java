package loqor.ait.tardis.data.permissions;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public record PermissionNode(String name, @Nullable PermissionNode parent, Map<String, PermissionNode> children) {

    private static final PermissionNode PERMISSIONS = create(
            "tardis", null,
            base -> create(
                    USE.self(), base,
                    USE.TRAVEL::node,
                    USE.ATTUNE::node
            )
    );

    private static PermissionNode create(String name, PermissionNode base, Function<PermissionNode, PermissionNode>... funcs) {
        PermissionNode parent = new PermissionNode(name, base, new HashMap<>());

        for (Function<PermissionNode, PermissionNode> func : funcs) {
            PermissionNode node = func.apply(parent);
            parent.children.put(node.name, node);
        }

        return parent;
    }

    public interface USE extends PermissionGroup {
        Permission ATTUNE = new Permission("attune"); // the key
        Permission TRAVEL = new Permission("travel");

        static String self() {
            return "use"; // the console
        }
    }

    // e.g. tardis.use.attune
    public static PermissionNode from(String id) {
        String[] parts = id.split("\\.");
        PermissionNode node = PERMISSIONS;

        for (int i = 1; i < parts.length; i++) {
            node = node.get(parts[i]);
        }

        return node;
    }

    public PermissionNode get(String id) {
        return this.children.get(id);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PermissionNode node && this.getId().equals(node.getId());
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
}
