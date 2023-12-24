package mdteam.ait.tardis.variant;

import mdteam.ait.client.renderers.consoles.ConsoleEnum;
import net.minecraft.util.Identifier;

public abstract class ConsoleVariantSchema {
    private final ConsoleEnum parent;
    private final Identifier id;

    public ConsoleVariantSchema(ConsoleEnum parent, Identifier id) {
        this.parent = parent;
        this.id = id;
    }

    public ConsoleEnum parent() { return parent; }
    public Identifier id() { return id; }

    public abstract Identifier texture();
    public abstract Identifier emission();
}
