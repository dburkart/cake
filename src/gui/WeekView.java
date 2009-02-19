/**View a week in the calendar.  Consists of 7 day views put together.
 * 
 * @author Robert Middleton
 * @author Vladimir Hadzhiyski
 * @author Dana Burkart
 * 			
 */

package gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import subsystem.CakeCal;
import subsystem.Cakeday;
import subsystem.Period;
import subsystem.SimpleDate;

public class WeekView implements ActionListener {

	private CakeGUI parent;
	private JPanel panel;
	private JPanel bigPanel;
	private Period startDay;
	private ArrayList<DayCanvas> days;
	private JLabel dateText;
	
	/**Constructor
	 * 
	 * @param parent the CakeGUI which is this week view's parent.
	 */
	public WeekView(CakeGUI parent){
		panel = new JPanel( new BorderLayout());
		SimpleDate d = new SimpleDate(parent.currentMonth, parent.currentDay, parent.currentYear);
		startDay = Period.parse(d.year + "." + d.month + "." +
				d.day + ":00.00-" + d.year + "." +
				d.month + "." + d.day + ":24.00");
		while(CakeCal.getDayOfWeek(startDay.start.date) != 0){	
			startDay.start.date.day--;
			startDay.end.date.day--;
		}

		this.dateText = new JLabel("" + parent.curMonths[parent.currentMonth-1] + " " +
				parent.currentDay + ", " + parent.currentYear);

		days = new ArrayList<DayCanvas>();

		//the week panel which goes in the center
		JPanel weekPanel = new JPanel(new GridLayout(1,7));
		for( int x = 0; x < 7; x++){
			days.add(new DayCanvas(x == 0, parent,startDay));
			weekPanel.add(days.get(x));
			startDay.start.date.day++;
			startDay.end.date.day++;
		}

		//the panel which has the name of the days 
		JPanel dayPanel = new JPanel( new GridLayout(1,7));

		JLabel label = new JLabel();

		for (int i=0; i<7; i++) {
			label = new JLabel(Cakeday.DAYS[i]);
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setFont(new Font("Verdana", Font.PLAIN, 12));
			dayPanel.add(label);
		}

		panel.add(weekPanel,BorderLayout.CENTER);
		panel.add(dayPanel, BorderLayout.NORTH);

		bigPanel = new JPanel(new BorderLayout());

		JPanel top = new JPanel();
		JButton prevWeek = new JButton("<");
		JButton nextWeek = new JButton(">");
		prevWeek.addActionListener(this);
		nextWeek.addActionListener(this);
		top.add(prevWeek);
		top.add(dateText);
		top.add(nextWeek);

		bigPanel.add(panel, BorderLayout.CENTER);
		bigPanel.add(top, BorderLayout.NORTH);

		this.parent = parent;
	}

	/**Get the main panel
	 * 
	 * @return The main panel of the week view
	 */
	JPanel getPanel() {
		return bigPanel;
	}

