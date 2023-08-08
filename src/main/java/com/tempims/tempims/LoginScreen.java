package com.tempims.tempims;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

import java.io.IOException;

public class LoginScreen {
    String errorStyle = "-fx-border-color: BLACK; -fx-border-width: 2; -fx-border-radius: 5;";
    String successStyle = "-fx-border-color: #A9A9A9; -fx-border-width: 2; -fx-border-radius: 5;";
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label warningLabel;

    public void initialize() {
        passwordField.setContextMenu(new ContextMenu());
        usernameField.setContextMenu(new ContextMenu());
    }

    public void loginButtonAction() throws IOException {
        int flag = UserInteractions.checkLogin(usernameField.getText(), passwordField.getText());
        if (flag == -1) {
            warningLabel.setText("Böyle bir kullanıcı yok.");
            TextField[] textFields = {usernameField};
            invalidInputAction(textFields);
        } else if (flag == 0) {
            warningLabel.setText("Bu kullanıcı adı için şifre yanlış.");
            TextField[] textFields = {passwordField};
            invalidInputAction(textFields);
        } else {
            if (Main.isFirstExec) {
                Stop[] stops = new Stop[]{new Stop(0, Color.web("#9e899b")), new Stop(1, Color.GRAY)};
                LinearGradient lg1 = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
                FXMLLoader fxmlLoader = new FXMLLoader(LoginScreen.class.getResource("FirstOpenScreen.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                scene.setFill(lg1);
                Main.globalStage.setScene(scene);
            } else {
                FirstOpenScreen.openMainScreen();
            }
        }

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
