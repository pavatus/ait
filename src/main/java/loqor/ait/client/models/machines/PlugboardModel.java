package loqor.ait.client.models.machines; // Made with Blockbench 4.9.4
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class PlugboardModel extends SinglePartEntityModel {
    private final ModelPart bone;

    public PlugboardModel(ModelPart root) {
        this.bone = root.getChild("bone");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData bone = modelPartData.addChild("bone",
                ModelPartBuilder.create().uv(0, 15).cuboid(1.15F, -2.85F, -2.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                        .uv(5, 15).cuboid(-0.5F, -2.85F, -2.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)).uv(10, 15)
                        .cuboid(-2.15F, -2.85F, -2.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)).uv(15, 15)
                        .cuboid(1.15F, -4.85F, -2.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)).uv(0, 18)
                        .cuboid(-0.5F, -4.85F, -2.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)).uv(5, 18)
                        .cuboid(-2.15F, -4.85F, -2.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)).uv(0, 0)
                        .cuboid(-4.0F, -12.0F, -1.0F, 8.0F, 12.0F, 2.0F, new Dilation(0.0F)).uv(0, 21)
                        .cuboid(-2.15F, -10.15F, -2.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)).uv(19, 20)
                        .cuboid(-0.5F, -10.15F, -2.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)).uv(20, 17)
                        .cuboid(1.15F, -10.15F, -2.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)).uv(20, 14)
                        .cuboid(-2.15F, -8.15F, -2.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)).uv(15, 18)
                        .cuboid(-0.5F, -8.15F, -2.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)).uv(10, 18)
                        .cuboid(1.15F, -8.15F, -2.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)),
                ModelTransform.pivot(0.0F, 22.0F, 7.0F));
        return TexturedModelData.of(modelData, 32, 32);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red,
            float green, float blue, float alpha) {
        bone.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart getPart() {
        return bone;
    }

    @Override
    public void setAngles(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw,
            float headPitch) {
    }
}
