package mdteam.ait.data;

import net.minecraft.world.World;

import java.io.Serial;
import java.io.Serializable;

public class SerialDimension implements Serializable {

    @Serial
    private static final long serialVersionUID = -8246269793252612741L;

    private final transient World dimension;

    private final String value;
    private final String registry;

    public SerialDimension(World dimension) {
        this.dimension = dimension;

        this.value = this.dimension.getRegistryKey().getValue().toString();
        this.registry = this.dimension.getRegistryKey().getRegistry().toString();
    }

    public String getValue() {
        return value;
    }

    public String getRegistry() {
        return registry;
    }

    public World get() {
        return dimension;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SerialDimension other)
            return this.value.equals(other.getValue()) &&
                    this.registry.equals(other.getRegistry());

        return false;
    }
}
