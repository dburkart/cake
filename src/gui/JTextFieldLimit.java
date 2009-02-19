/**
 * This is a class which helps limit the number of input characters 
 * in a JTextField!
 * 
 * @author Vladimir Hadzhiyski
 */

package gui;

import javax.swing.text.*;

/**
 * @author vrh8879 (Vladimir Hadzhiyski)
 * 
 * Properly formats text.
 *
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
