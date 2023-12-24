package mdteam.ait.client.renderers.consoles;

import com.google.common.collect.ImmutableMap;
import mdteam.ait.client.models.consoles.BorealisConsoleModel;
import mdteam.ait.client.models.consoles.ConsoleModel;
import mdteam.ait.client.models.consoles.TempConsoleModel;
import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.tardis.variant.console.ConsoleVariantSchema;
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

import java.util.Map;

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

        Class<? extends ConsoleModel> modelClass = entity.getEnum().getModelClass();

        if (console != null && console.getClass() != modelClass)
            console = null;

        if (console == null)
            this.console = entity.getEnum().createModel();

        ConsoleVariantSchema variant = entity.getVariant();

        BlockState blockState = entity.getCachedState();
        //float f = blockState.get(ConsoleBlock.FACING).asRotation();
        int maxLight = 0xFFFFFF;

        // fixme hardcoded to BorealisConsoleModel + just looks rly bad

//        matrices.push();
//        matrices.translate(1.05, 1.275, 1.08);
//        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
//        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(60f));
//        matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(25f));
//        matrices.scale(0.00325f, 0.00325f, 0.00325f);
//        AbsoluteBlockPos.Directed abpd = entity.getTardis().getTravel().getDestination();
//        AbsoluteBlockPos.Directed abpp = entity.getTardis().getTravel().getPosition();
//        String positionPosText = " " + abpp.getX() + ", " + abpp.getY() + ", " + abpp.getZ();
//        String positionDimensionText = " " + convertWorldValueToModified(abpp.getDimension().getValue());
//        String positionDirectionText = " " + abpp.getDirection().toString().toUpperCase();
//        String destinationPosText = " " + abpd.getX() + ", " + abpd.getY() + ", " + abpd.getZ();
//        String destinationDimensionText = " " + convertWorldValueToModified(abpd.getDimension().getValue());
//        String destinationDirectionText = " " + abpd.getDirection().toString().toUpperCase();
//        matrices.translate(60, 0, 0);
//        this.textRenderer.drawWithOutline(Text.of("  Position").asOrderedText(), (1 - (float) "  Position".length() / 0.5f), 0, maxLight,0x000000, matrices.peek().getPositionMatrix(),vertexConsumers, light);
//        this.textRenderer.drawWithOutline(Text.of(positionPosText).asOrderedText(), (1 - (float) positionPosText.length() / 0.5f), 8, maxLight,0x000000, matrices.peek().getPositionMatrix(),vertexConsumers, light);
//        this.textRenderer.drawWithOutline(Text.of(positionDimensionText).asOrderedText(), (1 - (float) positionDimensionText.length() / 0.5f), 16, maxLight,0x000000, matrices.peek().getPositionMatrix(),vertexConsumers, light);
//        this.textRenderer.drawWithOutline(Text.of(positionDirectionText).asOrderedText(), (1 - (float) positionDirectionText.length() / 0.5f), 24, maxLight,0x000000, matrices.peek().getPositionMatrix(),vertexConsumers, light);
//        this.textRenderer.drawWithOutline(Text.of("  Destination").asOrderedText(), (1 - (float) "  Destination".length() / 0.5f), 32, maxLight,0x000000, matrices.peek().getPositionMatrix(),vertexConsumers, light);
//        this.textRenderer.drawWithOutline(Text.of(destinationPosText).asOrderedText(), (1 - (float) destinationPosText.length() / 0.5f), 40, maxLight,0x000000, matrices.peek().getPositionMatrix(),vertexConsumers, light);
//        this.textRenderer.drawWithOutline(Text.of(destinationDimensionText).asOrderedText(), (1 - (float) destinationDimensionText.length() / 0.5f), 48, maxLight,0x000000, matrices.peek().getPositionMatrix(),vertexConsumers, light);
//        this.textRenderer.drawWithOutline(Text.of(destinationDirectionText).asOrderedText(), (1 - (float) destinationDirectionText.length() / 0.5f), 56, maxLight,0x000000, matrices.peek().getPositionMatrix(),vertexConsumers, light);
//        matrices.pop();

        matrices.push();
        //matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-f));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        if (console != null) {
            console.animateTile(entity);
            console.renderWithAnimations(entity, this.console.getPart(), matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentCull(variant.texture())), light, overlay, 1, 1, 1, 1);
            console.renderWithAnimations(entity, this.console.getPart(), matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentEmissive(variant.emission())), maxLight, overlay, 1, 1, 1, 1);
        }
        matrices.pop();
    }

}
