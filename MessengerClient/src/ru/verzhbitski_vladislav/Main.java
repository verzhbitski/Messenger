package ru.verzhbitski_vladislav;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;

        Parent rootLogin = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage.setScene(new Scene(rootLogin, 350, 100));
        stage.setTitle("Messenger / Login");
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
