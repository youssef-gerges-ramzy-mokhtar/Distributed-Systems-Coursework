import java.util.*;

class DoubleAuctionHandler implements java.io.Serializable {
	private HashMap<Integer, DoubleAuction> doubleAuctions;
	private HashSet<Integer> doubleAuctionBidPlaced;
	private AuctionItemHandler auctionItemHandler;

	public DoubleAuctionHandler(AuctionItemHandler auctionItemHandler) {
		this.doubleAuctions = new HashMap<Integer, DoubleAuction>();
		this.doubleAuctionBidPlaced = new HashSet<Integer>();
		this.auctionItemHandler = auctionItemHandler;
	}	

	public ArrayList<DoubleAuction> getDoubleAuctions() throws Exception {
		return new ArrayList<DoubleAuction>(this.doubleAuctions.values());
	}
	public DoubleAuction getDoubleAuction(int auctionId) throws Exception {
		if (!doubleAuctions.containsKey(auctionId))
			throw new Error("Double Auction ID Not Found");

		return doubleAuctions.get(auctionId);
	}
	public int createDoubleAuction(int itemId, String description, int buyersSize, int sellersSize) throws Exception {
		try {
			AuctionItem item = auctionItemHandler.getSpec(itemId);
			DoubleAuction doubleAuction = new DoubleAuction(description, item, buyersSize, sellersSize);
			doubleAuctions.put(doubleAuction.getAuctionId(), doubleAuction);

			System.out.println(doubleAuction.getAuctionStatus());
			return doubleAuction.getAuctionId();
		} catch (Exception error) {
			throw error;
		}
	}
	public String buyDoubleAuction(int auctionId, int buyingPrice, DoubleAuctionHandler bidder) throws Exception {
  		try {
  			DoubleAuction doubleAuction = getAuction(auctionId, doubleAuctions);

  			if (bidder.doubleAuctionBidPlaced(auctionId))
  				throw new Exception("You have already placed a bid on this Double Auction");
  			
  			doubleAuction.addBuyingPrice(buyingPrice);
  			bidder.setDoubleAuctionBidPlaced(auctionId);

  			return doubleAuction.getAuctionStatus();
  		} catch (Exception error) {
  			throw error;
  		}
  	}
  	public String sellDoubleAuction(int auctionId, int sellingPrice, DoubleAuctionHandler bidder) throws Exception {
  		try {
  			DoubleAuction doubleAuction = getAuction(auctionId, doubleAuctions);

  			if (bidder.doubleAuctionBidPlaced(auctionId))
  				throw new Exception("You have already placed a bid on this Double Auction");

  			doubleAuction.addSellingPrice(sellingPrice);
  			bidder.setDoubleAuctionBidPlaced(auctionId);

  			return doubleAuction.getAuctionStatus();
  		} catch (Exception error) {
  			throw error;
  		}
  	}
  	public boolean doubleAuctionExist(int auctionId) {
  		return doubleAuctions.containsKey(auctionId);
  	}
  	public boolean doubleAuctionBidPlaced(int auctionId) {
  		return doubleAuctionBidPlaced.contains(auctionId);
  	}
  	public void setDoubleAuctionBidPlaced(int auctionId) {
  		doubleAuctionBidPlaced.add(auctionId);
  	}

  	// Helpoer Methods
	private <T> T getAuction(int auctionId, HashMap<Integer, T> auctionMap) throws Exception {
		if (!auctionMap.containsKey(auctionId))
			throw new Error("Auction ID Not Found, Or doesn't belong to this user");

		return auctionMap.get(auctionId);
	}
}