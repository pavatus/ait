package mdteam.ait.client.models.doors;

import mdteam.ait.AITMod;
import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.client.renderers.exteriors.VariantEnum;
import mdteam.ait.core.blockentities.DoorBlockEntity;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

import java.util.function.Function;

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

        root.render(matrices, vertices, light, overlay,red,green,blue,pAlpha);
    }

    @Override
    public void setAngles(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

    }

    public Identifier getVariousTextures(ExteriorEnum exterior, VariantEnum variant) {
        /*new Identifier(AITMod.MOD_ID, TEXTURE_PATH + exterior.toString().toLowerCase() + "/" + exterior.toString().toLowerCase() + ".png");
        new Identifier(AITMod.MOD_ID, TEXTURE_PATH + exterior.toString().toLowerCase() + "/" + exterior.toString().toLowerCase() + "_emission" + ".png");*/
        Identifier texture = new Identifier(AITMod.MOD_ID, TEXTURE_PATH + exterior.toString().toLowerCase() + "/" + exterior.toString().toLowerCase() + "_" + variant.toString().toLowerCase() + ".png");
        Identifier capsule = new Identifier(AITMod.MOD_ID, "textures/blockentities/doors/capsule_door.png");
        Identifier shelter = new Identifier(AITMod.MOD_ID, "textures/blockentities/doors/shelter_door.png");
        if(exterior == ExteriorEnum.CAPSULE) {
            return capsule;
        } else if (exterior == ExteriorEnum.SHELTER) {
            return shelter;
        }
        return texture;
    }

    public Identifier getVariousEmission(Identifier id, ExteriorEnum exterior) {
        String originalPathNoPng = id.getPath().substring(0, id.getPath().length() - 4);
        String addedEmission = originalPathNoPng + "_emission.png";
        Identifier emission = new Identifier(AITMod.MOD_ID, addedEmission);
        Identifier shelter = new Identifier(AITMod.MOD_ID, "textures/blockentities/doors/shelter_door_emission.png");
        if(exterior == ExteriorEnum.SHELTER) {
            return shelter;
        } else if (exterior == ExteriorEnum.CAPSULE) {
            return null;
        }
        return emission;
    }
}
