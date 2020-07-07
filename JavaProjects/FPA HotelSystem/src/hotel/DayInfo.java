package hotel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Class to hold information about reservations on a day
 * v1.0
 * @author Anhduy Khong, Navya Kaur, Cheng Chin Lim
 *
 */
public class DayInfo {
	private ArrayList<Room> available;
	private ArrayList<Room> reserved;
	private LocalDate viewDate;
	private ArrayList<ChangeListener> listeners;
	public DayInfo(){
		available = new ArrayList<Room>();
		reserved = new ArrayList<Room>();
		viewDate = null;
		listeners = new ArrayList<ChangeListener>();
	}
	
	public void addChangeListener(ChangeListener listener){
		listeners.add(listener);
	}
	public void setDate(Hotel hotel, LocalDate date){
		viewDate = date;
		ArrayList<ArrayList<Room>> filteredRooms = hotel.getRoomInformation(date);
		available = filteredRooms.get(0);
		reserved = filteredRooms.get(1);
		for (ChangeListener listener: listeners){
			ChangeEvent event = new ChangeEvent(this);
			listener.stateChanged(event);
		}
	}
	public String getAvailableString(){
		String str = "";
		for (Room room: available){
			str = str + room.getNumber() + "\n";
		}
		return str;
	}
	
	public String getReservedString(){
		String str = "";
		for (Room room: reserved){
			str = str + room.getNumber() + "\n";
		}
		return str;
	}
	
	public String getDate(){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		return formatter.format(viewDate);
	}
}
