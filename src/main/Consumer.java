package main;

import java.util.Random;

public class Consumer{

  //����҃v���C����ID
  private int consumer_id_;

  //����҃v���C���̃��C�t�X�^�C��
  private int consumer_lifestyle_;

  //����҃v���C���̗����\
  private int[] gain_table_;

  //����҂̗���(CS)
  private int consumer_satisfaction_;

  //�]�ƈ��ʂ̊���CS���i�[����z��
  private int[] expect_cs_ = new int[2];

  //�I������]�ƈ�
  private int select_employee_;

  //����҂̃T�[�r�X���p��
  private boolean consumer_status_;

  //�g�p���闐���C���X�^���X
  Random rnd = new Random();

  //�R���X�g���N�^
  public Consumer(int id, int lifestyle, int[][] gain_tables){
    consumer_id_ = id;
    consumer_lifestyle_ = lifestyle;
    gain_table_ = gain_tables[lifestyle];
  }

 //���Z�b�g
  public void resetConsumer(int num_employees){
    select_employee_ = num_employees;
    consumer_status_ = false;
    consumer_satisfaction_ = 0;
  }

  //�e�]�ƈ��𗘗p�����ۂ�CS�̌v�Z
  public void calcExpectCS(int[][] employee_resources){

    //����CS�z��̒��g�����Z�b�g
    for(int i = 0; i < 2; i++){
      expect_cs_[i] = 0;
    }

    //�]�ƈ��ʂɌv�Z���Ċi�[
    for(int i = 0; i < 2; i++){
      for(int j = 0; j < 3; j++){
        if(employee_resources[i][j] == 2){ //�X�L�����x���Q�̂Ƃ�
          expect_cs_[i] += gain_table_[j*2+1];
        }else if(employee_resources[i][j] == 1){ //�X�L�����x���P�̂Ƃ�
          expect_cs_[i] += gain_table_[j*2];
        }else{ //�X�L���Ȃ��̂Ƃ�
          expect_cs_[i] += 0;
        }
      }
      // System.out.println(expect_cs_[i]);
    }
  }

  //�T�[�r�X���p�̈ӎv����
  public void consumerDecide(boolean[] employee_status, int price){

    // //���p�]�ƈ���������
    // select_employee_ = num_employees_; //���̏ꍇ�̓T�[�r�X�𗘗p���Ȃ����Ƃ�����

    //����]�ƈ���I��s�\�ȏꍇ�A����CS��0�ɂ���
    for(int i = 0; i < 2; i++){
      if(employee_status[i] == false){
        expect_cs_[i] = 0;
      }
    }

    //����CS�y��price���r����ԍ������̂�������A����CS�̏ꍇ�͂��̏]�ƈ��𗘗p���Aprice�̏ꍇ�̓T�[�r�X�𗘗p���Ȃ�
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


    //���̃V���b�g�ɂ�����cs���v�Z
    if(select_employee_ == 2){
      // consumer_status_ = false;
      consumer_satisfaction_ = 0;
    } else {
      consumer_status_ = true;
      consumer_satisfaction_ = expect_cs_[select_employee_];
    }
  }

  //id�𓚂���
  public int getConsumerID(){
    return consumer_id_;
  }

  //���C�t�X�^�C���𓚂���
  public int getLifestyle(){
    return consumer_lifestyle_;
  }

  //�����\�𓚂���
  public int[] getGainTable(){
    return gain_table_;
  }

  //����CS����������
  public int[] getExpectCS(){
    return expect_cs_;
  }

  //CS�𓚂���
  public int getCS(){
    return consumer_satisfaction_;
  }

  //�T�[�r�X�̗��p�󋵂𓚂���
  public boolean getConsumerStatus(){
    return consumer_status_;
  }

  //�I�������]�ƈ��𓚂���
  public int getSelectEmployee(){
    return select_employee_;
  }

}