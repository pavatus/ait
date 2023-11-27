package mdteam.ait.client.renderers.exteriors;

import com.google.common.collect.ImmutableMap;
import mdteam.ait.AITMod;
import mdteam.ait.client.models.exteriors.ExteriorModel;
import mdteam.ait.client.models.exteriors.FalloutExterior;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.blocks.ExteriorBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

import java.util.Map;

public class ExteriorRenderer<T extends ExteriorBlockEntity> implements BlockEntityRenderer<T> {
    public static final Identifier EXTERIOR_TEXTURE = new Identifier(AITMod.MOD_ID, ("textures/blockentities/exteriors/shelter.png"));
    public static final Identifier EXTERIOR_TEXTURE_EMISSION = new Identifier(AITMod.MOD_ID, "textures/blockentities/exteriors/shelter_emission.png");
    private final Map<ExteriorEnum, ModelPart> exteriormap;
    private ExteriorModel model;

    public Map<ExteriorEnum, ModelPart> getModels() {
        ImmutableMap.Builder<ExteriorEnum, ModelPart> builder = ImmutableMap.builder();
        builder.put(ExteriorEnum.SHELTER, FalloutExterior.getTexturedModelData().createModel());
        return builder.build();
    }
    public ExteriorRenderer(BlockEntityRendererFactory.Context ctx) {
        this.exteriormap = this.getModels();
    }

    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (model == null)
            this.model = new FalloutExterior(this.exteriormap.get(entity.getExterior()));

        BlockState blockState = entity.getCachedState();
        float f = blockState.get(ExteriorBlock.FACING).asRotation();
        int maxLight = 0x0;//0xF000F0;
       // float exteriorState = getMaterialStateForAlpha(entity.getMaterialState());
        matrices.push();
        matrices.translate(0.5, 0, 0.5);
        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(f));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        if(model != null) {
            ((FalloutExterior) model).door.yaw = entity.getLeftDoorRotation();
            model.renderWithAnimations(entity,this.model.getPart(),matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentCull(EXTERIOR_TEXTURE)), light, overlay, 1, 1, 1, 1);
            model.renderWithAnimations(entity,this.model.getPart(),matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentEmissive(EXTERIOR_TEXTURE_EMISSION)), light, overlay, 1, 1, 1, 1);
        }
        matrices.pop();
    }

    public float getMaterialStateForAlpha(MaterialStateEnum materialState) {
        switch(materialState) {
            case DEMAT:
                return -0.125F;
            case IN_VORTEX:
                return 0;
            case REMAT:
                return 0.125F;
            default:
                return 1;
        }
    }
}
