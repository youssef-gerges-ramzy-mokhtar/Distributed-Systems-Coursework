import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface AuctionApi extends Remote {
  // Auction Item Methods (DONE)
  public AuctionItem getItemSpec(String email, String password, int itemId) throws RemoteException;
  public int createAuctionItem(String email, String password, String itemTitle, String itemDescription, boolean itemCondition) throws RemoteException;
  public ArrayList<AuctionItem> getAuctionItems(String email, String password) throws RemoteException;

  // Forward/Reverse Auction Methods (DONE)
  public int createAuction(String email, String password, int itemId, int startingPrice, String description, int reservePrice) throws RemoteException;
  public Auction closeAuction(String email, String password, int auctionId) throws RemoteException;
  public ArrayList<Auction> getAuctions(String email, String password) throws RemoteException;
  public String bid(String email, String password, int auctionId, int biddingPrice) throws RemoteException;
  public ArrayList<Auction> getAuctions(String email, String password, String itemName) throws RemoteException;

  // Double Auction Methods (DONE)
  public ArrayList<DoubleAuction> getDoubleAuctions(String email, String password) throws RemoteException;
  public DoubleAuction getDoubleAuction(String email, String password, int auctionId) throws RemoteException;
  public int createDoubleAuction(String email, String password, int itemId, String description, int buyersSize, int sellersSize) throws RemoteException;
  public String buyDoubleAuction(String email, String password, int auctionId, int buyingPrice) throws RemoteException;
  public String sellDoubleAuction(String email, String password, int auctionId, int sellingPrice) throws RemoteException;

  // System Registration Methods (DONE)
  public UserInfo login(String email, String password) throws RemoteException;
  public UserInfo signup(String username, String name, String email, String password) throws RemoteException;
} 