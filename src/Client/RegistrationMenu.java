import java.util.*;

class RegistrationMenu {
	private AuctionApi auctionSystem;
	private Pair<UserInfo, String> user;

	public RegistrationMenu() {
		this.user = null;
		this.run();
	}

	private int menu() {
		System.out.println("Registration Menu: ");
		System.out.println("\t1: Login");
		System.out.println("\t2: SignUp");

		System.out.print("Please select a choice: ");		
		Scanner scanner = new Scanner(System.in);
		int choice = scanner.nextInt();

		return choice;
	}

	private void run() {
		boolean registrationSuccess = false;
		while (!registrationSuccess) {
			int choice = menu();

			try {
				auctionSystem = Client.connectToServer();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

			if (choice == 1)
				registrationSuccess = login();
			else if (choice == 2)
				registrationSuccess = signup();
		}

		System.out.println("Welcome to the Auction System " + this.user.first.getName());
	}

	private boolean login() {
		Scanner textScanner = new Scanner(System.in);

		System.out.print("email: ");
		String email = textScanner.nextLine();

		System.out.print("password: ");
		String password = textScanner.nextLine();

		try {
			this.user = new Pair<UserInfo, String>(auctionSystem.login(email, password), password);
			return true;
		} catch (Exception error) {
			System.out.println(error.getMessage());
			return false;
		}
	}

	private boolean signup() {
		Scanner textScanner = new Scanner(System.in);

		System.out.print("username: ");
		String username = textScanner.nextLine();

		System.out.print("name: ");
		String name = textScanner.nextLine();
		
		System.out.print("email: ");
		String email = textScanner.nextLine();

		System.out.print("password: ");
		String password = textScanner.nextLine();

		try {
			this.user = new Pair<UserInfo, String>(auctionSystem.signup(username, name, email, password), password);
			return true;
		} catch (Exception error) {
			System.out.println(error.getMessage());
			return false;
		}
	}

	public Pair<UserInfo, String> getUser() {
		return this.user;
	}
}