package mdteam.ait.client.models.exteriors;

import mdteam.ait.AITMod;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class TardimExteriorModel extends ExteriorModel {
    public static final Identifier EXTERIOR_TEXTURE = new Identifier(AITMod.MOD_ID, ("textures/blockentities/exteriors/tardim.png"));
    public static final Identifier EXTERIOR_TEXTURE_EMISSION = new Identifier(AITMod.MOD_ID, "textures/blockentities/exteriors/tardim_emission.png");

    private final ModelPart bone;
    public TardimExteriorModel(ModelPart root) {
        super(RenderLayer::getEntityCutoutNoCull);
        this.bone = root.getChild("tardis");
    }
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData tardis = modelPartData.addChild("tardis", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData body = tardis.addChild("body", ModelPartBuilder.create().uv(0, 25).cuboid(-11.0F, -32.0F, -8.0F, 3.0F, 32.0F, 16.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-8.0F, -40.0F, -8.0F, 16.0F, 8.0F, 16.0F, new Dilation(0.0F))
                .uv(39, 42).cuboid(-8.0F, -0.02F, -8.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.0F))
                .uv(39, 25).cuboid(-8.0F, 0.02F, -8.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r1 = body.addChild("cube_r1", ModelPartBuilder.create().uv(0, 25).cuboid(-11.0F, -32.0F, -8.0F, 3.0F, 32.0F, 16.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        ModelPartData cube_r2 = body.addChild("cube_r2", ModelPartBuilder.create().uv(0, 25).cuboid(-11.0F, -32.0F, -8.0F, 3.0F, 32.0F, 16.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        ModelPartData doors = tardis.addChild("doors", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData left = doors.addChild("left", ModelPartBuilder.create().uv(62, 59).cuboid(0.0F, -32.0F, -11.0F, 8.0F, 32.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData right = doors.addChild("right", ModelPartBuilder.create().uv(39, 59).cuboid(-8.0F, -32.0F, -11.0F, 8.0F, 32.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 128, 128);
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
        // matrices.scale(0.6F,0.6f,0.6f);
        matrices.translate(0, -1.5f, 0);

        this.bone.getChild("doors").getChild("left").yaw = -exterior.getCorrectDoorRotations()[1]; //fixme pivot poitns
        this.bone.getChild("doors").getChild("right").yaw = exterior.getCorrectDoorRotations()[0];

        super.renderWithAnimations(exterior, root, matrices, vertices, light, overlay, red, green, blue, pAlpha);

        matrices.pop();
    }
}
