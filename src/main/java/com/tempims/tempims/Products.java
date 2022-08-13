package com.tempims.tempims;

import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Products {
    TextField discountper = new TextField("0");
    TextField discount = new TextField("0");
    Label calsellprice = new Label("");
    int tax, unitsellprice, amount, calculatedunitsellprice, sellpricedb;
    String name, barcode;

    Products(String barcode, String name, int tax, int sellpricedb) {
        discount.setPromptText("0");
        discountper.setPromptText("0");
        this.name = name;
        this.barcode = barcode;
        this.unitsellprice = sellpricedb;
        this.sellpricedb = sellpricedb;
        this.calculatedunitsellprice = sellpricedb;
        this.tax = tax;
        this.amount = 1;
        this.calsellprice.setText(String.valueOf(sellpricedb));


    }

    public ObservableValue<TextField> observableValuedisper() {
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
    }

    public ObservableValue<TextField> observableValuedis() {
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
    }

    public ObservableValue<Label> observableValueprice() {
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

    public ObservableValue<String> getbarcode() {
        return new SimpleStringProperty(barcode);
    }

    public StringProperty getname() {
        return new SimpleStringProperty(name);
    }

    public ObservableValue<Integer> gettax() {
        return new SimpleIntegerProperty(tax).asObject();
    }

    public ObservableValue<Integer> getamount() {
        return new SimpleIntegerProperty(amount).asObject();
    }

    private Integer calculateperdis() {
        if (this.discount.getText().isEmpty()){
            return 0;
        }
        return ((Integer.parseInt(this.discount.getText()) * 100) / this.unitsellprice);
    }

    private Integer calculatedis() {
        if (this.discountper.getText().isEmpty()){
            return 0;
        }
        return (Integer.parseInt(this.discountper.getText()) * this.unitsellprice) / 100;
    }
    public void init(){
        discountper.setOnKeyTyped(keyEvent -> {
            try {
                calculatedunitsellprice = (sellpricedb - calculatedis());
                discount.setText(String.valueOf(calculatedis()));
                calsellprice.setText(String.valueOf((unitsellprice - Integer.parseInt(discount.getText())) * amount));
                MainScreen.changetotaldata();
            }catch (NumberFormatException e){
                discount.setText("");
                calsellprice.setText(String.valueOf(sellpricedb));
            }

        });
        discount.setOnKeyTyped(keyEvent -> {
            try {
                String[] strings = new String[]{"8"};
                if (Arrays.toString(keyEvent.getCharacter().getBytes(StandardCharsets.UTF_8)).equals(Arrays.toString(strings)) && discount.getText().isEmpty()){
                    System.out.println("back");
                    if (discount.getText().isEmpty()){
                        discountper.setText("0");
                        calsellprice.setText(String.valueOf(sellpricedb));
                    }
                }
                else {
                    calculatedunitsellprice = (sellpricedb - Integer.parseInt(discount.getText()));
                    discountper.setText(String.valueOf(calculateperdis()));
                    calsellprice.setText(String.valueOf((unitsellprice - Integer.parseInt(discount.getText())) * amount));}
                MainScreen.changetotaldata();
            }


            catch (NumberFormatException e){
                discount.setText("");
                calsellprice.setText(String.valueOf(sellpricedb));
            }
        });
        calsellprice.setText(String.valueOf((unitsellprice - Integer.parseInt(discount.getText())) * amount));
    }
}
