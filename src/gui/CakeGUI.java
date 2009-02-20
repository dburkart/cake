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
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import cake.Cake;

import subsystem.CakeCal;
import subsystem.Cakeday;
import subsystem.CalendarSettings;
import subsystem.Event;
import subsystem.Period;
import subsystem.SimpleDate;

/**
 * Creates a GUI for the Cake Calendar program.  Each instance is made by Cake.  
 * 
 * @author Robert Middleton
 * @author Jack Zhang
 * @author Dana Burkart
 * @author Vladimir Hadzhiyski
 */
public class CakeGUI implements ActionListener,MouseListener{
	
	private Cake parent;

	//The main window
	private JFrame window; // = new JFrame("Cake GUI");
	//private JMenuItem save = new JMenuItem("Save");

	//JPanels which each contain a separate panel of information 
	//these panels switch out of the main window when the appropriate
	//button(day,week,month) is pressed
	private JPanel day;
	private JPanel week; 
	private JPanel month;
	
	//Made public to update the MonthView class while switching the dates
	public JPanel smallMonth; //the small month on the GUI
	public JPanel center; //the JPanel which changes on the button pressed

	//String representations of the different views
	final static String WEEK = "Week";
	final String DAY = "Day";
	final static String MONTH = "Month";
	final int TODAYYEAR = CakeCal.getDate().year;
	final int TODAYMONTH = CakeCal.getDate().month;
	final int TODAYDAY = CakeCal.getDate().day;

	int currentYear = CakeCal.getDate().year;
	int currentMonth = CakeCal.getDate().month;
	int currentDay = CakeCal.getDate().day;
	int currentMonthOffset = CakeCal.getDayOfWeek(
			SimpleDate.parse(currentYear + "." + currentMonth + "." + 1));
	int pastMonthOffset;

	private ArrayList<Event> events = new ArrayList<Event>();

	public MonthView mview ;//= new MonthView(this);
	public WeekView weekView;// = new WeekView(this);
	public DayView dayView;// = new DayView(true, this);
	private JLabel miniCalTitle = new JLabel( Cakeday.MONTHS[currentMonth - 1] );
	private JLabel yTitle = new JLabel( currentYear + "" );
	private JToolBar buttonToolBar = new JToolBar();

	private JLabel miniDays[] = new JLabel[42];
	private JPanel miniMonth = new JPanel( new GridLayout( 6, 7 ));

	private JPanel bottomRight;

	int DayToErase = -1;
	int curMonths[] = CakeCal.getMonths(currentYear);	// create the days in the particular month

	private CakeCal calendar; 							// the calendar used to handle all of the events

	public CardLayout viewChanger; 									//the card layout used to switch out of the center
	
	private EventDialog panelEvent; // the instance of the dialog box in the side panel
	private SettingsDialog set; 									// the instance of the settings dialog box which pops up
	//public UpdateEventDialog updateEv;
	public EventDialog updateEv;
	
	private JPanel p = new JPanel();
	
	private CardLayout topRight; // the card layout for the top right part of the GUI
	
	//Setting border of the mini calendar
	private TitledBorder title; 
	
	//Setting yearField
	private JTextField yearField;
	
	private TitledBorder miniTitle;
	private JPanel topRightPanel;
	public EventShape selectedEvent;
	
	private boolean openState; //true if the calendar has events
	
	/**Constructor for the CakeGUI
	 * 
	 * @param c The CakeCal to use for this GUI.
	 */
	public CakeGUI(CakeCal c){
		topRight = new CardLayout();
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			
		}
		
		openState = false;
		
		//window = new JFrame("Cake Calendar - " + fileName);
		calendar = c;
		
		//ed = new EventDialog(this);
		//panelEvent = new EventDialog(this);
		panelEvent = new AddEventDialog(this);
		//updateEv = new UpdateEventDialog(this);
		updateEv = new UpdateEventDialog(this);
		
		set = new SettingsDialog(this);
		
		
		//initialize the classes for the different views
		mview = new MonthView(this);
		weekView = new WeekView(this);
		dayView = new DayView(true, this);

