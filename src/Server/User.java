import java.util.*;

public class User implements java.io.Serializable {
	private AuctionItemHandler auctionItemHandler;
	private NormalAuctionHandler normalAuctionHandler;
	private DoubleAuctionHandler doubleAuctionHandler;

	public User() {
		auctionItemHandler = new AuctionItemHandler();
		normalAuctionHandler = new NormalAuctionHandler(auctionItemHandler);
		doubleAuctionHandler = new DoubleAuctionHandler(auctionItemHandler);
	}

	// Auction Item Methods (Should just return the AuctionItemHandler) //
	public AuctionItemHandler getAuctionItemHandler() {
		return this.auctionItemHandler;
	}
	
	public NormalAuctionHandler getNormalAuctionHandler() {
		return this.normalAuctionHandler;
	}	

	public DoubleAuctionHandler getDoubleAuctionHandler() {
		return this.doubleAuctionHandler;
	}
}