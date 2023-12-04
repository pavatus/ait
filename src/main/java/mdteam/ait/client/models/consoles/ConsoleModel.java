package mdteam.ait.client.models.consoles;

import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import mdteam.ait.tardis.TardisTravel;

import java.util.function.Function;

public abstract class ConsoleModel extends SinglePartEntityModel {
    public static int MAX_TICK_COUNT = 2 * 20;

    public ConsoleModel() {
        this(RenderLayer::getEntityCutoutNoCull);
    }

    public ConsoleModel(Function<Identifier, RenderLayer> function) {
        super(function);
    }

    // Thanks craig for help w animation code
    public void animateTile(ConsoleBlockEntity console) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        if (console.getTardis() == null)
            return;
        // System.out.println(getAnimationForState(console.getTardis().getTravel().getState()));

        TardisTravel.State state = console.getTardis().getTravel().getState();
        this.updateAnimation(console.ANIM_FLIGHT, getAnimationForState(state), console.animationTimer);
    }
    public void renderWithAnimations(ConsoleBlockEntity console, ModelPart root, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float pAlpha) {
        if (console.getTardis() == null) return;
        root.render(matrices, vertices, light, overlay,red,green,blue,pAlpha);
    }

    @Override
    public void setAngles(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
    }

    public abstract Identifier getTexture();
    public abstract Identifier getEmission();
    public abstract Animation getAnimationForState(TardisTravel.State state);
}
