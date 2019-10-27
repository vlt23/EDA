package material.tree.binarytree;

import material.Position;
import material.tree.iterators.InorderBinaryTreeIterator;

import java.util.Iterator;

public class ArrayBinaryTree<E> implements BinaryTree<E> {

    private static class BTPos<T> implements Position<T> {
        private T element;
        private int position;

        public T getElement() {
            return element;
        }

        private BTPos(T element, int position) {
            this.element = element;
            this.position = position;
        }
    }

    private int size;
    private BTPos<E>[] elements;

    @SuppressWarnings("unchecked")
    public ArrayBinaryTree() {
        elements = new BTPos[1024];
        size = 0;
    }

    @Override
    public Position<E> left(Position<E> v) throws RuntimeException {
        BTPos<E> btPos = checkPosition(v);
        BTPos<E> left = elements[btPos.position * 2];
        if (left == null) {
            throw new RuntimeException("No left child");
        }
        return left;
    }

    @Override
    public Position<E> right(Position<E> v) throws RuntimeException {
        BTPos<E> btPos = checkPosition(v);
        BTPos<E> right = elements[btPos.position * 2 + 1];
        if (right == null) {
            throw new RuntimeException("No right child");
        }
        return right;
    }

    @Override
    public boolean hasLeft(Position<E> v) {
        BTPos<E> parent = checkPosition(v);
        return elements[parent.position * 2] != null;
    }

    @Override
    public boolean hasRight(Position<E> v) {
        BTPos<E> parent = checkPosition(v);
        return elements[parent.position * 2 + 1] != null;
    }

    @Override
    public E replace(Position<E> p, E e) {
        BTPos<E> btPos = checkPosition(p);
        E oldE = btPos.element;
        btPos.element = e;
        return oldE;
    }

    @Override
    public Position<E> sibling(Position<E> p) throws RuntimeException {
        BTPos<E> btPos = checkPosition(p);
        BTPos<E> parent = elements[btPos.position / 2];
        if (parent == null) {
            throw new RuntimeException("No sibling");
        }
        if (this.right(parent) != null && this.right(parent) != btPos) {
            return this.right(parent);
        } else if (this.left(parent) != null && this.left(parent) != btPos) {
            return this.left(parent);
        }
        throw new RuntimeException("No sibling");
    }

    @Override
    public Position<E> insertLeft(Position<E> p, E e) throws RuntimeException {
        BTPos<E> parent = checkPosition(p);
        int leftChildPos = parent.position * 2;
        if (elements[leftChildPos] != null) {
            throw new RuntimeException("Node already has a left child");
        }
        elements[leftChildPos] = new BTPos<>(e, leftChildPos);
        size++;
        return elements[leftChildPos];
    }

    @Override
    public Position<E> insertRight(Position<E> p, E e) throws RuntimeException {
        BTPos<E> parent = checkPosition(p);
        int rightChildPos = parent.position * 2 + 1;
        if (elements[rightChildPos] != null) {
            throw new RuntimeException("Node already has a right child");
        }
        elements[rightChildPos] = new BTPos<>(e, rightChildPos);
        size++;
        return elements[rightChildPos];
    }

    @Override
    public E remove(Position<E> p) throws RuntimeException {
        return null;
    }

    @Override
    public void swap(Position<E> p1, Position<E> p2) {

    }

    @Override
    public BinaryTree<E> subTree(Position<E> v) {
        return null;
    }

    @Override
    public void attachLeft(Position<E> p, BinaryTree<E> tree) throws RuntimeException {

    }

    @Override
    public void attachRight(Position<E> p, BinaryTree<E> tree) throws RuntimeException {

    }

    @Override
    public boolean isComplete() {
        InorderBinaryTreeIterator<E> iterator = new InorderBinaryTreeIterator<>(this);
        while (iterator.hasNext()) {
            Position<E> p = iterator.next();
            if (this.isInternal(p)) {
                if (!this.hasLeft(p) || !this.hasRight(p)) {
                    return false;
                }
            }
        }
        return true;
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
        return elements[1];
    }

    @Override
    public Position<E> parent(Position<E> v) throws RuntimeException {
        BTPos<E> child = checkPosition(v);
        BTPos<E> parent = elements[child.position / 2];
        if (parent == null) {
            throw new RuntimeException("No parent");
        }
        return parent;
    }

    @Override
    public Iterable<? extends Position<E>> children(Position<E> v) {
        return null;
    }

    @Override
    public boolean isInternal(Position<E> v) {
        return this.hasLeft(v) || this.hasRight(v);
    }

    @Override
    public boolean isLeaf(Position<E> v) throws RuntimeException {
        return !this.isInternal(v);
    }

    @Override
    public boolean isRoot(Position<E> v) {
        BTPos<E> btPos = checkPosition(v);
        return btPos == elements[1];
    }

    @Override
    public Position<E> addRoot(E e) throws RuntimeException {
        if (!isEmpty()) {
            throw new RuntimeException("Tree already has a root");
        }
        elements[1] = new BTPos<>(e, 1);
        size = 1;
        return elements[1];
    }

    @Override
    public Iterator<Position<E>> iterator() {
        return new InorderBinaryTreeIterator<>(this);
    }

    private BTPos<E> checkPosition(Position<E> p) {
        if (!(p instanceof BTPos)) {
            throw new RuntimeException("The position is invalid");
        }
        return (BTPos<E>) p;
    }

}
