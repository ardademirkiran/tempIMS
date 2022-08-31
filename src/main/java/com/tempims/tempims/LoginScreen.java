package com.tempims.tempims;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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
            if (Main.isFirstExec){
                FXMLLoader fxmlLoader = new FXMLLoader(LoginScreen.class.getResource("FirstOpenScreen.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Main.globalStage.setScene(scene);
            }else {
                FXMLLoader fxmlLoader = new FXMLLoader(LoginScreen.class.getResource("MainScreen.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Main.globalStage.setMaximized(true);
                Main.globalStage.setScene(scene);
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
