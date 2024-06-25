package loqor.ait.core.data;

import com.google.gson.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import loqor.ait.core.data.base.Exclude;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.lang.reflect.Type;
import java.util.Objects;

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

    public DirectedGlobalPos rotation(byte rotation) {
        return DirectedGlobalPos.create(this.dimension, this.pos, rotation);
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
        buf.writeRegistryKey(this.dimension);
        buf.writeBlockPos(this.pos);
        buf.writeByte(this.rotation);
    }

    public static DirectedGlobalPos read(PacketByteBuf buf) {
        RegistryKey<World> registryKey = buf.readRegistryKey(RegistryKeys.WORLD);
        BlockPos blockPos = buf.readBlockPos();
        byte rotation = buf.readByte();

        return DirectedGlobalPos.create(registryKey, blockPos, rotation);
    }

    public NbtCompound toNbt() {
        NbtCompound compound = NbtHelper.fromBlockPos(this.pos);
        compound.putString("dimension", this.dimension.getValue().toString());
        compound.putByte("rotation", this.rotation);

        return compound;
    }

    public static DirectedGlobalPos fromNbt(NbtCompound compound) {
        BlockPos pos = NbtHelper.toBlockPos(compound);
        RegistryKey<World> dimension = RegistryKey.of(RegistryKeys.WORLD,
                new Identifier(compound.getString("dimension")));

        byte rotation = compound.getByte("rotation");
        return DirectedGlobalPos.create(dimension, pos, rotation);
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

        private static Cached createNew(ServerWorld lastWorld, RegistryKey<World> newWorldKey, BlockPos pos, byte rotation) {
            if (lastWorld == null)
                return new Cached(newWorldKey, pos, rotation);

            ServerWorld newWorld = lastWorld;

            if (lastWorld.getRegistryKey() != newWorldKey)
                newWorld = lastWorld.getServer().getWorld(newWorldKey);

            return Cached.create(newWorld, pos, rotation);
        }

        public void init(MinecraftServer server) {
            if (this.world != null)
                this.world = server.getWorld(this.getDimension());
        }

        public ServerWorld getWorld() {
            return world;
        }

        @Override
        public Cached offset(int x, int y, int z) {
            return Cached.createSame(this.world, this.getDimension(), this.getPos().add(x, y, z), this.getRotation());
        }

        @Override
        public Cached world(RegistryKey<World> dimension) {
            return Cached.createNew(this.world, dimension, this.getPos(), this.getRotation());
        }

        @Override
        public Cached pos(BlockPos pos) {
            return Cached.createSame(this.world, this.getDimension(), pos, this.getRotation());
        }

        @Override
        public Cached pos(int x, int y, int z) {
            return pos(new BlockPos(x, y, z));
        }

        @Override
        public DirectedGlobalPos.Cached rotation(byte rotation) {
            return Cached.createSame(this.world, this.getDimension(), this.getPos(), rotation);
        }

        public Cached world(ServerWorld world) {
            return Cached.create(world, this.getPos(), this.getRotation());
        }

        public static Cached fromNbt(NbtCompound compound) {
            BlockPos pos = NbtHelper.toBlockPos(compound);
            RegistryKey<World> dimension = RegistryKey.of(RegistryKeys.WORLD,
                    new Identifier(compound.getString("dimension")));

            byte rotation = compound.getByte("rotation");
            return createNew(null, dimension, pos, rotation);
        }
    }

    public static Object serializer() {
        return new Serializer();
    }

    private static class Serializer implements JsonDeserializer<DirectedGlobalPos>, JsonSerializer<DirectedGlobalPos> {

        @Override
        public DirectedGlobalPos deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject obj = json.getAsJsonObject();

            RegistryKey<World> dimension = context.deserialize(obj.get("dimension"), RegistryKey.class);

            int x = obj.get("x").getAsInt();
            int y = obj.get("y").getAsInt();
            int z = obj.get("z").getAsInt();
            byte rotation = obj.get("rotation").getAsByte();

            return DirectedGlobalPos.create(dimension, new BlockPos(x, y, z), rotation);
        }

        @Override
        public JsonElement serialize(DirectedGlobalPos src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject result = new JsonObject();

            result.addProperty("dimension", src.getDimension().getValue().toString());
            result.addProperty("x", src.getPos().getX());
            result.addProperty("y", src.getPos().getY());
            result.addProperty("z", src.getPos().getZ());
            result.addProperty("rotation", src.getRotation());

            return result;
        }
    }
}
