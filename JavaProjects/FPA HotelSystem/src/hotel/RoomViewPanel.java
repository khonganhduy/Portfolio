package hotel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

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
 * Displays the room view of the hotel
 * v1.0
 * @author Anhduy Khong, Navya Kaur, Cheng Chin Lim
 *
 */
public class RoomViewPanel extends JPanel {
	private ArrayList<RoomButton> roomButtons;
	private RoomInfo roomInfo;

	public RoomViewPanel(Hotel hotel, HotelFrame frame) {
		this.roomButtons = new ArrayList<RoomButton>();
		this.roomInfo = new RoomInfo();
		JPanel luxGridPanel = new JPanel();
		JPanel ecoGridPanel = new JPanel();
		GridLayout grid = new GridLayout(2, 5);
		luxGridPanel.setLayout(grid);
		ecoGridPanel.setLayout(grid);
		
		this.setLayout(new BorderLayout());
		JPanel roomPanel = new JPanel();
		
		roomPanel.setLayout(new BoxLayout(roomPanel, BoxLayout.Y_AXIS));
		JPanel luxRoomViewPanel = new JPanel();
		JPanel ecoRoomViewPanel = new JPanel();

		JLabel title1 = new JLabel();
		title1.setText("Luxurious room numbers:");
		title1.setFont(title1.getFont().deriveFont(18f));

		JLabel title2 = new JLabel();
		title2.setText("Economic room numbers:");
		title2.setFont(title2.getFont().deriveFont(18f));

		// the above code is all about GUI

		generateButtons(hotel, hotel.getLuxuriousRooms(), luxGridPanel);
		generateButtons(hotel, hotel.getEconomicRooms(), ecoGridPanel);

		luxRoomViewPanel.add(title1);
		ecoRoomViewPanel.add(title2);

		roomPanel.add(luxRoomViewPanel);
		roomPanel.add(luxGridPanel);
		roomPanel.add(ecoRoomViewPanel);
		roomPanel.add(ecoGridPanel);
		this.add(roomPanel, BorderLayout.LINE_START);
		
		JPanel roomInfoPanel = new JPanel();
		roomInfoPanel.setLayout(new BoxLayout(roomInfoPanel, BoxLayout.Y_AXIS));
		
		JLabel title = new JLabel();
		// Please replace the text below with the data from the model
		title.setText("Room Number: ");
		title.setFont(title.getFont().deriveFont(23f));
		
		JTextArea details = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(details);
		scrollPane.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
		details.setEditable(false);
		scrollPane.setAlignmentX(LEFT_ALIGNMENT);
		// Please replace the text below with the data from the model
		details.setText("Transaction (Name, Range of the date)"); 
		details.setFont(details.getFont().deriveFont(18f));
		
		JButton save = new JButton ("Save");
		JButton menu = new JButton ("Return to Main Menu");
		
		roomInfo.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				title.setText("Room Number: " + roomInfo.getRoomNumber());
				details.setText(roomInfo.getRoomInfoString());
				frame.pack();
				revalidate();
				repaint();
			}	
		});
		
		save.addActionListener(event ->{
			hotel.save();
			JOptionPane.showMessageDialog(this,"Reservation information saved.");
		});
		menu.addActionListener(event ->{
			frame.getContentPane().removeAll();
			frame.getContentPane().add(frame.getMainMenu());
			frame.pack();
		});
		
		roomInfoPanel.add(title);
		roomInfoPanel.add(scrollPane);
		roomInfoPanel.add(save);
		roomInfoPanel.add(menu);
		this.add(roomInfoPanel, BorderLayout.LINE_END);
	}
	
	public void generateButtons(Hotel hotel, ArrayList<Room> rooms, JPanel gridPanel){
		for (Room room: rooms){
			RoomButton roomNoBtn = new RoomButton(room);
			roomNoBtn.setPreferredSize(new Dimension(50,50));
			roomNoBtn.setText(String.valueOf(room.getNumber()));
			roomNoBtn.addActionListener(event ->{
				resetColorsToBlack();
				roomNoBtn.setForeground(Color.RED);
				roomInfo.setRoom(hotel, roomNoBtn.getRoom());
			});
			gridPanel.add(roomNoBtn);
			// add buttons into array list for adding action listeners
			roomButtons.add(roomNoBtn);
		}
	}
	public void resetColorsToBlack(){
		for (JButton button : roomButtons) {
			button.setForeground(Color.BLACK);
		}
	}
}
