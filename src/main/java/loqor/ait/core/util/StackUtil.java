package loqor.ait.core.util;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class StackUtil {

    public static boolean equals(Collection<ItemStack> as, Collection<ItemStack> bs) {
        if (as.size() != bs.size())
            return false;

        for (ItemStack a : as) {
            boolean found = false;

            for (ItemStack b : bs) {
                if (ItemStack.areItemsEqual(a, b)) {
                    found = true;
                    break;
                }
            }

            if (!found)
                return false;
        }

        return true;
    }

    public static <T extends Collection<ItemStack>> T copy(T t, Supplier<T> supplier) {
        T copy = supplier.get();

        for (ItemStack stack : t) {
            copy.add(stack.copy());
        }

        return copy;
    }

    public static void spawn(World world, Position pos, ItemStack stack) {
        world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack));
    }

    public static void spawn(World world, BlockPos pos, ItemStack stack) {
        spawn(world, pos.toCenterPos(), stack);
    }

    public static void playBreak(PlayerEntity player) {
        player.playSound(SoundEvents.ENTITY_ITEM_BREAK, 0.8F, 0.8F + player.getWorld().getRandom().nextFloat() * 0.4F);
    }

    public static void scatter(World world, Position pos, Collection<ItemStack> stacks) {
        for (ItemStack stack : stacks) {
            ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), stack);
        }
    }

    public static void scatter(World world, BlockPos pos, Collection<ItemStack> stacks) {
        scatter(world, pos.toCenterPos(), stacks);
    }

    public static NbtCompound writeUnordered(NbtCompound nbt, Collection<ItemStack> stacks) {
        NbtList nbtList = new NbtList();

        for (ItemStack stack : stacks) {
            if (stack == null || stack.isEmpty())
                continue;

            NbtCompound nbtCompound = new NbtCompound();

            stack.writeNbt(nbtCompound);
            nbtList.add(nbtCompound);
        }

        if (!nbtList.isEmpty())
            nbt.put("Items", nbtList);

        return nbt;
    }

    public static NbtCompound write(NbtCompound nbt, List<ItemStack> stacks) {
        NbtList nbtList = new NbtList();

        for (int i = 0; i < stacks.size(); i++) {
            ItemStack stack = stacks.get(i);

            if (stack == null)
                stack = new ItemStack(Items.AIR);

            if (stack.isEmpty())
                continue;

            NbtCompound nbtCompound = new NbtCompound();
            nbtCompound.putByte("Slot", (byte)i);

            stack.writeNbt(nbtCompound);
            nbtList.add(nbtCompound);
        }

        if (!nbtList.isEmpty())
            nbt.put("Items", nbtList);

        return nbt;
    }

    public static NbtCompound write(NbtCompound nbt, ItemStack... stacks) {
        NbtList nbtList = new NbtList();

        for (int i = 0; i < stacks.length; i++) {
            ItemStack stack = stacks[i];

            if (stack == null)
                stack = new ItemStack(Items.AIR);

            if (stack.isEmpty())
                continue;

            NbtCompound nbtCompound = new NbtCompound();
            nbtCompound.putByte("Slot", (byte) i);

            stack.writeNbt(nbtCompound);
            nbtList.add(nbtCompound);
        }

        if (!nbtList.isEmpty())
            nbt.put("Items", nbtList);

        return nbt;
    }

    public static void read(NbtCompound nbt, List<ItemStack> stacks) {
        NbtList nbtList = nbt.getList("Items", 10);

        for(int i = 0; i < nbtList.size(); i++) {
            NbtCompound nbtCompound = nbtList.getCompound(i);
            int j = nbtCompound.getByte("Slot") & 255;

            if (j < stacks.size()) {
                stacks.set(j, ItemStack.fromNbt(nbtCompound));
            }
        }
    }

    public static ItemStack[] read(NbtCompound nbt) {
        NbtList nbtList = nbt.getList("Items", 10);
        ItemStack[] stacks = new ItemStack[nbtList.size()];

        for(int i = 0; i < nbtList.size(); i++) {
            NbtCompound nbtCompound = nbtList.getCompound(i);
            int j = nbtCompound.getByte("Slot") & 255;

            if (j < stacks.length) {
                stacks[j] = ItemStack.fromNbt(nbtCompound);
            }
        }

        return stacks;
    }

    public static void readUnordered(NbtCompound nbt, Collection<ItemStack> stacks) {
        NbtList nbtList = nbt.getList("Items", 10);

        for(int i = 0; i < nbtList.size(); i++) {
            stacks.add(ItemStack.fromNbt(nbtList.getCompound(i)));
        }
    }
}
