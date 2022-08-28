package com.tempims.tempims;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;

import java.util.ArrayList;

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
        sellScreenPerm = permissions.indexOf(0) == 1;
        entryScreenPerm = permissions.indexOf(1) == 1;
        historyScreenPerm = permissions.indexOf(2) == 1;
        trackStockScreenPerm = permissions.indexOf(3) == 1;
        statsScreenPerm = permissions.indexOf(4) == 1;
        usersScreenPerm = permissions.indexOf(5) == 1;
        checkBoxEntryScreenPerm.setSelected(entryScreenPerm);
        checkBoxStatsScreenPerm.setSelected(statsScreenPerm);
        checkBoxUsersScreenPerm.setSelected(usersScreenPerm);
        checkBoxHistoryScreenPerm.setSelected(historyScreenPerm);
        checkBoxSellScreenPerm.setSelected(sellScreenPerm);
        checkBoxTrackStockScreenPerm.setSelected(trackStockScreenPerm);
    }

    public static void changePermissions(String username, String permissions) {
        //sql part to insert new permissions string to USERS db by using username
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

    public ArrayList<User> getAllUsers() {
        ArrayList<User> usersList = new ArrayList<>();
        //sql part to get all users from USERS table
        //fill the arraylist with new User(username, permissions)
        return usersList;
    }

}
