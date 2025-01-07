package dev.drtheo.stp.mixin;

import java.io.File;

import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import dev.drtheo.stp.STPMinecraftClient;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ApiServices;
import net.minecraft.util.UserCache;

import loqor.ait.api.ClientWorldEvents;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin implements STPMinecraftClient {

    @Shadow @Nullable public ClientWorld world;

    @Shadow protected abstract void setWorld(@Nullable ClientWorld world);

    @Shadow private boolean integratedServerRunning;

    @Shadow @Final private YggdrasilAuthenticationService authenticationService;

    @Shadow @Final public File runDirectory;

    @Shadow @Final public ParticleManager particleManager;

    @Shadow @Final private BlockEntityRenderDispatcher blockEntityRenderDispatcher;

    @Shadow public abstract void updateWindowTitle();

    @Override
    public void stp$joinWorld(ClientWorld world) {
        this.world = world;

        MinecraftClient client = (MinecraftClient) (Object) this;
        this.particleManager.setWorld(world);
        this.blockEntityRenderDispatcher.setWorld(world);
        this.updateWindowTitle();

        ClientWorldEvents.CHANGE_WORLD.invoker().onChange(client, world);

        if (!this.integratedServerRunning) {
            ApiServices apiServices = ApiServices.create(this.authenticationService, this.runDirectory);
            apiServices.userCache().setExecutor(client);

            SkullBlockEntity.setServices(apiServices, client);
            UserCache.setUseRemote(false);
        }
    }
}
