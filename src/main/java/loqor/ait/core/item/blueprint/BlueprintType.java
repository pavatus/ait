package loqor.ait.core.item.blueprint;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public record BlueprintType(Identifier id, Text text) {

    private BlueprintType(Identifier id) {
        this(id, Text.translatable("tooltip.ait.blueprint." + id.getNamespace() + "." + id.getPath()));
    }

    public BlueprintType(Item item) {
        this(Registries.ITEM.getId(item));
    }
}
