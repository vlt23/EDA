package material.tree.iterators;

import material.Position;
import material.tree.Tree;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.Predicate;

/**
 * Generic preorder iterator for trees.
 *
 * @param <E>
 * @author A. Duarte, J. Vélez, J. Sánchez-Oro, JD. Quintana
 * @author vlt23
 */

public class PreorderIterator<E> implements Iterator<Position<E>> {

    private Deque<Position<E>> nodeStack;
    private final Tree<E> tree;
    private Predicate<Position<E>> predicate;

    public PreorderIterator(Tree<E> tree) {
        nodeStack = new LinkedList<>();
        this.tree = tree;
        nodeStack.addFirst(tree.root());
    }

    public PreorderIterator(Tree<E> tree, Position<E> start) {
        nodeStack = new LinkedList<>();
        this.tree = tree;
        nodeStack.addFirst(start);
    }

    public PreorderIterator(Tree<E> tree, Position<E> start, Predicate<Position<E>> predicate) {
        nodeStack = new LinkedList<>();
        this.tree = tree;
        this.predicate = predicate;
        nodeStack.addFirst(start);
    }

    private void _preOrder(Position<E> p) {
        Iterable<Position<E>> childrenI = (Iterable<Position<E>>) tree.children(p);
        Deque<Position<E>> children = new LinkedList<>();
        for (Position<E> child : childrenI) {
            children.addFirst(child);
        }
        while (!children.isEmpty()) {
            nodeStack.addFirst(children.removeFirst());
        }
    }

    @Override
    public boolean hasNext() {
        return !nodeStack.isEmpty();
    }

    @Override
    public Position<E> next() {
        Position<E> aux = nodeStack.removeFirst();
        _preOrder(aux);
        // If predicate test not passing, ignore current position and pick the next
        while (predicate != null && !predicate.test(aux)) {
            aux = nodeStack.removeFirst();
            _preOrder(aux);
            if (predicate.test(aux)) {
                return aux;
            }
        }
        return aux;
    }

}
