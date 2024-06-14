package loqor.ait.core.item.blueprint;

import loqor.ait.AITMod;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;

import static loqor.ait.core.item.blueprint.BlueprintItem.BLUEPRINT_TYPE;

public class BlueprintRegistry {

    public static final SimpleRegistry<BlueprintType> REGISTRY = FabricRegistryBuilder.createSimple(RegistryKey.<BlueprintType>ofRegistry(new Identifier(AITMod.MOD_ID, "blueprint_type"))).buildAndRegister();
    private static final Random random = (Random) AITMod.RANDOM;

    public static BlueprintType register(BlueprintType schema) {
        return Registry.register(REGISTRY, schema.id(), schema);
    }

    public static BlueprintType DEMATERIALIZATION_CIRCUIT;
    public static BlueprintType ARTRON_FLUID_LINK;
    public static BlueprintType DATA_FLUID_LINK;
    public static BlueprintType VORTEX_FLUID_LINK;
    public static BlueprintType ARTRON_MERCURIAL_LINK;
    public static BlueprintType DATA_MERCURIAL_LINK;
    public static BlueprintType VORTEX_MERCURIAL_LINK;

    public static void init() {
        DEMATERIALIZATION_CIRCUIT = register(new BlueprintType("dematerialization_circuit"));
        ARTRON_FLUID_LINK = register(new BlueprintType("artron_fluid_link"));
        DATA_FLUID_LINK = register(new BlueprintType("data_fluid_link"));
        VORTEX_FLUID_LINK = register(new BlueprintType("vortex_fluid_link"));
        ARTRON_MERCURIAL_LINK = register(new BlueprintType("artron_mercurial_link"));
        DATA_MERCURIAL_LINK = register(new BlueprintType("data_mercurial_link"));
        VORTEX_MERCURIAL_LINK = register(new BlueprintType("vortex_mercurial_link"));
    }

    public static BlueprintType getRandomEntry() {
        BlueprintType type = REGISTRY.get(random.nextBetween(0, REGISTRY.size()));
        return type;
    }

    public static ItemStack setBlueprint(ItemStack stack, BlueprintType blueprint) {
        stack.getOrCreateNbt().putString(BLUEPRINT_TYPE, blueprint.id());
        return stack;
    }
}
