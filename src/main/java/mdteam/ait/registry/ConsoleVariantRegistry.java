package mdteam.ait.registry;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.console.ConsoleSchema;
import mdteam.ait.tardis.variant.console.*;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConsoleVariantRegistry {
    public static final SimpleRegistry<ConsoleVariantSchema> REGISTRY = FabricRegistryBuilder.createSimple(RegistryKey.<ConsoleVariantSchema>ofRegistry(new Identifier(AITMod.MOD_ID, "console_variant"))).buildAndRegister();
    public static ConsoleVariantSchema register(ConsoleVariantSchema schema) {
        return Registry.register(REGISTRY, schema.id(), schema);
    }

    public static List<ConsoleVariantSchema> withParent(ConsoleSchema parent) {
        List<ConsoleVariantSchema> list = new ArrayList<>();

        for (ConsoleVariantSchema schema : REGISTRY) {
            if (schema.parent().equals(parent)) list.add(schema);
        }

        return list;
    }

    public static ConsoleVariantSchema HARTNELL;
    public static ConsoleVariantSchema HARTNELL_WOOD;
    public static ConsoleVariantSchema HARTNELL_KELT;
    public static ConsoleVariantSchema CORAL;
    public static ConsoleVariantSchema CORAL_BLUE;
    public static ConsoleVariantSchema CORAL_WHITE;
    public static ConsoleVariantSchema COPPER;
    public static ConsoleVariantSchema TOYOTA;
    public static ConsoleVariantSchema TOYOTA_BLUE;
    public static ConsoleVariantSchema TOYOTA_LEGACY;
    public static ConsoleVariantSchema ALNICO;
    public static ConsoleVariantSchema STEAM;

    public static void init() {

        // Hartnell variants
        HARTNELL = register(new HartnellVariant());
        HARTNELL_KELT = register(new KeltHartnellVariant());
        HARTNELL_WOOD = register(new WoodenHartnellVariant()); // fixme this texture is awful - make tright remake it

        // Coral variants
        CORAL = register(new CoralVariant());
        CORAL_BLUE = register(new BlueCoralVariant());
        CORAL_WHITE = register(new WhiteCoralVariant());

        // Copper variants
        COPPER = register(new CopperVariant());

        // Toyota variants
        TOYOTA = register(new ToyotaVariant());
        TOYOTA_BLUE = register(new ToyotaBlueVariant());
        TOYOTA_LEGACY = register(new ToyotaLegacyVariant());

        // Alnico variants
        ALNICO = register(new AlnicoVariant());

        // Steam variants
        STEAM = register(new SteamVariant());
    }
}
