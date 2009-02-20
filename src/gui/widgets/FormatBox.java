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

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

import subsystem.SimpleDate;

/**
 * A text box which will automatically format data.
 * 
 * @author Dana Burkart
 */
public class FormatBox extends JTextField implements KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String data = "";
	String type = "date";

	/**Default constructor.  Creates a new Format Box.
	 * 
	 */
	public FormatBox() {
		this.addKeyListener(this);
		this.data = "";
	}
	
	/**Constructor which takes in a string.
	 * 
	 * @param type The type of data that this format box will handle.
	 */
	public FormatBox(final String type) {
		this.addKeyListener(this);
		data = "";
		this.type = new String(type);
	}
	
	/**Set the data in the text box.
	 * 
	 * @param s The string to set the data in the box to.
	 */
	public void setData(final String s) {
		//System.out.println("Do we get in here?");
		if (type.equals("date")) {
			final SimpleDate d = SimpleDate.parse(s);
			final String year = new String(10000 + d.year + "").substring(1);
			final String month = new String(100 + d.month + "").substring(1);
			final String day = new String(100 + d.day + "").substring(1);
			this.data = year + month + day;
			this.setText(formatDate(data));
		}
	}
	
	/**Format the date that is received.
	 * 
	 * @param date The string to format.
	 * @return a properly formatted date.
	 */
	private String formatDate(final String date) {
		String nDate = new String(date);
		if (date.length() > 4) {
			nDate = date.substring(0, 4) + '-' + date.substring(4);
			if (date.length() > 6) {
				nDate = nDate.substring(0, 7) + '-' + nDate.substring(7);
			}
		}
		
		return nDate;
	}
	
	/**Format the time correctly.
	 * 
	 * @param time The string to format.
	 * @return A properly formatted time string for the Period class.
	 */
	private String formatTime(final String time) {
		String nTime = new String(time);
		if (time.length() > 2) {
			nTime = time.substring(0, 2) + ':' + time.substring(2);
		}
		return nTime;
	}
	
	/**Format the entire string correctly.
	 * 
	 * @return A properly formatted string.
	 */
	public String format() {
		String s = "";
		if (type.equals("date")) {
			s = data.substring(0, 4) + "." + data.substring(4, 6) + "." + data.substring(6);
		} else if (type.equals("time")) {
			s = data.substring(0, 2) + "." + data.substring(2);
		}
		return s;
	}
	
	/**Reset the data inside the box.
	 * 
	 */
	public void reset() {
		this.data = "";
		this.setText(this.data);
	}
	
	/**Called when a key is released.
	 * 
	 * @param e The key event generated.
	 */
	public void keyReleased(final KeyEvent e) {
		if (type.equals("date")) {
			//System.out.println("in here");
			if ((e.getKeyChar() < '0' || e.getKeyChar() > '9' || data.length() == 8) && e.getKeyCode() != KeyEvent.VK_BACK_SPACE) {
				this.setText(formatDate(data));
			} else {
				if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) data = data.substring(0, data.length()-1);
				else data = data.concat(e.getKeyChar() + "");
				this.setText(formatDate(data));
			}
		} else if (type.equals("time")) {
			//System.out.println("In here!");
			if ((e.getKeyChar() < '0' || e.getKeyChar() > '9' || data.length() == 4) && e.getKeyCode() != KeyEvent.VK_BACK_SPACE) {
				//System.out.println("not a number");
				this.setText(formatTime(data));
			} else {
				if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && data.length() > 0) data = data.substring(0, data.length()-1);
				else data = data.concat(e.getKeyChar() + "");
				this.setText(formatTime(data));
			}
		}

	}
	
	/**Called when a key is pressed.
	 * 
	 * @param e The key event generated.
	 */
	public void keyPressed(final KeyEvent e) {}
	
	/** Called when a key is typed.
	 * 
	 * @param e The key event generated.
	 */
	public void keyTyped(final KeyEvent e) {}

}
