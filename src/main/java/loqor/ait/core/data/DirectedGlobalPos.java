package loqor.ait.core.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import loqor.ait.core.data.base.Exclude;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.function.Function;

public class DirectedGlobalPos {

    public static final Codec<DirectedGlobalPos> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    World.CODEC.fieldOf("dimension")
                            .forGetter(DirectedGlobalPos::getDimension),
                    BlockPos.CODEC.fieldOf("pos")
                            .forGetter(DirectedGlobalPos::getPos),
                    Codec.BYTE.fieldOf("rotation")
                            .forGetter(DirectedGlobalPos::getRotation)
            ).apply(instance, DirectedGlobalPos::create));

    private final RegistryKey<World> dimension;
    private final BlockPos pos;
    private final byte rotation;

    private DirectedGlobalPos(RegistryKey<World> dimension, BlockPos pos, byte rotation) {
        this.dimension = dimension;
        this.pos = pos;

        this.rotation = rotation;
    }

    public DirectedGlobalPos pos(int x, int y, int z) {
        return this.pos(new BlockPos(x, y, z));
    }

    public DirectedGlobalPos pos(BlockPos pos) {
        return DirectedGlobalPos.create(this.dimension, pos, this.rotation);
    }

    public DirectedGlobalPos offset(int x, int y, int z) {
        return DirectedGlobalPos.create(this.dimension, this.pos.add(x, y, z), this.rotation);
    }

    public DirectedGlobalPos apply(Function<Integer, Integer> func) {
        return DirectedGlobalPos.create(this.dimension, new BlockPos(
                func.apply(this.pos.getX()),
                func.apply(this.pos.getY()),
                func.apply(this.pos.getZ())
        ), this.rotation);
    }

    public DirectedGlobalPos world(RegistryKey<World> world) {
        return DirectedGlobalPos.create(world, this.pos, this.rotation);
    }

    public static DirectedGlobalPos create(RegistryKey<World> dimension, BlockPos pos, byte rotation) {
        return new DirectedGlobalPos(dimension, pos, rotation);
    }

    public RegistryKey<World> getDimension() {
        return this.dimension;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public byte getRotation() {
        return this.rotation;
    }

    public Vec3i getVector() {
        return switch (this.rotation) {
            default -> new Vec3i(0, 0, 0);
            case 0 -> Direction.NORTH.getVector();
            case 1, 2, 3 -> Direction.NORTH.getVector().add(Direction.EAST.getVector());
            case 4 -> Direction.EAST.getVector();
            case 5, 6, 7 -> Direction.EAST.getVector().add(Direction.SOUTH.getVector());
            case 8 -> Direction.SOUTH.getVector();
            case 9, 10, 11 -> Direction.SOUTH.getVector().add(Direction.WEST.getVector());
            case 12 -> Direction.WEST.getVector();
            case 13, 14, 15 -> Direction.NORTH.getVector().add(Direction.SOUTH.getVector());
        };
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof DirectedGlobalPos globalPos))
            return false;

        return Objects.equals(this.dimension, globalPos.dimension) && Objects.equals(this.pos, globalPos.pos);
    }

    public int hashCode() {
        return Objects.hash(this.dimension, this.pos, this.rotation);
    }

    public String toString() {
        return this.dimension + " " + this.pos + " " + this.rotation;
    }

    public void write(PacketByteBuf buf) {
        buf.writeRegistryKey(this.getDimension());
        buf.writeBlockPos(this.getPos());
        buf.writeByte(this.rotation);
    }

    public static DirectedGlobalPos read(PacketByteBuf buf) {
        RegistryKey<World> registryKey = buf.readRegistryKey(RegistryKeys.WORLD);
        BlockPos blockPos = buf.readBlockPos();
        byte rotation = buf.readByte();

        return DirectedGlobalPos.create(registryKey, blockPos, rotation);
    }

    public static class Cached extends DirectedGlobalPos {

        @Exclude private ServerWorld world;

        private Cached(RegistryKey<World> key, BlockPos pos, byte rotation) {
            super(key, pos, rotation);
        }

        private Cached(ServerWorld world, BlockPos pos, byte rotation) {
            this(world.getRegistryKey(), pos, rotation);
            this.world = world;
        }

        public static Cached create(ServerWorld world, BlockPos pos, byte rotation) {
            return new Cached(world, pos, rotation);
        }

        private static Cached createSame(ServerWorld world, RegistryKey<World> dimension, BlockPos pos, byte rotation) {
            if (world == null)
                return new Cached(dimension, pos, rotation);

            return Cached.create(world, pos, rotation);
        }

        private static Cached createNew(ServerWorld lastWorld, RegistryKey<World> newWorld, BlockPos pos, byte rotation) {
            if (lastWorld == null)
                return new Cached(newWorld, pos, rotation);

            return Cached.create(lastWorld.getServer().getWorld(newWorld), pos, rotation);
        }

        public void init(MinecraftServer server) {
            this.world = server.getWorld(this.getDimension());
        }

        public ServerWorld getWorld() {
            return world;
        }

        @Override
        public DirectedGlobalPos.Cached offset(int x, int y, int z) {
            return Cached.createSame(this.world, this.getDimension(), this.getPos().add(x, y, z), this.getRotation());
        }

        @Override
        public DirectedGlobalPos.Cached world(RegistryKey<World> dimension) {
            return Cached.createNew(this.world, dimension, this.getPos(), this.getRotation());
        }
    }
}
