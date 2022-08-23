package com.tempims.tempims;

import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Stats {
    static HashMap<String, Number> productChartInfo= new HashMap<>();
    static LocalDate currentDate = java.time.LocalDate.now();

    public static void createChartInfo(){
        productChartInfo.clear();
        ResultSet rs = DBAccess.fetchProducts("statView");
        try{
            while(rs.next()){
                productChartInfo.put(rs.getString("NAME"), rs.getDouble("PROFIT"));
            }
        }
        catch(SQLException e){
            e.printStackTrace(System.out);
        }
    }


    public static void updateProfit(ObservableList<Products> soldProducts) {
        double totalProfit = 0;
        for(Products product : soldProducts){
            double unitBuyPrice = DBAccess.fetchUnitPrice(product.barcode); //sql part to get buyPrice per unit by using "product.barcode"
            double profitToAdd = product.amount * (product.calculatedunitsellprice - unitBuyPrice);
            totalProfit += profitToAdd;
            DBAccess.amendProfit(product.barcode,profitToAdd); //update profit on database
        }

        DBAccess.updateDailyProfit(currentDate,totalProfit);//sql part to update current daily profit with "current daily profit + totalProfit" by using "currentDate"

    }

    public static LinkedHashMap<String, Double> calculateMonthlyProfits(LinkedHashMap<LocalDate, Double> datesAndProfits){
        LinkedHashMap<String, Double> monthsAndProfits = new LinkedHashMap<>();
        String currentMonth = " ";
        Double monthsProfit = 0.00;
        String loopCurrentMonth;
        for (LocalDate ld : datesAndProfits.keySet()){
            loopCurrentMonth = ld.getMonthValue() + "/" + ld.getYear();
            if (!loopCurrentMonth.equals(currentMonth)){
                monthsAndProfits.put(currentMonth, monthsProfit);
                currentMonth = loopCurrentMonth;
                monthsProfit = 0.00;
            }
            monthsProfit += datesAndProfits.get(ld);
        }
        monthsAndProfits.put(currentMonth, monthsProfit);
        monthsAndProfits.remove(" ");
        System.out.println(monthsAndProfits);
        return monthsAndProfits;
    }


}
