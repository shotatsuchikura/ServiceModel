package main;

//��ƃv���C���N���X
public class Company{

//��Ƃ�ID
private int company_id_;

//��Ƃ̗���
private int company_profit_;

//�]�ƈ��̋���
private int salary_;

//�T�[�r�X���i
private int price_;

//�R���X�g���N�^
public Company(int id){
  company_id_ = id;
}

//���Z�b�g
public void resetCompany(){
  company_profit_ = 0;
}

//��Ƃ̗������v�Z����
public void calcCompanyProfit(int price, int num_user, int salary, int num_employees){
  company_profit_ = price * num_user - salary * num_employees;
}

//�����E���i���Z�b�g����
public void setSalaryPrice(int price, int salary){
  salary_ = salary;
  price_ = price;
}

//��Ƃ�ID�𓚂���
public int getCompanyID(){
  return company_id_;
}

//�����𓚂���
public int getCompanyProfit(){
  return company_profit_;
}

//�����𓚂���
public int getSalary(){
  return salary_;
}

//�T�[�r�X���i�𓚂���
public int getPrice(){
  return price_;
}


}
