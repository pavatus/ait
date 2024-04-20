package loqor.ait.core.item.part.link;

public class FluidLinkItem extends AbstractFluidLinkItem<FluidLinkItem.Type> {

    public FluidLinkItem(Type type, Settings settings) {
        super(type, settings);
    }

    public enum Type implements FluidLinkType<Type> {
        DATA(1.0f),
        ARTRON(1.5f),
        VORTEX(2.0f);

        private final float unitsPerTick;

        Type(float unitsPerTick) {
            this.unitsPerTick = unitsPerTick;
        }

        @Override
        public float unitsPerTick() {
            return this.unitsPerTick;
        }

        @Override
        public Type artron() {
            return ARTRON;
        }

        @Override
        public Type data() {
            return DATA;
        }

        @Override
        public Type vortex() {
            return VORTEX;
        }
    }
}
