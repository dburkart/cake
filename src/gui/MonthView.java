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

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import subsystem.CakeCal;
import subsystem.Cakeday;
import subsystem.Event;
import subsystem.Period;
import subsystem.SimpleDate;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.util.ArrayList;
import java.util.List;

/**
 *  Views months in the calendar.
 *  
 * @author Jack Zhang
 * @author Dana Burkart
 * @author Vladimir Hadzhiyski
 */
public class MonthView implements ActionListener, MouseListener {
	final static boolean shouldFill = true;					//default gridbaglayout constraints
	final static boolean shouldWeightX = true;
	final static boolean RIGHT_TO_LEFT = false;

	float[] selectColor = new float[3];

	final static String[] MONTHS = Cakeday.MONTHS;			//contains the name of months

	JPanel monthPanel;											//panel to be populated with number of days
	JTextArea currentDay;
	CakeGUI parent;					//labels which day it is within the month
	JLabel yTitle;				//Year to be displayed on the Title
	JLabel mTitle;				//Month to be displayed on the Title

	JTextArea[] days = new JTextArea[42];			//values to be stored in the MonthView

	int DayToErase = -1;
	int curMonths[];             // create the days in the particular month

	/**
	 * default constructor
	 * 
	 * @param		 parent			the instance of CakeGUI object to be copied.
	 */

	public MonthView( CakeGUI parent ) {
		this.parent = parent;
		Color.RGBtoHSB(103, 137, 163, selectColor);
		yTitle = new JLabel( parent.currentYear + "" );
		mTitle = new JLabel( MONTHS[parent.currentMonth - 1] );
		curMonths = CakeCal.getMonths(parent.currentYear); 
		monthPanel = parent.getMonthPanel();
	}

	/**
	 * Creates a Month view panel
	 * 
	 * @param 		pane				The JPanel for which the Month View will be created in.
	 * 
	 * @return		pane				A new JPanel filled with all the elements of the Month View.
	 */
	public JPanel addComponentsToPane(JPanel pane) {

		pane.removeAll();

		if (RIGHT_TO_LEFT) {														//set default gridbaglayout constraint values
			pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		}
		JButton button;
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		if (shouldFill) {
			//natural height, maximum width
			c.fill = GridBagConstraints.HORIZONTAL;
		}

		button = new JButton("<<"); 									//creates previous buttons for month and year
		button.addActionListener(this);
		JButton otherbutton = new JButton( "<" );
		otherbutton.addActionListener(this);
		JPanel b1 = new JPanel( new GridLayout(2,1) );
		b1.add( otherbutton );
		b1.add(button);
		c.weightx = 0.5;
		c.ipadx = 81;
		c.ipady = 70;
		c.fill = GridBagConstraints.VERTICAL;
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		pane.add(b1, c);

		JPanel Title = new JPanel( new GridLayout( 2, 1));				//creates titles for month and year
		mTitle.setFont( new Font( "Verdana", Font.PLAIN, 66));
		mTitle.setHorizontalAlignment(JLabel.CENTER);
		Title.add( mTitle );
		yTitle.setHorizontalAlignment(JLabel.CENTER);
		Title.add( yTitle );
		yTitle.setFont( new Font( "Verdana", Font.PLAIN, 66));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 1;
		c.gridy = 0;
		pane.add(Title, c);

		button = new JButton(">>");							//creates next buttons for month and year
		button.addActionListener(this);
		otherbutton = new JButton(">");
		otherbutton.addActionListener(this);
		JPanel b2 = new JPanel(new GridLayout(2,1) );
		b2.add(otherbutton);
		b2.add(button);
		c.fill = GridBagConstraints.VERTICAL;
		c.ipadx = 81;
		c.ipady = 70;
		c.weightx = 0.5;
		c.gridx = 2;
		c.gridy = 0;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		pane.add(b2, c);


		c.gridy = 1;
		c.gridx = 0;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.fill = GridBagConstraints.HORIZONTAL;
		JPanel week = new JPanel( new GridLayout(1,7) );
		
		JLabel label = new JLabel();
		
		for (int i=0; i<7; i++) {
			label = new JLabel(Cakeday.DAYS[i]);
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setFont(new Font("Verdana", Font.BOLD, 16));
			week.add(label);
		}
		
		pane.add(week, c);
		
		monthPanel = new JPanel( new GridLayout( 6, 7, 3, 3 )); 
		currentDay = new JTextArea("");
		for( int i = 0; i < 42; i++ ) {
			if(i >= parent.currentMonthOffset && i < curMonths[parent.currentMonth - 1] + parent.currentMonthOffset) {
				days[i] = new JTextArea(i + 1 - parent.currentMonthOffset + "" );
				days[i].setName(i + 1 - parent.currentMonthOffset + "");
				days[i].addMouseListener(this);
				
				
				Border line = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
				TitledBorder title = BorderFactory.createTitledBorder(line);
				days[i].setBorder(title);
				
				
				
			} else {
				days[i] = new JTextArea( " " );
				days[i].setName(" ");
				days[i].addMouseListener(this);
				
				Border line = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
				TitledBorder title = BorderFactory.createTitledBorder(line);
				days[i].setBorder(title);
			}

			days[i].setEditable(false);
			monthPanel.add( days[i] );
		}
		updateSelectedDay(parent.currentDay);

		//fills the rest of the gridbaglayout

		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1.0;   
		c.weightx = 1.0;
		c.anchor = GridBagConstraints.CENTER; 

		c.gridx = 0;
		c.gridwidth =0; 
		c.gridy =2;      
		pane.add(monthPanel, c);
		return pane;
	}

