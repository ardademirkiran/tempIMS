package com.tempims.tempims;

public class Product {
        String barcode;
        String brand;
        String name;
        int number;
        int tax;
        double unitBuyPrice;
        double totalBuyPrice;
        double unitSellPrice;

        public Product(String barcode, String brand, String name, int number, int tax, double unitBuyPrice, double totalBuyPrice, double unitSellPrice){
            this.barcode = barcode;
            this.brand = brand;
            this.name = name;
            this.number = number;
            this.tax = tax;
            this.unitBuyPrice = unitBuyPrice;
            this.totalBuyPrice = totalBuyPrice;
            this.unitSellPrice = unitSellPrice;
        }


}
