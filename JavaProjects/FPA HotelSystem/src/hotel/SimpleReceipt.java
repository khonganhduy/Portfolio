package hotel;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Formats a receipt in a simple manner for usage with strategy pattern
 * v1.0
 * @author Anhduy Khong, Navya Kaur, Cheng Chin Lim
 *
 */
public class SimpleReceipt implements ReceiptFormatter {
	
	// for the print out, it should be print in GUI after the 'DONE' button is clicked
	// Need to remove System.out.println eventually
	@Override
	public String formatReceipt(Receipt receipt) {
		// TODO Auto-generated method stub
		// I use the start date and the end date to determine if it's the current transaction
		String str = "";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		double price = 0;
		for (Reservation aReservation: receipt.getCurrent()) {
			str = str + "Room Number: " + aReservation.getRoom().getNumber() + "\nPrice: $" + (int)aReservation.getRoom().getRate()
					+ "\nReservation Period: " + formatter.format(aReservation.getStartDate()) +" - " + 
					formatter.format(aReservation.getEndDate()) + "\n\n";
			price = price + aReservation.getRoom().getRate();
		}
		str = str + "Total Price: $" + (int)price;
		return str;
	}
}
