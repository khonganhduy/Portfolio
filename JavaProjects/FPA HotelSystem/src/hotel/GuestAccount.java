package hotel;

import java.util.ArrayList;

/**
 * Class to hold information about a guest account
 * v1.0
 * @author Anhduy Khong, Navya Kaur, Cheng Chin Lim
 *
 */
public class GuestAccount extends UserAccount{
	private ArrayList<Reservation> reservations;
	public GuestAccount(String username, String password){
		super(username, password);
		this.reservations = new ArrayList<Reservation>();
	}
	public ArrayList<Reservation> getReservations() {
		return reservations;
	}
	public void setReservations(ArrayList<Reservation> reservations) {
		this.reservations = reservations;
	}
}
