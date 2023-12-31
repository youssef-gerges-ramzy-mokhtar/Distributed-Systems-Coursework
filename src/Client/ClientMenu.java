import java.util.*;

class ClientMenu {
	private AuctionApi auctionSystem;
	private Pair<UserInfo, String> user;
	private String email;
	private String password;
	private Scanner integerScanner;
	private Scanner textScanner;
	/*
		Here I am storing the Password at the Client/front-end that is very dangerous one solution which is out of the scope of this coursework
		(Maybe more Security Module), is that we can store the Password but encyrpted using the Server Public Key so the Password can only be
		decrypted by the Server Private Key which is sotred only on the Server. But anyways that is just a side not to consider.
	*/

	public ClientMenu(String email, String password) {
		this.email = email;
		this.password = password;
		this.integerScanner = new Scanner(System.in);
		this.textScanner = new Scanner(System.in);

		this.run();
	}

	private int menu() {		
		System.out.println("Choices: ");
		System.out.println("\t1: View Active Auctions");
		System.out.println("\t2: Bid");
		System.out.println("\t3. View Items you Created");
		System.out.println("\t4. View Item Specs");
		System.out.println("\t5. Add Item");
		System.out.println("\t6: Create Auction");
		System.out.println("\t7: Close Auction");
		System.out.println("\t8: Reverse Auction");
		System.out.println("\t9: View Double Auctions");
		System.out.println("\t10: View Single Double Auction");
		System.out.println("\t11: Create Double Auction");
		System.out.println("\t12: Add Buyer Bidding for Double Auction");
		System.out.println("\t13: Add Selling Bidding for Double Auction");
		System.out.println("\t14: Exit");

		System.out.print("Please select a choice: ");
		int choice = integerScanner.nextInt();

		return choice;		
	}

