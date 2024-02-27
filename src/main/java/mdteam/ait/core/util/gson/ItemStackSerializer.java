package mdteam.ait.core.util.gson;

import com.google.gson.*;
import mdteam.ait.core.util.gson.GsonNbtCompound;
import mdteam.ait.core.util.gson.NbtMixin;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

import java.lang.reflect.Type;
import java.util.Map;

public class ItemStackSerializer implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {


	@Override
	public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		Map<String, NbtElement> map = context.deserialize(json, Map.class);
		System.out.println(map);
		NbtCompound nbt = new GsonNbtCompound(map);
		System.out.println(nbt);
		return ItemStack.fromNbt(nbt);
	}

	@Override
	public JsonElement serialize(ItemStack src, Type typeOfSrc, JsonSerializationContext context) {
		NbtCompound nbt = src.writeNbt(new NbtCompound());
		return context.serialize(((NbtMixin) nbt).toGsonNbt().toMap());
	}
}
