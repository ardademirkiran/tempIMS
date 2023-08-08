package com.tempims.tempims;

import java.util.ArrayList;
import java.util.Objects;

public class ProductInteractions {
    public static void productEntry(String barcodeInput, String brandInput, String nameInput, String numberInput, String taxInput, String totalBuyPriceInput, String unitBuyPriceInput, String sellPriceInput) {

        DBAccess.insertProduct(barcodeInput, brandInput, nameInput, numberInput, taxInput, totalBuyPriceInput, unitBuyPriceInput, sellPriceInput);

    }


    public static SellScreenProduct getProduct(String barcode) {
        String[] productInfo = (DBAccess.newProductInfo(barcode)).split(":");
        if (Objects.equals(productInfo[0], "null")){
            System.out.println("Ürün dbde yok");
            return null;
        }
        String brand = productInfo[1];
        String name = productInfo[2]; //sql part to get name
        int productNumber = Integer.parseInt(productInfo[3]);
        int tax = Integer.parseInt(productInfo[4]); //sql part to get tax
        double unitBuyPrice = Double.parseDouble(productInfo[5].toString().replace(",", ""));
        double totalBuyPrice = Double.parseDouble(productInfo[6].toString().replace(",", ""));
        double unitSellPrice =  Double.parseDouble(productInfo[7].toString().replace(",", ""));

        return new SellScreenProduct(barcode, brand, name, productNumber, tax, unitBuyPrice, totalBuyPrice, unitSellPrice);
    }
    public static ArrayList<StockViewProduct> createAllProducts(){
        return DBAccess.fetchProducts();
    }
    public static ArrayList<SellScreenProduct> getNonBarcodeProducts(){
        return DBAccess.fetchNonBarcodeProducts();
    }

}