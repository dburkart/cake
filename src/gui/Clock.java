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
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import subsystem.CakeCal;
import subsystem.Cakeday;

import java.util.Calendar;

/**
 * Little clock widget prototype. Nothing being done with this as of right now
 * 
 * @author Vladimir Hadzhiyski
 */
public class Clock extends JPanel {
    
	javax.swing.Timer m_t;
    private JLabel label = new JLabel();
    
    private int hour;
    private int minute;
    private int second;
    private Calendar now;
    
	private int currentYear = CakeCal.getDate().year;
	private int currentMonth = CakeCal.getDate().month;
	private int currentDay = CakeCal.getDate().day;
    
	/**
	 * Constructor
	 */
    public Clock() {
    	setLayout(new FlowLayout(FlowLayout.CENTER));
    	
    	now = Calendar.getInstance();
    	
    	int dayOfWeek = now.get(Calendar.DAY_OF_WEEK);
    	
    	String todaysDate = " " + Cakeday.DAYS[dayOfWeek - 1] + ", " + Cakeday.MONTHS[currentMonth - 1] + " " + currentDay + 
    				   ", " + currentYear + " ";
    	
		//Border settings 
		Border line = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		TitledBorder title = BorderFactory.createTitledBorder(line, todaysDate);
		title.setTitleJustification(TitledBorder.CENTER);
		title.setTitleFont(new Font("Verdana", Font.BOLD, 12));
		setBorder(title);
		
    	
    	hour = now.get(Calendar.HOUR_OF_DAY);
    	minute = now.get(Calendar.MINUTE);
    	second = now.get(Calendar.SECOND);
    	
    	String currentTime = getHour(hour) + ":" + getMinute(minute) + ":" + getSecond(second);
    	label.setText(currentTime);
        label.setFont(new Font("Verdana", Font.PLAIN, 26));

        add(label);
        
        //... Create a 1-second timer.
        m_t = new javax.swing.Timer(1000, new ClockTickAction());
        m_t.start();  // Start the timer
    }

    /**
     * Description: Nested class which is designed to be executed every second!
     *              It gets the user's current time and sets it in a JLabel.
     * 
     * @author vrh8879 (Vladimir Hadzhiyski)
     *
     */
    private class ClockTickAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //... Get the current time.
            now = Calendar.getInstance();
            hour = now.get(Calendar.HOUR_OF_DAY);
            minute = now.get(Calendar.MINUTE);
            second = now.get(Calendar.SECOND);
            
            String currentTime = getHour(hour) + ":" + getMinute(minute) + ":" + getSecond(second);
			
            label.setFont(new Font("Verdana", Font.PLAIN, 26));
            label.setText(currentTime);
        }
    }
    
    /**
     * 
     * @param hour The user's hours - not formatted!
     * 
     * @return The user's current hour - formatted!
     */
    private String getHour(int hour) {
    	String hourString = Integer.toString(hour);
    	
    	if (hour < 10) {
    		hourString = "0" + hour;
    	}
    	
    	return hourString;
    }
    
    /**
     * 
     * @param minute The user's minutes - not formatted!
     * 
     * @return The user's current minute - formatted!
     */
    private String getMinute(int minute) {
    	String minuteString = Integer.toString(minute);
    	
    	if (minute < 10) {
    		minuteString = "0" + minute;
    	}
    	
    	return minuteString;
    }
    
    /**
     * 
     * @param second The user's seconds - not formatted!
     * 
     * @return The user's current seconds - formatted!
     */
    private String getSecond(int second) {
    	String secondString = Integer.toString(second);
    	
    	if (second < 10) {
    		secondString = "0" + second;
    	}
    	
    	return secondString;
    }
    
} //End of Clock()
