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
        int flag = Session.checkLogin(usernameField.getText(), passwordField.getText());
        if (flag == -1){
            System.out.println("Böyle bir kullanıcı adı yok.");
        } else if (flag == 0){
            System.out.println("Kullanıcı adı veya şifre yanlış.");
        } else{
            FXMLLoader fxmlLoader = new FXMLLoader(LoginScreen.class.getResource("MainScreen.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Main.globalStage.setMaximized(true);
            Main.globalStage.setMinHeight(Main.globalStage.getHeight());
            Main.globalStage.setMinWidth(Main.globalStage.getWidth());
            Main.globalStage.setScene(scene);
        }
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
