package gui;


import gui.widgets.AdvancedRecurrenceWidget;
import gui.widgets.FormatBox;
import gui.widgets.NthDayOfMonthRecurrenceWidget;
import gui.widgets.NthDayRecurrenceWidget;
import gui.widgets.RecurrenceWidget;
import gui.widgets.WeeklyRecurrenceWidget;

import java.awt.ComponentOrientation;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import subsystem.Event;
import subsystem.SimpleDate;


/**Add event dialog.  Superclass for all of the event dialogs.
 * 
 * @author vrh8879 (Vladimir Hadzhiyski)
 * @author rxm6930 (Robert Middleton)
 *
 */
public abstract class EventDialog implements ActionListener {
	final static boolean shouldFill = true;					//default gridbaglayout constraints
	final static boolean shouldWeightX = true;
	final static boolean RIGHT_TO_LEFT = false;

	//-- Variables that hold the text fields so that we can access them later
	protected FormatBox startDate;
	protected FormatBox endDate;
	protected JComboBox startTime;
	protected JComboBox endTime;
	protected JTextField title;
	protected JTextField location;
	protected JTextArea description;
	protected JCheckBox recurring;
	protected JComboBox recurringType;
	
	//-- Recurring event widgets --//
	protected RecurrenceWidget currentWidget;
	
	protected WeeklyRecurrenceWidget weeklyWidget;
	protected NthDayOfMonthRecurrenceWidget nthDayOfMonthWidget;
	protected NthDayRecurrenceWidget nthDayWidget;
	protected AdvancedRecurrenceWidget advancedWidget;

	protected CakeGUI parent;

	protected JDialog dialog;
	protected JFrame theFrame = new JFrame();
	protected JPanel thePane;  //the main panel of this dialog box.

	protected boolean shown; // if this dialog box has been shown yet(only relevant if this pops up)

	//-- TODO - Move this array to Cakeday, and make static
	protected String[] militaryTimes;
	
	protected Event globalE; // Only relevant for UpdateEvent



	/**Constructor
	 * 
	 * @param parent The CakeCal to add the event to.
	 */
	public EventDialog( CakeGUI parent ){
		this.parent = parent;
		shown = false;

		militaryTimes = new String[48];

		for( int x = 0; x < 48; x++ ){
			if( x % 2 == 0 ){
				militaryTimes[x] = x/2 + ".00";	
			}
			else{
				militaryTimes[x] = x/2 + ".30";
			}
		}
		
		//we only want one instance of each variable, so all variables are
		//instatntiated here and added later.
		startDate = new FormatBox("date");
		startTime = comboBox(16);
		//checkBox = new JCheckBox("All day event");
		endDate = new FormatBox("date");
		endTime = comboBox(20);
		
		thePane = new JPanel( new GridLayout(1,1) );
		thePane.add(addComponentsToPane());
		dialog = new JDialog(theFrame,"Add Event");

	}
	


	/**
	 * Creates a main panel for the dialog box

	 * 
	 * @return		pane				A new JPanel filled with all the elements of the dialog box.
	 */
	private JPanel addComponentsToPane() {
		//this.pane = pane;
		JPanel gridPanel = new JPanel();
		gridPanel.setLayout(new GridBagLayout()); 

		if( RIGHT_TO_LEFT ) {
			gridPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		}
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 0;
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		gridPanel.add( middlePanel(), c );
		
		c.gridx = 0;
		c.gridy = 1;
		c.weighty = 1;
		c.weightx = 1;
		c.fill = GridBagConstraints.BOTH;
		gridPanel.add( bottomPanel(), c );
		
		c.gridx = 0;
		c.gridy = 2;
		c.weighty = 0;
		gridPanel.add( buttonsPanel(), c);

		return gridPanel;
	}





