package mdteam.ait.core.entities.control.impl.pos;

import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public enum PosType implements StringIdentifiable {
    X() {
        @Override
        public BlockPos add(BlockPos pos, int amount) {
            return pos.add(amount,0,0);
        }
    },
    Y() {
        @Override
        public BlockPos add(BlockPos pos, int amount, World world) {
            return pos.withY(MathHelper.clamp(pos.getY(), world.getBottomY(), world.getTopY()));
        }

        @Override
        public BlockPos add(BlockPos pos, int amount) {
            return pos.withY(pos.getY() + amount);
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
    public BlockPos add(BlockPos pos, int amount, World world) {
        return add(pos,amount);
    }

    @Override
    public String asString() {
        return this.name().toLowerCase();
    }
}
