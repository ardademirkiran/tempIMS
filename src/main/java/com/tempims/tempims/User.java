package com.tempims.tempims;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

public class User {
    public String username;
    public String sellScreenPerm;
    public String entryScreenPerm;
    public String historyScreenPerm;
    public String trackStockScreenPerm;
    public String statsScreenPerm;
    public String usersScreenPerm;

    public User(String username, String permissions) {
        this.username = username;
        if(permissions.indexOf(0) == 1){
            sellScreenPerm = "1";
        } else {
            sellScreenPerm = "0";
        }
        if(permissions.indexOf(1) == 1){
            entryScreenPerm = "1";
        } else {
            entryScreenPerm = "0";
        }
        if(permissions.indexOf(2) == 1){
            historyScreenPerm = "1";
        } else {
            historyScreenPerm = "0";
        }
        if(permissions.indexOf(3) == 1){
            trackStockScreenPerm = "1";
        } else {
            trackStockScreenPerm = "0";
        }
        if(permissions.indexOf(4) == 1){
            statsScreenPerm = "1";
        } else {
            statsScreenPerm = "0";
        }
        if(permissions.indexOf(5) == 1){
            usersScreenPerm = "1";
        } else {
            usersScreenPerm = "0";
        }
    }

    public ObservableValue<String> getSellScreenPerm(){
        return new SimpleStringProperty(sellScreenPerm);
    }public ObservableValue<String> getEntryScreenPerm(){
        return new SimpleStringProperty(entryScreenPerm);
    } public ObservableValue<String> getHistoryScreenPerm(){
        return new SimpleStringProperty(historyScreenPerm);
    }public ObservableValue<String> getTrackStockScreenPerm(){
        return new SimpleStringProperty(trackStockScreenPerm);
    } public ObservableValue<String> getStatsScreenPerm(){
        return new SimpleStringProperty(statsScreenPerm);
    }public ObservableValue<String> getUsersScreenPerm(){
        return new SimpleStringProperty(usersScreenPerm);
    }


    public static void changePermissions(String username, String permissions) {
        //sql part to insert new permissions string to USERS db by using username
    }

}
