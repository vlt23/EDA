package material.linear;

/**
 * head --> first element (if empty, first empty position = 0)
 * tail --> last element + 1 (first empty position)
 * @author vlin2016
 * @param <E>
 */
public class ArrayQueue<E> implements Queue<E> {

    private E[] elements;
    private int size;
    private int head, tail;
    private int maxCapacity;

    @SuppressWarnings("unchecked")
    public ArrayQueue() {
        elements = (E[]) new Object[16];  // default maxCapacity = 16
        size = 0;
        head = 0;
        tail = 0;
        maxCapacity = 16;
    }

    @SuppressWarnings("unchecked")
    public ArrayQueue(int capacity) {
        elements = (E[]) new Object[capacity];
        size = 0;
        head = 0;
        tail = 0;
        maxCapacity = capacity;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public E front() {
        if (this.isEmpty()) {
            throw new RuntimeException("Queue is empty");
        }
        return this.elements[head];
    }

    @SuppressWarnings("unchecked")
    @Override
    public void enqueue(E element) {
        if (this.size == this.maxCapacity) {
            E[] duplicateSize = (E[]) new Object[this.maxCapacity * 2];
            for (int i = 0; i < this.maxCapacity; i++) {
                duplicateSize[i] = this.elements[(head + i) % maxCapacity];
            }
            this.elements = duplicateSize;
            this.head = 0;
            this.tail = maxCapacity;
            this.maxCapacity = this.maxCapacity * 2;
        }
        if (this.isEmpty()) {
            this.head = 0;
            this.tail = 0;
        }
        this.elements[this.tail] = element;
        this.tail = (this.tail + 1) % this.maxCapacity;
        this.size++;
    }

    @Override
    public E dequeue() {
        if (this.isEmpty()) {
            throw new RuntimeException("Queue is empty");
        }
        E aux = this.elements[head];
        this.head = (this.head + 1) % this.maxCapacity;
        this.size--;
        return aux;
    }

}
