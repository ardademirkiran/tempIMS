package com.tempims.tempims;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.util.Objects;

public class MainScreen {

    public Label username;
    public TableView<Products> sellScreenTable;
    public TableColumn<Products, String> barcodeCollumn;
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
    public boolean tabholding;
    String barcodeeverwritten = "";

    @FXML
    protected void sellButtonClicked() {
        ObservableList<Products> productslist = sellScreenTable.getItems();
        for (Products product : productslist) {
            ProductInteractions.sellProduct(product.barcode, product.amount);
        }
    }

    @FXML
    protected void tabChanged() {
        changeActiveUser(username);
    }

    @FXML
    protected void cancelButtonClicked() {
        sellScreenTable.getItems().removeAll(sellScreenTable.getItems());
    }

    @FXML
    protected void enterPressed(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            boolean add = false;
            boolean find = false;
            Products productdb = null;
            if (sellScreenTable.getItems().size() > 0) {
                for (Products pro : sellScreenTable.getItems()) {
                    if (Objects.equals(pro.barcode, barcodeField.getText())) {
                        pro.amount++;
                        pro.calsellprice.setText(String.valueOf((pro.unitsellprice - Integer.parseInt(pro.discount.getText())) * pro.amount));
                        find = true;
                    }
                }
            }
            if (!find) {
                productdb = ProductInteractions.getProduct(barcodeField.getText());
                add = true;
            }

            amountCollumn.setCellValueFactory(productsTextFieldCellDataFeatures -> (productsTextFieldCellDataFeatures.getValue().getamount()));
            discountCollumn.setCellValueFactory(productsTextFieldCellDataFeatures -> (productsTextFieldCellDataFeatures.getValue().observableValuedis()));
            percentageDiscountCollumn.setCellValueFactory(productsTextFieldCellDataFeatures -> (productsTextFieldCellDataFeatures.getValue().observableValuedisper()));
            barcodeCollumn.setCellValueFactory(productsTextFieldCellDataFeatures -> (productsTextFieldCellDataFeatures.getValue().getbarcode()));
            nameCollumn.setCellValueFactory(productsTextFieldCellDataFeatures -> (productsTextFieldCellDataFeatures.getValue().getname()));
            kdvCollumn.setCellValueFactory(productsTextFieldCellDataFeatures -> (productsTextFieldCellDataFeatures.getValue().gettax()));
            lastPriceCollumn.setCellValueFactory(productsTextFieldCellDataFeatures -> (productsTextFieldCellDataFeatures.getValue().observableValueprice()));
            if (add) {
                sellScreenTable.getItems().addAll(productdb);
            }
            sellScreenTable.refresh();
        }
    }


    @FXML
    protected void productEntryLabelBarcodeKeyPressed(KeyEvent event) {
        barcodeeverwritten += event.getCharacter();
        matchedbarcodes(); // eğer bi taneyse diğer bilgileri direkt dbden doldur

    }

    protected String[] matchedbarcodes() {
        String[] barcodes = {}; // dbden bak o ana kadar yazılan kısmı eşleşenleri returle
        return barcodes;
    }

    @FXML
    protected void productEntryLabelButtonOnClicked() {
        ProductInteractions.productEntry(productEntryLabelBarcode.getText(), productEntryLabelBrand.getText(), productEntryLabelName.getText(), productEntryLabelPiece.getText(), productEntryLabelTax.getText(), productEntryLabelBuyPrice.getText(), String.valueOf(Integer.parseInt(productEntryLabelBuyPrice.getText()) / Integer.parseInt(productEntryLabelPiece.getText())), productEntryLabelSellPrice.getText());
    }

    @FXML
    protected void buyPriceKeyPressed(){
        productEntryLabelPrice.setText(String.valueOf(Integer.parseInt(productEntryLabelBuyPrice.getText())/Integer.parseInt(productEntryLabelPiece.getText())));
    }
    protected void changeActiveUser(Label label) {
        label.setText("Kullanıcı: " + Session.username);
    }
}

