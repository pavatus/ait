package loqor.ait.core.item.blueprint;

import dev.pavatus.register.datapack.SimpleDatapackRegistry;

import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import loqor.ait.AITMod;
import loqor.ait.datagen.datagen_providers.loot.SetBlueprintLootFunction;


public class BlueprintRegistry extends SimpleDatapackRegistry<BlueprintSchema> {
    public static final LootFunctionType BLUEPRINT_TYPE = Registry.register(Registries.LOOT_FUNCTION_TYPE,
            AITMod.id("set_blueprint"),
            new LootFunctionType(new SetBlueprintLootFunction.Serializer()));

    private static final BlueprintRegistry instance = new BlueprintRegistry();

    public BlueprintRegistry() {
        super(BlueprintSchema::fromInputStream, BlueprintSchema.CODEC, "blueprint", true);
    }

    @Override
    protected void defaults() {
    }

    @Override
    public BlueprintSchema fallback() {
        throw new IllegalStateException("No fallback blueprint found");
    }

    public static BlueprintRegistry getInstance() {
        return instance;
    }
}
