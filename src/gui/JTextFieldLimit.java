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

import javax.swing.text.*;

/**
 * This is a class which helps limit the number of input characters 
 * in a JTextField!
 * 
 * @author Vladimir Hadzhiyski
 */
public class JTextFieldLimit extends PlainDocument {
    private int limit;
    private boolean toUppercase = false;
    
    /**
     * constructor
     * 
     * @param limit The number of characters the String is suppose
     * 				to contain.
     */
    JTextFieldLimit(int limit) {
        super();
        this.limit = limit;
    }
    
    /**
     * copy constructor.
     * 
     * @param limit The number of characters the String is suppose 
     * 				to contain.
     * @param upper	Tells whether the string is Uppercase or Lowercase.
     */
    JTextFieldLimit(int limit, boolean upper) {
        super();
        this.limit = limit;
        toUppercase = upper;
    }
    
    /**
     * inserts the string to an already made string.
     * 
     * @param offset How many places on the parent String after index 0
     * @param str The string to be inserted.
     * @param attr The attributes of the String that will be applied.
     */
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (str == null) return;
        
        if ((getLength() + str.length()) <= limit) {
            if (toUppercase) str = str.toUpperCase();
            super.insertString(offset, str, attr);
        }
    }
    
}
