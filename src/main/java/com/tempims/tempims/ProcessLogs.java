package com.tempims.tempims;

import javafx.collections.ObservableList;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ProcessLogs {
   static LocalDate currentDate;

   public static void recordSalesProcess(ObservableList<Products> soldProducts, double totalSellPrice) throws IOException {
      FileWriter logsFile = new FileWriter("logs.txt", true);
      BufferedWriter writer = new BufferedWriter(logsFile);
      String midText = "";
      for (Products product : soldProducts){
         midText += product.amount + "x" + product.name + "/-";
      }
      writer.write("SATIŞ//" + java.time.LocalDate.now() + "||" +  java.time.LocalTime.now() + "//" + midText + "//" + totalSellPrice + "//" + UserInteractions.user.username + "\n");
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

   public static void setUpDate() throws IOException {
      checkDateFile();
      currentDate = java.time.LocalDate.now();
      BufferedReader reader = new BufferedReader(new FileReader("date.txt"));
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      LocalDate lastLoggedDate = LocalDate.parse(reader.readLine(), formatter);
      if (lastLoggedDate.getMonthValue() != currentDate.getMonthValue() || lastLoggedDate.getYear() != currentDate.getYear()){
         DBAccess.clearProfitTable();
      }

      if (!lastLoggedDate.toString().equals(currentDate.toString())){
         DBAccess.insertProfitRow(currentDate);
         //sql part to clear daily profit columns at "PRODUCTS" table
      }

      BufferedWriter writer = new BufferedWriter(new FileWriter("date.txt"));
      writer.write(currentDate.toString());
      writer.close();

   }
   public static ArrayList<LogObject> getLogObjects() throws IOException {
      ArrayList<LogObject> logObjects = new ArrayList<>();
      BufferedReader reader = new BufferedReader(new FileReader("logs.txt"));
      String line;
      while((line = reader.readLine()) != null){
         String[] splittedLine = line.split("//");
         if (splittedLine[0].equals("SATIŞ")){
            String detailsString = "Satış Detayı:\n\n";
            String[] productsString = splittedLine[2].split("/-");
            for(String productAndPrice: productsString){
               String[] productSplitted = productAndPrice.split("x");
               detailsString += productSplitted[0] + " adet " + productSplitted[1] +  "\n";
            }
            logObjects.add(new LogObject(splittedLine[1], splittedLine[0], "Satış Fiyatı:\t" + splittedLine[3], detailsString, splittedLine[4]));
         } else if(splittedLine[0].equals("STOK TANIMI")){
            String explanationString = splittedLine[4] + " adet " + splittedLine[3];
            logObjects.add(new LogObject(splittedLine[1], splittedLine[0], "Ürün İsmi:\t" + explanationString, "Barkod: " + splittedLine[2], splittedLine[5]));

         } else if (splittedLine[0].equals("GİRİŞ")){
            logObjects.add(new LogObject(splittedLine[1], splittedLine[0], "Kullanıcı adı:\t" + splittedLine[2], "", splittedLine[2]));
         } else if(splittedLine[0].equals("YENİ KAYIT")){
            logObjects.add(new LogObject(splittedLine[1], splittedLine[0], "Kullanıcı adı:\t" + splittedLine[2], "", splittedLine[2]));
         }
      }
      return logObjects;
   }

   public static void checkDateFile() throws IOException {
      File dateFile = new File("date.txt");
      if(dateFile.createNewFile()){
         BufferedWriter writer = new BufferedWriter(new FileWriter(dateFile));
         writer.write("1111-11-11");
         writer.close();
      }
   }
}
