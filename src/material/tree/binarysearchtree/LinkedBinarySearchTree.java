package material.tree.binarysearchtree;

import material.Position;
import material.tree.binarytree.LinkedBinaryTree;
import material.tree.iterators.InorderBinaryTreeIterator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Realization of a dictionary by means of a binary search tree.
 *
 * @author R. Cabido, A. Duarte and J. Vélez
 * @author vlt23
 */

public class LinkedBinarySearchTree<E> implements BinarySearchTree<E> {

    private static class BSTIterator<T> implements Iterator<Position<T>> {
        private LinkedBinaryTree<T> lbt;
        int nonVisited;
        Iterator<Position<T>> it;

        private BSTIterator(LinkedBinarySearchTree<T> tree) {
            this.lbt = tree.binTree;
            this.it = lbt.iterator();
            this.nonVisited = tree.size();
        }

        @Override
        public boolean hasNext() {
            return (nonVisited > 0);
        }

        /**
         * This method visits the nodes of a tree by following a breath-first search
         */
        @Override
        public Position<T> next() {
            if (nonVisited == 0) {
                throw new RuntimeException("The BST has not more elements");
            }
            Position<T> aux = this.it.next();
            while (aux.getElement() == null) {
                aux = this.it.next();
            }
            nonVisited--;
            return aux;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not implemented.");
        }
    }

    protected LinkedBinaryTree<E> binTree;
    protected Comparator<E> comparator; // comparator
    protected int size = 0; // number of entries

    /**
     * Creates a BinarySearchTree with a default comparator.
     */
    public LinkedBinarySearchTree() {
        this(null);
    }

    /**
     * Creates a BinarySearchTree with the given comparator.
     */
    public LinkedBinarySearchTree(Comparator<E> c) {
        if (c == null) {
            this.comparator = new DefaultComparator<>();
        } else {
            this.comparator = c;
        }
        this.binTree = new LinkedBinaryTree<>();
        this.binTree.addRoot(null);
    }

    /**
     * Extracts the value of the entry at a given node of the tree.
     */
    protected E value(Position<E> position) {
        return position.getElement();
    }

//    /**
//     * Replaces an entry with a new entry (and reset the entry's location)
//     */
//    @Deprecated
//    protected void replacecValue(Position<E> pos, E ent) {
//        this.binTree.replace(pos, ent);
//    }

    /**
     * Expand an external node into an internal node with two external node children
     */
    protected void expandLeaf(Position<E> p, E e1, E e2) throws RuntimeException {
        if (!this.binTree.isLeaf(p)) {
            throw new RuntimeException("Node is not external");
        }
        this.binTree.insertLeft(p, e1);
        // SÓLO UNO DE LOS DOS. DEPENDE DE SI LA INSERCIÓN SE HACE CON <= O CON >=
        this.binTree.insertRight(p, e2);
    }

    /**
     * Remove an external node v and replace its parent with v's sibling
     */
    protected void removeAboveLeaf(Position<E> p) throws RuntimeException {
        Position<E> u = this.binTree.parent(p);
        this.binTree.remove(p);
        this.binTree.remove(u);
    }

    /**
     * Auxiliary method for inserting an entry at an external node
     */
    protected Position<E> insertAtLeaf(Position<E> pos, E value) {
        this.expandLeaf(pos, null, null);
        this.binTree.replace(pos, value);
        size++;
        return pos;
    }

    /**
     * Auxiliary method for removing an external node and its parent
     */
    protected void removeLeaf(Position<E> v) {
        removeAboveLeaf(v);
        size--;
    }

    /**
     * Auxiliary method used by find, insert, and remove.
     */
    protected Position<E> treeSearch(E value, Position<E> pos) throws RuntimeException {
        if (this.binTree.isLeaf(pos)) {
            return pos; // key not found, return external node
        }
        E curValue = pos.getElement();
        int comp = comparator.compare(value, curValue);
        if (comp < 0) {
            return treeSearch(value, this.binTree.left(pos)); // search left subtree
        } else if (comp > 0) {
            return treeSearch(value, this.binTree.right(pos)); // search right subtree
        }
        return pos; // return internal node where key is found
    }

    /**
     * Adds to list all entries in the subtree rooted at v having keys equal to k.
     */
    protected void addAll(List<Position<E>> list, Position<E> pos, E value) {
        if (this.binTree.isLeaf(pos)) {
            return;
        }
        Position<E> p = treeSearch(value, pos);
        if (!this.binTree.isLeaf(p)) { // we found an entry with key equal to k
            addAll(list, this.binTree.left(p), value);
            list.add(p); // add entries in inorder
            addAll(list, this.binTree.right(p), value);
        } // this recursive algorithm is simple, but it's not the fastest
    }

    /**
     * Returns the number of entries in the tree.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Returns whether the tree is empty.
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns an entry containing the given key. Returns null if no such entry exists.
     */
    @Override
    public Position<E> find(E value) {
        Position<E> curPos = treeSearch(value, this.binTree.root());
        if (this.binTree.isInternal(curPos)) {
            return curPos;
        }
        return null;
    }

