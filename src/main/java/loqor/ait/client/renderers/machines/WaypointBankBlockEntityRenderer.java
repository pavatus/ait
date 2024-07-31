package loqor.ait.client.renderers.machines;

import loqor.ait.core.AITItems;
import loqor.ait.core.blockentities.WaypointBankBlockEntity;
import loqor.ait.core.blocks.WaypointBankBlock;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.core.item.WaypointItem;
import loqor.ait.core.util.WorldUtil;
import loqor.ait.tardis.Tardis;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;

public class WaypointBankBlockEntityRenderer<T extends WaypointBankBlockEntity> implements BlockEntityRenderer<T> {
    private final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
    public WaypointBankBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}
    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {

        if (entity.getCachedState().get(WaypointBankBlock.HALF) == DoubleBlockHalf.UPPER)
            return;

        if (entity.tardis() == null || entity.tardis().isEmpty())
            return;

        Tardis tardis = entity.tardis().get();

        if (!tardis.engine().hasPower())
            return;

        matrices.push();
        matrices.translate(0.5, 0.75, 0.5);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(entity.getCachedState().get(WaypointBankBlock.FACING).asRotation()));
        matrices.scale(0.01f, 0.01f, 0.01f);
        matrices.translate(0f, -142f, -51f);

        DirectedGlobalPos.Cached abpd = tardis.travel().destination();
        BlockPos abpdPos = abpd.getPos();

        String destinationPosText = abpdPos.getX() + ", " + abpdPos.getY() + ", " + abpdPos.getZ();
        String destinationDimensionText = WorldUtil.worldText(abpd.getDimension()).getString();

        String name = "Sunrise Valley";
        this.textRenderer.draw(name, 0 - ((float) this.textRenderer.getWidth(name) / 2), 35, 0xFFFFFF,false, matrices.peek().getPositionMatrix(),vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0xF000F0, 0xF000F0);
        String buh = "------------";
        this.textRenderer.draw(buh, 0 - ((float) this.textRenderer.getWidth(buh) / 2), 46, 0x00F0FF,false, matrices.peek().getPositionMatrix(),vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0xF000F0, 0xF000F0);
        this.textRenderer.draw(destinationPosText, 0 - ((float) this.textRenderer.getWidth(destinationPosText) / 2), 55, 0xFFFFFF,false, matrices.peek().getPositionMatrix(),vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0xF000F0, 0xF000F0);
        String direction = "NORTH WEST";
        this.textRenderer.draw(direction, 0 - ((float) this.textRenderer.getWidth(direction) / 2), 67, 0xFFFFFF,false, matrices.peek().getPositionMatrix(),vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0xF000F0, 0xF000F0);
        this.textRenderer.draw(destinationDimensionText, 0 - ((float) this.textRenderer.getWidth(destinationDimensionText) / 2), 78, 0xFFFFFF,false, matrices.peek().getPositionMatrix(),vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0xF000F0, 0xF000F0);
        String which = "4/16";
        this.textRenderer.draw(which, 0 - ((float) this.textRenderer.getWidth(which) / 2), 96, 0x00F0FF,false, matrices.peek().getPositionMatrix(),vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0xF000F0, 0xF000F0);
        matrices.pop();

        matrices.push();

        ItemStack stack = new ItemStack(AITItems.WAYPOINT_CARTRIDGE);

        double offset = Math.sin((entity.getWorld().getTime() + tickDelta) / 8.0) / 18.0;

        stack.getOrCreateSubNbt(WaypointItem.DISPLAY_KEY).putInt(WaypointItem.COLOR_KEY, 0XFFFFFFF);

        NbtCompound compound = NbtHelper.fromBlockPos(new BlockPos(1, 1, 1));
        compound.putString("dimension", "blah blah blah");
        compound.putByte("rotation", (byte) 3);

        stack.getOrCreateNbt().put(WaypointItem.POS_KEY, compound);

        matrices.translate(0.5f, 2/*0.275f + offset*/, 0.5f);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-entity.getCachedState().get(WaypointBankBlock.FACING).asRotation()));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90f));
        matrices.translate(-0.0625f * 3.5, 0.0625f * 7, 0.0625f * 13.501);

        matrices.push();
        for (int i = 0; i < 8; i++) {
            matrices.translate(0, 0, 0.0625f * 2);

            if (i == 3) {
                matrices.push();
                matrices.translate(0, 0.0625f * 2, 0);
                MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformationMode.GROUND, 0x0F000F0, overlay, matrices, vertexConsumers, entity.getWorld(), 0);
                matrices.pop();
            } else {
                MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformationMode.GROUND, light, overlay, matrices, vertexConsumers, entity.getWorld(), 0);
            }
        }
        matrices.pop();

        matrices.push();
        matrices.translate(0.0625f * 7, 0, 0);
        for (int p = 0; p < 8; p++) {
            matrices.translate(0, 0, 0.0625f * 2);

            if (p == 11) {
                matrices.push();
                matrices.translate(0, 0.0625f * 2, 0);
                MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformationMode.GROUND, 0x0F000F0, overlay, matrices, vertexConsumers, entity.getWorld(), 0);
                matrices.pop();
            } else {
                MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformationMode.GROUND, light, overlay, matrices, vertexConsumers, entity.getWorld(), 0);
            }
        }
        matrices.pop();

        matrices.pop();

        // Color.ofhrsv(); is pretty cool
    }
}
