package com.tempims.tempims;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

public class MainScreen {

    public static TableColumn<Products, Label> lastPriceCollumnstatic;
    public static TableView<Products> sellScreenTablestatic;
    public static TableColumn<Products, TextField> discountCollumnstatic;
    public static TableColumn<Products, Integer> kdvCollumnstatic;
    public static Label totalDiscountLabelstatic;
    public static Label totalPriceLabelstatic;
    public static Label totalTaxLabelstatic;
    public static Label subTotalLabelstatic;
    public AnchorPane statisticAnchorPane;
    public Tab statisticsTab;
    public PieChart pieChart;
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
    public TableView<AllProducts> stockTable;
    public TableColumn<AllProducts, String> stockCollumnBarcode;
    public TableColumn<AllProducts, String> stockCollumnBrand;
    public TableColumn<AllProducts, String> stockCollumnName;
    public TableColumn<AllProducts, Integer> stockCollumnAmont;
    public TableColumn<AllProducts, Integer> stockCollumnTax;
    public TableColumn<AllProducts, Double> stockCollumnUnitBuy;
    public TableColumn<AllProducts, Double> stockCollumnUnitSell;
    public TableColumn<AllProducts, Double> stockCollumnExpectedProfit;
    CategoryAxis xAxis = new CategoryAxis();
    NumberAxis yAxis = new NumberAxis("TL", 0, 50, 5);
    public StackedBarChart<String, Number> stackedBarChart = new StackedBarChart<>(xAxis, yAxis);
    boolean eventhandleradded = false;
    String barcodeeverwritten = "";
    ContextMenu productEntryBarcodeContextMenu = new ContextMenu();

    public static void changetotaldata() {
        double totalprice = 0;
        double totaldiscount = 0;
        double subtotal = 0;
        totalPriceLabelstatic.setText(String.valueOf(0));
        totalDiscountLabelstatic.setText(String.valueOf(0));
        totalTaxLabelstatic.setText(String.valueOf(0));
        subTotalLabelstatic.setText(String.valueOf(0));

        for (Products item : sellScreenTablestatic.getItems()) {
            totalprice += Double.parseDouble(lastPriceCollumnstatic.getCellObservableValue(item).getValue().getText());
            totaldiscount += Double.parseDouble(discountCollumnstatic.getCellObservableValue(item).getValue().getText()) * item.amount;
            subtotal += Double.parseDouble(lastPriceCollumnstatic.getCellObservableValue(item).getValue().getText()) / (1 + kdvCollumnstatic.getCellObservableValue(item).getValue() / 100.0);
        }
        totalPriceLabelstatic.setText(String.valueOf(totalprice));
        subTotalLabelstatic.setText(String.valueOf(subtotal));
        totalDiscountLabelstatic.setText(String.valueOf(totaldiscount));
        totalTaxLabelstatic.setText(String.valueOf(totalprice - subtotal));
    }

    @FXML
    protected void sellButtonClicked() throws IOException {
        ObservableList<Products> productslist = sellScreenTable.getItems();
        ProcessLogs.recordSalesProcess(productslist, Double.parseDouble(totalPriceLabel.getText()));
        Stats.updateProfit(productslist);
        for (Products product : productslist) {
            ProductInteractions.sellProduct(product.barcode, product.amount);
        }
        cancelButtonClicked();
    }

    @FXML
    protected void tabChanged() {
        if (!eventhandleradded) {
            Main.globalStage.addEventHandler(KeyEvent.KEY_TYPED, this::sellScreenKeyTyped);
            eventhandleradded = true;
        }
        openedtab = tabpane.getSelectionModel().getSelectedItem();
        changeActiveUser(username);
        init();
        setcontextmenu();

        ContextMenu contextMenu = setcontextmenu();
        sellScreenTable.setOnContextMenuRequested(contextMenuEvent -> {
            final int[] i = {0};
            Node source = contextMenuEvent.getPickResult().getIntersectedNode();

            while (source != null && !(source instanceof TableRow)) {
                source = source.getParent();
            }

            if (source == null || ((TableRow<?>) source).isEmpty()) {
                sellScreenTable.getSelectionModel().clearSelection();

            }
            if (sellScreenTable.getSelectionModel().getSelectedItem() != null & !(source == null || ((TableRow<?>) source).isEmpty())) {
                contextMenu.show(sellScreenTable, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());
            }
            sellScreenTable.setOnMouseClicked(event -> {
                if (i[0] > 0) contextMenu.hide();
                else {
                    i[0]++;
                }
            });
        });
    }

    @FXML
    protected void cancelButtonClicked() {
        sellScreenTable.getItems().removeAll(sellScreenTable.getItems());
        changetotaldata();
    }

