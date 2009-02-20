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

import javax.swing.JTextField;

/**
 * An advanced widget for making your own CRExL expressions.
 * 
 * @author Robert Middleton
 */
public class AdvancedRecurrenceWidget extends RecurrenceWidget {

	private JTextField textBox;
	
	public AdvancedRecurrenceWidget(){
		textBox = new JTextField(20);
		this.setLayout(new FlowLayout());
		this.add(textBox);
	}
	
	@Override
	public String getREX() {
		return textBox.getText();
	}

	@Override
	public void reset() {
		textBox.setText("");

	}
	
	public void setREx(String rex) {
		textBox.setText(rex);
	}

}
