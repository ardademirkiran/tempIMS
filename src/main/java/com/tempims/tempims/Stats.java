package com.tempims.tempims;

import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Stats {
    static HashMap<String, Number> productChartInfo= new HashMap<>();

    public static void createChartInfo(){
        /*  sql part to get name and profit values of all products with != 0 profit value
        for () {
            productChartInfo.put(productName, profit)
      } */
        ResultSet rs = DBAccess.fetchProducts("statView");
        try{
            while(rs.next()){
                System.out.println("runned");
                productChartInfo.put(rs.getString("NAME"), rs.getDouble("PROFIT"));
            }
        }
        catch(SQLException e){
            e.printStackTrace(System.out);
        }
    }


    public static void updateProfit(ObservableList<Products> soldProducts) throws IOException {
        for(Products product : soldProducts){
            double unitBuyPrice = DBAccess.fetchUnitPrice(product.barcode); //sql part to get buyPrice per unit by using "product.barcode"
            double profitToAdd = product.amount * (product.calculatedunitsellprice - unitBuyPrice);
            DBAccess.amendProfit(product.barcode,profitToAdd); //update profit on database
        }

    }

}
