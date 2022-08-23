package com.tempims.tempims;

public class LogObject {
    String date;
    String type;
    String explanation;
    String details;
    String employee;

    public LogObject(String date, String type, String explanation, String details, String employee){
        this.date = date;
        this.type = type;
        this.explanation = explanation;
        this.details = details;
        this.employee = employee;
    }
}
