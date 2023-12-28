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

    public static ConsoleSchema BOREALIS;
    public static ConsoleSchema CORAL;
    public static ConsoleSchema HARTNELL;
    public static ConsoleSchema TEMP;

    public static void init() {
        BOREALIS = register(new BorealisConsole());
        CORAL = register(new CoralConsole());
        HARTNELL = register(new HartnellConsole());
        TEMP = register(new TempConsole());
    }
}
