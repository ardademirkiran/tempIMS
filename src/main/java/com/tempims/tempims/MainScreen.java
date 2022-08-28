package com.tempims.tempims;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;

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
    public TableColumn<AllProducts, Button> stockCollumnAddList;
    public Tab logTab;
    public AnchorPane stockControlPane;
    public TableView<LogObject> historyTable;
    public TableColumn<LogObject, String> historyTableDate;
    public TableColumn<LogObject, String> historyTableEmployee;
    public TableColumn<LogObject, String> historyTableType;
    public TableColumn<LogObject, String> historyTableExplanation;
    public VBox appVbox;
    public Tab userTab;
    public Button bitanebuton;
    public TableColumn<User,String> userTableUserName;
    public TableColumn<User,CheckBox> userTableSellScreenTabPerm;
    public TableColumn<User,CheckBox> userTableEntryScreenTabPerm;
    public TableColumn<User,CheckBox> userTableHistoryScreenTabPerm;
    public TableColumn<User,CheckBox> userTableStockControlScreenTabPerm;
    public TableColumn<User,CheckBox> userTableStatisticsScreenTabPerm;
    public TableView<User> userTable;
    CategoryAxis xAxis = new CategoryAxis();
    NumberAxis yAxis = new NumberAxis("TL", 0, 50, 5);
    public StackedBarChart<String, Number> stackedBarChart = new StackedBarChart<>(xAxis, yAxis);
    boolean eventhandleradded = false;
    String barcodeeverwritten = "";
    ContextMenu productEntryBarcodeContextMenu = new ContextMenu();
    boolean stockControlSwitchAdded = false;
    ToggleSwitch toggleSwitchStock = new ToggleSwitch("Sipariş Listesi", "Stok Kontrol");
    boolean switchadded = false;
    @FXML
    GridPane switchPane;
    @FXML
    ToggleSwitch toggleSwitchStatics = new ToggleSwitch("Günlük", "Aylık");

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
        if (productslist.size() > 0) {
            ProcessLogs.recordSalesProcess(productslist, Double.parseDouble(totalPriceLabel.getText()));
            Stats.updateProfit(productslist);
            for (Products product : productslist) {
                ProductInteractions.sellProduct(product.barcode, product.amount);
            }
            cancelButtonClicked();
        } else {
            System.out.println("bossatış");
        }
    }
    protected void editTabsForUsers(){
            if (!UserInteractions.user.entryScreenPerm) {
                tabpane.getTabs().remove(buyScreenTab);
            }
            if (!UserInteractions.user.historyScreenPerm) {
                tabpane.getTabs().remove(logTab);
            }
            if (!UserInteractions.user.sellScreenPerm) {
                tabpane.getTabs().remove(sellScreenTab);
            }
            if (!UserInteractions.user.statsScreenPerm) {
                tabpane.getTabs().remove(statisticsTab);
            }
            if (!UserInteractions.user.trackStockScreenPerm) {
                tabpane.getTabs().remove(stockControlTab);
            }
            if (!UserInteractions.user.usersScreenPerm) {
                tabpane.getTabs().remove(userTab);
            }
    }
    @FXML
    public void initialize(){
        editTabsForUsers();
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
        Main.globalStage.addEventHandler(KeyEvent.KEY_TYPED, this::sellScreenKeyTyped);
        eventhandleradded = true;

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
                label.setPrefWidth(productEntryLabelBarcode.getWidth() - 13);
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
        label.setText("Kullanıcı: " + UserInteractions.user.username);
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
        if (tabpane.getSelectionModel().getSelectedItem().equals(stockControlTab)) {
            stockCollumnBarcode.setCellValueFactory(allProductsStringCellDataFeatures -> allProductsStringCellDataFeatures.getValue().getbarcode());
            stockCollumnAmont.setCellValueFactory(allProductsStringCellDataFeatures -> allProductsStringCellDataFeatures.getValue().getamont());
            stockCollumnBrand.setCellValueFactory(allProductsStringCellDataFeatures -> allProductsStringCellDataFeatures.getValue().getbrand());
            stockCollumnName.setCellValueFactory(allProductsStringCellDataFeatures -> allProductsStringCellDataFeatures.getValue().getname());
            stockCollumnTax.setCellValueFactory(allProductsStringCellDataFeatures -> allProductsStringCellDataFeatures.getValue().gettax());
            stockCollumnUnitBuy.setCellValueFactory(allProductsStringCellDataFeatures -> allProductsStringCellDataFeatures.getValue().getunitbuy());
            stockCollumnUnitSell.setCellValueFactory(allProductsStringCellDataFeatures -> allProductsStringCellDataFeatures.getValue().getunitsell());
            stockCollumnExpectedProfit.setCellValueFactory(allProductsStringCellDataFeatures -> allProductsStringCellDataFeatures.getValue().getprofit());
            stockCollumnAddList.setCellValueFactory(allProductsButtonCellDataFeatures -> allProductsButtonCellDataFeatures.getValue().getButton());
            if (!stockControlSwitchAdded) {
                stockTable.getItems().removeAll(stockTable.getItems());
                stockTable.getItems().addAll(ProductInteractions.createAllProducts());
                stockControlSwitchAdded = true;
                stockControlPane.getChildren().add(toggleSwitchStock);
                toggleSwitchStock.setMinWidth(500);
                AnchorPane.setLeftAnchor(toggleSwitchStock, 500.0);
                AnchorPane.setRightAnchor(toggleSwitchStock, 500.0);
                toggleSwitchStock.switchOnProperty().addListener((a, b, c) -> {
                    if (c) {
                        stockTable.getItems().removeAll(stockTable.getItems());
                        stockTable.getItems().addAll(AllProducts.buyArray);
                        stockCollumnAddList.setVisible(false);
                    } else {
                        stockTable.getItems().removeAll(stockTable.getItems());
                        stockTable.getItems().addAll(ProductInteractions.createAllProducts());
                        stockCollumnAddList.setVisible(true);
                    }
                });
            }
        }
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

    public void statisticsTabOpened() {
        if (tabpane.getSelectionModel().getSelectedItem().equals(statisticsTab)) {
            if (!switchadded) {
                Label dateLabel = new Label(LocalDate.now().getMonth().toString());
                switchadded = true;
                ToggleSwitch finalToggleSwitch = toggleSwitchStatics;
                toggleSwitchStatics.switchOnProperty().addListener((a, b, c) -> {
                    if (c) {
                        initBarData(finalToggleSwitch.switchOnProperty().getValue());
                        dateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)) + " " + LocalDate.now().getDayOfWeek());
                    } else {
                        initBarData(finalToggleSwitch.switchOnProperty().getValue());
                        dateLabel.setText(LocalDate.now().getMonth().toString());
                    }
                });
                toggleSwitchStatics.setMaxWidth(200);
                toggleSwitchStatics.setAlignment(Pos.CENTER);
                GridPane.setHalignment(toggleSwitchStatics, HPos.CENTER);
                GridPane.setHalignment(dateLabel, HPos.CENTER);
                switchPane.add(toggleSwitchStatics, 0, 0, 1, 1);
                switchPane.add(dateLabel, 0, 1, 1, 1);
                switchPane.setPrefWidth(200);
            }
            initPieData(toggleSwitchStatics.switchOnProperty().getValue());
            initBarData(toggleSwitchStatics.switchOnProperty().getValue());
        }
        tabpane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            if (oldTab.equals(statisticsTab)) {
                for (PieChart.Data data : pieChart.getData()) {
                    data.nameProperty().set("");
                }
            }
        });
    }

    public void initPieData(Boolean isdaily) {
        pieChart.getData().removeAll(pieChart.getData());
        if (isdaily) {
            setPieChart(Stats.createDailyPieChartInfo());
        } else {
            setPieChart(Stats.createMonthlyPieChartInfo());
        }
    }

    public void initBarData(Boolean isdaily) {
        stackedBarChart.getData().removeAll(stackedBarChart.getData());
        LinkedHashMap<String, Double> datesAndProfits = DBAccess.barChartValues();
        if (isdaily) {
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
        pieChart.setLabelsVisible(true);
        pieChart.setAnimated(true);
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
        for (final StackedBarChart.Data<String, Number> data : series.getData()) {
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

    public void logTabOpened() throws IOException {
        if (tabpane.getSelectionModel().getSelectedItem().equals(logTab)) {
            historyTable.getItems().removeAll(historyTable.getItems());
            historyTableDate.setCellValueFactory(logObjectLocalDateCellDataFeatures -> logObjectLocalDateCellDataFeatures.getValue().getDate());
            historyTableEmployee.setCellValueFactory(logObjectLocalDateCellDataFeatures -> logObjectLocalDateCellDataFeatures.getValue().getEmployee());
            historyTableType.setCellValueFactory(logObjectLocalDateCellDataFeatures -> logObjectLocalDateCellDataFeatures.getValue().getType());
            historyTableExplanation.setCellValueFactory(logObjectLocalDateCellDataFeatures -> logObjectLocalDateCellDataFeatures.getValue().getExplanation());
            historyTable.getItems().addAll(ProcessLogs.getLogObjects());
            historyTable.setRowFactory(tv -> new TableRow<>() {
                Node detailsPane;

                {
                    selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                        if (isNowSelected && !itemProperty().getValue().getDetails().getValue().isEmpty()) {
                            getChildren().add(detailsPane);
                        } else {
                            getChildren().remove(detailsPane);
                        }
                        this.requestLayout();
                    });
                    detailsPane = createDetailsPane(itemProperty());
                }

                @Override
                protected void updateItem(LogObject item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null && item.type.equals("SATIŞ"))
                        setStyle("-fx-background-color: #79cc79");
                    else {
                        setStyle("");
                    }
                }

                @Override
                protected double computePrefHeight(double width) {
                    if (isSelected()) {
                        return super.computePrefHeight(width) + detailsPane.prefHeight(getWidth());
                    } else {
                        return super.computePrefHeight(width);
                    }
                }

                @Override
                protected void layoutChildren() {
                    super.layoutChildren();
                    if (isSelected()) {
                        double width = getWidth();
                        double paneHeight = detailsPane.prefHeight(width);
                        detailsPane.resizeRelocate(0, getHeight() - paneHeight, width, paneHeight);
                    }
                }
            });

        }
    }

    private Node createDetailsPane(ObjectProperty<LogObject> item) {
        BorderPane detailsPane = new BorderPane();
        Label detailsLabel = new Label();
        VBox labels = new VBox(detailsLabel);
        labels.setAlignment(Pos.CENTER_LEFT);
        labels.setPadding(new Insets(2, 2, 2, 16));
        detailsPane.setCenter(labels);
        detailsPane.setStyle("-fx-background-color: -fx-background; -fx-background: lightgray;");
        item.addListener((obs, oldItem, newItem) -> {
            if (newItem == null) {
                detailsLabel.setText("");
            } else {
                detailsLabel.setText(newItem.getDetails().getValue());
            }
        });
        return detailsPane;
    }
    @FXML
    protected void onUserTabOpened(){
        if (tabpane.getSelectionModel().getSelectedItem().equals(userTab)){
            userTable.getItems().removeAll(userTable.getItems());
            userTableUserName.setCellValueFactory(userCheckBoxCellDataFeatures -> userCheckBoxCellDataFeatures.getValue().getUserName());
            userTableEntryScreenTabPerm.setCellValueFactory(userCheckBoxCellDataFeatures -> userCheckBoxCellDataFeatures.getValue().getCheckBoxEntryScreenPerm());
            userTableHistoryScreenTabPerm.setCellValueFactory(userCheckBoxCellDataFeatures -> userCheckBoxCellDataFeatures.getValue().getCheckBoxHistoryScreenPerm());
            userTableSellScreenTabPerm.setCellValueFactory(userCheckBoxCellDataFeatures -> userCheckBoxCellDataFeatures.getValue().getCheckBoxSellScreenPerm());
            userTableStatisticsScreenTabPerm.setCellValueFactory(userCheckBoxCellDataFeatures -> userCheckBoxCellDataFeatures.getValue().getCheckBoxStatsScreenPerm());
            userTableStockControlScreenTabPerm.setCellValueFactory(userCheckBoxCellDataFeatures -> userCheckBoxCellDataFeatures.getValue().getCheckBoxTrackStockScreenPerm());
            userTable.getItems().addAll(UserInteractions.getAllUsers());
            userTable.getItems().add(new User("Rotroq","010101"));
        }
    }
    @FXML
    protected void onRegister() throws IOException {
        Stage registerStage = new Stage();
        registerStage.setTitle("Kaydol");
        FXMLLoader fxmlLoader = new FXMLLoader(LoginScreen.class.getResource("SignupScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 480, 360);
        Main.setGradient(registerStage, scene);
    }
}
