package com.tempims.tempims;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    static Stage globalStage;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        ProcessLogs.setUpDate();
        globalStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(LoginScreen.class.getResource("LoginScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 480, 360);
        stage.setTitle("tempIMS");
        Stop[] stops = new Stop[]{new Stop(0, Color.web("#9e899b")), new Stop(1, Color.GRAY)};
        LinearGradient lg1 = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
        scene.setFill(lg1);
        stage.setScene(scene);
        stage.show();
    }
}
