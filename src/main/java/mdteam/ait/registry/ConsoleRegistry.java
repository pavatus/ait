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
    public static ConsoleSchema CORAL; // @TODO implement the new coral when its made again
    public static ConsoleSchema HARTNELL;
    public static ConsoleSchema TEMP; // @TODO implement the new hudolin when its made again

    public static void init() {
        HARTNELL = register(new HartnellConsole());
        BOREALIS = register(new BorealisConsole());
        CORAL = register(new CoralConsole());
        //TEMP = register(new TempConsole());
    }
}
