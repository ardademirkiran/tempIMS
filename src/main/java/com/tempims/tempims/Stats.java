package com.tempims.tempims;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Stats {
    //static HashMap<String, Number> productChartInfo= new HashMap<>();
    static LocalDate currentDate = java.time.LocalDate.now();

    public static HashMap<String, Double> createPieChartData(ArrayList<SalesObject> salesObjects){
        HashMap<String, Double> pieChartDataMap = new HashMap<>();
        for (SalesObject salesObject : salesObjects){
            if (pieChartDataMap.containsKey(salesObject.getName())){
                Double currentProfitValue = pieChartDataMap.get(salesObject.getName());
                pieChartDataMap.replace(salesObject.getName(), currentProfitValue + salesObject.getProfit());
            } else {
                pieChartDataMap.put(salesObject.getName(), salesObject.getProfit());
            }
        }


        return pieChartDataMap;
    }

    public static LinkedHashMap<String, Double> createBarChartData(ArrayList<SalesObject> salesObjects, boolean isMonthly){
        LinkedHashMap<String, Double> barChartDataMap = new LinkedHashMap<>();
        String dateKey;
        for (SalesObject salesObject : salesObjects){
            if (isMonthly){
                dateKey = salesObject.getDateOfSale().getDayOfMonth() + "/" + salesObject.getDateOfSale().getDayOfMonth() + "/" + salesObject.getDateOfSale().getYear();
            } else {
                dateKey = salesObject.getDateOfSale().getDayOfMonth() + "/" + salesObject.getDateOfSale().getYear();
            }
            if (barChartDataMap.containsKey(dateKey)){
                Double currentProfitValue = barChartDataMap.get(dateKey);
                barChartDataMap.replace(dateKey, currentProfitValue + salesObject.getProfit());
            } else {
                barChartDataMap.put(dateKey, salesObject.getProfit());
            }
        }
        return barChartDataMap;
    }


    public static HashMap<String, Number> createMonthlyPieChartInfo(){
        HashMap<String, Number> productMonthlyChartInfo= new HashMap<>();
        ResultSet rs = DBAccess.fetchProducts("statView");
        try{
            while(rs.next()){
                productMonthlyChartInfo.put(rs.getString("NAME"), rs.getDouble("PROFIT_RETURN"));
            }
        }
        catch(SQLException e){
            e.printStackTrace(System.out);
        }
        return productMonthlyChartInfo;
    }

    public static HashMap<String, Number> createDailyPieChartInfo(){
        HashMap<String, Number> productDailyChartInfo= DBAccess.dailyPieChartInfo();
        //sql part to create daily profits hashmap

        return productDailyChartInfo;

    }

    public static double calculateProfit(Products product){
        double unitBuyPrice = DBAccess.fetchUnitBuyingPrice(product.barcode); //sql part to get buyPrice per unit by using "product.barcode"
        double profitToAdd = product.amount * (product.calculatedUnitSellPrice - unitBuyPrice);
        return profitToAdd;

    }


    public static LinkedHashMap<String, Double> calculateMonthlyProfits(LinkedHashMap<String, Double> datesAndProfits){
        LinkedHashMap<String, Double> monthsAndProfits = new LinkedHashMap<>();
        String currentMonth = "1111-11-11";
        Double monthsProfit = 0.00;
        String loopCurrentMonth;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (String ldString : datesAndProfits.keySet()){
            LocalDate ld = LocalDate.parse(ldString, formatter);
            loopCurrentMonth = ld.getMonthValue() + "/" + ld.getYear();
            if (!loopCurrentMonth.equals(currentMonth)){
                monthsAndProfits.put(currentMonth, monthsProfit);
                currentMonth = loopCurrentMonth;
                monthsProfit = 0.00;
            }
            monthsProfit += datesAndProfits.get(ldString);
        }
        monthsAndProfits.put(currentMonth, monthsProfit);
        monthsAndProfits.remove("1111-11-11");
        System.out.println(monthsAndProfits);
        return monthsAndProfits;
    }

    public static double calculateAverageEntryPrice(int prevAmount, double prevAvPrice, int numOfNewProducts, double newEntryPrice){
        double totalRevenue = (prevAmount * prevAvPrice) + (numOfNewProducts * newEntryPrice);
        return totalRevenue / (prevAmount + numOfNewProducts);
    }


}
