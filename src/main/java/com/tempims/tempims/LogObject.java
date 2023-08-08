package com.tempims.tempims;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.util.converter.LocalDateTimeStringConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;

public class LogObject implements Comparable<LogObject> {
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
    public LocalDateTime getDateTime(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd\tHH:mm");
        return LocalDateTime.parse(this.dateTime);
    }

    public ObservableValue<String> getType(){
        return new SimpleStringProperty(type);
    }public ObservableValue<String> getExplanation(){
        return new SimpleStringProperty(explanation);
    }public ObservableValue<String> getDetails(){
        return new SimpleStringProperty(details);
    }public ObservableValue<String> getEmployee(){
        return new SimpleStringProperty(employee);
    }public ObservableValue<String> getDateString(){
        return new SimpleStringProperty(dateTime);
    }

    @Override
    public int compareTo(LogObject o) {
        return o.getDateTime().compareTo(this.getDateTime());
    }
}