	/**
	 * create a Month view given a specific month and year.
	 * 
	 * @param 		month		The JPanel to be populated.	
	 * @param 		yr			the year to be displayed in the JPanel.
	 * @param		mon			The month to be displayed in the JPanel.		
	 */

	public void addComponentsToPane( JPanel month, int yr, int mon){
		parent.currentYear = yr;
		parent.currentMonth = mon;
		addComponentsToPane(month);
	}

	/**
	 * Changes the year in the title.
	 * 
	 */
	public void updateYear() {
		yTitle.setText( parent.currentYear + "" );
		updateMonth();
		this.updateDays();
	}

	/**
	 * Changes the month in the title.
	 * 
	 */
	public void updateMonth() {
		mTitle.setText( MONTHS[parent.currentMonth-1] );
		days[(parent.currentDay-1) + parent.pastMonthOffset].setBackground(Color.white);
		days[(parent.currentDay-1) + parent.pastMonthOffset].setForeground(Color.black);
		this.updateDays();
	}

	/**
	 * Updates the currently selected day to reflect that it is, indeed, selected.
	 * 
	 * @param s the day which is selected
	 */
	public void updateSelectedDay(int s) {
		
		//System.err.println("updateSelectedDay");
		
		for( int i = 0; i < days.length; i++ ) {
		//days[(CakeGUI.currentDay-1) + CakeGUI.currentMonthOffset].setBackground(Color.white);
		//days[(CakeGUI.currentDay-1) + CakeGUI.currentMonthOffset].setForeground(Color.black);
			days[i].setBackground(Color.white);
			days[i].setForeground(Color.black);
		}
		days[(s-1) + parent.currentMonthOffset].setBackground(
				Color.getHSBColor(selectColor[0], selectColor[1], selectColor[2]));
		days[(s-1) + parent.currentMonthOffset].setForeground(Color.white);
		if( parent.currentYear == parent.TODAYYEAR && parent.currentMonth == parent.TODAYMONTH) {
			days[(parent.TODAYDAY-1) + parent.currentMonthOffset].setForeground(Color.blue);
			DayToErase = (parent.TODAYDAY-1) + parent.currentMonthOffset;
		} else if( DayToErase > 0 ){
			days[DayToErase].setForeground(Color.black);
			DayToErase = -1;
		}
		
		//grays out days of the previous/next months
		int previousMonthDates = curMonths[ ((parent.currentMonth - 2)+12) % 12];
		
		for (int k=parent.currentMonthOffset - 1; k >= 0; k--) {
			days[k].setText(previousMonthDates + "");
			days[k].setForeground(Color.GRAY);
			days[k].setBackground( Color.LIGHT_GRAY);
			previousMonthDates--;
		}	
		

		int count = 1;
		for (int k=(curMonths[parent.currentMonth - 1] + parent.currentMonthOffset); k < 42; k++) {
			days[k].setText(count + "");
			days[k].setForeground(Color.GRAY);
			days[k].setBackground(Color.LIGHT_GRAY);
			count++;
		}	
		
		monthPanel.repaint();
	}

