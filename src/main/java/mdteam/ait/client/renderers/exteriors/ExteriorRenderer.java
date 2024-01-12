package mdteam.ait.client.renderers.exteriors;

import mdteam.ait.AITMod;
import mdteam.ait.client.models.exteriors.ExteriorModel;
import mdteam.ait.client.models.exteriors.SiegeModeModel;
import mdteam.ait.client.registry.ClientExteriorVariantRegistry;
import mdteam.ait.client.registry.exterior.ClientExteriorVariantSchema;
import mdteam.ait.client.renderers.AITRenderLayers;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.blocks.ExteriorBlock;
import mdteam.ait.tardis.handler.properties.PropertiesHandler;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import mdteam.ait.tardis.TardisExterior;
import org.joml.Quaternionf;

public class ExteriorRenderer<T extends ExteriorBlockEntity> implements BlockEntityRenderer<T> {
    private ExteriorModel model;
    private SiegeModeModel siege;
    private final EntityRenderDispatcher dispatcher;

    public static final Identifier DOOM_TEXTURE = new Identifier(AITMod.MOD_ID, "textures/blockentities/exteriors/doom/doom.png");
    public static final Identifier DOOM_TEXTURE_EMISSION = new Identifier(AITMod.MOD_ID, "textures/blockentities/exteriors/doom/doom_emission.png");

    public ExteriorRenderer(BlockEntityRendererFactory.Context ctx) {
        this.dispatcher = ctx.getEntityRenderDispatcher();
    }

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
        int maxLight = 0xF000F0;
        matrices.push();
        matrices.translate(0.5, 0, 0.5);
        if (!exteriorVariant.equals(ClientExteriorVariantRegistry.DOOM)) {
            matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(f));
        } else {
            if(MinecraftClient.getInstance().player == null) return;
            matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(MinecraftClient.getInstance().player.getHeadYaw() + 180f));
        }
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
            if (entity.getTardis().getHandlers().getOvergrownHandler().isOvergrown()) {
                model.renderWithAnimations(entity, this.model.getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.getEntityTranslucentCull(entity.getTardis().getHandlers().getOvergrownHandler().getOvergrownTexture())), light, overlay, 1, 1, 1, 1);
            }
            if (exteriorVariant.emission() != null && entity.getTardis().hasPower()) {
                boolean alarms = PropertiesHandler.getBool(entity.getTardis().getHandlers().getProperties(), PropertiesHandler.ALARM_ENABLED);

                model.renderWithAnimations(entity, this.model.getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.tardisRenderEmissionCull(exteriorVariant.emission(), false)), maxLight, overlay, 1, alarms ? 0.3f : 1 , alarms ? 0.3f : 1, 1);
            }
        }
        matrices.pop();
    }

    @Override
    public boolean rendersOutsideBoundingBox(T blockEntity) {
        return true;
    }
}
