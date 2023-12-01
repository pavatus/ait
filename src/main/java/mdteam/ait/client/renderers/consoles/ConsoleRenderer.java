package mdteam.ait.client.renderers.consoles;

import com.google.common.collect.ImmutableMap;
import mdteam.ait.AITMod;
import mdteam.ait.client.models.consoles.BorealisConsoleModel;
import mdteam.ait.client.models.consoles.ConsoleModel;
import mdteam.ait.client.models.consoles.TempConsoleModel;
import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.core.blocks.ConsoleBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import the.mdteam.ait.TardisConsole;

import java.util.Map;

public class ConsoleRenderer<T extends ConsoleBlockEntity> implements BlockEntityRenderer<T> {

    private final Map<ConsoleEnum, ModelPart> consolemap;

    private ConsoleModel console;
    public static final Identifier CONSOLE_TEXTURE = new Identifier(AITMod.MOD_ID, ("textures/blockentities/display_console.png"));
    public static final Identifier CONSOLE_TEXTURE_EMISSION = new Identifier(AITMod.MOD_ID, ("textures/blockentities/display_console_emission.png"));

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

        if (console != null && !(console.getClass().isInstance(modelClass)))
            console = null;

        if (console == null)
            this.console = entity.getTardis().getConsole().getType().createModel();

        BlockState blockState = entity.getCachedState();
        float f = blockState.get(ConsoleBlock.FACING).asRotation();
        int maxLight = 0xF000F0;
        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-f));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        if(console != null) {
            console.renderWithAnimations(entity,this.console.getPart(),matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentCull(console.getTexture())), light, overlay, 1, 1, 1, 1);
            console.renderWithAnimations(entity,this.console.getPart(),matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentEmissive(console.getEmission())), light, overlay, 1, 1, 1, 1);
        }
        matrices.pop();
    }

}
