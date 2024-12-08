package dev.pavatus.multidim.api;

import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.level.storage.LevelStorage;

public interface MultiDimServer {
    void multidim$addWorld(ServerWorld world);
    boolean multidim$hasWorld(RegistryKey<World> key);
    ServerWorld multidim$removeWorld(RegistryKey<World> key);

    LevelStorage.Session multidim$getSession();
}
