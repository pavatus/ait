package mdteam.ait.client.renderers.exteriors;

import mdteam.ait.client.models.exteriors.ExteriorModel;
import mdteam.ait.client.models.exteriors.SiegeModeModel;
import mdteam.ait.client.registry.ClientExteriorVariantRegistry;
import mdteam.ait.client.registry.exterior.ClientExteriorVariantSchema;
import mdteam.ait.client.renderers.AITRenderLayers;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.blocks.ExteriorBlock;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import mdteam.ait.tardis.TardisExterior;

public class ExteriorRenderer<T extends ExteriorBlockEntity> implements BlockEntityRenderer<T> {
    private ExteriorModel model;
    private SiegeModeModel siege;

    public ExteriorRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (entity.getTardis() == null) {
            return;
        }

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        ClientExteriorVariantSchema exteriorVariant = ClientExteriorVariantRegistry.withParent(entity.getTardis().getExterior().getVariant());
        TardisExterior tardisExterior = entity.getTardis().getExterior();

        if (tardisExterior == null) return;

        Class<? extends ExteriorModel> modelClass = exteriorVariant.model().getClass();

        if (model != null && !(model.getClass().isInstance(modelClass))) // fixme this is bad it seems to constantly create a new one anyway but i didnt realise.
            model = null;

        if (model == null)
            this.model = exteriorVariant.model();

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
        Identifier texture = exteriorVariant.texture();

        if (entity.getTardis().isSiegeMode()) {
            if (siege == null) siege = new SiegeModeModel(SiegeModeModel.getTexturedModelData().createModel());
            siege.renderWithAnimations(entity, this.siege.getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.getEntityTranslucentCull(SiegeModeModel.TEXTURE)), maxLight, overlay, 1, 1, 1, 1);
            matrices.pop();
            return;
        }

        if (model != null) {
            model.animateTile(entity);
            model.renderWithAnimations(entity, this.model.getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.getEntityTranslucentCull(texture)), light, overlay, 1, 1, 1, 1);
            if (entity.getTardis() == null) return; // WHY IS THIS NULL HERE, BUT NOT AT THE BEGINNING OF THIS FUCKING FUNCTION THREAD
            if (entity.getTardis().getHandlers().getOvergrown().isOvergrown()) {
                model.renderWithAnimations(entity, this.model.getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.getEntityTranslucentCull(entity.getTardis().getHandlers().getOvergrown().getOvergrownTexture())), light, overlay, 1, 1, 1, 1);
            }
            if (exteriorVariant.emission() != null && entity.getTardis().hasPower()) {
                boolean alarms = PropertiesHandler.getBool(entity.getTardis().getHandlers().getProperties(), PropertiesHandler.ALARM_ENABLED);

                model.renderWithAnimations(entity, this.model.getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.tardisRenderEmissionCull(exteriorVariant.emission(), false)), maxLight, overlay, 1, alarms ? 0.3f : 1 , alarms ? 0.3f : 1, 1);
            }
        }
        matrices.pop();
    }
}
