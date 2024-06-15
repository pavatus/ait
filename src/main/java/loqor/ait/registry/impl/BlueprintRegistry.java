package loqor.ait.registry.impl;

import loqor.ait.AITMod;
import loqor.ait.core.AITItems;
import loqor.ait.core.item.blueprint.BlueprintType;
import loqor.ait.datagen.datagen_providers.loot.SetBlueprintLootFunction;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

import java.util.Random;

public class BlueprintRegistry {

    public static final LootFunctionType BLUEPRINT_TYPE = Registry.register(Registries.LOOT_FUNCTION_TYPE,
            new Identifier(AITMod.MOD_ID, "set_blueprint"),
            new LootFunctionType(new SetBlueprintLootFunction.Serializer()));

    public static final SimpleRegistry<BlueprintType> REGISTRY = FabricRegistryBuilder.createSimple(
            RegistryKey.<BlueprintType>ofRegistry(new Identifier(AITMod.MOD_ID, "blueprint_type"))
    ).buildAndRegister();

    private static final Random random = AITMod.RANDOM;

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
        DEMATERIALIZATION_CIRCUIT = register(new BlueprintType(AITItems.DEMATERIALIZATION_CIRCUIT));

        ARTRON_FLUID_LINK = register(new BlueprintType(AITItems.ARTRON_FLUID_LINK));
        DATA_FLUID_LINK = register(new BlueprintType(AITItems.DATA_FLUID_LINK));
        VORTEX_FLUID_LINK = register(new BlueprintType(AITItems.VORTEX_FLUID_LINK));

        ARTRON_MERCURIAL_LINK = register(new BlueprintType(AITItems.ARTRON_MERCURIAL_LINK));
        DATA_MERCURIAL_LINK = register(new BlueprintType(AITItems.DATA_MERCURIAL_LINK));
        VORTEX_MERCURIAL_LINK = register(new BlueprintType(AITItems.VORTEX_MERCURIAL_LINK));
    }

    public static BlueprintType getRandomEntry() {
        return REGISTRY.get(random.nextInt(REGISTRY.size()));
    }

    public static ItemStack setBlueprint(ItemStack stack, BlueprintType blueprint) {
        stack.getOrCreateNbt().putString("id", blueprint.id().toString());
        return stack;
    }
}
