package com.tempims.tempims;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ProcessLogs {
   static LocalDate currentDate = java.time.LocalDate.now();

   public static boolean checkLogsFile() throws IOException {
      File logFile = new File("logs.txt");
      return logFile.createNewFile();
   }

   public static void recordRevenueToBuffer(double totalSells) throws IOException {
      FileWriter revenueFile = new FileWriter("lastRevenue.txt", false);
      BufferedWriter writer = new BufferedWriter(revenueFile);
      writer.write("CİRO//" + java.time.LocalDate.now() + "||" +  java.time.LocalTime.now() + "//" + totalSells + "//" + UserInteractions.user.username + "\n");
      writer.close();
   }

   public static void recordTotalSalesOnExit() throws IOException{
      BufferedReader reader = new BufferedReader(new FileReader("lastRevenue.txt"));
      String lastRevenueLogText = reader.readLine();
      if(lastRevenueLogText == null || !lastRevenueLogText.equals("")) {
         FileWriter logsFile = new FileWriter("logs.txt", true);
         BufferedWriter writer = new BufferedWriter(logsFile);
         FileWriter revenueFileWriter = new FileWriter("lastRevenue.txt", false);
         BufferedWriter writer2 = new BufferedWriter(revenueFileWriter);
         writer2.write("");
         writer.write(lastRevenueLogText + "\n");
         writer.close();
         writer2.close();
      }
   }

   public static void recordSalesProcess(String midText, double totalSellPrice) throws IOException {
      FileWriter logsFile = new FileWriter("logs.txt", true);
      BufferedWriter writer = new BufferedWriter(logsFile);
      writer.write("SATIŞ//" + java.time.LocalDate.now() + "||" +  java.time.LocalTime.now() + "//" + midText + "//" + totalSellPrice + "//" + UserInteractions.user.username + "\n");
      writer.close();

   }

   public static void recordReturnProcess(String midText, double totalSellPrice) throws IOException {
      FileWriter logsFile = new FileWriter("logs.txt", true);
      BufferedWriter writer = new BufferedWriter(logsFile);
      writer.write("İADE//" + java.time.LocalDate.now() + "||" +  java.time.LocalTime.now() + "//" + midText + "//" + totalSellPrice + "//" + UserInteractions.user.username + "\n");
      writer.close();

   }

   public static void recordUserProcess(int flag, String username) throws IOException {
      FileWriter logsFile = new FileWriter("logs.txt", true);
      BufferedWriter writer = new BufferedWriter(logsFile);
      if (flag == 1){
         writer.write("GİRİŞ//" + java.time.LocalDate.now() + "||" + java.time.LocalTime.now() + "//" + username + "\n");
      } else{
         writer.write("YENİ KAYIT//" + java.time.LocalDate.now() + "||" + java.time.LocalTime.now() + "//" + username + "\n");
      }
      writer.close();
   }
   public static void recordStockEntryProcess(String barcode, String name, String amount) throws IOException {
      FileWriter logsFile = new FileWriter("logs.txt", true);
      BufferedWriter writer = new BufferedWriter(logsFile);
      writer.write("STOK TANIMI//" + java.time.LocalDate.now() + "||" + java.time.LocalTime.now() + "//" + barcode + "//" + name + "//" + amount + "//" + UserInteractions.user.username + "\n");
      writer.close();


   }

   public static ArrayList<LogObject> getLogObjects(String minLimitOfDate, String maxLimitOfDate, String type) throws IOException {
      ArrayList<LogObject> logObjects = new ArrayList<>();
      BufferedReader reader = new BufferedReader(new FileReader("logs.txt"));
      String line;
      while((line = reader.readLine()) != null){
         String[] splittedLine = line.split("//");
         if (splittedLine[0].equals("SATIŞ")) {
            String detailsString = "Satış Detayı:\n\n";
            String[] productsString = splittedLine[2].split("/-");
            for (String productAndPrice : productsString) {
               String[] productSplitted = productAndPrice.split("x");
               detailsString += productSplitted[0] + " adet " + productSplitted[1] + "\n";
            }if(splittedLine[1].compareTo(minLimitOfDate) > 0 && splittedLine[1].compareTo(maxLimitOfDate) < 0) {
               if(type != null && type.equals(splittedLine[0])) {
                  logObjects.add(new LogObject(splittedLine[1], splittedLine[0], "Satış Fiyatı:\t" + splittedLine[3], detailsString, splittedLine[4]));
               } else if(type == null || type.equals("TÜMÜ")){
                  logObjects.add(new LogObject(splittedLine[1], splittedLine[0], "Satış Fiyatı:\t" + splittedLine[3], detailsString, splittedLine[4]));
               }
            }
         }else if (splittedLine[0].equals("İADE")){
               String detailsString = "İade Detayı:\n\n";
               String[] productsString = splittedLine[2].split("/-");
               for(String productAndPrice: productsString){
                  String[] productSplitted = productAndPrice.split("x");
                  detailsString += productSplitted[0] + " adet " + productSplitted[1] +  "\n";
               }
            if(splittedLine[1].compareTo(minLimitOfDate) > 0 && splittedLine[1].compareTo(maxLimitOfDate) < 0) {
               if(type != null && type.equals(splittedLine[0])) {
                  logObjects.add(new LogObject(splittedLine[1], splittedLine[0], "İade Fiyatı:\t" + splittedLine[3], detailsString, splittedLine[4]));
               } else if(type == null || type.equals("TÜMÜ")){
                  logObjects.add(new LogObject(splittedLine[1], splittedLine[0], "İade Fiyatı:\t" + splittedLine[3], detailsString, splittedLine[4]));
               }
            }

         } else if(splittedLine[0].equals("STOK TANIMI")){
            String explanationString = splittedLine[4] + " adet " + splittedLine[3];
            if(splittedLine[1].compareTo(minLimitOfDate) > 0 && splittedLine[1].compareTo(maxLimitOfDate) < 0) {
               if(type != null && type.equals(splittedLine[0])) {
                  logObjects.add(new LogObject(splittedLine[1], splittedLine[0], "Ürün İsmi:\t" + explanationString, "Barkod: \n" + splittedLine[2], splittedLine[5]));
               } else if(type == null || type.equals("TÜMÜ")){
                  logObjects.add(new LogObject(splittedLine[1], splittedLine[0], "Ürün İsmi:\t" + explanationString, "Barkod: \n" + splittedLine[2], splittedLine[5]));
               }
            }
         } else if (splittedLine[0].equals("GİRİŞ")){
            if(splittedLine[1].compareTo(minLimitOfDate) > 0 && splittedLine[1].compareTo(maxLimitOfDate) < 0) {
               if(type != null && type.equals(splittedLine[0])) {
                  logObjects.add(new LogObject(splittedLine[1], splittedLine[0], "Kullanıcı adı:\t" + splittedLine[2], "", splittedLine[2]));
               } else if(type == null || type.equals("TÜMÜ")){
                  logObjects.add(new LogObject(splittedLine[1], splittedLine[0], "Kullanıcı adı:\t" + splittedLine[2], "", splittedLine[2]));
               }
            }
         } else if(splittedLine[0].equals("YENİ KAYIT")){
            if(splittedLine[1].compareTo(minLimitOfDate) > 0 && splittedLine[1].compareTo(maxLimitOfDate) < 0) {
               if(type != null && type.equals(splittedLine[0])) {
                  logObjects.add(new LogObject(splittedLine[1], splittedLine[0], "Kullanıcı adı:\t" + splittedLine[2], "", splittedLine[2]));
               } else if(type == null || type.equals("TÜMÜ")){
                  logObjects.add(new LogObject(splittedLine[1], splittedLine[0], "Kullanıcı adı:\t" + splittedLine[2], "", splittedLine[2]));
               }
            }
         } else if(splittedLine[0].equals("CİRO")){
            if(splittedLine[1].compareTo(minLimitOfDate) > 0 && splittedLine[1].compareTo(maxLimitOfDate) < 0) {
               if(type != null && type.equals(splittedLine[0])) {
                  logObjects.add(new LogObject(splittedLine[1], splittedLine[0], "Toplam Satış:\t" + splittedLine[2], "", splittedLine[3]));
               } else if(type == null || type.equals("TÜMÜ")){
                  logObjects.add(new LogObject(splittedLine[1], splittedLine[0], "Toplam Satış:\t" + splittedLine[2], "", splittedLine[3]));
               }
            }
         }
      }
      return logObjects;
   }
}
