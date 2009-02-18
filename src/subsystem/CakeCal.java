package subsystem;


import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;


/**
 * This is the interface class for the 'process' component for the program. It 
 * provides an interface for the world, and kind of encapsulates what a calendar
 * is.
 * 
 * @author Dana Burkart (dsb3573)
 */
public class CakeCal {
	//-- NL is a cross-platform line feed; it will work on any OS (in theory).
	public static final String NL = System.getProperty("line.separator"); 	/**The new line seperator.  System-dependent*/
	public static final String TAB = "    ";								/** preferred tab length*/
	public static final String UNTITLED = "New Calendar";					/**"New Calendar"  Default name.*/
	public boolean modified;												/**True if the calendar has been modified 
																				since it was last saved.*/
	private EventDatabase eventDatabase;
	private CalendarSettings settings;
	

	//---Public Methods------------------------------------------------------//
	/**Main constructor.  Called unless there is an argument when Cake is invoked to open a file as a command line argument.
	 * 
	 */
	public CakeCal() {
		settings = new CalendarSettings();
		eventDatabase = new EventDatabase();
		settings.setName(UNTITLED);
		settings.setDateCreated(CakeCal.getDate());
		modified = false;
	}
	
	/**
	 * Constructor for CakeCal.  Called when the title of a calendar has been provided when Cake in invoked.
	 * 
	 * @param args The arguments.  
	 */
	public CakeCal(String args[]) {
		settings = new CalendarSettings();
		eventDatabase = new EventDatabase();
		settings.setName(UNTITLED);
		settings.setDateCreated(CakeCal.getDate());
		modified = false;
		if (args.length == 1) {
			this.loadCal(args[0]);
			settings.setName(new String(args[0]));
		}
	}
	
	/**Return the settings used by this calendar.
	 * 
	 * @return The settings class used by this calendar.
	 */
	public CalendarSettings getSettings() {
		return settings;
	}
	
	/**Set the settings used by this calendar.
	 * 
	 * @param newSettings The new settings to be used for this calendar.
	 */
	public void setSettings(CalendarSettings newSettings) {
		modified = true;
		settings = newSettings;
	}
	
	/**
	 * Loads a calendar into this CakeCal.
	 * 
	 * @param filename name of the file to load
	 * @return true if successful, false otherwise
	 */
	public boolean loadCal(String filename) {
		try {
			File file = new File(filename);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(file);
			document.getDocumentElement().normalize();
			NodeList settingsNodes = document.getElementsByTagName("settings");
			
			//-- First, lets load the settings
			for (int i = 0; i < settingsNodes.getLength(); i++) {
				CalendarSettings temp = new CalendarSettings();
				Element element = (Element) settingsNodes.item(i);
				
				Element nameE = (Element) element.getElementsByTagName("name").
					item(0);
				NodeList name = nameE.getChildNodes();
				temp.setName(name.item(0).getNodeValue());
				
				Element datecreatedE = (Element) element.
					getElementsByTagName("datecreated").item(0);
				NodeList datecreated = datecreatedE.getChildNodes();
				temp.setDateCreated(SimpleDate.parse(datecreated.item(0).
						getNodeValue()));
				
				Element ownerE = (Element) element.
					getElementsByTagName("owner").item(0);
				NodeList owner = ownerE.getChildNodes();
				if (owner.getLength() > 0) temp.setOwner(owner.item(0).getNodeValue());
				else temp.setOwner( "");
				
				settings = temp;
			}
			
			NodeList eventNodes = document.getElementsByTagName("event");
			
			//-- Now we load the events.
			for (int i = 0; i < eventNodes.getLength(); i++) {
				String tempTitle;
				String tempDesc;
				String tempRecu;
				String tempLoc;
				Period tempPd;
				
				Element element = (Element) eventNodes.item(i);
				
				Element titleE = (Element) element.getElementsByTagName("title")
					.item(0);
				NodeList title = titleE.getChildNodes();
				tempTitle = title.item(0).getNodeValue();
				
				Element descE = (Element) element.getElementsByTagName("desc").
					item(0);
				NodeList desc = descE.getChildNodes();
				if (desc.getLength() > 0) tempDesc = desc.item(0).getNodeValue();
				else tempDesc = "";
				
				Element recuE = (Element) element.getElementsByTagName("recu").
				item(0);
				NodeList recu = recuE.getChildNodes();
				if (recu.getLength() > 0) tempRecu = recu.item(0).getNodeValue();
				else tempRecu = "0";
				
				Element locationE = (Element) element.getElementsByTagName("location").item(0);
				NodeList location = locationE.getChildNodes();
				if (location.getLength() > 0) tempLoc = location.item(0).getNodeValue();
				else tempLoc = "";
				
				Element periodE = (Element) element.getElementsByTagName("period")
					.item(0);
				NodeList period = periodE.getChildNodes();
				tempPd = Period.parse(period.item(0).getNodeValue());
				
				addEvent(new Event(tempPd,tempTitle,tempDesc,tempRecu,tempLoc));
			}
		} catch (FileNotFoundException e) {
			System.err.println( "File not Found" );
			return false;
		} catch (Exception e){
			e.printStackTrace();
			e.getMessage();
			return false;
		}
		return true;
	}
	
