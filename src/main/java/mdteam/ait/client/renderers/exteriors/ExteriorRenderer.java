package mdteam.ait.client.renderers.exteriors;

import mdteam.ait.AITMod;
import mdteam.ait.client.models.exteriors.ExteriorModel;
import mdteam.ait.client.models.exteriors.SiegeModeModel;
import mdteam.ait.client.registry.ClientExteriorVariantRegistry;
import mdteam.ait.client.registry.console.ClientConsoleVariantSchema;
import mdteam.ait.client.registry.exterior.ClientExteriorVariantSchema;
import mdteam.ait.client.renderers.AITRenderLayers;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.blocks.ExteriorBlock;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import mdteam.ait.tardis.TardisExterior;
import org.joml.Vector3f;

public class ExteriorRenderer<T extends ExteriorBlockEntity> implements BlockEntityRenderer<T> {
    private ExteriorModel model;
    private SiegeModeModel siege;
    private final EntityRenderDispatcher dispatcher;


    public ExteriorRenderer(BlockEntityRendererFactory.Context ctx) {
        this.dispatcher = ctx.getEntityRenderDispatcher();
    }

    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (entity.findTardis().isEmpty()) {
            return;
        }

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        ClientExteriorVariantSchema exteriorVariant = ClientExteriorVariantRegistry.withParent(entity.findTardis().get().getExterior().getVariant());
        TardisExterior tardisExterior = entity.findTardis().get().getExterior();

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

        if(MinecraftClient.getInstance().player == null) return;

        Identifier texture = exteriorVariant.texture();
        Identifier emission = exteriorVariant.emission();

        float wrappedDegrees = MathHelper.wrapDegrees(MinecraftClient.getInstance().player.getHeadYaw() +
                (entity.findTardis().get().getHandlers().getExteriorPos().getDirection() == Direction.NORTH ||
                        entity.findTardis().get().getHandlers().getExteriorPos().getDirection() == Direction.SOUTH ? f + 180f : f));

        if(exteriorVariant.equals(ClientExteriorVariantRegistry.DOOM)) {
            texture = DoomConstants.getTextureForRotation(wrappedDegrees, entity.findTardis().get());
            emission = DoomConstants.getEmissionForRotation(DoomConstants.getTextureForRotation(wrappedDegrees, entity.findTardis().get()), entity.findTardis().get());
        }

        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(!exteriorVariant.equals(ClientExteriorVariantRegistry.DOOM) ? f :
                MinecraftClient.getInstance().player.getHeadYaw() + ((wrappedDegrees > -135 && wrappedDegrees < 135) ? 180f : 0f)));


        // -------------------------------------------------------------------------------------------------------------------

        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        try {
            if (entity.findTardis().get().isSiegeMode()) {
                if (siege == null) siege = new SiegeModeModel(SiegeModeModel.getTexturedModelData().createModel());
                siege.renderWithAnimations(entity, this.siege.getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.getEntityTranslucentCull(SiegeModeModel.TEXTURE)), maxLight, overlay, 1, 1, 1, 1);
                matrices.pop();
                return;
            }
        } catch (Exception e) {
            AITMod.LOGGER.error("Failed to render siege mode", e);
        }

        String name = entity.findTardis().get().getHandlers().getStats().getName();
        if (name.equalsIgnoreCase("grumm") || name.equalsIgnoreCase("dinnerbone")) {
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90f));
        }

        if (model != null) {
            model.renderWithAnimations(entity, this.model.getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.getEntityTranslucentCull(texture)), light, overlay, 1, 1, 1, 1);
            if (entity.findTardis().get().getHandlers().getOvergrown().isOvergrown()) {
                model.renderWithAnimations(entity, this.model.getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.getEntityTranslucentCull(entity.findTardis().get().getHandlers().getOvergrown().getOvergrownTexture())), light, overlay, 1, 1, 1, 1);
            }
            if (emission != null && entity.findTardis().get().hasPower()) {
                boolean alarms = PropertiesHandler.getBool(entity.findTardis().get().getHandlers().getProperties(), PropertiesHandler.ALARM_ENABLED);

                model.renderWithAnimations(entity, this.model.getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.tardisRenderEmissionCull(emission, true)), maxLight, overlay, 1, alarms ? 0.3f : 1 , alarms ? 0.3f : 1, 1);
            }
        }
        matrices.pop();
        if(!entity.findTardis().get().getHandlers().getSonic().hasSonic()) return;
        matrices.push();
        //matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(f + sonicItemRotations(exteriorVariant)[0]));
        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(f + exteriorVariant.sonicItemRotations()[0]), (float) entity.getPos().toCenterPos().x - entity.getPos().getX(), (float) entity.getPos().toCenterPos().y - entity.getPos().getY(), (float) entity.getPos().toCenterPos().z - entity.getPos().getZ());
        matrices.translate(exteriorVariant.sonicItemTranslations().x(), exteriorVariant.sonicItemTranslations().y(), exteriorVariant.sonicItemTranslations().z());
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(exteriorVariant.sonicItemRotations()[1]));
        matrices.scale(0.9f, 0.9f, 0.9f);
        int lightAbove = WorldRenderer.getLightmapCoordinates(entity.getWorld(), entity.getPos().up());
        ItemStack stack = entity.findTardis().get().getHandlers().getSonic().get();
        MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformationMode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 0);
        matrices.pop();
    }

    @Override
    public boolean rendersOutsideBoundingBox(T blockEntity) {
        return true;
    }
}
