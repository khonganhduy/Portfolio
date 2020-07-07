package hotel;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Class to hold information about reservations of an account
 * v1.0
 * @author Anhduy Khong, Navya Kaur, Cheng Chin Lim
 *
 */
public class AccountReservations {
	private ArrayList<Reservation> reservations;
	private ArrayList<ChangeListener> listeners;
	private GuestAccount account;
	
	public AccountReservations(GuestAccount account){
		this.listeners = new ArrayList<ChangeListener>();
		this.account = account;
		this.reservations = account.getReservations();
	}
	
	public void setReservations(GuestAccount account){
		this.reservations = account.getReservations();
		for (ChangeListener listener: listeners){
			ChangeEvent event = new ChangeEvent(this);
			listener.stateChanged(event);
		}
	}
	public void addChangeListener(ChangeListener listener){
		listeners.add(listener);
	}
	
	public String getReservationString(){
		String str = "";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		for (Reservation aReservation: reservations){
			str = str + "Room Number: " + aReservation.getRoom().getNumber() + "\nPrice: " + (int)aReservation.getRoom().getRate()
					+ "\nReservation Period: " + formatter.format(aReservation.getStartDate()) +" - " + 
					formatter.format(aReservation.getEndDate()) + "\n\n";
		}
		return str;
	}
}
