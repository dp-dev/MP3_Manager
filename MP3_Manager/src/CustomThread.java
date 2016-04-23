import java.io.File;

public class CustomThread extends Thread {
	Steuerung dieSteuerung;
	inits init = new inits();

	String[] searchWord = null;
	File dir;

	CustomThread(File dir, String[] find, Steuerung pSteuerung) {
		dieSteuerung = pSteuerung;
		this.searchWord = find;
		this.dir = dir;
	}

	@Override
	public void run() {
		pruefeObNamenGleich();
		dieSteuerung.threadBeendet();
		interrupt();
	}

	private void pruefeObNamenGleich() {
		File[] files = dir.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].getName().endsWith(".mp3")) {
					for (int k = 0; k < searchWord.length; k++) {
						if (files[i].getName().toLowerCase().contains(searchWord[k].toLowerCase())) {
							dieSteuerung.treffer.add(files[i]);
						}
					}
				}
			}
		}
	}
}