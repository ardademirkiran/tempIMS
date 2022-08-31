package com.tempims.tempims;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class FirstOpenScreen {
    public Button signupButton;
    public TextField companyName;
    public PasswordField passwordFieldAgain;
    public PasswordField passwordField;
    public TextField usernameField;


    public void signupButtonAction() throws IOException {
        int flag = UserInteractions.checkSignUp(usernameField.getText(), passwordField.getText(), passwordFieldAgain.getText(), "1111111");
        if (flag == -2) {
            System.out.println("Bu kullanıcı adı zaten kullanılıyor.");
        } else if (flag == -1) {
            System.out.println("Kullanıcı adı ya da şifre boş olamaz.");
        } else if (flag == 0) {
            System.out.println("Şifre ve şifre onayı birbiriyle eşleşmiyor.");
        } else if (companyName.getText().isEmpty()) {
            System.out.println("Şirket İsmi Boş Olamaz");
        }else{
            MainScreen.companyName = companyName.getText();
            DBAccess.removeUser("admin");
            UserInteractions.checkLogin(usernameField.getText(),passwordField.getText());
            System.out.println("Yeni kullanıcı oluşturuldu.");
            FXMLLoader fxmlLoader = new FXMLLoader(LoginScreen.class.getResource("MainScreen.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Main.globalStage.setMaximized(true);
            Main.globalStage.setScene(scene);
        }
    }
}
