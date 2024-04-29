package loqor.ait.core.item.part.link;

import loqor.ait.core.item.part.AbstractMachinePartItem;

public abstract class AbstractFluidLinkItem<T extends Enum<T> & AbstractFluidLinkItem.FluidLinkType<?>> extends AbstractMachinePartItem<T> {

    public AbstractFluidLinkItem(T type, Settings settings) {
        super(type, settings);
    }

    public interface FluidLinkType<T extends FluidLinkType<T>> {
        float unitsPerTick();

        T artron();
        T data();
        T vortex();
    }
}
