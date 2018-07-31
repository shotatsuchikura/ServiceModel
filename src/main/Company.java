package main;

//企業プレイヤクラス
public class Company{

//企業のID
private int company_id_;

//企業の利得
private int company_profit_;

//従業員の給料
private int salary_;

//サービス価格
private int price_;

//コンストラクタ
public Company(int id){
  company_id_ = id;
}

//リセット
public void resetCompany(){
  company_profit_ = 0;
}

//企業の利得を計算する
public void calcCompanyProfit(int price, int num_user, int salary, int num_employees){
  company_profit_ = price * num_user - salary * num_employees;
}

//給料・価格をセットする
public void setSalaryPrice(int price, int salary){
  salary_ = salary;
  price_ = price;
}

//企業のIDを答える
public int getCompanyID(){
  return company_id_;
}

//利得を答える
public int getCompanyProfit(){
  return company_profit_;
}

//給料を答える
public int getSalary(){
  return salary_;
}

//サービス価格を答える
public int getPrice(){
  return price_;
}


}
