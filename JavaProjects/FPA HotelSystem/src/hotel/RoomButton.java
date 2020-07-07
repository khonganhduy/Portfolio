package hotel;

import javax.swing.JButton;

/**
 * Button that holds a room
 * v1.0
 * @author Anhduy Khong, Navya Kaur, Cheng Chin Lim
 *
 */
public class RoomButton extends JButton{
	Room room;
	public RoomButton(Room room){
		this.room = room;
	}
	public void setText(String text){
		super.setText(text);
	}
	
	public Room getRoom(){
		return room;
	}
}
