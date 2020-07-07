package hotel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

/**
 * Class for displaying guest related GUI
 * v1.0
 * @author Anhduy Khong, Navya Kaur, Cheng Chin Lim
 *
 */
public class GuestPanel extends JPanel{
	private JPanel login;
	private JPanel roomView;
	private LocalDate in;
	private LocalDate out;
	private Receipt receipt;

	public GuestPanel(Hotel h, HotelFrame f) {
		JButton signUpButton = new JButton("Sign Up");
		JButton signInButton = new JButton("Sign In");
		add(signUpButton);
		add(signInButton);

		login = new JPanel();
		login.setLayout(new BoxLayout(login, BoxLayout.Y_AXIS));
		JTextField usernameField = createText("Username:", login);
		login.add(usernameField);
		JTextField passwordField = createText("Password:", login);
		login.add(passwordField);
		JButton signUp = new JButton("Create my account!");
		JButton signIn = new JButton("Enter");

		// login.add(signUp);

		signUpButton.addActionListener(event -> {
			removeAll();
			login.remove(signIn);
			login.add(signUp);
			add(login);
			f.pack();
			revalidate();
			repaint();
		});

		signInButton.addActionListener(event -> {
			removeAll();
			login.remove(signUp);
			login.add(signIn);
			add(login);
			f.pack();
			revalidate();
			repaint();
		});

		JPanel reservations = new JPanel();
		reservations.setLayout(new BoxLayout(reservations, BoxLayout.PAGE_AXIS));
		JButton makeRes = new JButton("Make a Reservation");
		JButton cancelRes = new JButton("View/Cancel a Reservation");

		signUp.addActionListener(event -> {
			if (usernameField.getText().equals("") || passwordField.getText().equals("")) {
				JOptionPane.showMessageDialog(f, "Please fill in all fields.");
			} else {
				h.createGuestAccount(usernameField.getText(), passwordField.getText());
				removeAll();
				reservations.add(makeRes);
				reservations.add(cancelRes);

				add(reservations);
				f.pack();
				revalidate();
				repaint();
			}
		});

		signIn.addActionListener(event -> {
			if (h.signInGuest(usernameField.getText(), passwordField.getText())) {
				removeAll();

				reservations.add(makeRes);
				reservations.add(cancelRes);

				add(reservations);
				f.pack();
				revalidate();
				repaint();
			} else {
				JOptionPane.showMessageDialog(null, "Incorrect username and/or password.");
			}
		});

		JPanel overallBook = new JPanel();
		overallBook.setLayout(new BoxLayout(overallBook, BoxLayout.PAGE_AXIS));
		JPanel book = new JPanel();
		// book.setLayout(new BorderLayout());
		JTextField check_in = createText("Check in: ", book);
		book.add(check_in);
		JTextField check_out = createText("Check out: ", book);
		book.add(check_out);

		overallBook.add(book);

		JPanel book2 = new JPanel();
		JTextField roomType = new JTextField("Room: ");
		roomType.setEditable(false);
		book2.add(roomType);
		JButton econRoom = new JButton("$100");
		book2.add(econRoom);
		JButton luxRoom = new JButton("$300");
		book2.add(luxRoom);

		econRoom.addActionListener(event -> {
			econRoom.setEnabled(false);
			luxRoom.setEnabled(true);

		});
		luxRoom.addActionListener(event -> {
			econRoom.setEnabled(true);
			luxRoom.setEnabled(false);
		});
		JButton showAvail = new JButton("Show Me Available Rooms");
		book2.add(showAvail);

		overallBook.add(book2);

		makeRes.addActionListener(event -> {
			removeAll();
			receipt = new Receipt((GuestAccount)h.getCurrentUser());
			add(overallBook);
			f.pack();
			revalidate();
			repaint();
		});

		cancelRes.addActionListener(event -> {
			// get user
			// get reservations
			// select reservatin
			// delete reservation
			removeAll();
			JPanel cancel = new JPanel();
			JPanel reservationPanel = new JPanel();
			JPanel infoPanel = new JPanel();
			reservationPanel.setLayout(new BoxLayout(reservationPanel, BoxLayout.Y_AXIS));
			infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
			JLabel text = new JLabel("CurrentReservations:");
			JTextArea cRes = new JTextArea(10, 25);
			cRes.setEditable(false);
			GuestAccount x = (GuestAccount) h.getCurrentUser();
			AccountReservations accountReservations = new AccountReservations(x);
			accountReservations.addChangeListener(changeEvent -> {
					cRes.setText(accountReservations.getReservationString());
			});
			JTextField roomNum = createText("Room Number: ", infoPanel);
			JTextField startCancel = createText("Check-in Date: ", infoPanel);
			JTextField endCancel = createText("Check-out: ", infoPanel);
			JButton c = new JButton("Cancel");
			JButton menu = new JButton("Return to main menu");
			JScrollPane scroll = new JScrollPane(cRes);
			scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			String area = accountReservations.getReservationString();
			cRes.setText(area);
			reservationPanel.add(text);
			reservationPanel.add(scroll);
			cancel.add(reservationPanel);
			cancel.add(infoPanel);
			infoPanel.add(c);
			infoPanel.add(menu);

			c.addActionListener(event1 -> {
				try {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
					int roomNumber = Integer.parseInt(roomNum.getText());
					LocalDate startDate = LocalDate.parse(startCancel.getText(), formatter);
					LocalDate endDate = LocalDate.parse(endCancel.getText(), formatter);
					Reservation toRemove = null;
					for (Reservation r : x.getReservations()) {
						if (r.getRoom().getNumber() == roomNumber && r.getStartDate().equals(startDate) && r.getEndDate().equals(endDate)) {
							toRemove = r;
						}
					}
					if (toRemove != null){
						h.cancelReservations(toRemove);
						accountReservations.setReservations(x);
						JOptionPane.showMessageDialog(cancel,
								"Your reservation for room " + roomNumber + " on " + startCancel.getText().toString() + "-"
										+ endCancel.getText().toString() + " has been cancelled");
					}
					else {
						JOptionPane.showMessageDialog(cancel, "Your reservation could not be found.");
					}
				}catch (NumberFormatException e){
					JOptionPane.showMessageDialog(cancel, "Please enter a valid room number.");
				}
				catch (Exception e) {
					JOptionPane.showMessageDialog(cancel, "Please enter a date in MM/DD/YYYY format.");
				}
			});
			
			menu.addActionListener(actionEvent ->{
				h.save();
				f.getContentPane().removeAll();
				f.getContentPane().add(f.getMainMenu());
				f.pack();
			});

			// remove reservation from user and from hotel
			add(cancel);
			f.pack();
			revalidate();
			repaint();
		});

		roomView = new JPanel();
		roomView.setLayout(new BorderLayout());

		JPanel roomInput = new JPanel();
		JTextField num = createText("Room: ", roomInput);
		roomInput.add(num);

		JPanel roomViewButtons = new JPanel();
		roomViewButtons.setLayout(new FlowLayout());
		JButton confirm = new JButton("Confirm");
		JButton done = new JButton("Done");
		roomViewButtons.add(confirm);
		roomViewButtons.add(done);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

		JTextField availTitle = new JTextField("Available Rooms: " + check_in.getText() + " - " + check_out.getText());
		JPanel middleSection = new JPanel();
		middleSection.setLayout(new BoxLayout(middleSection, BoxLayout.X_AXIS));
		JTextArea allRooms = new JTextArea();
		allRooms.setEditable(false);
		availTitle.setEditable(false);
		showAvail.addActionListener(event -> {
			try {
				if (checkDates(check_in.getText(), check_out.getText(), h)) {

					roomView.add(availTitle, BorderLayout.NORTH);

					in = LocalDate.parse(check_in.getText(), formatter);
					out = LocalDate.parse(check_out.getText(), formatter);

					String rooms = null;
					if (luxRoom.isEnabled()) {
						rooms = h.viewReservations((h.getAvailableEconomicRooms(in, out)));
						allRooms.setText(rooms);
					} else {
						rooms = h.viewReservations((h.getAvailableLuxuriousRooms(in, out)));
						allRooms.setText(rooms);
					}

					overallBook.remove(makeRes);
					overallBook.remove(cancelRes);
					middleSection.add(allRooms);
					middleSection.add(roomInput);

					roomView.add(middleSection, BorderLayout.CENTER);
					roomView.add(roomViewButtons, BorderLayout.SOUTH);

					overallBook.add(roomView);
					f.pack();
					revalidate();
					repaint();
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(overallBook, "Please enter a date in MM/DD/YYYY format.");
			}
		});
		GuestRoomInfo info = new GuestRoomInfo();
		info.addChangeListener(event -> {
			String rooms = null;
			if (luxRoom.isEnabled()) {
				rooms = h.viewReservations((h.getAvailableEconomicRooms(in, out)));
				allRooms.setText(rooms);
			} else {
				rooms = h.viewReservations((h.getAvailableLuxuriousRooms(in, out)));
				allRooms.setText(rooms);
			}
			repaint();
		});
		
		confirm.addActionListener(event -> {
			try {
				Reservation reservation = h.makeAReservation(LocalDate.parse(check_in.getText(), formatter),
						LocalDate.parse(check_out.getText(), formatter), Integer.parseInt(num.getText()));
				if (reservation != null) {
					info.setDate(h, in, out);
					receipt.addToCurrent(reservation);
					JOptionPane.showMessageDialog(roomView, "Your reservation has been confirmed.");
				} else {
					JOptionPane.showMessageDialog(roomView,
							"Please input a valid number from the available rooms list.");
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(roomView, e.getMessage());
			}
			num.setText("");
			overallBook.add(roomView);
			f.pack();
			revalidate();
			repaint();

		});

		done.addActionListener(event -> {
			h.save();
			f.getContentPane().removeAll();
			JPanel panel = new JPanel();
			JPanel buttons = new JPanel();
			panel.setLayout(new BorderLayout());
			JLabel query = new JLabel("Would you like your receipt?");
			JButton simple = new JButton("Simple");
			JButton comprehensive = new JButton("Comprehensive");
			JButton none = new JButton("None");
			buttons.add(simple);
			buttons.add(comprehensive);
			buttons.add(none);
			panel.add(query, BorderLayout.PAGE_START);
			panel.add(buttons, BorderLayout.PAGE_END);
			f.getContentPane().add(panel);
			f.pack();
			
			JPanel receiptPanel = new JPanel();
			JButton finished = new JButton("Done");
			receiptPanel.setLayout(new BoxLayout(receiptPanel, BoxLayout.Y_AXIS));
			JTextArea text = new JTextArea(10,25);
			text.setEditable(false);
			JScrollPane scroll = new JScrollPane(text);
			receiptPanel.add(scroll);
			receiptPanel.add(finished);
			simple.addActionListener(simpleEvent ->{
				f.getContentPane().removeAll();
				f.getContentPane().add(receiptPanel);
				text.setText(receipt.format(new SimpleReceipt()));
				f.pack();
			});
			comprehensive.addActionListener(comprehensiveEvent ->{
				f.getContentPane().removeAll();
				f.getContentPane().add(receiptPanel);
				text.setText(receipt.format(new ComprehensiveReceipt()));
				f.pack();
			});
			
			none.addActionListener(doneEvent ->{
				f.getContentPane().removeAll();
				f.getContentPane().add(f.getMainMenu());
				f.pack();
			});
			finished.addActionListener(finishedEvent ->{
				f.getContentPane().removeAll();
				f.getContentPane().add(f.getMainMenu());
				f.pack();
			});
			
			
			
		});

		f.pack();
		revalidate();
		repaint();

	}

	private static JTextField createText(String word, JPanel panel) {
		JTextField main = new JTextField(word);
		main.setEditable(false);
		main.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		panel.add(main);

		JTextField main1 = new JTextField();
		main1.setEditable(true);
		main1.setPreferredSize(new Dimension(100, 20));

		panel.add(main1);
		return main1;
	}

	private boolean checkDates(String in, String out, Hotel h) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

		LocalDate come = LocalDate.parse(in, formatter);
		LocalDate leave = LocalDate.parse(out, formatter);

		if (come.isBefore(LocalDate.now())){
			JOptionPane.showMessageDialog(this, "Your check-in date must be after the current date.");
			return false;
		}
		if (come.isAfter(leave)) {
			JOptionPane.showMessageDialog(this, "Your check-out date must be after you check-in date.");
			return false;
		}

		if (h.over60Days(come, leave)) {
			JOptionPane.showMessageDialog(this, "The length of stay cannot be over 60 days.");
			return false;
		}
		return true;

	}
}