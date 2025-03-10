package dev.amble.ait.registry.v2;

import com.mojang.serialization.Codec;
import dev.amble.ait.AITMod;
import dev.amble.lib.registry.SimpleAmbleRegistry;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.registry.Registry;

public class AITRegistries {

    public static ExteriorCategoryRegistry EXTERIOR_CATEGORY = new ExteriorCategoryRegistry();
    public static ExteriorVariantRegistry EXTERIOR_VARIANT = new ExteriorVariantRegistry();
    public static SimpleAmbleRegistry<String> SIMPLE = new SimpleAmbleRegistry<>(AITMod.id("simple"));

    public static void init() {
        Registry.register(SIMPLE.get(), AITMod.id("item1"), "item 1 value");
        Registry.register(SIMPLE.get(), AITMod.id("item2"), "item 2 value");
    }
}
