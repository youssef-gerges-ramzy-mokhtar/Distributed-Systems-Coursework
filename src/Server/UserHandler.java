import java.util.*;

class UserHandler implements java.io.Serializable {
	private static UserHandler userHandler = null;
	private HashMap<String, Pair<UserInfo, User>> users;

	private UserHandler() {
		this.users = new HashMap<String, Pair<UserInfo, User>>();		
		
		// Registring some Dummy Users to the System
		try {
			signup("joe", "youssef", "sofa.joe10@yahoo.com", "password");
			signup("yudai", "yudai", "yudai@email.com", "yudai_password");
			signup("deven", "deven", "deven@email.com", "deven_password");
		} catch (Exception error) {
			// do nothing I am sure that the signup will successed
		}
	}

	public UserInfo login(String email, String password) throws Exception {
		Pair<UserInfo, User> user = users.get(email);

		if (user == null)
			throw new Exception("Invalid User Credentials");

		if (!user.first.samePassword(password))
			throw new Exception("Invalid User Credentials");
	
		return user.first;
	}

	public UserInfo signup(String username, String name, String email, String password) throws Exception {
		if (users.containsKey(email))
			throw new Exception("Email Already Exists");

		UserInfo userInfo = new UserInfo(username, name, email, password);
		User user = new User(); // not sure if I would need to pass the userInfo Object to the User Object

		Pair<UserInfo, User> userData = new Pair<UserInfo, User>(userInfo, user);
		users.put(email, userData);

		return userInfo;
	}

	public User getUser(String email, String password) throws Exception {
		try {
			login(email, password);
			return users.get(email).second;
		} catch (Exception error) {
			throw error;
		}
	}

	public ArrayList<Pair<UserInfo, User>> getAllUsers() {
		return new ArrayList<Pair<UserInfo, User>>(users.values());
	}

	// Singelton
	public static UserHandler getUserHandler() {
		if (userHandler == null)
			userHandler = new UserHandler();

		return userHandler;
	}
}