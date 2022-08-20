package com.tempims.tempims;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

public class AllProducts {
    String barcode, brand, name;
    int amount,tax;
    double unitbuy,unitsell;
    AllProducts(String barcodeInput, String brandInput, String nameInput, String numberInput, String taxInput, String unitBuyPriceInput, String sellPriceInput){
        this.barcode = barcodeInput;
        this.brand = brandInput;
        this.name = nameInput;
        this.amount = Integer.parseInt(numberInput);
        this.tax = Integer.parseInt(taxInput);
        this.unitbuy = Double.parseDouble(unitBuyPriceInput);
        this.unitsell = Double.parseDouble(sellPriceInput);
    }
    public ObservableValue<Integer> gettax() {
        return new SimpleIntegerProperty(tax).asObject();
    }public ObservableValue<Integer> getamont() {
        return new SimpleIntegerProperty(amount).asObject();
    }public ObservableValue<Double> getunitbuy() {
        return new SimpleDoubleProperty(unitbuy).asObject();
    }public ObservableValue<Double> getunitsell() {
        return new SimpleDoubleProperty(unitsell).asObject();
    }public ObservableValue<String> getbarcode() {
        return new SimpleStringProperty(barcode);
    }public ObservableValue<String> getbrand() {
        return new SimpleStringProperty(brand);
    }public ObservableValue<String> getname() {
        return new SimpleStringProperty(name);
    }public ObservableValue<Double> getprofit() {
        return new SimpleDoubleProperty((unitsell-unitbuy)).asObject();
    }
}
