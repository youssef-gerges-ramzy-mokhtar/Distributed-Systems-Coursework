import java.util.*;

class NormalAuctionHandler implements java.io.Serializable {
	private HashMap<Integer, Auction> normalAuctions;
	private AuctionItemHandler auctionItemHandler;
		
	public NormalAuctionHandler(AuctionItemHandler auctionItemHandler) {
		this.normalAuctions = new HashMap<Integer, Auction>();
		this.auctionItemHandler = auctionItemHandler;
	}

	public int createAuction(int itemId, int startingPrice, String description, int reservePrice) throws Exception {
		try {
			AuctionItem item = auctionItemHandler.getSpec(itemId);
			Auction auction = new Auction(reservePrice, startingPrice, description, item);
			normalAuctions.put(auction.getAuctionId(), auction);
		
			auction.print();
			return auction.getAuctionId();
		} catch (Exception error) {
			throw error;
		}
	}
	public Auction closeAuction(int auctionId) throws Exception {
		try {
			Auction auction = getAuction(auctionId, normalAuctions);
			auction.hideReservePrice(false);
			
			return normalAuctions.remove(auctionId);
		} catch (Exception error) {
			throw error;
		}
	}
	public ArrayList<Auction> getAuctions(boolean hideReserve) {
		ArrayList<Auction> auctions = new ArrayList<Auction>(this.normalAuctions.values());
		for (Auction auction: auctions)
			auction.hideReservePrice(hideReserve);

		return auctions;
	}
	public ArrayList<Auction> getAuctions(String itemName, boolean hideReserve) {
		ArrayList<Auction> auctionsList = new ArrayList<Auction>();
		for (Auction auction: normalAuctions.values()) {
			if (auction.getItemSold().equals(itemName)) {
				auction.hideReservePrice(hideReserve); 
				auctionsList.add(auction);
			}
		}

		return auctionsList;
	}
	public String bid(int auctionId, String name, String email, int biddingPrice) throws Exception {
		try {
			Auction auction = getAuction(auctionId, normalAuctions);
	  		auction.bid(name, email, biddingPrice);
  			return "Bid went to " + name + ": " + biddingPrice;			
		} catch (Exception error) {
			throw error;
		}
	}
	public boolean auctionExist(int auctionId) {
		return normalAuctions.containsKey(auctionId);
	}

	// Helpoer Methods
	private <T> T getAuction(int auctionId, HashMap<Integer, T> auctionMap) throws Exception {
		if (!auctionMap.containsKey(auctionId))
			throw new Error("Auction ID Not Found, Or doesn't belong to this user");

		return auctionMap.get(auctionId);
	}
}