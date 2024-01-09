package mdteam.ait.client.renderers.consoles;

import mdteam.ait.AITMod;
import mdteam.ait.client.models.consoles.ConsoleGeneratorModel;
import mdteam.ait.client.models.consoles.ConsoleModel;
import mdteam.ait.client.models.consoles.CoralConsoleModel;
import mdteam.ait.client.models.consoles.HartnellConsoleModel;
import mdteam.ait.client.registry.ClientConsoleVariantRegistry;
import mdteam.ait.client.registry.console.ClientConsoleVariantSchema;
import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.core.blockentities.ConsoleGeneratorBlockEntity;
import mdteam.ait.core.blocks.ConsoleGeneratorBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class ConsoleGeneratorRenderer<T extends ConsoleGeneratorBlockEntity> implements BlockEntityRenderer<T> {

    private ConsoleGeneratorModel generator;
    private Identifier consoleTexture;
    private ConsoleModel console;

    public static final Identifier TEXTURE = new Identifier(AITMod.MOD_ID, "textures/blockentities/consoles/console_generator/console_generator.png");

    public ConsoleGeneratorRenderer(BlockEntityRendererFactory.Context ctx) {
        this.generator = new ConsoleGeneratorModel(ConsoleGeneratorModel.getTexturedModelData().createModel());
    }
    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {

        if(ClientConsoleVariantRegistry.REGISTRY.get(entity.getConsoleSchema().id()) == null) return;

        int maxLight = 0xFFFFFF;

        this.console = ClientConsoleVariantRegistry.REGISTRY.get(entity.getConsoleSchema().id()).model();
        this.consoleTexture = ClientConsoleVariantRegistry.REGISTRY.get(entity.getConsoleSchema().id()).texture();

        matrices.push();

        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        matrices.translate(0.5f, -1.5f, -0.5f);

        this.generator.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentCull(TEXTURE)), light, overlay, 1, 1, 1, 1);

        matrices.pop();

        matrices.push();

        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        matrices.translate(0.5f, -1.5f + entity.getWorld().random.nextFloat() * 0.02, -0.5f);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MinecraftClient.getInstance().getTickDelta() % 180));

        this.console.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentEmissive(this.consoleTexture)), maxLight, OverlayTexture.DEFAULT_UV, 0.3607843137f,0.9450980392f, 1, entity.getWorld().random.nextInt(32) != 6 ? 0.4f : 0.05f);

        matrices.pop();
    }
}
