package mdteam.ait.client.models.doors;

import mdteam.ait.AITMod;
import mdteam.ait.client.animation.exterior.door.DoorAnimations;
import mdteam.ait.core.blockentities.DoorBlockEntity;
import mdteam.ait.tardis.handler.DoorHandler;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class BoothDoorModel extends DoorModel {

    private final ModelPart booth;

    public BoothDoorModel(ModelPart root) {
        super(RenderLayer::getEntityCutoutNoCull);
        this.booth = root.getChild("booth");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData booth = modelPartData.addChild("booth", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 2.0F));

        ModelPartData floor = booth.addChild("floor", ModelPartBuilder.create().uv(74, 48).cuboid(-9.0F, -1.01F, -9.0F, 18.0F, 0.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData base_r1 = floor.addChild("base_r1", ModelPartBuilder.create().uv(58, 40).cuboid(-9.5F, -1.0F, 6.5F, 19.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        ModelPartData walls = booth.addChild("walls", ModelPartBuilder.create().uv(131, 108).cuboid(7.0F, -41.0F, -9.0F, 2.0F, 40.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r1 = walls.addChild("cube_r1", ModelPartBuilder.create().uv(131, 0).cuboid(7.0F, -40.0F, -9.0F, 2.0F, 40.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -1.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        ModelPartData roof = booth.addChild("roof", ModelPartBuilder.create().uv(55, 4).cuboid(-9.5F, -42.0F, -9.5F, 19.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r2 = roof.addChild("cube_r2", ModelPartBuilder.create().uv(58, 32).cuboid(-9.0F, -50.0F, 7.0F, 18.0F, 4.0F, 3.0F, new Dilation(0.5F))
                .uv(113, 43).cuboid(-9.0F, -50.0F, 7.0F, 18.0F, 8.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        ModelPartData door = booth.addChild("door", ModelPartBuilder.create().uv(93, 67).cuboid(-13.5F, -41.0F, -0.5F, 14.0F, 40.0F, 1.0F, new Dilation(0.0F))
                .uv(29, 111).cuboid(-13.5F, -41.0F, 0.0F, 14.0F, 40.0F, 0.0F, new Dilation(0.001F))
                .uv(14, 37).cuboid(-13.5F, -22.0F, -1.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(6.5F, 0.0F, -9.5F));
        return TexturedModelData.of(modelData, 256, 256);
    }

    @Override
    public ModelPart getPart() {
        return booth;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        booth.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Override
    public Animation getAnimationForDoorState(DoorHandler.DoorStateEnum state) {
        return switch (state) {
            case CLOSED -> DoorAnimations.K2BOOTH_EXTERIOR_CLOSE_ANIMATION;
            case FIRST -> DoorAnimations.K2BOOTH_EXTERIOR_OPEN_ANIMATION;
            case SECOND, BOTH -> Animation.Builder.create(0).build();
        };
    }

    @Override
    public void renderWithAnimations(DoorBlockEntity door, ModelPart root, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float pAlpha) {
        matrices.push();
        /*this.booth.getChild("door").yaw = door.getLeftDoorRotation() == 0 ? 0 : -1.575f;*/
        matrices.scale(0.95f, 0.95f, 0.95f);
        matrices.translate(0, -1.5f, 0);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180f));

        super.renderWithAnimations(door, root, matrices, vertices, light, overlay, red, green, blue, pAlpha);
        matrices.pop();
    }
}