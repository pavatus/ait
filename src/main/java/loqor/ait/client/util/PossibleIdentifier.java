package loqor.ait.client.util;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

/**
 * An extension of Identifier which checks whether the resource is present
 * todo - rename of class "PossibleIdentifier" no good
 * @author duzo
 */
public class PossibleIdentifier extends Identifier {
    private static final PossibleIdentifier EMPTY = new PossibleIdentifier("");

    private boolean checked;
    private boolean exists;

    protected PossibleIdentifier(String namespace, String path, @Nullable Identifier.ExtraData extraData) {
        super(namespace, path, extraData);
    }

    public PossibleIdentifier(String namespace, String path) {
        super(namespace, path);
    }

    public PossibleIdentifier(String id) {
        super(id);
    }

    protected PossibleIdentifier(Identifier id) {
        this(id.getNamespace(), id.getPath());
    }
    public static PossibleIdentifier of(Identifier id) {
        if (id == null) return empty();

        return new PossibleIdentifier(id);
    }
    public static PossibleIdentifier empty() {
        if (!EMPTY.checked) {
            EMPTY.exists = false;
            EMPTY.checked = true;
        }

        return EMPTY;
    }


    public boolean isPresent() {
        if (!checked) {
            exists = MinecraftClient.getInstance().getResourceManager().getResource(this).isPresent();
            checked = true;
        }

        return exists;
    }

    public PossibleIdentifier orElse(PossibleIdentifier other) {
        return this.isPresent() ? this : other;
    }
    public PossibleIdentifier orElse(Identifier other) {
        return this.orElse(PossibleIdentifier.of(other));
    }
}
