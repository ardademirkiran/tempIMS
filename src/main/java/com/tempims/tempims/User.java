package com.tempims.tempims;

import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;

public class User {
    public String username;
    public boolean sellScreenPerm;
    public boolean entryScreenPerm;
    public boolean historyScreenPerm;
    public boolean trackStockScreenPerm;
    public boolean statsScreenPerm;
    public boolean usersScreenPerm;

    public CheckBox checkBoxSellScreenPerm = new CheckBox();
    public CheckBox checkBoxEntryScreenPerm = new CheckBox();
    public CheckBox checkBoxHistoryScreenPerm = new CheckBox();
    public CheckBox checkBoxTrackStockScreenPerm = new CheckBox();
    public CheckBox checkBoxStatsScreenPerm = new CheckBox();
    public CheckBox checkBoxUsersScreenPerm = new CheckBox();

    public User(String username, String permissions) {
        this.username = username;
        sellScreenPerm = Character.toString(permissions.charAt(0)).equals("1");
        entryScreenPerm = Character.toString(permissions.charAt(1)).equals("1");
        historyScreenPerm = Character.toString(permissions.charAt(2)).equals("1");
        trackStockScreenPerm = Character.toString(permissions.charAt(3)).equals("1");
        statsScreenPerm = Character.toString(permissions.charAt(4)).equals("1");
        usersScreenPerm = Character.toString(permissions.charAt(5)).equals("1");
        checkBoxEntryScreenPerm.setSelected(entryScreenPerm);
        checkBoxEntryScreenPerm.selectedProperty().addListener((observable -> UserInteractions.changePermissions(username,getpermissions())));
        checkBoxStatsScreenPerm.setSelected(statsScreenPerm);
        checkBoxStatsScreenPerm.selectedProperty().addListener((observable -> UserInteractions.changePermissions(username,getpermissions())));
        checkBoxUsersScreenPerm.setSelected(usersScreenPerm);
        checkBoxUsersScreenPerm.selectedProperty().addListener((observable -> UserInteractions.changePermissions(username,getpermissions())));
        checkBoxHistoryScreenPerm.setSelected(historyScreenPerm);
        checkBoxHistoryScreenPerm.selectedProperty().addListener((observable -> UserInteractions.changePermissions(username,getpermissions())));
        checkBoxSellScreenPerm.setSelected(sellScreenPerm);
        checkBoxSellScreenPerm.selectedProperty().addListener((observable -> UserInteractions.changePermissions(username,getpermissions())));
        checkBoxTrackStockScreenPerm.setSelected(trackStockScreenPerm);
        checkBoxTrackStockScreenPerm.selectedProperty().addListener((observable -> UserInteractions.changePermissions(username,getpermissions())));
    }


    public String getpermissions(){
        return checkBoxSellScreenPerm.isSelected() +String.valueOf(checkBoxEntryScreenPerm.isSelected())+checkBoxHistoryScreenPerm.isSelected()+ checkBoxTrackStockScreenPerm.isSelected() +checkBoxStatsScreenPerm.isSelected()+checkBoxUsersScreenPerm.isSelected();
    }

    private ObservableValue<CheckBox> getCheckBoxObservableValue(CheckBox checkBoxEntryScreenPerm) {
        return new ObservableValue<CheckBox>() {
            @Override
            public void addListener(ChangeListener<? super CheckBox> changeListener) {

            }

            @Override
            public void removeListener(ChangeListener<? super CheckBox> changeListener) {

            }

            @Override
            public CheckBox getValue() {
                return checkBoxEntryScreenPerm;
            }

            @Override
            public void addListener(InvalidationListener invalidationListener) {

            }

            @Override
            public void removeListener(InvalidationListener invalidationListener) {

            }
        };
    }

    public ObservableValue<CheckBox> getCheckBoxUsersScreenPerm() {
        return getCheckBoxObservableValue(checkBoxUsersScreenPerm);
    }

    public ObservableValue<CheckBox> getCheckBoxHistoryScreenPerm() {
        return getCheckBoxObservableValue(checkBoxHistoryScreenPerm);
    }

    public ObservableValue<CheckBox> getCheckBoxSellScreenPerm() {
        return getCheckBoxObservableValue(checkBoxSellScreenPerm);
    }

    public ObservableValue<CheckBox> getCheckBoxStatsScreenPerm() {
        return getCheckBoxObservableValue(checkBoxStatsScreenPerm);
    }

    public ObservableValue<CheckBox> getCheckBoxTrackStockScreenPerm() {
        return getCheckBoxObservableValue(checkBoxTrackStockScreenPerm);
    }

    public ObservableValue<CheckBox> getCheckBoxEntryScreenPerm() {
        return getCheckBoxObservableValue(checkBoxEntryScreenPerm);
    }
    public SimpleStringProperty getUserName(){
        return new SimpleStringProperty(this.username);
    }



}
