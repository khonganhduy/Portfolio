package hotel;

import java.io.Serializable;

/**
 * Class holding information about a room
 * v1.0
 * @author Anhduy Khong, Navya Kaur, Cheng Chin Lim
 *
 */
public class Room implements Serializable{
	private int number;
	private double rate;
	
	public Room(int number, double rate){
		this.number = number;
		this.rate = rate;
	}
	
	public boolean isEqual(Room aRoom){
		return number == aRoom.getNumber();
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}
	
	@Override
	public boolean equals(Object o){
		Room room = (Room)o;
		return this.number == room.getNumber() && this.rate == room.getRate();
	}
	
}
