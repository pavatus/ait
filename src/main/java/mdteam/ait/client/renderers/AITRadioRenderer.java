package mdteam.ait.client.renderers;

import mdteam.ait.AITMod;
import mdteam.ait.core.blockentities.AITRadioBlockEntity;
import mdteam.ait.core.blocks.RadioBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.model.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

// Made with Blockbench 4.8.3
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class AITRadioRenderer<T extends AITRadioBlockEntity> implements BlockEntityRenderer<T> {

    public static final Identifier RADIO_TEXTURE = new Identifier(AITMod.MOD_ID, ("textures/block/radio.png"));
    public static final Identifier EMISSIVE_RADIO_TEXTURE = new Identifier(AITMod.MOD_ID, ("textures/block/radio_emissive.png"));
    private final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
    private final ModelPart radio;
    private ModelPart volume;
    private ModelPart tuner;
    private ModelPart vu_module;
    private ModelPart culling_panels;
    private ModelPart antennae;


    //@TODO Block entity stuff for the radio.

    public AITRadioRenderer(BlockEntityRendererFactory.Context ctx) {
        ModelPart root = texturedModelData().createModel();
        this.radio = root.getChild("radio");
    }

    public static TexturedModelData texturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData radio = modelPartData.addChild("radio", ModelPartBuilder.create().uv(0, 0).cuboid(-5.5F, -4.0F, -8.0F, 11.0F, 12.0F, 16.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 16.0F, 0.0F));

        ModelPartData volume = radio.addChild("volume", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(5.5F, 4.5F, -4.5F));

        ModelPartData tuner = radio.addChild("tuner", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(5.5F, 4.5F, 4.5F));

        ModelPartData vu_module = radio.addChild("vu_module", ModelPartBuilder.create().uv(44, 29).cuboid(0.0F, -4.0F, -4.0F, 0.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(5.6F, 1.4F, 0.0F));

        ModelPartData culling_panels = vu_module.addChild("culling_panels", ModelPartBuilder.create().uv(53, 41).mirrored().cuboid(-0.05F, -3.4F, 2.0F, 0.0F, 4.0F, 6.0F, new Dilation(0.0F)).mirrored(false)
                .uv(47, 41).cuboid(-0.05F, -3.4F, -8.0F, 0.0F, 4.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData antennae = radio.addChild("antennae", ModelPartBuilder.create().uv(0, 29).cuboid(0.0F, -17.0F, -8.0F, 0.0F, 17.0F, 16.0F, new Dilation(0.0F)), ModelTransform.pivot(-5.75F, 3.0F, 0.0F));
        return TexturedModelData.of(modelData, 128, 128);
    }

    @Override
    public void render(AITRadioBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        BlockState blockState = entity.getCachedState();
        float f = blockState.get(RadioBlock.FACING).asRotation();
        boolean renderOnThings = entity.isRadioOn();

        OrderedText vol = Text.literal("Volume").asOrderedText();
        OrderedText tuner = Text.literal("Tuning").asOrderedText();
        OrderedText volNum = Text.literal("" + ((int) (Math.nextUp(entity.getTickRot("volume") * (180 / Math.PI) * 11) / 360) + 1)).asOrderedText();
        String _stationname = "MDTEAM CO.";
        OrderedText stationName = Text.literal(_stationname).setStyle(Style.EMPTY.withBold(true)).asOrderedText();
        OrderedText frequency = Text.literal("108.8").asOrderedText();
        OrderedText fm = Text.literal("  FM").asOrderedText();

        int colorWhite = 0xFFFFFFFF;
        int maxLight = 0xF000F0;
        int colorBlack = 0x00000000;
        int colorTransparent = 0x00FFFFFF;

        matrices.push();

        matrices.translate(0.5, 1.5, 0.5);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-f - 90));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180.0f));
        this.radio.getChild("tuner").pitch = (float) entity.getTickRot("tuner");
        this.radio.getChild("volume").pitch = -(float) entity.getTickRot("volume");
        this.radio.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentCull(RADIO_TEXTURE)), light, overlay, 1, 1, 1, 1);

        matrices.push();
        matrices.translate(-0.001, 1, 0);
        if (renderOnThings)
            this.radio.getChild("vu_module").render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentEmissive(EMISSIVE_RADIO_TEXTURE, true)), maxLight, overlay, 1, 1, 1, 1);
        else
            this.radio.getChild("vu_module").render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentCull(EMISSIVE_RADIO_TEXTURE)), light, overlay, 1, 1, 1, 1);
        matrices.pop();

        matrices.push();
        matrices.translate(0.351, this.textRenderer.getWidth(vol) / (float) (Math.pow(8, 8)) + 1.45, this.textRenderer.getWidth(vol) / (float) (Math.pow(8, 8)) - 0.495);
        matrices.translate(0f, -0.26, 0.165);
        matrices.scale(0, 0.025F / 8F, 0.025F / 8F);
        //System.out.println((this.textRenderer.getWidth(text) / 1920F * 0.015F) / 0.075f);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-90));
        if (renderOnThings)
            this.textRenderer.drawWithOutline(vol, 0.25f, 0.25f, colorWhite, colorBlack, matrices.peek().getPositionMatrix(), vertexConsumers, light);
        matrices.pop();

        matrices.push();
        matrices.translate(0.351, this.textRenderer.getWidth(tuner) / (float) (Math.pow(8, 8)) + 1.45, this.textRenderer.getWidth(tuner) / (float) (Math.pow(8, 8)) - 0.495);
        matrices.translate(0f, -0.26, Math.abs(0.165 - 0.89));
        matrices.scale(0, 0.025F / 8F, 0.025F / 8F);
        //System.out.println((this.textRenderer.getWidth(text) / 1920F * 0.015F) / 0.075f);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-90));
        if (renderOnThings)
            this.textRenderer.drawWithOutline(tuner, 0.25f, 0.25f, colorWhite, colorBlack, matrices.peek().getPositionMatrix(), vertexConsumers, light);
        matrices.pop();

        matrices.push();
        matrices.translate(0.407, this.textRenderer.getWidth(vol) / (float) (Math.pow(8, 8)) + 1.45, this.textRenderer.getWidth(vol) / (float) (Math.pow(8, 8)) - 0.495);
        if (((int) (Math.nextUp(entity.getTickRot("volume") * (180 / Math.PI) * 11) / 360) + 1) >= 10) {
            matrices.translate(0f, -0.2, 0.69 - (double) this.textRenderer.getWidth(vol) / 64);
        } else {
            matrices.translate(0f, -0.2, 0.71 - (double) this.textRenderer.getWidth(vol) / 64);
        }
        matrices.scale(0, 0.05F / 8F, 0.05F / 8F);
        //System.out.println((this.textRenderer.getWidth(text) / 1920F * 0.015F) / 0.075f);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-90));
        if (renderOnThings)
            this.textRenderer.drawWithOutline(volNum, 0.25f, 0.25f, colorWhite, colorBlack, matrices.peek().getPositionMatrix(), vertexConsumers, light);
        matrices.pop();

        matrices.push();
        matrices.translate(0.351, this.textRenderer.getWidth(frequency) / (float) (Math.pow(8, 8)) + 1.4, this.textRenderer.getWidth(frequency) / (float) (Math.pow(8, 8)) - 0.62);
        matrices.translate(0f, -0.365, 0.945 - (double) this.textRenderer.getWidth(frequency) / 64);
        matrices.scale(0, 0.05F / 8F, 0.05F / 8F);
        //System.out.println((this.textRenderer.getWidth(text) / 1920F * 0.015F) / 0.075f);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-90));
        if (renderOnThings)
            this.textRenderer.drawWithOutline(frequency, 0.25f, 0.25f, colorWhite, colorBlack, matrices.peek().getPositionMatrix(), vertexConsumers, maxLight);
        matrices.pop();

        matrices.push();
        matrices.translate(0.351, this.textRenderer.getWidth(frequency) / (float) (Math.pow(8, 8)) + 1.4, this.textRenderer.getWidth(frequency) / (float) (Math.pow(8, 8)) - 0.62);
        matrices.translate(0f, -0.305, 0.945 - (double) this.textRenderer.getWidth(frequency) / 64);
        matrices.scale(0, 0.05F / 8F, 0.05F / 8F);
        //System.out.println((this.textRenderer.getWidth(text) / 1920F * 0.015F) / 0.075f);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-90));
        if (renderOnThings)
            this.textRenderer.drawWithOutline(fm, 0.25f, 0.25f, colorWhite, colorBlack, matrices.peek().getPositionMatrix(), vertexConsumers, maxLight);
        matrices.pop();

        matrices.push();
        matrices.translate(0.345, this.textRenderer.getWidth(frequency) / (float) (Math.pow(8, 8)) + 1.45, this.textRenderer.getWidth(frequency) / (float) (Math.pow(8, 8)) - 0.4975);
        matrices.translate(0f, -0.485, 0.5);
        matrices.scale(0, 0.05F / 8F, 0.05F / 8F);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-90));
        if (renderOnThings)
            if (_stationname.length() <= 12) {
                if (_stationname.length() <= 5 && _stationname.length() < 12) {
                    this.textRenderer.draw(stationName, -this.textRenderer.getWidth(stationName) / 0.1f, 0.25f, colorWhite, false, matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.NORMAL, colorTransparent, maxLight);
                } else {
                    this.textRenderer.draw(stationName, -this.returnScrollableNumber(stationName, this.textRenderer, this.textRenderer.getWidth(stationName) / 2f, 0.05f), 0.25f, colorWhite, false, matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.NORMAL, colorTransparent, maxLight);
                }
            } else
                new Error("Name is too long!", new Throwable(AITMod.MOD_ID + ": name in AITRadioRenderer exceeds 10 character units"));
        matrices.pop();

        matrices.pop();
    }

    public float returnScrollableNumber(OrderedText x, TextRenderer textRenderer, float xMarginLeft, float xMarginRight) {
        float p = textRenderer.getWidth(x) - xMarginRight;
        float o = (8f + 8f - textRenderer.fontHeight) / 2f + 1f;
        float k = o - p;
        if (p > k) {
            float l = (xMarginLeft * 3f + k);
            double d = (double) Util.getMeasuringTimeMs() / 1000.0;
            double e = Math.max((double) l * 0.5, 3.0);
            double f = Math.sin(Math.PI / 2 * Math.cos(Math.PI * 2 * d / e)) / 2.0 + 0.5;
            double g = MathHelper.lerp(f, 0.0, l);
            return p - (int) g;
        }
        return textRenderer.getWidth(x);
    }
}