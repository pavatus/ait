package mdteam.ait.client.models.exteriors;

import mdteam.ait.client.animation.exterior.door.DoorAnimations;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.entities.FallingTardisEntity;
import mdteam.ait.tardis.handler.DoorHandler;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.util.math.MatrixStack;

// Made with Blockbench 4.9.1
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class TardimExteriorModel extends ExteriorModel {
    private final ModelPart tardis;

    public TardimExteriorModel(ModelPart root) {
        super(RenderLayer::getEntityCutoutNoCull);
        this.tardis = root.getChild("tardis");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData tardis = modelPartData.addChild("tardis", ModelPartBuilder.create().uv(62, 58).cuboid(-11.0F, -32.0F, -8.0F, 3.0F, 32.0F, 16.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-8.0F, -40.0F, -8.0F, 16.0F, 8.0F, 16.0F, new Dilation(0.0F))
                .uv(78, 26).cuboid(-8.0F, -0.02F, -8.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.0F))
                .uv(62, 9).cuboid(-8.0F, 0.02F, -8.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData cube_r1 = tardis.addChild("cube_r1", ModelPartBuilder.create().uv(0, 25).cuboid(-11.0F, -32.0F, -8.0F, 3.0F, 32.0F, 16.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        ModelPartData cube_r2 = tardis.addChild("cube_r2", ModelPartBuilder.create().uv(39, 25).cuboid(-11.0F, -32.0F, -8.0F, 3.0F, 32.0F, 16.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        ModelPartData left_door = tardis.addChild("left_door", ModelPartBuilder.create().uv(23, 74).cuboid(-6.5F, -32.0F, -1.5F, 8.0F, 32.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(6.5F, 0.0F, -9.5F));

        ModelPartData right_door = tardis.addChild("right_door", ModelPartBuilder.create().uv(0, 74).cuboid(-1.5F, -32.0F, -1.5F, 8.0F, 32.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(-6.5F, 0.0F, -9.5F));
        return TexturedModelData.of(modelData, 256, 256);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        tardis.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart getPart() {
        return tardis;
    }

    @Override
    public void renderWithAnimations(ExteriorBlockEntity exterior, ModelPart root, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float pAlpha) {
        matrices.push();
        // matrices.scale(0.6F,0.6f,0.6f);
        matrices.translate(0, -1.5f, 0);

        /*this.tardis.getChild("left_door").yaw = exterior.getRightDoor() ? 0 : -1.575f;
        this.tardis.getChild("right_door").yaw = exterior.getLeftDoor() ? 0 : 1.575f;*/

        DoorHandler handler = exterior.tardis().getDoor();

        this.tardis.getChild("left_door").yaw = (handler.isLeftOpen() || handler.isOpen())  ? -1.575f : 0.0F;
        this.tardis.getChild("right_door").yaw = (handler.isRightOpen() || handler.isBothOpen()) ? 1.575f : 0.0F;

        super.renderWithAnimations(exterior, root, matrices, vertices, light, overlay, red, green, blue, pAlpha);

        matrices.pop();
    }

    @Override
    public Animation getAnimationForDoorState(DoorHandler.DoorStateEnum state) {
        return switch (state) {
            case CLOSED -> DoorAnimations.EXTERIOR_BOTH_CLOSE_ANIMATION;
            case FIRST -> DoorAnimations.EXTERIOR_FIRST_OPEN_ANIMATION;
            case SECOND -> DoorAnimations.EXTERIOR_SECOND_OPEN_ANIMATION;
            case BOTH -> DoorAnimations.EXTERIOR_BOTH_OPEN_ANIMATION;
        };
    }

    @Override
    public void renderFalling(FallingTardisEntity falling, ModelPart root, MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        matrices.push();
        matrices.translate(0, -1.5f, 0);

        super.renderFalling(falling, root, matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        matrices.pop();
    }
}