	/** Returns a JPanel with the middle part of the dialog box, the start/end date/time
	 * 
	 * 
	 * @author vrh88879
	 * 
	 * @return It returns start/end date/time panel
	 */
	private JPanel middlePanel() {
		JPanel pane = new JPanel();
		//thePane = pane;
		pane.removeAll();

		if (RIGHT_TO_LEFT) {														//set default gridbaglayout constraint values
			pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		}

		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		//c.insets = new Insets(0, 10, 1, 0); // padding
		c.insets = new Insets(0, 0, 10, 10); // padding
		if (shouldFill) {
			//natural height, maximum width
			c.fill = GridBagConstraints.HORIZONTAL;
		}

	//	c.weightx = 0.5;
	//	c.weighty = 0.5; 
		JLabel label = new JLabel("Subject:     ");		// 1st Line, shows subject title
		c.gridx = 1;
		c.gridy = 0;
		pane.add( label, c );
		
		title = new JTextField(10);						
		c.gridx = 2;
		c.gridy = 0;
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		pane.add( title, c );
		
		label = new JLabel("Location:    ");			// 2nd Line, shows title title
		c.gridx = 1;
		c.gridy = 1;
		pane.add( label, c);
		
		location = new JTextField(10);
		c.gridx = 2;
		c.gridy = 1;
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		pane.add( location, c );
		
		label = new JLabel(" ");						// 3rd Line, fills empty space and proper format label
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		pane.add(label, c);

		label = new JLabel("Start");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 2;
		pane.add(label, c);
		
		label = new JLabel("End");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 2;
		pane.add(label, c);

		label = new JLabel(" ");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 2;
		pane.add(label, c);

		label = new JLabel(" ");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 2;
		pane.add(label, c);
		
		startDate.setColumns(10);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 3;
		pane.add(startDate, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;			// 5th Line, shows start time info
		c.gridx = 1;
		c.gridy = 4;		
		pane.add(startTime, c);

		SimpleDate d = new SimpleDate(parent.currentMonth, parent.currentDay, parent.currentYear);
		startDate.setData(d.format());

		
		endDate.setData(d.format());
		endDate.setColumns(10); //Setting the width of the 1st textfield - start time
		endDate.setEnabled(false);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 3;
		pane.add(endDate, c);
	
		c.fill = GridBagConstraints.HORIZONTAL;			// 7th Line, shows end time info
		c.gridx = 2;
		c.gridy = 4;
		pane.add(endTime, c);

		label = new JLabel(" ");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 6;
		pane.add(label, c);
		
		label = new JLabel("Recurring? ");
		c.gridx = 1;
		c.gridy = 6;
		pane.add(label, c);
		
		recurring = new JCheckBox();
		recurring.addActionListener(this);
		recurring.setActionCommand("recurring");
		c.gridx = 2;
		c.gridy = 6;
		pane.add(recurring, c);
		
		recurringType = new JComboBox();
		recurringType.addActionListener(this);
		recurringType.setActionCommand("recurringTypeChanged");
		recurringType.setVisible(false);
		recurringType.addItem("Week Days");
		recurringType.addItem("N Days");
		recurringType.addItem("Nth Day of Month");
		recurringType.addItem("Custom");
		c.gridx = 1;
		c.gridy = 7;
		c.fill = GridBagConstraints.HORIZONTAL;
		pane.add(recurringType, c);

		weeklyWidget = new WeeklyRecurrenceWidget();
		weeklyWidget.setVisible(false);
		c.gridx = 1;
		c.gridy = 8;
		c.gridwidth = 2;
		c.weightx = 0;
		c.fill = GridBagConstraints.NONE;
		pane.add(weeklyWidget, c);
		
		nthDayOfMonthWidget = new NthDayOfMonthRecurrenceWidget();
		nthDayOfMonthWidget.setVisible(false);
		c.gridx = 1;
		c.gridy = 9;
		c.gridwidth = 2;
		c.weightx = 0;
		c.fill = GridBagConstraints.NONE;
		pane.add(nthDayOfMonthWidget, c);
		
		nthDayWidget = new NthDayRecurrenceWidget();
		nthDayWidget.setVisible(false);
		c.gridx = 1;
		c.gridy = 9;
		c.gridwidth = 2;
		c.weightx = 0;
		c.fill = GridBagConstraints.NONE;
		pane.add(nthDayWidget, c);
		
		advancedWidget = new AdvancedRecurrenceWidget();
		advancedWidget.setVisible(false);
		c.gridx = 1;
		c.gridy = 9;
		c.gridwidth = 2;
		c.weightx = 0;
		c.fill = GridBagConstraints.NONE;
		pane.add(advancedWidget, c);
		
		
		return pane;
	}	



	/** Returns the 'Description' field and the label.
	 * 
	 * 
	 * @author vrh88879
	 * 
	 * @return It returns 'Description' and the JTextBox panel -> bottomPanel
	 */
	private JPanel bottomPanel() {
		
		JPanel pane = new JPanel();

		if (RIGHT_TO_LEFT) {														//set default gridbaglayout constraint values
			pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		}

		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		//c.insets = new Insets(0, 10, 1, 0); // padding
		//c.insets = new Insets(0, 0, 10, 10); // padding
		if (shouldFill) {
			//natural height, maximum width
			c.fill = GridBagConstraints.HORIZONTAL;
		}
		c.insets = new Insets(0, 0, 10, 10);
/*
		c.fill = GridBagConstraints.HORIZONTAL;			//8th line, shows the All Day checkbox
		c.gridx = 0;
		c.gridy = 0;
		
		pane.add(checkBox, c);		
*/
		
		JLabel label = new JLabel("Description:"); //9th Line, shows the description of the event
		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridx = 0;
		c.gridy = 2;
		pane.add(label, c);
		
		description = new JTextArea(5, 20);
		description.setLineWrap(true);
		description.setWrapStyleWord(true);
		c.gridx = 0;
		c.gridy = 3;
		c.weightx = 2;
		c.weighty = 3;
		c. fill = GridBagConstraints.BOTH;

		JScrollPane scrollPane = new JScrollPane(description);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		pane.add( scrollPane, c );
		

		return pane;
	}

	/** Makes a JPanel with two buttons, 'Add Event' and 'Cancel'
	 * 
	 * 
	 * 
	 * @return JPanel with two buttons - 'Add Event' and 'Cancel'
	 */
	protected abstract JPanel buttonsPanel();

	private JComboBox comboBox(int selectedIndex) {
		String[] times = {"12:00 AM", "12:30 AM", "1:00 AM", "1:30 AM", "2:00 AM", "2:30 AM",
				"3:00 AM", "3:30 AM", "4:00 AM", "4:30 AM", "5:00 AM", "5:30 AM",
				"6:00 AM", "6:30 AM", "7:00 AM", "7:30 AM", "8:00 AM", "8:30 AM",
				"9:00 AM", "9:30 AM", "10:00 AM", "10:30 AM", "11:00 AM", "11:30 AM",
				"12:00 PM", "12:30 PM", "1:00 PM", "1:30 PM", "2:00 PM", "2:30 PM",
				"3:00 PM", "3:30 PM", "4:00 PM", "4:30 PM", "5:00 PM", "5:30 PM",
				"6:00 PM", "6:30 PM", "7:00 PM", "7:30 PM", "8:00 PM", "8:30 PM",
				"9:00 PM", "9:30 PM", "10:00 PM", "10:30 PM", "11:00 PM", "11:30 PM"};
		JComboBox box = new JComboBox(times);
		box.setSelectedIndex(selectedIndex);

		return box;
	}



	/**Displays the dialog box.  
	 * 
	 */
	public void showDialogBox() {

		if( shown ){
			SimpleDate d = new SimpleDate(parent.currentMonth, parent.currentDay, parent.currentYear);
			startDate.setData(d.format());
			endDate.setData(d.format());
			dialog.setVisible( true );
			return;
		}

		
		JPanel pane = new JPanel( new GridLayout(1,1) );
		//JPanel pane = new JPanel(thePane);
		pane.add(addComponentsToPane());
		dialog.add(pane);
		//dialog.add(thePane);
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		dialog.pack();
		dialog.setResizable(true);
		dialog.setSize(320,420);
		
		dialog.setLocationRelativeTo(null);
		dialog.requestFocus();
		//dialog.setAlwaysOnTop(true);
		dialog.setFocusableWindowState(true);
		dialog.setVisible(true);
		
		shown = true;

	}

	/**Makes it invisible!  You must call this, or else the dialog box will not dissapear.
	 * Note: this simply makes the dialog invisible, it does not actually end the process as that
	 * causes the CakeGUI to close as well.
	 */
	public void close(){
		//startDate.reset();
		//endDate.reset();
		title.setText("");
		description.setText("");
		location.setText("");
		recurring.setSelected(false);
		recurringType.setVisible(false);
		recurringType.setSelectedIndex(0);
		if (currentWidget != null) {
			currentWidget.setVisible(false);
			currentWidget.reset();
			currentWidget = null;
		}
		if(dialog.isVisible()){
			dialog.setVisible(false);
		}
	}
	
	/**Update the current month and current day from the CakeGUI.
	 * 
	 */
	public void update(){
		SimpleDate d = new SimpleDate(parent.currentMonth, parent.currentDay, parent.currentYear);
		startDate.setData(d.format());
		endDate.setData(d.format());
	}
	
	/**Fill in the dialog with the information passed in.
	 * 
	 * @param e The event to fill in the information for.
	 */
	public void fillIn(Event e){
			globalE = e;
			title.setText(e.getTitle());
			description.setText(e.getDescription());
			startDate.setData(e.getPeriod().start.date.format());
			endDate.setData(e.getPeriod().end.date.format());
			location.setText(e.getLocation());
			
			int hour = e.getPeriod().start.time.hour;
			int min = e.getPeriod().start.time.minutes;
			
			if( min == 30 ){
				startTime.setSelectedIndex((hour * 2 )+ 1);
			}else{
				startTime.setSelectedIndex(hour * 2);
			}
			
			hour = e.getPeriod().end.time.hour;
			min = e.getPeriod().end.time.minutes;
			if( min == 30 ){
				endTime.setSelectedIndex((hour * 2) + 1);
			}else{
				endTime.setSelectedIndex((hour*2)  );
			}
			
			System.out.println("E: " + e.getRecurringType());
			if (e.getRecurringType().equals("0") && e.getRecurringType().equals(null)) {
				recurring.setSelected(true);
				recurringType.setVisible(true);
				recurringType.setSelectedItem("Custom");
				this.advancedWidget.setREx(e.getRecurringType());
				endDate.setEnabled(true);
			}
		}

}
