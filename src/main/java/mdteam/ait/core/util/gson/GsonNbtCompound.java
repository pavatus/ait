package mdteam.ait.core.util.gson;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

import java.util.Map;

public class GsonNbtCompound extends NbtCompound {
	public GsonNbtCompound(Map<String, NbtElement> entries) {
		super(entries);
	}
	@Override
	public Map<String, NbtElement> toMap() {
		return super.toMap();
	}
}
