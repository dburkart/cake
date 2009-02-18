package cake;

import gui.CakeGUI;
import gui.Tray;

import java.awt.CardLayout;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import java.net.URL;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.help.*;

import subsystem.CakeCal;
import subsystem.Period;

/**
 * <h1>Cake Calendar</h1>
 * 
 * The main class of the Cake Calendar System.  This handles all of the GUI elements.  When you start the Cake Calendar, this makes a new
 * instance of itself, CakeCal, and CakeGUI.  When adding a new calendar, a new CakeCal and CakeGUI are associated with each other.
 * 
 * @author Robert Middleton
 */
public class Cake implements WindowListener,ActionListener{

	private  JFrame mainWindow;
	private  CardLayout bigViewChanger;
	private  JPanel main;
	private  CakeGUI currentView;
	//private  JMenu viewCal;
	private  JMenu calendar;
	private  ArrayList<CakeGUI> calendars;
	private  ArrayList<JRadioButtonMenuItem> radioButtons;
	private  String GUITitle = "Cake Calendar GUI - "; 
	private  String oldName;

	private ArrayList<String> invalidNames;

	private final int TODAYYEAR = CakeCal.getDate().year;
	private final int TODAYMONTH = CakeCal.getDate().month;
	private final int TODAYDAY = CakeCal.getDate().day;

	private final Period forTray = Period.parse( TODAYYEAR + "." + TODAYMONTH + "." + TODAYDAY + ":" + "00.00" + "-"
			+ TODAYYEAR + "." + TODAYMONTH + "." + TODAYDAY + ":" + "23.59");
	public Tray todaysEvents;

	private final String OPEN = "Open";
	private final String NEW = "New";
	private final String MERGE = "Merge";
	private final String SAVE = "Save";
	private final String SAVEAS = "Save As...";
	private final String CLOSE = "Close Calendar";
	private final String EXIT = "Exit";
	private final String SETTINGS = "Settings";
	private final String HELP = "Help";
	private final String ABOUT = "About";


	public static void main(String args[]) {		

		CakeCal cal = new CakeCal(args);
		CakeGUI gui = new CakeGUI(cal);

		Cake c = new Cake( gui );

		gui.setParent(c);

	}

	/**Construct
	 * 
	 * @param g - The CakeGUI to originally start the program with.
	 */
	public Cake(CakeGUI g){
		currentView = g;
		bigViewChanger = new CardLayout();
		main = new JPanel(bigViewChanger);
		//System.out.println(currentView.getSettings().getName());
		main.add(currentView.getPanel(),currentView.getSettings().getName());


		//Vlad's Menu
		JMenuBar menuBar = new JMenuBar();

		//Handling ---> FILE menu
		JMenu file = new JMenu("File");
		//NEW
		JMenuItem newCalendar = new JMenuItem(NEW);
		newCalendar.addActionListener(this);
		file.add(newCalendar);
		//OPEN
		JMenuItem open = new JMenuItem(OPEN);
		open.addActionListener(this);
		file.add(open);
		//MERGE
		JMenuItem merge = new JMenuItem(MERGE);
		merge.addActionListener(this);
		file.add(merge);
		//------------Separator-------------//
		file.addSeparator();
		//SAVE
		JMenuItem save = new JMenuItem(SAVE);
		save.addActionListener(this);
		file.add(save);
		//SAVE AS
		JMenuItem saveAs = new JMenuItem(SAVEAS);
		saveAs.addActionListener(this);
		file.add(saveAs);
		//-----------Separator-------------//
		file.addSeparator();
		//CLOSE CALENDAR
		JMenuItem closeCalendar = new JMenuItem(CLOSE);
		closeCalendar.addActionListener(this);
		file.add(closeCalendar);
		//EXIT
		JMenuItem exit = new JMenuItem(EXIT);
		exit.addActionListener(this);
		file.add(exit);

		//Handling ---> EDIT menu
		JMenu edit = new JMenu("Edit");
		//SETTINGS
		JMenuItem settings = new JMenuItem(SETTINGS);
		settings.addActionListener(this);
		edit.add(settings);


		//Handling ---> CALENDAR menu
		calendar = new JMenu("Calendar");
		JRadioButtonMenuItem cal1 = new JRadioButtonMenuItem(currentView.getSettings().getName(), true);
		radioButtons = new ArrayList<JRadioButtonMenuItem>();
		radioButtons.add( cal1 );
		//SETTINGS
		//JMenuItem  = new JMenuItem("Settings");
		cal1.addActionListener(this);
		calendar.add(cal1);


		//Handling ---> HELP menu
		JMenu help = new JMenu("Help");
		//HELP
		JMenuItem helpItem = new JMenuItem(HELP);
		helpItem.addActionListener(this);
		help.add(helpItem);

		JMenuItem aboutItem = new JMenuItem(ABOUT);
		aboutItem.addActionListener(this);
		help.add(aboutItem);


		menuBar.add(file);
		menuBar.add(edit);
		menuBar.add(calendar);
		menuBar.add(help);

		todaysEvents = new Tray( g.getCakeCal().getEvents(forTray),g.getCakeCal().getSettings().getFilename() );

		mainWindow = new JFrame("CakeGUI - " + currentView.getSettings().getFilename());
		mainWindow.add( main );
		mainWindow.setJMenuBar(menuBar);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.addWindowListener(this);
		mainWindow.pack();
		mainWindow.setExtendedState(mainWindow.getExtendedState() | Frame.MAXIMIZED_BOTH);
		mainWindow.setFocusable(true);
		mainWindow.setVisible(true);

		//switchComponent( currentView );
		bigViewChanger.show(main,currentView.getSettings().getName());
		mainWindow.setTitle(GUITitle + currentView.getSettings().getName());
		mainWindow.repaint();

		calendars = new ArrayList<CakeGUI>();
		calendars.add(currentView);

		invalidNames = new ArrayList<String>();
		invalidNames.add(OPEN);
		invalidNames.add(NEW);
		invalidNames.add(MERGE);
		invalidNames.add(SAVE);
		invalidNames.add(SAVEAS);
		invalidNames.add(CLOSE);
		invalidNames.add(EXIT);
		invalidNames.add(SETTINGS);
		invalidNames.add(HELP);
		invalidNames.add(ABOUT);
		invalidNames.add(currentView.getSettings().getName());

	}

