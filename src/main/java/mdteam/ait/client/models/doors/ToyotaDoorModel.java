package mdteam.ait.client.models.doors;

import mdteam.ait.AITMod;
import mdteam.ait.core.blockentities.DoorBlockEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class ToyotaDoorModel extends DoorModel {
    public static final Identifier TEXTURE = new Identifier(AITMod.MOD_ID, "textures/blockentities/doors/toyota.png");
    private final ModelPart door;
    public ToyotaDoorModel(ModelPart root) {
        this.door = root.getChild("door");
    }
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData door = modelPartData.addChild("door", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData Base_r1 = door.addChild("Base_r1", ModelPartBuilder.create().uv(0, 0).cuboid(-19.0F, -4.0F, 13.0F, 38.0F, 4.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 10.0F, 0.0F, 3.1416F, 0.0F));

        ModelPartData Roof = door.addChild("Roof", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -1.0F, 0.0F));

        ModelPartData PCB = door.addChild("PCB", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -1.0F, 0.0F));

        ModelPartData cube_r1 = PCB.addChild("cube_r1", ModelPartBuilder.create().uv(0, 10).cuboid(-17.0F, -61.0F, 13.0F, 34.0F, 5.0F, 6.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -3.0F, 10.0F, 0.0F, 3.1416F, 0.0F));

        ModelPartData Walls = door.addChild("Walls", ModelPartBuilder.create().uv(5, 79).cuboid(-14.0F, -60.0F, -6.0F, 1.0F, 56.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 79).cuboid(13.0F, -60.0F, -6.0F, 1.0F, 56.0F, 1.0F, new Dilation(0.0F))
                .uv(75, 10).cuboid(-13.0F, -60.0F, -6.0F, 26.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(10, 79).cuboid(13.0F, -60.0F, -6.5F, 1.0F, 56.0F, 0.0F, new Dilation(0.0F))
                .uv(75, 13).cuboid(-13.0F, -60.0F, -6.5F, 26.0F, 1.0F, 0.0F, new Dilation(0.0F))
                .uv(13, 79).cuboid(-14.0F, -60.0F, -6.5F, 1.0F, 56.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData Doors = door.addChild("Doors", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData Left = Doors.addChild("Left", ModelPartBuilder.create().uv(0, 22).cuboid(0.5F, -29.5F, 9.5F, 13.0F, 55.0F, 1.0F, new Dilation(0.0F))
                .uv(58, 22).cuboid(0.5F, -29.5F, 9.0F, 14.0F, 55.0F, 0.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(9.5F, -9.5F, 8.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-13.5F, -29.5F, -15.5F));

        ModelPartData Right = Doors.addChild("Right", ModelPartBuilder.create().uv(29, 22).cuboid(-13.5F, -29.5F, 9.5F, 13.0F, 55.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 10).cuboid(-12.5F, -10.5F, 8.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(13.5F, -29.5F, -15.5F));

        ModelPartData Posts = door.addChild("Posts", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, -32.0F));

        ModelPartData cube_r2 = Posts.addChild("cube_r2", ModelPartBuilder.create().uv(54, 78).cuboid(-18.0F, -65.0F, -18.0F, 4.0F, 61.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 10.0F, 0.0F, 3.1416F, 0.0F));

        ModelPartData cube_r3 = Posts.addChild("cube_r3", ModelPartBuilder.create().uv(71, 78).cuboid(-18.0F, -65.0F, -18.0F, 4.0F, 61.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 10.0F, 0.0F, 1.5708F, 0.0F));
        return TexturedModelData.of(modelData, 256, 256);
    }

    @Override
    public void renderWithAnimations(DoorBlockEntity doorEntity, ModelPart root, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float pAlpha) {
        this.door.getChild("Doors").getChild("Left").yaw = doorEntity.getLeftDoorRotation(); // fixme door has wrong pivot points

        matrices.push();
        matrices.scale(0.6f,0.6f,0.6f);
        matrices.translate(0,-1.5,0.35);
        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(180));

        super.renderWithAnimations(doorEntity, root, matrices, vertices, light, overlay, red, green, blue, pAlpha);
        matrices.pop();
    }

    @Override
    public Identifier getTexture() {
        return TEXTURE;
    }

    @Override
    public ModelPart getPart() {
        return door;
    }
}