    @FXML
    protected void productEntryLabelBarcodeKeyPressed(KeyEvent event) {
        byte[] enter = {13};
        if (!Arrays.toString(event.getCharacter().getBytes(StandardCharsets.UTF_8)).equals(Arrays.toString(enter))) {
            productEntryBarcodeContextMenu.hide();
            productEntryBarcodeContextMenu.getItems().clear();
            barcodeeverwritten = productEntryLabelBarcode.getText();
            setBarcodeContextMenu(matchedbarcodes(barcodeeverwritten));
        }
    }

    protected void setBarcodeContextMenu(ArrayList<String> matchedbarcodeslist) {
        if (matchedbarcodeslist.size() < 6 && matchedbarcodeslist.size() > 0) {
            for (String barcode : matchedbarcodeslist) {
                Label label = new Label(barcode);
                label.setPrefWidth(productEntryLabelBarcode.getWidth()-13);
                CustomMenuItem menuItem = new CustomMenuItem(label, true);
                menuItem.setOnAction(actionEvent -> {
                    productEntryLabelBarcode.setText(label.getText());
                    final AllProducts[] products = new AllProducts[1];
                    ProductInteractions.createAllProducts().forEach(allProducts -> products[0] = (allProducts.getbarcode().getValue().equals(label.getText()) ? allProducts : products[0]));
                    productEntryLabelBarcode.setText(products[0].getbarcode().getValue());
                    productEntryLabelPrice.setText(String.valueOf(products[0].getunitbuy().getValue()));
                    productEntryLabelBrand.setText(products[0].getbrand().getValue());
                    productEntryLabelName.setText(products[0].getname().getValue());
                    productEntryLabelTax.setText(String.valueOf(products[0].gettax().getValue()));
                    productEntryLabelSellPrice.setText(String.valueOf(products[0].getunitsell().getValue()));
                    productEntryBarcodeContextMenu.hide();
                });
                productEntryBarcodeContextMenu.getItems().add(menuItem);
            }
            productEntryBarcodeContextMenu.show(productEntryLabelBarcode, Side.BOTTOM, 0, 0);
        }
    }

    protected ArrayList<String> matchedbarcodes(String barcodeeverwritten) {
        ArrayList<String> barcodes = new ArrayList<>();
        int size = barcodeeverwritten.length();

        for (AllProducts allproducts : ProductInteractions.createAllProducts()) {
            if (allproducts.getbarcode().getValue().length() >= size) {
                if (allproducts.getbarcode().getValue().substring(0, size).equals(barcodeeverwritten)) {
                    barcodes.add(allproducts.getbarcode().getValue());
                }
            }
        }


        return barcodes;
    }

    @FXML
    protected void productEntryLabelButtonOnClicked() throws IOException {
        ProcessLogs.recordStockEntryProcess(productEntryLabelBarcode.getText(), productEntryLabelName.getText(), productEntryLabelPiece.getText());
        ProductInteractions.productEntry(productEntryLabelBarcode.getText(), productEntryLabelBrand.getText(), productEntryLabelName.getText(), productEntryLabelPiece.getText(), productEntryLabelTax.getText(), productEntryLabelBuyPrice.getText(), String.valueOf(Double.parseDouble(productEntryLabelBuyPrice.getText()) / Double.parseDouble(productEntryLabelPiece.getText())), productEntryLabelSellPrice.getText());
    }

    @FXML
    protected void barcodeFieldKeyTyped(KeyEvent keyEvent) {
        byte[] enter = {13};
        if (Arrays.toString(keyEvent.getCharacter().getBytes(StandardCharsets.UTF_8)).equals(Arrays.toString(enter))) {
            keyTypedAlgorithm();
        }
    }

    @FXML
    protected void buyPriceKeyPressed() {
        try {
            productEntryLabelPrice.setText(String.valueOf(Double.parseDouble(productEntryLabelBuyPrice.getText()) / Double.parseDouble(productEntryLabelPiece.getText())));
        } catch (NumberFormatException e) {
            productEntryLabelPrice.setText("0");
        }
    }

    protected void changeActiveUser(Label label) {
        label.setText("Kullanıcı: " + Session.username);
    }

    public void init() {
        lastPriceCollumnstatic = lastPriceCollumn;
        sellScreenTablestatic = sellScreenTable;
        discountCollumnstatic = discountCollumn;
        kdvCollumnstatic = kdvCollumn;
        totalDiscountLabelstatic = totalDiscountLabel;
        totalPriceLabelstatic = totalPriceLabel;
        totalTaxLabelstatic = totalTaxLabel;
        subTotalLabelstatic = subTotalLabel;
    }

