package mdteam.ait.client.util;

import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisTravel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;

public class ClientShakeUtil {
    private static final float SHAKE_CLAMP = 45.0f; // Adjust this value to set the maximum shake angle
    private static final float SHAKE_INTENSITY = 0.5f; // Adjust this value to control the intensity of the shake
    private static final int MAX_DISTANCE = 5; // The radius from the console where the player will feel the shake

    public static boolean shouldShake(Tardis tardis) {
        return ClientTardisUtil.getCurrentTardis().equals(tardis)
                && tardis.getTravel() != null
                && tardis.getTravel().getState() != TardisTravel.State.LANDED
                && ClientTardisUtil.distanceFromConsole() < MAX_DISTANCE;
    }

    /**
     * Shakes based off the distance of the gpu
     */
    public static void shakeFromConsole() {
        shake(1f - (float) (ClientTardisUtil.distanceFromConsole() / MAX_DISTANCE));
    }

    public static void shake(float scale) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        float targetPitch = getShakeX(client.player.getPitch(), scale);
        float targetYaw = getShakeY(client.player.getYaw(), scale);

        client.player.setPitch(MathHelper.lerp(SHAKE_INTENSITY,client.player.getPitch(), targetPitch));
        client.player.setYaw(MathHelper.lerp(SHAKE_INTENSITY,client.player.getYaw(), targetYaw));
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
}