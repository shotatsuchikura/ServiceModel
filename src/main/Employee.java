package main;

import java.util.Random;

//�]�ƈ��N���X
public class Employee{

////�g�p���闐���C���X�^���X
Random rnd = new Random();

//�]�ƈ���ID
private int employee_id_;

//�]�ƈ��̗���(ES)
private int employee_satisfaction_;

//�]�ƈ��̊l������
private int[] resources_ = new int[3];

//�]�ƈ����l���ł��鎑���̐�
private int num_resources_can_get_;

//�]�ƈ����l�����Ă��鎑���̐�
private int num_resources_got_;

//�]�ƈ��̒S�������
private int[] clients_ = new int[40];

//�]�ƈ����T�[�r�X�񋟉\�����Ȃ�
private boolean employee_status_;

//kyapa
private int employee_capacity_;

//�]�ƈ��̒S������Ґ�
private int num_clients_;

//�S���ڋq�̃^�C�v
private int clients_lifestyle_[] = new int[4];

//�g�[�^���̍��v
private int total_clients_lifestyle_[] = new int[4];

//���C���ڋq�̃��C�t�X�^�C��
private int client_main_lifestyle_;

//�R���X�g���N�^
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
  num_resources_can_get_ = (salary - min_salary) / effort; //�����l���\�������߂�
  employee_satisfaction_ = salary;
  //�����l���\����0�̂Ƃ�
  //�����Ȃ̂ł��K�v�Ȃ�
  if(num_resources_can_get_ == 0){ 
    resources_[0] = 1;
    resources_[1] = 0;
    resources_[2] = 0;
    employee_capacity_ = 20;
  }
  //�����l���\�P�̂Ƃ�
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
  //�����l���\2�̂Ƃ�
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
  //�����R�̂Ƃ�
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
  //�����S�̂Ƃ�
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
  //�����T�i�ȏ�j�̂Ƃ�
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

//�S������҂𓚂���
public int[] getClients(){
  return clients_;
}

//ES�𓚂���
public int getES(){
  return employee_satisfaction_;
}

//�S���ڋq�̃��C�X�X�^�C�����Ƃ̐�����������
public int[] getClientsLifestyle(){
  return clients_lifestyle_;
}

//�g�[�^���̒S���ڋq�^�C�v���𓚂���
public int[] getTotalClientLifestyle(){
  return total_clients_lifestyle_;
}

//�T�[�r�X�񋟉\��������
public boolean getEmployeeStatus(){
  return employee_status_;
}

//�S�����Ă������Ґ��𓚂���
public int getNumClients(){
  return num_clients_;
}

public int getMainClientLifestyle(){
return client_main_lifestyle_;
}

}
