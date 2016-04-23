
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

@SuppressWarnings("serial")
public class Oberflaeche extends JFrame implements ActionListener, ItemListener {

	Oberflaeche dieOberflaeche;
	Steuerung dieSteuerung;
	inits i = new inits();

	static JButton suchen, ordner, loeschen, alleLoeschen;
	static JTextField srcFolder, searchDetails;
	static JLabel vorgang, elemente;
	static JRadioButton rbleange, rbname;
	ButtonGroup group = new ButtonGroup();

	static List<File> ListeDateien = new ArrayList<File>();

	final static DefaultTableModel model = new DefaultTableModel();
	static JTable myTable = new JTable(model);
	JScrollPane scrollPane = new JScrollPane(myTable);

	File startFolder;
	String dir = null;

	static Boolean clearTable = false;

	public Oberflaeche(Steuerung pSteuerung) {
		dieSteuerung = pSteuerung;

		try {
			Image image = ImageIO.read(this.getClass().getResource("/img/logo.png"));
			setIconImage(image);
		} catch (Exception e) {
		}

		setTitle("MP3 Manager");
		setSize(500, 500);
		setLayout(null);

		// Path to folder + folderButton
		startFolder = new File(dieSteuerung.properties.getProperty("path", "C:/"));
		srcFolder = new JTextField("" + dieSteuerung.properties.getProperty("path", "C:/"));
		srcFolder.setBounds(10, 10, 380, 22);
		this.add(srcFolder);
		srcFolder.addActionListener(this);
		ordner = new JButton(dieSteuerung.getTextInSprache("ordner"));
		ordner.setBounds(390, 10, 88, 21);
		this.add(ordner);
		ordner.addActionListener(this);

		// Radio buttons
		rbname = new JRadioButton(dieSteuerung.getTextInSprache("sucheName"), true);
		rbname.setBounds(10, 40, 200, 20);
		this.add(rbname);
		rbname.addItemListener(this);
		rbleange = new JRadioButton(dieSteuerung.getTextInSprache("sucheLaenge"), false);
		rbleange.setBounds(220, 40, 200, 20);
		rbleange.addItemListener(this);
		this.add(rbleange);
		group.add(rbleange);
		group.add(rbname);

		// Details what to look for
		searchDetails = new JTextField("" + dieSteuerung.properties.getProperty("filterName", ""));
		searchDetails.setBounds(10, 65, 470, 22);
		this.add(searchDetails);

		// status for user
		vorgang = new JLabel(dieSteuerung.getTextInSprache("keineSuche"), SwingConstants.CENTER);
		vorgang.setBounds(10, 88, 380, 25);
		this.add(vorgang);

		suchen = new JButton(dieSteuerung.getTextInSprache("suchen"));
		suchen.setBounds(390, 90, 88, 21);
		this.add(suchen);
		suchen.addActionListener(this);

		// Insert column names
		String[] name = new String[3];
		name[0] = "";
		name[1] = dieSteuerung.getTextInSprache("tabelleUeberschriftDatei");
		name[2] = dieSteuerung.getTextInSprache("tabelleUeberschriftleange");

		for (int i = 0; i < name.length; i++) {
			model.addColumn(name[i]);
		}
		// Set size for columns
		myTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableColumn col = myTable.getColumnModel().getColumn(0);
		TableColumn col1 = myTable.getColumnModel().getColumn(1);
		TableColumn col2 = myTable.getColumnModel().getColumn(2);
		col.setPreferredWidth(20);
		col1.setPreferredWidth(352);
		col2.setPreferredWidth(80);
		scrollPane.setBounds(10, 120, 470, 320);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.add(scrollPane);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		loeschen = new JButton(dieSteuerung.getTextInSprache("loeschen"));
		loeschen.setBounds(10, 445, 150, 21);
		this.add(loeschen);
		loeschen.addActionListener(this);

		alleLoeschen = new JButton(dieSteuerung.getTextInSprache("loeschenAlle"));
		alleLoeschen.setBounds(165, 445, 150, 21);
		this.add(alleLoeschen);
		alleLoeschen.addActionListener(this);

		elemente = new JLabel(dieSteuerung.getTextInSprache("keineDateien"), SwingConstants.RIGHT);
		elemente.setBounds(320, 443, 160, 25);
		this.add(elemente);

		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == rbname) {
			if (rbname.isSelected()) {
				searchDetails.setText("" + dieSteuerung.properties.getProperty("filterName"));
			} else {
				searchDetails.setText("" + dieSteuerung.properties.getProperty("filterLeange"));
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ordner) {
			// File browser
			startFolder = dieSteuerung.OrdnerWaehlen(startFolder);
			if (startFolder != null) {
				dir = startFolder.getPath();
			}
			srcFolder.setText(dir);
			// System.out.println("startfolder: " + startFolder);
		}

		if (e.getSource() == suchen) {
			String dir = srcFolder.getText();
			File fs = new File(dir);
			if (rbleange.isSelected()) {
				int text = Integer.parseInt(searchDetails.getText());
				dieSteuerung.sucheNachLeange(fs, text, myTable, model);
			} else { // rbname is selected
				String[] text = searchDetails.getText().split("; ");
				dieSteuerung.sucheNachNamen(fs, text, myTable, model);
			}
			cellBool();
		}

		if (e.getSource() == loeschen) {
			// einzlne gewählte löschen
			int testo = 0;
			for (int i = 0; i < myTable.getRowCount(); i++) {
				boolean isChecked = (Boolean) myTable.getValueAt(i, 0);
				if (isChecked) {
					File datei = ListeDateien.get(i - testo);
					datei.delete();
					ListeDateien.remove(i - testo);
					testo++;
				}
			}
			i.initialisieren(myTable, model);
		}
		if (e.getSource() == alleLoeschen) {
			// Delete all elements
			int testo = 0;
			for (int i = 0; i < myTable.getRowCount(); i++) {
				File datei = ListeDateien.get(i - testo);
				datei.delete();
				ListeDateien.remove(i - testo);
				testo++;
			}
			i.initialisieren(myTable, model);
		}
	}

	private void cellBool() {
		// First column with the checkbox
		myTable.getColumn("").setCellRenderer(myTable.getDefaultRenderer(Boolean.class));
		myTable.getColumn("").setCellEditor(myTable.getDefaultEditor(Boolean.class));
	}
}
