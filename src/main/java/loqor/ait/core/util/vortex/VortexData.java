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
        return this.positions.size() * 3 * Double.BYTES;
    }

    public static VortexData deserialize(ByteBuffer buffer) {
        ArrayList<Vec3d> data = new ArrayList<Vec3d>();

        while (buffer.hasRemaining()) {
            double x, y, z;
            try {
                x = buffer.getDouble();
                y = buffer.getDouble();
                z = buffer.getDouble();
            } catch (BufferUnderflowException e) {
                AITMod.LOGGER.error("VortexData: Serialisation failed: Invalid Vec3d save format: {}", e.getMessage());
                return null;
            }
            Vec3d vec = new Vec3d(x, y, z);
            data.add(vec);
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
            buffer.putDouble(current.getX());
            buffer.putDouble(current.getY());
            buffer.putDouble(current.getZ());
        }
        return buffer;
    }
}
