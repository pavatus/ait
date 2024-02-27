package mdteam.ait.mixin.server;

import mdteam.ait.core.util.gson.GsonNbtCompound;
import mdteam.ait.core.util.gson.NbtMixin;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

// todo this is my worst idea yet. please for christs sake let this mod die so nobody has to deal with this torture.
@Mixin(value = NbtCompound.class, remap = false)
public abstract class NbtCompoundMixin implements NbtMixin {
	@Shadow protected abstract Map<String, NbtElement> toMap();

	@Override
	public GsonNbtCompound toGsonNbt() {
		return new GsonNbtCompound(this.toMap());
	}
}
