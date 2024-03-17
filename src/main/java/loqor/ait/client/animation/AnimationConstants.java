package loqor.ait.client.animation;

import net.minecraft.client.render.entity.animation.Transformation;
import org.joml.Vector3f;

public class AnimationConstants {

	public static final Transformation.Interpolation STEP = (dest, delta, keyframes, start, end, scale) -> {
		Vector3f vector3f = keyframes[start].target();
		Vector3f vector3f2 = keyframes[end].target();
		return vector3f.lerp(vector3f2, 0, dest).mul(scale);
	};

}
