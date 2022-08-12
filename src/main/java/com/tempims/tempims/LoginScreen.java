package com.tempims.tempims;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

import java.io.IOException;

public class LoginScreen {
    String errorStyle = String.format("-fx-border-color: BLACK; -fx-border-width: 2; -fx-border-radius: 5;");
    String successStyle = String.format("-fx-border-color: #A9A9A9; -fx-border-width: 2; -fx-border-radius: 5;");
    @FXML
    private Button loginButton, signupButton;
    @FXML
    private Label forgatLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label warningLabel;

    public void loginButtonAction(ActionEvent actionEvent) throws IOException {
        int flag = Session.checkLogin(usernameField.getText(), passwordField.getText());
        if (flag == -1) {
            warningLabel.setText("Böyle bir kullanıcı yok.");
            TextField[] textFields = {usernameField};
            invalidInputAction(textFields);
        } else if (flag == 0) {
            warningLabel.setText("Bu kullanıcı adı için şifre yanlış.");
            TextField[] textFields = {passwordField};
            invalidInputAction(textFields);
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginScreen.class.getResource("MainScreen.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Main.globalStage.setMaximized(true);
            Main.globalStage.setScene(scene);
        }

    }

    public void signupButtonAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginScreen.class.getResource("SignupScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 480, 360);
        Stop[] stops = new Stop[]{new Stop(0, Color.web("#9e899b")), new Stop(1, Color.GRAY)};
        LinearGradient lg1 = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
        scene.setFill(lg1);
        Main.globalStage.setScene(scene);

    }

    public void forgetPassword() {
        //şifremi unuttum kısmı
    }

    public void invalidInputAction(TextField[] textFields) {
        passwordField.setStyle(successStyle);
        usernameField.setStyle(successStyle);
        warningLabel.setVisible(true);
        for (TextField t : textFields) {
            t.setStyle(errorStyle);
        }
        passwordField.setText("");
        usernameField.setText("");
    }
}
