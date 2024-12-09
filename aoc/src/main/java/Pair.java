public record Pair<K extends Comparable<K>, V>(K key, V value) implements Comparable<Pair<K, V>> {

    @Override
    public int compareTo(Pair<K, V> other) {
        return this.key.compareTo(other.key);
    }
}