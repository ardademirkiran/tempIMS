package com.tempims.tempims;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductInteractions {
    public static void productEntry(String barcodeInput, String brandInput, String nameInput, String numberInput, String taxInput, String totalBuyPriceInput, String unitBuyPriceInput, String sellPriceInput) {

        DBAccess.insertProduct(barcodeInput, brandInput, nameInput, numberInput, taxInput, totalBuyPriceInput, unitBuyPriceInput, sellPriceInput);

    }

    public static void sellProduct(String barcode, int amountInput) {

        DBAccess.updateStock(barcode, -amountInput); //sql part to put remaining amount to DB

    }

    public static Products getProduct(String barcode) {
        String[] productInfo = (DBAccess.newProductInfo(barcode)).split(":");
        String name = productInfo[0]; //sql part to get name
        int tax = Integer.parseInt(productInfo[1]); //sql part to get tax
        double sellprice = Double.parseDouble(productInfo[2].replace(",", "."));//sql part to get sellprice

        return new Products(barcode, name, tax, sellprice);
    }
    public static ArrayList<AllProducts> createAllProducts(){
        ArrayList<AllProducts> allProducts = new ArrayList<>();
        ResultSet rs = DBAccess.fetchProducts("stockView");
        try {
            while (rs.next()) {
                allProducts.add(new AllProducts(rs.getString("BARCODE"), rs.getString("BRAND"), rs.getString("NAME"),
                        String.valueOf(rs.getInt("PRODUCT_NUMBER")), String.valueOf(rs.getInt("TAX")),
                        String.valueOf(rs.getDouble("UNIT_BUYING_PRICE")), String.valueOf(rs.getDouble("UNIT_SELLING_PRICE"))));
            }
        }
        catch (SQLException e){
            e.printStackTrace(System.out);
        }
        return allProducts;
    }

}