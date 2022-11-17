package com.tempims.tempims;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Stats {

    public static HashMap<String, Double> createPieChartData(ArrayList<SalesObject> salesObjects){
        HashMap<String, Double> pieChartDataMap = new HashMap<>();
        for (SalesObject salesObject : salesObjects){
            System.out.println(salesObject.getName());
            if (pieChartDataMap.containsKey(salesObject.getName())){
                Double currentProfitValue = pieChartDataMap.get(salesObject.getName());
                pieChartDataMap.replace(salesObject.getName(), currentProfitValue + salesObject.getProfit());
            } else {
                pieChartDataMap.put(salesObject.getName(), salesObject.getProfit());
            }
        }
        System.out.println(pieChartDataMap.get("d d"));


        return pieChartDataMap;
    }

    public static LinkedHashMap<String, Double> createLineChartData(ArrayList<SalesObject> salesObjects, boolean isMonthly){
        LinkedHashMap<String, Double> barChartDataMap = new LinkedHashMap<>();
        String dateKey;
        for (SalesObject salesObject : salesObjects){
            if (isMonthly){
                dateKey = salesObject.getDateOfSale().getDayOfMonth() + "/" + salesObject.getDateOfSale().getMonthValue() + "/" + salesObject.getDateOfSale().getYear();
            } else {
                dateKey = salesObject.getDateOfSale().getMonthValue() + "/" + salesObject.getDateOfSale().getYear();
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

    public static double calculateProfit(SellScreenProduct product){
        double unitBuyPrice = DBAccess.fetchUnitBuyingPrice(product.barcode); //sql part to get buyPrice per unit by using "product.barcode"
        double profitToAdd = product.amount * (product.calculatedUnitSellPrice - unitBuyPrice);
        return profitToAdd;

    }

    public static double calculateAverageEntryPrice(int prevAmount, double prevAvPrice, int numOfNewProducts, double newEntryPrice){
        double totalRevenue = (prevAmount * prevAvPrice) + (numOfNewProducts * newEntryPrice);
        return totalRevenue / (prevAmount + numOfNewProducts);
    }


}
