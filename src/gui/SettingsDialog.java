package gui;


import gui.widgets.FormatBox;

import java.awt.ComponentOrientation;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import subsystem.CalendarSettings;

/**Handles setting the settings for the user.
 * 
 * @author rxm6930 (Robert Middleton)
 *
 */
public class SettingsDialog implements ActionListener{

	private CalendarSettings settings;
	private CakeGUI parent;
	private JFrame mainFrame;
	private JPanel mainPanel;
	private JButton cancel;
	private JButton ok;
	private JTextField name;
	private JTextField owner;
	private FormatBox dateCreated;
	private JTextField fileName;
	private boolean shown;
	private JDialog dialog;

	/**Constructor for the settings
	 * 
	 * @param parent The CakeGUI parent
	 */
	public SettingsDialog( CakeGUI parent ){
		settings = parent.getSettings();
		this.parent = parent;
		mainFrame = new JFrame();
		shown = false;
	}

	/**Add the components to the main panel
	 * 
	 * @return A new JPanel with all of the elements filled out.
	 */
	private JPanel addComponentsToPane(){
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		mainPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(0, 0, 10, 10);


		//fill in the owner information
		JLabel ownerL = new JLabel("Owner: ");
		c.gridx = 0;
		c.gridy = 0;
		mainPanel.add( ownerL, c );

		owner = new JTextField();
		owner.setText(settings.getOwner());
		owner.setColumns(12);
		c.gridx = 1;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add( owner, c);


		//fill in the name information
		JLabel nameL = new JLabel("Name: ");
		c.gridx = 0;
		c.gridy = 1;
		mainPanel.add(nameL, c);

		name = new JTextField();
		name.setText(settings.getName());
		c.gridx = 1;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(name, c);

		//fill in the file name information
		JLabel fileL = new JLabel("Filename: ");
		c.gridx = 0;
		c.gridy = 2;
		mainPanel.add(fileL, c);

		fileName = new JTextField();
		fileName.setText(settings.getFilename());
		fileName.setColumns(12);
		fileName.setEditable(false);
		c.gridx = 1;
		c.gridy = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(fileName, c);


		//fill in the date created information
		JLabel dateL = new JLabel("Date Created: ");
		c.gridx = 0;
		c.gridy = 3;
		mainPanel.add( dateL, c );

		dateCreated = new FormatBox();
		dateCreated.setData( settings.getDateCreated().format() );
		dateCreated.setEditable(false);
		c.gridx = 1;
		c.gridy = 3;
		c.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add( dateCreated, c );

		c.fill = GridBagConstraints.HORIZONTAL;
		//add the 'cancel' and 'ok' buttons
		ok = new JButton("OK");
		ok.addActionListener(this);
		c.gridx = 0;
		c.gridy = 4;
		mainPanel.add( ok, c );

		cancel = new JButton("Cancel");
		cancel.addActionListener(this);
		c.gridx = 1;
		c.gridy = 4;
		mainPanel.add( cancel, c );

		return mainPanel;
	}

	/**Listens for an action that is performed and does the appropriate action.
	 * 
	 * @param e The action event to do something with.
	 */
	public void actionPerformed(ActionEvent e) {
		if( e.getActionCommand().equals("OK")){
			CalendarSettings settings2 = new CalendarSettings();
			settings2.setDateCreated(settings.getDateCreated());
			settings2.setFilename(fileName.getText());
			settings2.setName(name.getText());
			settings2.setOwner(owner.getText());
			parent.setSettings(settings2);
		}
		parent.finished();
		close();

	}

	/**Displays the dialog box.  
	 * 
	 */
	public void showDialogBox() {

		//if the box has already been shown, don't recreate it.
		if( shown ){
			settings = parent.getSettings();
			name.setText( settings.getName());
			owner.setText(settings.getOwner());
			dateCreated.setData(settings.getDateCreated().format());
			fileName.setText(settings.getFilename());
			dialog.setVisible( true );
			return;
		}

		dialog = new JDialog(mainFrame,"Calendar Settings");
		JPanel fillHerUp = new JPanel( new GridLayout(1,1) );
		fillHerUp.add(addComponentsToPane());
		dialog.add(fillHerUp);
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		dialog.pack();
		dialog.setVisible(true);
		dialog.setResizable(true);
		dialog.setSize(460,380);
		dialog.setLocationRelativeTo(null);
		dialog.requestFocus();
		dialog.setFocusableWindowState(true);

		shown = true;

	}

	/**Makes it invisible!  You must call this, or else the dialog box will not dissapear.
	 * Note: this simply makes the dialog invisible, it does not actually end the process as that
	 * causes the CakeGUI to close as well.
	 */
	public void close(){
		dialog.setVisible(false);
	}

}
