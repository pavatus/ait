package loqor.ait.client.renderers.machines;

import loqor.ait.AITMod;
import loqor.ait.client.models.machines.EngineCoreModel;
import loqor.ait.client.util.ClientLightUtil;
import loqor.ait.core.blockentities.EngineCoreBlockEntity;
import loqor.ait.tardis.Tardis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class EngineCoreBlockEntityRenderer implements BlockEntityRenderer<EngineCoreBlockEntity> {

    public static final Identifier TEXTURE = new Identifier(AITMod.MOD_ID, "textures/blockentities/machines/engine_core.png");
    public static final Identifier EMISSION = new Identifier(AITMod.MOD_ID, "textures/blockentities/machines/engine_core_emission.png");

    private final EngineCoreModel model;
    private int tickForSpin;

    public EngineCoreBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.model = new EngineCoreModel(EngineCoreModel.getTexturedModelData().createModel());
    }

    @Override
    public void render(EngineCoreBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        tickForSpin++;

        if (!entity.isLinked())
            return;

        Tardis tardis = entity.tardis().get();
        boolean power = tardis.engine().hasPower();

        matrices.push();
        matrices.translate(0.5f, 1f, 0.5f);

        if (power && entity.isActive())
            matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(((float) tickForSpin / 2000L) * 360.0f));

        model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE)), light, overlay, 1, 1, 1, 1);

        if (entity.isActive()) {
            ClientLightUtil.renderEmissive(
                    ClientLightUtil.Renderable.create(model::render), EMISSION, entity, this.model.getPart(), matrices, vertexConsumers,
                    light, overlay, 1, 1, 1, 1
            );
        }

        matrices.pop();
    }
}
