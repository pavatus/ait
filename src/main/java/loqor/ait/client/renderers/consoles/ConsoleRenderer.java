package loqor.ait.client.renderers.consoles;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.profiler.Profiler;

import loqor.ait.AITMod;
import loqor.ait.client.models.consoles.ConsoleModel;
import loqor.ait.client.util.ClientLightUtil;
import loqor.ait.core.blockentities.ConsoleBlockEntity;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.data.schema.console.ClientConsoleVariantSchema;

public class ConsoleRenderer<T extends ConsoleBlockEntity> implements BlockEntityRenderer<T> {

    private ClientConsoleVariantSchema variant;
    private ConsoleModel model;

    public ConsoleRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers,
            int light, int overlay) {
        if (!entity.isLinked())
            return;

        Tardis tardis = entity.tardis().get();
        Profiler profiler = entity.getWorld().getProfiler();

        this.renderConsole(profiler, tardis, entity, matrices, vertexConsumers, light, overlay);
    }

    private void renderConsole(Profiler profiler, Tardis tardis, T entity, MatrixStack matrices,
            VertexConsumerProvider vertexConsumers, int light, int overlay) {
        profiler.push("model");

        this.updateModel(entity);
        boolean hasPower = tardis.engine().hasPower();

        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));

        if (!AITMod.AIT_CONFIG.DISABLE_CONSOLE_ANIMATIONS()) {
            profiler.swap("animate");
            model.animateBlockEntity(entity, tardis.travel().getState(), hasPower);
        }

        profiler.swap("render");
        model.renderWithAnimations(entity, model.getPart(), matrices,
                vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentCull(variant.texture())), light, overlay, 1,
                1, 1, 1);

        if (hasPower) {
            profiler.swap("emission"); // emission {

            ClientLightUtil.renderEmissive(model::renderWithAnimations, variant.emission(), entity, model.getPart(),
                    matrices, vertexConsumers, light, overlay, 1, 1, 1, 1);
        }

        matrices.pop();
        profiler.swap("sonic"); // } emission / sonic {

        ItemStack stack = tardis.sonic().getConsoleSonic();

        if (stack == null) {
            profiler.pop(); // } sonic
            return;
        }

        matrices.push();
        matrices.translate(variant.sonicItemTranslations().x(), variant.sonicItemTranslations().y(),
                variant.sonicItemTranslations().z());
        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(variant.sonicItemRotations()[0]));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(variant.sonicItemRotations()[1]));
        matrices.scale(0.9f, 0.9f, 0.9f);

        int lightAbove = WorldRenderer.getLightmapCoordinates(entity.getWorld(), entity.getPos().up());
        MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformationMode.GROUND, lightAbove,
                overlay, matrices, vertexConsumers, entity.getWorld(), 0);

        matrices.pop();
        profiler.pop(); // } sonic
    }

    private void updateModel(T entity) {
        ClientConsoleVariantSchema variant = entity.getVariant().getClient();

        if (this.variant != variant) {
            this.variant = variant;
            this.model = variant.model();
        }
    }
}
