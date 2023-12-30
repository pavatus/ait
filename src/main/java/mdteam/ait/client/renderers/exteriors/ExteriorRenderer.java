package mdteam.ait.client.renderers.exteriors;

import mdteam.ait.client.models.exteriors.ExteriorModel;
import mdteam.ait.client.renderers.AITRenderLayers;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.blocks.ExteriorBlock;
import mdteam.ait.tardis.handler.properties.PropertiesHandler;
import mdteam.ait.tardis.handler.properties.PropertiesHolder;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import mdteam.ait.tardis.TardisExterior;

public class ExteriorRenderer<T extends ExteriorBlockEntity> implements BlockEntityRenderer<T> {
    private ExteriorModel model;

    public ExteriorRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (entity.tardis() == null) {
            return;
        }

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        TardisExterior tardisExterior = entity.tardis().getExterior();

        if (tardisExterior == null) return;

        Class<? extends ExteriorModel> modelClass = tardisExterior.getVariant().model().getClass();

        if (model != null && !(model.getClass().isInstance(modelClass))) // fixme this is bad it seems to constantly create a new one anyway but i didnt realise.
            model = null;

        if (model == null)
            this.model = tardisExterior.getVariant().model();

        BlockState blockState = entity.getCachedState();
        float f = blockState.get(ExteriorBlock.FACING).asRotation();
        int maxLight = 0xFFFFFF;
        /*matrices.push();
        matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(180f));
        matrices.translate(0, -2.72f, -0.32);
        matrices.scale(0.0125f, 0.0125f, 0.0125f);
        textRenderer.drawWithOutline(Text.of("POLICE -=- BOX").asOrderedText(), -"POLICE -=- BOX".length() * 5.625f, 0, 0xFFFFFF, 0x000000, matrices.peek().getPositionMatrix(),vertexConsumers, light);
        matrices.pop();*/
        matrices.push();
        matrices.translate(0.5, 0, 0.5);
        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(f));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        Identifier texture = entity.tardis().getExterior().getVariant().texture();
        if (model != null) {
            model.animateTile(entity);
            model.renderWithAnimations(entity, this.model.getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.getEntityTranslucentCull(texture)), light, overlay, 1, 1, 1, 1);
            if (entity.tardis().getHandlers().getOvergrownHandler().isOvergrown()) {
                model.renderWithAnimations(entity, this.model.getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.getEntityTranslucentCull(entity.tardis().getHandlers().getOvergrownHandler().getOvergrownTexture())), light, overlay, 1, 1, 1, 1);
            }
            if (tardisExterior.getVariant().emission() != null && !entity.tardis().getHandlers().getFuel().isOutOfFuel()) {
                boolean alarms = PropertiesHandler.getBool(entity.tardis().getHandlers().getProperties(), PropertiesHandler.ALARM_ENABLED);

                model.renderWithAnimations(entity, this.model.getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.tardisRenderEmissionCull(entity.tardis().getExterior().getVariant().emission(), false)), maxLight, overlay, 1, alarms ? 0.3f : 1 , alarms ? 0.3f : 1, 1);
            }
        }
        matrices.pop();
    }
}
