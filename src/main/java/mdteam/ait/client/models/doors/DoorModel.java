package mdteam.ait.client.models.doors;

import mdteam.ait.AITMod;
import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.core.blockentities.DoorBlockEntity;
import mdteam.ait.tardis.handler.DoorHandler;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

import java.util.function.Function;

@SuppressWarnings("rawtypes")
public abstract class DoorModel extends SinglePartEntityModel {
    public static int MAX_TICK_COUNT = 2 * 20;
    public static String TEXTURE_PATH = "textures/blockentities/exteriors/";

    public DoorModel() {
        this(RenderLayer::getEntityCutoutNoCull);
    }

    public DoorModel(Function<Identifier, RenderLayer> function) {
        super(function);
    }

    public void renderWithAnimations(DoorBlockEntity door, ModelPart root, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float pAlpha) {
        if (door.getTardis() == null) return;

        root.render(matrices, vertices, light, overlay, red, green, blue, pAlpha);
    }

    @Override
    public void setAngles(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

    }

    public void animateTile(DoorBlockEntity interiorDoor) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        if (interiorDoor.getTardis() == null)
            return;
        DoorHandler.DoorStateEnum state = interiorDoor.getTardis().getDoor().getDoorState();
        updateAnimation(interiorDoor.DOOR_STATE, getAnimationForDoorState(state), interiorDoor.animationTimer);
    }

    public Identifier getVariousEmission(Identifier id, ExteriorEnum exterior) {
        String originalPathNoPng = id.getPath().substring(0, id.getPath().length() - 4);
        String addedEmission = originalPathNoPng + "_emission.png";
        return new Identifier(AITMod.MOD_ID, addedEmission);
    }

    public abstract Animation getAnimationForDoorState(DoorHandler.DoorStateEnum state);
}
