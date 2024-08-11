package loqor.ait.core.util.bsp;

import java.util.Random;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

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

    private void genLhs(BinaryTree.Node node, Vec3d prevPos) {
        node.addLeft(this.genData(prevPos, 0));
    }

    private void genRhs(BinaryTree.Node node, Vec3d prevPos) {
        node.addRight(this.genData(prevPos, 1));
    }

    private void genBoth(BinaryTree.Node node, Vec3d prevPos) {
        this.genLhs(node, prevPos);
        this.genRhs(node, prevPos);
    }

    private Vec3d genData(Vec3d prev, int side) {
        int offset = this.rng.nextInt(256 * 3);
        Vec3d out = null;

        switch (side) {
            case 0 -> out = new Vec3d(prev.x - offset * 0.5, prev.y, prev.z - offset * 0.6);
            case 1 -> out = new Vec3d(prev.x + offset * 0.6, prev.y, prev.z - offset * 0.7);
        }
        return out;
    }

    private void genFor(BinaryTree.Node node, Vec3d prevPos) {
        // 0 - generate left hand side
        // 1 - generate right hand side
        // 2 - generate both
        int genSide = this.rng.nextInt(3);

        switch (genSide) {
            case 0 -> this.genLhs(node, prevPos);
            case 1 -> this.genRhs(node, prevPos);
            case 2 -> this.genBoth(node, prevPos);
        }
    }

    public void gen(BinaryTree binaryTree) {
        // serverside only
        if (this.world.isClient())
            return;

        this.binaryTree = binaryTree;
        this.bTreeInorderIterator = binaryTree.iterator();

        BinaryTree.Node node = this.binaryTree.getRootNode();
        Vec3d prevPos = node.getPos();

        // Generate for the root node first so the BTII doesn't break
        this.genFor(node, prevPos);

        int depth = 0;

        while (depth != maxDepth) {
            if (this.bTreeInorderIterator.hasNext()) {
                node = this.bTreeInorderIterator.next();
            } else {
                node.setLeft(this.genData(prevPos, 0));
                node = node.getLeft();
            }
            this.genFor(node, prevPos);
            prevPos = node.getPos();
            depth += 1;
        }
    }
}
