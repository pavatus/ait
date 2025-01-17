package loqor.ait.data.schema.exterior.variant.dalek_mod;


import loqor.ait.AITMod;
import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.core.tardis.animation.ExteriorAnimation;
import loqor.ait.core.tardis.animation.PulsatingAnimation;
import loqor.ait.data.Loyalty;
import loqor.ait.data.schema.door.DoorSchema;
import loqor.ait.data.schema.door.impl.DalekModDoorVariant;
import loqor.ait.data.schema.exterior.ExteriorVariantSchema;
import loqor.ait.data.schema.exterior.category.DalekModCategory;
import loqor.ait.registry.impl.door.DoorRegistry;


public abstract class DalekModVariant extends ExteriorVariantSchema {
    protected static final String TEXTURE_PATH = "textures/blockentities/exteriors/dalek_mod/dalek_mod_";

    protected DalekModVariant(Integer number) {
        super(DalekModCategory.REFERENCE, AITMod.id("exterior/dalek_mod/" + number),
                new Loyalty(Loyalty.Type.OWNER));
    }

    @Override
    public ExteriorAnimation animation(ExteriorBlockEntity exterior) {
        return new PulsatingAnimation(exterior);
    }

    @Override
    public DoorSchema door() {
        return DoorRegistry.REGISTRY.get(DalekModDoorVariant.REFERENCE);
    }


    @Override
    public boolean hasPortals() {
        return false;
    }

}
