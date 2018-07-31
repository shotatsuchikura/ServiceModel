package main;

import java.util.Random;

//従業員クラス
public class Employee{

////使用する乱数インスタンス
Random rnd = new Random();

//従業員のID
private int employee_id_;

//従業員の利得(ES)
private int employee_satisfaction_;

//従業員の獲得資源
private int[] resources_ = new int[3];

//従業員が獲得できる資源の数
private int num_resources_can_get_;

//従業員が獲得している資源の数
private int num_resources_got_;

//従業員の担当消費者
private int[] clients_ = new int[40];

//従業員がサービス提供可能かいなか
private boolean employee_status_;

//kyapa
private int employee_capacity_;

//従業員の担当消費者数
private int num_clients_;

//担当顧客のタイプ
private int clients_lifestyle_[] = new int[4];

//トータルの合計
private int total_clients_lifestyle_[] = new int[4];

//メイン顧客のライフスタイル
private int client_main_lifestyle_;

//コンストラクタ
public Employee(int id){
  employee_id_ = id;
  employee_status_ = true;
}

public void checkClientLifestyle(int workday, int num_lifestyles){
  if(workday == 0){
    // int r = rnd.nextInt(4);
    // client_main_lifestyle_ = r;
    client_main_lifestyle_ = 0;
  }else{
    for(int i = 0; i < num_lifestyles; i++){
      if(clients_lifestyle_[i] > clients_lifestyle_[client_main_lifestyle_]){
        client_main_lifestyle_ = i;
      }
      else if(clients_lifestyle_[i] == clients_lifestyle_[client_main_lifestyle_]){
        int r = rnd.nextInt(2);
        if(r == 0){
          client_main_lifestyle_ = i;
        }
      }
    }
  }
}

public void resetEmployeeStatus(){
  employee_status_ = true;
}

public void resetEmployee(){
  employee_status_ = true;
  employee_satisfaction_ = 0;
  num_clients_ = 0;
  for(int i = 0; i < 40; i++){
    clients_[i] = 0;
  }
  for(int i = 0; i < 4; i++){
    clients_lifestyle_[i] = 0;
  }
  num_resources_got_ = 0;
}

public void changeResource(int salary, int price, int effort, int min_salary){
  num_resources_can_get_ = (salary - min_salary) / effort; //資源獲得可能数を求める
  employee_satisfaction_ = salary;
  //資源獲得可能数が0のとき
  //自明なのでやる必要ない
  if(num_resources_can_get_ == 0){ 
    resources_[0] = 1;
    resources_[1] = 0;
    resources_[2] = 0;
    employee_capacity_ = 20;
  }
  //資源獲得可能１のとき
  else if(num_resources_can_get_ == 1){
    if(client_main_lifestyle_ == 0){
      resources_[0] = 1;
      resources_[1] = 0;
      resources_[2] = 1;
      employee_satisfaction_ -= effort;
      employee_capacity_ = 12;
    }else if(client_main_lifestyle_ == 1 || client_main_lifestyle_ == 2){
      resources_[0] = 2;
      resources_[1] = 0;
      resources_[2] = 0;
      employee_satisfaction_ -= effort;
      employee_capacity_ = 12;
    }else if(client_main_lifestyle_ == 3){
      resources_[0] = 1;
      resources_[1] = 1;
      resources_[2] = 0;
      employee_satisfaction_ -= effort;
      employee_capacity_ = 12;
    }else{
      resources_[0] = 1;
      resources_[1] = 0;
      resources_[2] = 0;
      employee_capacity_ = 20;
    }
  }
  //資源獲得可能2のとき
  else if(num_resources_can_get_ == 2){
    if(client_main_lifestyle_ == 0){
      resources_[0] = 1;
      resources_[1] = 0;
      resources_[2] = 2;
      employee_satisfaction_ -= (2 * effort);
      employee_capacity_ = 8;
    }else if(client_main_lifestyle_ == 1){
      resources_[0] = 2;
      resources_[1] = 0;
      resources_[2] = 1;
      employee_satisfaction_ -= (2 * effort);
      employee_capacity_ = 8;
    }else if(client_main_lifestyle_ == 2){
      resources_[0] = 2;
      resources_[1] = 1;
      resources_[2] = 0;
      employee_satisfaction_ -= (2 * effort);
      employee_capacity_ = 8;
    }else if(client_main_lifestyle_ == 3){
      resources_[0] = 1;
      resources_[1] = 2;
      resources_[2] = 0;
      employee_satisfaction_ -= (2 * effort);
      employee_capacity_ = 8;
    }else{
      resources_[0] = 1;
      resources_[1] = 0;
      resources_[2] = 0;
      employee_capacity_ = 20;
    }
  }
  //資源３のとき
  else if(num_resources_can_get_ == 3){
    if(client_main_lifestyle_ == 0){
      int r = rnd.nextInt(2);
      if(r==0){
        resources_[0] = 2;
        resources_[1] = 0;
      }else{
        resources_[0] = 1;
        resources_[1] = 1;
      }
      resources_[2] = 2;
      employee_satisfaction_ -= (3 * effort);
      employee_capacity_ = 6;
    }else if(client_main_lifestyle_ == 1){
      resources_[0] = 2;
      resources_[1] = 0;
      resources_[2] = 2;
      employee_satisfaction_ -= (3 * effort);
      employee_capacity_ = 6;
    }else if(client_main_lifestyle_ == 2){
      resources_[0] = 2;
      resources_[1] = 2;
      resources_[2] = 0;
      employee_satisfaction_ -= (3 * effort);
      employee_capacity_ = 6;
    }else if(client_main_lifestyle_ == 3){
      int r = rnd.nextInt(2);
      if(r==0){
        resources_[0] = 2;
        resources_[2] = 0;
      }else{
        resources_[0] = 1;
        resources_[2] = 1;
      }
      resources_[1] = 2;
      employee_satisfaction_ -= (3 * effort);
      employee_capacity_ = 6;
    }else{
      resources_[0] = 1;
      resources_[1] = 0;
      resources_[2] = 0;
      employee_capacity_ = 20;
    }
  }
  //資源４のとき
  else if(num_resources_can_get_ == 4){
    if(client_main_lifestyle_ == 0 ){
      int r = rnd.nextInt(2);
      if(r==0){
        resources_[0] = 2;
        resources_[1] = 1;
      }else{
        resources_[0] = 1;
        resources_[1] = 2;
      }
      resources_[2] = 2;
      employee_satisfaction_ -= (4 * effort);
      employee_capacity_ = 5;
    }else if(client_main_lifestyle_ == 1 ){
      resources_[0] = 2;
      resources_[1] = 0;
      resources_[2] = 2;
      employee_satisfaction_ -= (3 * effort);
      employee_capacity_ = 6;
    }else if(client_main_lifestyle_ == 2 ){
      resources_[0] = 2;
      resources_[1] = 2;
      resources_[2] = 0;
      employee_satisfaction_ -= (3 * effort);
      employee_capacity_ = 6;
    }else if(client_main_lifestyle_ == 3 ){
      int r = rnd.nextInt(2);
      if(r==0){
        resources_[0] = 2;
        resources_[2] = 1;
      }else{
        resources_[0] = 1;
        resources_[2] = 2;
      }
      resources_[1] = 2;
      employee_satisfaction_ -= (4 * effort);
      employee_capacity_ = 5;
    }else{
      resources_[0] = 1;
      resources_[1] = 0;
      resources_[2] = 0;
      employee_capacity_ = 20;
    }
  }
  //資源５（以上）のとき
  else if(num_resources_can_get_ >= 5){
    if(client_main_lifestyle_ == 0 ){
      resources_[0] = 2;
      resources_[1] = 2;
      resources_[2] = 2;
      employee_satisfaction_ -= (5 * effort);
      employee_capacity_ = 4;
    }else if(client_main_lifestyle_ == 1 ){
      resources_[0] = 2;
      resources_[1] = 2;
      resources_[2] = 2;
      employee_satisfaction_ -= (5 * effort);
      employee_capacity_ = 4;
    }else if(client_main_lifestyle_ == 2 ){
      resources_[0] = 2;
      resources_[1] = 2;
      resources_[2] = 2;
      employee_satisfaction_ -= (5 * effort);
      employee_capacity_ = 4;
    }else if(client_main_lifestyle_ == 3 ){
      resources_[0] = 2;
      resources_[1] = 2;
      resources_[2] = 2;
      employee_satisfaction_ -= (5 * effort);
      employee_capacity_ = 4;
    }else{
      resources_[0] = 1;
      resources_[1] = 0;
      resources_[2] = 0;
      employee_capacity_ = 20;
    }
  }
  
}

public int[] getResources(){
  return resources_;
}

public void heirCut(int client_id, Consumer[] consumer, int[] num_each_consumer_){
  clients_[client_id] = 1;
  if(client_id <= num_each_consumer_[0]-1){
    clients_lifestyle_[0] += 1;
    // total_clients_lifestyle_[0] += 1;
  }
  else if(client_id >= num_each_consumer_[0] && client_id <= num_each_consumer_[1]-1){
    clients_lifestyle_[1] += 1;
    // total_clients_lifestyle_[1] += 1;
  }
  else if(client_id >= num_each_consumer_[1] && client_id <= num_each_consumer_[2]-1){
    clients_lifestyle_[2] += 1;
    // total_clients_lifestyle_[2] += 1;
  }
  else{
    clients_lifestyle_[3] += 1;
    // total_clients_lifestyle_[3] += 1;
  }
  num_clients_ += 1;
  if(num_clients_ == employee_capacity_){
    employee_status_ = false;
  }
  employee_satisfaction_ += consumer[client_id].getCS() / 10;
}

//担当消費者を答える
public int[] getClients(){
  return clients_;
}

//ESを答える
public int getES(){
  return employee_satisfaction_;
}

//担当顧客のライススタイルごとの数をこたえる
public int[] getClientsLifestyle(){
  return clients_lifestyle_;
}

//トータルの担当顧客タイプ数を答える
public int[] getTotalClientLifestyle(){
  return total_clients_lifestyle_;
}

//サービス提供可能か答える
public boolean getEmployeeStatus(){
  return employee_status_;
}

//担当している消費者数を答える
public int getNumClients(){
  return num_clients_;
}

public int getMainClientLifestyle(){
return client_main_lifestyle_;
}

}
