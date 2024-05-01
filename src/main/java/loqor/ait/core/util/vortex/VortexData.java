package loqor.ait.core.util.vortex;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import java.util.ArrayList;
import java.util.List;

public record VortexData(List<VortexNode> nodes) {
    public void serialize(ByteArrayDataOutput out) {
        for (VortexNode vortexNode : this.nodes())
            putVortexNode(out, vortexNode);
    }

    public byte[] serialize() {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        serialize(out);
        return out.toByteArray();
    }

    public static VortexData deserialize(ByteArrayDataInput in) {
        List<VortexNode> nodes = new ArrayList<>();

        try {
            while (true) {
                VortexNode node = VortexNode.deserialize(in);
                nodes.add(node);
            }
        } catch (IllegalStateException e) {
            return new VortexData(nodes);
        }
    }

    public static VortexData deserialize(byte[] bytes) {
        ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
        return deserialize(in);
    }

    private void putVortexNode(ByteArrayDataOutput out, VortexNode node) {
        out.write(node.serialize());
    }
}
