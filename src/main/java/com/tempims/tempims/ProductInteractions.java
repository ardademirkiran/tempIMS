package com.tempims.tempims;

public class ProductInteractions {
    public static void productEntry(String barcodeInput, String brandInput, String nameInput, String numberInput, String taxInput, String totalBuyPriceInput, String unitBuyPriceInput, String sellPriceInput){
        /*String barcodeFromDB = "" ; //sql part to check barcode exists on DB by using barcodeInput
        if (barcodeFromDB == null){ // barcode doesn't exists on DB
            //sql parts to put Inputs (parameters of this method) to DB respectively

        } else {  //barcode exists on DB case, We are not dealing with this for now.

        }
        Commented out the above part, DBAccess function handles all internally*/
        DBAccess.insertProduct(barcodeInput,brandInput,nameInput,numberInput,taxInput,totalBuyPriceInput,unitBuyPriceInput,sellPriceInput);
    }
    public static void sellProduct(String barcode, int amountInput){
        /*int amountFromDB = DBAccess.getStock(barcode); //sql part to get real amount of the product
        Commented out the above part because the process is done internally*/
        DBAccess.updateStock(barcode, -amountInput); //sql part to put remaining amount to DB

    }
    public static Products getProduct(String barcode){
        String[] productInfo = (DBAccess.newProductInfo(barcode)).split(":");
        String name = productInfo[0]; //sql part to get name
        int tax = Integer.parseInt(productInfo[1]); //sql part to get tax
        int sellprice = Integer.parseInt(productInfo[2]);//sql part to get sellprice

        return new Products(barcode, name, tax, sellprice );
    }

}
