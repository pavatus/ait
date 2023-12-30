package mdteam.ait.client.renderers.exteriors;

import mdteam.ait.AITMod;
import mdteam.ait.client.models.exteriors.ExteriorModel;
import mdteam.ait.client.renderers.AITRenderLayers;
import mdteam.ait.core.AITItems;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.blocks.ExteriorBlock;
import mdteam.ait.core.item.SonicItem;
import mdteam.ait.tardis.handler.properties.PropertiesHandler;
import mdteam.ait.tardis.handler.properties.PropertiesHolder;
import net.fabricmc.fabric.mixin.object.builder.client.ModelPredicateProviderRegistryAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import mdteam.ait.tardis.TardisExterior;

import static mdteam.ait.core.item.SonicItem.MODE_KEY;

public class ExteriorRenderer<T extends ExteriorBlockEntity> implements BlockEntityRenderer<T> {
    private ExteriorModel model;

    private final ItemStack stack = new ItemStack(AITItems.MECHANICAL_SONIC_SCREWDRIVER, 1);

    public ExteriorRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (entity.tardis() == null) {
            return;
        }

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        TardisExterior tardisExterior = entity.tardis().getExterior();

        if (tardisExterior == null) return;

        Class<? extends ExteriorModel> modelClass = tardisExterior.getVariant().model().getClass();

        if (model != null && !(model.getClass().isInstance(modelClass))) // fixme this is bad it seems to constantly create a new one anyway but i didnt realise.
            model = null;

        if (model == null)
            this.model = tardisExterior.getVariant().model();

        BlockState blockState = entity.getCachedState();
        float f = blockState.get(ExteriorBlock.FACING).asRotation();
        int maxLight = 0xFFFFFF;
        /*matrices.push();
        matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(180f));
        matrices.translate(0, -2.72f, -0.32);
        matrices.scale(0.0125f, 0.0125f, 0.0125f);
        textRenderer.drawWithOutline(Text.of("POLICE -=- BOX").asOrderedText(), -"POLICE -=- BOX".length() * 5.625f, 0, 0xFFFFFF, 0x000000, matrices.peek().getPositionMatrix(),vertexConsumers, light);
        matrices.pop();*/

        // @TODO make this use the sonic to generate the interior the same as before AND NOT HARDCODE THE SONICS

        if(entity.tardis().getHandlers().getInteriorChanger().isGenerating()) {
            matrices.push();
            matrices.translate(this.valuesForRotationalTransform(blockState.get(ExteriorBlock.FACING))[0],
                    this.valuesForRotationalTransform(blockState.get(ExteriorBlock.FACING))[1],
                    this.valuesForRotationalTransform(blockState.get(ExteriorBlock.FACING))[2]);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-f));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(45f));
            matrices.scale(0.6f, 0.6f, 0.6f);
            if (entity.getWorld() == null) return;
            int lightAbove = WorldRenderer.getLightmapCoordinates(entity.getWorld(), entity.getPos().up());
            //VertexConsumerProvider provider = layer -> vertexConsumers.getBuffer(RenderLayer.getItemEntityTranslucentCull(new Identifier(AITMod.MOD_ID, "textures/item/sonic_tools/mechanical_tardis.png")));
            MinecraftClient.getInstance().getItemRenderer().renderItem(null, stack, ModelTransformationMode.GROUND, false, matrices, vertexConsumers, entity.getWorld(), lightAbove, overlay, 0);
            matrices.pop();
        }
        matrices.push();
        matrices.translate(0.5, 0, 0.5);
        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(f));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        Identifier texture = entity.tardis().getExterior().getVariant().texture();
        if (model != null) {
            model.animateTile(entity);
            model.renderWithAnimations(entity, this.model.getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.getEntityTranslucentCull(texture)), light, overlay, 1, 1, 1, 1);
            if (entity.tardis().getHandlers().getOvergrownHandler().isOvergrown()) {
                model.renderWithAnimations(entity, this.model.getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.getEntityTranslucentCull(entity.tardis().getHandlers().getOvergrownHandler().getOvergrownTexture())), light, overlay, 1, 1, 1, 1);
            }
            if (tardisExterior.getVariant().emission() != null) {
                boolean alarms = PropertiesHandler.getBool(entity.tardis().getHandlers().getProperties(), PropertiesHandler.ALARM_ENABLED);

                model.renderWithAnimations(entity, this.model.getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.tardisRenderEmissionCull(entity.tardis().getExterior().getVariant().emission(), false)), maxLight, overlay, 1, alarms ? 0.3f : 1 , alarms ? 0.3f : 1, 1);
            }
        }
        matrices.pop();
    }

    public float[] valuesForRotationalTransform(Direction direction) {
        return switch(direction) {
            case EAST -> new float[]{1.32f, 1.32f, 0.435f};
            case SOUTH -> new float[]{0.56f, 1.32f, 1.32f};
            case WEST -> new float[]{-0.32f, 1.32f, 0.56f};
            default -> new float[]{0.435f, 1.32f, -0.32f};
        };
    }
}
