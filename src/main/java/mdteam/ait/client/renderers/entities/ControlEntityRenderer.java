package mdteam.ait.client.renderers.entities;

import mdteam.ait.client.models.consoles.ControlModel;
import mdteam.ait.core.entities.ConsoleControlEntity;
import mdteam.ait.core.item.SonicItem;
import mdteam.ait.tardis.Tardis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
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
import org.joml.Matrix4f;

@Environment(value= EnvType.CLIENT)
public class ControlEntityRenderer
        extends LivingEntityRenderer<ConsoleControlEntity, ControlModel> {

    // Heh, IDK WHAT THE FUCK IM DOING LMAOOOOOO

    private static final Identifier TEXTURE = new Identifier("textures/entity/bat.png");

    public ControlEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new ControlModel(ControlModel.getTexturedModelData().createModel()), 0f);
    }

    @Override
    public void render(ConsoleControlEntity livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        /*matrixStack.push();
        matrixStack.scale(0.5f, 0.5f, 0.5f);
        HitResult hitresult = MinecraftClient.getInstance().crosshairTarget;
        if(hitresult != null) {
            boolean isPlayerLooking = ConsoleControlEntity.isPlayerLookingAtControl(hitresult, livingEntity);
            if(isPlayerLooking) {
                super.render(livingEntity, f, g, matrixStack, vertexConsumerProvider, i);
                matrixStack.push();
                matrixStack.scale(0.4f, 0.4f, 0.4f);
                TextRenderer text = this.getTextRenderer();
                text.drawWithOutline();
                super.render(livingEntity, f, g, matrixStack, vertexConsumerProvider, i);
                matrixStack.pop();
            }
        }
        matrixStack.pop();*/
        super.render(livingEntity, f, g, matrixStack, vertexConsumerProvider, i);
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
        float g = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25f);
        //int j = (int)(g * 255.0f) << 24;
        TextRenderer textRenderer = this.getTextRenderer();
        float h = (float) -textRenderer.getWidth(text) / 2;
        //textRenderer.draw(text, h, (float)text.getString().length(), 0x20FFFFFF, false, matrix4f, vertexConsumers, TextRenderer.TextLayerType.NORMAL, j, light);
        HitResult hitresult = MinecraftClient.getInstance().crosshairTarget;
        if(hitresult != null) {
            boolean isPlayerLooking = isPlayerLookingAtControl(hitresult, entity);
            OrderedText orderedText = Text.of(text.getString().toUpperCase().replace("_", " ")).asOrderedText();
            if (isPlayerLooking) {
                textRenderer.drawWithOutline(orderedText, h, (float) text.getString().length(), 0xF0F0F0, 0x000000, matrix4f, vertexConsumers, 0xFF);
            }
        }
        matrices.pop();
    }

    public static boolean isPlayerLookingAtControl(HitResult hitResult, ConsoleControlEntity entity) {
        if(!entity.getWorld().isClient())
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
            } else if(player.getOffHandStack().getItem() instanceof SonicItem) {
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

    @Override
    public Identifier getTexture(ConsoleControlEntity controlEntity) {
        return TEXTURE;
    }

    @Override
    protected void setupTransforms(ConsoleControlEntity controlEntity, MatrixStack matrixStack, float f, float g, float h) {

    }
}
