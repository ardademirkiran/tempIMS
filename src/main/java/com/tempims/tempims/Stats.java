package com.tempims.tempims;

import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Stats {
    static HashMap<String, Number> productChartInfo= new HashMap<>();

    public static void createChartInfo(){
        /*  sql part to get name and profit values of all products with != 0 profit value
        for () {
            productChartInfo.put(productName, profit)
      } */
    }


    public static void updateProfit(ObservableList<Products> soldProducts) throws IOException {
        for(Products product : soldProducts){
            double unitBuyPrice = 0; //sql part to get buyPrice per unit by using "product.barcode"
            double profitToAdd = product.amount * (product.calculatedunitsellprice - unitBuyPrice);
            //update profit on database
        }

    }

}
