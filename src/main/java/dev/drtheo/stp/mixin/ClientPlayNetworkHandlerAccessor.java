package dev.drtheo.stp.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.network.ClientDynamicRegistryType;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.CombinedDynamicRegistries;

@Mixin(ClientPlayNetworkHandler.class)
public interface ClientPlayNetworkHandlerAccessor {

    @Accessor
    CombinedDynamicRegistries<ClientDynamicRegistryType> getCombinedDynamicRegistries();

    @Accessor
    void setWorld(ClientWorld world);

    @Accessor
    int getChunkLoadDistance();

    @Accessor
    int getSimulationDistance();

    @Accessor
    ClientWorld getWorld();

    @Accessor
    void setWorldProperties(ClientWorld.Properties properties);

    @Accessor
    ClientWorld.Properties getWorldProperties();
}
