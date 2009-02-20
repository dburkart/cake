/*
 * Cake Calendar
 * Copyright (C) 2009  Hashem Assayari, Dana Burkart, Vladimir Hadzhiyski, 
 * Jack Zhang
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */

package gui.widgets;

import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;

/**
 * This is a widget for selecting a daily recurring event.
 * 
 * @author hashem assayari
 *
 */
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
