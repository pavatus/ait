package loqor.ait.core.util.bsp;

import loqor.ait.AITMod;
import loqor.ait.core.util.bsp.BinaryTree.Node;

import java.util.Stack;

public class BTreeInorderIterator {
    Stack<Node> traversal;

    public BTreeInorderIterator(Node root) {
        this.traversal = new Stack<Node>();
        this.moveLeft(root);
    }

    private void moveLeft(Node currentNode) {
        while (currentNode != null) {
            assert traversal != null;

            traversal.push(currentNode);
            currentNode = currentNode.left;
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

        if (current.right != null)
            this.moveLeft(current.right);
        return current;
    }

//    public Node peek() {
//        if (!this.hasNext()) {
//            AITMod.LOGGER.warn("BTreeInorderIterator: No next entry exists");
//            return null;
//        }
//        Node current = traversal.getLast();
//
//        if (current.right != null)
//            return current.right;
//        return current;
//    }
}
