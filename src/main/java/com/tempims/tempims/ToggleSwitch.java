package com.tempims.tempims;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.time.LocalDate;

public class ToggleSwitch extends HBox {

    private final Label label = new Label();
    private final Button button = new Button();

    private SimpleBooleanProperty isDaily = new SimpleBooleanProperty(false);

    public SimpleBooleanProperty switchOnProperty() {
        return isDaily;
    }

    private void init(String firsttext, String secondtext) {
        button.setText(firsttext);
        label.setText(secondtext);

        getChildren().addAll(label, button);
        button.setOnAction((e) -> {
            isDaily.set(!isDaily.get());
        });
        setStyle();
        bindProperties();
    }

    private void setStyle() {
        setWidth(120);
        label.setAlignment(Pos.CENTER);
        setStyle("-fx-background-color: grey; -fx-text-fill:black; -fx-background-radius: 20;");
        button.setStyle("-fx-text-fill:black; -fx-background-radius: 20;");
        setAlignment(Pos.CENTER_LEFT);
    }

    private void bindProperties() {
        label.prefWidthProperty().bind(widthProperty().divide(2));
        label.prefHeightProperty().bind(heightProperty());
        button.prefWidthProperty().bind(widthProperty().divide(2));
        button.prefHeightProperty().bind(heightProperty());
    }

    public ToggleSwitch(String firsttext, String secondtext) {
        init(firsttext,secondtext);
        isDaily.addListener((a, b, c) -> {
            if (c) {
                label.setText(firsttext);
                button.setText(secondtext);
                button.setStyle("-fx-text-fill:black; -fx-background-radius: 20;");
                label.setStyle("-fx-background-color: grey; -fx-text-fill:black; -fx-background-radius: 20;");
                label.toFront();
            } else {
                label.setText(secondtext);
                button.setText(firsttext);
                button.setStyle("-fx-text-fill:black; -fx-background-radius: 20;");
                label.setStyle("-fx-background-color: grey; -fx-text-fill:black; -fx-background-radius: 20;");
                button.toFront();
            }
        });
    }
}
