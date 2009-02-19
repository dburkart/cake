/**
 * Stores all the settings a calendar can have. Is stored at the head of every saved calendar.
 * 
 * @author Dana Burkart
 */

package subsystem;

public class CalendarSettings {
	private String name;
	private SimpleDate dateCreated;
	private String owner;
	private String filename;
	
	/**Default constructor for the CalendarSettings.
	 * 
	 */
	public CalendarSettings() {
		name = CakeCal.UNTITLED;
		dateCreated = CakeCal.getDate();
		owner = System.getProperty("user.name");
		filename = "";
	}
	
	/**Returns the settings in properly formatted XML to save.
	 * 
	 * @return An XML string representation of the data.
	 */
	public String toXML() {
		String xml = "<settings>" + CakeCal.NL + CakeCal.TAB +
		"<name>" + name + "</name>" + CakeCal.NL + CakeCal.TAB +
		"<datecreated>" + dateCreated.format() + "</datecreated>" + CakeCal.NL + CakeCal.TAB +
		"<owner>" + owner + "</owner>" + CakeCal.NL +
		"</settings>" + CakeCal.NL;
		return xml;
	}

	/**Get the name of this calendar,
	 * 
	 * @return the name of this calendar
	 */
	public String getName() {
		return name;
	}

	/**Set the name of the calendar.
	 * 
	 * @param name The new name of this calendar
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**Get what date this calendar was created.
	 * 
	 * @return The date this calendar was created.
	 */
	public SimpleDate getDateCreated() {
		return dateCreated;
	}

	/**Set the date that this calendar was created.
	 * 
	 * @param dateCreated The date that this calendar was created.
	 */
	public void setDateCreated(SimpleDate dateCreated) {
		this.dateCreated = dateCreated;
	}

	/**Return the owner of this calendar.
	 * 
	 * @return The owner of this calendar.
	 */
	public String getOwner() {
		return owner;
	}

	/**Set the owner of this calendar.
	 * 
	 * @param owner The new owner of this calendar
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**Get the filename of this calendar.
	 * 
	 * @return The filename of this calendar.
	 */
	public String getFilename() {
		return filename;
	}

	/**Set the filename of this calendar.
	 * 
	 * @param filename The new file name of this calendar.
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}
}