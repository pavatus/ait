package mdteam.ait.client.util;

import net.minecraft.client.MinecraftClient;

public class ClientShakeUtil {
    public static void shake(float scale) {
        if (MinecraftClient.getInstance().player == null) return;

        MinecraftClient.getInstance().player.setPitch(getShakeX(scale));
        MinecraftClient.getInstance().player.setYaw(getShakeY(scale));
    }

    /**
     * gets the current yaw and adds an amount of a random from 0 to 1 multiplied by the scale
     * @param scale the intensity of the shake
     * @return the new yaw
     */
    private static float getShakeY(float scale) {
        if (MinecraftClient.getInstance().player == null) return 0;

        var temp = (MinecraftClient.getInstance().player.getRandom().nextFloat() * scale);

        // this is a long line but i have a vendetta against creating variable for some reason
        return MinecraftClient.getInstance().player.getYaw() + (MinecraftClient.getInstance().player.getRandom().nextBoolean() ? temp : -temp);
    }

    /**
     * gets the current pitch and adds an amount of a random from 0 to 1 multiplied by the scale
     * @param scale the intensity of the shake
     * @return the new pitch
     */
    private static float getShakeX(float scale) {
        if (MinecraftClient.getInstance().player == null) return 0;

        var temp = (MinecraftClient.getInstance().player.getRandom().nextFloat() * scale);

        // yeah
        return MinecraftClient.getInstance().player.getPitch() + (MinecraftClient.getInstance().player.getRandom().nextBoolean() ? temp : -temp);
    }
}
