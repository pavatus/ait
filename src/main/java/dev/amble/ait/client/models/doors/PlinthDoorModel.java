package dev.amble.ait.client.models.doors;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

import dev.amble.ait.AITMod;
import dev.amble.ait.api.link.v2.block.AbstractLinkableBlockEntity;
import dev.amble.ait.core.tardis.handler.DoorHandler;

public class PlinthDoorModel extends DoorModel {

    private final ModelPart plinth;

    public PlinthDoorModel(ModelPart root) {
        this.plinth = root.getChild("plinth");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData plinth = modelPartData.addChild("plinth", ModelPartBuilder.create(),
                ModelTransform.pivot(-20.0F, 27.0F, 0.0F));

        plinth.addChild("door", ModelPartBuilder.create().uv(72, 61).cuboid(-12.0F, -42.0F, 0.0F, 12.0F, 42.0F, 2.0F,
                new Dilation(0.0F)), ModelTransform.pivot(26.0F, -3.0F, -8.0F));

        plinth.addChild("frame",
                ModelPartBuilder.create().uv(56, 0).cuboid(11.0F, -48.0F, -9.0F, 18.0F, 3.0F, 3.0F, new Dilation(0.0F))
                        .uv(28, 79).cuboid(26.0F, -45.0F, -8.0F, 2.0F, 42.0F, 2.0F, new Dilation(0.0F)).uv(36, 79)
                        .cuboid(12.0F, -45.0F, -8.0F, 2.0F, 42.0F, 2.0F, new Dilation(0.0F)),
                ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        return TexturedModelData.of(modelData, 128, 128);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red,
            float green, float blue, float alpha) {
        plinth.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Override
    public void renderWithAnimations(AbstractLinkableBlockEntity linkableBlockEntity, ModelPart root, MatrixStack matrices,
                                     VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float pAlpha) {
        matrices.push();
        matrices.translate(0, -1.5f, 0);
        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(180f));

        if (!AITMod.CONFIG.CLIENT.ANIMATE_DOORS) {
            plinth.getChild("door").yaw = linkableBlockEntity.tardis().get().door().isOpen() ? -1.75f : 0f;
        } else {
            float maxRot = 90f;
            plinth.getChild("door").yaw = -(float) Math.toRadians(maxRot*linkableBlockEntity.tardis().get().door().getLeftRot());
        }
        super.renderWithAnimations(linkableBlockEntity, root, matrices, vertices, light, overlay, red, green, blue, pAlpha);

        matrices.pop();
    }

    @Override
    public ModelPart getPart() {
        return plinth;
    }

    @Override
    public Animation getAnimationForDoorState(DoorHandler.AnimationDoorState state) {
        return Animation.Builder.create(0).build();
    }
}
