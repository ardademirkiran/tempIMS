package com.tempims.tempims;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import java.security.Key;

public class MainScreen {

    public Label username;
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


    public TextField productEntryLabelBarcode;
    public TextField productEntryLabelName;
    public TextField productEntryLabelExplanation;
    public TextField productEntryLabelTax;
    public TextField productEntryLabelDiscount;
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
        System.out.println("b");
    }
    @FXML
    protected void cancelButtonClicked(){
        System.out.println("c");
    }


    protected void productEntryLabelBarcodeKeyPressed(KeyEvent event) {
        barcodeeverwritten += event.getCharacter();
        matchedbarcodes(); // eğer bi taneyse diğer bilgileri direkt dbden doldur

    }
    protected String[] matchedbarcodes(){
        String[] barcodes = {}; // dbden bak o ana kadar yazılan kısmı eşleşenleri returle
        return barcodes;
    }
    protected void productEntryLabelButtonOnClicked(){
        // eğer barkod bulunamadıysa yeni ürün kaydı, bulunduysa stoğa ekle
    }
}

