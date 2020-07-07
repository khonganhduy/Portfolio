package hotel;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Class to hold room information for usage with room view panel
 * v1.0
 * @author Anhduy Khong, Navya Kaur, Cheng Chin Lim
 *
 */
public class RoomInfo {
	private Room room;
	private int roomNum;
	private double price;
	private ArrayList<Reservation> reservations;
	private ArrayList<ChangeListener> listeners;
	
	public RoomInfo(){
		this.room = null;
		this.roomNum = -1;
		this.price = 0;
		this.reservations = new ArrayList<Reservation>();
		listeners = new ArrayList<ChangeListener>();
	}
	
	public void setRoom(Hotel hotel, Room room){
		this.room = room;
		this.roomNum = room.getNumber();
		this.price = room.getRate();
		ArrayList<Reservation> roomReservations = new ArrayList<Reservation>();
		for (Reservation aReservation: hotel.getReservations()){
			if (aReservation.getRoom().equals(this.room)){
				roomReservations.add(aReservation);
			}
		}
		this.reservations = roomReservations;
		for (ChangeListener listener: listeners){
			ChangeEvent e = new ChangeEvent(this);
			listener.stateChanged(e);
		}
	}
	
	public void addChangeListener(ChangeListener listener){
		listeners.add(listener);
	}
	
	public String getRoomInfoString(){
		String str = String.format("%s%.2f%s", "Price: $", price, "\nReserved Dates:\n");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		for (Reservation aReservation: reservations){
			str = str + formatter.format(aReservation.getStartDate()) +" - " + formatter.format(aReservation.getEndDate()) + "\n";
		}
		return str;
	}
	
	public int getRoomNumber(){
		return roomNum;
	}
}
