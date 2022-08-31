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
    TextField discountPercentage = new TextField("0");
    TextField discount = new TextField("0");
    Label sellPriceLabel = new Label("");
    int amount, tax;
    Double unitSellPrice;
    Double calculatedUnitSellPrice;
    Double sellPriceDB;
    String name, barcode;
    Products(String barcode, String name, Integer tax, Double sellpricedb) {
        discount.setPromptText("0");
        discountPercentage.setPromptText("0");
        this.name = name;
        this.barcode = barcode;
        this.unitSellPrice = sellpricedb;
        this.sellPriceDB = sellpricedb;
        this.calculatedUnitSellPrice = sellpricedb;
        this.tax = tax;
        this.amount = 1;
        this.sellPriceLabel.setText(String.valueOf(sellpricedb));
        discount.setStyle("-fx-font-size: 25");
        discountPercentage.setStyle("-fx-font-size: 25");
        sellPriceLabel.setStyle("-fx-font-size: 25");
        init();
    }

    public ObservableValue<TextField> getObservableDiscountPercentage() {
        return getObservableValue(discountPercentage);
    }

    private ObservableValue<TextField> getObservableValue(TextField a) {
        return new ObservableValue<>() {
            @Override
            public void addListener(ChangeListener<? super TextField> changeListener) {

            }

            @Override
            public void removeListener(ChangeListener<? super TextField> changeListener) {

            }

            @Override
            public TextField getValue() {
                return a;
            }

            @Override
            public void addListener(InvalidationListener invalidationListener) {

            }

            @Override
            public void removeListener(InvalidationListener invalidationListener) {

            }
        };
    }

    public ObservableValue<TextField> getObservableDiscount() {
        return getObservableValue(discount);
    }

    public ObservableValue<Label> getObservablePrice() {
        return new ObservableValue<>() {
            @Override
            public void addListener(ChangeListener<? super Label> changeListener) {

            }

            @Override
            public void removeListener(ChangeListener<? super Label> changeListener) {

            }

            @Override
            public Label getValue() {
                return sellPriceLabel;
            }

            @Override
            public void addListener(InvalidationListener invalidationListener) {

            }

            @Override
            public void removeListener(InvalidationListener invalidationListener) {

            }
        };
    }

    public ObservableValue<String> getBarcode() {
        return new SimpleStringProperty(barcode);
    }

    public StringProperty getName() {
        return new SimpleStringProperty(name);
    }

    public ObservableValue<Integer> getTax() {
        return new SimpleIntegerProperty(tax).asObject();
    }

    public ObservableValue<Integer> getAmount() {
        return new SimpleIntegerProperty(amount).asObject();
    }

    private Double calculateDiscountPercentage() {
        if (this.discount.getText().isEmpty()){
            return 0.0;
        }
        return ((Double.parseDouble(this.discount.getText()) * 100) / this.unitSellPrice);
    }

    private Double calculateDiscount() {
        if (this.discountPercentage.getText().isEmpty()){
            return 0.0;
        }
        return (Double.parseDouble(this.discountPercentage.getText()) * this.unitSellPrice) / 100;
    }
    public void init(){
        discountPercentage.setOnKeyTyped(keyEvent -> {
            try {
                calculatedUnitSellPrice = (sellPriceDB - calculateDiscount());
                discount.setText(String.valueOf(calculateDiscount()));
                sellPriceLabel.setText(String.valueOf((unitSellPrice - Double.parseDouble(discount.getText())) * amount));
                MainScreen.setLabelDisplay();
            }catch (NumberFormatException e){
                discount.setText("");
                sellPriceLabel.setText(String.valueOf(sellPriceDB));
            }

        });
        discount.setOnKeyTyped(keyEvent -> {
            try {
                String[] strings = new String[]{"8"};
                if (Arrays.toString(keyEvent.getCharacter().getBytes(StandardCharsets.UTF_8)).equals(Arrays.toString(strings)) && discount.getText().isEmpty()){
                    if (discount.getText().isEmpty()){
                        discountPercentage.setText("0");
                        sellPriceLabel.setText(String.valueOf(sellPriceDB));
                    }
                }
                else {
                    calculatedUnitSellPrice = (sellPriceDB - Double.parseDouble(discount.getText()));
                    discountPercentage.setText(String.valueOf(calculateDiscountPercentage()));
                    sellPriceLabel.setText(String.valueOf((unitSellPrice - Double.parseDouble(discount.getText())) * amount));}
                MainScreen.setLabelDisplay();
            }


            catch (NumberFormatException e){
                discount.setText("");
                sellPriceLabel.setText(String.valueOf(sellPriceDB));
            }
        });
        sellPriceLabel.setText(String.valueOf((unitSellPrice - Double.parseDouble(discount.getText())) * amount));
    }
}
