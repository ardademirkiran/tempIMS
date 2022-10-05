package com.tempims.tempims;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

import java.io.IOException;

public class FirstOpenScreen {
    public Button signupButton;
    public TextField companyName;
    public PasswordField passwordFieldAgain;
    public PasswordField passwordField;
    public TextField usernameField;

    static void openMainScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginScreen.class.getResource("MainScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stop[] stops = new Stop[]{new Stop(0, Color.web("#9e899b")), new Stop(1, Color.GRAY)};
        LinearGradient lg1 = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
        scene.setFill(lg1);
        Main.globalStage.setScene(scene);
        Main.globalStage.setFullScreen(true);
        Main.globalStage.setHeight(800);
        Main.globalStage.setWidth(1100);
    }

    public void initialize() {
        passwordFieldAgain.setContextMenu(new ContextMenu());
        passwordField.setContextMenu(new ContextMenu());
        usernameField.setContextMenu(new ContextMenu());
    }

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
        } else {
            MainScreen.companyName = companyName.getText();
            DBAccess.removeUser("admin");
            UserInteractions.checkLogin(usernameField.getText(), passwordField.getText());
            System.out.println("Yeni kullanıcı oluşturuldu.");
            openMainScreen();
        }
    }
}
