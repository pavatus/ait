package loqor.ait.core.item.blueprint;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;

public class Blueprint {
    private final BlueprintSchema source;
    private final List<ItemStack> requirements;

    public Blueprint(BlueprintSchema source) {
        this.source = source;

        this.requirements = source.inputs().toStacks();
    }
    public Blueprint(NbtCompound nbt) {
        this(BlueprintRegistry.getInstance().get(new Identifier(nbt.getString("id"))));

        this.requirements.clear();
        this.fromNbt(nbt);
    }

    /**
     * attempts to resolve a requirement from the list of requirements by removing it if this stack is a valid requirement
     * @param stack the stack to resolve
     * @return true if the stack was a valid requirement and was removed, false otherwise
     */
    public boolean tryAdd(ItemStack stack) {
        boolean success = false;

        for (ItemStack requirement : requirements) {
            if (ItemStack.areEqual(requirement, stack)) {
                success = true; // to avoid concurrent modification
                break;
            }
        }

        if (success) {
            requirements.remove(stack);
        }

        return success;
    }

    public boolean isComplete() {
        return requirements.isEmpty();
    }
    public ItemStack getOutput() {
        return source.output();
    }

    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();
        nbt.putString("id", source.id().toString());

        NbtList list = new NbtList();
        for (ItemStack stack : requirements) {
            list.add(stack.writeNbt(new NbtCompound()));
        }
        nbt.put("requirements", list);

        return nbt;
    }
    protected NbtCompound fromNbt(NbtCompound nbt) {
        NbtList list = nbt.getList("requirements", 10);
        for (int i = 0; i < list.size(); i++) {
            requirements.add(ItemStack.fromNbt(list.getCompound(i)));
        }

        return nbt;
    }
}
