package loqor.ait.client.models.entities.projectiles;


import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.ProjectileEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class StaserBoltEntityModel extends EntityModel<ProjectileEntityRenderState> {
    public StaserBoltEntityModel(ModelPart modelPart) {
        super(modelPart, RenderLayer::getEntityCutout);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData bone = modelPartData.addChild("bone", ModelPartBuilder.create().uv(0, 12).cuboid(0.0F, -1.0F, -5.0F, 0.0F, 2.0F, 10.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 21.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

        ModelPartData cube_r1 = bone.addChild("cube_r1", ModelPartBuilder.create().uv(0, 12).cuboid(0.0F, -1.0F, -5.0F, 0.0F, 2.0F, 10.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));
        return TexturedModelData.of(modelData, 32, 32);
    }

    public void setAngles(ProjectileEntityRenderState projectileEntityRenderState) {
        super.setAngles(projectileEntityRenderState);
        if (projectileEntityRenderState.shake > 0.0F) {
            float f = -MathHelper.sin(projectileEntityRenderState.shake * 3.0F) * projectileEntityRenderState.shake;
            ModelPart var10000 = this.root;
            var10000.roll += f * 0.017453292F;
        }

    }

    public void render(MatrixStack matrices, VertexConsumer buffer, int light, int defaultUv, float v, float v1, float v2, float v3) {
    }
}