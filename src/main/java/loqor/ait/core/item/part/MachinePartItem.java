package loqor.ait.core.item.part;

import loqor.ait.core.AITItems;
import net.minecraft.item.Item;

import java.util.function.Supplier;

public class MachinePartItem extends AbstractMachinePartItem<MachinePartItem.Type> {

    public MachinePartItem(Type type, Settings settings) {
        super(type, settings);
    }

    public enum Type {
        CONDENSER(() -> AITItems.CONDENSER),
        MANIPULATOR(() -> AITItems.MANIPULATOR),
        MATTER_BIN(() -> AITItems.MANIPULATOR), // not implemented
        BULB(() -> AITItems.BULB),
        INDUCTOR(() -> AITItems.INDUCTOR);

        private final Supplier<Item> toItem;

        Type(Supplier<Item> toItem) {
            this.toItem = toItem;
        }

        public Item item() {
            return this.toItem.get();
        }
    }
}
