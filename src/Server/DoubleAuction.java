import java.util.*;
import java.io.*;

public class DoubleAuction implements java.io.Serializable {
	private int auctionId;
	private String description;
	private AuctionItem item;
	private Integer buyers[];
	private Integer sellers[];
	private int buyerIdx;
	private int sellerIdx;
	private static IDGenerator idGenerator = new IDGenerator();

	public DoubleAuction(String description, AuctionItem item, int buyersSize, int sellersSize) throws Exception {
		if (buyersSize <= 0)
			throw new Exception("The Number of Buyers Cannot be less than or equal Zero");
		if (sellersSize <= 0)
			throw new Exception("The Number of Sellers Cannot be less than or equal Zero");

		this.description = description;
		this.item = item;
		this.auctionId = idGenerator.generateId();

		this.buyers = new Integer[buyersSize];
		this.sellers = new Integer[sellersSize];

		this.buyerIdx = this.sellerIdx = 0;
	}

	private boolean auctionFinished() {
		return (this.buyerIdx >= buyers.length && this.sellerIdx >= sellers.length);
	}

	public String getAuctionStatus() {
		String auctionInfo = "Double Auction with ID = " + auctionId;
		auctionInfo += " (" + getRemainingSellers() + " Reamining Sellers";
		auctionInfo += ", " + getRemainingBuyers() + " Reamining Buyers)";

		if (!auctionFinished())
			return auctionInfo + " Still Running";
		else
			auctionInfo += " Finished";

		Arrays.sort(sellers);
		Arrays.sort(buyers, Collections.reverseOrder());

		int totalProfit = 0;
		for (int i = 0; i < Math.min(sellers.length, buyers.length); i++) {
			if (buyers[i] < sellers[i])
				break;

			totalProfit += buyers[i] - sellers[i];
		}

		String sellerDetails = "\tSellers: " + Arrays.toString(this.sellers);
		String buyerDetails = "\tBuyers: " + Arrays.toString(this.buyers);
		String profitDetails = "\tTotal Profit: " + Integer.toString(totalProfit);
		return auctionInfo + "\n" + sellerDetails + "\n" + buyerDetails + "\n" + profitDetails;
	}

	public void addSellingPrice(int sellingPrice) throws Exception {
		if (this.sellerIdx >= this.sellers.length)
			throw new Exception("Double Auction Sellers Finished");
		if (sellingPrice < 0)
			throw new Exception("selling price cannot be -ve");

		this.sellers[sellerIdx++] = sellingPrice;
	}

	public void addBuyingPrice(int buyingPrice) throws Exception {
		if (this.buyerIdx >= this.buyers.length)
			throw new Exception("Double Auction Buyers Finished");
		if (buyingPrice < 0)
			throw new Exception("buying price cannot be -ve");

		this.buyers[buyerIdx++] = buyingPrice;
	}

	public int getAuctionId() {
		return this.auctionId;
	}

	private int getRemainingSellers() {
		return sellers.length - sellerIdx;
	}

	private int getRemainingBuyers() {
		return buyers.length - buyerIdx;
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