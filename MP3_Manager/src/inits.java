import java.io.File;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;

public class inits {

	public void initialisieren(JTable myTable, DefaultTableModel model) {
		Object[][] row = new Object[1][1];

		// delete rows from past search
		if (Oberflaeche.clearTable == true) {
			for (int i = myTable.getRowCount() - 1; i >= 0; i--) {
				model.removeRow(i);
			}
			Oberflaeche.clearTable = false;
		}

		// add all new rows to table
		if (Oberflaeche.ListeDateien.size() != 0) {
			for (int i = 0; i < Oberflaeche.ListeDateien.size(); i++) {
				model.addRow(row);
				model.setValueAt(false, i, 0);
				model.setValueAt(Oberflaeche.ListeDateien.get(i).getName(), i, 1);
				model.setValueAt(längeMp3(Oberflaeche.ListeDateien.get(i)), i, 2);
			}
			Oberflaeche.clearTable = true;
		}
	}

	private String längeMp3(File dir) {
		String duration = "0";
		try {
			MP3File f = (MP3File) AudioFileIO.read(dir);
			MP3AudioHeader audioHeader = f.getMP3AudioHeader();
			duration = audioHeader.getTrackLength() + "s";
		} catch (Exception e) {
			// System.out.println("FEHLER: längeMp3()"); e.printStackTrace();
			duration = "n/a";
		}
		return duration;
	}
}
