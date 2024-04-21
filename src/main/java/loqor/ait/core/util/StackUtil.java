package loqor.ait.core.util;

import net.minecraft.item.ItemStack;

import java.util.Set;

public class StackUtil {

    public static boolean equals(Set<ItemStack> as, Set<ItemStack> bs) {
        for (ItemStack a : as) {
            boolean found = false;
            for (ItemStack b : bs) {
                if (ItemStack.areItemsEqual(a, b)) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                return false;
            }
        }

        return true;
    }
}
