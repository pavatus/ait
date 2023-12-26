package mdteam.ait.client.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;

public class ClientShakeUtil {
    private static final float SHAKE_CLAMP = 45.0f; // Adjust this value to set the maximum shake angle
    private static final float SHAKE_INTENSITY = 0.5f; // Adjust this value to control the intensity of the shake

    public static void shake(float scale) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        float targetPitch = getShakeX(client.player.getPitch(), scale);
        float targetYaw = getShakeY(client.player.getYaw(), scale);

        client.player.setPitch(lerp(client.player.getPitch(), targetPitch, SHAKE_INTENSITY));
        client.player.setYaw(lerp(client.player.getYaw(), targetYaw, SHAKE_INTENSITY));
    }

    private static float getShakeY(float baseYaw, float scale) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return baseYaw;

        float temp = (client.player.getRandom().nextFloat() * scale);
        float shakeYaw = baseYaw + (client.player.getRandom().nextBoolean() ? temp : -temp);

        return MathHelper.clamp(shakeYaw, baseYaw - SHAKE_CLAMP, baseYaw + SHAKE_CLAMP);
    }

    private static float getShakeX(float basePitch, float scale) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return basePitch;

        float temp = (client.player.getRandom().nextFloat() * (scale / 2));
        float shakePitch = basePitch + (client.player.getRandom().nextBoolean() ? temp : -temp);

        return MathHelper.clamp(shakePitch, basePitch - SHAKE_CLAMP, basePitch + SHAKE_CLAMP);
    }

    private static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }
}