package ru.verzhbitski_vladislav;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import ru.verzhbitski_vladislav.client.Client;

public class LoginController {
    @FXML
    private TextField hostField;

    @FXML
    private TextField usernameField;

    public void login() {
        int port;
        String host;

        String enteredHost = hostField.getText();
        String username = usernameField.getText();

        String[] split = enteredHost.split(":");

        if (split.length != 2) {
            showError("Invalid host");
            return;
        }

        try {
            port = Integer.parseInt(split[1]);
            host = split[0];
            if (host.equals("")) {
                throw new IllegalArgumentException();
            }
            if (port > 65535 || port < 0) {
                throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            showError("Invalid host");
            return;
        }

        if (username.equals("")) {
            showError("Invalid username");
            return;
        }

        try {
            Client.getInstance().connect(host, port, username);
        } catch (Exception e) {
            showError("Couldn't connect ot server");
            return;
        }
        try {
            Parent root1 = FXMLLoader.load(getClass().getResource("chat.fxml"));
            Main.stage.setTitle("Messenger / " + username);
            Main.stage.setScene(new Scene(root1, 500, 500));
            Main.stage.setResizable(true);
            Main.stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showError(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Error");
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
}
