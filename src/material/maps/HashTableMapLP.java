package material.maps;

/**
 * @param <K> The key
 * @param <V> The stored value
 * @author vlt23
 */
public class HashTableMapLP<K, V> extends AbstractHashTableMap<K, V> {

    public HashTableMapLP(int size) {
        super(size);
    }

    /**
     * Creates a hash table with prime factor 109345121 and capacity 1000.
     */
    public HashTableMapLP() {
        super();
    }

    public HashTableMapLP(int p, int cap) {
        super(p, cap);
    }

    @Override
    protected int offset(K key, int i) {
        return i;
    }

}
