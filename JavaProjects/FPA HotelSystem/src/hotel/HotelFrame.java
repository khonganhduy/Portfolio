package hotel;

import javax.swing.JFrame;

/**
 * Class to create custom frame for hotel
 * v1.0
 * @author Anhduy Khong, Navya Kaur, Cheng Chin Lim
 *
 */
public class HotelFrame extends JFrame {
	private MenuPanel main;
	private Hotel hotel;
	public HotelFrame(Hotel hotel) {
		this.hotel = hotel;
		main = new MenuPanel(hotel, this);
		this.add(main);
	}
	public MenuPanel getMainMenu(){
		return new MenuPanel(hotel, this);
	}
}
