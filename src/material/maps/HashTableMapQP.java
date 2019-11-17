package material.maps;
/**
 * @param <K> The key
 * @param <V> The stored value
     */
public class HashTableMapQP<K, V> extends AbstractHashTableMap<K, V> {

    public HashTableMapQP(int size) {
        super(size);
    }

    public HashTableMapQP() {
        super();
    }

    public HashTableMapQP(int p, int cap) {
        super(p,cap);
    }

    @Override
    protected int offset(K key, int i) {
        int c1 = 7;
        int c2 = 11;
        return c1 * i + c2 * (int) Math.pow(i, 2);
    }

}
