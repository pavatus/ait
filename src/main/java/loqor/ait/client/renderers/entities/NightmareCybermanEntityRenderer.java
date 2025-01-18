package loqor.ait.client.renderers.entities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.model.SkeletonEntityModel;
import net.minecraft.util.Identifier;

import loqor.ait.core.entities.DummyCybermanEntity;

@Environment(value= EnvType.CLIENT)
public class NightmareCybermanEntityRenderer
        extends BipedEntityRenderer<DummyCybermanEntity, SkeletonEntityModel<DummyCybermanEntity>> {
    private static final Identifier TEXTURE = new Identifier("textures/entity/skeleton/skeleton.png");

    @Override
    public Identifier getTexture(DummyCybermanEntity dummyCybermanEntity) {
        return TEXTURE;
    }

    @Override
    protected boolean isShaking(DummyCybermanEntity dummyCybermanEntity) {
        return dummyCybermanEntity.isShaking();
    }
}
