/**The add event dialog box.  
 * 
 * @author Robert Middleton
 *
 */

package gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import subsystem.Event;
import subsystem.Period;

public class AddEventDialog extends EventDialog implements ActionListener{

	public AddEventDialog(CakeGUI parent) {
		super(parent);
	}

	@Override
	protected JPanel buttonsPanel() {
		JPanel pane = new JPanel();
		pane.setLayout(new FlowLayout(FlowLayout.TRAILING));

		//JButton delete = new JButton("Delete Event");
		//delete.addActionListener(this);
		//pane.add(delete);

		JButton add = new JButton("Add Event");
		add.addActionListener(this);
		pane.add(add);

		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(this);
		pane.add(cancel);

		return pane;
	}

	/**Listens for an action.  If the action is the "Add Event" button being pressed, will parse input, 
	 * make a new event, and add it to the event database specified by the CakeCal passed in the constructor.
	 * 
	 * @param e The ActionEvent generated.
	 */
	public void actionPerformed( ActionEvent e ){
		if( e.getActionCommand() == "Add Event" ) {	

			try {
				if( endTime.getSelectedIndex() <= startTime.getSelectedIndex() ){
					JOptionPane.showMessageDialog(theFrame, "Your event starts before it ends!", "Bad Information", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if ( startDate.isValid() && endDate.isValid() ) {
					Period p = Period.parse(startDate.format() + ":" + militaryTimes[startTime.getSelectedIndex()] + "-" +
							endDate.format() + ":" + militaryTimes[endTime.getSelectedIndex()]);
					Event ev;
					if (currentWidget != null)
						ev = new Event(p, title.getText(), description.getText(), currentWidget.getREX());
					else
						ev = new Event(p, title.getText(), description.getText(), "");
					ev.setLocation(location.getText());
					if ( ev.isValid() ) {

						parent.addEvent(ev);
						close();	

					} else {

						JOptionPane.showMessageDialog(theFrame, "The information you entered is incorrect", "messed up data", JOptionPane.ERROR_MESSAGE);

					}
				}
			} catch ( Exception ex ) {
				JOptionPane.showMessageDialog(theFrame, "GUI unhappy.  How did you get to this state?", "GUI doesn't like you.", JOptionPane.ERROR_MESSAGE);
			}			
		}
		else if (e.getActionCommand() == "Cancel" ){
			//if the user wants to cancel, just close the window.
			close();
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
