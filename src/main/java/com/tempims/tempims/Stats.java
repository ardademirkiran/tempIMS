package com.tempims.tempims;

import javafx.collections.ObservableList;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Stats {
    static HashMap<String, Number> productChartInfo= new HashMap<>();
    static LocalDate currentDate = java.time.LocalDate.now();

    public static void createChartInfo(){
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


    public static void updateProfit(ObservableList<Products> soldProducts) throws IOException {
        double totalProfit = 0;
        for(Products product : soldProducts){
            double unitBuyPrice = DBAccess.fetchUnitPrice(product.barcode); //sql part to get buyPrice per unit by using "product.barcode"
            double profitToAdd = product.amount * (product.calculatedunitsellprice - unitBuyPrice);
            totalProfit += profitToAdd;
            DBAccess.amendProfit(product.barcode,profitToAdd); //update profit on database
        }

        DBAccess.updateDailyProfit(currentDate,totalProfit);//sql part to update current daily profit with "current daily profit + totalProfit" by using "currentDate"

    }


}
