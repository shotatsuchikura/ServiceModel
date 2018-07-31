package main;

import java.util.Random;

public class Consumer{

  //消費者プレイヤのID
  private int consumer_id_;

  //消費者プレイヤのライフスタイル
  private int consumer_lifestyle_;

  //消費者プレイヤの利得表
  private int[] gain_table_;

  //消費者の利得(CS)
  private int consumer_satisfaction_;

  //従業員別の期待CSを格納する配列
  private int[] expect_cs_ = new int[2];

  //選択する従業員
  private int select_employee_;

  //消費者のサービス利用状況
  private boolean consumer_status_;

  //使用する乱数インスタンス
  Random rnd = new Random();

  //コンストラクタ
  public Consumer(int id, int lifestyle, int[][] gain_tables){
    consumer_id_ = id;
    consumer_lifestyle_ = lifestyle;
    gain_table_ = gain_tables[lifestyle];
  }

 //リセット
  public void resetConsumer(int num_employees){
    select_employee_ = num_employees;
    consumer_status_ = false;
    consumer_satisfaction_ = 0;
  }

  //各従業員を利用した際のCSの計算
  public void calcExpectCS(int[][] employee_resources){

    //期待CS配列の中身をリセット
    for(int i = 0; i < 2; i++){
      expect_cs_[i] = 0;
    }

    //従業員別に計算して格納
    for(int i = 0; i < 2; i++){
      for(int j = 0; j < 3; j++){
        if(employee_resources[i][j] == 2){ //スキルレベル２のとき
          expect_cs_[i] += gain_table_[j*2+1];
        }else if(employee_resources[i][j] == 1){ //スキルレベル１のとき
          expect_cs_[i] += gain_table_[j*2];
        }else{ //スキルなしのとき
          expect_cs_[i] += 0;
        }
      }
      // System.out.println(expect_cs_[i]);
    }
  }

  //サービス利用の意思決定
  public void consumerDecide(boolean[] employee_status, int price){

    // //利用従業員を初期化
    // select_employee_ = num_employees_; //この場合はサービスを利用しないことを示す

    //ある従業員を選択不可能な場合、期待CSを0にする
    for(int i = 0; i < 2; i++){
      if(employee_status[i] == false){
        expect_cs_[i] = 0;
      }
    }

    //期待CS及びpriceを比較し一番高いものを見つける、期待CSの場合はその従業員を利用し、priceの場合はサービスを利用しない
    if(expect_cs_[0] >= price && expect_cs_[1] >= price){
      if(expect_cs_[0] > expect_cs_[1]){
        select_employee_ = 0;
      }
      else if(expect_cs_[0] < expect_cs_[1]){
        select_employee_ = 1;
      }
      else if(expect_cs_[0] == expect_cs_[1]){
        int r = rnd.nextInt(2);
        if(r == 0){
          select_employee_ = 0;
        }
        else{
          select_employee_ = 1;
        }
      }
    }
    else if(expect_cs_[0] >= price && expect_cs_[1] < price){
      select_employee_ = 0;
    }
    else if(expect_cs_[0] < price && expect_cs_[1] >= price){
      select_employee_ = 1;
    }


    //このショットにおけるcsを計算
    if(select_employee_ == 2){
      // consumer_status_ = false;
      consumer_satisfaction_ = 0;
    } else {
      consumer_status_ = true;
      consumer_satisfaction_ = expect_cs_[select_employee_];
    }
  }

  //idを答える
  public int getConsumerID(){
    return consumer_id_;
  }

  //ライフスタイルを答える
  public int getLifestyle(){
    return consumer_lifestyle_;
  }

  //利得表を答える
  public int[] getGainTable(){
    return gain_table_;
  }

  //期待CSをこたえる
  public int[] getExpectCS(){
    return expect_cs_;
  }

  //CSを答える
  public int getCS(){
    return consumer_satisfaction_;
  }

  //サービスの利用状況を答える
  public boolean getConsumerStatus(){
    return consumer_status_;
  }

  //選択した従業員を答える
  public int getSelectEmployee(){
    return select_employee_;
  }

}