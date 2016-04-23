import java.io.File;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;

public class CustomThreadLaenge extends Thread {
	Steuerung dieSteuerung;
	int searchWord;
	File dir;

	CustomThreadLaenge(File dir, int find, Steuerung pSteuerung) {
		dieSteuerung = pSteuerung;
		this.searchWord = find;
		this.dir = dir;
	}

	@Override
	public void run() {
		pruefeObLaengeKleiner();
		dieSteuerung.threadBeendet();
		interrupt();
	}

	private void pruefeObLaengeKleiner() {
		File[] files = dir.listFiles();
		int duration = 0;
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].getName().endsWith(".mp3")) {
					duration = längeMp3(files[i]);
					if ((duration <= searchWord)) {
						dieSteuerung.treffer.add(files[i]);
					}
				}
			}
		}
	}

	public int längeMp3(File dir) {
		int leange;
		try {
			MP3File f = (MP3File) AudioFileIO.read(dir);
			MP3AudioHeader audioHeader = f.getMP3AudioHeader();
			leange = audioHeader.getTrackLength();
		} catch (Exception e) {
			leange = 0;
			System.out.println("FEHLER! längeMp3()");
		}
		return leange;
	}
}
