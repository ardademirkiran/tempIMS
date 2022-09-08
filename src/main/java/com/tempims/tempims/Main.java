package com.tempims.tempims;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    static Stage globalStage;
    static Boolean isFirstExec = false;
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        globalStage = stage;
        isFirstExec = ProcessLogs.checkLogsFile();
        if (isFirstExec){
            DBAccess.insertUser("admin", UserInteractions.hashPassword("admin"), "1111111");
        } else {
            System.out.println("Bu programın ilk açılışı değil");
        }FXMLLoader fxmlLoader = new FXMLLoader(LoginScreen.class.getResource("LoginScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 480, 360);
        stage.setTitle("tempIMS");
        setGradient(stage, scene);
        stage.setFullScreenExitHint("Tam Ekrandan Çıkmak İçin Esc'ye basınız");
        stage.fullScreenProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if (!aBoolean){
                    stage.setMinWidth(1100);
                    stage.setMinHeight(800);
                }
            }
        });
        stage.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (KeyCode.F11.equals(keyEvent.getCode())){
                stage.setFullScreen(!stage.isFullScreen());
            }
        });
    }

    static void setGradient(Stage stage, Scene scene) {
        Stop[] stops = new Stop[]{new Stop(0, Color.web("#9e899b")), new Stop(1, Color.GRAY)};
        LinearGradient lg1 = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
        scene.setFill(lg1);
        stage.setScene(scene);
        stage.show();
    }
}
