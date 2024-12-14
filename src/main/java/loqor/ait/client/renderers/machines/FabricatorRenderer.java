package loqor.ait.client.renderers.machines;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.profiler.Profiler;

import loqor.ait.AITMod;
import loqor.ait.client.models.machines.FabricatorModel;
import loqor.ait.client.util.ClientLightUtil;
import loqor.ait.core.AITItems;
import loqor.ait.core.blockentities.FabricatorBlockEntity;
import loqor.ait.core.blocks.FabricatorBlock;

public class FabricatorRenderer<T extends FabricatorBlockEntity> implements BlockEntityRenderer<T> {

    public static final Identifier FABRICATOR_TEXTURE = new Identifier(AITMod.MOD_ID, "textures/block/fabricator.png");
    public static final Identifier EMISSIVE_FABRICATOR_TEXTURE = new Identifier(AITMod.MOD_ID,
            "textures/block/fabricator_emission.png");
    private final FabricatorModel fabricatorModel;

    public FabricatorRenderer(BlockEntityRendererFactory.Context ctx) {
        this.fabricatorModel = new FabricatorModel(FabricatorModel.getTexturedModelData().createModel());
    }

    @Override
    public void render(FabricatorBlockEntity entity, float tickDelta, MatrixStack matrices,
            VertexConsumerProvider vertexConsumers, int light, int overlay) {
        Profiler profiler = entity.getWorld().getProfiler();
        profiler.push("fabricator");

        matrices.push();
        matrices.translate(0.5f, 1.5f, 0.5f);

        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
        matrices.multiply(RotationAxis.POSITIVE_Y
                .rotationDegrees(entity.getCachedState().get(FabricatorBlock.FACING).asRotation()));

        this.fabricatorModel.render(matrices,
                vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(FABRICATOR_TEXTURE)), light, overlay, 1.0F,
                1.0F, 1.0F, 1.0F);

        if (entity.isValid()) {
            ClientLightUtil.renderEmissive(ClientLightUtil.Renderable.create(fabricatorModel::render),
                    EMISSIVE_FABRICATOR_TEXTURE, entity, fabricatorModel.getPart(), matrices, vertexConsumers, light,
                    overlay, 1, 1, 1, 1);
        }

        matrices.pop();
        matrices.push();

        ItemStack stack = entity.getShowcaseStack();

        if (!stack.isEmpty()) {
            double offset = Math.sin((entity.getWorld().getTime() + tickDelta) / 8.0) / 18.0;

            if (stack.getItem() == AITItems.DEMATERIALIZATION_CIRCUIT) {
                matrices.scale(0.75f, 0.75f, 0.75f);
                matrices.translate(0.65f, 0.35f + (offset / 2), 0.65f);
            } else {
                matrices.scale(1, 1, 1);
                matrices.translate(0.5f, 0.275f + offset, 0.5f);
            }

            MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformationMode.GROUND, light,
                    overlay, matrices, vertexConsumers, entity.getWorld(), 0);
        }
        matrices.pop();
        profiler.pop();
    }
}
