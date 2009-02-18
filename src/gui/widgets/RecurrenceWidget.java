package gui.widgets;
/**
 * Abstract GUI component for recurrence expression builders.
 * 
 * @author dana
 *
 */

import javax.swing.JPanel;

public abstract class RecurrenceWidget extends JPanel {
	
	/**
	 * Builds and returns the recurrence expression
	 * 
	 * @return recurrence expression in this widget
	 */
	public abstract String getREX();
	
	public abstract void reset();
}
