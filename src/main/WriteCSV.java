package main;

//csvに結果を書き出す用のクラス

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class WriteCSV{
public void writeResult(int time, int step, int price, int salary, int company_profit, int employee1_profit, int employee2_profit, int[] consumer_profit, int[] employee1_resources, int[] employee2_resources, int[] employee1_clients, int[] employee2_clients, int social_profit, int first_price, int first_salary){
  String filename_ = String.format("result_price%d_salary%d.csv", first_price, first_salary);
  try{
    File csv = new File(filename_);
    BufferedWriter bw = new BufferedWriter(new FileWriter(csv,true));
    bw.newLine();
    bw.write(time + "," + step + "," + price + "," + salary + "," +company_profit + "," + employee1_profit + "," + employee1_resources[0] + ", " + employee1_resources[1] + "," + employee1_resources[2] + "," + employee1_clients[0] + "," + employee1_clients[1] + "," + employee1_clients[2] + "," + employee1_clients[3] + "," + employee2_profit + "," + employee2_resources[0] + "," +employee2_resources[1] + "," + employee2_resources[2] + "," + employee2_clients[0] + "," + employee2_clients[1] + "," + employee2_clients[2] + "," + employee2_clients[3] + "," + consumer_profit[0] + "," +  consumer_profit[1] + "," +consumer_profit[2] + "," + consumer_profit[3] + "," + social_profit);//書き出す物をかく（引数に書く）
    bw.close();
  }catch (FileNotFoundException e) {
    // Fileオブジェクト生成時の例外捕捉
    e.printStackTrace();
  } catch (IOException e) {
    // BufferedWriterオブジェクトのクローズ時の例外捕捉
    e.printStackTrace();
  }
}
}