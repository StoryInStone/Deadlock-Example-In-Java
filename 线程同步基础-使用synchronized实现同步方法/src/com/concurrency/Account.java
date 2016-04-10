package com.concurrency;


/*
 * 
 * ��ʾ��
 * ����һ���˺��࣬����һ�������˺ŵ�ģ�ͣ�ֻ��һ��˫���ȸ���������balance��
 * ʵ��set��get��������ȡ�˻�����
 * ʵ��addAccount()���������Ὣ��������ݷŵ����balance��ȥ��ע�⣬��ͬһʱ�䣬����ֻ����һ���̸߳ı�balance���ֵ��
 * ʵ��subtractAccount()�����������������������п۳���ע��Ҫ��ͬ�ϡ�
 * 
 * */
public class Account {
	
	private double balance;

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	public synchronized void addAccount(double money) {
		double tmp = balance;
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			
		}
		tmp = tmp + money;
		balance = tmp;
	}
	
	public synchronized void subtractAccount(double money) {
		double tmp = balance;
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			
		}
		tmp = tmp - money;
		balance = tmp;
	}

}
