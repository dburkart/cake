package gui.widgets;
/**
 * This is a widget for selecting a daily recurring event.
 * 
 * @author hashemassayari
 *
 */

import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class NthDayRecurrenceWidget extends RecurrenceWidget {

	private JTextField nDays = new JTextField(5);
	private JLabel label1 = new JLabel("Every ");
	private JLabel label2 = new JLabel(" days");
	
	/**
	 * Default constructor
	 * 
	 */
	public NthDayRecurrenceWidget() {
		
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		this.add(label1);
		this.add(nDays);
		this.add(label2);
		
	}

	/**
	 * this function generates the regular expression used to generate the dates that this event recurs on
	 * 
	 * @return the regular expression that is used to generate the recurring event.
	 */
	public String getREX() {
		
		return "[" + nDays.getText() + "]";
		
	}
	
	/**
	 * resets all the field in this widgets to the default values.
	 * 
	 */
	public void reset() {
		
		nDays.setText("");
		
	}
	
	public static void main( String args[] ) {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			
		}
		JFrame frame = new JFrame("NDay widget");
		NthDayRecurrenceWidget weekly = new NthDayRecurrenceWidget();
		
		frame.setLayout(new GridLayout(1,1));
		frame.add(weekly);
		
		frame.setSize(200, 32);
		frame.setVisible(true);
		frame.pack();
		
	}
	
}
