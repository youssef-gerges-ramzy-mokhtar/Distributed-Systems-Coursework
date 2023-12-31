import java.util.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import org.jgroups.*;
import org.jgroups.blocks.*;
import org.jgroups.util.*;
import java.lang.reflect.*;
import java.util.logging.*;
import java.io.*;

public class Server extends ReceiverAdapter implements AuctionApi {
	private JChannel channel;
	private JChannel serversChannel;
	private RpcDispatcher disp;
	private UserHandler userHandler;

	public Server() {
		super();
		userHandler = UserHandler.getUserHandler();
	}

	// Auction Item API
	public AuctionItem getItemSpec(String email, String password, int itemId) throws RemoteException {
		try {
			User user = userHandler.getUser(email, password);
			return user.getAuctionItemHandler().getSpec(itemId);		
		} catch (Exception error) {
			throw new RemoteException(error.getMessage());
		}
	}
  	public int createAuctionItem(String email, String password, String itemTitle, String itemDescription, boolean itemCondition) throws RemoteException {
		try {
			User user = userHandler.getUser(email, password);
			return user.getAuctionItemHandler().createAuctionItem(itemTitle, itemDescription, itemCondition);
		} catch (Exception error) {
			throw new RemoteException(error.getMessage());
		}
  	}
  	public ArrayList<AuctionItem> getAuctionItems(String email, String password) throws RemoteException {
  		try {
			User user = userHandler.getUser(email, password);
			return user.getAuctionItemHandler().getAuctionItems();
		} catch (Exception error) {
			throw new RemoteException(error.getMessage());
		}
  	}

  	// Forward/Reverse Auction API
  	public int createAuction(String email, String password, int itemId, int startingPrice, String description, int reservePrice) throws RemoteException {
  		try {
			User user = userHandler.getUser(email, password);
			return user.getNormalAuctionHandler().createAuction(itemId, startingPrice, description, reservePrice);
		} catch (Exception error) {
			throw new RemoteException(error.getMessage());
		}
  	}
  	public Auction closeAuction(String email, String password, int auctionId) throws RemoteException {
  		try {
			User user = userHandler.getUser(email, password);
			return user.getNormalAuctionHandler().closeAuction(auctionId);
		} catch (Exception error) {
			throw new RemoteException(error.getMessage());
		}
  	}
  	public ArrayList<Auction> getAuctions(String email, String password) throws RemoteException {
  		try {
  			User currentUser = null;
  			try {
				currentUser = userHandler.getUser(email, password);
  			} catch (Exception error) {}

			ArrayList<Auction> auctions = new ArrayList<Auction>();
			ArrayList<Pair<UserInfo, User>> users = userHandler.getAllUsers();
			for (Pair<UserInfo, User> user: users) {
				boolean hideReserve = true;
				if (user.second == currentUser)
					hideReserve = false;

				auctions.addAll(user.second.getNormalAuctionHandler().getAuctions(hideReserve));
			}

			return auctions;
		} catch (Exception error) {
			throw new RemoteException(error.getMessage());
		}
  	}
  	public String bid(String email, String password, int auctionId, int biddingPrice) throws RemoteException { // That is not DRY AT ALL
  		try {
			UserInfo currentUser = null;
			try {
				currentUser = userHandler.login(email, password);
			} catch (Exception error) {}

			ArrayList<Pair<UserInfo, User>> users = userHandler.getAllUsers();
			for (Pair<UserInfo, User> user: users) {
				if (!user.second.getNormalAuctionHandler().auctionExist(auctionId))
					continue;
				if (user.first == currentUser)
					throw new Exception("You cannot Bid for an Auction you have Created");
				
				return user.second.getNormalAuctionHandler().bid(auctionId, currentUser.getName(), email, biddingPrice);
			}

			throw new Exception("Auction ID Not Found");
		} catch (Exception error) {
			throw new RemoteException(error.getMessage());
		}
  	}
  	public ArrayList<Auction> getAuctions(String email, String password, String itemName) throws RemoteException { // That is not DRY AT ALL
  		try {
  			User currentUser = null;
  			try {
				currentUser = userHandler.getUser(email, password);
  			} catch (Exception error) {}

			ArrayList<Auction> auctions = new ArrayList<Auction>();
			ArrayList<Pair<UserInfo, User>> users = userHandler.getAllUsers();
			for (Pair<UserInfo, User> user: users) {
				boolean hideReserve = true;
				if (user.second == currentUser)
					hideReserve = false;

				auctions.addAll(user.second.getNormalAuctionHandler().getAuctions(itemName, hideReserve));
			}

			return auctions;
		} catch (Exception error) {
			throw new RemoteException(error.getMessage());
		}
  	}

