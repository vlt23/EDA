package material.tree.iterators;

import material.Position;
import material.tree.binarytree.BinaryTree;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Inorder iterator for binary trees.
 *
 * @param <T>
 * @author jvelez, JD. Quintana, vlt23
 */
public class InorderBinaryTreeIterator<T> implements Iterator<Position<T>> {

    private Deque<Position<T>> nodeStack = new LinkedList<>();
    private final BinaryTree<T> tree;
    private boolean isBeginRoot;

    public InorderBinaryTreeIterator(BinaryTree<T> tree) {
        this.tree = tree;
        if (!tree.isEmpty()) {
            goToLastInLeft(tree.root());
        }
        isBeginRoot = true;
    }

    public InorderBinaryTreeIterator(BinaryTree<T> tree, Position<T> node) {
        this.tree = tree;
        goToLastInLeft(node);
        isBeginRoot = node == tree.root();
    }

    private void goToLastInLeft(Position<T> node) {
        nodeStack.addFirst(node);
        while (tree.hasLeft(node)) {
            node = tree.left(node);
            nodeStack.addFirst(node);
        }
    }

    @Override
    public boolean hasNext() {
        return (!nodeStack.isEmpty());
    }

    /**
     * This method visits the nodes of a tree by following a breath-first search
     */
    @Override
    public Position<T> next() {
        Position<T> aux = nodeStack.removeFirst();
        if (tree.hasRight(aux)) {
            goToLastInLeft(tree.right(aux));
        } else if (!isBeginRoot && nodeStack.isEmpty()) {
            Position<T> parent = tree.parent(aux);
            Position<T> child = aux;
            boolean ifAdd = true;  // need to not iterate twice
            while (tree.hasRight(parent) && tree.right(parent) == child) {
                if (tree.root() == parent) {
                    ifAdd = false;
                    break;
                }
                parent = tree.parent(parent);
                child = tree.parent(child);
            }
            if (ifAdd) {
                nodeStack.addFirst(parent);
            }
            if (parent == tree.root()) {
                isBeginRoot = true;
            }
        }
        return aux;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not implemented.");
    }

}
