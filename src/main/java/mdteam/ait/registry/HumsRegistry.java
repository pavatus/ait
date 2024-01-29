package mdteam.ait.registry;

import mdteam.ait.AITMod;
import mdteam.ait.core.AITSounds;
import mdteam.ait.tardis.sound.HumSound;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

public class HumsRegistry {
    public static final SimpleRegistry<HumSound> REGISTRY = FabricRegistryBuilder.createSimple(RegistryKey.<HumSound>ofRegistry(new Identifier(AITMod.MOD_ID, "hum"))).buildAndRegister();
    public static HumSound register(HumSound schema) {
        return Registry.register(REGISTRY, schema.id(), schema);
    }

    public static HumSound TOYOTA;
    public static HumSound CORAL;

    public static void init() {
        TOYOTA = register(HumSound.create(AITMod.MOD_ID, "toyota", AITSounds.TOYOTA_HUM));
        CORAL = register(HumSound.create(AITMod.MOD_ID, "coral", AITSounds.CORAL_HUM));
    }
}
