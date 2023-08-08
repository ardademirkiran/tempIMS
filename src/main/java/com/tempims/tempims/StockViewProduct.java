package com.tempims.tempims;

import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;

import java.util.HashSet;

public class StockViewProduct extends Product {
    public static HashSet<StockViewProduct> buyArray = new HashSet<>() {};
    Button button = new Button("Ekle");
    StockViewProduct(String barcode, String brand, String name, int number, int tax, double unitBuyPrice, double totalBuyPrice, double unitSellPrice){
        super(barcode, brand, name, number, tax, unitBuyPrice, totalBuyPrice, unitSellPrice);
        StockViewProduct allProducts = this;
        button.setOnAction(actionEvent -> buyArray.add(allProducts));
    }
    public ObservableValue<Integer> getTax() {
        return new SimpleIntegerProperty(tax).asObject();
    }public ObservableValue<Integer> getAmount() {
        return new SimpleIntegerProperty(number).asObject();
    }public ObservableValue<Double> getUnitBuyPrice() {
        return new SimpleDoubleProperty(unitBuyPrice).asObject();
    }public ObservableValue<Double> getUnitSellPrice() {
        return new SimpleDoubleProperty(unitSellPrice).asObject();
    }public ObservableValue<String> getBarcode() {
        return new SimpleStringProperty(barcode);
    }public ObservableValue<String> getBrand() {
        return new SimpleStringProperty(brand);
    }public ObservableValue<String> getName() {
        return new SimpleStringProperty(name);
    }public ObservableValue<Double> getProfit() {
        return new SimpleDoubleProperty((unitSellPrice - unitBuyPrice)).asObject();
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
