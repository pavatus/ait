package loqor.ait.core.util.vortex;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import loqor.ait.core.util.bsp.BinaryTree;
import net.minecraft.util.math.Vec3d;

/*
    VORTEX_NODE_BRANCH SERIALISED BINARY LAYOUT
    [ GUARD_TYPE | POS:X | POS:Y | POS:Z | PTL:X | PTL:Y | PTL:Z | PTR:X | PTR:Y | PTR:Z | GUARD_END ]

    VORTEX_NODE_LEAF LAYOUT
    [ GUARD_TYPE | POS:X | POS:Y | POS:Z | GUARD_END ]
 */

public class VortexNode {
    private boolean isLeaf;
    private Vec3d pos;
    private Vec3d ptrToLeft;
    private Vec3d ptrToRight;

    public static final int GUARD_LEAF_NODE_MARK = 0xDEADBEEF;
    public static final int GUARD_END_NODE_MARK = 0xFDFDFDFD;
    public static final int EMPTY_VEC3D_FILLER = 0xDEADFA11;

    public VortexNode(boolean isLeaf, Vec3d data) {
        this.isLeaf = isLeaf;
        this.pos = data;
        this.ptrToLeft = null;
        this.ptrToRight = null;
    }

    public VortexNode(boolean isLeaf, Vec3d pos, Vec3d ptrToLeft, Vec3d ptrToRight) {
        this.isLeaf = isLeaf;
        this.pos = pos;
        this.ptrToLeft = ptrToLeft;
        this.ptrToRight = ptrToRight;
    }

    public VortexNode(BinaryTree.Node node) {
        this.isLeaf = node.isLeaf();
        this.pos = node.getPos();
        this.ptrToLeft = node.getPtrToLeft();
        this.ptrToRight = node.getPtrToRight();
    }

    public void serialize(ByteArrayDataOutput out) {
        if (this.isLeaf) {
            putLeaf(out, this.pos);
            return;
        }
        putBranch(out, this.pos, this.ptrToLeft, this.ptrToRight);
    }

    public byte[] serialize() {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        this.serialize(out);
        return out.toByteArray();
    }

    public static VortexNode deserialize(ByteArrayDataInput in) {
        if (in.readInt() == GUARD_LEAF_NODE_MARK)
            return getLeaf(in);
        return getBranch(in);
    }

    private static VortexNode getLeaf(ByteArrayDataInput in) {
        Vec3d pos = getOptionalVec3d(in);
        in.skipBytes(Integer.BYTES);
        return new VortexNode(true, pos);
    }

    private static VortexNode getBranch(ByteArrayDataInput in) {
        Vec3d pos = getOptionalVec3d(in);
        Vec3d ptl = getOptionalVec3d(in);
        Vec3d ptr = getOptionalVec3d(in);
        in.skipBytes(Integer.BYTES);
        return new VortexNode(false, pos, ptl, ptr);
    }

    private static void putLeaf(ByteArrayDataOutput out, Vec3d pos) {
        out.writeInt(GUARD_LEAF_NODE_MARK);
        putVec3d(out, pos);
        out.writeInt(GUARD_END_NODE_MARK);
    }

    private static void putBranch(ByteArrayDataOutput out, Vec3d pos, Vec3d ptrToLeft, Vec3d ptrToRight) {
        out.writeInt(GUARD_LEAF_NODE_MARK);
        putOptionalVec3d(out, pos);
        putOptionalVec3d(out, ptrToLeft);
        putOptionalVec3d(out, ptrToRight);
        out.writeInt(GUARD_END_NODE_MARK);
    }

    public static VortexNode deserialize(byte[] bytes) {
        ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
        return deserialize(in);
    }

    private static Vec3d getOptionalVec3d(ByteArrayDataInput in) {
        double x = in.readDouble();

        if ((int)x == EMPTY_VEC3D_FILLER) {
            in.skipBytes(Double.BYTES * 2);
            return null;
        }
        return new Vec3d(x, in.readDouble(), in.readDouble());
    }

    private static void putVec3d(ByteArrayDataOutput out, Vec3d vec) {
        out.writeDouble(vec.x);
        out.writeDouble(vec.y);
        out.writeDouble(vec.z);
    }

    private static void putOptionalVec3d(ByteArrayDataOutput out, Vec3d vec) {
        if (vec == null) {
            putEmptyVec3d(out);
        } else {
            putVec3d(out, vec);
        }
    }

    private static void putEmptyVec3dElement(ByteArrayDataOutput out) {
        out.writeDouble(EMPTY_VEC3D_FILLER);
    }

    private static void putEmptyVec3d(ByteArrayDataOutput out) {
        for (int i = 0; i < 3; i++) putEmptyVec3dElement(out);
    }
}
