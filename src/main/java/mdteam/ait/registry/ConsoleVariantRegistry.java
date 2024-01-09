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

    public static Collection<ConsoleVariantSchema> withParent(ConsoleSchema parent) {
        List<ConsoleVariantSchema> list = new ArrayList<>();

        for (ConsoleVariantSchema schema : REGISTRY) {
            if (schema.parent().equals(parent)) list.add(schema);
        }

        return list;
    }

    //public static ConsoleVariantSchema BOREALIS;
    //public static ConsoleVariantSchema AUTUMN;
    public static ConsoleVariantSchema HARTNELL;
    public static ConsoleVariantSchema HARTNELL_WOOD;
    public static ConsoleVariantSchema HARTNELL_KELT;
    public static ConsoleVariantSchema CORAL;
    public static ConsoleVariantSchema CORAL_BLUE;
    public static ConsoleVariantSchema CORAL_WHITE;
    public static ConsoleVariantSchema TEMP; // @TODO implement the new hudolin when its made again

    public static void init() {

        // Hartnell variants
        HARTNELL = register(new HartnellVariant());
        HARTNELL_KELT = register(new KeltHartnellVariant());
        HARTNELL_WOOD = register(new WoodenHartnellVariant()); // fixme this texture is awful - make tright remake it

        // Borealis variants
        //BOREALIS = register(new BorealisVariant());
        //AUTUMN = register(new AutumnVariant());

        // Coral variants
        CORAL = register(new CoralVariant());
        CORAL_BLUE = register(new BlueCoralVariant());
        CORAL_WHITE = register(new WhiteCoralVariant());

        // "Temp" variants
        //TEMP = register(new TempVariant());
    }
}
