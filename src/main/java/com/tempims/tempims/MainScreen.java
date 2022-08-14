package com.tempims.tempims;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
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
    public TabPane tabpane;
    String barcodeeverwritten = "";


    public TableView<AllProducts> stockTable;
    public TableColumn<AllProducts,String> stockCollumnBarcode;
    public TableColumn<AllProducts,String> stockCollumnBrand;
    public TableColumn<AllProducts,String> stockCollumnName;
    public TableColumn<AllProducts,Integer> stockCollumnAmont;
    public TableColumn<AllProducts,Integer> stockCollumnTax;
    public TableColumn<AllProducts,Integer> stockCollumnUnitBuy;
    public TableColumn<AllProducts,Integer> stockCollumnUnitSell;
    public TableColumn<AllProducts,Integer> stockCollumnExpectedProfit;




    @FXML
    protected void sellButtonClicked() throws IOException {
        ObservableList<Products> productslist = sellScreenTable.getItems();
        ProcessLogs.recordSalesProcess(productslist, Double.parseDouble(totalPriceLabel.getText()));
        for (Products product : productslist) {
            ProductInteractions.sellProduct(product.barcode, product.amount);
        }
    }

    @FXML
    protected void tabChanged() {
        changeActiveUser(username);
        init();
        setcontextmenu();

        ContextMenu contextMenu = setcontextmenu();
        sellScreenTable.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent contextMenuEvent) {
                final int[] i = {0};
                Node source = contextMenuEvent.getPickResult().getIntersectedNode();

                while (source != null && !(source instanceof TableRow)) {
                    source = source.getParent();
                }

                if (source == null || ((TableRow<?>) source).isEmpty()) {
                    sellScreenTable.getSelectionModel().clearSelection();

                }
                if (sellScreenTable.getSelectionModel().getSelectedItem()!=null & !(source == null || ((TableRow<?>) source).isEmpty())){
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
    protected void productEntryLabelButtonOnClicked() throws IOException {
        ProcessLogs.recordStockEntryProcess(productEntryLabelBarcode.getText(), productEntryLabelName.getText(), productEntryLabelPiece.getText());
        ProductInteractions.productEntry(productEntryLabelBarcode.getText(), productEntryLabelBrand.getText(), productEntryLabelName.getText(), productEntryLabelPiece.getText(), productEntryLabelTax.getText(), productEntryLabelBuyPrice.getText(), String.valueOf(Integer.parseInt(productEntryLabelBuyPrice.getText()) / Integer.parseInt(productEntryLabelPiece.getText())), productEntryLabelSellPrice.getText());
    }

    @FXML
    protected void buyPriceKeyPressed(){
        try {
            productEntryLabelPrice.setText(String.valueOf(Integer.parseInt(productEntryLabelBuyPrice.getText())/Integer.parseInt(productEntryLabelPiece.getText())));
        }catch (NumberFormatException e){
            productEntryLabelPrice.setText("0");
        }
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
        for (Products pro:sellScreenTable.getItems()) {
            pro.init();
        }
        amountCollumn.setCellValueFactory(productsTextFieldCellDataFeatures -> (productsTextFieldCellDataFeatures.getValue().getamount()));
        discountCollumn.setCellValueFactory(productsTextFieldCellDataFeatures -> (productsTextFieldCellDataFeatures.getValue().observableValuedis()));
        percentageDiscountCollumn.setCellValueFactory(productsTextFieldCellDataFeatures -> (productsTextFieldCellDataFeatures.getValue().observableValuedisper()));
        barcodeCollumn.setCellValueFactory(productsTextFieldCellDataFeatures -> (productsTextFieldCellDataFeatures.getValue().getbarcode()));
        nameCollumn.setCellValueFactory(productsTextFieldCellDataFeatures -> (productsTextFieldCellDataFeatures.getValue().getname()));
        kdvCollumn.setCellValueFactory(productsTextFieldCellDataFeatures -> (productsTextFieldCellDataFeatures.getValue().gettax()));
        lastPriceCollumn.setCellValueFactory(productsTextFieldCellDataFeatures -> (productsTextFieldCellDataFeatures.getValue().observableValueprice()));
        sellScreenTable.refresh();
    }
    public ContextMenu setcontextmenu(){
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
        return contextMenu;
    }
    @FXML
    public void onstockcontrolopened(){
        System.out.println("aaaa");
        ProductInteractions.createAllProducts();
        stockCollumnBarcode.setCellValueFactory(allProductsStringCellDataFeatures -> allProductsStringCellDataFeatures.getValue().getbarcode());
        stockCollumnAmont.setCellValueFactory(allProductsStringCellDataFeatures -> allProductsStringCellDataFeatures.getValue().getamont());
        stockCollumnBrand.setCellValueFactory(allProductsStringCellDataFeatures -> allProductsStringCellDataFeatures.getValue().getbrand());
        stockCollumnName.setCellValueFactory(allProductsStringCellDataFeatures -> allProductsStringCellDataFeatures.getValue().getname());
        stockCollumnTax.setCellValueFactory(allProductsStringCellDataFeatures -> allProductsStringCellDataFeatures.getValue().gettax());
        stockCollumnUnitBuy.setCellValueFactory(allProductsStringCellDataFeatures -> allProductsStringCellDataFeatures.getValue().getunitbuy());
        stockCollumnUnitSell.setCellValueFactory(allProductsStringCellDataFeatures -> allProductsStringCellDataFeatures.getValue().getunitsell());
        stockCollumnExpectedProfit.setCellValueFactory(allProductsStringCellDataFeatures -> allProductsStringCellDataFeatures.getValue().getprofit());
        stockTable.getItems().add(new AllProducts("a","b","c","2","3","10","20"));
    }
    @FXML
    public void stockControlClicked(MouseEvent mouseEvent){
        if (mouseEvent.getClickCount()==2 & stockTable.getSelectionModel().getSelectedItem()!=null){
            tabpane.getSelectionModel().select(buyScreenTab);
            AllProducts selectedproduct = stockTable.getSelectionModel().getSelectedItem();
            productEntryLabelBarcode.setText(selectedproduct.getbarcode().getValue());
            productEntryLabelPrice.setText(String.valueOf(selectedproduct.getunitbuy().getValue()));
            productEntryLabelPiece.setText(String.valueOf(selectedproduct.getamont().getValue()));
            productEntryLabelBuyPrice.setText(String.valueOf(selectedproduct.getunitbuy().getValue()*selectedproduct.getamont().getValue()));
            productEntryLabelBrand.setText(selectedproduct.getbrand().getValue());
            productEntryLabelName.setText(selectedproduct.getname().getValue());
            productEntryLabelTax.setText(String.valueOf(selectedproduct.gettax().getValue()));
            productEntryLabelSellPrice.setText(String.valueOf(selectedproduct.getunitsell().getValue()));
        }
    }
}

