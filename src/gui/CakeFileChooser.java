/**Filters the files to search for.  This is used when the user uses an OpenDialog or a SaveDialog.  This will filter by folders, and 
 * display .cml files in the file browser.
 * 
 * @author Robert Middleton
 *
 */

package gui;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class CakeFileChooser extends FileFilter {

	/**Default constructor
	 * 
	 */
	public CakeFileChooser() {
	}

	/**Accept method.  Takes in a file, and returns weather or not this file is accepted.
	 * 
	 * @param arg0 The file to check the extension of
	 * 
	 * @return true if the file is accepted, false otherwise
	 */
	public boolean accept(File arg0) {
		if( arg0.isDirectory()){
			return true;
		}
		//if the extension ends in '.xml', return true
		//get the substring of the extension of the file
		int indexOf = arg0.getName().indexOf('.');
		if( indexOf == -1 ){
			return false;
		}
		
		String extension = arg0.getName().substring(indexOf, arg0.getName().length());
		if (extension.equals(".cml")){
			return true;
		}
		
		else{
			return false;
		}
	}

	/** Main method, tests the program.
	 * @param args
	 */
	public static void main(String[] args) {
		CakeFileChooser c = new CakeFileChooser();
		System.out.println(c.accept( new File("jklts.xml")));
		System.out.println(c.accept( new File("jkl.doc")));

	}

	@Override
	public String getDescription() {
		String str = "Cake Markup Language Files(.cml)";
		return str;
	}

}
