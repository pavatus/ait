package loqor.ait.core.util.bsp;

import loqor.ait.AITMod;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class BTreeGenerator {
    private final int maxDepth;

    private BinaryTree binaryTree;
    private BTreeInorderIterator bTreeInorderIterator;
    private final Random rng;
    private final ServerWorld world;

    public BTreeGenerator(ServerWorld world) {
        this.maxDepth = 2048;
        this.binaryTree = null;
        this.bTreeInorderIterator = null;
        this.rng = new Random(world.getSeed());
        this.world = world;
    }

    // 'magic number' my ass
    private void genLhs(BinaryTree.Node node, Vec3d prevPos) {
        int offset = this.rng.nextInt(256 * 3);
        node.addLeft(new Vec3d(prevPos.x - offset * 0.5, prevPos.y, prevPos.z - offset * 0.6));
    }

    private void genRhs(BinaryTree.Node node, Vec3d prevPos) {
        int offset = this.rng.nextInt(256 * 3);
        node.addRight(new Vec3d(prevPos.x + offset * 0.6, prevPos.y, prevPos.z - offset * 0.7));
    }

    private void genBoth(BinaryTree.Node node, Vec3d prevPos) {
        this.genLhs(node, prevPos);
        this.genRhs(node, prevPos);
    }

    public void gen(BinaryTree binaryTree) {
        // serverside only
        if (this.world.isClient())
            return;

        this.binaryTree = binaryTree;
        this.bTreeInorderIterator = new BTreeInorderIterator(this.binaryTree.rootNode);

        BinaryTree.Node node = this.binaryTree.rootNode;
        Vec3d prevPos = node.getData();

        int depth = 0;

        while (true) {
            if (depth == maxDepth)
                return;

            // 0 - generate left hand side
            // 1 - generate right hand side
            // 2 - generate both
            int genSide = this.rng.nextInt(3);

            switch (genSide) {
                case 0 -> genLhs(node, prevPos);
                case 1 -> genRhs(node, prevPos);
                case 2 -> genBoth(node, prevPos);
                default -> AITMod.LOGGER.error("How the fuck did that happen? How did you do that?");
            }
            if (this.bTreeInorderIterator.hasNext()) {
                node = this.bTreeInorderIterator.next();
            } else {
                node = new BinaryTree.Node(prevPos);
            }
            depth += 1;
        }
    }
}
