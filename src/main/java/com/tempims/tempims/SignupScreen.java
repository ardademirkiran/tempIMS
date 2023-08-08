package com.tempims.tempims;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;

public class SignupScreen {
    private MainScreen mainScreenController;

    public static String username;
    public TextField usernameField;
    public PasswordField passwordField;
    public PasswordField passwordField1;
    public Button signupButton;

    @FXML
    public void initialize() {
        usernameField.setContextMenu(new ContextMenu());
        passwordField1.setContextMenu(new ContextMenu());
        passwordField.setContextMenu(new ContextMenu());
        if (username.isEmpty()) {
            usernameField.setText("");
            signupButton.setText("Kaydol");
            usernameField.setFocusTraversable(true);
            usernameField.setEditable(true);
        } else {
            usernameField.setText(username);
            usernameField.setEditable(false);
            usernameField.setFocusTraversable(false);
            signupButton.setText("Şifreyi Yenile");
        }
    }

    @FXML
    public void signupButtonAction() throws IOException, SQLException {
        if (username.isEmpty()) {
            int flag = UserInteractions.checkSignUp(usernameField.getText(), passwordField.getText(), passwordField1.getText(), "1111110");
            if (flag == -2) {
                System.out.println("Bu kullanıcı adı zaten kullanılıyor.");
            } else if (flag == -1) {
                System.out.println("Kullanıcı adı ya da şifre boş olamaz.");
            } else if (flag == 0) {
                System.out.println("Şifre ve şifre onayı birbiriyle eşleşmiyor.");
            } else {
                System.out.println("Yeni kullanıcı oluşturuldu.");
                mainScreenController.onUserTabOpened();
            }
        } else {
            if (passwordField.getText().equals("") || passwordField1.getText().equals("") || usernameField.getText().equals("")) {
                System.out.println("Kullanıcı adı ya da şifre boş olamaz.");
            } else if (!passwordField1.getText().equals(passwordField.getText())) {
                System.out.println("Şifre ve şifre onayı birbiriyle eşleşmiyor.");
            } else {
                UserInteractions.changePassword(username, passwordField.getText());
            }
        }
    }
    public void setFirstController(MainScreen mainScreenController) {
        this.mainScreenController = mainScreenController;
    }
}
