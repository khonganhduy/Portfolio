package hotel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * Class to display the calendar
 * v1.0
 * @author Anhduy Khong, Navya Kaur, Cheng Chin Lim
 *
 */
public class CalendarPanel extends JPanel {
	final int MAX_NUM_OF_BUTTONS = 42;
	final String[] DAYS_OF_WEEK = { "S", "M", "T", "W", "T", "F", "S" };
	private ArrayList<JButton> daysOfMonthButtons;
	private LocalDate now; // the date now
	private JPanel gridPanel;
	private JFrame frame;
	private Hotel hotel;
	private DayInfo dayInfo;

	public CalendarPanel(Hotel hotel, HotelFrame frame) {
		this.frame = frame;
		this.hotel = hotel;
		this.dayInfo = new DayInfo();
		daysOfMonthButtons = new ArrayList<>();
		now = LocalDate.now();

		this.setLayout(new BorderLayout());
		// top bar for month year and forward backward buttons
		JPanel topBar = new JPanel();
		topBar.setLayout(new BorderLayout());
		
		// month year label
		JLabel monthYearLabel = new JLabel();
		monthYearLabel.setText(getMonthAndYear());
		monthYearLabel.setFont(monthYearLabel.getFont().deriveFont(20f));
		topBar.add(monthYearLabel, BorderLayout.CENTER);

		// forward and backward button
		JPanel buttonPanel = new JPanel();
		JButton forwardBtn = new JButton();
		forwardBtn.setText("<");
		JButton backwardBtn = new JButton();
		backwardBtn.setText(">");

		// add those buttons to the top bar
		buttonPanel.add(forwardBtn);
		buttonPanel.add(backwardBtn);
		topBar.add(buttonPanel, BorderLayout.LINE_END);

		// create a grid panel to show the days of the month
		gridPanel = new JPanel();

		// action listeners for forward and backward buttons
		forwardBtn.addActionListener(e -> {
			now = now.minusMonths(1);
			updateCalendarMonth(monthYearLabel);
		});

		backwardBtn.addActionListener(e -> {
			now = now.plusMonths(1);
			updateCalendarMonth(monthYearLabel);
		});
		// For labels
		redraw();
		// fill out the button for current month

		this.add(topBar, BorderLayout.PAGE_START);
		this.add(gridPanel, BorderLayout.CENTER);
	}

	// get the month and year to be displayed on the top bar
	public String getMonthAndYear() {
		return String.valueOf(now.getMonth()) + " " + now.getYear();
	}

	public void makeDaysOfMonthButtonsBlack() {
		for (JButton button : daysOfMonthButtons) {
			button.setForeground(Color.BLACK);
		}
	}

	public void updateCalendarMonth(JLabel monthYearLabel) {
		monthYearLabel.setText(getMonthAndYear());
		redraw();
	}
	
	public void redraw(){
		gridPanel.removeAll();
		setLayoutManager();
		for (int i = 0; i < DAYS_OF_WEEK.length; i++) {
			JLabel dayOfWeekLabel = new JLabel();
			dayOfWeekLabel.setText(DAYS_OF_WEEK[i]);
			dayOfWeekLabel.setBorder(new EmptyBorder(30, 30, 0, 0));
			gridPanel.add(dayOfWeekLabel);
		}
		createDayButtons();
		frame.pack();
		gridPanel.revalidate();
		gridPanel.repaint();
	}

	public void createDayButtons(){
		LocalDate c = now;
		LocalDate counter = c;
		int dayOfWeekEnumVal = c.withDayOfMonth(1).getDayOfWeek().getValue();
		counter = counter.minusDays(counter.getDayOfMonth() - 1);
		int i = 0;
		while (counter.getMonth() == c.getMonth()){
			DayButton button = new DayButton(counter);
			if (i >= dayOfWeekEnumVal || dayOfWeekEnumVal == 7){
				button.setText(String.valueOf(counter.getDayOfMonth()));
				button.addActionListener(event ->{
					makeDaysOfMonthButtonsBlack();
					button.setForeground(Color.RED);
					dayInfo.setDate(hotel, button.getDate());
				});
				counter = counter.plusDays(1);
			} 
			else {
				button.setVisible(false);
			}
			daysOfMonthButtons.add(button);
			gridPanel.add(button);
			i++;
		}
	}
	
	public void setLayoutManager(){
		int dayOfWeekEnumVal = now.withDayOfMonth(1).getDayOfWeek().getValue();
		if ((dayOfWeekEnumVal == 6 && now.lengthOfMonth() > 28) || (dayOfWeekEnumVal == 5 && now.lengthOfMonth() > 30)){
			gridPanel.setLayout(new GridLayout(7,7));
		}
		else {
			gridPanel.setLayout(new GridLayout(6,7));
		}
	}
	
	public DayInfo getDayInfo(){
		return dayInfo;
	}
}
