package loqor.ait.data;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.function.Function;

import com.google.gson.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;

public class DirectedBlockPos {

    public static final Codec<DirectedBlockPos> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(BlockPos.CODEC.fieldOf("pos").forGetter(DirectedBlockPos::getPos),
                    Codec.BYTE.fieldOf("rotation").forGetter(DirectedBlockPos::getRotation))
            .apply(instance, DirectedBlockPos::create));

    private final BlockPos pos;
    private final byte rotation;

    private DirectedBlockPos(BlockPos pos, byte rotation) {
        this.pos = pos;
        this.rotation = rotation;
    }

    public DirectedBlockPos pos(int x, int y, int z) {
        return this.pos(new BlockPos(x, y, z));
    }

    public DirectedBlockPos pos(BlockPos pos) {
        return DirectedBlockPos.create(pos, this.rotation);
    }

    public DirectedBlockPos offset(int x, int y, int z) {
        return DirectedBlockPos.create(this.pos.add(x, y, z), this.rotation);
    }

    public DirectedBlockPos apply(Function<Integer, Integer> func) {
        return DirectedBlockPos.create(
                new BlockPos(func.apply(this.pos.getX()), func.apply(this.pos.getY()), func.apply(this.pos.getZ())),
                this.rotation);
    }

    public static DirectedBlockPos create(BlockPos pos, byte rotation) {
        return new DirectedBlockPos(pos, rotation);
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

    public Direction toMinecraftDirection() {
        return switch (this.rotation) {
            case 1, 2, 3, 4 -> Direction.EAST;
            case 5, 6, 7, 8 -> Direction.SOUTH;
            case 9, 10, 11, 12 -> Direction.WEST;
            default -> Direction.NORTH;
        };
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof DirectedBlockPos blockPos))
            return false;

        return Objects.equals(this.pos, blockPos.pos) && Objects.equals(this.rotation, blockPos.rotation);
    }

    public int hashCode() {
        return Objects.hash(this.pos, this.rotation);
    }

    public String toString() {
        return this.pos + " " + this.rotation;
    }

    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.getPos());
        buf.writeByte(this.rotation);
    }

    public static DirectedBlockPos read(PacketByteBuf buf) {
        BlockPos blockPos = buf.readBlockPos();
        byte rotation = buf.readByte();

        return DirectedBlockPos.create(blockPos, rotation);
    }

    public static Object serializer() {
        return new Serializer();
    }

    private static class Serializer implements JsonDeserializer<DirectedBlockPos>, JsonSerializer<DirectedBlockPos> {

        @Override
        public DirectedBlockPos deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject obj = json.getAsJsonObject();

            int x = obj.get("x").getAsInt();
            int y = obj.get("y").getAsInt();
            int z = obj.get("z").getAsInt();
            byte rotation = obj.get("rotation").getAsByte();

            return DirectedBlockPos.create(new BlockPos(x, y, z), rotation);
        }

        @Override
        public JsonElement serialize(DirectedBlockPos src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject result = new JsonObject();

            result.addProperty("x", src.getPos().getX());
            result.addProperty("y", src.getPos().getY());
            result.addProperty("z", src.getPos().getZ());
            result.addProperty("rotation", src.getRotation());

            return result;
        }
    }
}
