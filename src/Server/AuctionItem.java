import java.io.*;

public class AuctionItem implements java.io.Serializable {
	private int itemId;
	public String itemTitle;
	public String itemDescription;
	public boolean itemCondition;
	private static IDGenerator idGenerator = new IDGenerator();

	public AuctionItem(String itemTitle, String itemDescription, boolean itemCondition) {
		this.itemTitle = itemTitle;
		this.itemDescription = itemDescription;
		this.itemCondition = itemCondition;

		this.itemId = idGenerator.generateId();
	}

	public int getItemId() {
		return this.itemId;
	}

	public String getItemName() {
		return this.itemTitle;
	}

	public void print() {
		System.out.println("\t\t" + itemId);
		System.out.println("\t\t" + itemTitle);
		System.out.println("\t\t" + itemDescription);
		System.out.println("\t\t" + itemCondition);
	}

	// Custom serialization methods
    private void writeObject(ObjectOutputStream out) throws Exception {
        out.defaultWriteObject(); // Serialize the non-static fields
        out.writeObject(idGenerator); // Serialize the static variable
    }

    // Custom deserialization methods
    private void readObject(ObjectInputStream in) throws Exception {
        in.defaultReadObject(); // Deserialize the non-static fields
        idGenerator = (IDGenerator) in.readObject(); // Deserialize the static variable
    }
}