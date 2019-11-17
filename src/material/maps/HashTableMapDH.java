package material.maps;

/**
 * @param <K> The key
 * @param <V> The stored value
 */
public class HashTableMapDH<K, V> extends AbstractHashTableMap<K, V> {

    public HashTableMapDH(int size) {
        super(size);
    }

    public HashTableMapDH() {
        super();
    }

    public HashTableMapDH(int p, int cap) {
        super(p, cap);
    }

    @Override
    protected int offset(K key, int i) {
        int prime = findPrimeMinorThatN();
        return (prime - (key.hashCode() % prime)) * i;
    }

    private int findPrimeMinorThatN() {
        int[] somePrimes = {997, 523, 223, 73, 23, 7, 3};
        int i = 0;
        while (super.capacity < somePrimes[i]) {
            i++;
        }
        return somePrimes[i];
    }
}
