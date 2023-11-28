package mdteam.ait.client.models.exteriors;

import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import the.mdteam.ait.TardisTravel;

import java.awt.geom.Point2D;
import java.util.function.Function;

public abstract class ExteriorModel extends SinglePartEntityModel {
    public static int MAX_TICK_COUNT = 2 * 20;
    private float alpha = 1f;

    private float getAlpha() {
        return alpha;
    }
    private void setAlpha(float var) {
        this.alpha = var;
    }

    public ExteriorModel() {
        this(RenderLayer::getEntityCutoutNoCull);
    }

    public ExteriorModel(Function<Identifier, RenderLayer> function) {
        super(function);
    }

    // Thanks craig for help w animation code @TODO more craig thank yous
    public void animateTile(ExteriorBlockEntity exterior) {
        if (exterior.ANIMATION_STATE.isRunning()) {
            // updateAnimation(exterior.ANIMATION_STATE, exterior.getAnimation(), MinecraftClient.getInstance().player.age);
        }
    }
    public void renderWithAnimations(ExteriorBlockEntity exterior, ModelPart root, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float pAlpha) {
        if (exterior.getTardis() == null) return;

        if (exterior.getTardis().getTravel().getState() == TardisTravel.State.DEMAT) {
            this.setAlpha(this.getAlpha() * ((MAX_TICK_COUNT - exterior.tickCount) / MAX_TICK_COUNT));
        }

        root.render(matrices, vertices, light, overlay,red,green,blue,this.getAlpha());
    }
}