  	// Double Auction API
 	public ArrayList<DoubleAuction> getDoubleAuctions(String email, String password) throws RemoteException {
 		try {
 			ArrayList<DoubleAuction> doubleAuctions = new ArrayList<DoubleAuction>();
 			ArrayList<Pair<UserInfo, User>> users = userHandler.getAllUsers();
			for (Pair<UserInfo, User> user: users)
				doubleAuctions.addAll(user.second.getDoubleAuctionHandler().getDoubleAuctions());

			return doubleAuctions;
 		} catch (Exception error) {
 			throw new RemoteException(error.getMessage());
 		}
 	}
  	public DoubleAuction getDoubleAuction(String email, String password, int auctionId) throws RemoteException {
  		try {
			ArrayList<Pair<UserInfo, User>> users = userHandler.getAllUsers();
			for (Pair<UserInfo, User> user: users)
				if (user.second.getDoubleAuctionHandler().doubleAuctionExist(auctionId))
					return user.second.getDoubleAuctionHandler().getDoubleAuction(auctionId);

  			throw new Exception("Double Auction ID Not Found");
  		} catch (Exception error) {
  			throw new RemoteException(error.getMessage());
  		}
  	}
  	public int createDoubleAuction(String email, String password, int itemId, String description, int buyersSize, int sellersSize) throws RemoteException {
  		try {
  			User user = userHandler.getUser(email, password);
  			return user.getDoubleAuctionHandler().createDoubleAuction(itemId, description, buyersSize, sellersSize);
  		} catch (Exception error) {
  			throw new RemoteException(error.getMessage());
  		}
  	}
  	public String buyDoubleAuction(String email, String password, int auctionId, int buyingPrice) throws RemoteException {
  		try {
  			User buyer = userHandler.getUser(email, password);

 			ArrayList<Pair<UserInfo, User>> users = userHandler.getAllUsers();
			for (Pair<UserInfo, User> user: users)
				if (user.second.getDoubleAuctionHandler().doubleAuctionExist(auctionId))
					return user.second.getDoubleAuctionHandler().buyDoubleAuction(auctionId, buyingPrice, buyer.getDoubleAuctionHandler());

			throw new Exception("Double Auction ID Not Found");
  		} catch (Exception error) {
  			throw new RemoteException(error.getMessage());
  		}
  	}
  	public String sellDoubleAuction(String email, String password, int auctionId, int sellingPrice) throws RemoteException {
  		try {
  			User seller = userHandler.getUser(email, password);

  			ArrayList<Pair<UserInfo, User>> users = userHandler.getAllUsers();
			for (Pair<UserInfo, User> user: users)
				if (user.second.getDoubleAuctionHandler().doubleAuctionExist(auctionId))
					return user.second.getDoubleAuctionHandler().sellDoubleAuction(auctionId, sellingPrice, seller.getDoubleAuctionHandler());

			throw new Exception("Double Auction ID Not Found");
  		} catch (Exception error) {
  			throw new RemoteException(error.getMessage());
  		}
  	}

  	// System Registration API
  	public UserInfo login(String email, String password) throws RemoteException {
  		try {
  			return userHandler.login(email, password);
  		} catch (Exception error) {
  			throw new RemoteException(error.getMessage());
  		}
  	}
 	public UserInfo signup(String username, String name, String email, String password) throws RemoteException {
 		try {
 			return userHandler.signup(username, name, email, password);
 		} catch (Exception error) {
 			throw new RemoteException(error.getMessage());
 		}
 	}

 	// Helper Methods //
 	private void connectToClusters() throws Exception {
		try {
			LogManager.getLogManager().reset();
			
			channel = new JChannel();
			channel.setReceiver(this);
			channel.setName(AuctionConstants.SERVER_CLASS_NAME);
			channel.connect(AuctionConstants.MAIN_CLUSTER_NAME);
			disp = new RpcDispatcher(channel, this);

			serversChannel = new JChannel();
			serversChannel.setReceiver(this);
			serversChannel.setName(AuctionConstants.SERVER_CLASS_NAME);
			serversChannel.connect(AuctionConstants.SERVER_CLUSTER_NAME);
			serversChannel.getState(null, AuctionConstants.TIMEOUT);
		} catch (Exception e) {
			System.out.println("Error Connecting to Cluster");
			throw e;
		}
	}
	public void viewAccepted(View newView) {
		if (channel.getView().equals(newView))
    		System.out.println(">>> Auction View: " + newView);
		else
			System.out.println(">>> Servers View: " + newView);
	}
	public void getState(OutputStream output) throws Exception {
		System.out.println(">>> I am the Coordinator for State Transfer");
		synchronized(userHandler) {
			Util.objectToStream(userHandler, new DataOutputStream(output));
		}
	}
	public void setState(InputStream input) throws Exception {
		synchronized(userHandler) {
			userHandler = (UserHandler) Util.objectFromStream(new DataInputStream(input));
		}

		System.out.println(">>> State Transfer Finished");
	}

	public static void main(String[] args) {
		try {
    		Server server = new Server();
    		server.connectToClusters();
        	System.out.println("Server Replica Ready");
     	} catch (Exception e) {
        	System.err.println("Exception Happened");
        	e.printStackTrace();
      	}
	}
}