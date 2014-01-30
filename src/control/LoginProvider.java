package control;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import model.Settings;
import view.Dashboard;

public class LoginProvider implements EventHandler<ActionEvent> {

	private final TextField usernameField;
	private final PasswordField passwordField;
	private final RadioButton savePwd;
	private final Settings settings;

	public LoginProvider(TextField usernameField, PasswordField passwordField, RadioButton savePwd) {
		this.usernameField = usernameField;
		this.passwordField = passwordField;
		this.savePwd = savePwd;
		this.settings = Settings.getInstance();
	}

	@Override
	public void handle(ActionEvent event) {
		Dashboard.setStatusText("", false);
		Dashboard.showLoader(true);
		Dashboard.setMenuTransparent(false);
		Dashboard.setSigInTransparent(true);
		final String username = usernameField.getText();
		final boolean validUsername = username.length() != 5 || !username.startsWith("u");
		if (validUsername) {
			Dashboard.setStatusText("Ungültiger Benutzername", true);
			usernameField.requestFocus();
			usernameField.selectAll();
			Dashboard.fadeInLogin();
			Dashboard.showLoader(false);
			return;
		} else {
			final String password = passwordField.getText();
			if (password.length() < 1) {
				Dashboard.setStatusText("Ungültiges Passwort", true);
				passwordField.requestFocus();
				passwordField.selectAll();
				Dashboard.fadeInLogin();
				Dashboard.showLoader(false);
				return;
			}
			if (savePwd.isSelected()) {
				settings.storeUsername(username);
				settings.storePassword(password);
			} else {
				settings.storeUsername("");
				settings.storePassword("");
			}

			new Thread(new Runnable() {
				@Override
				public void run() {
					new IliasStarter(username, password).login();
				}
			}).start();
		}
	}
}