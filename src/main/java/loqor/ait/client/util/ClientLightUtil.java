package loqor.ait.client.util;

import net.minecraft.util.math.MathHelper;

public class ClientLightUtil {
	public static float getBrightnessForInterior(int lightLevel) {
		float f = (float) lightLevel / 15.0f;
		float g = f / (4.0f - 3.0f * f);
		return MathHelper.lerp(0f /* this number here adjusts the brightness todo stuff with it */, g, 1.0f);
	}
}
