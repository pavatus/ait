package loqor.ait.client.renderers.consoles;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.profiler.Profiler;

import loqor.ait.AITMod;
import loqor.ait.client.models.consoles.ConsoleModel;
import loqor.ait.client.util.ClientLightUtil;
import loqor.ait.core.blockentities.ConsoleBlockEntity;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.core.tardis.control.impl.DirectionControl;
import loqor.ait.core.tardis.handler.travel.TravelHandler;
import loqor.ait.core.tardis.handler.travel.TravelHandlerBase;
import loqor.ait.core.util.WorldUtil;
import loqor.ait.data.DirectedGlobalPos;
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
        // if (variant.hasConsoleText())
            this.renderConsoleText(tardis, entity, matrices, vertexConsumers, light, overlay);
    }

    private void renderConsoleText(Tardis tardis, T entity, MatrixStack matrices,
                                   VertexConsumerProvider vertexConsumers, int light, int overlay) {
        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer renderer = client.textRenderer;
        TravelHandler travel = tardis.travel();
        DirectedGlobalPos abpd = travel.getState() == TravelHandlerBase.State.FLIGHT
                ? travel.getProgress()
                : travel.position();
        DirectedGlobalPos.Cached dabpd = travel.destination();
        DirectedGlobalPos.Cached abpp = travel.isLanded() || travel.getState() != TravelHandlerBase.State.MAT
                ? travel.getProgress()
                : travel.position();

        BlockPos abppPos = abpp.getPos();
        BlockPos abpdPos = abpd.getPos();
        matrices.push();
        // TODO dont forget to add variant.getConsoleTextPosition()!
        matrices.translate(0.5, 0.75, 0.5);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        matrices.scale(0.005f, 0.005f, 0.005f);
        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(-60f));
        matrices.translate(-60f, -228, -188);
        String positionPosText = " " + abppPos.getX() + ", " + abppPos.getY() + ", " + abppPos.getZ();
        Text positionDimensionText = WorldUtil.worldText(abpp.getDimension());
        String positionDirectionText = " " + DirectionControl.rotationToDirection(abpp.getRotation()).toUpperCase();
        String destinationPosText = " " + abpdPos.getX() + ", " + abpdPos.getY() + ", " + abpdPos.getZ();
        Text destinationDimensionText = WorldUtil.worldText(abpd.getDimension());
        String destinationDirectionText = " " + DirectionControl.rotationToDirection(abpd.getRotation()).toUpperCase();
        renderer.drawWithOutline(Text.of("❌").asOrderedText(), 0, 40, 0xF00F00, 0x000000,
                matrices.peek().getPositionMatrix(), vertexConsumers, 0xF000F0);
        renderer.drawWithOutline(Text.of(positionPosText).asOrderedText(), 0, 48, 0xFFFFFF, 0x000000,
                matrices.peek().getPositionMatrix(), vertexConsumers, 0xF000F0);
        renderer.drawWithOutline(positionDimensionText.asOrderedText(), 0, 56, 0xFFFFFF, 0x000000,
                matrices.peek().getPositionMatrix(), vertexConsumers, 0xF000F0);
        renderer.drawWithOutline(Text.of(positionDirectionText).asOrderedText(), 0, 64, 0xFFFFFF, 0x000000,
                matrices.peek().getPositionMatrix(), vertexConsumers, 0xF000F0);
        matrices.pop();

        matrices.push();
        matrices.translate(0.5, 0.75, 0.5);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        matrices.scale(0.005f, 0.005f, 0.005f);
        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(120f));
        matrices.translate(-60f, -228, -188);
        renderer.drawWithOutline(Text.of("✛").asOrderedText(), 0, 40, 0x00F0FF, 0x000000,
                matrices.peek().getPositionMatrix(), vertexConsumers, 0xF000F0);
        renderer.drawWithOutline(Text.of(destinationPosText).asOrderedText(), 0, 48, 0xFFFFFF, 0x000000,
                matrices.peek().getPositionMatrix(), vertexConsumers, 0xF000F0);
        renderer.drawWithOutline(destinationDimensionText.asOrderedText(), 0, 56, 0xFFFFFF, 0x000000,
                matrices.peek().getPositionMatrix(), vertexConsumers, 0xF000F0);
        renderer.drawWithOutline(Text.of(destinationDirectionText).asOrderedText(), 0, 64, 0xFFFFFF, 0x000000,
                matrices.peek().getPositionMatrix(), vertexConsumers, 0xF000F0);
        matrices.pop();

        matrices.push();
        matrices.translate(0.5, 0.75, 0.5);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        matrices.scale(0.015f, 0.015f, 0.015f);
        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(120f));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-72.5f));
        matrices.translate(-5f, 62, -48);
        String progressText = tardis.travel().getState() == TravelHandlerBase.State.LANDED
                ? "0%"
                : tardis.travel().getDurationAsPercentage() + "%";
        /*renderer.drawWithOutline(Text.of("⏳").asOrderedText(), 0, 0, 0x00FF0F, 0x000000,
                matrices.peek().getPositionMatrix(), vertexConsumers, 0xF000F0);*/
        renderer.drawWithOutline(Text.of(progressText).asOrderedText(), 0, 0, 0xFFFFFF, 0x000000,
                matrices.peek().getPositionMatrix(), vertexConsumers, 0xF000F0);
        matrices.pop();
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
