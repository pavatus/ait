package mdteam.ait.client.renderers.consoles;

import mdteam.ait.client.models.consoles.ConsoleModel;
import mdteam.ait.client.registry.ClientConsoleVariantRegistry;
import mdteam.ait.client.registry.console.ClientConsoleVariantSchema;
import mdteam.ait.client.renderers.AITRenderLayers;
import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.tardis.console.type.ConsoleTypeSchema;
import mdteam.ait.tardis.data.SonicHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class ConsoleRenderer<T extends ConsoleBlockEntity> implements BlockEntityRenderer<T> {
    private ConsoleModel console;
    public ConsoleRenderer(BlockEntityRendererFactory.Context ctx){}
    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (entity.findTardis().isEmpty()) return;

        ClientConsoleVariantSchema variant = ClientConsoleVariantRegistry.withParent(entity.getVariant());

        if(variant == null) return;

        Class<? extends ConsoleModel> modelClass = variant.model().getClass();

        if (console != null && console.getClass() != modelClass)
            console = null;

        if (console == null)
            this.console = variant.model();
        // BlockState blockState = entity.getCachedState();
        // float f = blockState.get(ConsoleBlock.FACING).asRotation();
        int maxLight = 0xFFFFFF;

        matrices.push();
        //matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(f));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        if (console != null) {
            if (entity.findTardis().isEmpty()) return; // for some it forgets the tardis can be null, fucking weird
            console.animateBlockEntity(entity);
            console.renderWithAnimations(entity, this.console.getPart(), matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentCull(variant.texture())), light, overlay, 1, 1, 1, 1);

            if (entity.findTardis().get().hasPower())
                console.renderWithAnimations(entity, this.console.getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.tardisRenderEmissionCull(variant.emission(), true)), maxLight, overlay, 1, 1, 1, 1);        }
        matrices.pop();
        if(!entity.findTardis().get().getHandlers().getSonic().hasSonic(SonicHandler.HAS_CONSOLE_SONIC)) return;
        matrices.push();
        matrices.translate(sonicItemTranslations(variant).x(), sonicItemTranslations(variant).y(), sonicItemTranslations(variant).z());
        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(sonicItemRotations(variant)[0]));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(sonicItemRotations(variant)[1]));
        matrices.scale(0.9f, 0.9f, 0.9f);
        int lightAbove = WorldRenderer.getLightmapCoordinates(entity.getWorld(), entity.getPos().up());
        ItemStack stack = entity.findTardis().get().getHandlers().getSonic().get(SonicHandler.HAS_CONSOLE_SONIC);
        MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformationMode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 0);
        matrices.pop();
    }

    private Vector3f sonicItemTranslations(ClientConsoleVariantSchema schema) {
        return switch(schema.parent().id().getPath()) {
            case "console/hartnell" -> new Vector3f(0.1f, 1.2f, 0.26f);
            case "console/coral" -> new Vector3f(1.15f, 1.2f, 0.5f);
            case "console/toyota" -> new Vector3f(-0.5275f, 1.35f, 0.7f);
            case "console/alnico" -> new Vector3f(-0.55f, 1.1f, -0.1f);
            case "console/steam" -> new Vector3f(0.9f, 1.125f, -0.19f);
            default -> new Vector3f(0, 0, 0);
        };
    }

    private float[] sonicItemRotations(ClientConsoleVariantSchema schema) {
        return switch(schema.parent().id().getPath()) {
            case "console/hartnell", "console/alnico" -> new float[] {120f, 135f};
            case "console/coral" -> new float[] {90f, 135f};
            case "console/toyota" -> new float[] {-120f, -45f};
            case "console/steam" -> new float[] {30f, 120f};
            default -> new float[] {0, 0};
        };
    }

}
