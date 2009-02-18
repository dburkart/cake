package gui;
/**An exception that is thrown if the input is not legal.
 * 
 * @author dsb3573 (Dana Burkart)
 *
 */
public class IllegalInputException extends Exception {
	private static final long serialVersionUID = -6502474940244940604L;
	private String message;
	
	/**Constructor for a new exception.  Takes a string as a parameter for the message.
	 * 
	 * @param s The string to construct for the message
	 */
	public IllegalInputException(String s) {
		message = s;
	}
	
	/**Get the message of this exception
	 * 
	 * @return The message of this exception.
	 */
	public String getMessage() {
		return message;
	}
}
