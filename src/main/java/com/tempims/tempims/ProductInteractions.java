package com.tempims.tempims;

public class ProductInteractions {
    public static void productEntry(String barcodeInput, String brandInput, String nameInput, String numberInput, String taxInput, String totalBuyPriceInput, String unitBuyPriceInput, String sellPriceInput){
        String barcodeFromDB = "" ; //sql part to check barcode exists on DB by using barcodeInput
        if (barcodeFromDB == null){ // barcode doesn't exists on DB
            //sql parts to put Inputs (parameters of this method) to DB respectively

        } else {  //barcode exists on DB case, We are not dealing with this for now.

        }

    }
}
