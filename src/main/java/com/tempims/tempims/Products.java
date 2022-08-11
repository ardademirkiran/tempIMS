package com.tempims.tempims;

import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Products {
    TextField discountper = new TextField("0");
    TextField discount = new TextField("0");
    Label calsellprice = new Label("");
    int barcode,tax,sellprice,amount;
    String name;
    Products(int barcode, String name, int tax, int sellprice){
        this.name = name;
        this.barcode = barcode;
        this.sellprice = sellprice;
        this.tax= tax;
        this.amount = 1;
        this.calsellprice.setText(String.valueOf(sellprice));
        discountper.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                calsellprice.setText(String.valueOf(sellprice-calculatedis()));
                discount.setText(String.valueOf(calculatedis()));
            }
        });
        discount.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                calsellprice.setText(String.valueOf(sellprice-Integer.parseInt(discount.getText())));
                discountper.setText(String.valueOf(calculateperdis()));
            }
        });
    }

    public ObservableValue<TextField> observableValuedisper(){
        return new ObservableValue<TextField>() {
            @Override
            public void addListener(ChangeListener<? super TextField> changeListener) {

            }

            @Override
            public void removeListener(ChangeListener<? super TextField> changeListener) {

            }

            @Override
            public TextField getValue() {
                return discountper;
            }

            @Override
            public void addListener(InvalidationListener invalidationListener) {

            }

            @Override
            public void removeListener(InvalidationListener invalidationListener) {

            }
        };
    }public ObservableValue<TextField> observableValuedis(){
        return new ObservableValue<TextField>() {
            @Override
            public void addListener(ChangeListener<? super TextField> changeListener) {

            }

            @Override
            public void removeListener(ChangeListener<? super TextField> changeListener) {

            }

            @Override
            public TextField getValue() {
                return discount;
            }

            @Override
            public void addListener(InvalidationListener invalidationListener) {
            }

            @Override
            public void removeListener(InvalidationListener invalidationListener) {

            }
        };
    }public ObservableValue<Label> observableValueprice(){
        return new ObservableValue<Label>() {
            @Override
            public void addListener(ChangeListener<? super Label> changeListener) {

            }

            @Override
            public void removeListener(ChangeListener<? super Label> changeListener) {

            }

            @Override
            public Label getValue() {
                return calsellprice;
            }

            @Override
            public void addListener(InvalidationListener invalidationListener) {

            }

            @Override
            public void removeListener(InvalidationListener invalidationListener) {

            }
        };
    }

    public ObservableValue<Integer> getbarcode(){
        return new SimpleIntegerProperty(barcode).asObject();
    }public StringProperty getname(){
        return new SimpleStringProperty(name);
    }public ObservableValue<Integer> gettax(){
        return new SimpleIntegerProperty(tax).asObject();
    }public ObservableValue<Integer> getamount(){
        return new SimpleIntegerProperty(amount).asObject();
    }
    private Integer calculateperdis(){
        return ((Integer.parseInt(this.discount.getText())*100) / this.sellprice);
    }private Integer calculatedis(){
        return (Integer.parseInt(this.discountper.getText())*this.sellprice)/100;
    }
}
