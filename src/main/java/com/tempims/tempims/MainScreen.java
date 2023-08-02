package com.tempims.tempims;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
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
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class MainScreen {

    public static TableColumn<SellScreenProduct, Label> lastPriceCollumnstatic;
    public static TableView<SellScreenProduct> sellScreenTablestatic;
    public static TableColumn<SellScreenProduct, TextField> discountCollumnstatic;
    public static TableColumn<SellScreenProduct, Integer> kdvCollumnstatic;
    public static Label totalDiscountLabelstatic;
    public static Label totalPriceLabelstatic;
    public static Label totalTaxLabelstatic;
    public static Label subTotalLabelstatic;
    public static double totalRevenue = 0;
    static String companyName;
    public AnchorPane statisticAnchorPane;
    public Tab statisticsTab;
    public PieChart pieChart;
    public Label username;
    public TableView<SellScreenProduct> sellScreenTable;
    public TableColumn<SellScreenProduct, String> barcodeCollumn;
    public TableColumn<SellScreenProduct, String> nameCollumn;
    public TableColumn<SellScreenProduct, Integer> amountCollumn;
    public TableColumn<SellScreenProduct, Integer> kdvCollumn;
    public TableColumn<SellScreenProduct, TextField> percentageDiscountCollumn;
    public TableColumn<SellScreenProduct, TextField> discountCollumn;
    public TableColumn<SellScreenProduct, Label> lastPriceCollumn;
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
    public Label ciro_label;
    public Label subTotalLabel;
    public TableView<StockViewProduct> stockTable;
    public TableColumn<StockViewProduct, String> stockCollumnBarcode;
    public TableColumn<StockViewProduct, String> stockCollumnBrand;
    public TableColumn<StockViewProduct, String> stockCollumnName;
    public TableColumn<StockViewProduct, Integer> stockCollumnAmont;
    public TableColumn<StockViewProduct, Integer> stockCollumnTax;
    public TableColumn<StockViewProduct, Double> stockCollumnUnitBuy;
    public TableColumn<StockViewProduct, Double> stockCollumnUnitSell;
    public TableColumn<StockViewProduct, Double> stockCollumnExpectedProfit;
    public TableColumn<StockViewProduct, Button> stockCollumnAddList;
    public Tab logTab;
    public AnchorPane stockControlPane;
    public TableView<LogObject> historyTable;
    public TableColumn<LogObject, String> historyTableDate;
    public TableColumn<LogObject, String> historyTableEmployee;
    public TableColumn<LogObject, String> historyTableType;
    public TableColumn<LogObject, String> historyTableExplanation;
    public VBox appVbox;
    public Tab userTab;
    public TableColumn<User, String> userTableUserName;
    public TableColumn<User, CheckBox> userTableSellScreenTabPerm;
    public TableColumn<User, CheckBox> userTableEntryScreenTabPerm;
    public TableColumn<User, CheckBox> userTableHistoryScreenTabPerm;
    public TableColumn<User, CheckBox> userTableStockControlScreenTabPerm;
    public TableColumn<User, CheckBox> userTableStatisticsScreenTabPerm;
    public TableView<User> userTable;
    public TableColumn<User, CheckBox> userTableUsersScreenTabPerm;
    public Label companyNameLabel;
    public CategoryAxis xAxis = new CategoryAxis();
    public NumberAxis yAxis = new NumberAxis("TL", 0, 50, 5);
    public LineChart<String, Number> stackedBarChart = new LineChart<>(xAxis, yAxis);
    public String barcodeeverwritten = "";
    public ContextMenu productEntryBarcodeContextMenu = new ContextMenu();
    public ToggleSwitch toggleSwitchStock = new ToggleSwitch("Sipariş Listesi", "Stok Kontrol");
    public GridPane statisticsGridPane;
    public Label dateLabel;
    public ToggleSwitch lineToggleSwitch = new ToggleSwitch("Aylık", "Yıllık");
    public ToggleSwitch pieToggleSwitch = new ToggleSwitch("Günlük", "Aylık");
    public Label barcodeInfo;
    public Label brandInfo;
    public Label nameInfo;
    public Label amountInfo;
    public Label taxInfo;
    public Label buyPriceInfo;
    public Label buyPricePerAmountInfo;
    public Label sellPriceInfo;
    public Label discountTextLabel;
    public Label subTotalTextLabel;
    public Label sellPriceTextLabel;
    public Label taxTextLabel;
    public CheckBox productEntryTabCheckBox;
    List<TextField> productEntryLabelTextFields;
    Font font = Font.loadFont(new FileInputStream("src/main/resources/com/tempims/tempims/DS-DIGIT.TTF"), 45);
    @FXML
    Label ciro_text_label;
    Map<String, String> test1 = Map.of("48", "0", "49", "1", "50", "2", "51", "3", "52", "4", "53", "5", "54", "6", "55", "7", "56", "8", "57", "9");
    ArrayList<byte[]> bytes = new ArrayList<>();
    boolean isHolding = false;
    String amountByte = "";
    Integer amountInteger = 1;

    public MainScreen() throws FileNotFoundException {
    }

    public static void setLabelDisplay() {
        double totalprice = 0;
        double totaldiscount = 0;
        double subtotal = 0;
        totalPriceLabelstatic.setText(String.valueOf(0));
        totalDiscountLabelstatic.setText(String.valueOf(0));
        totalTaxLabelstatic.setText(String.valueOf(0));
        subTotalLabelstatic.setText(String.valueOf(0));

        for (SellScreenProduct item : sellScreenTablestatic.getItems()) {
            totalprice += Double.parseDouble(lastPriceCollumnstatic.getCellObservableValue(item).getValue().getText());
            totaldiscount += Double.parseDouble(discountCollumnstatic.getCellObservableValue(item).getValue().getText()) * item.amount;
            subtotal += Double.parseDouble(lastPriceCollumnstatic.getCellObservableValue(item).getValue().getText()) / (1 + kdvCollumnstatic.getCellObservableValue(item).getValue() / 100.0);
        }
        totalPriceLabelstatic.setText(String.format(Locale.ROOT, "%.2f", totalprice));
        subTotalLabelstatic.setText(String.format(Locale.ROOT, "%.2f", subtotal));
        totalDiscountLabelstatic.setText(String.format(Locale.ROOT, "%.2f", totaldiscount));
        totalTaxLabelstatic.setText(String.format(Locale.ROOT, "%.2f", totalprice - subtotal));
    }

    static void inputController(TextField selectedTextField) {
        selectedTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("^[0-9.]*$")) {
                if (newValue.matches("^[0-9]+\\.([0-9]{0,2})")) {

                } else if (newValue.matches("^[0-9]*\\.([0-9]*)\\.*") && selectedTextField.getText().length() >= 2) {
                    StringBuilder sb = new StringBuilder(newValue);
                    sb.deleteCharAt(sb.length() - 1);
                    newValue = String.valueOf(sb);
                    selectedTextField.setText(newValue);
                }
            } else {
                selectedTextField.setText(newValue.replaceAll("[^\\d.]", ""));
            }
        });
    }

    @FXML
    protected void sellButtonClicked() throws IOException, SQLException {
        String totalpricelabeltext = totalPriceLabel.getText();
        if (sellScreenTable.getItems().size() > 0) {
            SellScreenProduct[] sellScreenProductList = sellScreenTable.getItems().toArray(new SellScreenProduct[0]);
            cancelButtonClicked();
            Connection conn = DBAccess.connect();
            double totalProfit = 0;
            String midText = "";
            for (SellScreenProduct product : sellScreenProductList) {
                totalRevenue += product.sellPriceDB * product.amount;
                double profitToAdd = Stats.calculateProfit(product);
                DBAccess.updateStock(conn, product.barcode, -product.amount);

                LocalDate date = java.time.LocalDate.now();

                DBAccess.amendProfit(conn, String.valueOf(date.getDayOfMonth()), String.valueOf(date.getMonthValue()), String.valueOf(date.getYear()), product.barcode, product.displayName, profitToAdd);
                midText += product.amount + "x" + product.name + "/-";
                totalProfit += profitToAdd;
            }
            ProcessLogs.recordSalesProcess(midText, Double.parseDouble(totalpricelabeltext));
            conn.close();
            ciro_label.setText(String.format(Locale.ROOT, "%.2f", totalRevenue));
        }
    }

    @FXML
    protected void returnButtonClicked() throws IOException, SQLException {
        Connection conn = DBAccess.connect();
        String totalpricelabeltext = totalPriceLabel.getText();
        if (sellScreenTable.getItems().size() > 0) {
            SellScreenProduct[] sellScreenProductList = sellScreenTable.getItems().toArray(new SellScreenProduct[0]);
            cancelButtonClicked();
            String midText = "";
            for (SellScreenProduct product : sellScreenProductList) {
                totalRevenue -= product.sellPriceDB * product.amount;
                double profitToAdd = Stats.calculateProfit(product);
                DBAccess.updateStock(conn, product.barcode, product.amount);

                LocalDate date = java.time.LocalDate.now();
                System.out.println(date);

                DBAccess.amendProfit(conn, String.valueOf(date.getDayOfMonth()), String.valueOf(date.getMonthValue()), String.valueOf(date.getYear()), product.barcode, product.displayName, (product.unitBuyPrice - product.unitSellPrice) * product.amount);
                midText += product.amount + "x" + product.name + "/-";
            }
            ciro_label.setText(String.format(Locale.ROOT, "%.2f", totalRevenue));
            ProcessLogs.recordReturnProcess(midText, Double.parseDouble(totalpricelabeltext));
        }
        conn.close();
    }

    protected void editTabsForUsers() {
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
    public void initialize() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("company.txt"));
        String companyName = reader.readLine();
        companyNameLabel.setText(companyName);
        reader.close();
        totalPriceLabel.setFont(font);
        subTotalLabel.setFont(font);
        totalDiscountLabel.setFont(font);
        totalTaxLabel.setFont(font);
        discountTextLabel.setFont(font);
        subTotalTextLabel.setFont(font);
        sellPriceTextLabel.setFont(font);
        ciro_label.setFont(font);
        ciro_text_label.setFont(font);
        taxTextLabel.setFont(font);
        barcodeInfo.setTooltip(createToolTip("Tanımlanan ürünün barkodu bu alana yazılır."));
        brandInfo.setTooltip(createToolTip("Ürünün ait olduğu marka bu alana yazılır."));
        nameInfo.setTooltip(createToolTip("Ürünün adı bu alana yazılır."));
        amountInfo.setTooltip(createToolTip("Tanımlanan ürünün giriş adeti bu alana yazılır."));
        taxInfo.setTooltip(createToolTip("Ürünün KDV oranı bu alana yazılır."));
        buyPriceInfo.setTooltip(createToolTip("Tanımlanan ürünün toplam alış fiyatı bu alana yazılır. Birim alış fiyatını uygulama otomatik olarak hesaplayacaktır."));
        buyPricePerAmountInfo.setTooltip(createToolTip("Tanımlanan ürünün birim alış fiyatı, otomatik olarak hesaplanır."));
        sellPriceInfo.setTooltip(createToolTip("Tanımlanan ürünün reyon satış fiyatı bu alana yazılır."));
        sellScreenTable.setPlaceholder(new Label("Bir ürün eklemeden satış veya iade yapamazsınız"));
        stockTable.setPlaceholder(new Label("Henüz bir ürün kaydı oluşturulmadı"));
        companyNameLabel.setText("Şirket: " + companyName);
        editTabsForUsers(); // user permissions for tabs
        changeActiveUser(username);
        init(); // an initialization for static variables

        ContextMenu contextMenuSell = setSellScreenContextMenu();
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
                contextMenuSell.show(sellScreenTable, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());
            }
            sellScreenTable.setOnMouseClicked(event -> {
                if (i[0] > 0) contextMenuSell.hide();
                else {
                    i[0]++;
                }
            });
        });
        try {
            ContextMenu contextMenuStock = setStockControlContextMenu();
            stockTable.setOnContextMenuRequested(contextMenuEvent -> {
                final int[] i = {0};
                Node source = contextMenuEvent.getPickResult().getIntersectedNode();

                while (source != null && !(source instanceof TableRow)) {
                    source = source.getParent();
                }

                if (source == null || ((TableRow<?>) source).isEmpty()) {
                    stockTable.getSelectionModel().clearSelection();

                }
                if (stockTable.getSelectionModel().getSelectedItem() != null & !(source == null || ((TableRow<?>) source).isEmpty())) {
                    contextMenuStock.show(stockTable, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());
                }
                sellScreenTable.setOnMouseClicked(event -> {
                    if (i[0] > 0) contextMenuStock.hide();
                    else {
                        i[0]++;
                    }
                });
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }


        Main.globalStage.addEventHandler(KeyEvent.KEY_TYPED, this::sellScreenKeyTyped); //sell screen key event handler
        Main.globalStage.addEventHandler(KeyEvent.KEY_RELEASED, this::sellScreenKeyReleased);
        stockTable.getItems().removeAll(stockTable.getItems()); // stock control switch and addlist
        stockTable.getItems().addAll(ProductInteractions.createAllProducts());
        stockControlPane.getChildren().add(toggleSwitchStock);
        toggleSwitchStock.setMinWidth(500);
        AnchorPane.setLeftAnchor(toggleSwitchStock, 500.0);
        AnchorPane.setRightAnchor(toggleSwitchStock, 500.0);
        toggleSwitchStock.switchOnProperty().addListener((a, b, c) -> {
            if (c) {
                stockTable.getItems().removeAll(stockTable.getItems());
                stockTable.getItems().addAll(StockViewProduct.buyArray);
                stockCollumnAddList.setVisible(false);
            } else {
                stockTable.getItems().removeAll(stockTable.getItems());
                stockTable.getItems().addAll(ProductInteractions.createAllProducts());
                stockCollumnAddList.setVisible(true);
            }
        });

        pieToggleSwitch.switchOnProperty().addListener((a, b, c) -> { // pieChart initializations
            try {
                if (c) {
                    initPieData(pieToggleSwitch.switchOnProperty().getValue());
                } else {
                    initPieData(pieToggleSwitch.switchOnProperty().getValue());
                }
            } catch (SQLException e) {
                e.printStackTrace(System.out);
            }
        });

        pieToggleSwitch.setMaxWidth(400);
        pieToggleSwitch.setMaxHeight(40);
        pieToggleSwitch.setAlignment(Pos.CENTER);
        GridPane.setHalignment(pieToggleSwitch, HPos.CENTER);
        statisticsGridPane.add(pieToggleSwitch, 0, 1, 1, 1);

        lineToggleSwitch.switchOnProperty().addListener((a, b, c) -> { // lineChart initializations
            try {
                if (c) {
                    initLineData(lineToggleSwitch.switchOnProperty().getValue());
                } else {
                    initLineData(lineToggleSwitch.switchOnProperty().getValue());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        lineToggleSwitch.setMaxWidth(400);
        lineToggleSwitch.setMaxHeight(40);
        lineToggleSwitch.setAlignment(Pos.CENTER);
        GridPane.setHalignment(lineToggleSwitch, HPos.CENTER);
        statisticsGridPane.add(lineToggleSwitch, 1, 1, 1, 1);

        productEntryLabelPrice.setContextMenu(new ContextMenu());
        productEntryLabelPiece.setContextMenu(new ContextMenu());
        productEntryLabelBrand.setContextMenu(new ContextMenu());
        productEntryLabelName.setContextMenu(new ContextMenu());
        productEntryLabelTax.setContextMenu(new ContextMenu());
        productEntryLabelBuyPrice.setContextMenu(new ContextMenu());
        productEntryLabelSellPrice.setContextMenu(new ContextMenu());
        productEntryLabelBarcode.setContextMenu(new ContextMenu());
        barcodeField.setContextMenu(new ContextMenu());

        productEntryLabelName.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (productEntryTabCheckBox.isSelected()) {
                    productEntryLabelBarcode.setText(productEntryLabelName.getText());
                    productEntryLabelBrand.setText(productEntryLabelName.getText());
                }
            }
        });

        productEntryTabCheckBox.selectedProperty().addListener((a, b, c) -> {
            productEntryLabelBrand.setDisable(c);
            productEntryLabelBarcode.setDisable(c);
            if (c) {
                productEntryLabelBarcode.setText(productEntryLabelName.getText());
                productEntryLabelBrand.setText(productEntryLabelName.getText());
            }
        });
        List<TextField> productEntryLabelTextFields = Arrays.asList(productEntryLabelBrand, productEntryLabelName, productEntryLabelTax, productEntryLabelPiece, productEntryLabelBuyPrice, productEntryLabelSellPrice);
        for (TextField t : productEntryLabelTextFields) {
            t.setOnKeyTyped(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent keyEvent) {
                    t.getStyleClass().remove("error");
                }
            });
        }
        buyScreenTab.getStyleClass().add("my-container");

    }

    private ContextMenu setStockControlContextMenu() throws SQLException {
        Connection conn = DBAccess.connect();
        ContextMenu contextMenu = new ContextMenu();
        MenuItem decreaseItem = new MenuItem("Azalt");
        decreaseItem.setOnAction(actionEvent -> {
            if (stockTable.getSelectionModel().getSelectedItem().number > 0) {
                stockTable.getSelectionModel().getSelectedItem().number--;
                DBAccess.updateStock(conn, stockTable.getSelectionModel().getSelectedItem().barcode, -1);

            }

            refreshStockTableData();
            init();
            setLabelDisplay();

        });
        MenuItem removeItem = new MenuItem("Sil");
        removeItem.setOnAction(actionEvent -> {
            //DBAccess.updateStock(conn, stockTable.getSelectionModel().getSelectedItem().barcode, -stockTable.getSelectionModel().getSelectedItem().number);
            try {
                DBAccess.deleteRecord(stockTable.getSelectionModel().getSelectedItem().barcode);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            stockTable.getSelectionModel().getSelectedItem().number = 0;
            refreshStockTableData();
            //FIXME
            init();
            setLabelDisplay();
        });
        contextMenu.getItems().add(decreaseItem);
        contextMenu.getItems().add(removeItem);
        return contextMenu;

    }

    private void refreshStockTableData() {
        stockCollumnAmont.setCellValueFactory(productsTextFieldCellDataFeatures -> (productsTextFieldCellDataFeatures.getValue().getAmount()));
        stockTable.refresh();
    }

    private void sellScreenKeyReleased(KeyEvent keyEvent) {
        byte[] tab = {9};
        byte[] empty = {0};
        if (bytes.size() != 0) {
            if (Arrays.toString(bytes.get(0)).equals(Arrays.toString(tab))) {
                bytes.add(empty);
                bytes.remove(bytes.size() - 1);
                bytes.remove(bytes.size() - 1);
            }
            if (bytes.isEmpty()) {
                isHolding = false;
            }
        }

    }

    @FXML
    public void sellScreenKeyTyped(KeyEvent keyEvent) {
        if (tabpane.getSelectionModel().getSelectedItem().getId().equals(sellScreenTab.getId())) {
            byte[] enter = {13};
            byte[] back = {8};
            byte[] tab = {9};
            if (Arrays.toString(keyEvent.getCharacter().getBytes(StandardCharsets.UTF_8)).equals(Arrays.toString(enter))) {
                keyTypedAlgorithm(barcodeField.getText());
            } else if (Arrays.toString(keyEvent.getCharacter().getBytes(StandardCharsets.UTF_8)).equals(Arrays.toString(back))) {
                String prebarcode = barcodeField.getText();
                prebarcode = prebarcode.substring(0, prebarcode.length() - 1);
                barcodeField.setText(prebarcode);
            } else if (Arrays.toString(keyEvent.getCharacter().getBytes(StandardCharsets.UTF_8)).equals(Arrays.toString(tab))) {
                if (!isHolding) {
                    isHolding = true;
                    bytes.add((keyEvent.getCharacter().getBytes(StandardCharsets.UTF_8)));
                }
            } else {
                if (isHolding) {
                    bytes.add(keyEvent.getCharacter().getBytes(StandardCharsets.UTF_8));
                    amountByte += Arrays.toString(keyEvent.getCharacter().getBytes(StandardCharsets.UTF_8));
                } else {
                    if (!amountByte.isEmpty()) {
                        String realamount = "";
                        String[] amountSplitted = amountByte.split("]\\[");
                        for (String s : amountSplitted) {
                            s = s.replaceAll("]", "").replaceAll("\\[", "");
                            realamount += test1.get(s);
                        }
                        amountByte = "";
                        try {
                            amountInteger = Integer.parseInt(realamount);
                        } catch (Exception ignored) {
                        }
                    }
                    String prebarcode = barcodeField.getText();
                    barcodeField.setText(prebarcode + keyEvent.getCharacter());
                }
            }
        }
    }

    public Tooltip createToolTip(String HintText) {
        Tooltip tooltip = new Tooltip(HintText);
        tooltip.setStyle("""
                -fx-border-color: WHITESMOKE;
                -fx-border-width: 1px;
                -fx-background-color: WHITESMOKE;
                -fx-text-fill: black;
                -fx-background-radius: 4;
                -fx-effect: dropshadow( gaussian , BLACK , 4,0,0.5,1.8 );
                -fx-border-radius: 4;
                -fx-opacity: 1.0;""".indent(4));
        tooltip.setGraphicTextGap(0.0);
        tooltip.setShowDelay(Duration.ZERO);
        tooltip.showDurationProperty().set(Duration.hours(1));
        return tooltip;
    }

    @FXML
    protected void cancelButtonClicked() {
        sellScreenTable.getItems().removeAll(sellScreenTable.getItems());
        setLabelDisplay();
    }

    @FXML
    protected void productEntryLabelBarcodeKeyPressed(KeyEvent event) throws SQLException {
        byte[] enter = {13};

        if (!Arrays.toString(event.getCharacter().getBytes(StandardCharsets.UTF_8)).equals(Arrays.toString(enter))) {
            productEntryBarcodeContextMenu.hide();
            productEntryBarcodeContextMenu.getItems().clear();
            barcodeeverwritten = productEntryLabelBarcode.getText();
            setBarcodeContextMenu(matchedbarcodes(barcodeeverwritten));
        }
    }

    protected void setBarcodeContextMenu(ArrayList<String> matchedbarcodeslist) {
        matchedbarcodeslist.removeIf(String::isEmpty);
        if (matchedbarcodeslist.size() < 6 && matchedbarcodeslist.size() > 0) {
            for (String barcode : matchedbarcodeslist) {
                Label label = new Label(barcode);
                label.setPrefWidth(productEntryLabelBarcode.getWidth() - 13);
                CustomMenuItem menuItem = new CustomMenuItem(label, true);
                menuItem.setOnAction(actionEvent -> {
                    productEntryLabelBarcode.setText(label.getText());
                    final StockViewProduct[] products = new StockViewProduct[1];
                    ProductInteractions.createAllProducts().forEach(allProducts -> products[0] = (allProducts.getBarcode().getValue().equals(label.getText()) ? allProducts : products[0]));
                    productEntryLabelBarcode.setText(products[0].getBarcode().getValue());
                    productEntryLabelPrice.setText(String.valueOf(products[0].getUnitBuyPrice().getValue()));
                    productEntryLabelBrand.setText(products[0].getBrand().getValue());
                    productEntryLabelName.setText(products[0].getName().getValue());
                    productEntryLabelTax.setText(String.valueOf(products[0].getTax().getValue()));
                    productEntryLabelSellPrice.setText(String.valueOf(products[0].getUnitSellPrice().getValue()));
                    productEntryBarcodeContextMenu.hide();
                    List<TextField> productEntryLabelTextFields = Arrays.asList(productEntryLabelBarcode, productEntryLabelBrand, productEntryLabelName, productEntryLabelTax, productEntryLabelPiece, productEntryLabelBuyPrice, productEntryLabelSellPrice);
                    for (TextField t : productEntryLabelTextFields) {
                        t.getStyleClass().remove("error");
                    }
                });
                productEntryBarcodeContextMenu.getItems().add(menuItem);
            }
            productEntryBarcodeContextMenu.show(productEntryLabelBarcode, Side.BOTTOM, 0, 0);
        }
    }

    protected ArrayList<String> matchedbarcodes(String barcodeeverwritten) throws SQLException {
        ArrayList<String> barcodes = new ArrayList<>();
        int size = barcodeeverwritten.length();

        for (StockViewProduct allproducts : ProductInteractions.createAllProducts()) {
            if (allproducts.getBarcode().getValue().length() >= size) {
                if (allproducts.getBarcode().getValue().substring(0, size).equals(barcodeeverwritten)) {
                    barcodes.add(allproducts.getBarcode().getValue());
                }
            }
        }


        return barcodes;
    }

    @FXML
    protected void productEntryLabelButtonOnClicked() throws IOException, SQLException {
        HashMap<TextField, Boolean> textFields = new HashMap<>() {
        };
        textFields.put(productEntryLabelPiece, true);
        textFields.put(productEntryLabelTax, true);
        textFields.put(productEntryLabelBuyPrice, true);
        textFields.put(productEntryLabelSellPrice, true);
        textFields.put(productEntryLabelBrand, true);
        textFields.put(productEntryLabelName, true);
        textFields.put(productEntryLabelBarcode, true);
        for (TextField textField : textFields.keySet()) {
            if (!inputChecker(textField)) {
                textFields.put(textField, false);
                textField.getStyleClass().remove("error");
            }
        }
        if (!textFields.containsValue(false)) {
            ProcessLogs.recordStockEntryProcess(productEntryLabelBarcode.getText(), productEntryLabelName.getText(), productEntryLabelPiece.getText());
            ProductInteractions.productEntry(productEntryLabelBarcode.getText(), productEntryLabelBrand.getText(), productEntryLabelName.getText(), productEntryLabelPiece.getText(), productEntryLabelTax.getText(), productEntryLabelBuyPrice.getText(), String.format(Locale.ROOT, "%.2f", Double.parseDouble(productEntryLabelBuyPrice.getText()) / Double.parseDouble(productEntryLabelPiece.getText())), productEntryLabelSellPrice.getText());
            productEntryLabelBarcode.setText("");
            productEntryLabelSellPrice.setText("");
            productEntryLabelTax.setText("");
            productEntryLabelName.setText("");
            productEntryLabelBrand.setText("");
            productEntryLabelBuyPrice.setText("");
            productEntryLabelPiece.setText("");
            productEntryLabelPrice.setText("");
            productEntryTabCheckBox.setSelected(false);
            for (TextField txt : textFields.keySet()) {
                txt.setStyle(productEntryLabelPrice.getStyle());
            }

        } else {
            for (TextField txt : textFields.keySet()) {
                if (!textFields.get(txt)) {
                    txt.styleProperty().unbind();
                    txt.getStyleClass().add("error");
                } else {
                    txt.getStyleClass().remove("error");
                }
            }
        }

    }

    @FXML
    protected void barcodeFieldKeyTyped(KeyEvent keyEvent) {
        byte[] enter = {13};
        if (Arrays.toString(keyEvent.getCharacter().getBytes(StandardCharsets.UTF_8)).equals(Arrays.toString(enter))) {
            keyTypedAlgorithm(barcodeField.getText());
        }
    }

    @FXML
    protected void taxInputController() {
        inputController(productEntryLabelTax);
    }

    @FXML
    protected void buyPriceInputController() {
        inputController(productEntryLabelBuyPrice);
        buyPriceKeyPressed();
    }

    @FXML
    protected void sellPriceInputController() {
        inputController(productEntryLabelSellPrice);
    }

    @FXML
    protected void productAmountInputController() {
        productEntryLabelPiece.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^[0-9]*$")) {
                productEntryLabelPiece.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    @FXML
    protected void buyPriceKeyPressed() {
        try {
            productEntryLabelPrice.setText(String.format(Locale.ROOT, "%.2f", Double.parseDouble(productEntryLabelBuyPrice.getText()) / Double.parseDouble(productEntryLabelPiece.getText())));
        } catch (NumberFormatException e) {
            productEntryLabelPrice.setText("0");
        }
    }

    protected void changeActiveUser(Label label) {
        if (UserInteractions.user.isAdmin) {
            label.setText("Kullanıcı: " + UserInteractions.user.username + "    (Yönetici)");
        } else {
            label.setText("Kullanıcı: " + UserInteractions.user.username);
        }
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

    public void refreshSellScreenTableData() {
        for (SellScreenProduct pro : sellScreenTable.getItems()) {
            pro.controlDiscountFields();
        }
        amountCollumn.setCellValueFactory(productsTextFieldCellDataFeatures -> (productsTextFieldCellDataFeatures.getValue().getAmount()));
        discountCollumn.setCellValueFactory(productsTextFieldCellDataFeatures -> (productsTextFieldCellDataFeatures.getValue().getObservableDiscount()));
        percentageDiscountCollumn.setCellValueFactory(productsTextFieldCellDataFeatures -> (productsTextFieldCellDataFeatures.getValue().getObservableDiscountPercentage()));
        barcodeCollumn.setCellValueFactory(productsTextFieldCellDataFeatures -> (productsTextFieldCellDataFeatures.getValue().getBarcode()));
        nameCollumn.setCellValueFactory(productsTextFieldCellDataFeatures -> (productsTextFieldCellDataFeatures.getValue().getName()));
        kdvCollumn.setCellValueFactory(productsTextFieldCellDataFeatures -> (productsTextFieldCellDataFeatures.getValue().getTax()));
        lastPriceCollumn.setCellValueFactory(productsTextFieldCellDataFeatures -> (productsTextFieldCellDataFeatures.getValue().getObservablePrice()));
        sellScreenTable.refresh();
    }

    public ContextMenu setSellScreenContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem decreaseItem = new MenuItem("Azalt");
        decreaseItem.setOnAction(actionEvent -> {
            if (sellScreenTable.getSelectionModel().getSelectedItem().amount == 1) {
                sellScreenTable.getItems().remove(sellScreenTable.getSelectionModel().getSelectedItem());
            } else {
                sellScreenTable.getSelectionModel().getSelectedItem().amount--;
            }
            refreshSellScreenTableData();
            init();
            setLabelDisplay();

        });
        MenuItem removeItem = new MenuItem("Sil");
        removeItem.setOnAction(actionEvent -> {
            sellScreenTable.getItems().remove(sellScreenTable.getSelectionModel().getSelectedItem());
            if (sellScreenTable.getSelectionModel().getSelectedItem() != null) {
                sellScreenTable.getSelectionModel().getSelectedItem().controlDiscountFields();
            }
            refreshSellScreenTableData();
            init();
            setLabelDisplay();
        });
        contextMenu.getItems().add(decreaseItem);
        contextMenu.getItems().add(removeItem);
        return contextMenu;
    }

    @FXML
    public void onStockControlOpened() throws SQLException {
        if (tabpane.getSelectionModel().getSelectedItem().equals(stockControlTab)) {
            toggleSwitchStock.switchOnProperty().set(false);
            stockTable.getItems().removeAll(stockTable.getItems());
            stockCollumnBarcode.setCellValueFactory(allProductsStringCellDataFeatures -> allProductsStringCellDataFeatures.getValue().getBarcode());
            stockCollumnAmont.setCellValueFactory(allProductsStringCellDataFeatures -> allProductsStringCellDataFeatures.getValue().getAmount());
            stockCollumnBrand.setCellValueFactory(allProductsStringCellDataFeatures -> allProductsStringCellDataFeatures.getValue().getBrand());
            stockCollumnName.setCellValueFactory(allProductsStringCellDataFeatures -> allProductsStringCellDataFeatures.getValue().getName());
            stockCollumnTax.setCellValueFactory(allProductsStringCellDataFeatures -> allProductsStringCellDataFeatures.getValue().getTax());
            stockCollumnUnitBuy.setCellValueFactory(allProductsStringCellDataFeatures -> allProductsStringCellDataFeatures.getValue().getUnitBuyPrice());
            stockCollumnUnitSell.setCellValueFactory(allProductsStringCellDataFeatures -> allProductsStringCellDataFeatures.getValue().getUnitSellPrice());
            stockCollumnExpectedProfit.setCellValueFactory(allProductsStringCellDataFeatures -> allProductsStringCellDataFeatures.getValue().getProfit());
            stockCollumnAddList.setCellValueFactory(allProductsButtonCellDataFeatures -> allProductsButtonCellDataFeatures.getValue().getButton());
            stockTable.getItems().removeAll(stockTable.getItems());
            stockTable.getItems().addAll(ProductInteractions.createAllProducts());
        }
    }

    @FXML
    public void stockDisplayDoubleClick(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2 & stockTable.getSelectionModel().getSelectedItem() != null) {
            StockViewProduct selectedproduct = stockTable.getSelectionModel().getSelectedItem();
            tabpane.getSelectionModel().select(buyScreenTab);
            productEntryLabelBarcode.setText(selectedproduct.getBarcode().getValue());
            productEntryLabelPrice.setText(String.valueOf(selectedproduct.getUnitBuyPrice().getValue()));
            productEntryLabelBrand.setText(selectedproduct.getBrand().getValue());
            productEntryLabelName.setText(selectedproduct.getName().getValue());
            productEntryLabelTax.setText(String.valueOf(selectedproduct.getTax().getValue()));
            productEntryLabelSellPrice.setText(String.valueOf(selectedproduct.getUnitSellPrice().getValue()));
        }
    }

    private void keyTypedAlgorithm(String Barcode) {
        boolean find = false;
        SellScreenProduct productdb;
        if (sellScreenTable.getItems().size() > 0) {
            for (SellScreenProduct pro : sellScreenTable.getItems()) {
                if (Objects.equals(pro.barcode, Barcode)) {
                    pro.amount = pro.amount + amountInteger;
                    amountInteger = 1;
                    pro.sellPriceLabel.setText(String.valueOf((pro.unitSellPrice - Double.parseDouble(pro.discount.getText())) * pro.amount));
                    find = true;
                }
            }
        }
        if (!find) {
            try {
                productdb = ProductInteractions.getProduct(Barcode);
                productdb.amount = productdb.amount + amountInteger - 1;
                amountInteger = 1;
                sellScreenTable.getItems().addAll(productdb);
            } catch (NullPointerException e) {
                System.out.println("Bu barkod kayıtlı değil.");
            }
        }
        refreshSellScreenTableData();
        sellScreenTable.refresh();
        init();
        setLabelDisplay();
        barcodeField.setText("");
    }

    public void statisticsTabOpened() throws SQLException {
        dateLabel.setText(LocalDate.now().toString());
        if (tabpane.getSelectionModel().getSelectedItem().equals(statisticsTab)) {
            initPieData(pieToggleSwitch.switchOnProperty().getValue());
            initLineData(lineToggleSwitch.switchOnProperty().getValue());
        }
        tabpane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            if (oldTab.equals(statisticsTab)) {
                for (PieChart.Data data : pieChart.getData()) {
                    data.nameProperty().set("");
                }
            }
        });
    }

    @FXML
    public void decreaseMonthButtonAction() throws SQLException {
        LocalDate localDate = LocalDate.parse(dateLabel.getText());
        dateLabel.setText(String.valueOf(localDate.minusMonths(1)));
        initLineData(lineToggleSwitch.switchOnProperty().getValue());
        initPieData(pieToggleSwitch.switchOnProperty().getValue());
    }

    @FXML
    public void decreaseDayButtonAction() throws SQLException {
        LocalDate localDate = LocalDate.parse(dateLabel.getText());
        dateLabel.setText(String.valueOf(localDate.minusDays(1)));
        initLineData(lineToggleSwitch.switchOnProperty().getValue());
        initPieData(pieToggleSwitch.switchOnProperty().getValue());
    }

    @FXML
    public void increaseDayButtonAction() throws SQLException {
        LocalDate localDate = LocalDate.parse(dateLabel.getText());
        dateLabel.setText(String.valueOf(localDate.plusDays(1)));
        initLineData(lineToggleSwitch.switchOnProperty().getValue());
        initPieData(pieToggleSwitch.switchOnProperty().getValue());
    }

    @FXML
    public void increaseMonthButtonAction() throws SQLException {
        LocalDate localDate = LocalDate.parse(dateLabel.getText());
        dateLabel.setText(String.valueOf(localDate.plusMonths(1)));
        initLineData(lineToggleSwitch.switchOnProperty().getValue());
        initPieData(pieToggleSwitch.switchOnProperty().getValue());
    }

    public void initPieData(Boolean isdaily) throws SQLException {
        pieChart.getData().removeAll(pieChart.getData());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateOfLabel = LocalDate.parse(dateLabel.getText(), formatter);
        ArrayList<SalesObject> salesObjects;
        if (isdaily) {
            salesObjects = DBAccess.createSalesObjects("daily", dateOfLabel);
        } else {
            salesObjects = DBAccess.createSalesObjects("monthly", dateOfLabel);
        }
        if (salesObjects.size() > 15) {
            HashMap<String, Double> stats = Stats.createPieChartData(salesObjects).entrySet().stream().sorted((i2, i1) -> i1.getValue().compareTo(i2.getValue())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
            ArrayList<String> arrayListSales = new ArrayList<>(stats.keySet());
            ArrayList<String> stringsTop15 = new ArrayList<>(arrayListSales.subList(0, 14));
            double otherProfit = 0;
            for (String object : arrayListSales.subList(14, arrayListSales.size())) {
                otherProfit += stats.get(object);
            }
            HashMap<String, Double> returnHashMap = new HashMap<>();
            for (String s : stringsTop15) {
                returnHashMap.put(s, stats.get(s));
            }
            returnHashMap.put("Other", otherProfit);

            setPieChart(returnHashMap);
        } else {
            setPieChart(Stats.createPieChartData(salesObjects));

        }

    }

    public void initLineData(Boolean isMonthly) throws SQLException {
        stackedBarChart.getData().removeAll(stackedBarChart.getData());
        //LinkedHashMap<String, Double> datesAndProfits = DBAccess.barChartValues();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateOfLabel = LocalDate.parse(dateLabel.getText(), formatter);
        if (isMonthly) {
            ArrayList<SalesObject> salesObjects = DBAccess.createSalesObjects("monthly", dateOfLabel);
            setBarChart(Stats.createLineChartData(salesObjects, isMonthly));
        } else {
            //sql part to get sales records with matching YEAR values and put them into an ArrayList<SalesObject> salesObjects
            ArrayList<SalesObject> salesObjects = DBAccess.createSalesObjects("annual", dateOfLabel);
            setBarChart(Stats.createLineChartData(salesObjects, isMonthly));

        }


    }

    public void setPieChart(HashMap<String, Double> nameAndPrice) {
        for (String name : nameAndPrice.keySet()) {
            pieChart.getData().add(new PieChart.Data(name, nameAndPrice.get(name)));
        }
        Label pieInfo = new Label("");
        pieInfo.setTextFill(Color.BLACK);
        DoubleBinding total = Bindings.createDoubleBinding(() -> pieChart.getData().stream().mapToDouble(PieChart.Data::getPieValue).sum(), pieChart.getData());
        for (final PieChart.Data data : pieChart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_MOVED, e -> {
                pieInfo.setVisible(true);
                pieInfo.setTranslateX(e.getSceneX() + 5);
                pieInfo.setTranslateY(e.getSceneY() - 90);
                String text = String.format(Locale.ROOT, "%.1f%%", 100 * data.getPieValue() / total.get());
                pieInfo.setText(text + "\n" + data.getPieValue() + "TL");
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
        stackedBarChart.setLegendVisible(false);
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
            historyTableDate.setCellValueFactory(logObjectLocalDateCellDataFeatures -> logObjectLocalDateCellDataFeatures.getValue().getDateString());
            historyTableEmployee.setCellValueFactory(logObjectLocalDateCellDataFeatures -> logObjectLocalDateCellDataFeatures.getValue().getEmployee());
            historyTableType.setCellValueFactory(logObjectLocalDateCellDataFeatures -> logObjectLocalDateCellDataFeatures.getValue().getType());
            historyTableExplanation.setCellValueFactory(logObjectLocalDateCellDataFeatures -> logObjectLocalDateCellDataFeatures.getValue().getExplanation());
            historyTable.getItems().addAll(ProcessLogs.getLogObjects());
            Collections.reverse(historyTable.getItems());
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
                    if (item != null) {
                        switch (item.type) {
                            case "SATIŞ" -> setStyle("-fx-background-color: #b5d5b5");
                            case "İADE" -> setStyle("-fx-background-color: #b9878d");
                            case "STOK TANIMI" -> setStyle("-fx-background-color: #a2b2c0");
                            case "ÇIKIŞ" -> setStyle("-fx-background-color: #05f6ee");
                            default -> setStyle("");
                        }
                    } else {
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
    protected void onUserTabOpened() throws SQLException {
        if (tabpane.getSelectionModel().getSelectedItem().equals(userTab)) {
            userTable.getItems().removeAll(userTable.getItems());
            userTableUserName.setCellValueFactory(userCheckBoxCellDataFeatures -> userCheckBoxCellDataFeatures.getValue().getUserName());
            userTableEntryScreenTabPerm.setCellValueFactory(userCheckBoxCellDataFeatures -> userCheckBoxCellDataFeatures.getValue().getCheckBoxEntryScreenPerm());
            userTableHistoryScreenTabPerm.setCellValueFactory(userCheckBoxCellDataFeatures -> userCheckBoxCellDataFeatures.getValue().getCheckBoxHistoryScreenPerm());
            userTableSellScreenTabPerm.setCellValueFactory(userCheckBoxCellDataFeatures -> userCheckBoxCellDataFeatures.getValue().getCheckBoxSellScreenPerm());
            userTableStatisticsScreenTabPerm.setCellValueFactory(userCheckBoxCellDataFeatures -> userCheckBoxCellDataFeatures.getValue().getCheckBoxStatsScreenPerm());
            userTableStockControlScreenTabPerm.setCellValueFactory(userCheckBoxCellDataFeatures -> userCheckBoxCellDataFeatures.getValue().getCheckBoxTrackStockScreenPerm());
            userTableUsersScreenTabPerm.setCellValueFactory(userCheckBoxCellDataFeatures -> userCheckBoxCellDataFeatures.getValue().getCheckBoxUsersScreenPerm());
            ArrayList<User> users = UserInteractions.getAllUsers();
            if (!UserInteractions.user.isAdmin) {
                users.removeIf(user -> (user.usersScreenPerm && !(user.getUserName().getValue().equals(UserInteractions.user.getUserName().getValue()))));
            }
            for (User user : users) {
                if (user.getUserName().getValue().equals(UserInteractions.user.getUserName().getValue())) {
                    user.setNotEditable();
                }
            }
            userTable.getItems().addAll(users);
        }
    }

    @FXML
    protected void onRegister() throws IOException {
        Stage registerStage = new Stage();
        registerStage.setTitle("Kaydol");
        SignupScreen.username = "";
        FXMLLoader fxmlLoader = new FXMLLoader(LoginScreen.class.getResource("SignupScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 480, 360);
        ArrayList<Node> nodes = new ArrayList<>(scene.getRoot().getChildrenUnmodifiable());
        Button registerButton = (Button) nodes.get(4);
        SignupScreen signupScreenController = fxmlLoader.getController();
        signupScreenController.setFirstController(FirstOpenScreen.mainScreenLoader.getController());
        registerButton.addEventHandler(new EventType<ActionEvent>(), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    onUserTabOpened();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        registerStage.setAlwaysOnTop(true);
        Main.setGradient(registerStage, scene);
    }

    @FXML
    protected void removeUserButtonEvent() throws SQLException {
        if (userTable.getSelectionModel().getSelectedItem() != null && !userTable.getSelectionModel().getSelectedItem().getUserName().getValue().equals(UserInteractions.user.getUserName().getValue())) {
            UserInteractions.deleteUser(userTable.getSelectionModel().getSelectedItem().username);
            onUserTabOpened();
        }
    }

    @FXML
    protected void changePasswordButtonAction() throws IOException {
        if (userTable.getSelectionModel().getSelectedItem() != null) {
            Stage changePassStage = new Stage();
            changePassStage.setTitle("Şifre Değiştir");
            SignupScreen.username = userTable.getSelectionModel().getSelectedItem().username;
            FXMLLoader fxmlLoader = new FXMLLoader(LoginScreen.class.getResource("SignupScreen.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 480, 360);
            changePassStage.setAlwaysOnTop(true);
            Main.setGradient(changePassStage, scene);
            changePassStage.setOnCloseRequest(windowEvent -> {
                try {
                    onUserTabOpened();
                } catch (SQLException e) {
                    e.printStackTrace(System.out);
                }
            });
        }
    }

    @FXML
    protected void productsWithoutBarcodeButton() throws SQLException {

        Stage productsWithoutBarcodeStage = new Stage();
        productsWithoutBarcodeStage.setTitle("Barkodsuz Ürün Seçimi");
        GridPane gridPane = new GridPane();
        ArrayList<Product> allProductsArrayList = new ArrayList<>(DBAccess.fetchNonBarcodeProducts());
        int sqrt = (int) Math.sqrt(allProductsArrayList.size()) + 1;
        for (int i = 0; i < sqrt; i++) {
            for (int j = 0; j < sqrt; j++) {
                try {
                    Button product = new Button(allProductsArrayList.get(i * sqrt + (j)).brand);
                    product.setId(allProductsArrayList.get(i * sqrt + (j)).barcode);
                    product.setPrefHeight(30);
                    product.setPrefWidth(200);
                    GridPane.setConstraints(product, j, i);
                    gridPane.getChildren().add(product);
                    GridPane.setMargin(product, new Insets(10));
                    product.setOnAction(actionEvent -> keyTypedAlgorithm((product.getId())));
                } catch (IndexOutOfBoundsException ignored) {
                }
            }
        }
        gridPane.setStyle("-fx-background-color: 'src/main/resources/com/tempims/tempims/index.jpeg'");
        Scene productsWithoutBarcodeScene = new Scene(gridPane);
        Stop[] stops = new Stop[]{new Stop(0, Color.web("#9e899b")), new Stop(1, Color.GRAY)};
        LinearGradient lg1 = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
        productsWithoutBarcodeScene.setFill(lg1);
        productsWithoutBarcodeStage.setScene(productsWithoutBarcodeScene);
        productsWithoutBarcodeStage.setAlwaysOnTop(true);
        productsWithoutBarcodeStage.show();
    }

    public boolean inputChecker(TextField txtField) {
        return (!txtField.getText().equals("") && !txtField.getText().equals("."));
    }

    public void product_entry_name_action(KeyEvent actionEvent) {
        System.out.println("oldu");
        if (productEntryTabCheckBox.isSelected()){
            String name =productEntryLabelName.getText();
            productEntryLabelBarcode.setText(name);
            productEntryLabelBrand.setText(name);
        }
    }
}