	/**Does something depending on the action.
	 * 
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("<")) {
			if (parent.currentDay <= 7 ) {
				if (parent.currentMonth > 1) {				
					parent.currentDay = parent.curMonths[parent.currentMonth - 2] - (7 - parent.currentDay);		
					parent.updateMonth(parent.currentMonth - 1);
				} else {
					parent.currentDay = parent.curMonths[11] - (7 - parent.currentDay);
					parent.updateMonth(12);
					
					if (parent.currentDay > 7) {
						parent.updateYear(parent.currentYear - 1);
					}
				}
			} else {
				parent.currentDay -= 7;
			}
		} else if (e.getActionCommand().equals(">")) {
			if (parent.currentDay <= parent.curMonths[parent.currentMonth - 1]) {
				parent.currentDay += 7;
				
				if (parent.currentDay > parent.curMonths[parent.currentMonth - 1]) {
					parent.currentDay = (parent.currentDay - parent.curMonths[parent.currentMonth - 1]);
					
					if (parent.currentMonth <= 11) {
						parent.updateMonth(parent.currentMonth + 1);
					} else {
						parent.updateMonth(1);
						parent.updateYear(parent.currentYear + 1);
					}
				}
				
			}
		}
		
		parent.mview.updateSelectedDay(parent.currentDay);

		parent.updateMonth(parent.currentMonth);
		parent.updateYear(parent.currentYear);
		parent.highlightToday(parent.currentDay);
	}
	

	/**Update the week based on the current period.
	 * 
	 */
	public void updateWeek(){
		SimpleDate d = new SimpleDate(parent.currentMonth, parent.currentDay, parent.currentYear);
		startDay = Period.parse(d.year + "." + d.month + "." +
				d.day + ":00.00-" + d.year + "." +
				d.month + "." + d.day + ":24.00");
		while(CakeCal.getDayOfWeek(startDay.start.date) != 0){
			startDay.start.date.day = --startDay.end.date.day;
		}

		for( int x = 0; x < days.size(); x++){
			DayCanvas day = days.get(x);
			day.setDay(new Period(startDay));
			day.render();
			startDay.start.date.day++;
			startDay.end.date.day++;
		}

		String weekDate = "";
		
		SimpleDate myDate = new SimpleDate(parent.currentMonth, parent.currentDay, parent.currentYear);
		
		int dayOfWeek = CakeCal.getDayOfWeek(myDate);
		int startDateOfWeek = parent.currentDay - dayOfWeek;
		int endDateOfWeek = startDateOfWeek + 6;
		
		if (startDateOfWeek <= 0) {	
			endDateOfWeek = (7 - parent.currentMonthOffset);
			
			if (parent.currentMonth > 1) { 
				startDateOfWeek = parent.curMonths[parent.currentMonth - 2] - (parent.currentMonthOffset - 1);
				
				weekDate = Cakeday.MONTHS[parent.currentMonth - 2] + " " + startDateOfWeek +
						   " - " + Cakeday.MONTHS[parent.currentMonth - 1] + " " + endDateOfWeek +
						   ", " + parent.currentYear;
			} else { 
				startDateOfWeek = parent.curMonths[11] - (parent.currentMonthOffset - 1);
				
				weekDate = Cakeday.MONTHS[11] + " " + startDateOfWeek + ", " + (parent.currentYear - 1) +
						   " - " + Cakeday.MONTHS[parent.currentMonth - 1] + " " + endDateOfWeek +
						   ", " + parent.currentYear;
			}
		} else if (endDateOfWeek > parent.curMonths[parent.currentMonth-1]) {
			endDateOfWeek = endDateOfWeek - parent.curMonths[parent.currentMonth - 1];
			
			if (parent.currentMonth == 12) {
				weekDate = Cakeday.MONTHS[parent.currentMonth - 1] + " " + startDateOfWeek + ", " + parent.currentYear +
				   " - " + Cakeday.MONTHS[parent.currentMonth % 12] + " " + endDateOfWeek +
				   ", " + (parent.currentYear + 1);
			}
			
			if (parent.currentMonth == 12) {
				if (parent.currentDay >= 7) { 
					weekDate = Cakeday.MONTHS[parent.currentMonth - 1] + " " + startDateOfWeek + ", " + parent.currentYear +
					   " - " + Cakeday.MONTHS[parent.currentMonth % 12] + " " + endDateOfWeek +
					   ", " + (parent.currentYear + 1);
				} 
			} else { 
				weekDate = Cakeday.MONTHS[parent.currentMonth - 1] + " " + startDateOfWeek +
				   " - " + Cakeday.MONTHS[parent.currentMonth % 12] + " " + endDateOfWeek +
				   ", " + (parent.currentYear);
			}
		} else {
			weekDate = Cakeday.MONTHS[parent.currentMonth - 1] + " " + startDateOfWeek +
			" - " + endDateOfWeek + ", " + parent.currentYear;
		}
		
		this.dateText.setText(weekDate);
	}

	/**Causes the day canvas that is associated with this week view to scroll, and all the other ones to scroll 
	 * at the same time.
	 * 
	 * @param e The MouseWheelEvent to parse
	 * @param dayOfSender The day which sent this command
	 * @param right ??
	 */
	public void Scroll(MouseWheelEvent e, int dayOfSender) {
		for (int i = 0; i < 7; i++) {
			if (days.get(i).getDay().start.date.day != dayOfSender) days.get(i).zoomScroll(e);
		}
	}

}
