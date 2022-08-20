package com.tempims.tempims;

import java.sql.*;

public class DBAccess {
    private static Connection connect() {
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:tempims.db";
            conn = DriverManager.getConnection(url); // Connects to database or creates one in the relative directory if it doesn't exist
            System.out.println("Connected successfully !");
            createUserTable(conn);
            createProductTable(conn);
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
                    + " PASSWORD CHAR(127)\n"
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
                    + " UNIT_PRICE DECIMAL(32,2),\n"
                    + " BUYING_PRICE DECIMAL(32,2),\n"
                    + " SELLING_PRICE DECIMAL(32,2),\n"
                    + " PROFIT DECIMAL(32,2)\n"
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
            String productCheck = String.format("SELECT * FROM PRODUCTS WHERE BARCODE = '%s'", barcodeInput);
            Statement checkStmt = conn.createStatement();
            ResultSet rs = checkStmt.executeQuery(productCheck);
            if (!rs.next()) { // Product doesn't exist, add new product to database
                String newProduct = "INSERT INTO PRODUCTS (BARCODE, BRAND, NAME, PRODUCT_NUMBER, TAX, UNIT_PRICE, BUYING_PRICE, SELLING_PRICE, PROFIT) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 0)";
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
            /*else{ // Product is already on the database, change the stock
                updateStock(barcodeInput, Integer.parseInt(numberInput));
            }*/

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
            String getCurrentStock = String.format("SELECT PRODUCT_NUMBER FROM PRODUCTS WHERE BARCODE = '%s'", barcode);
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
            String updateQuery = String.format("UPDATE PRODUCTS SET PRODUCT_NUMBER = '%d' WHERE BARCODE = '%s'", updatedStock, barcode);
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

    protected static void insertUser(String userName, String password) {
        Connection conn = connect();
        try {
            String sql = "INSERT INTO USERS (USERNAME, PASSWORD) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userName);
            pstmt.setString(2, password);
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
            String sql = String.format("SELECT PASSWORD FROM USERS WHERE USERNAME = '%s'", userName);
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
            String sql = String.format("SELECT * FROM USERS WHERE USERNAME = '%s'", userName);
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
            String sql = String.format("SELECT NAME, TAX, SELLING_PRICE FROM PRODUCTS WHERE BARCODE = '%s'", barcode);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            String returnVal = String.format("%s:%d:%,.2f", rs.getString("NAME"), rs.getInt("TAX"), rs.getDouble("SELLING_PRICE"));
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
        Connection conn = connect();
        if(purpose.equals("stockView")){
            try{
                String sql = "SELECT * FROM PRODUCTS";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                return rs;
            }
            catch (SQLException e) {
                e.printStackTrace(System.out);
            }
            finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace(System.out);
                }
            }
        }

        else if(purpose.equals("statView")){
            try{
                String sql = "SELECT NAME, PROFIT FROM PRODUCTS WHERE PROFIT <> 0.00 ";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                return rs;
            }
            catch (SQLException e) {
                e.printStackTrace(System.out);
            }
            finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace(System.out);
                }
            }
        }
        return null;
    }

    protected static double fetchUnitPrice(String barcode){
        Connection conn = connect();
        try {
            String sql = String.format("SELECT UNIT_PRICE FROM PRODUCTS WHERE BARCODE = '%s'", barcode);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            return rs.getDouble("UNIT_PRICE");
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

    protected static void amendProfit(String barcode, double newProfit){
        Connection conn = connect();
        try{
            String sql = String.format("UPDATE PRODUCTS SET PROFIT = PROFIT + '%,.2f' WHERE BARCODE = '%s'",newProfit,barcode);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
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
}