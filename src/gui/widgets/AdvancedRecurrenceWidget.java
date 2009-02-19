/**
 * An advanced widget for making your own CRExL expressions.
 * 
 * @author Robert Middleton
 */

package gui.widgets;

import java.awt.FlowLayout;

import javax.swing.JTextField;

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
