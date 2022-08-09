package com.tempims.tempims;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBAccess {
    private static Connection connect(){
        Connection conn = null;
        try{
            String url = "jdbc:sqlite:tempims.db";
            conn = DriverManager.getConnection(url); // Connects to database or creates one in the relative directory if it doesn't exist
            System.out.println("Connected successfully !");
            createUserTable(conn);
            return conn;
        }
        catch(SQLException e){
            e.printStackTrace(System.out);
        }
        return conn;
    }
    protected static void createUserTable(Connection conn){
        try{
            String sql = "CREATE TABLE IF NOT EXISTS USERS (\n"
                    +" USERNAME CHAR PRIMARY KEY,\n"
                    +" PASSWORD CHAR\n"
                    +");";
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        }
        catch(SQLException e){
            e.printStackTrace(System.out);
        }
    }
    protected static void insertUser(String userName, String password){
        try{
            Connection conn = connect();
            String sql = "INSERT INTO USERS (USERNAME, PASSWORD) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userName);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace(System.out);
        }

    }
    protected static String fetchPassword(String userName){
        try {
            Connection conn = connect();
            String sql = String.format("SELECT PASSWORD FROM USERS WHERE USERNAME = '%s'", userName);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            return rs.getString("PASSWORD");
        }
        catch(SQLException e){
            e.printStackTrace(System.out);
        }
        return null;
    }
    protected static int checkUsername(String userName){
        int size = 0;
        try{
            Connection conn = connect();
            String sql = String.format("SELECT * FROM USERS WHERE USERNAME = '%s'", userName);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if(rs != null){
                rs.last();
                size = rs.getRow();
                return size;
            }
        }
        catch(SQLException e){
            e.printStackTrace(System.out);
        }
        return size;
    }

}