	/**
	 * Saves a calendar to a file.
	 * 
	 * @param filename name of the file to save to
	 * @return whether or not the save was successful
	 */
	public boolean saveCal(String filename) throws IOException {
		String xml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" + NL;
		xml += "<calendar>" + NL +
		settings.toXML() + eventDatabase.toXML() + "</calendar>";
		
		// append the '.xml' extension onto the string if the user has not 
		// added it manually.
		int lastIndex = filename.lastIndexOf( '.' );
		if( lastIndex == -1 ){
			filename = filename.concat(".cml");
		}
			
		BufferedWriter out = new BufferedWriter(new FileWriter(filename));
	    out.write(xml);
	    out.close();
	    modified = false;
	    return true;
	}
	
	/**
	 * Returns the day of a week a date occurs on. Does this in O(c), which helps
	 * the calendar render speedily.
	 * 
	 * @param d the date to get the weekday of
	 * @return the day of the week
	 */
	public static int getDayOfWeek(SimpleDate d) {
		return Cakeday.getDayOfWeek(d);
	}
	
	/**
	 * Returns an array of months for a given year.
	 * 
	 * @param year which year to get months for
	 * @return array of months for the year 
	 */
	public static int[] getMonths(int year) {
		return Cakeday.getMonths(year);
	}
	
	/**
	 * Returns a SimpleDate object containing the current date.
	 * 
	 * @return SimpleDate of the current date.
	 */
	public static SimpleDate getDate() {
		String df = "yyyy.MM.dd";
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(df);
		return SimpleDate.parse(sdf.format(cal.getTime()));
	}
	
	/**
	 * Returns a list of events within a certain period.
	 * 
	 * @param p the period to use
	 * @return an ArrayList containing the events within that period
	 */
	public ArrayList<Event> getEvents(Period p) {
		return eventDatabase.getEvents(p);
	}
	
	/**
	 * Adds an event to the event database. 
	 * 
	 * @param e the event to add to the database
	 * @return the event added with the UID filled out (for fast searching)
	 */
	public Event addEvent(Event e) {
		modified = true;
		return eventDatabase.addEvent(e);
	}
		
	/**
	 * Updates an event in the EventDatabase, and returns the event updated,
	 * (with has the updated UID)
	 * 
	 * @param e event to update
	 * @return updated event
	 */
	public Event updateEvent(Event e) {
		modified = true;
		Event temp = eventDatabase.updateEvent(e);
		return temp;
	}
	
	/**
	 * Deletes an equivalent event from the event database.
	 * 
	 * @param e event to delete
	 * @return whether or not it was successful
	 */
	public boolean deleteEvent(Event e) {
		modified = true;
		return eventDatabase.deleteEvent(e);
	}
} //-- CakeCal
