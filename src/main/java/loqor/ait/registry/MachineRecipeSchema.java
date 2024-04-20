package loqor.ait.registry;

import com.google.gson.*;
import loqor.ait.registry.datapack.Identifiable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.lang.reflect.Type;
import java.util.Set;

public class MachineRecipeSchema implements Identifiable {

    private final Identifier id;
    private final ItemStack output;
    private final Set<ItemStack> input;

    public MachineRecipeSchema(Identifier id, ItemStack output, Set<ItemStack> input) {
        this.id = id;
        this.output = output;
        this.input = input;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() == null) return false;

        MachineRecipeSchema that = (MachineRecipeSchema) o;

        return id.equals(that.id);
    }

    public Identifier id() {
        return id;
    }

    public ItemStack output() {
        return output;
    }

    public Set<ItemStack> input() {
        return this.input;
    }

    public static Object serializer() {
        return new Serializer();
    }

    private static class Serializer implements JsonSerializer<MachineRecipeSchema>, JsonDeserializer<MachineRecipeSchema> {

        @Override
        public MachineRecipeSchema deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return MachineRecipeRegistry.getInstance().get(new Identifier(json.getAsJsonPrimitive().getAsString()));
        }

        @Override
        public JsonElement serialize(MachineRecipeSchema src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.id().toString());
        }
    }
}
