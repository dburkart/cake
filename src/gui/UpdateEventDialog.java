package gui;


import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import subsystem.Event;
import subsystem.Period;

/**Update event dialog.
 * 
 * @author rxm6930 (Robert Middleton)
 *
 */
public class UpdateEventDialog extends EventDialog implements ActionListener{
	

	/**Constructor.  Just calls the super constructor.
	 * 
	 * @param parent The CakeGUI to use as this dialog's parent.
	 */
	public UpdateEventDialog(CakeGUI parent) {
		super(parent);
		this.endDate.setEnabled(false);
	}

	@Override
	protected JPanel buttonsPanel() {
		JPanel pane = new JPanel();
		pane.setLayout(new FlowLayout(FlowLayout.TRAILING));
		
		JButton delete = new JButton("Delete Event");
		delete.addActionListener(this);
		pane.add(delete);

		JButton save = new JButton("Update Event");
		save.addActionListener(this);
		pane.add(save);

		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(this);
		pane.add(cancel);

		return pane;
	}

	/**Updates/Deletes/Cancel as appropriate.
	 * 
	 */
	public void actionPerformed(ActionEvent e) {
		if( e.getActionCommand() == "Update Event" ) {	
			
			try {
				if( endTime.getSelectedIndex() <= startTime.getSelectedIndex() ){
					JOptionPane.showMessageDialog(theFrame, "Your event starts before it ends!", "Bad Information", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if ( startDate.isValid() && endDate.isValid() ) {
					Period p = Period.parse(startDate.format() + ":" + militaryTimes[startTime.getSelectedIndex()] + "-" +
							endDate.format() + ":" + militaryTimes[endTime.getSelectedIndex()]);
					Event ev = new Event(globalE);
					ev.setTitle(title.getText());
					ev.setDescription(description.getText());
					ev.setLocation(location.getText() );
					ev.setPeriod(p);
					ev.parseRecurring(currentWidget.getREX());
					System.out.println(ev.getRecurringType());
					if ( ev.isValid() ) {
						//System.out.println( "in action:\n" + ev.toString());
						parent.updateEvent(ev);
						parent.updateDays();
						parent.mview.updateDays();
						close();	

					} else {

						JOptionPane.showMessageDialog(theFrame, "The information you entered is incorrect", "messed up data", JOptionPane.ERROR_MESSAGE);

					}
				}
			} catch ( Exception ex ) {
				JOptionPane.showMessageDialog(theFrame, "GUI doesn't like you.", "GUI unhappy.  How did you get to this state?", JOptionPane.ERROR_MESSAGE);
				ex.printStackTrace();
			}	
			parent.switchTopRightCard("Add Event");
		}
		else if (e.getActionCommand() == "Cancel" ){
			//if the user wants to cancel, just close the window.
			close();
		}
		else if( e.getActionCommand() == "Delete Event"){
			parent.deleteEvent(globalE);
			parent.updateDays();
			parent.switchTopRightCard("Add Event");
			close();
		}
		
		else if (e.getActionCommand() == "recurring") {
			recurringType.setVisible(!recurringType.isVisible());
		}
		
		else if (e.getActionCommand() == "recurring") {
			recurringType.setVisible(!recurringType.isVisible());
			if (recurringType.getSelectedItem().equals("Week Days") && recurringType.isVisible()) {
				if (recurringType.getSelectedItem().equals("Week Days")) {
					weeklyWidget.setVisible(true);
					currentWidget = weeklyWidget;
				} else if (recurringType.getSelectedItem().equals("Nth Day of Month")) {
					nthDayOfMonthWidget.setVisible(true);
					currentWidget = nthDayOfMonthWidget;
				} else if (recurringType.getSelectedItem().equals("N Days")) {
					nthDayWidget.setVisible(true);
					currentWidget = nthDayWidget;
				} else if (recurringType.getSelectedItem().equals("Custom")) {
					advancedWidget.setVisible(true);
					currentWidget = advancedWidget;
				}
			} else {
				weeklyWidget.setVisible(false);
				nthDayOfMonthWidget.setVisible(false);
				nthDayWidget.setVisible(false);
				advancedWidget.setVisible(false);
				currentWidget = null;
			}
		}

		else if ( e.getActionCommand() == "recurringTypeChanged" ) {
			try {
				weeklyWidget.setVisible(false);
				nthDayOfMonthWidget.setVisible(false);
				nthDayWidget.setVisible(false);
				advancedWidget.setVisible(false);
				currentWidget = null;
				if (recurringType.getSelectedItem().equals("Week Days")) {
					weeklyWidget.setVisible(true);
					currentWidget = weeklyWidget;
				}else if( recurringType.getSelectedItem().equals("Nth Day of Month")){
					nthDayOfMonthWidget.setVisible(true);
					currentWidget = nthDayOfMonthWidget;
				}else if( recurringType.getSelectedItem().equals("N Days")){
					nthDayWidget.setVisible(true);
					currentWidget = nthDayWidget;
				}else if( recurringType.getSelectedItem().equals("Custom")){
					advancedWidget.setVisible(true);
					currentWidget = advancedWidget;
				}
			} catch (Exception n) {

			}
		}
		
		if (!recurring.isSelected()) 
			endDate.setEnabled(false);
		else
			endDate.setEnabled(true);
		
	}
	
	

}
