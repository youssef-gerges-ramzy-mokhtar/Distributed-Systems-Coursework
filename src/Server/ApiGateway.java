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

public class ApiGateway extends ReceiverAdapter implements AuctionApi {
	private JChannel channel;
	private JChannel apiGatewaysChannel;
	private RpcDispatcher disp;
	private RequestOptions opts;
	private boolean connectedToRmi;

	public ApiGateway() {
		super();
		this.connectedToRmi = false;
	}

	// Auction Item API
	public AuctionItem getItemSpec(String email, String password, int itemId) throws RemoteException {
		try {
			MethodCall call = new MethodCall(
				Class
				.forName(AuctionConstants.SERVER_CLASS_NAME)
				.getMethod("getItemSpec", String.class, String.class, int.class)
			);
			call.setArgs(email, password, itemId);

			return makeCallToCluster(call);
		} catch (Exception error) {
			throw new RemoteException(error.getMessage());
		}
	}
  	public int createAuctionItem(String email, String password, String itemTitle, String itemDescription, boolean itemCondition) throws RemoteException {
		try {
			MethodCall call = new MethodCall(
				Class
				.forName(AuctionConstants.SERVER_CLASS_NAME)
				.getMethod("createAuctionItem", String.class, String.class, String.class, String.class, boolean.class)
			);
			call.setArgs(email, password, itemTitle, itemDescription, itemCondition);

			return makeCallToCluster(call);
		} catch (Exception error) {
			throw new RemoteException(error.getMessage());
		}
  	}
  	public ArrayList<AuctionItem> getAuctionItems(String email, String password) throws RemoteException {
  		try {
  			MethodCall call = new MethodCall(
				Class
				.forName(AuctionConstants.SERVER_CLASS_NAME)
				.getMethod("getAuctionItems", String.class, String.class)
			);
			call.setArgs(email, password);

  			return makeCallToCluster(call);
		} catch (Exception error) {
			throw new RemoteException(error.getMessage());
		}
  	}

  	// Forward/Reverse Auction API
  	public int createAuction(String email, String password, int itemId, int startingPrice, String description, int reservePrice) throws RemoteException {
  		try {
  			MethodCall call = new MethodCall(
				Class
				.forName(AuctionConstants.SERVER_CLASS_NAME)
				.getMethod("createAuction", String.class, String.class, int.class, int.class, String.class, int.class)
			);
			call.setArgs(email, password, itemId, startingPrice, description, reservePrice);

			return makeCallToCluster(call);
		} catch (Exception error) {
			throw new RemoteException(error.getMessage());
		}
  	}
  	public Auction closeAuction(String email, String password, int auctionId) throws RemoteException {
  		try {
  			MethodCall call = new MethodCall(
  				Class
				.forName(AuctionConstants.SERVER_CLASS_NAME)
				.getMethod("closeAuction", String.class, String.class, int.class)
  			);
  			call.setArgs(email, password, auctionId);

  			return makeCallToCluster(call);
		} catch (Exception error) {
			throw new RemoteException(error.getMessage());
		}
  	}
  	public ArrayList<Auction> getAuctions(String email, String password) throws RemoteException {
  		try {
  			MethodCall call = new MethodCall(
  				Class
				.forName(AuctionConstants.SERVER_CLASS_NAME)
				.getMethod("getAuctions", String.class, String.class)
  			);
  			call.setArgs(email, password);

  			return makeCallToCluster(call);
		} catch (Exception error) {
			throw new RemoteException(error.getMessage());
		}
  	}
  	public String bid(String email, String password, int auctionId, int biddingPrice) throws RemoteException { // That is not DRY AT ALL
  		try {
  			MethodCall call = new MethodCall(
  				Class
				.forName(AuctionConstants.SERVER_CLASS_NAME)
				.getMethod("bid", String.class, String.class, int.class, int.class)
  			);
  			call.setArgs(email, password, auctionId, biddingPrice);

  			return makeCallToCluster(call);
		} catch (Exception error) {
			throw new RemoteException(error.getMessage());
		}
  	}
  	public ArrayList<Auction> getAuctions(String email, String password, String itemName) throws RemoteException { // That is not DRY AT ALL
  		try {
  			MethodCall call = new MethodCall(
  				Class
				.forName(AuctionConstants.SERVER_CLASS_NAME)
				.getMethod("getAuctions", String.class, String.class, String.class)
  			);
  			call.setArgs(email, password, itemName);

  			return makeCallToCluster(call);
		} catch (Exception error) {
			throw new RemoteException(error.getMessage());
		}
  	}

