package material.maps;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Separate chaining table implementation of hash tables. Note that all
 * "matching" is based on the equals method.
 *
 * @author A. Duarte, J. Vélez, J. Sánchez-Oro, JD. Quintana, vlt23
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

        private int index;  // the bucket array index
        private int pos;  // the list pos
        private List<HashEntry<T, U>>[] bucket;

        public HashTableMapIterator(List<HashEntry<T, U>>[] map, int numElems) {
            this.bucket = map;
            this.pos = 0;
            if (numElems == 0) {
                this.index = bucket.length;
            } else {
                this.index = 0;
                goToNextElement();
            }
        }

        private void goToNextElement() {
            if (bucket[index] != null && this.pos < bucket[index].size() - 1) {
                this.pos++;
            } else {
                this.pos = 0;
                this.index++;
                while (this.index < bucket.length && this.bucket[index] == null) {
                    this.index++;
                }
            }
        }

        @Override
        public boolean hasNext() {
            return this.index < this.bucket.length;
        }

        @Override
        public Entry<T, U> next() {
            if (hasNext()) {
                Entry<T, U> toReturn = this.bucket[index].get(pos);
                goToNextElement();
                return toReturn;
            }
            throw new IllegalStateException("The map has not more elements");
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not implemented.");
        }
    }

    private static class HashTableMapKeyIterator<T, U> implements Iterator<T> {

        private HashTableMapIterator<T, U> it;

        public HashTableMapKeyIterator(HashTableMapIterator<T, U> it) {
            this.it = it;
        }

        @Override
        public T next() {
            return it.next().getKey();
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not implemented.");
        }
    }

    private static class HashTableMapValueIterator<T, U> implements Iterator<U> {

        private HashTableMapIterator<T, U> it;

        public HashTableMapValueIterator(HashTableMapIterator<T, U> it) {
            this.it = it;
        }

        @Override
        public U next() {
            return it.next().getValue();
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not implemented.");
        }
    }

    private int n;
    private int prime, capacity;
    private long scale, shift;
    private List<HashEntry<K, V>>[] bucket;

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
            // setValue return the old value
            return bucket[index].get(pos).setValue(value);
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
            if (bucket[index].get(i).getKey().equals(key)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new HashTableMapIterator<>(this.bucket, this.n);
    }

    @Override
    public Iterable<K> keys() {
        return () -> new HashTableMapKeyIterator<>(new HashTableMapIterator<>(bucket, n));
    }

    @Override
    public Iterable<V> values() {
        return () -> new HashTableMapValueIterator<>(new HashTableMapIterator<>(bucket, n));
    }

    @Override
    public Iterable<Entry<K, V>> entries() {
        return () -> new HashTableMapIterator<>(bucket, n);
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
        List<HashEntry<K, V>>[] old = bucket;
        bucket = (ArrayList<HashEntry<K, V>>[]) new ArrayList[capacity];
        Random rand = new Random();
        scale = rand.nextInt(prime - 1) + 1;
        shift = rand.nextInt(prime);
        for (List<HashEntry<K, V>> entryList : old) {
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
