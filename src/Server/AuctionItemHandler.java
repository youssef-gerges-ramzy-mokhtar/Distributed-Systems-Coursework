import java.util.*;

public class AuctionItemHandler implements java.io.Serializable {
	private HashMap<Integer, AuctionItem> auctionItems;

	public AuctionItemHandler() {
		auctionItems = new HashMap<Integer, AuctionItem>();

		String[] itemDescriptions = {"Samsung M31", "Iphone Pro Max"};
		Boolean[] itemCondition = {true, false};
		for (int i = 0; i < itemDescriptions.length; i++) {
			AuctionItem item = new AuctionItem("Phone", itemDescriptions[i], itemCondition[i]);
			this.auctionItems.put(item.getItemId(), item);
		}
	}

	public AuctionItem getSpec(int itemId) throws Exception {
		if (!auctionItems.containsKey(itemId))
			throw new Exception("User don't have this Item ID");
	
		return auctionItems.get(itemId);
	}

	public int createAuctionItem(String itemTitle, String itemDescription, boolean itemCondition) {
		AuctionItem auctionItem = new AuctionItem(itemTitle, itemDescription, itemCondition);
		auctionItems.put(auctionItem.getItemId(), auctionItem);

		return auctionItem.getItemId();
	}

	public ArrayList<AuctionItem> getAuctionItems() {
		return new ArrayList<AuctionItem>(auctionItems.values());
	}
}