  	// Double Auction API
 	public ArrayList<DoubleAuction> getDoubleAuctions(String email, String password) throws RemoteException {
 		try {
 			MethodCall call = new MethodCall(
  				Class
				.forName(AuctionConstants.SERVER_CLASS_NAME)
				.getMethod("getDoubleAuctions", String.class, String.class)
  			);
  			call.setArgs(email, password);

  			return makeCallToCluster(call);
 		} catch (Exception error) {
 			throw new RemoteException(error.getMessage());
 		}
 	}
  	public DoubleAuction getDoubleAuction(String email, String password, int auctionId) throws RemoteException {
  		try {
  			MethodCall call = new MethodCall(
  				Class
				.forName(AuctionConstants.SERVER_CLASS_NAME)
				.getMethod("getDoubleAuction", String.class, String.class, int.class)
  			);
  			call.setArgs(email, password, auctionId);

  			return makeCallToCluster(call);
  		} catch (Exception error) {
  			throw new RemoteException(error.getMessage());
  		}
  	}
  	public int createDoubleAuction(String email, String password, int itemId, String description, int buyersSize, int sellersSize) throws RemoteException {
  		try {
  			MethodCall call = new MethodCall(
  				Class
				.forName(AuctionConstants.SERVER_CLASS_NAME)
				.getMethod("createDoubleAuction", String.class, String.class, int.class, String.class, int.class, int.class)
  			);
  			call.setArgs(email, password, itemId, description, buyersSize, sellersSize);

  			return makeCallToCluster(call);
  		} catch (Exception error) {
  			throw new RemoteException(error.getMessage());
  		}
  	}
  	public String buyDoubleAuction(String email, String password, int auctionId, int buyingPrice) throws RemoteException {
  		try {
  			MethodCall call = new MethodCall(
  				Class
				.forName(AuctionConstants.SERVER_CLASS_NAME)
				.getMethod("buyDoubleAuction", String.class, String.class, int.class, int.class)
  			);
  			call.setArgs(email, password, auctionId, buyingPrice);

  			return makeCallToCluster(call);
  		} catch (Exception error) {
  			throw new RemoteException(error.getMessage());
  		}
  	}
  	public String sellDoubleAuction(String email, String password, int auctionId, int sellingPrice) throws RemoteException {
  		try {
  			MethodCall call = new MethodCall(
  				Class
				.forName(AuctionConstants.SERVER_CLASS_NAME)
				.getMethod("sellDoubleAuction", String.class, String.class, int.class, int.class)
  			);
  			call.setArgs(email, password, auctionId, sellingPrice);

  			return makeCallToCluster(call);
  		} catch (Exception error) {
  			throw new RemoteException(error.getMessage());
  		}
  	}

  	// System Registration API
  	public UserInfo login(String email, String password) throws RemoteException {
  		try {
  			MethodCall call = new MethodCall(
  				Class
				.forName(AuctionConstants.SERVER_CLASS_NAME)
				.getMethod("login", String.class, String.class)
  			);
  			call.setArgs(email, password);

  			return makeCallToCluster(call);
  		} catch (Exception error) {
  			throw new RemoteException(error.getMessage());
  		}
  	}
 	public UserInfo signup(String username, String name, String email, String password) throws RemoteException {
 		try {
 			MethodCall call = new MethodCall(
  				Class
				.forName(AuctionConstants.SERVER_CLASS_NAME)
				.getMethod("signup", String.class, String.class, String.class, String.class)
  			);
  			call.setArgs(username, name, email, password);

  			return makeCallToCluster(call);
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
			channel.setName(AuctionConstants.API_GATEWAY_CLASS_NAME);
			channel.connect(AuctionConstants.MAIN_CLUSTER_NAME);
			channel.setDiscardOwnMessages(true);
			disp = new RpcDispatcher(channel, this);

			apiGatewaysChannel = new JChannel();
			apiGatewaysChannel.setReceiver(this);
			apiGatewaysChannel.setName(AuctionConstants.API_GATEWAY_CLASS_NAME);
			apiGatewaysChannel.connect(AuctionConstants.API_GATEWAY_CLUSTER_NAME);

			opts = new RequestOptions(ResponseMode.GET_ALL, AuctionConstants.TIMEOUT);
		} catch (Exception e) {
			System.out.println("Exception when Starting the JGroup");
			throw e;
		}
	}
	private <T> T makeCallToCluster(MethodCall call) throws Exception {
		try {
			System.out.println("\n=========== " + call + " ===========");
			
			// Sending the call to the server replicas only
			ArrayList<Address> serverReplicas = new ArrayList<Address>();
			for (Address addr: channel.getView().getMembers())
				if (addr.toString().equals(AuctionConstants.SERVER_CLASS_NAME))
					serverReplicas.add(addr);

			RspList<T> rspList = disp.callRemoteMethods(
				serverReplicas, 
				call,
				opts
			);

			T res = rspList.getFirst();
			
			// Print Statement for debugging purposes only
			System.out.println(">>> Response(s): " + rspList);
			System.out.println(">>> Response: " + res);
			System.out.println("#######");

			// Exception Occurred (No Server Replica have any result)
			if (res == null) {
				Rsp rsp = (Rsp) rspList.values().toArray()[0];
				InvocationTargetException exception = (InvocationTargetException) rsp.getException();
				throw new Exception(exception.getCause());
			}

			return res;
		} catch (Exception e) {
			throw e;
		}
	}
	private void connectToRmi() throws Exception {
		try {
			if (this.connectedToRmi)
				return;

	    	AuctionApi auctionApi = (AuctionApi) UnicastRemoteObject.exportObject(this, 0);
	    	Registry registry = LocateRegistry.getRegistry();
	    	registry.rebind(AuctionConstants.RMI_SERVER_NAME, auctionApi);

			System.out.println(">>> Successfuly Connected to RMI");		
	    	this.connectedToRmi = true;
		} catch (Exception e) {
			throw e;
		}
	}
	private void createDigitalSignature() throws Exception {
		try {
	    	DigitalSignature digitalSignature = new DigitalSignature();
	    	digitalSignature.createDigitalSignature();
		} catch (Exception e) {
			throw e;
		}
	}
	public void viewAccepted(View newView) {
		if (apiGatewaysChannel != null && apiGatewaysChannel.getAddress().equals(newView.get(0))) { // The first address is always the coordinator of the View
			try {
				this.connectToRmi();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 

		if (channel.getView().equals(newView))
    		System.out.println(">>> Auction View: " + newView);
    	else
    		System.out.println(">>> Api-Gateways View: " + newView);
	}

	public static void main(String[] args) {
		try {
    		ApiGateway s = new ApiGateway();
    		s.connectToClusters();
 			s.createDigitalSignature();

        	System.out.println("Server Ready");
     	} catch (Exception e) {
        	System.err.println("Exception Happened");
        	e.printStackTrace();
      	}
	}
}