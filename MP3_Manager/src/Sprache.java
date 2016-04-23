import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

public class Sprache {

	static final String[] SPRACHEN = { "English", "Deutsch" };
	Steuerung dieSteuerung;

	public Sprache(Steuerung pSteuerung) {
		dieSteuerung = pSteuerung;
	}

	public void waehleSprache() {
		String land;
		String sprache = (String) JOptionPane.showInputDialog(null, "Select your language:", "Language Selection",
				JOptionPane.INFORMATION_MESSAGE, null, SPRACHEN, SPRACHEN[0]);
		if (sprache != null) {
			switch (sprache) {
			case "Deutsch":
				sprache = "de";
				land = "DE";
				break;
			default:
				sprache = "en";
				land = "US";
				break;
			}
			Locale langu = new Locale(sprache, land);
			ResourceBundle rBundle = ResourceBundle.getBundle("lang/language", langu);
			dieSteuerung.initOberflaeche(rBundle);
		} else {
			System.exit(0);
		}
	}
}
