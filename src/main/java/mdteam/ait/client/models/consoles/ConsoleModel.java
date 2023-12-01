package mdteam.ait.client.models.consoles;

import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.render.entity.model.SinglePartEntityModelWithChildTransform;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

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
    // @TODO animation stuff for ze console
    public void animateTile(ConsoleBlockEntity console) {
        if (console.ANIMATION_STATE.isRunning()) {
            // updateAnimation(console.ANIMATION_STATE, console.getAnimation(), MinecraftClient.getInstance().player.age);
        }
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
}
