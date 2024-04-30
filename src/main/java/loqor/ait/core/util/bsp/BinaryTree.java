package loqor.ait.core.util.bsp;

import loqor.ait.AITMod;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;
import org.apache.logging.log4j.core.jmx.Server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BinaryTree {
    protected Node rootNode;

    public BTreeInorderIterator iterator() {
        return new BTreeInorderIterator(this.getRootNode());
    }

    public int byteSize() {
        return Node.getChildrenCount(this.getRootNode()) * 3 * Long.BYTES;
    }

    public static class Node {
        Node left;
        Node right;
        Vec3d data;

        public Node(Vec3d data) {
            this.data = data;
            this.left = null;
            this.right = null;
        }

        public void addLeft(Vec3d data) {
            this.left = new Node(data);
        }

        public void addRight(Vec3d data) {
            this.right = new Node(data);
        }

        public Node getLeft() {
            return this.left;
        }

        public Node getRight() {
            return this.right;
        }

        public Vec3d getData() {
            return this.data;
        }

        public boolean isLeaf() {
            return this.left == null && this.right == null;
        }

        public boolean isBranch() {
            return this.left != null || this.right != null;
        }

        public static int getChildrenCount(Node node) {
            if (node == null)
                return 0;
            int i = 1;

            i += getChildrenCount(node.getLeft());
            i += getChildrenCount(node.getRight());

            return i;
        }
    };

    public BinaryTree(Vec3d root_data) {
        this.rootNode = new Node(root_data);
    }

    public Node getRootNode() {
        return this.rootNode;
    }

    public ByteBuffer toNioByteBuffer() {
        int size = this.byteSize() * 8;
        AITMod.LOGGER.info(String.format("Saving BinaryTree with arbitrary size of %d", size));

        BTreeInorderIterator it = new BTreeInorderIterator(this.getRootNode());
        ByteBuffer buffer = ByteBuffer.allocate(size);

        Node node = this.getRootNode();

        while (node != null) {
            buffer.putDouble(node.getData().x)
                    .putDouble(node.getData().y)
                    .putDouble(node.getData().z)
                    .rewind();
            node = it.next();
        }
        return buffer;
    }
}