	/**Add a new card
	 * 
	 * @param g
	 */
	public void addNewComp( CakeGUI g){
		JMenuItem cal1 = new JMenuItem(g.getSettings().getName());
		calendar.add(cal1);
		main.add(g.getPanel(),g.getSettings().getName());
		mainWindow.repaint();

	}

	/**Add a new card with no pre-set GUI.  Creates a new instance of CakeGUI and CakeCal.
	 * 
	 */
	public void addNewComp(){
		currentView = new CakeGUI(new CakeCal()); //make a new current view
		currentView.setParent(this);
		//the card layout switches based on the title of the calendar;
		//10 calendars named 'New Calendar' don't really accomplish anything
		Object name = JOptionPane.showInputDialog(mainWindow, "Enter a title:"); 
		if( name == null){
			return;
		}

		//check to see if the name is valid
		if( invalidNames.contains((String)name) ){
			JOptionPane.showMessageDialog(mainWindow, "Sorry, that name is not valid.");
			return;
		}
		currentView.getSettings().setName((String)name);


		//makes none of the radio buttons selected.
		for( int x = 0; x < radioButtons.size(); x++){
			radioButtons.get(x).setSelected(false);
		}

		//add another radio button and an action listener, make it selected
		JRadioButtonMenuItem cal1 = new JRadioButtonMenuItem(currentView.getSettings().getName(),true);
		cal1.addActionListener(this);
		calendar.add(cal1);
		radioButtons.add( cal1 );

		//add the new pane to the card layout, update the title
		main.add(currentView.getPanel(), currentView.getSettings().getName());
		mainWindow.setTitle(GUITitle + currentView.getSettings().getName());
		mainWindow.repaint();
		calendars.add(currentView);
		switchComponent(currentView);
	}

	/**Switch to the specified component.
	 * 
	 * @param card
	 */
	public void switchComponent( CakeGUI card ){
		//currentView = card;
		bigViewChanger.show(main,card.getSettings().getName());
		//makes none of the radio buttons selected.
		for( int x = 0; x < radioButtons.size(); x++){
			radioButtons.get(x).setSelected(false);
			if( radioButtons.get(x).getText().equals(card.getSettings().getName())){
				radioButtons.get(x).setSelected(true);
			}
		}
		mainWindow.setTitle(GUITitle + currentView.getSettings().getName());

	}

	/**Called when the window is activated.  Ignored.
	 * 
	 * @param e The window event to do something with.
	 */
	public void windowActivated(WindowEvent e) {

	}

	/**Called when the window is closed.
	 * 
	 * @param e The WindowEvent to do something with
	 */
	public void windowClosed(WindowEvent e) {
	}

	/**Called when the window is closing, and when the user goes to File -> Exit
	 * 
	 * @param e The WindowEvent to do something with.
	 */
	public void windowClosing(WindowEvent e) {
		for( int x = 0; x < calendars.size(); x++){
			currentView = calendars.get(x);
			int selection = 3; //non-existant selection by default
			if( currentView.isModified() ){
				selection = JOptionPane.showConfirmDialog(mainWindow,"Save calendar " + currentView.getSettings().getName() + " before Quitting?");
				//System.out.println(selection);
				if( selection == 0 && !currentView.isModified()){ // 0 = yes
					currentView.saveFile();
				}
				else if( selection == 0 ){
					currentView.saveFileAs();
				}
			}

			if( selection == 2 ){
				return;
			}
		}

		System.exit(0);
	}

