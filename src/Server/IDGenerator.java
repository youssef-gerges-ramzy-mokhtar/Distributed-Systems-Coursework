public class IDGenerator implements java.io.Serializable {
	private int id;

	public IDGenerator() {
		this.id = 0;
	}

	int generateId() {
		int oldId = id;
		++id;
		return oldId;
	}
}
