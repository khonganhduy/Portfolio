package hotel;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Class to display manager GUI
 * v1.0
 * @author Anhduy Khong, Navya Kaur, Cheng Chin Lim
 *
 */
public class ManagerPanel extends JPanel{
	private JPanel viewSelection;
	private RoomViewPanel roomView;
	private CalendarViewPanel calendarView;
	
	public ManagerPanel(Hotel hotel, HotelFrame frame){
		viewSelection = new JPanel();
		JButton roomViewButton = new JButton("Room View");
		JButton calendarViewButton = new JButton("Calendar View");
		viewSelection.add(roomViewButton);
		viewSelection.add(calendarViewButton);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JTextField usernameField = createText("Username:", this);
		JTextField passwordField = createText("Password:", this);
		JButton signIn = new JButton("Sign In");
		add(signIn);
		signIn.addActionListener(event ->{
			if(!usernameField.getText().equals("") && !passwordField.getText().equals("") && hotel.signInManager(usernameField.getText(), passwordField.getText())){
				removeAll();
				this.setLayout(new FlowLayout());
				add(viewSelection);
				frame.pack();
				revalidate();
				repaint();	
			}
			else {
				JOptionPane.showMessageDialog(this, "Please enter a valid username or password.");
			}
		});
		calendarViewButton.addActionListener(event ->{
			calendarView = new CalendarViewPanel(hotel, frame);
			removeAll();
			add(calendarView);
			frame.pack();
			revalidate();
			repaint();
		});
		roomViewButton.addActionListener(event ->{
			roomView = new RoomViewPanel(hotel, frame);
			removeAll();
			add(roomView);
			frame.pack();
			revalidate();
			repaint();
		});
		revalidate();
	}
	private static JTextField createText(String word,JPanel panel) {
		JTextField text = new JTextField(word);
		text.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		JTextField writeText = new JTextField();
		text.setEditable(false);
		writeText.setEditable(true);
		writeText.setPreferredSize(new Dimension(100,20));
		panel.add(text);
		panel.add(writeText);
		return writeText;
	}	
	
}
