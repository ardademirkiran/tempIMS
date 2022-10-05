package com.tempims.tempims;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductInteractions {
    public static void productEntry(String barcodeInput, String brandInput, String nameInput, String numberInput, String taxInput, String totalBuyPriceInput, String unitBuyPriceInput, String sellPriceInput) {

        DBAccess.insertProduct(barcodeInput, brandInput, nameInput, numberInput, taxInput, totalBuyPriceInput, unitBuyPriceInput, sellPriceInput);

    }


    public static SellScreenProduct getProduct(String barcode) {
        String[] productInfo = (DBAccess.newProductInfo(barcode)).split(":");
        String brand = productInfo[1];
        String name = productInfo[2]; //sql part to get name
        int productNumber = Integer.parseInt(productInfo[3]);
        int tax = Integer.parseInt(productInfo[4]); //sql part to get tax
        double unitBuyPrice = Double.parseDouble(productInfo[5]);
        double totalBuyPrice = Double.parseDouble(productInfo[6]);
        double unitSellPrice =  Double.parseDouble(productInfo[7]);

        return new SellScreenProduct(barcode, brand, name, productNumber, tax, unitBuyPrice, totalBuyPrice, unitSellPrice);
    }
    public static ArrayList<StockViewProduct> createAllProducts(){
        ArrayList<StockViewProduct> allProducts = new ArrayList<>();
        ResultSet rs = DBAccess.fetchProducts("stockView");
        try {
            while (rs.next()) {
                allProducts.add(new StockViewProduct(rs.getString("BARCODE"), rs.getString("BRAND"), rs.getString("NAME"), rs.getInt("PRODUCT_NUMBER"), rs.getInt("TAX"), rs.getDouble("UNIT_BUYING_PRICE"), rs.getDouble("TOTAL_BUYING_PRICE"), rs.getDouble("UNIT_SELLING_PRICE")));
            }
        }
        catch (SQLException e){
            e.printStackTrace(System.out);
        }
        return allProducts;
    }
    public static ArrayList<SellScreenProduct> getNonBarcodeProducts(){
        ArrayList<SellScreenProduct> nonBarcodeProducts = new ArrayList<>();
        //nonBarcodeProducts = sql part
        return nonBarcodeProducts;
    }

}