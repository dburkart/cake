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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 * This is a widget for selecting a recurring event that is some combination of
 * week days.
 * 
 * @author Dana Burkart
 */
@SuppressWarnings("serial")
public class WeeklyRecurrenceWidget extends RecurrenceWidget implements ActionListener {
	JButton sunday, monday, tuesday, wednesday, thursday, friday, saturday;
	boolean checked[] = new boolean[7];
	Color b;
	
	/**
	 * Default constructor.
	 */
	public WeeklyRecurrenceWidget() {
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		for (boolean c : checked) {
			c = false;
		}
		
		this.add(sunday = new JButton("Su"));
		this.add(monday = new JButton("Mo"));
		this.add(tuesday = new JButton("Tu"));
		this.add(wednesday = new JButton("We"));
		this.add(thursday = new JButton("Th"));
		this.add(friday = new JButton("Fr"));
		this.add(saturday = new JButton("Sa"));

		sunday.setMaximumSize(new Dimension(24,24));
		monday.setMaximumSize(new Dimension(24,24));
		tuesday.setMaximumSize(new Dimension(24,24));
		wednesday.setMaximumSize(new Dimension(24,24));
		thursday.setMaximumSize(new Dimension(24,24));
		friday.setMaximumSize(new Dimension(24,24));
		saturday.setMaximumSize(new Dimension(24,24));
		
		sunday.setMargin(new Insets(2,2,2,2));
		monday.setMargin(new Insets(2,2,2,2));
		tuesday.setMargin(new Insets(2,2,2,2));
		wednesday.setMargin(new Insets(2,2,2,2));
		thursday.setMargin(new Insets(2,2,2,2));
		friday.setMargin(new Insets(2,2,2,2));
		saturday.setMargin(new Insets(2,2,2,2));
		
		//sunday.setFont(new Font("Arial", Font.PLAIN, 8));
		
		sunday.addActionListener(this);
		monday.addActionListener(this);
		tuesday.addActionListener(this);
		wednesday.addActionListener(this);
		thursday.addActionListener(this);
		friday.addActionListener(this);
		saturday.addActionListener(this);
		
		b = sunday.getBackground();
		
		this.setVisible(true);
	}
	
	/**
	 * Returns a string containing the recurring expression of the widget.
	 * 
	 * @return selected recurring expression
	 */
	public String getREX() {
		int i = 1;
		String s = "";
		
		for (boolean c : checked) {
			if (c) {
				s += "[" + -i + "]";
			}
			i++;
		}
		
		return s;
	}

	/**
	 * ActionListener, really only manages days being selected.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Su")) {
			checked[0] = !checked[0];
			toggle(sunday);
		} else if (e.getActionCommand().equals("Mo")) {
			checked[1] = !checked[1];
			toggle(monday);
		} else if (e.getActionCommand().equals("Tu")) {
			checked[2] = !checked[2];
			toggle(tuesday);
		} else if (e.getActionCommand().equals("We")) {
			checked[3] = !checked[3];
			toggle(wednesday);
		} else if (e.getActionCommand().equals("Th")) {
			checked[4] = !checked[4];
			toggle(thursday);
		} else if (e.getActionCommand().equals("Fr")) {
			checked[5] = !checked[5];
			toggle(friday);
		} else if (e.getActionCommand().equals("Sa")) {
			checked[6] = !checked[6];
			toggle(saturday);
		}
		
		System.out.println(this.getREX());
	}
	
	/**
	 * Toggles the passed in button... Really shitty right now, because it only
	 * changes the foreground color of the button; has to be made to toggle nicer
	 * 
	 * @param button the button to toggle
	 */
	public void toggle(JButton button) {
		if (button.getForeground() != Color.RED) {
			button.setForeground(Color.RED);
		} else {
			button.setForeground(Color.BLACK);
		}
	}
	
	public void reset() {
		sunday.setForeground(Color.BLACK);
		monday.setForeground(Color.BLACK);
		tuesday.setForeground(Color.BLACK);
		wednesday.setForeground(Color.BLACK);
		thursday.setForeground(Color.BLACK);
		friday.setForeground(Color.BLACK);
		saturday.setForeground(Color.BLACK);
		
		for (int i = 0; i < checked.length; i++) {
			checked[i] = false;
		}
	}
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			
		}
		JFrame frame = new JFrame("weekly widget");
		WeeklyRecurrenceWidget weekly = new WeeklyRecurrenceWidget();
		
		frame.setLayout(new GridLayout(1,1));
		frame.add(weekly);
		
		frame.setSize(200, 32);
		frame.setVisible(true);
		frame.pack();
	}
}
