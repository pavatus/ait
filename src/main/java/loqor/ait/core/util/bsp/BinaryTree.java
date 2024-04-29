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
    public Node rootNode;

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

    public void saveTree(MinecraftServer server) {
        int size = Node.getChildrenCount(this.getRootNode()) * 3 * Double.BYTES;
        AITMod.LOGGER.info(String.format("Saving BinaryTree with arbitrary size of %d", size));

        BTreeInorderIterator it = new BTreeInorderIterator(this.getRootNode());
        ByteBuffer buffer = ByteBuffer.allocate(size);

        Node node = this.getRootNode();

        while (node != null) {
            this.saveData(node.getData(), buffer);
            node = it.next();
        }

        try {
            Path save_path = ServerTardisManager.getRootSavePath(server).resolve("vortex");
            Files.createDirectories(Paths.get(save_path.toString()));
            File outFd = new File(save_path.resolve("vortex.dat").toString());

            AITMod.LOGGER.info("Saving binary tree data to {}", save_path.resolve("vortex.dat"));

            int bytes;
            try (FileChannel fc = new FileOutputStream(outFd, false).getChannel()) {
                bytes = fc.write(buffer);
            }
            AITMod.LOGGER.info("Saved BinaryTree, {} bytes written", bytes);
        } catch (IOException e) {
            AITMod.LOGGER.error("Unable to save the binary tree: {}", e.getMessage());
        }
    }

    private void saveData(Vec3d data, ByteBuffer buf) {
        buf.put((byte) data.x);
        buf.put((byte) data.y);
        buf.put((byte) data.z);
    }

//    public void debugPrint() {
//        BTreeInorderIterator it = new BTreeInorderIterator(this.rootNode);
//
//        Node node = this.rootNode;
//        int i = 0;
//
//        while (node != null) {
//            Vec3d d = node.getData();
//            AITMod.LOGGER.info(String.format("BinaryTree: Node {%d}, data: [%f, %f, %f]", i, d.x, d.y, d.z));
//            node = it.next();
//            i++;
//        }
//    }
}
