package gui.widgets;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**Widget for selecting the Nth day of every month.
 * 
 * @author rxm6930
 *
 */
public class NthDayOfMonthRecurrenceWidget extends RecurrenceWidget {
	
	private JTextField day;
	
	public NthDayOfMonthRecurrenceWidget(){
		this.setLayout(new FlowLayout());
		this.add(new JLabel("The "));
		day = new JTextField(5);
	
		this.add(day);
		this.add(new JLabel(" of every month."));
	}

	@Override
	public String getREX() {
		int theDay;
		try{
			theDay = Integer.parseInt(day.getText());
		}
		catch( NumberFormatException e ){
			JOptionPane.showMessageDialog(this, "That value is invalid.");
			return null;
		}
		
		return "[" + theDay + "d]";
	}

	@Override
	public void reset() {
		day.setText("");
		
	}

}
