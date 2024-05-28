package loqor.ait.client.models.consoles;

import loqor.ait.core.blockentities.ConsoleBlockEntity;
import loqor.ait.tardis.TardisTravel;
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
public abstract class ConsoleModel extends SinglePartEntityModel {
	public static int MAX_TICK_COUNT = 2 * 20;

	public ConsoleModel() {
		this(RenderLayer::getEntityCutoutNoCull);
	}

	public ConsoleModel(Function<Identifier, RenderLayer> function) {
		super(function);
	}

	public void animateBlockEntity(ConsoleBlockEntity console) {
		// fyi, this is directly referencing camel animation code, its just specific according to the block entity that is being used
		// to detect different states. - Loqor
		this.getPart().traverse().forEach(ModelPart::resetTransform);
		if (console.findTardis().isEmpty())
			return;

		TardisTravel.State state = console.findTardis().get().travel().getState();

		this.updateAnimation(console.ANIM_STATE, getAnimationForState(state), console.age);
	}

	public void renderWithAnimations(ConsoleBlockEntity console, ModelPart root, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float pAlpha) {
		root.render(matrices, vertices, light, overlay, red, green, blue, pAlpha);
	}

	@Override
	public void setAngles(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
	}

	public abstract Animation getAnimationForState(TardisTravel.State state);
}
