package material.maps;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Separate chaining table implementation of hash tables. Note that all
 * "matching" is based on the equals method.
 *
 * @author A. Duarte, J. Vélez, J. Sánchez-Oro, JD. Quintana
 */
public class HashTableMapSC<K, V> implements Map<K, V> {

    private static class HashEntry<T, U> implements Entry<T, U> {

        protected T key;
        protected U value;

        public HashEntry(T k, U v) {
            key = k;
            value = v;
        }

        @Override
        public U getValue() {
            return value;
        }

        @Override
        public T getKey() {
            return key;
        }

        public U setValue(U val) {
            U valueToReturn = value;
            value = val;
            return valueToReturn;
        }

        @Override
        public boolean equals(Object o) {
            if (o.getClass() != this.getClass()) {
                return false;
            }
            HashEntry<T, U> ent;
            try {
                ent = (HashEntry<T, U>) o;
            } catch (ClassCastException ex) {
                return false;
            }
            return (ent.getKey().equals(this.key)) && (ent.getValue().equals(this.value));
        }
    }

    private static class HashTableMapIterator<T, U> implements Iterator<Entry<T, U>> {

        public HashTableMapIterator(List<HashEntry<T, U>>[] map, int numElems) {
            throw new RuntimeException("Not yet implemented.");
        }

        @Override
        public boolean hasNext() {
            throw new RuntimeException("Not yet implemented.");
        }

        @Override
        public Entry<T, U> next() {
            throw new RuntimeException("Not yet implemented.");
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not implemented.");

        }
    }

    private static class HashTableMapKeyIterator<T, U> implements Iterator<T> {

        public HashTableMapKeyIterator(HashTableMapIterator<T, U> it) {
            throw new RuntimeException("Not yet implemented.");
        }

        @Override
        public T next() {
            throw new RuntimeException("Not yet implemented.");
        }

        @Override
        public boolean hasNext() {
            throw new RuntimeException("Not yet implemented.");
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not implemented.");
        }
    }

    private static class HashTableMapValueIterator<T, U> implements Iterator<U> {

        public HashTableMapValueIterator(HashTableMapIterator<T, U> it) {
            throw new RuntimeException("Not yet implemented.");
        }

        @Override
        public U next() {
            throw new RuntimeException("Not yet implemented.");
        }

        @Override
        public boolean hasNext() {
            throw new RuntimeException("Not yet implemented.");
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not implemented.");
        }
    }

    private int n;
    private int prime, capacity;
    private long scale, shift;
    private ArrayList<HashEntry<K, V>>[] bucket;

    /**
     * Creates a hash table with prime factor 109345121 and capacity 1000.
     */
    public HashTableMapSC() {
        this(109345121, 1000);
    }

    /**
     * Creates a hash table with prime factor 109345121 and given capacity.
     *
     * @param cap initial capacity
     */
    public HashTableMapSC(int cap) {
        this(109345121, cap);
    }

    /**
     * Creates a hash table with the given prime factor and capacity.
     *
     * @param p   prime number
     * @param cap initial capacity
     */
    public HashTableMapSC(int p, int cap) {
        this.prime = p;
        this.capacity = cap;
        this.n = 0;
        this.bucket = (ArrayList<HashEntry<K, V>>[]) new ArrayList[capacity];
        Random rand = new Random();
        this.scale = rand.nextInt(prime - 1) + 1;
        this.shift = rand.nextInt(prime);
    }

    /**
     * Hash function applying MAD method to default hash code.
     *
     * @param key Key
     * @return the hash value
     */
    protected int hashValue(K key) {
        return (int) ((Math.abs(key.hashCode() * scale + shift) % prime) % capacity);
    }


    @Override
    public int size() {
        return n;
    }

    @Override
    public boolean isEmpty() {
        return n == 0;
    }

    @Override
    public V get(K key) {
        checkKey(key);
        int index = hashValue(key);
        int pos = findKey(index, key);
        if (pos != -1) {
            return bucket[index].get(pos).getValue();
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        checkKey(key);
        int index = hashValue(key);
        int pos = findKey(index, key);
        if (pos != -1) {
            V valueToReturn = bucket[index].get(pos).getValue();
            bucket[index].get(pos).setValue(value);
            return valueToReturn;
        }
        if (n >= capacity / 2) {
            rehash(capacity * 2);
            index = hashValue(key);
            if (bucket[index] == null) {
                bucket[index] = new ArrayList<>();
            }
        }
        bucket[index].add(new HashEntry<>(key, value));
        n++;
        return null;
    }

    @Override
    public V remove(K key) {
        checkKey(key);
        int index = hashValue(key);
        int pos = findKey(index, key);
        if (pos != -1) {
            V valueToReturn = bucket[index].get(pos).getValue();
            bucket[index].remove(pos);
            n--;
            return valueToReturn;
        }
        return null;
    }

    private int findKey(int index, K key) {
        if (bucket[index] == null) {
            bucket[index] = new ArrayList<>();
        }
        for (int i = 0; i < bucket[index].size(); i++) {
            if (bucket[index].get(i).getKey() == key) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        throw new RuntimeException("Not yet implemented.");
    }

    @Override
    public Iterable<K> keys() {
        throw new RuntimeException("Not yet implemented.");
    }

    @Override
    public Iterable<V> values() {
        throw new RuntimeException("Not yet implemented.");
    }

    @Override
    public Iterable<Entry<K, V>> entries() {
        throw new RuntimeException("Not yet implemented.");
    }

    /**
     * Determines whether a key is valid.
     *
     * @param k Key
     */
    protected void checkKey(K k) {
        if (k == null) {
            throw new IllegalStateException("Invalid key: null.");
        }
    }

    /**
     * Increase/reduce the size of the hash table and rehashes all the entries.
     */
    protected void rehash(int newCap) {
        if (newCap < this.size() * 2) {
            return;
        }
        capacity = newCap;
        ArrayList<HashEntry<K, V>>[] old = bucket;
        bucket = (ArrayList<HashEntry<K,V>>[]) new ArrayList[capacity];
        Random rand = new Random();
        scale = rand.nextInt(prime - 1) + 1;
        shift = rand.nextInt(prime);
        for (ArrayList<HashEntry<K, V>> entryList : old) {
            if (entryList != null) {
                for (HashEntry<K, V> entry : entryList) {
                    int index = hashValue(entry.getKey());
                    if (bucket[index] == null) {
                        bucket[index] = new ArrayList<>();
                    }
                    bucket[index].add(entry);
                }
            }
        }
    }

}
