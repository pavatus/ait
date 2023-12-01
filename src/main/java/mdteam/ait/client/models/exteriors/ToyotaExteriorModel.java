package mdteam.ait.client.models.exteriors;

import mdteam.ait.AITMod;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class ToyotaExteriorModel extends ExteriorModel {
    public static final Identifier EXTERIOR_TEXTURE = new Identifier(AITMod.MOD_ID, ("textures/blockentities/exteriors/toyota.png"));
    public static final Identifier EXTERIOR_TEXTURE_EMISSION = new Identifier(AITMod.MOD_ID, "textures/blockentities/exteriors/toyota_emission.png");

    private final ModelPart bone;
    public ToyotaExteriorModel(ModelPart root) {
        super(RenderLayer::getEntityCutoutNoCull);
        this.bone = root.getChild("bone");
    }
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData bone = modelPartData.addChild("bone", ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(-26.5F, -1.0F, -26.5F, 53.0F, 3.0F, 53.0F, new Dilation(0.0F)).mirrored(false)
                .uv(129, 57).cuboid(-22.5F, -88.0F, -22.5F, 45.0F, 4.0F, 45.0F, new Dilation(0.0F))
                .uv(129, 57).cuboid(-22.5F, -84.0F, -22.5F, 45.0F, 1.0F, 45.0F, new Dilation(0.0F))
                .uv(160, 0).cuboid(-20.5F, -90.75F, -20.5F, 41.0F, 3.0F, 41.0F, new Dilation(0.0F))
                .uv(364, 15).cuboid(-16.5F, -91.75F, -16.5F, 33.0F, 1.0F, 33.0F, new Dilation(0.0F))
                .uv(12, 26).cuboid(-3.5F, -94.5F, -3.5F, 7.0F, 3.0F, 7.0F, new Dilation(0.0F))
                .uv(0, 37).cuboid(-3.0F, -101.525F, -3.0F, 6.0F, 1.0F, 6.0F, new Dilation(0.0F))
                .uv(30, 63).cuboid(-2.0F, -102.525F, -2.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.0F))
                .uv(34, 57).cuboid(-2.0F, -95.0F, -2.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.0F))
                .uv(34, 22).cuboid(-2.0F, -101.025F, -2.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.0F))
                .uv(16, 0).cuboid(-0.5F, -103.025F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(14, 65).cuboid(-1.5F, -99.5F, -1.5F, 3.0F, 5.0F, 3.0F, new Dilation(0.0F))
                .uv(66, 73).cuboid(-1.75F, -99.5F, -1.75F, 3.5F, 4.0F, 3.5F, new Dilation(0.0F))
                .uv(20, 79).cuboid(-1.5F, -100.5F, -1.5F, 3.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(296, 298).cuboid(-24.0F, -77.0F, -23.0F, 47.0F, 1.0F, 47.0F, new Dilation(0.0F))
                .uv(279, 427).cuboid(-21.0F, -83.0F, -26.0F, 42.0F, 6.0F, 52.0F, new Dilation(0.0F))
                .uv(142, 387).cuboid(-25.0F, -84.0F, -25.0F, 6.0F, 83.0F, 6.0F, new Dilation(0.0F))
                .uv(217, 387).cuboid(19.0F, -84.0F, -25.0F, 6.0F, 83.0F, 6.0F, new Dilation(0.0F))
                .uv(192, 387).cuboid(19.0F, -84.0F, 19.0F, 6.0F, 83.0F, 6.0F, new Dilation(0.0F))
                .uv(167, 387).cuboid(-25.0F, -84.0F, 19.0F, 6.0F, 83.0F, 6.0F, new Dilation(0.0F))
                .uv(34, 44).cuboid(-23.5F, -86.0F, -23.5F, 4.0F, 3.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 45).cuboid(19.5F, -86.0F, -23.5F, 4.0F, 3.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 57).cuboid(19.5F, -86.0F, 19.5F, 4.0F, 3.0F, 4.0F, new Dilation(0.0F))
                .uv(17, 57).cuboid(-23.5F, -86.0F, 19.5F, 4.0F, 3.0F, 4.0F, new Dilation(0.0F))
                .uv(133, 175).cuboid(-24.0F, -76.0F, -0.5F, 1.0F, 75.0F, 1.0F, new Dilation(0.0F))
                .uv(222, 163).cuboid(-23.0F, -76.0F, -19.0F, 1.0F, 75.0F, 38.0F, new Dilation(0.0F))
                .uv(123, 175).cuboid(22.0F, -76.0F, -0.5F, 1.0F, 75.0F, 1.0F, new Dilation(0.0F))
                .uv(143, 163).cuboid(21.0F, -76.0F, -19.0F, 1.0F, 75.0F, 38.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 22.0F, 0.0F));

        ModelPartData cube_r1 = bone.addChild("cube_r1", ModelPartBuilder.create().uv(0, 175).cuboid(-23.0F, -66.0F, -19.0F, 1.0F, 75.0F, 38.0F, new Dilation(0.0F))
                .uv(128, 175).cuboid(-24.0F, -66.0F, -0.5F, 1.0F, 75.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -10.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        ModelPartData cube_r2 = bone.addChild("cube_r2", ModelPartBuilder.create().uv(279, 368).cuboid(-21.0F, -35.0F, -26.0F, 42.0F, 6.0F, 52.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -48.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        ModelPartData cube_r3 = bone.addChild("cube_r3", ModelPartBuilder.create().uv(33, 29).cuboid(0.0F, -83.0F, -4.0F, 0.0F, 6.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -17.5F, 0.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData cube_r4 = bone.addChild("cube_r4", ModelPartBuilder.create().uv(17, 37).cuboid(0.0F, -83.0F, -4.0F, 0.0F, 6.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -17.5F, 0.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData bone3 = bone.addChild("bone3", ModelPartBuilder.create(), ModelTransform.pivot(-18.5F, -48.0F, -21.5F));

        ModelPartData cube_r5 = bone3.addChild("cube_r5", ModelPartBuilder.create().uv(11, 0).cuboid(16.5F, -39.5F, -18.0F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(32.5F, 38.5F, 16.0F, 0.0F, 1.5708F, 0.0F));

        ModelPartData cube_r6 = bone3.addChild("cube_r6", ModelPartBuilder.create().uv(138, 175).cuboid(15.5F, -66.0F, -0.5F, 1.0F, 75.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(18.5F, 38.0F, 15.0F, 0.0F, 1.5708F, 0.0F));

        ModelPartData cube_r7 = bone3.addChild("cube_r7", ModelPartBuilder.create().uv(101, 251).cuboid(15.5F, -66.0F, -19.0F, 1.0F, 75.0F, 19.0F, new Dilation(0.0F)), ModelTransform.of(18.5F, 38.0F, 16.0F, 0.0F, 1.5708F, 0.0F));

        ModelPartData bone4 = bone.addChild("bone4", ModelPartBuilder.create(), ModelTransform.pivot(18.5F, -48.0F, -21.45F));

        ModelPartData cube_r8 = bone4.addChild("cube_r8", ModelPartBuilder.create().uv(23, 133).cuboid(15.65F, -29.5F, -12.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(16.5F, -39.5F, -12.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F))
                .uv(79, 175).cuboid(15.5F, -66.0F, -14.0F, 1.0F, 75.0F, 19.0F, new Dilation(0.0F)), ModelTransform.of(-4.5F, 38.0F, 15.95F, 0.0F, 1.5708F, 0.0F));
        return TexturedModelData.of(modelData, 512, 512);
    }

    @Override
    public ModelPart getPart() {
        return bone;
    }

    @Override
    public Identifier getTexture() {
        return EXTERIOR_TEXTURE;
    }

    @Override
    public Identifier getEmission() {
        return EXTERIOR_TEXTURE_EMISSION;
    }

    @Override
    public void renderWithAnimations(ExteriorBlockEntity exterior, ModelPart root, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float pAlpha) {
        matrices.push();
        matrices.scale(0.5F,0.5f,0.5f);
        matrices.translate(0, -1.5f, 0);

        super.renderWithAnimations(exterior, root, matrices, vertices, light, overlay, red, green, blue, pAlpha);

        matrices.pop();
    }
}
