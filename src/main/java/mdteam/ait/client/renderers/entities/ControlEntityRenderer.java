package mdteam.ait.client.renderers.entities;

import mdteam.ait.AITMod;
import mdteam.ait.client.models.consoles.ControlModel;
import mdteam.ait.core.AITItems;
import mdteam.ait.core.entities.ConsoleControlEntity;
import mdteam.ait.core.item.SonicItem;
import mdteam.ait.core.util.AITConfig;
import mdteam.ait.core.util.AITConfigModel;
import mdteam.ait.tardis.Tardis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;

import static mdteam.ait.AITMod.AIT_CONFIG;

@Environment(value = EnvType.CLIENT)
public class ControlEntityRenderer
        extends LivingEntityRenderer<ConsoleControlEntity, ControlModel> {

    private static final Identifier TEXTURE = new Identifier(AITMod.MOD_ID, "textures/entity/control/sequenced.png");

    ControlModel model = new ControlModel(ControlModel.getTexturedModelData().createModel());

    public ControlEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new ControlModel(ControlModel.getNotModelData().createModel()), 0f);
    }

    @Override
    public void render(ConsoleControlEntity livingEntity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
        super.render(livingEntity, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
    }

    @Override
    protected void renderLabelIfPresent(ConsoleControlEntity entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        double d = this.dispatcher.getSquaredDistanceToCamera(entity);
        if (d > 4096.0) {
            return;
        }
        float f = entity.getNameLabelHeight() - 0.3f;
        matrices.push();
        matrices.translate(0.0f, f, 0.0f);
        matrices.multiply(this.dispatcher.getRotation());
        matrices.scale(-0.0075f, -0.0075f, 0.0075f);
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        TextRenderer textRenderer = this.getTextRenderer();
        float h = (float) -textRenderer.getWidth(text) / 2;
        HitResult hitresult = MinecraftClient.getInstance().crosshairTarget;
        if (hitresult != null) {
            boolean isPlayerLooking = isPlayerLookingAtControl(hitresult, entity);
            OrderedText orderedText = Text.of(text.getString().toUpperCase().replace("_", " ")).asOrderedText();
            if (isPlayerLooking) {
                textRenderer.drawWithOutline(orderedText, h, (float) text.getString().length(), 0xF0F0F0, 0x000000, matrix4f, vertexConsumers, 0xFF);
            }
        }
        matrices.pop();
        if (hitresult != null) {
            boolean isPlayerHoldingScanningSonic = isPlayerHoldingScanningSonic(entity);
            if (isPlayerHoldingScanningSonic) {
                if (entity.isPartOfSequence()) {
                    matrices.push();
                    matrices.scale(0.4f, 0.4f, 0.4f);
                    matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(180f));
                    matrices.translate(0, (-2 - entity.getControlHeight() / 2) + entity.getWorld().random.nextFloat() * 0.02, 0);
                    matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MinecraftClient.getInstance().getTickDelta() % 180));
                    this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE)), light, OverlayTexture.DEFAULT_UV,
                            !entity.wasSequenced() ? 0.95f - (entity.getSequenceColor() >= 1 ? entity.getSequenceColor() + 1 : entity.getSequenceColor() * 0.35f) : 0.0f,
                            !entity.wasSequenced() ? 0.3f + (entity.getSequenceColor() >= 1 ? entity.getSequenceColor() + 1 : entity.getSequenceColor() * 0.35f) : 0.9f,
                            !entity.wasSequenced() ? 0.3f + (entity.getSequenceColor() >= 1 ? entity.getSequenceColor() + 1 : entity.getSequenceColor() * 0.35f) : 0.1f, entity.getWorld().random.nextInt(32) != 6 ? 0.4f : 0.05f);
                    this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEyes(TEXTURE)), 0xFF00F0, OverlayTexture.DEFAULT_UV,
                            !entity.wasSequenced() ? 0.95f - (entity.getSequenceColor() >= 1 ? entity.getSequenceColor() + 1 : entity.getSequenceColor() * 0.35f) : 0.5f,
                            !entity.wasSequenced() ? 0.3f + (entity.getSequenceColor() >= 1 ? entity.getSequenceColor() + 1 : entity.getSequenceColor() * 0.35f) : 0.9f,
                            !entity.wasSequenced() ? 0.3f + (entity.getSequenceColor() >= 1 ? entity.getSequenceColor() + 1 : entity.getSequenceColor() * 0.35f) : 0.5f, entity.getWorld().random.nextInt(32) != 6 ? 0.4f : 0.05f);
                    matrices.pop();
                }
            }
        }
    }

    public static boolean isPlayerLookingAtControl(HitResult hitResult, ConsoleControlEntity entity) {
        if (entity.getWorld() == null || !entity.getWorld().isClient())
            return false;
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null) {
            if (player.getMainHandStack().getItem() instanceof SonicItem) {
                ItemStack sonic = player.getMainHandStack();
                NbtCompound nbt = sonic.getOrCreateNbt();
                if (hitResult.getType() == HitResult.Type.ENTITY) {
                    Entity hitEntity = ((EntityHitResult) hitResult).getEntity();
                    return hitEntity != null && hitEntity.equals(entity) && nbt.contains(SonicItem.MODE_KEY) && nbt.getInt(SonicItem.MODE_KEY) == 3;
                }
            } else if (player.getOffHandStack().getItem() instanceof SonicItem) {
                ItemStack sonic = player.getOffHandStack();
                NbtCompound nbt = sonic.getOrCreateNbt();
                if (hitResult.getType() == HitResult.Type.ENTITY) {
                    Entity hitEntity = ((EntityHitResult) hitResult).getEntity();
                    return hitEntity != null && hitEntity.equals(entity) && nbt.contains(SonicItem.MODE_KEY) && nbt.getInt(SonicItem.MODE_KEY) == 3;
                }
            }
        }
        return false;
    }

    public static boolean isPlayerHoldingScanningSonic(ConsoleControlEntity entity) {
        if (entity.getWorld() == null || !entity.getWorld().isClient())
            return false;
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null) {
            if (player.getOffHandStack().getItem() instanceof SonicItem) {
                ItemStack sonic = player.getOffHandStack();
                NbtCompound nbt = sonic.getOrCreateNbt();
                return nbt.contains(SonicItem.MODE_KEY) && nbt.getInt(SonicItem.MODE_KEY) == 3;
            }
        }
        return false;
    }

    @Override
    public Identifier getTexture(ConsoleControlEntity controlEntity) {
        return TEXTURE;
    }

    @Override
    protected void setupTransforms(ConsoleControlEntity controlEntity, MatrixStack matrixStack, float f, float g, float h) {

    }
}
