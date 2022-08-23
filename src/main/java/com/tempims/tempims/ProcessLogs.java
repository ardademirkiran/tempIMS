package com.tempims.tempims;

import javafx.collections.ObservableList;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ProcessLogs {

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

   public static void recordDaily(){
      //if db doesn't include Stats.currentDate then insert it with a "0" profit value
      DBAccess.insertProfitRow(Stats.currentDate);
      // else do nothing
   }
}
