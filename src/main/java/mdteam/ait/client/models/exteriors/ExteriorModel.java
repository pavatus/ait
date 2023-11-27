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

    // Function to compute a point on a quadratic Bezier curve
    private static Point2D.Double quadraticBezier(Point2D.Double p0, Point2D.Double p1, Point2D.Double p2, double t) {
        double x = (1 - t) * (1 - t) * p0.x + 2 * (1 - t) * t * p1.x + t * t * p2.x;
        double y = (1 - t) * (1 - t) * p0.y + 2 * (1 - t) * t * p1.y + t * t * p2.y;
        return new Point2D.Double(x, y);
    }

    // Function to update a value based on the y position in a quadratic Bezier curve
    private static double updateValue(double currentValue, int tickCount) {
        // Define control points of the quadratic Bezier curve
        Point2D.Double p0 = new Point2D.Double(0, 0);
        Point2D.Double p1 = new Point2D.Double(0.5, 1); // Adjust control point coordinates as needed
        Point2D.Double p2 = new Point2D.Double(1, 0);

        // Compute y position on the Bezier curve based on tickCount
        double t = (double) tickCount / MAX_TICK_COUNT;
        Point2D.Double pointOnCurve = quadraticBezier(p0, p1, p2, t);
        double newY = pointOnCurve.getY();

        // Update the value based on the y position
        double updatedValue = currentValue + newY;

        return updatedValue;
    }

    // Thanks craig for help w animation code @TODO more craig thank yous
    public void animateTile(ExteriorBlockEntity exterior) {
        if (exterior.ANIMATION_STATE.isRunning()) {
            // updateAnimation(exterior.ANIMATION_STATE, exterior.getAnimation(), MinecraftClient.getInstance().player.age);
        }
    }
    public void renderWithAnimations(ExteriorBlockEntity exterior, ModelPart root, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float pAlpha) {
        if (exterior.getTardis() == null) return;

        root.render(matrices, vertices, light, overlay,red,green,blue,this.getAlpha());
    }
}
