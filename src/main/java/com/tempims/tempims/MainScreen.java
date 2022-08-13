package com.tempims.tempims;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.AccessibleRole;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.Objects;

public class MainScreen {

    public static TableColumn<Products,Label> lastPriceCollumnstatic;
    public static TableView<Products> sellScreenTablestatic;
    public static TableColumn<Products, TextField> discountCollumnstatic;
    public static TableColumn<Products, Integer> kdvCollumnstatic;
    public static Label totalDiscountLabelstatic;
    public static Label totalPriceLabelstatic;
    public static Label totalTaxLabelstatic;




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
    public Label totalDiscountLabel;
    public Label totalPriceLabel;
    public Label totalTaxLabel;
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
        init();
        setcontextmenu();
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
            refreshtabledata();
            if (add) {
                sellScreenTable.getItems().addAll(productdb);
            }
            sellScreenTable.refresh();
            init();
            changetotaldata();
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
    public static void changetotaldata(){
        int totalprice = 0;
        int totaldiscount = 0;
        double totaltax = 0.0;
        totalPriceLabelstatic.setText(String.valueOf(0));
        totalDiscountLabelstatic.setText(String.valueOf(0));
        totalTaxLabelstatic.setText(String.valueOf(0));
        for (Products item : sellScreenTablestatic.getItems()) {
            totalprice += Integer.parseInt(lastPriceCollumnstatic.getCellObservableValue(item).getValue().getText());
            totaldiscount += Integer.parseInt(discountCollumnstatic.getCellObservableValue(item).getValue().getText())* item.amount;
            totaltax += Double.parseDouble(String.valueOf(kdvCollumnstatic.getCellObservableValue(item).getValue()))*Double.parseDouble(lastPriceCollumnstatic.getCellObservableValue(item).getValue().getText())/100;
        }
        totalPriceLabelstatic.setText(String.valueOf(totalprice));
        totalDiscountLabelstatic.setText(String.valueOf(totaldiscount));
        totalTaxLabelstatic.setText(String.valueOf(totaltax));
    }
    public void init(){
        lastPriceCollumnstatic = lastPriceCollumn;
        sellScreenTablestatic = sellScreenTable;
        discountCollumnstatic = discountCollumn;
        kdvCollumnstatic = kdvCollumn;
        totalDiscountLabelstatic = totalDiscountLabel;
        totalPriceLabelstatic = totalPriceLabel;
        totalTaxLabelstatic = totalTaxLabel;
    }
    public void refreshtabledata(){
        amountCollumn.setCellValueFactory(productsTextFieldCellDataFeatures -> (productsTextFieldCellDataFeatures.getValue().getamount()));
        discountCollumn.setCellValueFactory(productsTextFieldCellDataFeatures -> (productsTextFieldCellDataFeatures.getValue().observableValuedis()));
        percentageDiscountCollumn.setCellValueFactory(productsTextFieldCellDataFeatures -> (productsTextFieldCellDataFeatures.getValue().observableValuedisper()));
        barcodeCollumn.setCellValueFactory(productsTextFieldCellDataFeatures -> (productsTextFieldCellDataFeatures.getValue().getbarcode()));
        nameCollumn.setCellValueFactory(productsTextFieldCellDataFeatures -> (productsTextFieldCellDataFeatures.getValue().getname()));
        kdvCollumn.setCellValueFactory(productsTextFieldCellDataFeatures -> (productsTextFieldCellDataFeatures.getValue().gettax()));
        lastPriceCollumn.setCellValueFactory(productsTextFieldCellDataFeatures -> (productsTextFieldCellDataFeatures.getValue().observableValueprice()));
        sellScreenTable.refresh();
    }
    public void setcontextmenu(){
        ContextMenu contextMenu = new ContextMenu();
        MenuItem azalt = new MenuItem("Azalt");
        azalt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (sellScreenTable.getSelectionModel().getSelectedItem().amount == 1){
                    sellScreenTable.getItems().remove(sellScreenTable.getSelectionModel().getSelectedItem());
                }else{
                    sellScreenTable.getSelectionModel().getSelectedItem().amount--;
                }

                refreshtabledata();
                init();
                changetotaldata();

            }
        });MenuItem sil = new MenuItem("Sil");
        sil.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                sellScreenTable.getItems().remove(sellScreenTable.getSelectionModel().getSelectedItem());
                if (sellScreenTable.getSelectionModel().getSelectedItem()!=null){
                    sellScreenTable.getSelectionModel().getSelectedItem().init();
                }
                refreshtabledata();
                init();
                changetotaldata();
            }
        });
        contextMenu.getItems().add(azalt);
        contextMenu.getItems().add(sil);
        sellScreenTable.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent contextMenuEvent) {
                final int[] i = {0};
                if (sellScreenTable.getSelectionModel().getSelectedItem()!=null){
                    contextMenu.show(sellScreenTable,contextMenuEvent.getScreenX(),contextMenuEvent.getScreenY());
                }sellScreenTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (i[0] >0)
                        contextMenu.hide();else{
                            i[0]++;
                        }
                    }
                });
            }
        });
    }
}

