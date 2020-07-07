package hotel;

import javax.swing.JFrame;

/**
 * Tests hotel program
 * v1.0
 * @author Anhduy Khong, Navya Kaur, Cheng Chin Lim
 *
 */
public class HotelReservationTester {
	public static void main(String args[]) {
		Hotel hotel = new Hotel();

		// Debug lines start here (add more reservations for testing if desired)
		// makeAReservation takes in (startDate, endDate, roomNumber)
		hotel.createManagerAccount("manager", "account");
		// Debug lines end here
		HotelFrame hotelFrame = new HotelFrame(hotel);
		hotelFrame.pack();
		hotelFrame.setVisible(true);
		hotelFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
