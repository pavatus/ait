package dev.pavatus.multidim.mixin;

import java.util.Map;

import dev.pavatus.multidim.api.MultiDimServer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.level.storage.LevelStorage;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin implements MultiDimServer {

    @Shadow @Final private Map<RegistryKey<World>, ServerWorld> worlds;

    @Shadow @Final public LevelStorage.Session session;

    @Override
    public void multidim$addWorld(ServerWorld world) {
        this.worlds.put(world.getRegistryKey(), world);
    }

    @Override
    public ServerWorld multidim$removeWorld(RegistryKey<World> key) {
        return this.worlds.remove(key);
    }

    @Override
    public LevelStorage.Session multidim$getSession() {
        return this.session;
    }
}
