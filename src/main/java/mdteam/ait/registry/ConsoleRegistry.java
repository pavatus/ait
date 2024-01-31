package mdteam.ait.registry;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.console.*;
import mdteam.ait.tardis.variant.console.ConsoleVariantSchema;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

public class ConsoleRegistry {
    public static final SimpleRegistry<ConsoleSchema> REGISTRY = FabricRegistryBuilder.createSimple(RegistryKey.<ConsoleSchema>ofRegistry(new Identifier(AITMod.MOD_ID, "console"))).buildAndRegister();
    public static ConsoleSchema register(ConsoleSchema schema) {
        return Registry.register(REGISTRY, schema.id(), schema);
    }

    public static ConsoleSchema CORAL;
    public static ConsoleSchema HARTNELL;
    public static ConsoleSchema COPPER;
    public static ConsoleSchema TOYOTA;
    public static ConsoleSchema ALNICO;
    public static ConsoleSchema STEAM;

    public static void init() {
        HARTNELL = register(new HartnellConsole());
        CORAL = register(new CoralConsole());
        //COPPER = register(new CopperConsole());
        TOYOTA = register(new ToyotaConsole());
        ALNICO = register(new AlnicoConsole());
        STEAM = register(new SteamConsole());
    }
}