	private void run() {
		int choice = 1;
		while (true) {
			choice = menu();

			try {
				auctionSystem = Client.connectToServer();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

			if (choice == 1)
				viewActiveAuctions();
			else if (choice == 2)
				bid();
			else if (choice == 3)
				viewItems();
			else if (choice == 4)
				viewItemSpecs();
			else if (choice == 5)
				addItem();
			else if (choice == 6)
				createAuction();
			else if (choice == 7)
				closeAuction();
			else if (choice == 8)
				reverseAuction();
			else if (choice == 9)
				viewDoubleAuctions();
			else if (choice == 10)
				viewSingleDoubleAcution();
			else if (choice == 11)
				createDoubleAuction();
			else if (choice == 12)
				doubleAuctionBuyerBid();
			else if (choice == 13)
				doubleAuctionSellerBid();
			else if (choice == 14)
				break;
		}
	}

	// The following are all functions that handle every different kind of operation a client can perform from the frontend
	private void viewActiveAuctions() {
		try {
			this.displayAuctions(auctionSystem.getAuctions(email, password));
		} catch (Exception error) {
			System.out.println(error.getMessage());
		}
	}

	private void bid() {
		try {
			System.out.println("Please enter the following:");
			System.out.print("Auction ID: ");
			int auctionId = integerScanner.nextInt();

			System.out.print("Your bid: ");
			int biddingPrice = integerScanner.nextInt();

    		System.out.println(auctionSystem.bid(email, password, auctionId, biddingPrice) + "\n");
		} catch (Exception error) {
			System.out.println(error.getMessage());
		} 
	}

	private void viewItems() {
		try {
			ArrayList<AuctionItem> items = auctionSystem.getAuctionItems(email, password);
			for (AuctionItem item: items) {
				System.out.println("+++++++++++++++++++++");
				item.print();
			}

			System.out.println();
		} catch (Exception error) {
			System.out.println(error.getMessage());
		}
	}

	private void viewItemSpecs() {
		try {
			System.out.print("Item ID: ");
			int itemId = integerScanner.nextInt();

			AuctionItem item = auctionSystem.getItemSpec(email, password, itemId);
			item.print();
			System.out.println();
		} catch (Exception error) {
			System.out.println(error.getMessage());
		}
	}

	private void addItem() {
		try {
			System.out.print("Item Name: ");
			String itemName = textScanner.nextLine();

			System.out.print("Item Description: ");
			String itemDescription = textScanner.nextLine();

			System.out.print("Is Item New (1/0): ");
			int itemCondition = integerScanner.nextInt();

			int itemId = auctionSystem.createAuctionItem(email, password, itemName, itemDescription, itemCondition != 0);
			System.out.println("Item ID = " + itemId + "\n");
		} catch (Exception error) {
			System.out.println(error.getMessage());
		}
	}

	private void createAuction() {
		try {
			System.out.println("Please enter the following:");
			System.out.print("Item ID: ");
			int itemId = integerScanner.nextInt();	

			System.out.print("Start Price: ");
			int startPrice = integerScanner.nextInt();
	    	
	    	System.out.print("Auction Description: ");
			String description = textScanner.nextLine();

			System.out.print("Reserve Price: ");
			int reservePrice = integerScanner.nextInt();

			int auctionId = auctionSystem.createAuction(email, password, itemId, startPrice, description, reservePrice);
			System.out.println("Created Auction ID = " + auctionId + "\n");
		} catch (Exception error) {
			System.out.println(error.getMessage());
		}
	}

	private void closeAuction() {
		try {
			System.out.print("Please enter the Auction ID: ");
			int auctionId = integerScanner.nextInt();

			Auction auction = auctionSystem.closeAuction(email, password, auctionId);
			auction.print();

			if (!auction.reserveReached())
				System.out.println("Auction not Sold as Reserve Price not Reached");

			System.out.println();
		} catch (Exception error) {
			System.out.println(error.getMessage());
		}
	}

	private void reverseAuction() {
		try {
			System.out.print("Browse Auction with Item Name: ");
			String itemName = textScanner.nextLine();

			this.displayAuctions(auctionSystem.getAuctions(email, password, itemName));
		} catch (Exception error) {
			System.out.println(error.getMessage());
		}
	}

	private void displayAuctions(ArrayList<Auction> auctions) {
		if (auctions.size() == 0)
			System.out.println("No Active Auctions at the moment");

    	for (Auction auction: auctions)
    		auction.print();
    	System.out.println();
	}

	private void viewDoubleAuctions() {
		try {
			ArrayList<DoubleAuction> doubleAuctions = auctionSystem.getDoubleAuctions(email, password);
			if (doubleAuctions.size() == 0)
				System.out.println("No Active Double Auctions at the Moment");

			for (DoubleAuction doubleAuction: doubleAuctions)
				System.out.println(doubleAuction.getAuctionStatus());
		} catch (Exception error) {
			System.out.println(error.getMessage());
		}	
	}

	private void viewSingleDoubleAcution() {
		try {
			System.out.print("Double Auction ID: ");
			int auctionId = integerScanner.nextInt();

			DoubleAuction doubleAuction = auctionSystem.getDoubleAuction(email, password, auctionId);
			System.out.println(doubleAuction.getAuctionStatus());
		} catch (Exception error) {
			System.out.println(error.getMessage());
		}
	}

	private void createDoubleAuction() {
		try {
			System.out.print("Item ID: ");
			int itemId = integerScanner.nextInt();

			System.out.print("Description: ");
			String description = textScanner.nextLine();

			System.out.print("Amount of Buyers: ");
			int buyersAmount = integerScanner.nextInt();

			System.out.print("Amount of Sellers: ");
			int sellersAmount = integerScanner.nextInt();

			int auctionId = auctionSystem.createDoubleAuction(email, password, itemId, description, buyersAmount, sellersAmount);
			System.out.println("Double Auction Created with ID = " + auctionId);
		} catch (Exception error) {
			System.out.println(error.getMessage());
		}
	}

	private void doubleAuctionBuyerBid() {
		try {
			System.out.print("Double Auction ID: ");
			int auctionId = integerScanner.nextInt();

			System.out.print("Buying Bid: ");
			int bid = integerScanner.nextInt();

			System.out.println(auctionSystem.buyDoubleAuction(email, password, auctionId, bid));
		} catch (Exception error) {
			System.out.println(error.getMessage());
		}
	}

	private void doubleAuctionSellerBid() {
		try {
			System.out.print("Double Auction ID: ");
			int auctionId = integerScanner.nextInt();

			System.out.print("Selling Bid: ");
			int bid = integerScanner.nextInt();

			System.out.println(auctionSystem.sellDoubleAuction(email, password, auctionId, bid));
		} catch (Exception error) {
			System.out.println(error.getMessage());
		}
	}
}