    public void refreshtabledata() {
        for (Products pro : sellScreenTable.getItems()) {
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

    public ContextMenu setcontextmenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem azalt = new MenuItem("Azalt");
        azalt.setOnAction(actionEvent -> {
            if (sellScreenTable.getSelectionModel().getSelectedItem().amount == 1) {
                sellScreenTable.getItems().remove(sellScreenTable.getSelectionModel().getSelectedItem());
            } else {
                sellScreenTable.getSelectionModel().getSelectedItem().amount--;
            }
            refreshtabledata();
            init();
            changetotaldata();

        });
        MenuItem sil = new MenuItem("Sil");
        sil.setOnAction(actionEvent -> {
            sellScreenTable.getItems().remove(sellScreenTable.getSelectionModel().getSelectedItem());
            if (sellScreenTable.getSelectionModel().getSelectedItem() != null) {
                sellScreenTable.getSelectionModel().getSelectedItem().init();
            }
            refreshtabledata();
            init();
            changetotaldata();
        });
        contextMenu.getItems().add(azalt);
        contextMenu.getItems().add(sil);
        return contextMenu;
    }

    @FXML
    public void onstockcontrolopened() {
        stockTable.getItems().removeAll(stockTable.getItems());
        stockCollumnBarcode.setCellValueFactory(allProductsStringCellDataFeatures -> allProductsStringCellDataFeatures.getValue().getbarcode());
        stockCollumnAmont.setCellValueFactory(allProductsStringCellDataFeatures -> allProductsStringCellDataFeatures.getValue().getamont());
        stockCollumnBrand.setCellValueFactory(allProductsStringCellDataFeatures -> allProductsStringCellDataFeatures.getValue().getbrand());
        stockCollumnName.setCellValueFactory(allProductsStringCellDataFeatures -> allProductsStringCellDataFeatures.getValue().getname());
        stockCollumnTax.setCellValueFactory(allProductsStringCellDataFeatures -> allProductsStringCellDataFeatures.getValue().gettax());
        stockCollumnUnitBuy.setCellValueFactory(allProductsStringCellDataFeatures -> allProductsStringCellDataFeatures.getValue().getunitbuy());
        stockCollumnUnitSell.setCellValueFactory(allProductsStringCellDataFeatures -> allProductsStringCellDataFeatures.getValue().getunitsell());
        stockCollumnExpectedProfit.setCellValueFactory(allProductsStringCellDataFeatures -> allProductsStringCellDataFeatures.getValue().getprofit());
        stockTable.getItems().addAll(ProductInteractions.createAllProducts());
        stockTable.refresh();
    }

    @FXML
    public void stockControlClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2 & stockTable.getSelectionModel().getSelectedItem() != null) {
            AllProducts selectedproduct = stockTable.getSelectionModel().getSelectedItem();
            tabpane.getSelectionModel().select(buyScreenTab);
            productEntryLabelBarcode.setText(selectedproduct.getbarcode().getValue());
            productEntryLabelPrice.setText(String.valueOf(selectedproduct.getunitbuy().getValue()));
            productEntryLabelBrand.setText(selectedproduct.getbrand().getValue());
            productEntryLabelName.setText(selectedproduct.getname().getValue());
            productEntryLabelTax.setText(String.valueOf(selectedproduct.gettax().getValue()));
            productEntryLabelSellPrice.setText(String.valueOf(selectedproduct.getunitsell().getValue()));
        }
    }

    @FXML
    public void sellScreenKeyTyped(KeyEvent keyEvent) {
        if (tabpane.getSelectionModel().getSelectedItem().getId().equals(sellScreenTab.getId())) {
            byte[] enter = {13};
            byte[] back = {8};
            if (Arrays.toString(keyEvent.getCharacter().getBytes(StandardCharsets.UTF_8)).equals(Arrays.toString(enter))) {
                keyTypedAlgorithm();
            } else if (Arrays.toString(keyEvent.getCharacter().getBytes(StandardCharsets.UTF_8)).equals(Arrays.toString(back))) {
                String prebarcode = barcodeField.getText();
                prebarcode = prebarcode.substring(0, prebarcode.length() - 1);
                barcodeField.setText(prebarcode);
            } else {
                String prebarcode = barcodeField.getText();
                barcodeField.setText(prebarcode + keyEvent.getCharacter());
            }
        }
    }

    private void keyTypedAlgorithm() {
        boolean add = false;
        boolean find = false;
        Products productdb = null;
        if (sellScreenTable.getItems().size() > 0) {
            for (Products pro : sellScreenTable.getItems()) {
                if (Objects.equals(pro.barcode, barcodeField.getText())) {
                    pro.amount++;
                    pro.calsellprice.setText(String.valueOf((pro.unitsellprice - Double.parseDouble(pro.discount.getText())) * pro.amount));
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
    boolean switchadded = false;
    @FXML
    GridPane switchPane;
    @FXML
    ToggleSwitch toggleSwitch = new ToggleSwitch();
    public void statisticsTabOpened() {
        if (tabpane.getSelectionModel().getSelectedItem().equals(statisticsTab)) {
            if (!switchadded){
                Label dateLabel = new Label(LocalDate.now().getMonth().toString());
                switchadded = true;
                ToggleSwitch finalToggleSwitch = toggleSwitch;
                toggleSwitch.switchOnProperty().addListener((a, b, c) -> {
                    if (c) {
                        initBarData(finalToggleSwitch.switchOnProperty().getValue());
                        dateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)) + " " +LocalDate.now().getDayOfWeek());
                    } else {
                        initBarData(finalToggleSwitch.switchOnProperty().getValue());
                        dateLabel.setText(LocalDate.now().getMonth().toString());
                    }
                });
                toggleSwitch.setMaxWidth(200);
                toggleSwitch.setAlignment(Pos.CENTER);
                switchPane.setHalignment(toggleSwitch,HPos.CENTER);
                switchPane.setHalignment(dateLabel,HPos.CENTER);
                switchPane.add(toggleSwitch, 0,0,1,1);
                switchPane.add(dateLabel,0,1,1,1);
                switchPane.setPrefWidth(200);
            }
            initPieData(toggleSwitch.switchOnProperty().getValue());
            initBarData(toggleSwitch.switchOnProperty().getValue());
        }
    }
    public void initPieData(Boolean isdaily){
        pieChart.getData().removeAll(pieChart.getData());
        Stats.createChartInfo();
        setPieChart(Stats.productChartInfo);
    }
    public void initBarData(Boolean isdaily){
        stackedBarChart.getData().removeAll(stackedBarChart.getData());
        //LinkedHashMap<String, Double> datesAndProfits = A <String, Double> LinkedHashMap from PROFITS TABLE
        LinkedHashMap<String, Double> datesAndProfits = DBAccess.barChartValues();
        /*datesAndProfits.put("2022-08-09", 43.0);
        datesAndProfits.put("2022-08-12", 32.0);
        datesAndProfits.put("2022-09-15", 37.0);
        datesAndProfits.put("2022-09-18", 51.0);
        datesAndProfits.put("2022-10-19", 63.50);
        datesAndProfits.put("2022-10-11", 35.43);
        datesAndProfits.put("2022-11-28", 31.46);*/
        if (isdaily){
            setBarChart(datesAndProfits);
        } else {
            HashMap<String, Double> barChartData = Stats.calculateMonthlyProfits(datesAndProfits);
            setBarChart(barChartData);

        }

    }
    public void setPieChart(HashMap<String, Number> nameAndPrice) {
        for (String name : nameAndPrice.keySet()) {
            pieChart.getData().add(new PieChart.Data(name, nameAndPrice.get(name).doubleValue()));
        }
        Label pieInfo = new Label("");
        pieInfo.setTextFill(Color.BLACK);
        DoubleBinding total = Bindings.createDoubleBinding(() -> pieChart.getData().stream().mapToDouble(PieChart.Data::getPieValue).sum(), pieChart.getData());
        for (final PieChart.Data data : pieChart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_MOVED, e -> {
                pieInfo.setVisible(true);
                pieInfo.setTranslateX(e.getSceneX() + 5);
                pieInfo.setTranslateY(e.getSceneY() - 90);
                String text = String.format("%.1f%%", 100 * data.getPieValue() / total.get());
                pieInfo.setText(text + "\n" + (int) data.getPieValue() + "TL");
            });
            data.getNode().addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> pieInfo.setVisible(false));
        }
        pieChart.setLabelsVisible(false);
        statisticAnchorPane.getChildren().add(pieInfo);

    }

    public void setBarChart(HashMap<String, Double> dateAndPriceList) {
        var series = new StackedBarChart.Series<String, Number>();
        for (String date : dateAndPriceList.keySet()) {
            series.getData().add(new XYChart.Data<>(date, dateAndPriceList.get(date)));
        }
        series.setName("Tarih");
        stackedBarChart.getData().add(series);
        stackedBarChart.setAnimated(false);
        Label barinfo = new Label("");
        for (final StackedBarChart.Data<String,Number> data : series.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_MOVED, mouseEvent -> {
                barinfo.setVisible(true);
                barinfo.setTranslateX(mouseEvent.getSceneX());
                barinfo.setTranslateY(mouseEvent.getSceneY() - 80);
                barinfo.setText(data.getYValue().toString());
            });
            data.getNode().addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> barinfo.setVisible(false));

        }
        barinfo.setTextFill(Color.BLACK);

        statisticAnchorPane.getChildren().add(barinfo);
    }

}
