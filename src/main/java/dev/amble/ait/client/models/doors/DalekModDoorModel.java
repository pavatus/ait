package dev.amble.ait.client.models.doors;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

import dev.amble.ait.AITMod;
import dev.amble.ait.api.link.v2.block.AbstractLinkableBlockEntity;
import dev.amble.ait.core.tardis.handler.DoorHandler;

public class DalekModDoorModel extends DoorModel {
    private final ModelPart dalekmod;
    public DalekModDoorModel(ModelPart root) {
        this.dalekmod = root.getChild("dalekmod");

    }
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData dalekmod = modelPartData.addChild("dalekmod", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData Doors = dalekmod.addChild("Doors", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 2.0F, 2.0F));

        ModelPartData right_door = Doors.addChild("right_door", ModelPartBuilder.create().uv(48, 141).cuboid(0.0F, -17.0F, 0.0F, 8.0F, 32.0F, 2.0F, new Dilation(0.0F))
        .uv(144, 128).cuboid(0.0F, -18.0F, 0.0F, 8.0F, 32.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-8.0F, -16.0F, -9.0F));

        ModelPartData left_door = Doors.addChild("left_door", ModelPartBuilder.create().uv(68, 162).cuboid(-8.0F, -17.0F, 0.0F, 8.0F, 32.0F, 2.0F, new Dilation(0.0F))
        .uv(88, 162).cuboid(-8.0F, -18.0F, 0.0F, 8.0F, 32.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(8.0F, -16.0F, -9.0F));
        return TexturedModelData.of(modelData, 256, 256);
    }
    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red,
                       float green, float blue, float alpha) {
        dalekmod.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Override
    public void renderWithAnimations(AbstractLinkableBlockEntity doorEntity, ModelPart root, MatrixStack matrices,
                                     VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float pAlpha) {
        if (!AITMod.CONFIG.CLIENT.ANIMATE_DOORS) {
            DoorHandler door = doorEntity.tardis().get().door();

            this.dalekmod.getChild("Doors").getChild("left_door").yaw = (door.isLeftOpen() || door.isOpen()) ? -5.0f : 0.0F;
            this.dalekmod.getChild("Doors").getChild("right_door").yaw = (door.isRightOpen() || door.areBothOpen())
                    ? 5.0f
                    : 0.0F;
        } else {
            float maxRot = 80f;
        this.dalekmod.getChild("Doors").getChild("left_door").yaw = (float) Math.toRadians(maxRot*doorEntity.tardis().get().door().getLeftRot());
        this.dalekmod.getChild("Doors").getChild("right_door").yaw = (float) -Math.toRadians(maxRot*doorEntity.tardis().get().door().getRightRot());
        }

        matrices.push();
        matrices.scale(0.955F, 0.955F, 0.955F);
        matrices.translate(0, -1.5, 0.1);
        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(180));

        super.renderWithAnimations(doorEntity, root, matrices, vertices, light, overlay, red, green, blue, pAlpha);
        matrices.pop();
    }

    @Override
    public Animation getAnimationForDoorState(DoorHandler.AnimationDoorState state) {
        return Animation.Builder.create(0).build();/*return switch (state) {
            case CLOSED -> DoorAnimations.INTERIOR_BOTH_CLOSE_ANIMATION;
            case FIRST -> DoorAnimations.INTERIOR_FIRST_OPEN_ANIMATION;
            case SECOND -> DoorAnimations.INTERIOR_SECOND_OPEN_ANIMATION;
            case BOTH -> DoorAnimations.INTERIOR_BOTH_OPEN_ANIMATION;
        };*/
    }

    @Override
    public ModelPart getPart() {
        return dalekmod;
    }
}