	/**Called when the window is deactivated.  Ignored.
	 * 
	 */
	public void windowDeactivated(WindowEvent e) {
	}

	/**Called when the window is Deiconified.  Ignored.
	 * 
	 */
	public void windowDeiconified(WindowEvent e) {

	}

	/**Called when the window is iconified.  Ignored.
	 * 
	 */
	public void windowIconified(WindowEvent e) {

	}

	/**Called when the window is opened.  Ignored.
	 * 
	 */
	public void windowOpened(WindowEvent e) {

	}

	/**Does an action depending on the specified ActionEvent.  Handles switching calendars, and menu bar.  Other actions are performed
	 * by the CakeGUI.
	 * 
	 * @param e The ActionEvent to do something with.
	 */
	public void actionPerformed(ActionEvent e) {
		if( e.getActionCommand().equals("New")){
			//			bigViewChanger.addLayoutComponent((new CakeGUI(new CakeCal())).p, "Calendar 2");
			//JMenuItem cal1 = new JMenuItem("Calendar 2");
			//viewCal.add(cal1);
			addNewComp();
		}
		else if( e.getActionCommand().equals("Exit" )) {
			//System.exit(0);
			this.windowClosing(new WindowEvent(mainWindow,WindowEvent.WINDOW_CLOSING));
		}else if (e.getActionCommand().equals("Open")) {
			CakeGUI oldCurrentView = currentView;
			
			if( currentView.isModified() || currentView.isOpenState() ){
				openNewComp();
			}else{
				currentView.openFile();
			}
			
			//check to see if the name is valid
			if( invalidNames.contains(currentView.getSettings().getName()) ){
				JOptionPane.showMessageDialog(mainWindow, "You already have a calendar with that name open!");
				
				
				bigViewChanger.removeLayoutComponent(currentView.getPanel());
				//remove the calendar from the array list of calendars
				for( int x = 0; x < calendars.size(); x++){
					if( calendars.get(x).equals(currentView)){
						calendars.remove(x);
					}
				}
				//remove the calendar from the array list of radio buttons
				for( int x = 0; x < radioButtons.size(); x ++){
					if( radioButtons.get(x).getText().equals(currentView.getSettings().getName())){
						calendar.remove(radioButtons.get(x));
						radioButtons.remove(x);
					}
				}

				//update the view(defaults to the first calendar)
				if( calendars.size() > 0){
					currentView = calendars.get(0);
					radioButtons.get(0).setSelected(true);
					switchComponent( currentView );
				}
				
				currentView = oldCurrentView;
				return;
			}
			invalidNames.add(currentView.getSettings().getName() );
			
			bigViewChanger.addLayoutComponent(currentView.getPanel(), currentView.getSettings().getName());
			switchComponent(currentView);
			
			mainWindow.setTitle( GUITitle + currentView.getSettings().getName() );
			updateCals();
		} else if (e.getActionCommand().equals("Save")) {

			if (currentView.getSettings().getName().equals(CakeCal.UNTITLED)) {
				currentView.saveFileAs();
			} else currentView.saveFile();

		} else if (e.getActionCommand().equals("Save As...")) {
			currentView.saveFileAs();
		}	else if( e.getActionCommand().equals("Settings")) {
			oldName = currentView.getSettings().getName();
			currentView.showSettings();

		} else if( e.getActionCommand().equals("Close")){
			int selection = 3; //non-existant selection by default
			if( currentView.isModified() ){
				selection = JOptionPane.showConfirmDialog(mainWindow,"Close Calendar " + currentView.getSettings().getName() + "?");
				//System.out.println(selection);
				if( selection == 0 && !currentView.isModified()){ // 0 = yes
					currentView.saveFile();
				}
				else if( selection == 0 ){
					currentView.saveFileAs();
				}
			}

			if( selection == 2 ){
				return;
			}
			bigViewChanger.removeLayoutComponent(currentView.getPanel());
			//remove the calendar from the array list of calendars
			for( int x = 0; x < calendars.size(); x++){
				if( calendars.get(x).equals(currentView)){
					calendars.remove(x);
				}
			}
			//remove the calendar from the array list of radio buttons
			for( int x = 0; x < radioButtons.size(); x ++){
				if( radioButtons.get(x).getText().equals(currentView.getSettings().getName())){
					calendar.remove(radioButtons.get(x));
					radioButtons.remove(x);
				}
			}

			//update the view(defaults to the first calendar)
			if( calendars.size() > 0){
				currentView = calendars.get(0);
				radioButtons.get(0).setSelected(true);
				switchComponent( currentView );
			}
			else{
				bigViewChanger.addLayoutComponent(new JPanel(), "nothing");
			}
		}else if( e.getActionCommand().equals("Help")){

			JHelp helpViewer = null;
			try {
				ClassLoader cl = Cake.class.getClassLoader();
				URL url = HelpSet.findHelpSet(cl, "doc/helpset.hs");
				helpViewer = new JHelp(new HelpSet(cl, url));
				helpViewer.setCurrentID("Cake.Introduction");
			} catch (Exception x) {
				System.out.println("API Help set not found");
			}

			// Create a new frame.
			JFrame frame = new JFrame();
			// Set it's size.
			frame.setBounds(new Rectangle((mainWindow.getWidth()/2)-400, (mainWindow.getHeight()/2)-300, 800, 600));
			// Add the created helpViewer to it.
			frame.getContentPane().add(helpViewer);
			// Set a default close operation.
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			// Make the frame visible.
			frame.setVisible(true);

		} else if (e.getActionCommand().equals("About")) {
			JOptionPane.showMessageDialog(mainWindow,"\t\t\t\t\t\t\t\t\tCake Calendar\n\nMade By:\nHashem Assayari\n" +
				"Dana Burkart\nVladimir Hadzhiyski\nRobert Middleton\nJack Zhang" +
				"\n\n\n2009");
		} else if( e.getActionCommand().equals("Merge")){
			currentView.openFile();
		}


		//search thru the array to find the right card to switch to
		//if there is no calendar with this name, it does not switch
		for( int x = 0; x < calendars.size(); x++ ){
			if( calendars.get(x).getSettings().getName().equals(e.getActionCommand())){
				currentView = calendars.get(x);
				switchComponent( currentView );
			}
		}

		if( currentView.isModified() == true ) {
			todaysEvents.updateTodayEvent( currentView.getCakeCal().getEvents(forTray));
		}

	}

