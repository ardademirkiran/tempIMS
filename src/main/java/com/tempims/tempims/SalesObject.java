package com.tempims.tempims;

import java.time.LocalDate;

public class SalesObject {


    private LocalDate dateOfSale;
    private String name;
    private Double profit;

    public LocalDate getDateOfSale() {
        return dateOfSale;
    }


    public Double getProfit() {
        return profit;
    }

    public String getName() {
        return name;
    }

    public SalesObject(LocalDate dateOfSale, String name, Double profit){
        this.dateOfSale = dateOfSale;
        this.name = name;
        this.profit = profit;
    }
}
