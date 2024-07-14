package loqor.ait.client.renderers.machines;

import loqor.ait.AITMod;
import loqor.ait.client.models.machines.EngineCoreModel;
import loqor.ait.client.renderers.AITRenderLayers;
import loqor.ait.client.util.ClientLightUtil;
import loqor.ait.core.blockentities.EngineCoreBlockEntity;
import loqor.ait.tardis.Tardis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.ConduitBlockEntity;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public class EngineCoreBlockEntityRenderer implements BlockEntityRenderer<EngineCoreBlockEntity> {

    public static final Identifier TEXTURE = new Identifier(AITMod.MOD_ID, "textures/blockentities/machines/engine_core.png");
    public static final Identifier EMISSION = new Identifier(AITMod.MOD_ID, "textures/blockentities/machines/engine_core_emission.png");

    /*public static final SpriteIdentifier BASE_TEXTURE;
    public static final SpriteIdentifier CAGE_TEXTURE;
    public static final SpriteIdentifier WIND_TEXTURE;
    public static final SpriteIdentifier WIND_VERTICAL_TEXTURE;
    public static final Identifier OPEN_EYE_TEXTURE;
    public static final SpriteIdentifier CLOSED_EYE_TEXTURE;
    private final ModelPart conduitEye;
    private final ModelPart conduitWind;
    private final ModelPart conduitShell;
    private final ModelPart conduit;
    private final BlockEntityRenderDispatcher dispatcher;*/
    EngineCoreModel model;
    private int tickForSpin;
    public EngineCoreBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        // this.dispatcher = ctx.getRenderDispatcher();
        this.model = new EngineCoreModel(EngineCoreModel.getTexturedModelData().createModel());
        /*this.conduitEye = EngineCoreBlockEntityRenderer.getEyeTexturedModelData().createModel();
        this.conduitWind = EngineCoreBlockEntityRenderer.getWindTexturedModelData().createModel();
        this.conduitShell = EngineCoreBlockEntityRenderer.getShellTexturedModelData().createModel();
        this.conduit = EngineCoreBlockEntityRenderer.getPlainTexturedModelData().createModel();*/
    }

    @Override
    public void render(EngineCoreBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        tickForSpin++;

        if (entity.findTardis().isEmpty()) {
            return;
        }

        Tardis tardis = entity.findTardis().get();

        boolean bl = tardis.engine().hasPower();

        matrices.push();
        // matrices.scale(10, 10, 10);
        matrices.translate(0.5f, 1f, 0.5f);
        // matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));

        if (bl && entity.isActive()) {
            //matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(((float) tickForSpin / 350L) * 360.0f));
            //matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(((float) tickForSpin / 700L) * 360.0f));
            matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(((float) tickForSpin / 2000L) * 360.0f));
        }

        model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE)), light, overlay, 1, 1, 1, 1);

        if (entity.isActive()) {
            ClientLightUtil.renderEmissive(
                    ClientLightUtil.Renderable.create(model::render), EMISSION, entity, this.model.getPart(), matrices, vertexConsumers,
                    light, overlay, 1, 1, 1, 1
            );
        }

        matrices.pop();
    }

    /*public static TexturedModelData getEyeTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("eye", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 0.0F, new Dilation(0.01F)), ModelTransform.NONE);
        return TexturedModelData.of(modelData, 16, 16);
    }

    public static TexturedModelData getWindTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("wind", ModelPartBuilder.create().uv(0, 0).cuboid(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F), ModelTransform.NONE);
        return TexturedModelData.of(modelData, 64, 32);
    }

    public static TexturedModelData getShellTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("shell", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F), ModelTransform.NONE);
        return TexturedModelData.of(modelData, 32, 16);
    }

    public static TexturedModelData getPlainTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("shell", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F), ModelTransform.NONE);
        return TexturedModelData.of(modelData, 32, 16);
    }*/

    /*public void render(EngineCoreBlockEntity coreBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        float g = (float)coreBlockEntity.ticks + f;
        float h;
        matrixStack.push();
        matrixStack.scale(2f, 2f, 2f);
        matrixStack.translate(-0.25f, -0.25f, -0.25f);
        if (!coreBlockEntity.isActive()) {
            h = coreBlockEntity.getRotation(0.0F);
            VertexConsumer vertexConsumer = BASE_TEXTURE.getVertexConsumer(vertexConsumerProvider, RenderLayer::getEntitySolid);
            matrixStack.push();
            matrixStack.translate(0.5F, 0.5F, 0.5F);
            matrixStack.multiply((new Quaternionf()).rotationY(h * 0.017453292F));
            this.conduitShell.render(matrixStack, vertexConsumer, i, j, 245f / 255f, 111f / 255f, 39f / 255f, 1);
            matrixStack.pop();
        } else {
            h = coreBlockEntity.getRotation(f) * 57.295776F;
            float k = MathHelper.sin(g * 0.1F) / 2.0F + 0.5F;
            k += k * k;
            matrixStack.push();
            matrixStack.translate(0.5F, 0.3F + k * 0.2F, 0.5F);
            Vector3f vector3f = (new Vector3f(0.5F, 1.0F, 0.5F)).normalize();
            matrixStack.multiply((new Quaternionf()).rotationAxis(h * 0.017453292F, vector3f));
            this.conduit.render(matrixStack, CAGE_TEXTURE.getVertexConsumer(vertexConsumerProvider, RenderLayer::getEntityCutoutNoCull), i, j, 245f / 255f, 111 / 255f, 39f / 255f, 1);
            matrixStack.pop();
            int l = coreBlockEntity.ticks / 66 % 3;
            matrixStack.push();
            matrixStack.translate(0.5F, 0.5F, 0.5F);
            if (l == 1) {
                matrixStack.multiply((new Quaternionf()).rotationX(1.5707964F));
            } else if (l == 2) {
                matrixStack.multiply((new Quaternionf()).rotationZ(1.5707964F));
            }

            VertexConsumer vertexConsumer2 = (l == 1 ? WIND_VERTICAL_TEXTURE : WIND_TEXTURE).getVertexConsumer(vertexConsumerProvider, RenderLayer::getEntityCutoutNoCull);
            this.conduitWind.render(matrixStack, vertexConsumer2, i, j, 245f / 255f, 111 / 255f, 39f / 255f, 1);
            matrixStack.pop();
            matrixStack.push();
            matrixStack.translate(0.5F, 0.5F, 0.5F);
            matrixStack.scale(0.875F, 0.875F, 0.875F);
            matrixStack.multiply((new Quaternionf()).rotationXYZ(3.1415927F, 0.0F, 3.1415927F));
            this.conduitWind.render(matrixStack, vertexConsumer2, i, j, 245f / 255f, 111 / 255f, 39f / 255f, 1);
            matrixStack.pop();
            Camera camera = this.dispatcher.camera;
            matrixStack.push();
            matrixStack.translate(0.5F, 0.3F + k * 0.2F, 0.5F);
            matrixStack.scale(0.5F, 0.5F, 0.5F);
            float m = -camera.getYaw();
            matrixStack.multiply((new Quaternionf()).rotationYXZ(m * 0.017453292F, camera.getPitch() * 0.017453292F, 3.1415927F));
            float n = 1.3333334F;
            matrixStack.scale(1.3333334F, 1.3333334F, 1.3333334F);
            this.conduitEye.render(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucentCull(OPEN_EYE_TEXTURE)), i, j, 1f, 1f, 1f, 1);
            this.conduitEye.render(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucentEmissive(OPEN_EYE_TEXTURE)), i, j, 1f, 1f, 1f, 1);
            matrixStack.pop();
        }
        matrixStack.pop();
    }*/

    /*static {
        BASE_TEXTURE = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("entity/conduit/base"));
        CAGE_TEXTURE = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("entity/conduit/cage"));
        WIND_TEXTURE = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("entity/conduit/wind"));
        WIND_VERTICAL_TEXTURE = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("entity/conduit/wind_vertical"));
        OPEN_EYE_TEXTURE = new Identifier(AITMod.MOD_ID, "textures/environment/eye_of_harmony.png");
        CLOSED_EYE_TEXTURE = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("entity/conduit/closed_eye"));
    }*/
}
