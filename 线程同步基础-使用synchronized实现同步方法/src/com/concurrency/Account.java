package com.concurrency;


/*
 * 
 * 提示：
 * 创建一个账号类，这是一个银行账号的模型，只有一个双精度浮点型属性balance。
 * 实现set和get方法来读取账户的余额。
 * 实现addAccount()方法，它会将传入的数据放到余额balance中去。注意，在同一时间，我们只允许一个线程改变balance这个值。
 * 实现subtractAccount()方法，它将传入的数量余额中扣除，注意要求同上。
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
