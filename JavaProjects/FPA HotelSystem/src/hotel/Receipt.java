package hotel;

import java.util.ArrayList;

/**
 * Receipt holding transaction information
 * v1.0
 * @author Anhduy Khong, Navya Kaur, Cheng Chin Lim
 *
 */
public class Receipt {
	private ArrayList<Reservation> allReservations;
	private ArrayList<Reservation> currentTransaction;
	public Receipt(GuestAccount account){
		this.allReservations = account.getReservations();
		this.currentTransaction = new ArrayList<Reservation>();
	}
	public void addToCurrent(Reservation reservation){
		currentTransaction.add(reservation);
	}
	public ArrayList<Reservation> getCurrent(){
		return currentTransaction;
	}
	
	public ArrayList<Reservation> getAll(){
		return allReservations;
	}
	
	public String format(ReceiptFormatter formatter){
		return formatter.formatReceipt(this);
	}
}
