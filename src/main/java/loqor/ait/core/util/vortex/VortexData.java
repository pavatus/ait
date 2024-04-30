package loqor.ait.core.util.vortex;

import io.netty.buffer.ByteBuf;
import loqor.ait.AITMod;
import net.minecraft.util.math.Vec3d;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;

public record VortexData(ArrayList<Vec3d> positions) {
    public int byteSize() {
        return this.positions.size() * 3 * Double.BYTES * 8;
    }

    public static VortexData deserialize(ByteBuffer buffer) {
        ArrayList<Vec3d> data = new ArrayList<>();

        while (buffer.hasRemaining()) {
            double x, y, z;
            try {
                x = buffer.getDouble();
                y = buffer.getDouble();
                z = buffer.getDouble();
            } catch (BufferUnderflowException e) {
                AITMod.LOGGER.error("VortexData: Deserialization failed: Invalid Vec3d save format: {}", e.getMessage());
                return null;
            }
            data.add(new Vec3d(x, y, z));
        }
        return new VortexData(data);
    }

    public static VortexData deserialize(ByteBuf buffer) {
        return VortexData.deserialize(buffer.nioBuffer());
    }

    public ByteBuffer serialize() {
        Iterator<Vec3d> it = this.positions.iterator();
        Vec3d current = this.positions.get(0);
        ByteBuffer buffer = ByteBuffer.allocateDirect(this.byteSize());

        while (it.hasNext()) {
            buffer.putDouble(current.x);
            buffer.putDouble(current.y);
            buffer.putDouble(current.z);
        }
        return buffer;
    }
}
