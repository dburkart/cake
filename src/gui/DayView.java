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

package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import subsystem.Cakeday;
import subsystem.Period;

/**
 * View a day in the calendar.  Basically serves as a wrapper class for an 
 * instance of DayCanvas.
 * 
 * @author Robert Middleton
 *
 */
public class DayView implements ActionListener {
	
	private JPanel pane;
	private CakeGUI parent;
	private DayCanvas day;
	private JLabel dateText;

	
	/**Constructor for the day view
	 * 
	 */
	public DayView(boolean showTime, CakeGUI parent){
		this.parent = parent;
		this.dateText = new JLabel(Cakeday.MONTHS[parent.currentMonth] + " " +
				parent.currentDay + ", " + parent.currentYear);

		pane = new JPanel(new BorderLayout());

		day = new DayCanvas(showTime, this.parent);
		
		JPanel top = new JPanel();
		JButton prevDay = new JButton("<");
		JLabel currentDay = dateText;
		JButton nextDay = new JButton(">");
		prevDay.addActionListener(this);
		nextDay.addActionListener(this);
		top.add(prevDay);
		top.add(currentDay);
		top.add(nextDay);
		
		pane.add(top, BorderLayout.NORTH);
		pane.add(day, BorderLayout.CENTER);
	}
	
	public DayView(boolean showTime, CakeGUI parent, Period today){
		this.parent = parent;
		pane = new JPanel(new GridLayout(1,1));
		
		day = new DayCanvas(showTime, this.parent, today);
		pane.add(day);
	}
	
	/**Update the day canvas.
	 * 
	 */
    public void updateDay() {
    	this.dateText.setText(Cakeday.MONTHS[parent.currentMonth - 1] + " " +
				parent.currentDay + ", " + parent.currentYear);
    	day.update();
    	day.render();
    }
    
    /**Get the JPanel
     * 
     * @return The JPanel for this DayView
     */
    public JPanel getPane(){
    	return pane;
    }

    /**Do something depending on the ActionEvent.  Handles the buttons to go to the next/previous day.
     * 
     * @param e The ActionEvent to do something with.
     */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == ">") {
			if (parent.currentDay == parent.curMonths[parent.currentMonth-1]) {
				if (parent.currentMonth == 12) {
					parent.updateMonth(1);
					parent.updateYear(parent.currentYear + 1);
				} else 
					parent.updateMonth(parent.currentMonth + 1);
				
				parent.highlightToday(1);
				
			} else 
				parent.highlightToday(parent.currentDay + 1);
			
			this.day.render();
			
		} else if (e.getActionCommand() == "<") {
			if (parent.currentDay == 1) {
				if (parent.currentMonth == 1) {
					parent.updateMonth(12);
					parent.updateYear(parent.currentYear - 1);
				} else 
					parent.updateMonth(parent.currentMonth - 1);
				
				parent.highlightToday(parent.curMonths[parent.currentMonth - 1]);
			
			} else 
				parent.highlightToday(parent.currentDay-1);
			
			this.day.render();
		}
		
		parent.mview.updateSelectedDay(parent.currentDay);
		
		this.dateText.setText(Cakeday.MONTHS[parent.currentMonth - 1] + " " +
				parent.currentDay + ", " + parent.currentYear);
		parent.switchTopRightCard("Add Event");
	}
}
