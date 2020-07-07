package hotel;

import java.awt.BorderLayout;
import javax.swing.JPanel;

/**
 * Class to display calendar panel and info by day
 * v1.0
 * @author Anhduy Khong, Navya Kaur, Cheng Chin Lim
 *
 */
public class CalendarViewPanel extends JPanel{
	public CalendarViewPanel(Hotel hotel, HotelFrame frame){
		this.setLayout(new BorderLayout());
		CalendarPanel calendarPanel = new CalendarPanel(hotel, frame);
		DayInfoPanel dayInfoPanel = new DayInfoPanel(hotel, frame, calendarPanel);
		this.add(calendarPanel, BorderLayout.LINE_START);
		this.add(dayInfoPanel, BorderLayout.LINE_END);
	}
}
