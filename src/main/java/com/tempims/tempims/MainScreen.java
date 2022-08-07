package com.tempims.tempims;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class MainScreen {

    public TableView<Products> sellScreenTable;
    public TableColumn<Products,Long> barcodeCollumn;
    public TableColumn<Products, String> nameCollumn;
    public TableColumn<Products, Integer> amountCollumn;
    public TableColumn<Products, Double> priceCollumn;
    public TableColumn<Products, Double> kdvCollumn;
    public TableColumn<Products, Integer> percentageDiscountCollumn;
    public TableColumn<Products, Double> discountCollumn;
    public TableColumn<Products, Double> lastPriceCollumn;
    public Button sellButton;
    public TextField barcodeField;
    public Tab sellScreenTab;
    public Tab buyScreenTab;
    public Tab returnTab;
    public Tab stockControlTab;


    @FXML
    protected void sellbuttonclicked(){
        System.out.println("a");
    }
    @FXML
    protected void tabchanged(){
        System.out.println("b");
    }
}
