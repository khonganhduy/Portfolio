package hotel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Selection panel between guest and manager
 * v1.0
 * @author Anhduy Khong, Navya Kaur, Cheng Chin Lim
 *
 */
public class MenuPanel extends JPanel {
	public MenuPanel(Hotel hotel, HotelFrame frame){
		GuestPanel gp = new GuestPanel(hotel, frame);
		JButton guestButton = new JButton("Guest");
		JButton managerButton = new JButton("Manager");
		this.add(guestButton);
		this.add(managerButton);
		guestButton.addActionListener(event -> {
			frame.getContentPane().removeAll();
			frame.getContentPane().add(gp);
			frame.repaint();
			frame.pack();
		});
		managerButton.addActionListener(event -> {
			ManagerPanel mp = null;
			try {
			       FileInputStream fileIn = new FileInputStream("/Users/Akabe/Desktop/Programming/inout.txt");
			         ObjectInputStream in = new ObjectInputStream(fileIn);
			         Hotel h = (Hotel) in.readObject();
			         mp = new ManagerPanel(h, frame);
			         in.close();
			         fileIn.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			catch (ClassNotFoundException c) {
		         c.printStackTrace();
		         return;
		    }
			frame.getContentPane().removeAll();
			frame.getContentPane().add(mp);
			frame.repaint();
			frame.pack();
		});
	}
}
