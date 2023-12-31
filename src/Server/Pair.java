public class Pair<K,V> implements java.io.Serializable {
	public K first;
	public V second;

	public Pair(K first, V second) {
		this.first = first;
		this.second = second;
	}
}