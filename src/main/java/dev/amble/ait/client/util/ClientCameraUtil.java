package dev.amble.ait.client.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

public class ClientCameraUtil {
    public static boolean isBehindCamera(BlockPos pos) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) return false; // Safety check

        // Get the camera position (player's eye position)
        Vec3d cameraPos = client.player.getCameraPosVec(1.0F); // 1.0F is full tick delta

        // Get the camera's look direction
        Vec3d lookVec = client.player.getRotationVec(1.0F); // Normalized vector of where player is looking

        // Convert BlockPos to Vec3d (center of the block)
        Vec3d blockVec = new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);

        // Vector from camera to BlockPos
        Vec3d relativeVec = blockVec.subtract(cameraPos);

        // Dot product: positive = in front, negative = behind
        double dotProduct = relativeVec.dotProduct(lookVec);

        return dotProduct < 0; // Behind if dot product is negative
    }

    public static boolean isInVisibleArea(BlockPos pos) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null)
            return false;


        Vec3d eyePos = client.player.getEyePos();

        // Calculate target position (center of the block)
        Vec3d targetVec = new Vec3d(
                pos.getX() + 0.5,
                pos.getY() + 0.5,
                pos.getZ() + 0.5
        );

        // Create raycast context
        RaycastContext context = new RaycastContext(
                eyePos,
                targetVec,
                RaycastContext.ShapeType.COLLIDER,
                RaycastContext.FluidHandling.NONE,
                client.player
        );

        // Perform the raycast
        BlockHitResult result = client.world.raycast(context);

        // Check if the raycast hit our target block or nothing (meaning target is visible)
        if (result.getType() == HitResult.Type.MISS) {
            return true; // Nothing blocking the view
        }

        if (result.getType() == HitResult.Type.BLOCK) {
            // Check if the hit position matches our target
            BlockPos hitPos = result.getBlockPos();
            return hitPos.equals(pos);
        }

        return false;
    }
}
