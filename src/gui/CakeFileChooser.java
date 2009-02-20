/*
 * Cake Calendar
 * Copyright (C) 2009  Hashem Assayari, Dana Burkart, Vladimir Hadzhiyski, 
 * Jack Zhang
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */

package gui;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**Filters the files to search for.  This is used when the user uses an 
 * OpenDialog or a SaveDialog.  This will filter by folders, and display .cml 
 * files in the file browser.
 * 
 * @author Robert Middleton
 *
 */
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
