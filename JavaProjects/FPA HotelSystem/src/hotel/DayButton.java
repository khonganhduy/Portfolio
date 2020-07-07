package hotel;

import java.time.LocalDate;

import javax.swing.JButton;

/**
 * Class for button holding a date
 * v1.0
 * @author Anhduy Khong, Navya Kaur, Cheng Chin Lim
 *
 */
public class DayButton extends JButton{
	private LocalDate date;
	public DayButton(LocalDate date){
		this.date = date;
	}
	public void setText(String text){
		super.setText(text);
	}
	public LocalDate getDate(){
		return this.date;
	}
}
