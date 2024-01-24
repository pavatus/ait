package mdteam.ait.client.renderers.monitors;

import mdteam.ait.AITMod;
import mdteam.ait.client.models.monitors.CRTMonitorModel;
import mdteam.ait.core.blockentities.AITRadioBlockEntity;
import mdteam.ait.core.blockentities.MonitorBlockEntity;
import mdteam.ait.core.blocks.MonitorBlock;
import mdteam.ait.core.blocks.RadioBlock;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.wrapper.client.manager.ClientTardisManager;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

import static mdteam.ait.tardis.control.impl.DimensionControl.convertWorldValueToModified;
import static mdteam.ait.tardis.handler.FuelHandler.TARDIS_MAX_FUEL;
import static mdteam.ait.tardis.util.TardisUtil.isClient;

// Made with Blockbench 4.8.3
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class MonitorRenderer<T extends MonitorBlockEntity> implements BlockEntityRenderer<T> {

    public static final Identifier MONITOR_TEXTURE = new Identifier(AITMod.MOD_ID, ("textures/blockentities/monitors/crt_monitor.png"));
    public static final Identifier EMISSIVE_MONITOR_TEXTURE = new Identifier(AITMod.MOD_ID, ("textures/blockentities/monitors/crt_monitor_emission.png"));
    private final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
    private final CRTMonitorModel crtMonitorModel;

    public MonitorRenderer(BlockEntityRendererFactory.Context ctx) {
        this.crtMonitorModel = new CRTMonitorModel(CRTMonitorModel.getTexturedModelData().createModel());
    }

    @Override
    public void render(MonitorBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {

        BlockState blockState = entity.getCachedState();

        float f = blockState.get(MonitorBlock.FACING).asRotation();

        matrices.push();

        matrices.translate(0.5f, 1.5f, 0.5f);

        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(f));

        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));

        this.crtMonitorModel.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(MONITOR_TEXTURE)), light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
        this.crtMonitorModel.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentEmissive(EMISSIVE_MONITOR_TEXTURE)), 0xF000F00, overlay, 1.0F, 1.0F, 1.0F, 1.0F);

        matrices.pop();

        if(entity.getTardisID() == null) return;
        Tardis tardis = ClientTardisManager.getInstance().getLookup().get(entity.getTardisID());
        if(tardis == null) return;

        matrices.push();
        matrices.translate(0.5, 0.75, 0.5);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(blockState.get(MonitorBlock.FACING) == Direction.EAST || blockState.get(MonitorBlock.FACING) == Direction.WEST ? f + 180 : f));
        matrices.scale(0.005f, 0.005f, 0.005f);
        AbsoluteBlockPos.Directed abpd = tardis.getTravel().getDestination();
        AbsoluteBlockPos.Directed abpp = tardis.getTravel().getPosition();
        String positionPosText = " " + abpp.getX() + ", " + abpp.getY() + ", " + abpp.getZ();
        String positionDimensionText = " " + convertWorldValueToModified(abpp.getDimension().getValue());
        String positionDirectionText = " " + abpp.getDirection().toString().toUpperCase();
        String destinationPosText = " " + abpd.getX() + ", " + abpd.getY() + ", " + abpd.getZ();
        String destinationDimensionText = " " + convertWorldValueToModified(abpd.getDimension().getValue());
        String destinationDirectionText = " " + abpd.getDirection().toString().toUpperCase();
        String flightTimeText = tardis.getTravel().getState() == TardisTravel.State.LANDED ? "0%" : tardis.getHandlers().getFlight().getDurationAsPercentage() + "%";
        String fuelText = Math.round((tardis.getFuel() / TARDIS_MAX_FUEL) * 100) + "%";
        matrices.translate(-50f, 0, -80);
        this.textRenderer.drawWithOutline(Text.of("❌").asOrderedText(), 0, 0, 0xF00F00,0x000000, matrices.peek().getPositionMatrix(),vertexConsumers, 0xF000F0);
        this.textRenderer.drawWithOutline(Text.of(positionPosText).asOrderedText(), 0, 8, 0xFFFFFF,0x000000, matrices.peek().getPositionMatrix(),vertexConsumers, 0xF000F0);
        this.textRenderer.drawWithOutline(Text.of(positionDimensionText).asOrderedText(), 0, 16, 0xFFFFFF,0x000000, matrices.peek().getPositionMatrix(),vertexConsumers, 0xF000F0);
        this.textRenderer.drawWithOutline(Text.of(positionDirectionText).asOrderedText(), 0, 24, 0xFFFFFF,0x000000, matrices.peek().getPositionMatrix(),vertexConsumers, 0xF000F0);
        this.textRenderer.drawWithOutline(Text.of("✛").asOrderedText(), 0, 40, 0x00F0FF,0x000000, matrices.peek().getPositionMatrix(),vertexConsumers, 0xF000F0);
        this.textRenderer.drawWithOutline(Text.of(destinationPosText).asOrderedText(), 0, 48, 0xFFFFFF,0x000000, matrices.peek().getPositionMatrix(),vertexConsumers, 0xF000F0);
        this.textRenderer.drawWithOutline(Text.of(destinationDimensionText).asOrderedText(), 0, 56, 0xFFFFFF,0x000000, matrices.peek().getPositionMatrix(),vertexConsumers, 0xF000F0);
        this.textRenderer.drawWithOutline(Text.of(destinationDirectionText).asOrderedText(), 0, 64, 0xFFFFFF,0x000000, matrices.peek().getPositionMatrix(),vertexConsumers, 0xF000F0);
        this.textRenderer.drawWithOutline(Text.of("⛽").asOrderedText(), 0, 78, 0xFAF000,0x000000, matrices.peek().getPositionMatrix(),vertexConsumers, 0xF000F0);
        this.textRenderer.drawWithOutline(Text.of(fuelText).asOrderedText(), 8, 78, 0xFFFFFF,0x000000, matrices.peek().getPositionMatrix(),vertexConsumers, 0xF000F0);
        this.textRenderer.drawWithOutline(Text.of("⏳").asOrderedText(), 0, 92, 0x00FF0F,0x000000, matrices.peek().getPositionMatrix(),vertexConsumers, 0xF000F0);
        this.textRenderer.drawWithOutline(Text.of(flightTimeText).asOrderedText(), 8, 92, 0xFFFFFF,0x000000, matrices.peek().getPositionMatrix(),vertexConsumers, 0xF000F0);

        if(tardis.getHandlers().getAlarms().isEnabled()) {
            this.textRenderer.drawWithOutline(Text.of("⚠").asOrderedText(), 84, 87, 0xFE0000, 0x000000, matrices.peek().getPositionMatrix(), vertexConsumers, 0xF000F0);
        }

        matrices.pop();
    }
}