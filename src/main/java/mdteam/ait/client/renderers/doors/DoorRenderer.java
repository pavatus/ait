package mdteam.ait.client.renderers.doors;

import com.google.common.collect.ImmutableMap;
import mdteam.ait.AITMod;
import mdteam.ait.client.models.doors.DoorModel;
import mdteam.ait.client.models.doors.FalloutDoorModel;
import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.core.blockentities.DoorBlockEntity;
import mdteam.ait.core.blocks.ExteriorBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import the.mdteam.ait.TardisExterior;

import java.util.Map;

public class DoorRenderer<T extends DoorBlockEntity> implements BlockEntityRenderer<T> {
    public static final Identifier DOOR_TEXTURE = new Identifier(AITMod.MOD_ID, ("textures/blockentities/doors/shelter_door.png"));
    private final Map<ExteriorEnum, ModelPart> exteriormap;
    private DoorModel model;

    public Map<ExteriorEnum, ModelPart> getModels() {
        ImmutableMap.Builder<ExteriorEnum, ModelPart> builder = ImmutableMap.builder();
        builder.put(ExteriorEnum.SHELTER, FalloutDoorModel.getTexturedModelData().createModel());
        return builder.build();
    }

    public DoorRenderer(BlockEntityRendererFactory.Context ctx) {
        this.exteriormap = this.getModels();
    }

    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (entity.getTardis() == null)
            return;

        TardisExterior tardisExterior = entity.getTardis().getExterior();
        Class<? extends DoorModel> modelClass = tardisExterior.getType().getDoorClass();

        if (model != null && !(model.getClass().isInstance(modelClass)))
            model = null;

        if (model == null)
            this.model = entity.getTardis().getExterior().getType().createDoorModel();

        BlockState blockState = entity.getCachedState();
        float f = blockState.get(ExteriorBlock.FACING).asRotation();
        int maxLight = 0xFFFFFF;//0xF000F0;
        // float exteriorState = getMaterialStateForAlpha(entity.getMaterialState());
        matrices.push();
        matrices.translate(0.5, 0, 0.5);
        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(f));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        if(model != null) {
            model.renderWithAnimations(entity,this.model.getPart(),matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentCull(model.getTexture())), light, overlay, 1, 1, 1, 1);
            model.renderWithAnimations(entity,this.model.getPart(),matrices, vertexConsumers.getBuffer(RenderLayer.getEyes(model.getEmission())), maxLight, overlay, 1, 1, 1, 1);
        }
        matrices.pop();
    }
}
