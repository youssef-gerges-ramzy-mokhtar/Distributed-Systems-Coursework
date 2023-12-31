import java.io.*;

public class Auction implements java.io.Serializable {
	private int auctionId;
	private int reservePrice;
	private int startingPrice;
	private int curPrice;
	private String description;
	private AuctionItem item;
	private String bidderName;
	private String bidderEmail;
	private boolean reserveHidden;
	private static IDGenerator idGenerator = new IDGenerator();

	public Auction(int reservePrice, int startingPrice, String description, AuctionItem item) throws Exception {
		if (startingPrice <= 0)
			throw new Exception("Cannot Have a Negative Starting Price");
		if (reservePrice <= 0)
			throw new Exception("Cannot Have a Negative Reserve Price");

		this.reservePrice = reservePrice;
		this.startingPrice = startingPrice;
		this.description = description;
		this.item = item;
		this.curPrice = startingPrice;
		this.reserveHidden = false;

		this.bidderName = this.bidderEmail = "";
		this.auctionId = idGenerator.generateId();
	}

	public int getAuctionId() {
		return auctionId;
	}

	public boolean reserveReached() {
		return curPrice > reservePrice;
	}

	public String getItemSold() {
		return this.item.getItemName();
	}

	public void print() {
		System.out.println("{");
		System.out.println("\tauctionId: " + auctionId);
		
		if (!this.reserveHidden)
			System.out.println("\treservePrice: " + reservePrice);
		
		System.out.println("\tstartingPrice: " + startingPrice);	
		System.out.println("\tcurPrice: " + curPrice);	
		System.out.println("\tdescription: " + description);	
	
		if (!bidderName.equals(""))
			System.out.println("\tbidderName: " + bidderName);
		
		if (!bidderEmail.equals(""))
			System.out.println("\tbidderEmail: " + bidderEmail);

		System.out.println("\titem: {");
		item.print();
		System.out.println("\t}");
	
		System.out.println("}");	
	}

	public void bid(String bidderName, String bidderEmail, int biddingPrice) throws Exception {
		if (biddingPrice <= 0) 
			throw new Exception("Cannot Bid with a Negative Price");
		if (biddingPrice <= curPrice)
			throw new Exception("Cannot Bid with a Value less than the current Bid Price");	

		curPrice = biddingPrice;
		this.bidderName = bidderName;
		this.bidderEmail = bidderEmail;
	}

	public void hideReservePrice(boolean reserveHidden) {
		this.reserveHidden = reserveHidden;
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