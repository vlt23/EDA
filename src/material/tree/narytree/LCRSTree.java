package material.tree.narytree;

import material.Position;

import java.util.Iterator;

/**
 * A linked class for a tree where nodes have an arbitrary number of children.
 *
 * @param <E> the elements stored in the tree
 * @author Raul Cabido, Abraham Duarte, Jose Velez, Jesús Sánchez-Oro
 */
public class LCRSTree<E> implements NAryTree<E> {

    /**
     * Inner class which represents a node of the tree
     *
     * @param <T> the type of the elements stored in a node
     */
    private static class TreeNode<T> implements Position<T> {

        private T element; // The element stored in the position
        private TreeNode<T> parent; // The parent of the node
        private TreeNode<T> leftChild; // The first left children of the node
        private TreeNode<T> rightSibling; // The next right sibling of the node
        private LCRSTree<T> myTree; // A reference to the tree where the node belongs

        /**
         * Constructor of the class
         *
         * @param t the tree where the node is stored
         * @param e the element to store in the node
         * @param p the parent of the node
         * @param c the list of children of the node
         */
        public TreeNode(LCRSTree<T> t, T e, TreeNode<T> p, TreeNode<T> c) {
            this.element = e;
            this.parent = p;
            this.leftChild = c;
            this.myTree = t;
        }

        @Override
        public T getElement() {
            return element;
        }

        /**
         * Sets the element stored at this position
         *
         * @param o the element to store in the node
         */
        public final void setElement(T o) {
            element = o;
        }

        /**
         * Accesses to the first left child of this node
         *
         * @return the first left child
         */
        public TreeNode<T> getLeftChild() {
            return leftChild;
        }

        /**
         * Sets the first left child of this node
         *
         * @param c the node to be used as first left child of this position
         */
        public void setLeftChild(TreeNode<T> c) {
            leftChild = c;
        }

        /**
         * Accesses to the next right sibling of this node
         *
         * @return the next right sibling
         */
        public TreeNode<T> getRightSibling() {
            return rightSibling;
        }

        /**
         * Sets the next right sibling of this node
         *
         * @param c the node to be used as next right sibling of this position
         */
        public void setRightSibling(TreeNode<T> c) {
            this.rightSibling = c;
        }

        /**
         * Accesses to the parent of this node
         *
         * @return the parent of this node
         */
        public TreeNode<T> getParent() {
            return parent;
        }

        /**
         * Sets the parent of this node
         *
         * @param v the node to be used as parent
         */
        public final void setParent(TreeNode<T> v) {
            parent = v;
        }

        /**
         * Consults the tree in which this node is stored
         *
         * @return a reference to the tree where the node belongs
         */
        public LCRSTree<T> getMyTree() {
            return myTree;
        }

        /**
         * Sets the tree where this node belongs
         *
         * @param myTree the tree where this node belongs
         */
        public void setMyTree(LCRSTree<T> myTree) {
            this.myTree = myTree;
        }
    }

    private TreeNode<E> root;
    private int size;

    public LCRSTree() {
        root = null;
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Position<E> root() throws RuntimeException {
        if (isEmpty()) {
            throw new RuntimeException("The tree is empty");
        }
        return root;
    }

    @Override
    public Position<E> parent(Position<E> v) throws RuntimeException {
        TreeNode<E> node = checkPosition(v);
        TreeNode<E> parent = node.getParent();
        if (parent == null) {
            throw new RuntimeException("The node has not parent");
        }
        return parent;
    }

    @Override
    public Iterable<? extends Position<E>> children(Position<E> v) {
        throw new RuntimeException("Not yet implemented");
    }

    @Override
    public boolean isInternal(Position<E> v) {
        return !isLeaf(v);
    }

    @Override
    public boolean isLeaf(Position<E> v) throws RuntimeException {
        TreeNode<E> node = checkPosition(v);
        return node.getLeftChild() == null;
    }

    @Override
    public boolean isRoot(Position<E> v) {
        TreeNode<E> node = checkPosition(v);
        return node == this.root;
    }

    @Override
    public Position<E> addRoot(E e) throws RuntimeException {
        if (!isEmpty()) {
            throw new RuntimeException("Tree already has a root");
        }
        root = new TreeNode<>(this, e, null, null);
        size = 1;
        return root;
    }

    @Override
    public Iterator<Position<E>> iterator() {
        throw new RuntimeException("Not yet implemented");
    }

    @Override
    public E replace(Position<E> p, E e) {
        TreeNode<E> node = checkPosition(p);
        E exElement = node.getElement();
        node.setElement(e);
        return exElement;
    }

    @Override
    public void swapElements(Position<E> p1, Position<E> p2) {
        TreeNode<E> node1 = checkPosition(p1);
        TreeNode<E> node2 = checkPosition(p2);
        E aux = node1.getElement();
        node1.setElement(node2.getElement());
        node2.setElement(aux);
    }

    @Override
    public Position<E> add(E element, Position<E> p) {
        throw new RuntimeException("Not yet implemented");
    }

    @Override
    public void remove(Position<E> p) {
        throw new RuntimeException("Not yet implemented");
    }

    @Override
    public void moveSubtree(Position<E> pOrig, Position<E> pDest) throws RuntimeException {
        throw new RuntimeException("Not yet implemented");
    }

    /**
     * Validates the given position, casting it to TreeNode if valid
     *
     * @param p the position to be converted
     * @return the position casted to TreeNode
     * @throws IllegalStateException if the position is not valid
     */
    private TreeNode<E> checkPosition(Position<E> p)
            throws IllegalStateException {
        if (p == null || !(p instanceof TreeNode)) {
            throw new IllegalStateException("The position is invalid");
        }
        TreeNode<E> aux = (TreeNode<E>) p;

        if (aux.getMyTree() != this) {
            throw new IllegalStateException("The node is not from this tree");
        }
        return aux;
    }

}
