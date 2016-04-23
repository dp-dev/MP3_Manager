package File;

import java.io.File;

import javax.swing.JFileChooser;

public class DirBrowser {

	public File OrdnerWaehlen(File anf) {
		File file;
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(anf);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.showOpenDialog(null);
		if (chooser.getSelectedFile() != null) {
			file = chooser.getSelectedFile();
		} else {
			file = anf;
		}
		return file;
	}
}
