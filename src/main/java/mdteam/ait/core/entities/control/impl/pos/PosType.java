package mdteam.ait.core.entities.control.impl.pos;

import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;

public enum PosType implements StringIdentifiable {
    X() {
        @Override
        public BlockPos add(BlockPos pos, int amount) {
            return pos.add(amount,0,0);
        }
    },
    Y() {
        @Override
        public BlockPos add(BlockPos pos, int amount) {
            return pos.add(0,amount,0);
        }
    },
    Z() {
        @Override
        public BlockPos add(BlockPos pos, int amount) {
            return pos.add(0,0,amount);
        }
    };

    // adds an amount to a blockpos based on this type
    public abstract BlockPos add(BlockPos pos, int amount);

    @Override
    public String asString() {
        return this.name().toLowerCase();
    }
}
