package hotel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

/**
 * Class to hold information about a hotel
 * v1.0
 * @author Anhduy Khong, Navya Kaur, Cheng Chin Lim
 *
 */
public class Hotel implements Serializable{
	private ArrayList<Room> luxuriousRooms;
	private ArrayList<Room> economicRooms;
	private ArrayList<GuestAccount> guests;
	private ManagerAccount manager;
	private ArrayList<Reservation> reservations;
	private UserAccount currentUser;

	public Hotel() {
		this.luxuriousRooms = new ArrayList<Room>();
		this.economicRooms = new ArrayList<Room>();
		for (int i = 0; i < 10; i++) {
			luxuriousRooms.add(new Room(i, 300));
		}
		for (int i = 10; i < 20; i++) {
			economicRooms.add(new Room(i, 100));
		}
		guests = new ArrayList<GuestAccount>();
		manager = null;
		reservations = new ArrayList<Reservation>();
		currentUser = null;
	}

	public ArrayList<Room> getLuxuriousRooms() {
		return luxuriousRooms;
	}

	public void setLuxuriousRooms(ArrayList<Room> luxuriousRooms) {
		this.luxuriousRooms = luxuriousRooms;
	}

	public ArrayList<Room> getEconomicRooms() {
		return economicRooms;
	}

	public void setEconomicRooms(ArrayList<Room> economicRooms) {
		this.economicRooms = economicRooms;
	}

	public ArrayList<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(ArrayList<Reservation> reservations) {
		this.reservations = reservations;
	}

	public ArrayList<GuestAccount> getGuests() {
		return guests;
	}

	public void setGuests(ArrayList<GuestAccount> guests) {
		this.guests = guests;
	}

	public ManagerAccount getManagers() {
		return manager;
	}

	public void setManager(ManagerAccount managers) {
		this.manager = managers;
	}

	public boolean createGuestAccount(String username, String password) {
		for (GuestAccount guest : guests) {
			if (guest.getUsername().equals(username)) {
				return false;
			}
		}
		GuestAccount g = new GuestAccount(username, password);
		currentUser = g;
		return guests.add(g);
	}

	public void createManagerAccount(String username, String password) {
		this.manager = new ManagerAccount(username, password);
	}

	public UserAccount getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(UserAccount currentUser) {
		this.currentUser = currentUser;
	}

	public Reservation makeAReservation(LocalDate startDate, LocalDate endDate, int roomNumber) {
		Room room = null;
		GuestAccount guestAccount = (GuestAccount) currentUser;
		if (roomNumber >= 0 && roomNumber < 10) {
			room = luxuriousRooms.get(roomNumber);
		}
		else if (roomNumber < 20) {
			room = economicRooms.get(roomNumber - 10);
		}
		else {
			return null;
		}
		for (Reservation reservation: reservations){
			if (reservation.conflict(room, startDate, endDate)){
				return null;
			}
		}
		Reservation reservation = new Reservation(currentUser, room, startDate, endDate);
		reservations.add(reservation);
		guestAccount.getReservations().add(reservation);
		return reservation;
	}

	public boolean over60Days(LocalDate startDate, LocalDate endDate) {
		long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
		return daysBetween > 60;
	}

	public boolean signInGuest(String username, String password) {
		for (GuestAccount account : guests) {
			if (account.getUsername().equals(username) && account.getPassword().equals(password)) {
				currentUser = account;
				return true;
			}
		}
		return false;
	}
	
	public boolean signInManager(String username, String password){
		if (manager.getUsername().equals(username) && manager.getPassword().equals(password)) {
			currentUser = manager;
			return true;
		}
		return false;
	}

	public ArrayList<Room> getAvailableLuxuriousRooms(LocalDate startDate, LocalDate endDate) {
		ArrayList<Room> available = new ArrayList<Room>();
		ArrayList<Room> reserved = new ArrayList<Room>();
		for (Room aRoom : luxuriousRooms) {
			int i = 0;
			while (i < reservations.size()) {
				if (!available.contains(aRoom) && !reserved.contains(aRoom)){
					if (reservations.get(i).conflict(aRoom, startDate, endDate)){
						reserved.add(aRoom);
					}
				}
				i++;
			}
		}
		for (Room aRoom: luxuriousRooms){
			if (!reserved.contains(aRoom)){
				available.add(aRoom);
			}
		}
		return available;
	}
	public ArrayList<Room> getAvailableEconomicRooms(LocalDate startDate, LocalDate endDate) {
		ArrayList<Room> available = new ArrayList<Room>();
		ArrayList<Room> reserved = new ArrayList<Room>();
		for (Room aRoom : economicRooms) {
			int i = 0;
			while (i < reservations.size()) {
				if (!available.contains(aRoom) && !reserved.contains(aRoom)){
					if (reservations.get(i).conflict(aRoom, startDate, endDate)){
						reserved.add(aRoom);
					}
				}
				i++;
			}
		}
		for (Room aRoom: economicRooms){
			if (!reserved.contains(aRoom)){
				available.add(aRoom);
			}
		}
		return available;
	}

	public ArrayList<ArrayList<Room>> getRoomInformation(LocalDate date) {
		ArrayList<ArrayList<Room>> filteredRooms = new ArrayList<ArrayList<Room>>();
		ArrayList<Room> available = new ArrayList<Room>();
		ArrayList<Room> reserved = new ArrayList<Room>();
		for (Room aRoom : luxuriousRooms) {
			int i = 0;
			while (i < reservations.size()) {
				if (!available.contains(aRoom) && !reserved.contains(aRoom)){
					if (reservations.get(i).conflictManager(aRoom, date)){
						reserved.add(aRoom);
					}
				}
				i++;
			}
		}
		for (Room aRoom: luxuriousRooms){
			if (!reserved.contains(aRoom)){
				available.add(aRoom);
			}
		}
		for (Room aRoom : economicRooms) {
			int i = 0;
			while (i < reservations.size()) {
				if (!available.contains(aRoom) && !reserved.contains(aRoom)){
					if (reservations.get(i).conflictManager(aRoom, date)){
						reserved.add(aRoom);
					}
				}
				i++;
			}
		}
		for (Room aRoom : economicRooms){
			if (!reserved.contains(aRoom)){
				available.add(aRoom);
			}
		}
		filteredRooms.add(available);
		filteredRooms.add(reserved);
		return filteredRooms;
	}

	public String viewReservations(ArrayList<Room> rooms){
		String reservations = null;
		for(Room theRoom : rooms) {
			if (reservations == null) {
				reservations = "Room: " + theRoom.getNumber() + '\n';
			}
			else {
				reservations += "Room: " + theRoom.getNumber() + '\n';
			}	
		}
		return reservations;
	}

	public boolean cancelReservations(Reservation reservation) {
		GuestAccount account = (GuestAccount) currentUser;
		return reservations.remove(reservation) && account.getReservations().remove(reservation);
	}
	
	public void save(){
		// // below is to serialize the above array list to a text file
		FileOutputStream fileOut;
		try {
			// change the file path below
			fileOut = new FileOutputStream("/Users/Anhduy/Desktop/Programming/inout.txt");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(this);
			out.close();
			fileOut.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// // above is serialization
	}
}
