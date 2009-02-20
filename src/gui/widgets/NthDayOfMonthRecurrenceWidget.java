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
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * Widget for selecting the Nth day of every month.
 * 
 * @author Robert Middleton
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
