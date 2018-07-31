package main;

import java.util.Random;
import java.util.*;

public class Game2{
  public static void main(String[] args){

    //各種パラメータの設定（ここだけ変更すれば良い形にする）--------------------------------------------------------------------

    //企業プレイヤーの数
    int num_company_ = 1;
    //従業員プレイヤーの数
    int num_employees_ = 2;
    //消費者プレイヤーの数
    int num_consumers_ = 40;

    //企業プレイヤーのパラメータ
    int first_salary_ = 15000; //給与
    int first_price_ = 5000; //価格

    //従業員プレイヤーのパラメータ
    int num_resources_ = 3; //資源の種類
    int worktime_ = 600; //労働可能時間（1日に働く分数）
    int[] duration_ = {30,50,70,90,110,130}; //資源の個数ごとにかかる施術時間
    int effort_ = 1000; //スキルを１段階獲得・維持するために必要なコスト（1スキルあたり1日30分程度の訓練が必要と想定）
    int min_salary_ = 10000; //従業員の最低日給水準（これを下回るようなスキル獲得はしない）

    //消費者プレイヤーのパラメータ
    int num_lifestyles_ = 4; //ライフスタイルの種類
    int[][] gain_tables_ = { //ライフスタイルごとの利得表
                            {1000, 2000, 1000, 2000, 2000, 3000}, //①アクティブ消費派
                            {1000, 3000, 0, 1000, 1000, 2000}, //②生活不安消費派
                            {1000, 3000, 1000, 2000, 0, 1000}, //③堅実消費派
                            {1000, 2000, 2000, 4000, 1000, 2000}  //④こだわり消費派
                           };
    int[] num_each_lifestyles_ = {10,20,30,40}; //各ライフスタイルの人数は10人ずつ

    //その他設定
    int workdays_ = 100; //労働日数
    int times_ = 30; //同じ設定でシミュレーションを回す回数
    Random rnd = new Random(); //使用する乱数インスタンス
    WriteCSV wc = new WriteCSV(); //書き出す用のインスタンス

    //パラメータ設定終了---------------------------------------------------------------------------------------------------

    //エージェント作成----------------------------------------------------------------------------------------------------
    Company[] company_ = new Company[num_company_];
    for(int i = 0; i < num_company_; i++){
      company_[i] = new Company(i);
    }
    Employee[] employee_ = new Employee[num_employees_];
    for(int i = 0; i < num_employees_; i++){
      employee_[i] = new Employee(i);
    }
    Consumer[] consumer_ = new Consumer[num_consumers_];
    int lifestyle = 0;
    for(int i = 0; i < num_consumers_; i++){ //ライフスタイルの異なる４種類の消費者プレイヤを作成
      if(i < num_each_lifestyles_[lifestyle]){
        consumer_[i] = new Consumer(i, lifestyle, gain_tables_);
      }
      else if(i == num_each_lifestyles_[lifestyle]){
        lifestyle += 1;
        consumer_[i] = new Consumer(i, lifestyle, gain_tables_);
      }
    }
    //------------------------------------------------------------------------------------------------------------------

    //その他変数箱づくり
    int[][] employee_resources_ = new int[num_employees_][num_resources_]; //資源マトリックス
    boolean status_ = true; //サービス利用可能かどうか
    int num_false_employee_ = 0; //サービス提供不可能な従業員の数
    boolean[] employees_status_ = {true, true}; //従業員1,2がサービス提供可能か否か
    int salary_minus_; //給料−
    int salary_plus_; //給料＋
    int price_minus_; //価格-
    int price_plus_; //価格＋
    int num_user_; //サービス利用者の数
    int[] expect_company_profit_ = new int[5]; //企業の利得予想に使う箱
    int best_tactics_; //企業の戦略の箱
    int tmp_salary_; //一時的に給料を格納
    int tmp_price_; //一時的に価格を格納
    int social_profit_; //社会余剰
    //消費者のライフスタイルごとの利得
    int[] consumer_profit_ = {0, 0, 0, 0};
    //0~79のランダムな数列をつくる
    List<Integer> consumer_list_ = new ArrayList<Integer>();
    for(int i = 0; i < num_consumers_; i++){
      consumer_list_.add(i);
    }

    //timesループ--------------------------------------------------------------------------------------------------------
    for(int time_ = 0; time_ < times_; time_++){
      //workdaysループ---------------------------------------------------------------------------------------------------
      for(int workday_ = 0; workday_ < workdays_; workday_++){
        //各エージェントのパラメータをリセット
        //リセット前に顧客のメインライフスタイルを知る
        for(int i = 0; i < num_employees_; i++){
          employee_[i].checkClientLifestyle(workday_, num_lifestyles_);
          // System.out.println("初期:"+employee_[i].getMainClientLifestyle());
        }
        //企業のリセット
        for(int i = 0; i < num_company_; i++){
          company_[i].resetCompany();
        }
        //従業員のリセット
        for(int i = 0; i < num_employees_; i++){
          employee_[i].resetEmployee();
        }
        //消費者のリセット
        for(int i = 0; i < num_consumers_; i++){
          consumer_[i].resetConsumer(num_employees_);
        }
        //消費者の来店順番をシャッフル
        Collections.shuffle(consumer_list_); //workday_の間一定にすることで、企業の利得予想を正確にする

        //社会総余剰のリセット
        social_profit_ = 0;


        //サービス価格と給与の決定
        if(workday_ == 0){ //【1回目】サービス価格と給与に初期値を代入する
          for(int i = 0; i < num_company_; i++){
            company_[i].setSalaryPrice(first_price_, first_salary_);
          }
        }else{ //【２回目以降】サービス価格と給与を変更する
          //５種類の条件をすべて計算して、選択する処理
          //tactics1:現状維持
          //tactics2:価格up
          //tactics3:価格down
          //tactics4:給与up
          //tactics5:給与down
          for(int tactics_ = 0; tactics_ < 5; tactics_++){ //5つの戦略比較
            // for(int i = 0; i < num_employees_; i++){
            //   System.out.println(employee_[i].getMainClientLifestyle());
            // }
            //価格と給与セット
            if (tactics_ == 0){
              tmp_price_ = company_[0].getPrice();
              tmp_salary_ = company_[0].getSalary();
            }else if(tactics_ == 1){
              tmp_price_ = company_[0].getPrice() + 1000;
              tmp_salary_ = company_[0].getSalary();
            }else if(tactics_ == 2){
              tmp_price_ = company_[0].getPrice() - 1000;
              tmp_salary_ = company_[0].getSalary();
            }else if(tactics_ == 3){
              tmp_price_ = company_[0].getPrice();
              tmp_salary_ = company_[0].getSalary() + 1000;
            }else{
              tmp_price_ = company_[0].getPrice();
              tmp_salary_ = company_[0].getSalary() - 1000;
            }

            // System.out.println(tmp_price_);

            //従業員ステータスのリセット
            for(int j = 0; j < num_employees_; j++){
              employee_[j].resetEmployee();
              employees_status_[j] = employee_[j].getEmployeeStatus();
            }

            for(int i = 0; i < num_consumers_; i++){
              consumer_[i].resetConsumer(num_employees_);
            }

            //資源を獲得する
            for(int i = 0; i < num_employees_; i++){
              employee_[i].changeResource(tmp_salary_, tmp_price_, effort_, min_salary_);
              // System.out.println(effort_);
              // System.out.println(min_salary_);
            }
            //資源マトリックスを作成
            for(int i = 0; i < num_employees_; i++){
              employee_resources_[i]= employee_[i].getResources();
            }
            //各消費者が予想CSを計算
            for(int i = 0; i < num_consumers_; i++){
              consumer_[i].calcExpectCS(employee_resources_);
              // System.out.println(consumer_[i].getExpectCS()[0]);
            }
            

            //消費者の意思決定
            for(int i = 0; i < num_consumers_; i++){
              for(int j = 0; j < num_employees_; j++){
                if(employee_[j].getEmployeeStatus() == true){
                  status_ = true;
                }
                else{
                  num_false_employee_ += 1;
                  employees_status_[j] = false;
                  // System.out.println("従業員"+j+"さんはサービスの提供を終了しました");
                }
              }
              if(num_false_employee_ == num_employees_){ //従業員が全員falseならサービス終了
                status_ = false;
              }
              num_false_employee_ = 0; //リセット
              // System.out.println(i+"番目のお客さんが来る際、サービスを提供できるか？"+status_);
              if(status_ == false){ //サービス利用不可なら終了
                // System.out.println("サービスは終了しました");
                break;
              }
              else{
                for(int k = 0; k < num_consumers_; k++){
                  if(consumer_[k].getConsumerID() == consumer_list_.get(i)){ //消費者IDと順番IDが一緒なら消費者が意思決定
                    // System.out.println(employees_status_[0]);
                    // System.out.println(employees_status_[1]);
                    // System.out.println(tmp_price_);
                    consumer_[k].consumerDecide(employees_status_, tmp_price_);
                    // System.out.println("IDが"+k+"のお客さんの選択した従業員は"+consumer_[k].getSelectEmployee());

                    for(int j = 0; j < num_employees_; j++){//選択された従業員がいれば、髪をきる
                      if(j == consumer_[k].getSelectEmployee()){
                        employee_[j].heirCut(consumer_[k].getConsumerID(), consumer_, num_each_lifestyles_);
                      }
                    }


                  }
                }
              }
            }

            //サービス利用者のカウント
            num_user_ = 0;
            for(int i = 0; i < num_employees_; i++){
              for(int j = 0; j < num_consumers_; j++){
                if(employee_[i].getClients()[j] == 1){
                  num_user_ += 1;
                }
              }
            }
            // System.out.println(num_user_);

            //企業の利得計算
            for(int i = 0; i < num_company_; i++){
              company_[i].calcCompanyProfit(tmp_price_, num_user_, tmp_salary_, num_employees_);
            }
            //期待利得配列に格納
            expect_company_profit_[tactics_] = company_[0].getCompanyProfit();
            // System.out.println(expect_company_profit_[tactics_]);
          }
          best_tactics_ = 0; //現状を一番いいと仮定
          for(int tactics_ = 3; tactics_ < 5; tactics_++){
            if(tactics_ == 4 && company_[0].getSalary() == min_salary_){
              break;
            }
            if(expect_company_profit_[tactics_] > expect_company_profit_[best_tactics_]){
              best_tactics_ = tactics_;
            }
          }
          // System.out.println(best_tactics_);
          if(best_tactics_ == 1){
            company_[0].setSalaryPrice(company_[0].getPrice() + 1000, company_[0].getSalary());
          }else if(best_tactics_ == 2){
            company_[0].setSalaryPrice(company_[0].getPrice() - 1000, company_[0].getSalary());
          }else if(best_tactics_ == 3){
            company_[0].setSalaryPrice(company_[0].getPrice(), company_[0].getSalary() + 1000);
          }else if(best_tactics_ == 4){
            company_[0].setSalaryPrice(company_[0].getPrice(), company_[0].getSalary() - 1000);
          }
        }


        // System.out.println(company_[0].getSalary());

        //従業員ステータスのリセット
        for(int j = 0; j < num_employees_; j++){
          employee_[j].resetEmployee();
          employees_status_[j] = employee_[j].getEmployeeStatus();
        }

        for(int i = 0; i < num_consumers_; i++){
          consumer_[i].resetConsumer(num_employees_);
        }

        //資源を獲得する
        for(int i = 0; i < num_employees_; i++){
          employee_[i].changeResource(company_[0].getSalary(), company_[0].getPrice(), effort_, min_salary_);
          // System.out.println(effort_);
          // System.out.println(min_salary_);
        }
        // System.out.println(employee_[0].getES());

        //資源マトリックスを作成
        for(int i = 0; i < num_employees_; i++){
          employee_resources_[i]= employee_[i].getResources();
        }

        //各消費者が予想CSを計算
        for(int i = 0; i < num_consumers_; i++){
          consumer_[i].calcExpectCS(employee_resources_);
        }

        //消費者の意思決定
        for(int i = 0; i < num_consumers_; i++){
          for(int j = 0; j < num_employees_; j++){
            if(employee_[j].getEmployeeStatus() == true){
              status_ = true;
            }else{
              num_false_employee_ += 1;
              employees_status_[j] = false;
              // System.out.println("従業員"+j+"さんはサービスの提供を終了しました");
            }
          }
          if(num_false_employee_ == num_employees_){ //従業員が全員falseならサービス終了
            status_ = false;
          }

          num_false_employee_ = 0; //リセット
          // System.out.println(i+"番目のお客さんが来る際、サービスを提供できるか？"+status_);
          if(status_ == false){ //サービス利用不可なら終了
            // System.out.println("サービスは終了しました");
            break;
          }else{
            for(int k = 0; k < num_consumers_; k++){
              if(consumer_[k].getConsumerID() == consumer_list_.get(i)){ //消費者IDと順番IDが一緒なら消費者が意思決定
                consumer_[k].consumerDecide(employees_status_, company_[0].getPrice());
                // System.out.println("IDが"+k+"のお客さんの選択した従業員は"+consumer_[k].getSelectEmployee());
                for(int j = 0; j < num_employees_; j++){//選択された従業員がいれば、髪をきる
                  if(j == consumer_[k].getSelectEmployee()){
                    employee_[j].heirCut(consumer_[k].getConsumerID(), consumer_, num_each_lifestyles_);
                  }
                }
              }
            }
          }
        }

        //サービスの利用者をカウント
        num_user_ = 0;
        for(int i = 0; i < num_employees_; i++){
          for(int j = 0; j < num_consumers_; j++){
            if(employee_[i].getClients()[j] == 1){
              num_user_ += 1;
            }
          }
        }
        //企業の利得計算
        for(int i = 0; i < num_company_; i++){
          company_[i].calcCompanyProfit(company_[i].getPrice(), num_user_, company_[i].getSalary(), num_employees_);
        }

        for(int i = 0; i < num_consumers_; i++){
          if(i >= 0 && i <= num_each_lifestyles_[0]-1){
            consumer_profit_[0] += consumer_[i].getCS();
          }
          else if(i >= num_each_lifestyles_[0] && i <= num_each_lifestyles_[1]-1){
            consumer_profit_[1] += consumer_[i].getCS();
          }
          else if(i >= num_each_lifestyles_[1] && i <= num_each_lifestyles_[2]-1){
            consumer_profit_[2] += consumer_[i].getCS();
          }
          else{
            consumer_profit_[3] += consumer_[i].getCS();
          }
        }

        //社会総余剰の計算
        social_profit_ += company_[0].getCompanyProfit();
        for(int i = 0; i < num_employees_; i++){
          social_profit_ += employee_[i].getES();
        }
        for(int i = 0; i < num_consumers_; i++){
          social_profit_ += consumer_[i].getCS();
        }

        //結果の書き出し
        wc.writeResult(time_, workday_, company_[0].getPrice(), company_[0].getSalary(), //基本情報
          company_[0].getCompanyProfit(), employee_[0].getES(), employee_[1].getES(), consumer_profit_, //利得
          employee_[0].getResources(), employee_[1].getResources(), //資源
          employee_[0].getClientsLifestyle(), employee_[1].getClientsLifestyle(), //ライフスタイルごとの担当顧客
          social_profit_,//社会余剰
          first_price_, first_salary_); 

        consumer_profit_[0] = 0;
        consumer_profit_[1] = 0;
        consumer_profit_[2] = 0;
        consumer_profit_[3] = 0;
        social_profit_ = 0;

      }//workdaysループ終了-----------------------------------------------------------------------------------------------
    }//timesループ終了----------------------------------------------------------------------------------------------------
  }
}
