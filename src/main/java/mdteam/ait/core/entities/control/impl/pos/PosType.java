package mdteam.ait.core.entities.control.impl.pos;

import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

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
            //fixed boundaries for the y value :) i spent like 45 minutes writing janky inline if statements checking for it.. it worked, but this is better :) - Loqor
            return pos.withY(MathHelper.clamp(pos.getY() + amount, -64, 256));
            //@TODO in the nether, search below 128 and above 0.
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
