package com.tempims.tempims;

import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.Locale;

public class Products {
    TextField discountPercentage = new TextField("0");
    TextField discount = new TextField("0");
    Label sellPriceLabel = new Label("");
    int amount, tax;
    Double unitSellPrice;
    Double calculatedUnitSellPrice;
    Double sellPriceDB;
    String name, barcode;

    Products(String barcode, String name, String brand, Integer tax, Double sellpricedb) {
        this.name = brand + " " + name;
        this.barcode = barcode;
        this.unitSellPrice = sellpricedb;
        this.sellPriceDB = sellpricedb;
        this.calculatedUnitSellPrice = sellpricedb;
        this.tax = tax;
        this.amount = 1;
        this.sellPriceLabel.setText(String.valueOf(sellpricedb));
        init();
        discount.setPromptText("0");
        discountPercentage.setPromptText("0");
        discount.setStyle("-fx-font-size: 25");
        discountPercentage.setStyle("-fx-font-size: 25");
        sellPriceLabel.setStyle("-fx-font-size: 25");
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
        return ((Double.parseDouble(this.discount.getText()) * 100) / this.unitSellPrice);
    }

    private Double calculateDiscount() {
        return (Double.parseDouble(this.discountPercentage.getText()) * this.unitSellPrice) / 100;
    }

    protected void discountPercentageController(){
            try{
                Double discountNumber = calculateDiscount();
                calculatedUnitSellPrice = (sellPriceDB - discountNumber);
                discount.setText(String.valueOf(discountNumber));
                sellPriceLabel.setText(String.format(Locale.ROOT, "%.2f",(unitSellPrice - Double.parseDouble(discount.getText())) * amount));
                MainScreen.setLabelDisplay();
            } catch (NumberFormatException exception){
                discount.setText("0.00");
                sellPriceLabel.setText(String.format(Locale.ROOT, "%.2f",sellPriceDB * amount));
            }
        }

        protected void discountController(){
                try {
                    calculatedUnitSellPrice = (sellPriceDB - Double.parseDouble(discount.getText()));
                    discountPercentage.setText(String.valueOf(calculateDiscountPercentage()));
                    sellPriceLabel.setText(String.format(Locale.ROOT, "%.2f", (unitSellPrice - Double.parseDouble(discount.getText())) * amount));
                    MainScreen.setLabelDisplay();
                } catch (NumberFormatException exception){
                    sellPriceLabel.setText(String.format(Locale.ROOT, "%.2f",sellPriceDB * amount));
                }
        }


    public void init() {
        MainScreen.inputController(discount);
        MainScreen.inputController(discountPercentage);
        discountPercentage.setOnKeyTyped(keyEvent -> {
            discountPercentageController();
        });
        discount.setOnKeyTyped(keyEvent -> {
            discountController();
        });
        sellPriceLabel.setText(String.valueOf((unitSellPrice - Double.parseDouble(discount.getText())) * amount));

       // discountPercentage.setOnMouseReleased(e -> {if(discountPercentage.getText().isEmpty()){discountPercentage.setText("0.31");}});
        //discount.setOnMouseReleased(e -> {if(discount.getText().isEmpty()){discount.setText("0.31");}});

    }
}
