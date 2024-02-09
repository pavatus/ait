package mdteam.ait.client.renderers.consoles;

import mdteam.ait.client.models.consoles.ConsoleModel;
import mdteam.ait.client.registry.ClientConsoleVariantRegistry;
import mdteam.ait.client.registry.console.ClientConsoleVariantSchema;
import mdteam.ait.client.renderers.AITRenderLayers;
import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

public class ConsoleRenderer<T extends ConsoleBlockEntity> implements BlockEntityRenderer<T> {
    private final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
    private ConsoleModel console;
    public ConsoleRenderer(BlockEntityRendererFactory.Context ctx) {}
    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (entity.findTardis().isEmpty()) return;

        ClientConsoleVariantSchema variant = ClientConsoleVariantRegistry.withParent(entity.getVariant());

        Class<? extends ConsoleModel> modelClass = variant.model().getClass();

        if (console != null && console.getClass() != modelClass)
            console = null;

        if (console == null)
            this.console = variant.model();
        // BlockState blockState = entity.getCachedState();
        // float f = blockState.get(ConsoleBlock.FACING).asRotation();
        int maxLight = 0xFFFFFF;

        matrices.push();
        //matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-f));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        if (console != null) {
            if (entity.findTardis().isEmpty()) return; // for some it forgets the tardis can be null, fucking weird
            console.animateTile(entity);
            console.renderWithAnimations(entity, this.console.getPart(), matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentCull(variant.texture())), light, overlay, 1, 1, 1, 1);

            if (entity.findTardis().get().hasPower())
                console.renderWithAnimations(entity, this.console.getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.tardisRenderEmissionCull(variant.emission(), true)), maxLight, overlay, 1, 1, 1, 1);        }
        matrices.pop();
    }

}
