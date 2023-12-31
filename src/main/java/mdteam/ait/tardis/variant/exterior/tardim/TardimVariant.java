package mdteam.ait.tardis.variant.exterior.tardim;

import mdteam.ait.AITMod;
import mdteam.ait.client.animation.ExteriorAnimation;
import mdteam.ait.client.animation.PulsatingAnimation;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.registry.DoorRegistry;
import mdteam.ait.tardis.exterior.TardimExterior;
import mdteam.ait.tardis.variant.door.DoorSchema;
import mdteam.ait.tardis.variant.door.TardimDoorVariant;
import mdteam.ait.tardis.variant.exterior.ExteriorVariantSchema;
import net.minecraft.util.Identifier;

// a useful class for creating tardim variants as they all have the same filepath you know
public abstract class TardimVariant extends ExteriorVariantSchema {
    private final String name;
    protected static final String TEXTURE_PATH = "textures/blockentities/exteriors/tardim/tardim_";

    protected TardimVariant(String name, String modId) { // idk why i added the modid bit i dont use it later lol
        super(TardimExterior.REFERENCE, new Identifier(modId, "exterior/tardim/" + name));

        this.name = name;
    }
    protected TardimVariant(String name) {
        this(name, AITMod.MOD_ID);
    }

    @Override
    public ExteriorAnimation animation(ExteriorBlockEntity exterior) {
        return new PulsatingAnimation(exterior);
    }

    @Override
    public DoorSchema door() {
        return DoorRegistry.REGISTRY.get(TardimDoorVariant.REFERENCE);
    }

}