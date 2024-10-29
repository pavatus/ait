package loqor.ait.client.renderers.machines;

import org.joml.Matrix4f;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.LightType;

import loqor.ait.AITMod;
import loqor.ait.client.models.machines.PlugboardModel;
import loqor.ait.core.blockentities.PlugBoardBlockEntity;
import loqor.ait.core.blocks.PlugBoardBlock;

public class PlugBoardRenderer<T extends PlugBoardBlockEntity> implements BlockEntityRenderer<T> {

    private final MinecraftClient client = MinecraftClient.getInstance();
    private EntityRenderDispatcher dispatcher;
    PlugboardModel model;
    public static final Identifier TEXTURE = new Identifier(AITMod.MOD_ID,
            "textures/blockentities/machines/plugboard.png");

    public PlugBoardRenderer(BlockEntityRendererFactory.Context ctx) {
        this.dispatcher = ctx.getEntityRenderDispatcher();
        this.model = new PlugboardModel(PlugboardModel.getTexturedModelData().createModel());
    }

    @Override
    public void render(PlugBoardBlockEntity entity, float tickDelta, MatrixStack matrices,
            VertexConsumerProvider vertexConsumers, int light, int overlay) {
        BlockState state = entity.getCachedState();
        for (int i = 0; i < 12; i++) {
            ItemStack link = entity.getLink(i);

            if (link == null)
                continue;

            matrices.push();
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));

            PlugBoardBlock.Handle<?> handle = PlugBoardBlock.HANDLES[i];
            VoxelShape shape = handle.shape().get(entity.getCachedState().get(PlugBoardBlock.FACING));

            matrices.translate(shape.getMin(Direction.Axis.X), shape.getMin(Direction.Axis.Y),
                    shape.getMin(Direction.Axis.Z));

            this.client.getItemRenderer().renderItem(link, ModelTransformationMode.FIXED, light, overlay, matrices,
                    vertexConsumers, entity.getWorld(), 0);

            matrices.pop();
        }
        matrices.push();
        matrices.translate(0.5, 1.5, 0.5);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(state.get(PlugBoardBlock.FACING).asRotation()));
        this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(TEXTURE)), light, overlay, 1,
                1, 1, 1);
        matrices.pop();

        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) {
            return;
        }

        this.renderLeash(entity, tickDelta, matrices, vertexConsumers, player);
    }

    private <E extends ClientPlayerEntity> void renderLeash(PlugBoardBlockEntity entity, float tickDelta,
            MatrixStack matrices, VertexConsumerProvider provider, E holdingEntity) {
        int u;
        matrices.push();
        Vec3d vec3d = new Vec3d(3.5, 130.5, -4.5); // holdingEntity.getLeashPos(tickDelta);
        double d = (MathHelper.lerp(tickDelta, entity.getPos().getY(), entity.getPos().getY())
                * ((float) Math.PI / 180)) + 1.5707963267948966;
        Vec3d vec3d2 = new Vec3d(0.5, 0.5, 0);
        double e = Math.cos(d) * vec3d2.z + Math.sin(d) * vec3d2.x;
        double f = Math.sin(d) * vec3d2.z - Math.cos(d) * vec3d2.x;
        double g = MathHelper.lerp((double) tickDelta, entity.getPos().getX(), entity.getPos().getX()) + e;
        double h = MathHelper.lerp((double) tickDelta, entity.getPos().getY(), entity.getPos().getY()) + vec3d2.y;
        double i = MathHelper.lerp((double) tickDelta, entity.getPos().getZ(), entity.getPos().getZ()) + f;
        matrices.translate(e, vec3d2.y, f);
        float j = (float) (vec3d.x - g);
        float k = (float) (vec3d.y - h);
        float l = (float) (vec3d.z - i);
        float m = 0.025f;
        VertexConsumer vertexConsumer = provider.getBuffer(RenderLayer.getLeash());
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        float n = MathHelper.inverseSqrt(j * j + l * l) * 0.025f / 2.0f;
        float o = l * n;
        float p = j * n;
        BlockPos blockPos = BlockPos.ofFloored(entity.getPos().getX(), entity.getPos().getY(), entity.getPos().getZ());
        BlockPos blockPos2 = BlockPos.ofFloored(holdingEntity.getCameraPosVec(tickDelta));
        int q = entity.getWorld().getLightLevel(LightType.BLOCK, blockPos);
        int r = holdingEntity.getWorld().getLightLevel(LightType.BLOCK, blockPos2);
        int s = entity.getWorld().getLightLevel(LightType.SKY, blockPos);
        int t = entity.getWorld().getLightLevel(LightType.SKY, blockPos2);
        for (u = 0; u <= 24; ++u) {
            PlugBoardRenderer.renderLeashPiece(vertexConsumer, matrix4f, j, k, l, q, r, s, t, 0.025f, 0.025f, o, p, u,
                    false);
        }
        for (u = 24; u >= 0; --u) {
            PlugBoardRenderer.renderLeashPiece(vertexConsumer, matrix4f, j, k, l, q, r, s, t, 0.025f, 0.0f, o, p, u,
                    true);
        }
        matrices.pop();
    }

    private static void renderLeashPiece(VertexConsumer vertexConsumer, Matrix4f positionMatrix, float f, float g,
            float h, int leashedEntityBlockLight, int holdingEntityBlockLight, int leashedEntitySkyLight,
            int holdingEntitySkyLight, float i, float j, float k, float l, int pieceIndex, boolean isLeashKnot) {
        float m = (float) pieceIndex / 24.0f;
        int n = (int) MathHelper.lerp(m, (float) leashedEntityBlockLight, (float) holdingEntityBlockLight);
        int o = (int) MathHelper.lerp(m, (float) leashedEntitySkyLight, (float) holdingEntitySkyLight);
        int p = LightmapTextureManager.pack(n, o);
        float q = pieceIndex % 2 == (isLeashKnot ? 1 : 0) ? 0.7f : 1.0f;
        float r = 0.1f * q;
        float s = 0.1f * q;
        float t = 0.1f * q;
        float u = f * m;
        float v = g > 0.0f ? g * m * m : g - g * (1.0f - m) * (1.0f - m);
        float w = h * m;
        vertexConsumer.vertex(positionMatrix, u - k, v + j, w + l).color(r, s, t, 1.0f).light(p).next();
        vertexConsumer.vertex(positionMatrix, u + k, v + i - j, w - l).color(r, s, t, 1.0f).light(p).next();
    }

    protected int getBlockLight(T entity, BlockPos pos) {
        return entity.getWorld().getLightLevel(LightType.BLOCK, pos);
    }
}
