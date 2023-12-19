package mdteam.ait.client.renderers.consoles;

import com.google.common.collect.ImmutableMap;
import mdteam.ait.client.models.consoles.BorealisConsoleModel;
import mdteam.ait.client.models.consoles.ConsoleModel;
import mdteam.ait.client.models.consoles.TempConsoleModel;
import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import mdteam.ait.tardis.TardisConsole;
import org.joml.Matrix4f;

import java.util.Map;

import static mdteam.ait.tardis.control.impl.DimensionControl.convertWorldValueToModified;

public class ConsoleRenderer<T extends ConsoleBlockEntity> implements BlockEntityRenderer<T> {

    private final Map<ConsoleEnum, ModelPart> consolemap;

    private final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

    private ConsoleModel console;

    public Map<ConsoleEnum, ModelPart> getModels() {
        ImmutableMap.Builder<ConsoleEnum, ModelPart> builder = ImmutableMap.builder();
        builder.put(ConsoleEnum.TEMP, TempConsoleModel.getTexturedModelData().createModel());
        builder.put(ConsoleEnum.BOREALIS, BorealisConsoleModel.getTexturedModelData().createModel());
        return builder.build();
    }

    public ConsoleRenderer(BlockEntityRendererFactory.Context ctx) {
        this.consolemap = this.getModels();
    }

    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (entity.getTardis() == null)
            return;

        TardisConsole tardisConsole = entity.getTardis().getConsole();
        Class<? extends ConsoleModel> modelClass = tardisConsole.getType().getModelClass();

        if (console != null && console.getClass() != modelClass)
            console = null;

        if (console == null)
            this.console = entity.getTardis().getConsole().getType().createModel();

        BlockState blockState = entity.getCachedState();
        //float f = blockState.get(ConsoleBlock.FACING).asRotation();
        int maxLight = 0xFFFFFF;
        matrices.push();
        matrices.translate(1.125, 1.3, 1.125);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(60f));
        matrices.scale(0.00325f, 0.00325f, 0.00325f);
        AbsoluteBlockPos.Directed abpd = entity.getTardis().getTravel().getDestination();
        AbsoluteBlockPos.Directed abpp = entity.getTardis().getTravel().getPosition();
        String positionPosText = " " + abpp.getX() + ", " + abpp.getY() + ", " + abpp.getZ();
        String positionDimensionText = " " + convertWorldValueToModified(abpp.getWorld().getRegistryKey().getValue().getPath());
        String positionDirectionText = " " + abpp.getDirection().toString().toUpperCase();
        String destinationPosText = " " + abpd.getX() + ", " + abpd.getY() + ", " + abpd.getZ();
        String destinationDimensionText = " " + convertWorldValueToModified(abpd.getWorld().getRegistryKey().getValue().getPath());
        String destinationDirectionText = " " + abpd.getDirection().toString().toUpperCase();
        matrices.translate(60, 0, 0);
        this.textRenderer.draw("  Position", (1 - (float) "  Position".length() / 0.5f), 0, maxLight,false, matrices.peek().getPositionMatrix(),vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0x0, light);
        this.textRenderer.draw(positionPosText, (1 - (float) positionPosText.length() / 0.5f), 8, maxLight,false, matrices.peek().getPositionMatrix(),vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0x0, light);
        this.textRenderer.draw(positionDimensionText, (1 - (float) positionDimensionText.length() / 0.5f), 16, maxLight,false, matrices.peek().getPositionMatrix(),vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0x0, light);
        this.textRenderer.draw(positionDirectionText, (1 - (float) positionDirectionText.length() / 0.5f), 24, maxLight,false, matrices.peek().getPositionMatrix(),vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0x0, light);
        this.textRenderer.draw("  Destination", (1 - (float) "  Destination".length() / 0.5f), 32, maxLight,false, matrices.peek().getPositionMatrix(),vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0x0, light);
        this.textRenderer.draw(destinationPosText, (1 - (float) destinationPosText.length() / 0.5f), 40, maxLight,false, matrices.peek().getPositionMatrix(),vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0x0, light);
        this.textRenderer.draw(destinationDimensionText, (1 - (float) destinationDimensionText.length() / 0.5f), 48, maxLight,false, matrices.peek().getPositionMatrix(),vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0x0, light);
        this.textRenderer.draw(destinationDirectionText, (1 - (float) destinationDirectionText.length() / 0.5f), 56, maxLight,false, matrices.peek().getPositionMatrix(),vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0x0, light);
        matrices.pop();
        matrices.push();
        //matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-f));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        if (console != null) {
            console.animateTile(entity);
            console.renderWithAnimations(entity, this.console.getPart(), matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentCull(console.getTexture())), light, overlay, 1, 1, 1, 1);
            console.renderWithAnimations(entity, this.console.getPart(), matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentEmissive(console.getEmission())), maxLight, overlay, 1, 1, 1, 1);
        }
        matrices.pop();
    }

}
