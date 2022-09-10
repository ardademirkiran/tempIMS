package com.tempims.tempims;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ObjectProperty;
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
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    static String companyName;
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

    public static void setLabelDisplay() {
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
        totalPriceLabelstatic.setText(String.format(Locale.ROOT, "%.2f", totalprice));
        subTotalLabelstatic.setText(String.format(Locale.ROOT, "%.2f", subtotal));
        totalDiscountLabelstatic.setText(String.format(Locale.ROOT, "%.2f", totaldiscount));
        totalTaxLabelstatic.setText(String.format(Locale.ROOT, "%.2f", totalprice - subtotal));
    }

    @FXML
    protected void sellButtonClicked() throws IOException {
        String totalpricelabeltext = totalPriceLabel.getText();
        if (sellScreenTable.getItems().size() > 0) {
            Products[] productsList = sellScreenTable.getItems().toArray(new Products[0]);
            cancelButtonClicked();
            double totalProfit = 0;
            String midText = "";
            for (Products product : productsList) {
                double profitToAdd = Stats.calculateProfit(product);
                DBAccess.updateStock(product.barcode, -product.amount);
                DBAccess.amendProfit(product.barcode, profitToAdd);
                midText += product.amount + "x" + product.name + "/-";
                totalProfit += profitToAdd;
            }
            ProcessLogs.recordSalesProcess(midText, Double.parseDouble(totalpricelabeltext));
            DBAccess.updateDailyProfit(ProcessLogs.currentDate, totalProfit);

        }
    }

    @FXML
    protected void returnButtonClicked() throws IOException {
        String totalpricelabeltext = totalPriceLabel.getText();
        if (sellScreenTable.getItems().size() > 0) {
            Products[] productsList = sellScreenTable.getItems().toArray(new Products[0]);
            cancelButtonClicked();
            String midText = "";
            for (Products product : productsList) {
                double profitToAdd = Stats.calculateProfit(product);
                DBAccess.updateStock(product.barcode, product.amount);
                DBAccess.amendProfit(product.barcode, -profitToAdd);
                midText += product.amount + "x" + product.name + "/-";
            }
            ProcessLogs.recordReturnProcess(midText, Double.parseDouble(totalpricelabeltext));
        }
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
    public void initialize() {
        // FIXME: 9.09.2022 
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

        setContextMenu(); //sellscreentable contextmenu
        ContextMenu contextMenu = setContextMenu();
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

        Main.globalStage.addEventHandler(KeyEvent.KEY_TYPED, this::sellScreenKeyTyped); //sell screen key event handler

        stockTable.getItems().removeAll(stockTable.getItems()); // stock control switch and addlist
        stockTable.getItems().addAll(ProductInteractions.createAllProducts());
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

        pieToggleSwitch.switchOnProperty().addListener((a, b, c) -> { // pieChart initializations
            if (c) {
                initPieData(pieToggleSwitch.switchOnProperty().getValue());
            } else {
                initPieData(pieToggleSwitch.switchOnProperty().getValue());
            }
        });
        pieToggleSwitch.setMaxWidth(400);
        pieToggleSwitch.setMaxHeight(40);
        pieToggleSwitch.setAlignment(Pos.CENTER);
        GridPane.setHalignment(pieToggleSwitch, HPos.CENTER);
        statisticsGridPane.add(pieToggleSwitch, 0, 1, 1, 1);

        lineToggleSwitch.switchOnProperty().addListener((a, b, c) -> { // lineChart initializations
            if (c) {
                System.out.println("aylık");
                initLineData(lineToggleSwitch.switchOnProperty().getValue());
            } else {
                initLineData(lineToggleSwitch.switchOnProperty().getValue());
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
        ProductInteractions.productEntry(productEntryLabelBarcode.getText(), productEntryLabelBrand.getText(), productEntryLabelName.getText(), productEntryLabelPiece.getText(), productEntryLabelTax.getText(), productEntryLabelBuyPrice.getText(), String.format(Locale.ROOT, "%.2f", Double.parseDouble(productEntryLabelBuyPrice.getText()) / Double.parseDouble(productEntryLabelPiece.getText())), productEntryLabelSellPrice.getText());
        productEntryLabelBarcode.setText("");
        productEntryLabelSellPrice.setText("");
        productEntryLabelTax.setText("");
        productEntryLabelName.setText("");
        productEntryLabelBrand.setText("");
        productEntryLabelBuyPrice.setText("");
        productEntryLabelPiece.setText("");
        productEntryLabelPrice.setText("");
    }

    @FXML
    protected void barcodeFieldKeyTyped(KeyEvent keyEvent) {
        byte[] enter = {13};
        if (Arrays.toString(keyEvent.getCharacter().getBytes(StandardCharsets.UTF_8)).equals(Arrays.toString(enter))) {
            keyTypedAlgorithm();
        }
    }


    static void inputController(TextField selectedTextField) {
        selectedTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("^[0-9.]*$")) {
                if (newValue.matches("^[0-9]+\\.([0-9]{0,2})")) {
                    System.out.println("input doğru ve noktalı");

                } else if (newValue.matches("^[0-9]*\\.([0-9]*)\\.*") && selectedTextField.getText().length() >= 2) {
                    System.out.println("Harf silinecek");
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

    public void refreshTableData() {
        for (Products pro : sellScreenTable.getItems()) {
            pro.init();
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

    public ContextMenu setContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem decreaseItem = new MenuItem("Azalt");
        decreaseItem.setOnAction(actionEvent -> {
            if (sellScreenTable.getSelectionModel().getSelectedItem().amount == 1) {
                sellScreenTable.getItems().remove(sellScreenTable.getSelectionModel().getSelectedItem());
            } else {
                sellScreenTable.getSelectionModel().getSelectedItem().amount--;
            }
            refreshTableData();
            init();
            setLabelDisplay();

        });
        MenuItem removeItem = new MenuItem("Sil");
        removeItem.setOnAction(actionEvent -> {
            sellScreenTable.getItems().remove(sellScreenTable.getSelectionModel().getSelectedItem());
            if (sellScreenTable.getSelectionModel().getSelectedItem() != null) {
                sellScreenTable.getSelectionModel().getSelectedItem().init();
            }
            refreshTableData();
            init();
            setLabelDisplay();
        });
        contextMenu.getItems().add(decreaseItem);
        contextMenu.getItems().add(removeItem);
        return contextMenu;
    }

    @FXML
    public void onStockControlOpened() {
        if (tabpane.getSelectionModel().getSelectedItem().equals(stockControlTab)) {
            toggleSwitchStock.switchOnProperty().set(false);
            stockTable.getItems().removeAll(stockTable.getItems());
            stockCollumnBarcode.setCellValueFactory(allProductsStringCellDataFeatures -> allProductsStringCellDataFeatures.getValue().getbarcode());
            stockCollumnAmont.setCellValueFactory(allProductsStringCellDataFeatures -> allProductsStringCellDataFeatures.getValue().getamont());
            stockCollumnBrand.setCellValueFactory(allProductsStringCellDataFeatures -> allProductsStringCellDataFeatures.getValue().getbrand());
            stockCollumnName.setCellValueFactory(allProductsStringCellDataFeatures -> allProductsStringCellDataFeatures.getValue().getname());
            stockCollumnTax.setCellValueFactory(allProductsStringCellDataFeatures -> allProductsStringCellDataFeatures.getValue().gettax());
            stockCollumnUnitBuy.setCellValueFactory(allProductsStringCellDataFeatures -> allProductsStringCellDataFeatures.getValue().getunitbuy());
            stockCollumnUnitSell.setCellValueFactory(allProductsStringCellDataFeatures -> allProductsStringCellDataFeatures.getValue().getunitsell());
            stockCollumnExpectedProfit.setCellValueFactory(allProductsStringCellDataFeatures -> allProductsStringCellDataFeatures.getValue().getprofit());
            stockCollumnAddList.setCellValueFactory(allProductsButtonCellDataFeatures -> allProductsButtonCellDataFeatures.getValue().getButton());
            stockTable.getItems().removeAll(stockTable.getItems());
            stockTable.getItems().addAll(ProductInteractions.createAllProducts());
        }
    }

    @FXML
    public void stockDisplayDoubleClick(MouseEvent mouseEvent) {
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
        boolean find = false;
        Products productdb;
        if (sellScreenTable.getItems().size() > 0) {
            for (Products pro : sellScreenTable.getItems()) {
                if (Objects.equals(pro.barcode, barcodeField.getText())) {
                    pro.amount++;
                    pro.sellPriceLabel.setText(String.valueOf((pro.unitSellPrice - Double.parseDouble(pro.discount.getText())) * pro.amount));
                    find = true;
                }
            }
        }
        if (!find) {
            try {
                productdb = ProductInteractions.getProduct(barcodeField.getText());
                sellScreenTable.getItems().addAll(productdb);
            } catch (NullPointerException e) {
                System.out.println("Bu barkod kayıtlı değil.");
            }
        }
        refreshTableData();
        sellScreenTable.refresh();
        init();
        setLabelDisplay();
        barcodeField.setText("");
    }

    public void statisticsTabOpened() {
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
    public void decreaseMonthButtonAction() {
        LocalDate localDate = LocalDate.parse(dateLabel.getText());
        dateLabel.setText(String.valueOf(localDate.minusMonths(1)));
    }

    @FXML
    public void decreaseDayButtonAction() {
        LocalDate localDate = LocalDate.parse(dateLabel.getText());
        dateLabel.setText(String.valueOf(localDate.minusDays(1)));
    }

    @FXML
    public void increaseDayButtonAction() {
        LocalDate localDate = LocalDate.parse(dateLabel.getText());
        dateLabel.setText(String.valueOf(localDate.plusDays(1)));
    }

    @FXML
    public void increaseMonthButtonAction() {
        LocalDate localDate = LocalDate.parse(dateLabel.getText());
        dateLabel.setText(String.valueOf(localDate.plusMonths(1)));
    }

    public void initPieData(Boolean isdaily) {
        pieChart.getData().removeAll(pieChart.getData());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateOfLabel = LocalDate.parse(dateLabel.getText(), formatter);
        if (isdaily) {
            //dateOfLabel.getDayOfMonth();
            //dateOfLabel.getMonthValue();
            //dateOfLabel.getYear();

            //sql part to get sales records with matching day, month, year values with dateOfLabel and put them into an ArrayList<SalesObject> salesObjects
            //setPieChart(Stats.createPieChartData(salesObjects));
            setPieChart(Stats.createDailyPieChartInfo());
        } else {
            //sql part to get sales records with matching month, year values with dateOfLabel and put them into an ArrayList<SalesObject> salesObjects
            //setPieChart(Stats.createPieChartData(salesObjects));
            setPieChart(Stats.createMonthlyPieChartInfo());
        }
    }

    public void initLineData(Boolean isMonthly) {
        stackedBarChart.getData().removeAll(stackedBarChart.getData());
        LinkedHashMap<String, Double> datesAndProfits = DBAccess.barChartValues();
        if (isMonthly) {
            //sql part to get sales records with matching month, year values and put them into an ArrayList<SalesObject> salesObjects
            //setBarChart(Stats.createLineChartData(salesObjects, isMonthly));
            setBarChart(datesAndProfits);
        } else {
            //sql part to get sales records with matching year values and put them into an ArrayList<SalesObject> salesObjects
            //setBarChart(Stats.createLineChartData(salesObjects, isMonthly));
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
                String text = String.format(Locale.ROOT, "%.1f%%", 100 * data.getPieValue() / total.get());
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
                            case "SATIŞ" -> setStyle("-fx-background-color: #79cc79");
                            case "İADE" -> setStyle("-fx-background-color: #e13359");
                            case "STOK TANIMI" -> setStyle("-fx-background-color: #71b6e5");
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
    protected void onUserTabOpened() {
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
        registerStage.setAlwaysOnTop(true);
        Main.setGradient(registerStage, scene);
        registerStage.setOnCloseRequest(windowEvent -> onUserTabOpened());
    }

    @FXML
    protected void removeUserButtonEvent() {
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
            changePassStage.setOnCloseRequest(windowEvent -> onUserTabOpened());
        }
    }

    @FXML
    protected void productsWithoutBarcodeButton() {
        // TODO: BUNA FİKİR LAZIM NASI YAPACAZ OLM

    }
}
