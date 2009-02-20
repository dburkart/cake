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

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import subsystem.Event;
import subsystem.Period;

/**
 * This class will create an icon for the system tray as well as handle
 * any notifications for the calendar.
 * 
 * @author Jack Zhang
 *
 */
public class Tray implements ActionListener {
	
	public SystemTray tray;
	public static ArrayList<Event> today;
	public TrayIcon trayIcon;
	private String name;
	public Clock blcok = new Clock();
	
	public static void main(String[] args) {
		Tray tray = new Tray();
	}
	
	/**
	 * empty constructor
	 */
	public Tray() {
		this.initialize();
	}
	
	/**
	 * Default constructor, sets today's events
	 * 
	 * @param today
	 */
	
	public Tray( ArrayList<Event> today, String name ) {
		this.name = name;
		this.today = today;
		this.initialize();
	}
	
	/**
	 * intializes the System Tray.
	 */
	public void initialize() {
		if (!SystemTray.isSupported()) {
			System.out.println("SystemTray is not supported");
			return;
		} else if( tray != SystemTray.getSystemTray() ) {
		
		ActionListener exitListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		};
	            
		PopupMenu popup = new PopupMenu();
		MenuItem defaultItem = new MenuItem("Exit");
		defaultItem.addActionListener(exitListener);
		MenuItem openCakeItem = new MenuItem("Open Cake");
		openCakeItem.addActionListener(this);
		popup.add(defaultItem);
		popup.add(openCakeItem);
	    
		tray = SystemTray.getSystemTray();
		
		Image img = Toolkit.getDefaultToolkit().getImage("doc/images/cake.jpg");
		trayIcon = new TrayIcon(img, "Calendar", popup);
		trayIcon.setImageAutoSize(true);
		
		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			System.out.println("TrayIcon could not be added.");
			return;
		}
		this.updateTodayEvent(today);
		}
	}
	
	/**
	 * updates today's events
	 * 
	 * @param today
	 */
	public void updateTodayEvent( ArrayList<Event> today ) {
		this.today = today;
		
		if( today == null || today.size() == 0 ) {
			trayIcon.setToolTip( "No events have been scheduled for today");
		} else {
			int cnt = today.size();
			String singleorplural;
			if( today.size() == 1) {
				singleorplural = "event";
			} else {
				singleorplural = "events";
			}
			trayIcon.displayMessage( "Events", "You have " + cnt + " " + singleorplural + " scheduled for today \n" 
					+ this.eventToString(), TrayIcon.MessageType.INFO);


		    Period p = today.get(0).getPeriod();
		    System.out.println( p.start.time.hour );

			trayIcon.setToolTip( "You have " + cnt + " " + singleorplural);
		}
		
	}
	
	/**
	 * creates the format of the notification window.
	 * 
	 * @return
	 */
	public String eventToString() {
		String altogether = "";
		
	    Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat( "HH");
		int systemHour = Integer.parseInt( sdf.format(cal.getTime()));
		boolean enterString = false;
		int eventNum = 0;
		Event temp = null;
		
		if( today != null ) {
			for( int i = 0; i < today.size(); i++ ) {
				if(today.get(i).getPeriod().start.time.hour > systemHour) {
					temp = today.get(i);
					eventNum = i+1;
					i = today.size();
					enterString = true;
				}
			}
		}
		
		if( enterString == true ) {
			altogether = "Your " + "event #" + eventNum + " in Calendar " + name + " is \n";
			altogether = altogether + "Event: " + temp.getTitle() + "\n";
			altogether = altogether + "Time: " + temp.getPeriod().start.time.toString() + "\n";
			altogether = altogether + "Location: " + temp.getLocation() + "\n";
			altogether = altogether + "Description " + temp.getDescription() + "\n";
		} else {
			altogether = "All your events are already done.";
		}
		return altogether;
	}

	/**
	 * handles options in the pop-up menu.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "Open Cake") {
			try {
				Process p = Runtime.getRuntime().exec("java -jar Cake.jar");
			} catch (Exception m) {
				System.out.println(m.getMessage());
			}
		}
	}
	
	/**
	 * tells the notification of the window the name of the calendar.
	 * 
	 * @param newName
	 */
	
	public void setName( String newName ) {
		name = newName;
	}
}