		//initialize the three JPanels which will swap out of the center
		initializeCenter();

		//Initialize the buttons in the top right panel
		JButton today = new JButton("Today");
		JButton day = new JButton("Day");
		JButton week = new JButton("Week");
		JButton month = new JButton("Month");
		JButton addEvent = new JButton("Add Event");

		//add action listeners to the buttons
		today.addActionListener(this);
		day.addActionListener(this);
		week.addActionListener(this);
		month.addActionListener(this);
		addEvent.addActionListener(this);

		//add the cards to the top right panel
		topRightPanel = new JPanel( this.topRight );
		topRightPanel.add( panelEvent.thePane, "Add Event" );
		topRightPanel.add( updateEv.thePane, "Update Event" );
		topRight.show(topRightPanel,DAY );
		
		FlowLayout f = new FlowLayout();
		f.setAlignment(FlowLayout.LEFT);
		JPanel top = new JPanel(f);
		buttonToolBar.add(month);
		buttonToolBar.add(week);
		buttonToolBar.add(day);
		buttonToolBar.add(today);
		buttonToolBar.setLayout( new GridLayout() );
		top.add(buttonToolBar);
		buttonToolBar.setPreferredSize(new Dimension(900,32));

		//initialize the bottom right panel
		bottomRight = new JPanel();
		bottomRight.setLayout(new BorderLayout());

		//initialize the top part of the bottom right panel
		JPanel bottomRightTop = new JPanel();
		bottomRightTop.setLayout( new BorderLayout());

		JButton monthPrevious = new JButton( "<" );
		monthPrevious.addActionListener(this);
		JButton monthNext = new JButton( ">" );
		monthNext.addActionListener(this);
		JButton yearPrevious = new JButton( "<<" );
		yearPrevious.addActionListener(this);
		JButton yearNext = new JButton( ">>" );
		yearNext.addActionListener(this);
		
		JPanel leftButtons = new JPanel();
		leftButtons.setLayout(new GridLayout(1,2));
		leftButtons.add(yearPrevious);
		leftButtons.add(monthPrevious);
		bottomRightTop.add(leftButtons, BorderLayout.WEST);
		
		JPanel rightButtons = new JPanel();
		rightButtons.setLayout(new GridLayout(1,2)); 
		rightButtons.add(monthNext);
		rightButtons.add(yearNext);
		bottomRightTop.add(rightButtons, BorderLayout.EAST);
		
		yearField = new JTextField();
		yearField.setFont(new Font("Verdana", Font.BOLD, 12));
		yearField.setHorizontalAlignment(JTextField.CENTER);
		yearField.setDocument(new JTextFieldLimit(4));
		yearField.setText("YYYY");
		yearField.setActionCommand("YYYY");
		yearField.addActionListener(this);	

		bottomRightTop.add(yearField, BorderLayout.CENTER);
		
		//------------TITLED BORDER-------------
		String shortDate = " " + curMonths[currentMonth - 1] + ", " + currentYear + " ";
		
		Border line = BorderFactory.createLineBorder(Color.BLACK);
		title = BorderFactory.createTitledBorder(line, shortDate);
		title.setTitleJustification(TitledBorder.RIGHT);
		title.setTitleFont(new Font("Verdana", Font.BOLD, 12));
		
		bottomRight.setBorder(title);
		//------------END OF---TITLED BORDER-------------
		
		bottomRight.add(bottomRightTop, BorderLayout.NORTH);
		bottomRight.add(this.smallMonth, BorderLayout.CENTER);

		JPanel right = new JPanel();
		
		right.setLayout(new GridBagLayout());
		GridBagConstraints g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = 0;
		g.weighty = 1;
		g.weightx = 1;
		g.fill = GridBagConstraints.BOTH;
		right.add(topRightPanel, g);
		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = 1;
		g.weightx = 1;
		g.weighty = 0;
		g.fill = GridBagConstraints.HORIZONTAL;
		right.add(bottomRight, g);		

