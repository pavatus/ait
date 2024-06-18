package loqor.ait.client.models.exteriors;

import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.core.entities.FallingTardisEntity;
import loqor.ait.core.entities.TardisRealEntity;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.DoorData;
import loqor.ait.tardis.data.loyalty.Loyalty;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.util.function.Function;

import static loqor.ait.tardis.animation.ExteriorAnimation.*;

@SuppressWarnings("rawtypes")
public abstract class ExteriorModel extends SinglePartEntityModel {

	public ExteriorModel() {
		this(RenderLayer::getEntityCutoutNoCull);
	}

	public ExteriorModel(Function<Identifier, RenderLayer> function) {
		super(function);
	}

	private static float getAnimationLengthInTicks(Animation anim) {
		return anim.lengthInSeconds() * 20;
	}

	private void checkAnimationTimer(ExteriorBlockEntity exterior) {
		DoorData.DoorStateEnum state = exterior.tardis().get().getDoor().getDoorState();
		Animation anim = getAnimationForDoorState(state);

		int max = (int) getAnimationLengthInTicks(anim);

		if (exterior.animationTimer > max) {
			exterior.animationTimer = max;
		}
	}

	public void renderWithAnimations(ExteriorBlockEntity exterior, ModelPart root, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		if (exterior.tardis().isEmpty())
			return;

		PlayerEntity player = MinecraftClient.getInstance().player;
		Tardis tardis = exterior.tardis().get();
		alpha = ExteriorModel.getAlpha(exterior, tardis, player);

		root.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}

	protected static float getAlpha(ExteriorBlockEntity exterior, Tardis tardis, PlayerEntity player) {
		if (!tardis.getHandlers().getCloak().isEnabled())
			return exterior.getAlpha();

		if (!tardis.loyalty().get(player).isOf(Loyalty.Type.COMPANION))
			return 0f;

		double distance = distanceFromTardis(player, tardis);

		if (!isNearTardis(player, tardis, MAX_CLOAK_DISTANCE, distance))
			return 0f;

		float alpha = 1f - (float) (distance / MAX_CLOAK_DISTANCE);

		if (exterior.getAlpha() == 0.105f)
			return alpha;

		return alpha * exterior.getAlpha();
	}

	public void renderFalling(FallingTardisEntity falling, ModelPart root, MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		root.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}

	public void renderRealWorld(TardisRealEntity realEntity, ModelPart root, MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		if (realEntity.getTardis() == null) return;

		root.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}

	@Override
	public void setAngles(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) { }

	public abstract Animation getAnimationForDoorState(DoorData.DoorStateEnum state);
}
