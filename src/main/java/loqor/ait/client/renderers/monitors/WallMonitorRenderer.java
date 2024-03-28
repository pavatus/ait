package loqor.ait.client.renderers.monitors;

import loqor.ait.AITMod;
import loqor.ait.client.models.decoration.PlaqueModel;
import loqor.ait.core.blockentities.PlaqueBlockEntity;
import loqor.ait.core.blockentities.WallMonitorBlockEntity;
import loqor.ait.core.blocks.PlaqueBlock;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.control.impl.DimensionControl;
import loqor.ait.tardis.util.AbsoluteBlockPos;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;

public class WallMonitorRenderer<T extends WallMonitorBlockEntity> implements BlockEntityRenderer<T> {

    public static final Identifier PLAQUE_TEXTURE = new Identifier(AITMod.MOD_ID, ("textures/blockentities/decoration/plaque.png"));
    private final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
    private final PlaqueModel plaqueModel;

    public WallMonitorRenderer(BlockEntityRendererFactory.Context ctx) {
        this.plaqueModel = new PlaqueModel(PlaqueModel.getTexturedModelData().createModel());
    }

    @Override
    public void render(WallMonitorBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {

        BlockState blockState = entity.getCachedState();

        Direction k = blockState.get(PlaqueBlock.FACING);

        matrices.push();

        matrices.translate(0.5f, 1.5f, 0.5f);

        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(k.asRotation()));

        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));

        this.plaqueModel.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(PLAQUE_TEXTURE)), light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);

        matrices.pop();

        if(entity.findTardis().isEmpty()) return;
        Tardis tardis = entity.findTardis().get();

        matrices.push();
        matrices.translate(0.5, 0.75, 0.5);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(k.asRotation()));
        matrices.scale(0.01f, 0.01f, 0.01f);
        float xVal = 0;
        matrices.translate(xVal, -35f, 35f);

        AbsoluteBlockPos.Directed abpp = tardis.getTravel().getPosition();
        AbsoluteBlockPos.Directed abpd = tardis.getTravel().getDestination();

        String positionPosText = " " + abpp.getX() + ", " + abpp.getY() + ", " + abpp.getZ();
        String positionDimensionText = " " + DimensionControl.convertWorldValueToModified(abpp.getDimension().getValue());
        String positionDirectionText = " " + abpp.getDirection().toString().toUpperCase();

        String destinationPosText = abpd.getX() + ", " + abpd.getY() + ", " + abpd.getZ();

        this.textRenderer.drawWithOutline(Text.of(positionPosText).asOrderedText(), xVal - ((float) this.textRenderer.getWidth(tardis.getHandlers().getStats().getCreationString()) / 2), 35, 0xFFFFFF,0x000000, matrices.peek().getPositionMatrix(),vertexConsumers, 0xF000F0);
        this.textRenderer.drawWithOutline(Text.of("-========-").asOrderedText(), xVal - ((float) this.textRenderer.getWidth("-========-") / 2), 55, 0xFFFFFF,0x000000, matrices.peek().getPositionMatrix(),vertexConsumers, 0xF000F0);
        this.textRenderer.drawWithOutline(Text.of(destinationPosText).asOrderedText(), xVal - ((float) this.textRenderer.getWidth(tardis.getHandlers().getStats().getName()) / 2), 75, 0xFFFFFF,0x000000, matrices.peek().getPositionMatrix(),vertexConsumers, 0xF000F0);

        matrices.pop();
    }
}