package com.concurrency;

public class ATMtransfer implements Runnable {

	private Account from;
	private Account to;
	private double money;

	public ATMtransfer(Account from, Account to, Double money) {
		this.from = from;
		this.to = to;
		this.money = money;
	}

	@Override
	public void run() {
		transfer(from, to, money);
	}

	public void transfer(Account from, Account to, double money) {
		int fromHash = System.identityHashCode(from);
		int toHash = System.identityHashCode(to);
		if (fromHash < toHash) {
			synchronized (from) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {

				}
				synchronized (to) {
					double fromTemp = from.getBalance();
					double toTemp = to.getBalance();

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {

					}

					fromTemp = fromTemp - money;
					from.setBalance(fromTemp);
					toTemp = toTemp + money;
					to.setBalance(toTemp);
				}
			}
		} else if (fromHash > toHash) {
			synchronized (to) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {

				}
				synchronized (from) {
					double fromTemp = from.getBalance();
					double toTemp = to.getBalance();

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {

					}

					fromTemp = fromTemp - money;
					from.setBalance(fromTemp);
					toTemp = toTemp + money;
					to.setBalance(toTemp);
				}
			}
		} else {
			// 当两个对象的hash值一致时，我们再加一把锁来处理，这里就不写了。因为概率很小。
		}

	}

}
