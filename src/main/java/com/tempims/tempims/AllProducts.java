package com.tempims.tempims;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

public class AllProducts {
    String barcode, brand, name;
    int amount,tax,unitbuy,unitsell;
    AllProducts(String barcodeInput, String brandInput, String nameInput, String numberInput, String taxInput, String unitBuyPriceInput, String sellPriceInput){
        this.barcode = barcodeInput;
        this.brand = brandInput;
        this.name = nameInput;
        this.amount = Integer.parseInt(numberInput);
        this.tax = Integer.parseInt(taxInput);
        this.unitbuy = Integer.parseInt(unitBuyPriceInput);
        this.unitsell = Integer.parseInt(sellPriceInput);
    }
    public ObservableValue<Integer> gettax() {
        return new SimpleIntegerProperty(tax).asObject();
    }public ObservableValue<Integer> getamont() {
        return new SimpleIntegerProperty(amount).asObject();
    }public ObservableValue<Integer> getunitbuy() {
        return new SimpleIntegerProperty(unitbuy).asObject();
    }public ObservableValue<Integer> getunitsell() {
        return new SimpleIntegerProperty(unitsell).asObject();
    }public ObservableValue<String> getbarcode() {
        return new SimpleStringProperty(barcode);
    }public ObservableValue<String> getbrand() {
        return new SimpleStringProperty(brand);
    }public ObservableValue<String> getname() {
        return new SimpleStringProperty(name);
    }public ObservableValue<Integer> getprofit() {
        return new SimpleIntegerProperty((unitsell-unitbuy)).asObject();
    }
}
