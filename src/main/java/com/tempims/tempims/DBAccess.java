package com.tempims.tempims;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class DBAccess {
    private static Connection connect() {
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:tempims.db";
            conn = DriverManager.getConnection(url); // Connects to database or creates one in the relative directory if it doesn't exist
            System.out.println("Connected successfully !");
            createUserTable(conn);
            createProductTable(conn);
            createProfitTable(conn);
            return conn;
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
        return conn;
    }

    protected static void createUserTable(Connection conn) {
        try {
            String sql = "CREATE TABLE IF NOT EXISTS USERS (\n"
                    + " USERNAME CHAR(127) PRIMARY KEY,\n"
                    + " PASSWORD CHAR(127),\n"
                    + " PERMISSION CHAR(127)\n"
                    + ");";
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    protected static void createProductTable(Connection conn) {
        try {
            String sql = "CREATE TABLE IF NOT EXISTS PRODUCTS (\n"
                    + " BARCODE CHAR(127) PRIMARY KEY,\n"
                    + " BRAND CHAR(127),\n"
                    + " NAME CHAR(127),\n"
                    + " PRODUCT_NUMBER TINYINT(127),\n"
                    + " TAX TINYINT(127),\n"
                    + " UNIT_BUYING_PRICE DECIMAL(32,2),\n"
                    + " TOTAL_BUYING_PRICE DECIMAL(32,2),\n"
                    + " UNIT_SELLING_PRICE DECIMAL(32,2),\n"
                    + " PROFIT_RETURN DECIMAL(32,2),\n"
                    + " DAILY_PROFIT_RETURN DECIMAL(32,2)\n"
                    + ");";
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    protected static void createProfitTable(Connection conn) {
        try {
            String sql = "CREATE TABLE IF NOT EXISTS PROFITS (\n"
                    + " DAY CHAR(127),\n"
                    + " MONTH CHAR(127),\n"
                    + " YEAR CHAR(127),\n"
                    + " PRODUCT_BARCODE CHAR(127),\n"
                    + " PRODUCT_NAME CHAR(127),\n"
                    + " GROSS_PROFIT DECIMAL(32,2)\n"
                    + ");";
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    protected static void insertProduct(String barcodeInput, String brandInput, String nameInput, String numberInput, String taxInput, String totalBuyPriceInput, String unitBuyPriceInput, String sellPriceInput) {
        Connection conn = connect();
        try {
            String productCheck = String.format(Locale.ROOT,"SELECT * FROM PRODUCTS WHERE BARCODE = '%s'", barcodeInput);
            Statement checkStmt = conn.createStatement();
            ResultSet rs = checkStmt.executeQuery(productCheck);
            if (!rs.next()) { // Product doesn't exist, add new product to database
                String newProduct = "INSERT INTO PRODUCTS (BARCODE, BRAND, NAME, PRODUCT_NUMBER, TAX, UNIT_BUYING_PRICE, TOTAL_BUYING_PRICE, UNIT_SELLING_PRICE, PROFIT_RETURN, DAILY_PROFIT_RETURN) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 0, 0)";
                PreparedStatement pstmt = conn.prepareStatement(newProduct);
                pstmt.setString(1, barcodeInput);
                pstmt.setString(2, brandInput);
                pstmt.setString(3, nameInput);
                pstmt.setInt(4, Integer.parseInt(numberInput));
                pstmt.setInt(5, Integer.parseInt(taxInput));
                pstmt.setDouble(6, Double.parseDouble(unitBuyPriceInput));
                pstmt.setDouble(7, Double.parseDouble(totalBuyPriceInput));
                pstmt.setDouble(8, Double.parseDouble(sellPriceInput));
                pstmt.executeUpdate();
            }
            else{ // Product is already on the database, change the stock and price columns if necessary
                conn.close();
                int currentStock = getStock(barcodeInput);
                double currentAvgUnitBuyingPrice = fetchUnitBuyingPrice(barcodeInput);
                double avgUnitBuyingPrice = Stats.calculateAverageEntryPrice(currentStock,currentAvgUnitBuyingPrice,Integer.parseInt(numberInput),Double.parseDouble(unitBuyPriceInput));
                updatePriceInfo(avgUnitBuyingPrice,Double.parseDouble(sellPriceInput),Double.parseDouble(totalBuyPriceInput),barcodeInput);
                updateStock(barcodeInput, Integer.parseInt(numberInput));
            }

        } catch (SQLException e) {
            e.printStackTrace(System.out);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace(System.out);
            }
        }
    }

    protected static int getStock(String barcode) {
        Connection conn = connect();
        try {
            String getCurrentStock = String.format(Locale.ROOT,"SELECT PRODUCT_NUMBER FROM PRODUCTS WHERE BARCODE = '%s'", barcode);
            Statement currentStockStatement = conn.createStatement();
            ResultSet currentStockRS = currentStockStatement.executeQuery(getCurrentStock);
            return currentStockRS.getInt("PRODUCT_NUMBER");
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace(System.out);
            }
        }
        return 0;
    }

    protected static void updateStock(String barcode, int stockChange) {
        Connection conn = connect();
        try {
            int updatedStock = stockChange + getStock(barcode);
            String updateQuery = String.format(Locale.ROOT,"UPDATE PRODUCTS SET PRODUCT_NUMBER = '%d' WHERE BARCODE = '%s'", updatedStock, barcode);
            Statement updatedStockStatement = conn.createStatement();
            updatedStockStatement.executeUpdate(updateQuery);
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace(System.out);
            }
        }

    }

    protected static void insertUser(String userName, String password, String permissions) {
        Connection conn = connect();
        try {
            String sql = "INSERT INTO USERS (USERNAME, PASSWORD, PERMISSION) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userName);
            pstmt.setString(2, password);
            pstmt.setString(3, permissions);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace(System.out);
            }
        }
    }

    protected static String fetchPassword(String userName) {
        Connection conn = connect();
        try {
            String sql = String.format(Locale.ROOT,"SELECT PASSWORD FROM USERS WHERE USERNAME = '%s'", userName);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            String returnVal = rs.getString("PASSWORD");
            return returnVal;
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace(System.out);
            }
        }
        return null;
    }

    protected static int checkUsername(String userName) {
        int size = 0;
        Connection conn = connect();
        try {
            String sql = String.format(Locale.ROOT,"SELECT * FROM USERS WHERE USERNAME = '%s'", userName);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs != null) {
                rs.last();
                size = rs.getRow();
                return size;
            }
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace(System.out);
            }
        }
        return size;
    }

    protected static String newProductInfo(String barcode) {
        Connection conn = connect();
        try {
            String sql = String.format(Locale.ROOT,"SELECT BARCODE, BRAND, NAME, PRODUCT_NUMBER, TAX, UNIT_BUYING_PRICE, TOTAL_BUYING_PRICE, UNIT_SELLING_PRICE FROM PRODUCTS WHERE BARCODE = '%s'", barcode);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            String returnVal = String.format(Locale.ROOT,"%s:%s:%s:%d:%d:%,.2f:%.2f:%,.2f",rs.getString("BARCODE"), rs.getString("BRAND"), rs.getString("NAME"), rs.getInt("PRODUCT_NUMBER") ,rs.getInt("TAX"), rs.getDouble("UNIT_BUYING_PRICE"), rs.getDouble("TOTAL_BUYING_PRICE"), rs.getDouble("UNIT_SELLING_PRICE"));
            return returnVal;
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace(System.out);
            }
        }
        return null;
    }

    protected static ResultSet fetchProducts(String purpose){
        if(purpose.equals("stockView")){
            Connection connStock = connect();
            try{
                String sqlStock = "SELECT * FROM PRODUCTS";
                Statement stmtStock = connStock.createStatement();
                ResultSet rsStock = stmtStock.executeQuery(sqlStock);
                return rsStock;
            }
            catch (SQLException e) {
                e.printStackTrace(System.out);
            }
        }

        else if(purpose.equals("statView")){
            Connection connStat = connect();
            try{
                String sqlStat = "SELECT NAME, PROFIT_RETURN FROM PRODUCTS WHERE PROFIT_RETURN <> 0.00 ";
                Statement stmtStat = connStat.createStatement();
                ResultSet rsStat = stmtStat.executeQuery(sqlStat);
                return rsStat;
            }
            catch (SQLException e) {
                System.out.println("Problem 1");
                e.printStackTrace(System.out);
            }
        }
        return null;
    }

    protected static double fetchUnitBuyingPrice(String barcode){
        Connection conn = connect();
        try {
            String sql = String.format(Locale.ROOT,"SELECT UNIT_BUYING_PRICE FROM PRODUCTS WHERE BARCODE = '%s'", barcode);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            return rs.getDouble("UNIT_BUYING_PRICE");
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace(System.out);
            }
        }
        return 0;
    }

    protected static void amendProfit(String day, String month, String year, String barcode, String name, double newProfit){
        Connection conn = connect();
        try{
            //String sql = String.format(Locale.ROOT,"UPDATE PRODUCTS SET PROFIT_RETURN = PROFIT_RETURN + '%,.2f', DAILY_PROFIT_RETURN = DAILY_PROFIT_RETURN + '%,.2f' WHERE BARCODE = '%s'",newProfit,newProfit,barcode);
            String sql = "INSERT INTO PROFITS (DAY, MONTH, YEAR, PRODUCT_BARCODE, PRODUCT_NAME, GROSS_PROFIT) VALUES (?,?,?,?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, day); pstmt.setString(2, month); pstmt.setString(3, year);
            pstmt.setString(4, barcode); pstmt.setString(5, name); pstmt.setDouble(6,newProfit);
            pstmt.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace(System.out);
        }
        finally{
            try{
                conn.close();
            }
            catch (SQLException e){
                e.printStackTrace(System.out);
            }
        }
    }

    protected static void updateDailyProfit(LocalDate date, double newDailyProfit){
        Connection conn = connect();
        try{
            String sql = String.format(Locale.ROOT,"UPDATE PROFITS SET GROSS_PROFIT = GROSS_PROFIT + '%,.2f' WHERE DATE = '%s'",
                    newDailyProfit, date); // might create a problem later, should test and see (using date without parsing) !!!
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        }
        catch(SQLException e){
            e.printStackTrace(System.out);
        }
        finally{
            try{
                conn.close();
            }
            catch (SQLException e){
                e.printStackTrace(System.out);
            }
        }
    }

    protected static void updatePriceInfo(double newUnitPrice, double newSellingPrice, double newBuyingPrice, String barcode){
        Connection conn = connect();
        try{
            String sql = String.format(Locale.ROOT,"UPDATE PRODUCTS SET UNIT_BUYING_PRICE = '%,.2f', UNIT_SELLING_PRICE = '%,.2f', TOTAL_BUYING_PRICE = TOTAL_BUYING_PRICE + '%,.2f' WHERE BARCODE = '%s'",
                    newUnitPrice, newSellingPrice, newBuyingPrice, barcode);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        }
        catch(SQLException e){
            e.printStackTrace(System.out);
        }
        finally{
            try{
                conn.close();
            }
            catch(SQLException e){
                e.printStackTrace(System.out);
            }
        }
    }

    protected static LinkedHashMap<String, Double> barChartValues(){
        Connection conn = connect();
        LinkedHashMap<String, Double> returnVal = new LinkedHashMap<>();
        try{
            String sql = "SELECT * FROM PROFITS";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                returnVal.put(rs.getString("DATE"), rs.getDouble("GROSS_PROFIT"));
            }
            return returnVal;
        }
        catch(SQLException e){
            e.printStackTrace(System.out);
        }
        finally{
            try{
                conn.close();
            }
            catch(SQLException e){
                e.printStackTrace(System.out);
            }
        }
        return returnVal;
    }

    protected static void clearProfitTable(){
        Connection conn = connect();
        try{
            String sql = "DELETE FROM PROFITS";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        }
        catch(SQLException e){
            e.printStackTrace(System.out);
        }
        finally{
            try{
                conn.close();
            }
            catch(SQLException e){
                e.printStackTrace(System.out);
            }
        }
    }

    protected static void changePassword(String userName, String newPassword){
        Connection conn = connect();
        try{
            String sql = String.format(Locale.ROOT,"UPDATE USERS SET PASSWORD = '%s' WHERE USERNAME = '%s'",newPassword, userName);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        }
        catch(SQLException e){
            e.printStackTrace(System.out);
        }
        finally{
            try{
                conn.close();
            }
            catch(SQLException e){
                e.printStackTrace(System.out);
            }
        }
    }

    protected static void removeUser(String userName){
        Connection conn = connect();
        try{
            String sql = String.format(Locale.ROOT,"DELETE FROM USERS WHERE USERNAME = '%s'", userName);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        }
        catch(SQLException e){
            e.printStackTrace(System.out);
        }
        finally{
            try{
                conn.close();
            }
            catch(SQLException e){
                e.printStackTrace(System.out);
            }
        }
    }

    protected static ArrayList<User> fetchUsers(){
        ArrayList<User> userList = new ArrayList<>();
        Connection conn = connect();
        try{
            String sql = "SELECT USERNAME, PERMISSION FROM USERS";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                userList.add(new User(rs.getString("USERNAME"), rs.getString("PERMISSION")));
            }
            return userList;
        }
        catch(SQLException e){
            e.printStackTrace(System.out);
        }
        finally{
            try{
                conn.close();
            }
            catch(SQLException e){
                e.printStackTrace(System.out);
            }
        }
        return userList;
    }

    protected static String fetchUserPermission(String userName){
        Connection conn = connect();
        String userPermission = "";
        try{
            String sql = String.format(Locale.ROOT,"SELECT PERMISSION FROM USERS WHERE USERNAME = '%s'", userName);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            userPermission = rs.getString("PERMISSION");
            return userPermission;
        }
        catch(SQLException e){
            e.printStackTrace(System.out);
        }
        finally{
            try{
                conn.close();
            }
            catch(SQLException e){
                e.printStackTrace(System.out);
            }
        }
        return userPermission;
    }

    protected static void setUserPermission(String userName, String permission){
        Connection conn = connect();
        try{
            String sql = String.format(Locale.ROOT,"UPDATE USERS SET PERMISSION = '%s' WHERE USERNAME = '%s'",permission, userName);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        }
        catch(SQLException e){
            e.printStackTrace(System.out);
        }
        finally{
            try{
                conn.close();
            }
            catch(SQLException e){
                e.printStackTrace(System.out);
            }
        }

    }

    protected static void clearDailyProfits(){
        Connection conn = connect();
        try{
            String sql = String.format(Locale.ROOT,"UPDATE PRODUCTS SET DAILY_PROFIT_RETURN = '%.2f'",0.00);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        }
        catch(SQLException e){
            e.printStackTrace(System.out);
        }
        finally{
            try{
                conn.close();
            }
            catch(SQLException e){
                e.printStackTrace(System.out);
            }
        }
    }

    protected static void clearMonthlyProfits(){
        Connection conn = connect();
        try{
            String sql = String.format(Locale.ROOT,"UPDATE PRODUCTS SET PROFIT_RETURN = '%.2f'",0.00);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        }
        catch(SQLException e){
            e.printStackTrace(System.out);
        }
        finally{
            try{
                conn.close();
            }
            catch(SQLException e){
                e.printStackTrace(System.out);
            }
        }
    }

    protected static HashMap<String, Number> dailyPieChartInfo(){
        HashMap<String, Number> info = new HashMap<>();
        Connection conn = connect();
        try{
            String sql = "SELECT NAME, DAILY_PROFIT_RETURN FROM PRODUCTS";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                info.put(rs.getString("NAME"), rs.getDouble("DAILY_PROFIT_RETURN"));
            }
            return info;

        }
        catch(SQLException e){
            e.printStackTrace(System.out);
        }
        finally{
            try{
                conn.close();
            }
            catch(SQLException e){
                e.printStackTrace(System.out);
            }
        }
        return info;
    }

    protected static ArrayList<SalesObject> createSalesObjects(String queryFilter, LocalDate date){
        ArrayList<SalesObject> salesObjects = new ArrayList<>();
        Connection conn = connect();
        try{
            ResultSet rs = null;
            if(queryFilter.equals("daily")){
                String sql = String.format(Locale.ROOT,"SELECT PRODUCT_NAME, GROSS_PROFIT FROM PROFITS WHERE DAY = '%s' AND MONTH = '%s' AND YEAR = '%s'",
                        String.valueOf(date.getDayOfMonth()), String.valueOf(date.getMonthValue()), String.valueOf(date.getYear()));
                Statement stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);
            }
            else if(queryFilter.equals("monthly")){
                String sql = String.format(Locale.ROOT,"SELECT PRODUCT_NAME, GROSS_PROFIT FROM PROFITS WHERE MONTH = '%s' AND YEAR = '%s'",
                        String.valueOf(date.getMonthValue()), String.valueOf(date.getYear()));
                Statement stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);
            }
            else if(queryFilter.equals("annual")){
                String sql = String.format(Locale.ROOT,"SELECT PRODUCT_NAME, GROSS_PROFIT FROM PROFITS WHERE YEAR = '%s'",
                        String.valueOf(date.getYear()));
                Statement stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);
            }
            while(rs.next()){
                salesObjects.add(new SalesObject(date, rs.getString("PRODUCT_NAME"), rs.getDouble("GROSS_PROFIT")));
            }
            return salesObjects;
        }
        catch(SQLException e){
            e.printStackTrace(System.out);
        }
        finally{
            try{
                conn.close();
            }
            catch(SQLException e){
                e.printStackTrace(System.out);
            }
        }
        return salesObjects;
    }
}