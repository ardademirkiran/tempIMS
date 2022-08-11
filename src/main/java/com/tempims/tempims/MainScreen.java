package com.tempims.tempims;

import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.Key;

public class MainScreen {

    public Label username;
    public TableView<Products> sellScreenTable;
    public TableColumn<Products, Integer> barcodeCollumn;
    public TableColumn<Products, String> nameCollumn;
    public TableColumn<Products, Integer> amountCollumn;
    public TableColumn<Products, Integer> kdvCollumn;
    public TableColumn<Products, TextField> percentageDiscountCollumn;
    public TableColumn<Products, TextField> discountCollumn;
    public TableColumn<Products, Label> lastPriceCollumn;
    public Button sellButton;
    public TextField barcodeField;
    public Tab sellScreenTab;
    public Tab buyScreenTab;
    public Tab returnTab;
    public Tab stockControlTab;


    public TextField productEntryLabelBarcode;
    public TextField productEntryLabelName;
    public TextField productEntryLabelTax;
    public TextField productEntryLabelBuyPrice;
    public TextField productEntryLabelSellPrice;
    public TextField productEntryLabelPiece;
    public Button productEntryLabelButton;
    public TextField productEntryLabelBrand;
    public TextField productEntryLabelPrice;
    String barcodeeverwritten = "";


    @FXML
    protected void sellbuttonclicked(){
        System.out.println("a");
    }
    @FXML
    protected void tabchanged(){
        changeactiveuser(username);
    }
    @FXML
    protected void cancelButtonClicked(){
        System.out.println("c");
    }
    @FXML
    protected void enterpressed(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ENTER){
            Products product = new Products(11,"kahve",8,50);
            amountCollumn.setCellValueFactory(productsTextFieldCellDataFeatures -> (productsTextFieldCellDataFeatures.getValue().getamount()));
            discountCollumn.setCellValueFactory(productsTextFieldCellDataFeatures -> (productsTextFieldCellDataFeatures.getValue().observableValuedis()));
            percentageDiscountCollumn.setCellValueFactory(productsTextFieldCellDataFeatures -> (productsTextFieldCellDataFeatures.getValue().observableValuedisper()));
            barcodeCollumn.setCellValueFactory(productsTextFieldCellDataFeatures -> (productsTextFieldCellDataFeatures.getValue().getbarcode()));
            nameCollumn.setCellValueFactory(productsTextFieldCellDataFeatures -> (productsTextFieldCellDataFeatures.getValue().getname()));
            kdvCollumn.setCellValueFactory(productsTextFieldCellDataFeatures -> (productsTextFieldCellDataFeatures.getValue().gettax()));
            lastPriceCollumn.setCellValueFactory(productsTextFieldCellDataFeatures -> (productsTextFieldCellDataFeatures.getValue().observableValueprice()));
            sellScreenTable.getItems().addAll(product);
        }
    }








    @FXML
    protected void productEntryLabelBarcodeKeyPressed(KeyEvent event) {
        barcodeeverwritten += event.getCharacter();
        matchedbarcodes(); // eğer bi taneyse diğer bilgileri direkt dbden doldur

    }
    protected String[] matchedbarcodes(){
        String[] barcodes = {}; // dbden bak o ana kadar yazılan kısmı eşleşenleri returle
        return barcodes;
    }
    @FXML
    protected void productEntryLabelButtonOnClicked(){
        ProductInteractions.productEntry(productEntryLabelBarcode.getText(),productEntryLabelBrand.getText(),productEntryLabelName.getText(),productEntryLabelPiece.getText(),productEntryLabelTax.getText(),productEntryLabelBuyPrice.getText(),String.valueOf(Integer.parseInt(productEntryLabelBuyPrice.getText())/Integer.parseInt(productEntryLabelPiece.getText())),productEntryLabelSellPrice.getText());

    }
    protected void changeactiveuser(Label label){
        label.setText("Kullanıcı: " + Session.username);
    }
}

