package dev.drtheo.stp.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.map.MapState;

@Mixin(ClientWorld.class)
public interface ClientWorldInvoker {

    @Invoker("getMapStates")
    Map<String, MapState> stp$getMapStates();

    @Invoker("putMapStates")
    void stp$putMapStates(Map<String, MapState> map);
}
