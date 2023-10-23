package mdteam.ait.core.helper;

import mdteam.ait.AITMod;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.io.Serializable;

public class SerialDimension implements Serializable {

    private static final long serialVersionUID = 1L;

    private transient World dimension;
    private String dimensionValue;
    private String dimensionRegistry;

    public SerialDimension(World dimension) {
        set(dimension);
    }

    public String getValue() {
        return dimensionValue;
    }

    public String getRegistry() {
        return dimensionRegistry;
    }

    public World get() {
        if(dimension == null) {
            dimension = AITMod.mcServer.getWorld(RegistryKey.of(RegistryKey.ofRegistry(new Identifier(dimensionRegistry)),new Identifier(dimensionValue)));
        }
        return dimension;
    }


    public void set(World dimension) {
        this.dimension = dimension;
        this.dimensionValue = this.dimension.getRegistryKey().getValue().toString();
        this.dimensionRegistry = this.dimension.getRegistryKey().getRegistry().toString();
    }

}
