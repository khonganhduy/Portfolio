package hotel;

import java.time.format.DateTimeFormatter;

/**
 * Class to format to a comprehensive receipt
 * v1.0
 * @author Anhduy Khong, Navya Kaur, Cheng Chin Lim
 *
 */
public class ComprehensiveReceipt implements ReceiptFormatter {

	@Override
	public String formatReceipt(Receipt receipt) {
		// TODO Auto-generated method stub
		// just print all the details of all the transactions of the user
		String str = "";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		double price = 0;
		for (Reservation aReservation: receipt.getAll()) {
			str = str + "Room Number: " + aReservation.getRoom().getNumber() + "\nPrice: $" + (int)aReservation.getRoom().getRate()
					+ "\nReservation Period: " + formatter.format(aReservation.getStartDate()) +" - " + 
					formatter.format(aReservation.getEndDate()) + "\n\n";
			price = price + aReservation.getRoom().getRate();
		}
		str = str + "Total Price: $" + (int)price;
		return str;
	}
}
