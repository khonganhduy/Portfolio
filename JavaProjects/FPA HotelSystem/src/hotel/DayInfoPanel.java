package hotel;

import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Class to display day info
 * v1.0
 * @author Anhduy Khong, Navya Kaur, Cheng Chin Lim
 *
 */
public class DayInfoPanel extends JPanel{
	private DayInfo dayInfo;
	public DayInfoPanel(Hotel hotel, HotelFrame frame, CalendarPanel calendarPanel){
		dayInfo = calendarPanel.getDayInfo();

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JLabel title = new JLabel();
		title.setText("Selected date:");
		title.setFont(title.getFont().deriveFont(23f));
		
		JLabel label1 = new JLabel();
		label1.setText("Available rooms: ");
		label1.setFont(label1.getFont().deriveFont(18f));
		
		JTextArea availableRooms = new JTextArea(5,25);
		availableRooms.setEditable(true);
		JScrollPane availableScrollPane = new JScrollPane(availableRooms);
		availableScrollPane.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
		// Please replace the text below with the data from the model
		availableRooms.setText("(Room numbers of available rooms)");
		availableRooms.setFont(availableRooms.getFont().deriveFont(18f));
		
		JLabel label2 = new JLabel();
		label2.setText("Reserved rooms: ");
		label2.setFont(label2.getFont().deriveFont(18f));
		
		JTextArea reservedRooms = new JTextArea(5,25);
		reservedRooms.setEditable(true);
		JScrollPane reservedScrollPane = new JScrollPane(reservedRooms);
		reservedScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		// Please replace the text below with the data from the model
		reservedRooms.setText("(Room numbers of reserved rooms)");
		reservedRooms.setFont(reservedRooms.getFont().deriveFont(18f));
		
		availableScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		reservedScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		

		JButton save = new JButton("Save");
		JButton menu = new JButton("Return to Main Menu");
		dayInfo.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				title.setText("Selected Date: " + dayInfo.getDate());
				availableRooms.setText(dayInfo.getAvailableString());
				reservedRooms.setText(dayInfo.getReservedString());
				frame.pack();
				revalidate();
				repaint();
			}
		});
		
		menu.addActionListener(event ->{
			frame.getContentPane().removeAll();
			frame.getContentPane().add(frame.getMainMenu());
			frame.pack();
		});
		
		save.addActionListener(event ->{
			hotel.save();
			JOptionPane.showMessageDialog(this, "Reservation information saved.");
		});
		
		this.add(title);
		this.add(label1);
		this.add(availableScrollPane);
		this.add(label2);
		this.add(reservedScrollPane);
		this.add(save);
		this.add(menu);
	}

}
