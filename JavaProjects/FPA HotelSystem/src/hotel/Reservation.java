package hotel;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Class holding information about a reservation
 * v1.0
 * @author Anhduy Khong, Navya Kaur, Cheng Chin Lim
 *
 */
public class Reservation implements Serializable{
	private UserAccount user;
	private Room room;
	private LocalDate startDate;
	private LocalDate endDate;
	
	public Reservation(UserAccount user, Room room, LocalDate startDate, LocalDate endDate){
		this.user = user;
		this.room = room;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public boolean conflict(Room room, LocalDate startDate, LocalDate endDate){
		return this.room.equals(room) && (!this.endDate.isBefore(startDate) && !this.endDate.isAfter(endDate) || !endDate.isBefore(this.startDate) && !endDate.isAfter(this.endDate));		
	}
	
	public boolean conflictManager(Room room, LocalDate date){
		return this.room.equals(room) && (!date.isBefore(this.startDate) && !date.isAfter(this.endDate));
	}

	public UserAccount getUser() {
		return user;
	}

	public void setUser(UserAccount user) {
		this.user = user;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
}
