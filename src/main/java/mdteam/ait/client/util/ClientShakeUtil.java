package mdteam.ait.client.util;

import net.minecraft.client.MinecraftClient;

public class ClientShakeUtil {
    public static void shake(double scale) {
        if (MinecraftClient.getInstance().player == null) return;

        MinecraftClient.getInstance().player.setPitch(getShakeX(scale));
        MinecraftClient.getInstance().player.setYaw(getShakeY(scale));
    }

    /**
     * gets the current yaw and adds an amount of a random from 0 - 1 timesed by the scale
     * @param scale the intensity of the shake
     * @return the new yaw
     */
    private static float getShakeY(double scale) {
        if (MinecraftClient.getInstance().player == null) return 0;

        var temp = (MinecraftClient.getInstance().player.getRandom().nextFloat() * (float) scale);

        // this is a long line but i have a vendetta against creating variable for some reason
        return MinecraftClient.getInstance().player.getYaw() + (MinecraftClient.getInstance().player.getRandom().nextBoolean() ? temp : -temp);
    }

    /**
     * gets the current pitch and adds an amount of a random from 0 - 1 timesed by the scale
     * @param scale the intensity of the shake
     * @return the new pitch
     */
    private static float getShakeX(double scale) {
        if (MinecraftClient.getInstance().player == null) return 0;

        var temp = (MinecraftClient.getInstance().player.getRandom().nextFloat() * (float) scale);

        // yeah
        return MinecraftClient.getInstance().player.getPitch() + (MinecraftClient.getInstance().player.getRandom().nextBoolean() ? temp : -temp);
    }
}
