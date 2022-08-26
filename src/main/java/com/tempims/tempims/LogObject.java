package com.tempims.tempims;

import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.time.LocalDateTime;

public class LogObject {
    String dateTime;
    String type;
    String explanation;
    String details;
    String employee;

    public LogObject(String date, String type, String explanation, String details, String employee){
        String[] strings = date.split("\\|\\|");
        strings[1] = strings[1].substring(0,8);
        this.dateTime = strings[0]+"\t"+strings[1];
        this.type = type;
        this.explanation = explanation;
        this.details = details;
        this.employee = employee;
    }
    public ObservableValue<String> getType(){
        return new SimpleStringProperty(type);
    }public ObservableValue<String> getExplanation(){
        return new SimpleStringProperty(explanation);
    }public ObservableValue<String> getDetails(){
        return new SimpleStringProperty(details);
    }public ObservableValue<String> getEmployee(){
        return new SimpleStringProperty(employee);
    }public ObservableValue<String> getDate(){
        return new SimpleStringProperty(dateTime);
    }

}
