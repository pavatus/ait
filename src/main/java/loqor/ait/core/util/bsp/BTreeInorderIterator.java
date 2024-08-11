package loqor.ait.core.util.bsp;

import java.util.Stack;

import loqor.ait.AITMod;
import loqor.ait.core.util.bsp.BinaryTree.Node;

public class BTreeInorderIterator {
    private final Stack<Node> traversal;

    public BTreeInorderIterator(Node root) {
        this.traversal = new Stack<Node>();
        this.moveLeft(root);
    }

    private void moveLeft(Node currentNode) {
        while (currentNode != null) {
            traversal.push(currentNode);
            currentNode = currentNode.getLeft();
        }
    }

    public boolean hasNext() {
        return !traversal.isEmpty();
    }

    public Node next() {
        if (!this.hasNext()) {
            AITMod.LOGGER.warn("BTreeInorderIterator: No next entry exists");
            return null;
        }
        Node current = traversal.pop();

        if (current.getRight() != null)
            this.moveLeft(current.getRight());
        return current;
    }
}
