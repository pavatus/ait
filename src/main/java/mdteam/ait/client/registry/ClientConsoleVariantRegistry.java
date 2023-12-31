package mdteam.ait.client.registry;

import mdteam.ait.AITMod;
import mdteam.ait.client.registry.console.ClientConsoleVariantSchema;
import mdteam.ait.client.registry.console.impl.*;
import mdteam.ait.tardis.variant.console.*;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

public class ClientConsoleVariantRegistry {
    public static final SimpleRegistry<ClientConsoleVariantSchema> REGISTRY = FabricRegistryBuilder.createSimple(RegistryKey.<ClientConsoleVariantSchema>ofRegistry(new Identifier(AITMod.MOD_ID, "client_console_variant"))).buildAndRegister();
    public static ClientConsoleVariantSchema register(ClientConsoleVariantSchema schema) {
        return Registry.register(REGISTRY, schema.id(), schema);
    }

    /**
     * Will return the clients version of a servers console variant
     * @param parent
     * @return the first variant found as there should only be one client version
     */
    public static ClientConsoleVariantSchema withParent(ConsoleVariantSchema parent) {
        for (ClientConsoleVariantSchema schema : REGISTRY) {
            if (schema.parent().equals(parent)) return schema;
        }

        return null;
    }

    public static ClientConsoleVariantSchema BOREALIS;
    public static ClientConsoleVariantSchema AUTUMN;
    public static ClientConsoleVariantSchema HARTNELL;
    public static ClientConsoleVariantSchema HARTNELL_WOOD;
    public static ClientConsoleVariantSchema HARTNELL_KELT;

    public static void init() {
        // Borealis variants
        BOREALIS = register(new ClientBorealisVariant());
        AUTUMN = register(new ClientAutumnVariant());

        // Hartnell variants
        HARTNELL = register(new ClientHartnellVariant());
        HARTNELL_KELT = register(new ClientKeltHartnellVariant());
        HARTNELL_WOOD = register(new ClientWoodenHartnellVariant()); // fixme this texture is awful - make tright remake it
    }
}
