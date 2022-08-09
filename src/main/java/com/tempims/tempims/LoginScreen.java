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
    @FXML
    private Label warningLabel;
    String errorStyle = String.format("-fx-border-color: BLACK; -fx-border-width: 2; -fx-border-radius: 5;");
    String successStyle = String.format("-fx-border-color: #A9A9A9; -fx-border-width: 2; -fx-border-radius: 5;");

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
        
        //eğer yanlışsa hangisi yanlışsa içine yolla
        if (true){
            TextField[] textFields = {usernameField,passwordField};
            unvalidinfo(textFields);
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
    public void forgatPass(){
        //şifremi unuttum kısmı
    }
    public void unvalidinfo(TextField[] textFields){
        passwordField.setStyle(successStyle);
        usernameField.setStyle(successStyle);
        warningLabel.setVisible(true);
        for (TextField t: textFields) {
            t.setStyle(errorStyle);
        }
        if (textFields.length > 1 ||  textFields[0] == usernameField){
            warningLabel.setText("Kullanıcı adı hatalı");
        }else{
            warningLabel.setText("Şifre hatalı");
        }
        passwordField.setText("");
        usernameField.setText("");
    }
}
