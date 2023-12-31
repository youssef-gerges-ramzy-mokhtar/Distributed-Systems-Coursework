public class UserInfo implements java.io.Serializable {
	private String username;
	private String name;
	private String email;
	private String password;

	public UserInfo(String username, String name, String email, String password) {
		this.username = username;
		this.name = name;
		this.email = email;
		this.password = password;
	}

	public String getUserName() {
		return this.username;
	}

	public String getName() {
		return this.name;
	}

	public String getEmail() {
		return this.email;
	}

	public boolean samePassword(String password) {
		return this.password.equals(password);
	}
}