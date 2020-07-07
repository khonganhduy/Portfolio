package hotel;

import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Class to hold information about available rooms
 * v1.0
 * @author Anhduy Khong, Navya Kaur, Cheng Chin Lim
 *
 */
public class GuestRoomInfo {
	private ArrayList<Room> availableLux;
	private ArrayList<Room> availableEcon;
	private ArrayList<ChangeListener> listeners;
	public GuestRoomInfo(){
		availableLux = new ArrayList<Room>();
		availableEcon = new ArrayList<Room>();
		listeners = new ArrayList<ChangeListener>();
	}
	
	public void addChangeListener(ChangeListener listener){
		listeners.add(listener);
	}
	public void setDate(Hotel hotel, LocalDate startDate, LocalDate endDate){
		this.availableLux = hotel.getAvailableLuxuriousRooms(startDate, endDate);
		this.availableEcon = hotel.getAvailableEconomicRooms(startDate, endDate);
		for (ChangeListener listener: listeners){
			ChangeEvent event = new ChangeEvent(this);
			listener.stateChanged(event);
		}
	}
	public String getAvailableLuxString(){
		String str = "";
		for (Room room: availableLux){
			str = str + room.getNumber() + "\n";
		}
		return str;
	}
	
	public String getAvailableEconString(){
		String str = "";
		for (Room room: availableEcon){
			str = str + room.getNumber() + "\n";
		}
		return str;
	}
}
