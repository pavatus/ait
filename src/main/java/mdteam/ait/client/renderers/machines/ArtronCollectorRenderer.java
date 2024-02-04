package mdteam.ait.client.renderers.machines;

import mdteam.ait.AITMod;
import mdteam.ait.client.models.machines.ArtronCollectorModel;
import mdteam.ait.core.blockentities.ArtronCollectorBlockEntity;
import mdteam.ait.core.blocks.ArtronCollectorBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

// Made with Blockbench 4.8.3
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class ArtronCollectorRenderer<T extends ArtronCollectorBlockEntity> implements BlockEntityRenderer<T> {

    public static final Identifier COLLECTOR_TEXTURE = new Identifier(AITMod.MOD_ID, ("textures/blockentities/machines/artron_collector.png"));
    public static final Identifier EMISSIVE_COLLECTOR_TEXTURE = new Identifier(AITMod.MOD_ID, ("textures/blockentities/machines/artron_collector_emission.png"));
    private final ArtronCollectorModel artronCollectorModel;

    public ArtronCollectorRenderer(BlockEntityRendererFactory.Context ctx) {
        this.artronCollectorModel = new ArtronCollectorModel(ArtronCollectorModel.getTexturedModelData().createModel());
    }

    @Override
    public void render(ArtronCollectorBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {

        BlockState blockState = entity.getCachedState();

        float f = blockState.get(ArtronCollectorBlock.FACING).asRotation();

        if (MinecraftClient.getInstance().world == null) return;

        matrices.push();

        matrices.translate(0.5f, 1.5f, 0.5f);

        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(f));

        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));

        ModelPart batteryLevels = artronCollectorModel.collector.getChild("battery_levels");

        artronCollectorModel.collector.getChild("spinner").yaw =
                entity.getFuel() > 0 ? ((float) MinecraftClient.getInstance().world.getTime() / 12000L) * 180.0f : 0;

        batteryLevels.getChild("b_1").visible = entity.getFuel() > 250 && entity.getFuel() < 500;
        batteryLevels.getChild("b_2").visible = entity.getFuel() > 500 && entity.getFuel() < 750;
        batteryLevels.getChild("b_3").visible = entity.getFuel() > 750 && entity.getFuel() < 1000;
        batteryLevels.getChild("b_4").visible = entity.getFuel() > 1000 && entity.getFuel() < 1250;
        batteryLevels.getChild("b_5").visible = entity.getFuel() > 1250 && entity.getFuel() < 1500;
        batteryLevels.getChild("b_6").visible = entity.getFuel() == 1500;

        this.artronCollectorModel.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(COLLECTOR_TEXTURE)), light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
        this.artronCollectorModel.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentEmissive(EMISSIVE_COLLECTOR_TEXTURE)), 0xF000F00, overlay, 1.0F, 1.0F, 1.0F, 1.0F);

        matrices.pop();
    }
}