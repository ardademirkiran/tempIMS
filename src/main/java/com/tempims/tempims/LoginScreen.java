package com.tempims.tempims;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LoginScreen {
    @FXML
    private Button loginButton,signupButton;
    @FXML
    private Label  forgatLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;


    public void loginButtonAction(ActionEvent actionEvent) throws IOException {
        String line;
        BufferedReader reader = new BufferedReader(new FileReader("datas.txt"));
        while ((line = reader.readLine()) != null){
            String[] splittedLine = line.split("\t");
            if (usernameField.getText().equals(splittedLine[0]) &&  passwordField.getText().equals(splittedLine[1])){
                FXMLLoader fxmlLoader = new FXMLLoader(LoginScreen.class.getResource("MainScreen.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Main.globalStage.setMaximized(true);
                Main.globalStage.setMinHeight(Main.globalStage.getHeight());
                Main.globalStage.setMinWidth(Main.globalStage.getWidth());
                Main.globalStage.setScene(scene);
            }
        }
        System.out.println("Giriş başarısız");
    }

    public void signupButtonAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginScreen.class.getResource("SignupScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 480, 360);
        Stop[] stops = new Stop[] { new Stop(0, Color.web("#9e899b")), new Stop(1, Color.GRAY)};
        LinearGradient lg1 = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
        scene.setFill(lg1);
        Main.globalStage.setScene(scene);

    }
}
