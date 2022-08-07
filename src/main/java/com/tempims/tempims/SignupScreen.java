package com.tempims.tempims;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class SignupScreen {


    public TextField usernameField;
    public PasswordField passwordField;
    public PasswordField passwordField1;

    public void loginButtonAction(ActionEvent actionEvent) throws IOException {
        Stop[] stops = new Stop[] { new Stop(0, Color.web("#9e899b")), new Stop(1, Color.GRAY)};
        LinearGradient lg1 = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
        FXMLLoader fxmlLoader = new FXMLLoader(LoginScreen.class.getResource("LoginScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 480, 360);
        scene.setFill(lg1);
        Main.globalStage.setScene(scene);
    }

    public void signupButtonAction(ActionEvent actionEvent) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String password1 = passwordField1.getText();
        BufferedWriter writer = new BufferedWriter(new FileWriter("datas.txt", true));
        if (password1.equals(password)){
            writer.write(username + "\t" + password + "\n");
            writer.close();
            loginButtonAction(new ActionEvent());
        }

    }
}
