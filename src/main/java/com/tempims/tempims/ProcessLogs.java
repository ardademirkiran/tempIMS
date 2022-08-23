package com.tempims.tempims;

import javafx.collections.ObservableList;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ProcessLogs {
   static LocalDate currentDate;

   public static void recordSalesProcess(ObservableList<Products> soldProducts, double totalSellPrice) throws IOException {
      FileWriter logsFile = new FileWriter("logs.txt", true);
      BufferedWriter writer = new BufferedWriter(logsFile);
      String midText = "|| ";
      for (Products product : soldProducts){
         midText += product.amount + " adet " + product.name + " || ";
      }
      writer.write("[" + java.time.LocalDate.now() + "||" + java.time.LocalTime.now() + "] [SATIŞ] " + midText + " Fiyat: " + totalSellPrice  + " | Personel: " + Session.username + "\n");
      writer.close();

   }

   public static void recordUserProcess(int flag, String username) throws IOException {
      FileWriter logsFile = new FileWriter("logs.txt", true);
      BufferedWriter writer = new BufferedWriter(logsFile);
      if (flag == 1){
         writer.write("[" + java.time.LocalDate.now() + "||" + java.time.LocalTime.now() + "] " + "[GİRİŞ] Kullanıcı adı: "+ username + "\n");
      } else{
         writer.write("[" + java.time.LocalDate.now() + "||" + java.time.LocalTime.now() + "] [YENİ KAYIT] Kullanıcı adı: " + username + "\n");
      }
      writer.close();
   }
   public static void recordStockEntryProcess(String barcode, String name, String amount) throws IOException {
      FileWriter logsFile = new FileWriter("logs.txt", true);
      BufferedWriter writer = new BufferedWriter(logsFile);
      writer.write("[" + java.time.LocalDate.now() + "||" + java.time.LocalTime.now() + "] " + "[STOK TANIMI] Barkod: " + barcode + " | Ürün adı: " + name + " | Adet: " + amount + " | Personel: " + Session.username +"\n");
      writer.close();


   }

   public static void setUpDate() throws IOException {
      currentDate = java.time.LocalDate.now();
      BufferedReader reader = new BufferedReader(new FileReader("date.txt"));
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      LocalDate lastLoggedDate = LocalDate.parse(reader.readLine(), formatter);
      if (!lastLoggedDate.toString().equals(currentDate.toString())){
         DBAccess.insertProfitRow(currentDate);
      }

      if (lastLoggedDate.getMonthValue() != currentDate.getMonthValue() || lastLoggedDate.getYear() != currentDate.getYear()){
         //sql part to update to 0(zero) all of profit values from PROFITS table  
      }

      BufferedWriter writer = new BufferedWriter(new FileWriter("date.txt"));
      writer.write(currentDate.toString());
      writer.close();

   }
}
