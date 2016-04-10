package com.concurrency;

public class ATMtransferC implements Runnable {

	private Account from;
	private Account to;
	private double money;

	public ATMtransferC(Account from, Account to, Double money) {
		this.from = from;
		this.to = to;
		this.money = money;
	}

	@Override
	public void run() {
		transfer(from, to, money);
	}

	public void transfer(Account from, Account to, double money) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {

		}
		from.subtractAccount(money);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {

		}
		to.addAccount(money);
	}

}
