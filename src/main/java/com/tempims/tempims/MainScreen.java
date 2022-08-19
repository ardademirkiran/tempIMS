package com.tempims.tempims;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class MainScreen {

    public static TableColumn<Products,Label> lastPriceCollumnstatic;
    public static TableView<Products> sellScreenTablestatic;
    public static TableColumn<Products, TextField> discountCollumnstatic;
    public static TableColumn<Products, Integer> kdvCollumnstatic;
    public static Label totalDiscountLabelstatic;
    public static Label totalPriceLabelstatic;
    public static Label totalTaxLabelstatic;
    public static Label subTotalLabelstatic;
    public AnchorPane statisticAnchorPane;


    CategoryAxis xAxis = new CategoryAxis();
    NumberAxis yAxis = new NumberAxis("TL",0,50,5);
    public StackedBarChart<String,Number> stackedBarChart = new StackedBarChart<>(xAxis,yAxis);
    public PieChart pieChart;

    boolean eventhandleradded = false;
    public Tab openedtab;

    public Label username;
    public TableView<Products> sellScreenTable;
    public TableColumn<Products, String> barcodeCollumn;
    public TableColumn<Products, String> nameCollumn;
    public TableColumn<Products, Integer> amountCollumn;
    public TableColumn<Products, Integer> kdvCollumn;
    public TableColumn<Products, TextField> percentageDiscountCollumn;
    public TableColumn<Products, TextField> discountCollumn;
    public TableColumn<Products, Label> lastPriceCollumn;
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
    public Label subTotalLabel;
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
        if (!eventhandleradded){
        Main.globalStage.addEventHandler(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                sellScreenKeyTyped(keyEvent);
            }
        });
        eventhandleradded = true;
        }
        openedtab = tabpane.getSelectionModel().getSelectedItem();
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
        int subtotal = 0;
        totalPriceLabelstatic.setText(String.valueOf(0));
        totalDiscountLabelstatic.setText(String.valueOf(0));
        totalTaxLabelstatic.setText(String.valueOf(0));
        subTotalLabelstatic.setText(String.valueOf(0));

        for (Products item : sellScreenTablestatic.getItems()) {
            totalprice += Integer.parseInt(lastPriceCollumnstatic.getCellObservableValue(item).getValue().getText());
            totaldiscount += Integer.parseInt(discountCollumnstatic.getCellObservableValue(item).getValue().getText())* item.amount;
            subtotal += Integer.parseInt(lastPriceCollumnstatic.getCellObservableValue(item).getValue().getText())/(1 + kdvCollumnstatic.getCellObservableValue(item).getValue()/100.0);
        }
        totalPriceLabelstatic.setText(String.valueOf(totalprice));
        subTotalLabelstatic.setText(String.valueOf(subtotal));
        totalDiscountLabelstatic.setText(String.valueOf(totaldiscount));
        totalTaxLabelstatic.setText(String.valueOf(totalprice-subtotal));
    }
    public void init(){
        lastPriceCollumnstatic = lastPriceCollumn;
        sellScreenTablestatic = sellScreenTable;
        discountCollumnstatic = discountCollumn;
        kdvCollumnstatic = kdvCollumn;
        totalDiscountLabelstatic = totalDiscountLabel;
        totalPriceLabelstatic = totalPriceLabel;
        totalTaxLabelstatic = totalTaxLabel;
        subTotalLabelstatic = subTotalLabel;
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
    @FXML
    public void sellScreenKeyTyped(KeyEvent keyEvent){
        if (tabpane.getSelectionModel().getSelectedItem().getId().equals(sellScreenTab.getId())){
            byte[] enter = {13};
            byte[] back = {8};
            if (Arrays.toString(keyEvent.getCharacter().getBytes(StandardCharsets.UTF_8)).equals(Arrays.toString(enter))) {
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
                barcodeField.setText("");
            }
            else if(Arrays.toString(keyEvent.getCharacter().getBytes(StandardCharsets.UTF_8)).equals(Arrays.toString(back))){
                String prebarcode = barcodeField.getText();
                prebarcode = prebarcode.substring(0,prebarcode.length()-1);
                barcodeField.setText(prebarcode);
            }else{
                String prebarcode = barcodeField.getText();
                barcodeField.setText(prebarcode+keyEvent.getCharacter());
            }
        }
    }
    @FXML
    public void statisticsTabOpened(){
        HashMap<String,Number> pieChartData = new HashMap<>(); // Name and total price of that product
        pieChartData.put("Kalem",100);
        pieChartData.put("Silgi",200);
        pieChartData.put("Portakal",300);
        pieChartData.put("Limon",400);
        setPieChart(pieChartData);


        HashMap<LocalDate,Number> barChartData = new HashMap<>(); // date and total price of that day
        barChartData.put(LocalDate.of(2022,8,20),50);
        barChartData.put(LocalDate.of(2022,8,21),100);
        barChartData.put(LocalDate.of(2022,8,22),80);
        barChartData.put(LocalDate.of(2022,8,23),200);
        setBarChart(barChartData);


    }
    public void setPieChart(HashMap<String,Number> nameAndPrice){
        for (String name: nameAndPrice.keySet()) {
            pieChart.getData().add(new PieChart.Data(name, (Integer)nameAndPrice.get(name)+0.0));
        }Label pieInfo = new Label("");
        pieInfo.setTextFill(Color.BLACK);
        DoubleBinding total = Bindings.createDoubleBinding(() ->
                (Double) pieChart.getData().stream().mapToDouble(PieChart.Data::getPieValue).sum(), pieChart.getData());
        for (final PieChart.Data data : pieChart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_MOVED,
                    e -> {
                        pieInfo.setVisible(true);
                        pieInfo.setTranslateX(e.getSceneX()+5);
                        pieInfo.setTranslateY(e.getSceneY()-90);
                        String text = String.format("%.1f%%", 100*data.getPieValue()/total.get()) ;
                        pieInfo.setText(text+"\n"+(int)data.getPieValue()+"TL");
                    }
            );data.getNode().addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> {
                pieInfo.setVisible(false);
            });
        }statisticAnchorPane.getChildren().add(pieInfo);

    }

    public void setBarChart(HashMap<LocalDate,Number> dateAndPriceList){
        var series = new StackedBarChart.Series<String,Number>();
        for (LocalDate date : dateAndPriceList.keySet()) {
            series.getData().add(new XYChart.Data<>(date.toString(),dateAndPriceList.get(date)));
        }series.setName("Tarih");
        stackedBarChart.getData().add(series);
        stackedBarChart.setAnimated(false);
        Label barinfo = new Label("");
        for (final StackedBarChart.Data data: series.getData()){
            data.getNode().addEventHandler(MouseEvent.MOUSE_MOVED, mouseEvent -> {
                barinfo.setVisible(true);
                barinfo.setTranslateX(mouseEvent.getSceneX());
                barinfo.setTranslateY(mouseEvent.getSceneY()-80);
                barinfo.setText(data.getYValue().toString());
            });
            data.getNode().addEventHandler(MouseEvent.MOUSE_EXITED,mouseEvent -> {
                barinfo.setVisible(false);
            });

        }barinfo.setTextFill(Color.BLACK);

        statisticAnchorPane.getChildren().add(barinfo);
    }

}

