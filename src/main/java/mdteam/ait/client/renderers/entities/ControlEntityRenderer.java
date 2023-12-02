package mdteam.ait.client.renderers.entities;

import mdteam.ait.client.models.consoles.ControlModel;
import mdteam.ait.core.entities.ConsoleControlEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

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
        //super.render(livingEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(ConsoleControlEntity controlEntity) {
        return TEXTURE;
    }

    @Override
    protected void scale(ConsoleControlEntity controlEntity, MatrixStack matrixStack, float f) {
        matrixStack.scale(1f, 1f, 1f);
    }

    @Override
    protected void setupTransforms(ConsoleControlEntity controlEntity, MatrixStack matrixStack, float f, float g, float h) {

    }
}