    /**
     * Returns an iterable collection of all the entries containing the given key.
     */
    @Override
    public Iterable<Position<E>> findAll(E value) {
        List<Position<E>> list = new ArrayList<>();
        addAll(list, this.binTree.root(), value);
        return list;
    }

    /**
     * Inserts an entry into the tree and returns the newly created entry.
     */
    @Override
    public Position<E> insert(E value) {
        Position<E> insPos = treeSearch(value, this.binTree.root());
        // To consider nodes already in the tree with the same key
        while (!this.binTree.isLeaf(insPos)) {
            insPos = treeSearch(value, this.binTree.right(insPos)); // iterative search for insertion position
        }
        return insertAtLeaf(insPos, value);
    }

    protected Position<E> getLeafToRemove(Position<E> pos) {
        Position<E> remPos = pos;

        if (this.binTree.isLeaf(this.binTree.left(remPos))) {
            remPos = this.binTree.left(remPos); // left easy case
        } else if (this.binTree.isLeaf(this.binTree.right(remPos))) {
            remPos = this.binTree.right(remPos); // right easy case
        } else { // entry is at a node with internal children
            Position<E> swapPos = remPos; // find node for moving
            // entry
            remPos = this.binTree.right(swapPos);
            do {
                remPos = this.binTree.left(remPos);
            } while (this.binTree.isInternal(remPos));
                /* Deprecated. Doesn't update AVLInfo.getTreePosition...
                //replacecValue(swapPos, this.binTree.parent(remPos).getElement());
                */
            this.binTree.swap(swapPos, this.binTree.parent(remPos));
        }
        return remPos;
    }

    /**
     * Removes and returns a given entry.
     */
    @Override
    public E remove(Position<E> pos) throws RuntimeException {
        //this.checkPosition(pos); // may throw an InvalidPositionException
        E toReturn = pos.getElement(); // entry to be returned
        Position<E> remPos = getLeafToRemove(pos);
        removeLeaf(remPos);
        return toReturn;
    }

    /**
     * Returns an iterator of the elements stored at the nodes.
     */
    public Iterator<Position<E>> iterator() {
        return new BSTIterator<>(this);
    }

    public Iterable<Position<E>> findRange(E minValue, E maxValue) throws RuntimeException {
        if (comparator.compare(minValue, maxValue) > 0) {  // E parameter
            throw new RuntimeException("Invalid range. (min>max)");
        }

        List<Position<E>> list = new ArrayList<>();
        Position<E> currentValuePos = findNextPos(minValue, binTree.root());
        if (currentValuePos == null) {
            return list;  // empty list, minValue is higher than the max node
        }

        Iterator<Position<E>> it = new InorderBinaryTreeIterator<>(binTree, currentValuePos);
        Position<E> pos;
        while (it.hasNext()) {
            pos = it.next();
            // Ignoring trash nodes
            if (pos.getElement() != null) {
                if (comparator.compare(pos.getElement(), maxValue) <= 0) {
                    list.add(pos);
                } else {
                    break;
                }
            }
        }
        return list;
    }

    /**
     * Recursive method to find where the value's position
     * @author vlt23
     * @param value to find
     * @param currentPos to see if has the value
     * @return the exactly value's position, or the next if not found
     */
    private Position<E> findNextPos(E value, Position<E> currentPos) {
        // Trash node, not found the exactly value's node, return to parent
        if (currentPos.getElement() == null) {
            return null;
        }

        if (comparator.compare(value, currentPos.getElement()) < 0) {  // Left
            if (binTree.hasLeft(currentPos)) {
                // Go to left subtree
                Position<E> returnedPos = findNextPos(value, binTree.left(currentPos));
                if (returnedPos != null) {  // Node found (can be exactly or the next), return directly
                    return returnedPos;
                }
                // returnedPos is null, search the next value's node
                if (comparator.compare(value, currentPos.getElement()) > 0) {
                    return null;  // Go to parent node
                }
            }
            return currentPos;  // Found the next value's node
        } else if (comparator.compare(value, currentPos.getElement()) > 0) {  // Right
            if (binTree.hasRight(currentPos)) {
                // Go to right subtree
                Position<E> returnedPos = findNextPos(value, binTree.right(currentPos));
                if (returnedPos != null) {  // Node found (can be exactly or the next), return directly
                    return returnedPos;
                }
                // returnedPos is null, search the next value's node
                if (comparator.compare(value, currentPos.getElement()) > 0) {
                    return null;  // Go to parent node
                }
            }
            return currentPos;  // Found the next value's node
        } else if (comparator.compare(currentPos.getElement(), value) == 0) {  // Found the exactly node
            return currentPos;  // Return the exactly node
        }
        return null;  // Not found the exactly node, search the next value's node
    }

