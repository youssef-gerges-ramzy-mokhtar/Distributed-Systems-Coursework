import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;


public class Client {
    public static AuctionApi connectToServer() throws Exception {
        try {
            String name = "myserver";
            Registry registry = LocateRegistry.getRegistry("localhost");
            AuctionApi server = (AuctionApi) registry.lookup(name);

            return server;
        } catch (Exception e) {
            System.out.println("Exception at Client");
            e.printStackTrace();
            throw e;
        }
    }

    public static void main(String[] args) {
        try {
            // Verifying Server Identify
            ServerVerifier serverVerifier = new ServerVerifier();
            serverVerifier.verifyServerSignature();
            serverVerifier.displayHashValues();

            // Displaying the User Interface
            ConsoleUI consoleUI = new ConsoleUI();
        } catch (Exception e) {
        	System.out.println("Exception at Client");
        	e.printStackTrace();
        }
    }
}

class ConsoleUI {
    public ConsoleUI() {
        System.out.println("===== Auction System =====");
        RegistrationMenu registrationMenu = new RegistrationMenu(); 
        Pair<UserInfo, String> registeredUser = registrationMenu.getUser();

        ClientMenu clientMenu = new ClientMenu(registeredUser.first.getEmail(), registeredUser.second);
    }
}