		p.setLayout(new BorderLayout());
		p.add(top, BorderLayout.NORTH);
		p.add( BorderLayout.EAST, right );
		p.add( BorderLayout.CENTER, this.center);
		buttonToolBar.setFloatable(false);
		
		

		viewChanger.show(center, MONTH);
		updateCurrentEvents();
		updateDays();
		mview.updateDays();

	}

	/**Initialize the three panels which will switch out in the center
	 * 
	 */
	private void initializeCenter() {
		viewChanger = new CardLayout();

		//initialize the 3 different JPanels which go in the center
		center = new JPanel( viewChanger );


		month = new JPanel();
		mview.addComponentsToPane(month);

		//JPanel weekTitles = new JPanel( new GridLayout(7,7));
		smallMonth = new JPanel( new GridLayout(7,7));
		
		String weekDay[] = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
		
		JLabel label = new JLabel();
		
		for (int i=0; i<7; i++) {
			label = new JLabel(weekDay[i]);
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setFont(new Font("Verdana", Font.BOLD, 12));
			smallMonth.add(label);
		}
		

		//smallMonth = new JPanel( new GridLayout( 6, 7 ));

		for( int i = 0; i < 42; i++ ) {
			if(i >= currentMonthOffset && i  < curMonths[currentMonth - 1] + currentMonthOffset){
				miniDays[i] = new JLabel(i - currentMonthOffset + 1 + "");
				miniDays[i].setName(i - currentMonthOffset + 1 + "");
				miniDays[i].addMouseListener((MouseListener) this);
				miniDays[i].setHorizontalAlignment(JLabel.CENTER);
				if( mview.hasEvents( new SimpleDate(currentMonth, i, currentYear )) == true) {
					miniDays[i].setToolTipText( "You have events scheduled here.");
				} else {
					miniDays[i].setToolTipText( "No Events have been scheduled.");
				}
				
				if (i == currentDay + currentMonthOffset -1) {
					miniDays[i].setFont(new Font("Verdana", Font.BOLD, 12));
					miniDays[i].setOpaque(true);
					
					Border line = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
					TitledBorder title = BorderFactory.createTitledBorder(line);
					miniDays[i].setBorder(title);					
				}
			}

			else {
				miniDays[i] = new JLabel("");
				miniDays[i].setName("");
				miniDays[i].addMouseListener((MouseListener) this);
				miniDays[i].setHorizontalAlignment(JLabel.CENTER);
			}

			smallMonth.add(miniDays[i]);
			
			smallMonth.setBorder(miniTitle);
		}

		for (int j=0; j<42; j++) {
			Border line = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder title = BorderFactory.createTitledBorder(line);
			miniDays[j].setBorder(title);
		}
		
		int count = 1;
		
		for (int k=curMonths[currentMonth - 1]; k < 42; k++) {
			miniDays[k].setName(count++ + "");
		}
		
		
		week = weekView.getPanel();

		day = dayView.getPane();
		day.setFocusable(true);

		center.add(week, WEEK);
		center.add(day, DAY);
		center.add(month, MONTH);
	}

	/**Listens for an action.  Takes the appropriate action depending on what was generated.
	 * 
	 * @param e The action event that is generated
	 */
	public void actionPerformed( ActionEvent e ){
		if( e.getActionCommand() == ">>" ) {
			updateYear(currentYear + 1);
			mview.updateYear();
			dayView.updateDay();
		} else if( e.getActionCommand() == "<<" ){
			if( currentYear <= 1 ){
				JOptionPane.showMessageDialog(window, "Sorry, you can't go back that far!");
				return;
			}
			updateYear(currentYear - 1);
			mview.updateYear();
			dayView.updateDay();
		} else if( e.getActionCommand() == ">" ) {
			if( currentMonth == 12 )
				updateMonth(1);
			else
				updateMonth(currentMonth + 1);
			mview.updateMonth();
			dayView.updateDay();
			
		} else if( e.getActionCommand() == "<") {
			if( currentMonth == 1 )
				updateMonth(12);
			else
				updateMonth(currentMonth - 1);
			mview.updateMonth();
			dayView.updateDay();
		}  else if( e.getActionCommand() == "Today" ) {
			updateMonth(TODAYMONTH);
			updateYear(TODAYYEAR);
			mview.updateMonth();
			mview.updateYear();
			mview.updateSelectedDay(TODAYDAY);
			dayView.updateDay();
			highlightToday(TODAYDAY);
		} else if (e.getActionCommand() == "Day") {
			dayView.updateDay();
		} else if (e.getActionCommand() == "YYYY") {
			
			String yearString = yearField.getText();
			int year = 0;
			
			try {
				//Getting the text field input and checking if it's integer!
				year = Integer.parseInt(yearString);
			} catch (NumberFormatException ex) {
				System.err.println(ex);
			}
			
			//If the user's input is not
			if( year == 0 ){
				JOptionPane.showMessageDialog(window, "Your input value is incorrect! Please, enter an integer value between 1 and 9999!");
				return;
			} else {
				updateYear(year);
				mview.updateYear();
				dayView.updateDay();
			}
			
		} else 
			switchTopRightCard("Add Event");
		
		viewChanger.show(center, (String)e.getActionCommand());
		
	}
	
	/**Switch out the top right card to be either an AddEvent or an UpdateEvent.
	 * 
	 * @param s The string associated with the card that you want to switch to.
	 */
	public void switchTopRightCard(String s) {
		topRight.show(topRightPanel, s);
	}

	/**Open a file.  Called when user goes to File -> Open
	 * 
	 */
	public void openFile() {
		
		openState = true;
		
		JFileChooser chooser = new JFileChooser();
		chooser.addChoosableFileFilter( new CakeFileChooser() );

		chooser.showOpenDialog(window);
		File f = chooser.getSelectedFile();
		if( f == null ) {
			return;
		}
		calendar.loadCal(f.getPath());
		calendar.getSettings().setFilename(f.getPath());

		this.updateCurrentEvents();
		this.updateDays();
		mview.updateDays();
		dayView.updateDay();
	}

	/**Save a file.  Called when the user goes to File -> Save
	 * 
	 */
	public void saveFile() {
		try {
			calendar.saveCal(calendar.getSettings().getFilename());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/**Save a file as something else.  Called when the user goes to File -> Save As
	 * 
	 */
	public void saveFileAs() {
		JFileChooser chooser = new JFileChooser();
		chooser.addChoosableFileFilter( new CakeFileChooser() );
		chooser.setDialogType(JFileChooser.SAVE_DIALOG);
		chooser.showSaveDialog(window);
		File f = chooser.getSelectedFile();
		if (f == null) return;
		try{
			if( calendar.getSettings().getName() == CakeCal.UNTITLED ){
				calendar.getSettings().setName(f.getName() );
				parent.updateGUI( CakeCal.UNTITLED );
			}
			calendar.saveCal( f.getPath() );
		}
		catch (IOException e ){
			System.out.println( e.getMessage() );
		}
		calendar.getSettings().setFilename(f.getPath());
		//save.setEnabled(calendar.modified);
	}

	/**Updates the year.  Will also update the month depending on the year.
	 * 
	 * @param newYear The new year that has been inputed.
	 */
	public void updateYear( int newYear ) {
		currentYear = newYear;
		yTitle.setText(currentYear + "");
		updateMonth(currentMonth);
		panelEvent.update();
		this.updateDays();
	}

	/**A method to update the current month.  Sets the variable that controls the month and
	 * updates the text.
	 * 
	 * @param newMonth an integer value between 0 and 11.
	 */
	public void updateMonth( int newMonth ) {
		pastMonthOffset = currentMonthOffset;
		currentMonthOffset = CakeCal.getDayOfWeek(new SimpleDate((newMonth), 1, currentYear));
		miniDays[(currentDay - 1) + pastMonthOffset].setForeground(Color.black);
		currentMonth = newMonth;
		miniCalTitle.setText( curMonths[currentMonth-1] + ", " + currentYear );
		events = calendar.getEvents(Period.parse(currentYear + "." + currentMonth + ".1:00.00-" + currentYear +
				"." + currentMonth + "." + curMonths[currentMonth-1] + ":24.00"));
		panelEvent.update();

		if( currentDay > (CakeCal.getMonths(currentYear))[currentMonth-1] ){
			currentDay = (CakeCal.getMonths(currentYear))[currentMonth-1];
		}
		updateDays();
		mview.updateMonth();
		dayView.updateDay();
		weekView.updateWeek();
		this.updateDays();
	}

	/**
	 *	On the mini month this method will always highlight the current day in red
	 * 
	 * @param s The new day
	 */
	public void highlightToday(int s) {
		
		// Creates border on the selected day
		for (int i=0; i<42; i++) {
			float[] n = new float[3];
			Color.RGBtoHSB(212, 208, 200, n);
			miniDays[i].setOpaque(true);
			miniDays[i].setForeground(Color.BLACK);
			miniDays[i].setFont(null);
			
			Border line = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder title = BorderFactory.createTitledBorder(line);
			miniDays[i].setBorder(title);
		}
		
		//Creates the font for the selected day
		miniDays[(currentDay-1) + currentMonthOffset].setForeground(Color.black);
		miniDays[(s-1) + currentMonthOffset].setForeground(Color.BLACK);
		miniDays[(s-1) + currentMonthOffset].setFont(new Font("Verdana", Font.BOLD, 12));
		miniDays[(s-1) + currentMonthOffset].setOpaque(true);
		
		//Set the border of the selected date in the mini calendar
		Border line = BorderFactory.createEtchedBorder(Color.BLACK, Color.BLACK);
		TitledBorder title = BorderFactory.createTitledBorder(line);
		miniDays[(s-1) + currentMonthOffset].setBorder(title);
		
		//miniDays[(s-1) + currentMonthOffset].setBackground(Color.WHITE);
		
		currentDay = s;
		if( currentMonth == TODAYMONTH && currentYear == TODAYYEAR ) {
			// Highlights the current day in red when the current month and year is selected
			miniDays[(TODAYDAY-1) + currentMonthOffset].setForeground(Color.RED);
			miniDays[(TODAYDAY-1) + currentMonthOffset].setFont(new Font("Verdana", Font.BOLD, 12));
			DayToErase = (TODAYDAY-1) + currentMonthOffset;
		} else if(DayToErase > 0){
			// changes the current day back to black when the current month and year are not highlighted.
			miniDays[DayToErase].setForeground(Color.black);
			DayToErase = -1;
		}
		

		//This var holds the number of dates of the previous month
		int previousMonthDates = curMonths[ ((currentMonth - 2)+12) % 12];
		
		for (int k=currentMonthOffset - 1; k >= 0; k--) {
			miniDays[k].setText(previousMonthDates + "");
			miniDays[k].setForeground(Color.GRAY);
			previousMonthDates--;
		}	
		
		//Testing	
		
		
		//Testing
		//System.out.println("curMonths[currentMonth - 1] ==" + curMonths[currentMonth - 1]);

		int count = 1;
		for (int k=(curMonths[currentMonth - 1] + currentMonthOffset); k < 42; k++) {
			miniDays[k].setText(count + "");
			miniDays[k].setForeground(Color.GRAY);
			count++;
		}		
		//Testing
		
		smallMonth.repaint();
		
		miniMonth.repaint();
		panelEvent.update();
		weekView.updateWeek();
	}


	/**Updates the days, and how they are viewed.
	 * 
	 */
	public void updateDays() {

		curMonths = CakeCal.getMonths(currentYear);             // create the days in the particular month

		for( int i = 0; i < 42; i++ ) {
			if( i >= currentMonthOffset && i < curMonths[currentMonth - 1] + currentMonthOffset ){	
				miniDays[i].setText( i - currentMonthOffset + 1 +"" );
				miniDays[i].setName( i - currentMonthOffset + 1 + "");
				if( mview.hasEvents( new SimpleDate(currentMonth, i, currentYear )) == true) {
					miniDays[i].setToolTipText( "You have events scheduled here.");
				} else {
					miniDays[i].setToolTipText( "No events have been scheduled");
				}
				
				Border line = BorderFactory.createEtchedBorder(Color.BLACK, Color.BLACK);
				TitledBorder title = BorderFactory.createTitledBorder(line);
				miniDays[currentDay + (currentMonthOffset - 1)].setBorder(title);
				miniDays[currentDay + (currentMonthOffset - 1)].setFont(new Font("Verdana", Font.BOLD, 12));
			} else {
				miniDays[i].setToolTipText(null);
				miniDays[i].setText( " " );
				miniDays[i].setName( " " );
			}
			
			if (i == currentDay + currentMonthOffset - 1) {
				miniDays[i].setForeground(Color.red);
			}
			
			//Setting properly (one-digit) alignment in the mini calendar
			if (miniDays[i].getText().length() == 1) {
				String s = " " + miniDays[i].getText();
				miniDays[i].setText(s);
			}
			
			float[] n = new float[3];
			Color.RGBtoHSB(212, 208, 200, n);
			miniDays[i].setOpaque(true);
			miniDays[i].setBackground(Color.WHITE);
			miniDays[i].setForeground(Color.BLACK);
			miniDays[i].setFont(null);
			
			Border line = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder title = BorderFactory.createTitledBorder(line);
			miniDays[i].setBorder(title);
			
			if( currentMonth == TODAYMONTH && currentYear == TODAYYEAR ) {
				miniDays[(TODAYDAY-1) + currentMonthOffset].setForeground(Color.RED);
				miniDays[(TODAYDAY-1) + currentMonthOffset].setFont(new Font("Verdana", Font.BOLD, 12));
			}
			
		}

		if(DayToErase > 0){
			miniDays[DayToErase].setForeground(Color.black);
			DayToErase = -1;
		}

		
		//This var holds the number of dates of the previous month
		int previousMonthDates = curMonths[ ((currentMonth - 2)+12) % 12];
		
		float[] n = new float[3];
		Color.RGBtoHSB(200, 200, 200, n);
		
		for (int k=currentMonthOffset - 1; k >= 0; k--) {
			miniDays[k].setText(previousMonthDates + "");
			miniDays[k].setForeground(Color.GRAY);
			miniDays[k].setBackground(Color.getHSBColor(n[0], n[1], n[2]));
			previousMonthDates--;
		}	
		
		int count = 1;
		for (int k=(curMonths[currentMonth - 1] + currentMonthOffset); k < 42; k++) {
			miniDays[k].setText(count + "");
			miniDays[k].setForeground(Color.GRAY);
			miniDays[k].setBackground(Color.getHSBColor(n[0], n[1], n[2]));
			count++;
		}		
		
		String shortDate = " " + Cakeday.MONTHS[currentMonth - 1];
		title.setTitle(shortDate);
		
		bottomRight.setBorder(title);
		
		bottomRight.repaint();
		//------------END OF---TITLED BORDER-------------
		
		//-------Updates the year in the JTextField in the mini calendar------//
		yearField.setText("" + currentYear);
		//-------Updates the year in the JTextField in the mini calendar------//
		
		smallMonth.repaint();
		
		weekView.updateWeek();
		dayView.updateDay();
		
		miniMonth.repaint();
		

	}


	/**Called when there is a mouse event.  Updates the mini-calendar.
	 * 
	 * @param e The mouse event generated.
	 */
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2 && !e.getComponent().getName().trim().equals("")) {
			viewChanger.show(center, DAY);
			int s = Integer.parseInt(e.getComponent().getName());
			mview.updateSelectedDay(s);
			dayView.updateDay();
			highlightToday(s); 
		}
		if (!e.getComponent().getName().trim().equals("") ) {
			int s = Integer.parseInt(e.getComponent().getName());
			mview.updateSelectedDay(s);
			dayView.updateDay();
			highlightToday(s);	
		}
	}

	/**Method to handle when the mouse enters a particular area.  Does not do anything.
	 * 
	 * @param e The mouse event generated.
	 */
	public void mouseEntered(MouseEvent e) {}

	/**Method to handle when the mouse exits a particular area.  Does not do anything.
	 * 
	 * @param e The mouse event generated.
	 */
	public void mouseExited(MouseEvent e) {}

	/**Method to handle when the mouse is clicked.  Does not do anything.
	 * 
	 * @param e The mouse event generated.
	 */
	public void mousePressed(MouseEvent e) {}

	/**Method to handle when the mouse is released.  Does not do anything.
	 * 
	 * @param e The mouse event generated.
	 */
	public void mouseReleased(MouseEvent e) {

	}

	/**Get the events for a specific period.
	 * 
	 * @param p The period to get events for
	 * @return The events for the specific period
	 */
	public List<Event> getEvents( Period p)  {
		return calendar.getEvents(p);
	}

	/**Adds and event to the event database and updates the month view to reflect the change
	 * 
	 * @param e The event to add to the event database
	 */
	public void addEvent( Event e){
		events.add( calendar.addEvent(e) );
		this.updateMonth(currentMonth);
		mview.updateDays();
	}
	
	/**Updates an event.  Simply calls CakeCal.updateEvent()
	 * 
	 * @param e The event to update.
	 */
	public void updateEvent(Event e) {
		calendar.updateEvent(e);
	}
	
	/**Delete an event.  Simply calls CakeCal.deleteEvent()
	 * 
	 * @param e The event to delete.
	 */
	public void deleteEvent(Event e) {
		calendar.deleteEvent(e);
	}

	/**Updates the current events
	 * 
	 */
	public void updateCurrentEvents() {
		events = calendar.getEvents(Period.parse(currentYear + "." + currentMonth + ".1:00.00-" + currentYear +
				"." + currentMonth + "." + curMonths[currentMonth-1] + ":24.00"));
	}
	
	/**Return the settings used by this calendar.
	 * 
	 * @return The settings used by this calendar.
	 */
	public CalendarSettings getSettings(){
		return calendar.getSettings();
	}
	
	/**Set the settings used by this calendar.
	 * 
	 * @param c The CalendarSettings that this calendar should used.
	 */
	public void setSettings( CalendarSettings c){
		calendar.setSettings(c);
	}
	
	/**Show the settings dialog.
	 * 
	 */
	public void showSettings(){
		set.showDialogBox();
	}
	
	/**Return the month panel used by this CakeGUI for the MonthView
	 * 
	 * @return The month panel used.
	 */
	public JPanel getMonthPanel(){
		return month;
	}
	
	/**Set the month panel to a new panel.
	 * 
	 * @param m The new Month panel to use.
	 */
	public void setMonthPanel( JPanel m ){
		month = m;
	}
	
	/**Calls CakeCal modified.
	 * 
	 * @return True if the calendar has been modified, else false
	 */
	public boolean isModified(){
		return calendar.modified;
	}
	
	/** Return an array list of events that the calendar is currently keeping track of
	 * 
	 * @return The array list of events that the calendar is keeping track of
	 */
	public ArrayList<Event> getEvents(){
		return events;
	}
	
	/**Return the instance of JPanel that this calendar uses
	 * 
	 * @return The instance of JPanel that all the elements are in for this calendar.
	 */
	public JPanel getPanel(){
		return p;
	}
	
	/**Gets the current date that is displayed by this CakeGUI.
	 * 
	 * @return A new SimpleDate which represents the current day.
	 */
	public SimpleDate getCurrentDate() {
		return new SimpleDate(this.currentMonth, this.currentDay, this.currentYear);
	}
	
	/**Called by the settings dialog when it is done getting the new settings.
	 * 
	 */
	public void finished(){
		parent.updateGUI();
	}
	
	/**Set the parent of this CakeGUI.  The parent is defined as the instance of Cake which created this CakeGUI.
	 * 
	 * @param c The instance of Cake to set the parent of this CakeGUI to.
	 */
	public void setParent(Cake c){
		parent = c;
	}
	
	public CakeCal getCakeCal() {
		return calendar;
	}
	
	public boolean isOpenState(){
		return openState;
	}
	
	public void updateDayView(){
		dayView.updateDay();
		day.setFocusable(true);
	}

}



