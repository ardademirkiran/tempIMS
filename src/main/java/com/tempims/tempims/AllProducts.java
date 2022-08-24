package com.tempims.tempims;

import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AllProducts {
    public static HashSet<AllProducts> buyArray = new HashSet<>() {};
    Button button = new Button("Ekle");
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
        AllProducts allProducts = this;
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                buyArray.add(allProducts);
            }
        });
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
    public ObservableValue<Button> getButton(){
        return new ObservableValue<>() {
            @Override
            public void addListener(ChangeListener<? super Button> changeListener) {

            }

            @Override
            public void removeListener(ChangeListener<? super Button> changeListener) {

            }

            @Override
            public Button getValue() {
                return button;
            }

            @Override
            public void addListener(InvalidationListener invalidationListener) {

            }

            @Override
            public void removeListener(InvalidationListener invalidationListener) {

            }
        };
    }
}
