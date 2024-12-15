package loqor.ait.core.item.blueprint;

import dev.pavatus.register.datapack.SimpleDatapackRegistry;




public class BlueprintRegistry extends SimpleDatapackRegistry<BlueprintSchema> {
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
