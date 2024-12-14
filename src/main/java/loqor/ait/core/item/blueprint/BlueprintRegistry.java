package loqor.ait.core.item.blueprint;

import dev.pavatus.register.datapack.SimpleDatapackRegistry;

import net.minecraft.item.ItemStack;

import loqor.ait.core.AITItems;
import net.minecraft.item.Items;


public class BlueprintRegistry extends SimpleDatapackRegistry<BlueprintSchema> {
    private static final BlueprintRegistry instance = new BlueprintRegistry();

    public BlueprintRegistry() {
        super(BlueprintSchema::fromInputStream, BlueprintSchema.CODEC, "blueprint", true);
    }

    public static BlueprintSchema DEMAT_CIRCUIT;

    @Override
    protected void defaults() {
        DEMAT_CIRCUIT = register(new BlueprintSchema(AITItems.DEMATERIALIZATION_CIRCUIT.getDefaultStack(), new BlueprintSchema.InputList(new BlueprintSchema.Input(AITItems.ZEITON_SHARD, 1, 7), new BlueprintSchema.Input(Items.REDSTONE, 1, 3))));
    }

    @Override
    public BlueprintSchema fallback() {
        return DEMAT_CIRCUIT;
    }

    public static BlueprintRegistry getInstance() {
        return instance;
    }
}