	/**
	 * Changes the arrangement of days in the Month view.
	 * 
	 */
	public void updateDays() {
		curMonths = CakeCal.getMonths(parent.currentYear);             // create the days in the particular month
		ArrayList<Event> curEvents = parent.getEvents();
		float[] n;
		
		for( int i = 0; i < 42; i++ ) {	
			days[i].setBackground(Color.WHITE);
			days[i].setForeground(Color.BLACK);
			n = new float[3];
			Color.RGBtoHSB(212, 208, 200, n);
			days[i].setOpaque(true);
			if(i >= parent.currentMonthOffset && i < curMonths[parent.currentMonth-1] + parent.currentMonthOffset) {
				days[i].setText(i + 1 - parent.currentMonthOffset + "" );
				days[i].setName(i + 1 - parent.currentMonthOffset + "" );
				days[i].addMouseListener(this);
				for( int j = 0; j < curEvents.size(); j++){
					if( curEvents.get(j).getPeriod().start.date.day == i+1-parent.currentMonthOffset ){
						days[i].setText(days[i].getText() + "\n   " + curEvents.get(j).getTitle());
					}
				}
				

			} else {
				days[i].setText(" ");
				days[i].setName(" ");
				days[i].addMouseListener(this);
			}

			days[i].setEditable(false);
			if (i == parent.currentDay + parent.currentMonthOffset - 1) {
				days[i].setBackground(Color.getHSBColor(selectColor[0], selectColor[1], selectColor[2]));
				days[i].setForeground(Color.white);
			}
			
		}
		
		if(DayToErase > 0){
			days[DayToErase].setForeground(Color.black);
			DayToErase = -1;
		}
		
		for (int i=0; i<parent.currentMonthOffset; i++) {
			if (days[i].getBackground() != Color.WHITE) {
				days[i].setBackground(Color.WHITE);
				days[i].setForeground(Color.BLACK);
			}
		}

		for (int i=curMonths[parent.currentMonth - 1]; i<42; i++) {
			if (days[i].getBackground() != Color.WHITE) {
				days[i].setBackground(Color.WHITE);
				days[i].setForeground(Color.BLACK);
			}
		}
		
		//grays out days of the previous/next months
		int previousMonthDates = curMonths[ ((parent.currentMonth - 2)+12) % 12];
		
		for (int k=parent.currentMonthOffset - 1; k >= 0; k--) {
			days[k].setText(previousMonthDates + "");
			days[k].setForeground(Color.GRAY);
			days[k].setBackground( Color.LIGHT_GRAY);
			previousMonthDates--;
		}	
		

		int count = 1;
		for (int k=(curMonths[parent.currentMonth - 1] + parent.currentMonthOffset); k < 42; k++) {
			days[k].setText(count + "");
			days[k].setForeground(Color.GRAY);
			days[k].setBackground(Color.LIGHT_GRAY);
			count++;
		}	
		
		monthPanel.repaint();
		parent.setMonthPanel(monthPanel);
	}

/**
 * changes the view of the MonthView and updates the mini calendar
 * depending on the button that is pressed.
 * 
 * @param e The button that is pressed.
 */
	
	public void actionPerformed( ActionEvent e ){
		if( e.getActionCommand() == ">>" ) {
			parent.updateYear(parent.currentYear + 1);
			updateYear();

		} else if( e.getActionCommand() == "<<" ){
			parent.updateYear(parent.currentYear - 1);
			updateYear();
		} else if( e.getActionCommand() == ">" ){
			if( parent.currentMonth == 12 )
				parent.updateMonth(1);
			else
				parent.updateMonth(parent.currentMonth + 1);
			updateMonth();

		} else if( e.getActionCommand() == "<") {
			if( parent.currentMonth == 1 )
				parent.updateMonth(12);
			else
				parent.updateMonth(parent.currentMonth - 1);
			updateMonth();
		}
	}

	/**
	 * changes directly from month view to day view.
	 * 
	 * @param e The MouseEvent that detects either a single click or
	 * 			a Double Click.
	 */
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2 && !e.getComponent().getName().trim().equals("")) {
			parent.updateDayView();
			parent.viewChanger.show(parent.center, parent.DAY);
		}
		if (!e.getComponent().getName().trim().equals("")) {
			int s = Integer.parseInt(e.getComponent().getName());
			updateSelectedDay(s);
			parent.highlightToday(s);
		}
	}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

	/**
	 * updates events in the month view.
	 * 
	 * @param day The day to be updated.
	 */
	public void updateEvents( int day ){
		Period p = Period.parse( parent.currentYear + "." + parent.currentMonth + "." + day +
				":00.00-"+ parent.currentYear + "." + parent.currentMonth + "." + day + ":00.00");
		List<Event> events = parent.getEvents( p );
		String text = days[day+parent.currentMonthOffset-1].getText();
		for( int x = 0; x < events.size(); x++){
			text = text.concat(events.get(x).getTitle() + " - " + events.get(x).getDescription() + "\n");
		}
		days[day+parent.currentMonthOffset-1].setText(text);
		monthPanel.repaint();
	}

	/**
	 * Detects whether there is an event on the Month View.
	 * 
	 * @param date The date to be checked.
	 * @return i true if the day has events, false otherwise.
	 */
	public boolean hasEvents( SimpleDate date ) {
		boolean i = false;
		if( days[date.day].getText().equals(date.day - parent.currentMonthOffset + 1 + "")) {
			i = false;
		} else if( days[date.day].getText() == "" ) {
			i = false;
		} else {
			i = true;
		}
		
		return i;
	}
	
}
