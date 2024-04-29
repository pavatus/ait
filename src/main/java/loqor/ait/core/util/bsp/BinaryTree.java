package loqor.ait.core.util.bsp;

import loqor.ait.AITMod;
import net.minecraft.util.math.Vec3d;

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
            return !this.isLeaf();
        }
    };

    public BinaryTree(Vec3d root_data) {
        this.rootNode = new Node(root_data);
    }

    public void debugPrint() {
        BTreeInorderIterator it = new BTreeInorderIterator(this.rootNode);

        Node node = this.rootNode;
        int i = 0;

        while (node != null) {
            Vec3d d = node.getData();
            AITMod.LOGGER.info(String.format("BinaryTree: Node {%d}, value: [{%e}, {%e}, {%e}]", i, d.x, d.y, d.z));
            node = it.next();
            i++;
        }
    }
}
