package mdteam.ait.core;

import mdteam.ait.client.renderers.consoles.ConsoleEnum;
import mdteam.ait.tardis.variant.console.*;
import net.minecraft.util.Identifier;

import java.util.*;

// Should be moved to a proper fabric / mc register, but this is okay for now.
public class AITConsoleVariants {
    private static final Map<Identifier, ConsoleVariantSchema> map = new HashMap<>();

    /**
     * Console Variants are registered here:
     */
    public static void init() {
        // Borealis variants
        register(new BorealisVariant());
        register(new AutumnVariant());

        // Hartnell variants
        register(new HartnellVariant());
        register(new WoodenHartnellVariant());

        // "temp" variants
        register(new TempVariant());
    }

    public static void register(ConsoleVariantSchema variant) {
        map.put(variant.id(), variant);
    }

    public static ConsoleVariantSchema get(Identifier id) {
        return map.get(id);
    }

    public static Collection<ConsoleVariantSchema> iterator() {
        return map.values();
    }
    public static Collection<ConsoleVariantSchema> withParent(ConsoleEnum parent) {
        List<ConsoleVariantSchema> list = new ArrayList<>();

        for (ConsoleVariantSchema schema : iterator()) {
            if (schema.parent().equals(parent)) list.add(schema);
        }

        return list;
    }
}
