package loqor.ait.core.item.blueprint;

import dev.pavatus.register.datapack.SimpleDatapackRegistry;

import net.minecraft.item.ItemStack;

import loqor.ait.core.AITItems;


public class BlueprintRegistry extends SimpleDatapackRegistry<BlueprintSchema> {
    private static final BlueprintRegistry instance = new BlueprintRegistry();

    public BlueprintRegistry() {
        super(BlueprintSchema::fromInputStream, BlueprintSchema.CODEC, "blueprint", true);
    }

    public static BlueprintSchema DEMAT_CIRCUIT;

    @Override
    protected void defaults() {
        DEMAT_CIRCUIT = register(new BlueprintSchema(new ItemStack(AITItems.DEMATERIALIZATION_CIRCUIT), new BlueprintSchema.InputList(new BlueprintSchema.Input(AITItems.ZEITON_SHARD, 1, 7))));
    }

    @Override
    public BlueprintSchema fallback() {
        return DEMAT_CIRCUIT;
    }

    public static BlueprintRegistry getInstance() {
        return instance;
    }
}