	/**Update the GUI to reflect the new changes in the title of a calendar.
	 * 
	 */
	public void updateGUI(){
		bigViewChanger.removeLayoutComponent(currentView.getPanel());
		main.add(currentView.getPanel(), currentView.getSettings().getName());
		bigViewChanger.show(main, currentView.getSettings().getName());
		mainWindow.setTitle(GUITitle + currentView.getSettings().getName());
		//System.err.println("title-" + currentView.getSettings().getName());
		//replace the old radio button name with a new one for things to work right
		for( int x = 0; x < radioButtons.size(); x++ ){
			if( radioButtons.get(x).getText().equals(oldName)){
				radioButtons.get(x).setText(currentView.getSettings().getName());
			}
		}
	}
	
	/**Called by CakeGUI with the new name of the calendar when saving a file.  Sets up a proper call to updateGUI()
	 * 
	 * @param name The old name of the calendar
	 */
	public void updateGUI( String name ){
		oldName = name;
		updateGUI();
	}

	/**Open a new component; brings up an open dialog instead of prompting for a title.
	 * 
	 */
	public void openNewComp(){
		currentView = new CakeGUI(new CakeCal()); //make a new current view
		currentView.setParent(this);
		//the card layout switches based on the title of the calendar;
		//10 calendars named 'New Calendar' don't really accomplish anything
		currentView.openFile();
		if( currentView.getSettings().getName().equals(CakeCal.UNTITLED)){
			Object name = JOptionPane.showInputDialog(mainWindow, "Rename this Calendar:"); 
			if( name == null ){
				return;
			}
			currentView.getSettings().setName((String)name);
		}


		//makes none of the radio buttons selected.
		for( int x = 0; x < radioButtons.size(); x++){
			radioButtons.get(x).setSelected(false);
		}

		//add another radio button and an action listener, make it selected
		JRadioButtonMenuItem cal1 = new JRadioButtonMenuItem(currentView.getSettings().getName(),true);
		cal1.addActionListener(this);
		calendar.add(cal1);
		radioButtons.add( cal1 );

		//add the new pane to the card layout, update the title
		main.add(currentView.getPanel(), currentView.getSettings().getName());
		mainWindow.setTitle(GUITitle + currentView.getSettings().getName());
		mainWindow.repaint();
		calendars.add(currentView);
		switchComponent(currentView);
	}
	
	/**Update the titles of the radio buttons
	 * 
	 */
	public void updateCals(){
		String formerName = currentView.getCakeCal().getSettings().getFilename();
		for( int x = 0; x < calendars.size(); x++ ){
			radioButtons.get(x).setText(calendars.get(x).getSettings().getName());
		}
		if( currentView.getCakeCal().getSettings().getFilename() != formerName ) {
			todaysEvents.setName(currentView.getCakeCal().getSettings().getFilename());
			todaysEvents.updateTodayEvent( currentView.getCakeCal().getEvents(forTray));
		}
	}
	
	
}
