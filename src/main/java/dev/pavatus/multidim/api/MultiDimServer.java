package dev.pavatus.multidim.api;

import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.level.storage.LevelStorage;

public interface MultiDimServer {
    void multidim$addWorld(ServerWorld world);
    ServerWorld multidim$removeWorld(RegistryKey<World> key);

    LevelStorage.Session multidim$getSession();
}