    public Position<E> first() throws RuntimeException {
        if (this.isEmpty()) {
            throw new RuntimeException("No first element.");
        }
        Position<E> pos = this.binTree.root();
        while (this.binTree.isInternal(pos)) {
            if (this.binTree.hasLeft(pos)) {
                pos = this.binTree.left(pos);
            }
        }
        if (pos.getElement() == null) {
            pos = this.binTree.parent(pos);
        }
        return pos;
    }

    public Position<E> last() throws RuntimeException {
        if (this.isEmpty()) {
            throw new RuntimeException("No last element.");
        }
        Position<E> pos = this.binTree.root();
        while (this.binTree.isInternal(pos)) {
            if (this.binTree.hasRight(pos)) {
                pos = this.binTree.right(pos);
            }
        }
        if (pos.getElement() == null) {
            pos = this.binTree.parent(pos);
        }
        return pos;
    }

    public Iterable<Position<E>> successors(Position<E> pos) {
        //TODO: Practica 5 Ejercicio 2
        throw new RuntimeException("Not yet implemented.");

    }

    public Iterable<Position<E>> predecessors(Position<E> pos) {
        //TODO: Practica 5 Ejercicio 2
        throw new RuntimeException("Not yet implemented.");
    }

}


class ReestructurableBinaryTree<T> extends LinkedBinaryTree<T> {

    public ReestructurableBinaryTree() {
        this.addRoot(null);
    }

    /**
     * Performs a tri-node restructuring. Assumes the nodes are in one of
     * following configurations:
     *
     * <pre>
     *          z=c       z=c        z=a         z=a
     *         /  \      /  \       /  \        /  \
     *       y=b  t4   y=a  t4    t1  y=c     t1  y=b
     *      /  \      /  \           /  \         /  \
     *    x=a  t3    t1 x=b        x=b  t4       t2 x=c
     *   /  \          /  \       /  \             /  \
     *  t1  t2        t2  t3     t2  t3           t3  t4
     * </pre>
     *
     * @return the new root of the restructured subtree
     */
    public Position<T> restructure(Position<T> posNode, LinkedBinarySearchTree<T> bst) {
        BTNode<T> lowKey, midKey, highKey, t1, t2, t3, t4;
        Position<T> posParent = bst.binTree.parent(posNode); // assumes x has a parent
        Position<T> posGrandParent = bst.binTree.parent(posParent); // assumes y has a parent
        boolean nodeLeft = (posNode == bst.binTree.left(posParent));
        boolean parentLeft = (posParent == bst.binTree.left(posGrandParent));
        BTNode<T> node = (BTNode<T>) posNode, parent = (BTNode<T>) posParent, grandParent = (BTNode<T>) posGrandParent;
        if (nodeLeft && parentLeft) { // Desequilibrio izda-izda
            lowKey = node;
            midKey = parent;
            highKey = grandParent;
            t1 = lowKey.getLeft();
            t2 = lowKey.getRight();
            t3 = midKey.getRight();
            t4 = highKey.getRight();
        } else if (!nodeLeft && parentLeft) { // Desequilibrio izda-dcha
            lowKey = parent;
            midKey = node;
            highKey = grandParent;
            t1 = lowKey.getLeft();
            t2 = midKey.getLeft();
            t3 = midKey.getRight();
            t4 = highKey.getRight();
        } else if (nodeLeft && !parentLeft) { // Desequilibrio dcha-izda
            lowKey = grandParent;
            midKey = node;
            highKey = parent;
            t1 = lowKey.getLeft();
            t2 = midKey.getLeft();
            t3 = midKey.getRight();
            t4 = highKey.getRight();
        } else { // Desequilibrio dcha-dcha
            lowKey = grandParent;
            midKey = parent;
            highKey = node;
            t1 = lowKey.getLeft();
            t2 = midKey.getLeft();
            t3 = highKey.getLeft();
            t4 = highKey.getRight();
        }

        // put b at z's place
        if (bst.binTree.isRoot(posGrandParent)) {
            bst.binTree = (LinkedBinaryTree<T>) bst.binTree.subTree(midKey); // FIXED: bad practice...
        } else {
            BTNode<T> zParent = (BTNode<T>) bst.binTree.parent(posGrandParent);
            if (posGrandParent == bst.binTree.left(zParent)) {
                midKey.setParent(zParent);
                zParent.setLeft(midKey);
            } else { // z was a right child
                midKey.setParent(zParent);
                zParent.setRight(midKey);
            }
        }
        // place the rest of the nodes and their children
        midKey.setLeft(lowKey);
        lowKey.setParent(midKey);
        midKey.setRight(highKey);
        highKey.setParent(midKey);
        lowKey.setLeft(t1);
        t1.setParent(lowKey);
        lowKey.setRight(t2);
        t2.setParent(lowKey);
        highKey.setLeft(t3);
        t3.setParent(highKey);
        highKey.setRight(t4);
        t4.setParent(highKey);

        return midKey; // the new root of this subtree
    }

}
