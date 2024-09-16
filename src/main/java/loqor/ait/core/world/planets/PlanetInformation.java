package loqor.ait.core.world.planets;

import net.minecraft.world.World;

// @TODO: use for the new dimensions being added - Loqor
public interface PlanetInformation {
    boolean ait$hasOxygen(World world);
    float ait$getGravity();
}