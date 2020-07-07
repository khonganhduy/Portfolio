package hotel;

import java.io.Serializable;

/**
 * Class to hold information about a user account
 * v1.0
 * @author Anhduy Khong, Navya Kaur, Cheng Chin Lim
 *
 */
public class UserAccount implements Serializable{
	private String username;
	private String password;
	
	public UserAccount (String username, String password){
		this.password = password;
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
