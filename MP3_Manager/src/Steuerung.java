
import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import File.DirBrowser;

public class Steuerung {
	Oberflaeche dieOberflaeche;
	DirBrowser derDB;
	inits initTable;
	Boolean threadRunning;
	int anzahlThreads;
	List<File> treffer = new ArrayList<File>();
	Properties properties = new Properties();
	ResourceBundle rBundle;

	public Steuerung() {
		// Get current config File for user properties
		try {
			BufferedInputStream stream = new BufferedInputStream(new FileInputStream("manager.properties"));
			properties.load(stream);
			stream.close();
		} catch (IOException e) {
			// e.printStackTrace();
		}
		Sprache dieSprache = new Sprache(this);
		dieSprache.waehleSprache();
	}

	public String getTextInSprache(String key) {
		return rBundle.getString(key);
	}

	public void initOberflaeche(ResourceBundle pBundle) {
		rBundle = pBundle;
		dieOberflaeche = new Oberflaeche(this);
		derDB = new DirBrowser();
		initTable = new inits();
		threadRunning = false;
		anzahlThreads = 0;
	}

	public static void main(String[] args) {
		new Steuerung();
	}

	public File OrdnerWaehlen(File anf) {
		return derDB.OrdnerWaehlen(anf);
	}

	public void sucheNachNamen(File dir, String[] find, JTable myTable, DefaultTableModel model) {
		grafischSucheStart();
		File[] files = dir.listFiles();

		// Thread for main folder
		anzahlThreads++;
		CustomThread sucheName = new CustomThread(dir, find, this);
		sucheName.start();
		// System.out.println("THREAD GESTARTET: " + anzahlThreads);

		// Threads for subfolders
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				anzahlThreads++;
				CustomThread sucheNameUnterordner = new CustomThread(files[i], find, this);
				sucheNameUnterordner.start();
				// System.out.println("THREAD GESTARTET: " + anzahlThreads);
			}
		}
	}

	public void sucheNachLeange(File dir, int find, JTable myTable, DefaultTableModel model) {
		grafischSucheStart();
		File[] files = dir.listFiles();

		// Thread for main folder
		anzahlThreads++;
		CustomThreadLaenge sucheLaenge = new CustomThreadLaenge(dir, find, this);
		sucheLaenge.start();
		// System.out.println("THREAD GESTARTET: " + anzahlThreads);

		// Threads for subfolders
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				anzahlThreads++;
				CustomThreadLaenge sucheLeangeUnterordner = new CustomThreadLaenge(files[i], find, this);
				sucheLeangeUnterordner.start();
				// System.out.println("THREAD GESTARTET: " + anzahlThreads);
			}
		}
	}

	private void grafischSucheStart() {
		Oberflaeche.vorgang.setText(getTextInSprache("laeuftSuche"));
		Oberflaeche.vorgang.setForeground(Color.RED);
		threadRunning = true;
		Oberflaeche.ListeDateien.clear();
		treffer.clear();
	}

	public void threadBeendet() {
		anzahlThreads--;
		if (anzahlThreads < 1) {
			anzahlThreads = 0;
			threadRunning = false;
			if (treffer.size() >= Integer.parseInt(properties.getProperty("maxElements", "1500"))) {
				JOptionPane.showMessageDialog(null,
						getTextInSprache("ueber1500Anf")
								+ Integer.parseInt(properties.getProperty("maxElements", "1500")) + " "
								+ getTextInSprache("ueber1500End"),
						getTextInSprache("warnung"), JOptionPane.WARNING_MESSAGE);
			} else {
				Oberflaeche.ListeDateien = treffer;
				initTable.initialisieren(Oberflaeche.myTable, Oberflaeche.model);
				Oberflaeche.elemente.setText("" + treffer.size() + " " + getTextInSprache("anzahlDateien"));
			}
			Oberflaeche.vorgang.setText(getTextInSprache("endeSuche"));
			Oberflaeche.vorgang.setForeground(Color.BLACK);
		}
		// System.out.println("THREAD BEENDET: " + anzahlThreads);
	}

}
