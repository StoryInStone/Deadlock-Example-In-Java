package com.concurrency;

public class Main {
	
	public static void main(String[] args) {
		testTransferB();
	}
	
	public static void testTransferA() {
		Account a = new Account();
		a.setBalance(1000.0);
		Account b = new Account();
		b.setBalance(1000.0);
		
		ATMtransfer task1 = new ATMtransfer(a, b, 100.0);
		ATMtransfer task2 = new ATMtransfer(b, a, 100.0);
		
		Thread thread1 = new Thread(task1);
		Thread thread2 = new Thread(task2);
		thread1.start();
		thread2.start();
		System.out.println(a.getBalance());
		System.out.println(b.getBalance());
	}
	
	public static void testTransferB() {
		Account a = new Account();
		a.setBalance(1000.0);
		Account b = new Account();
		b.setBalance(1000.0);
		
		ATMtransferB task1 = new ATMtransferB(a, b, 100.0);
		ATMtransferB task2 = new ATMtransferB(b, a, 100.0);
		
		Thread thread1 = new Thread(task1);
		Thread thread2 = new Thread(task2);
		thread1.start();
		thread2.start();
		System.out.println(a.getBalance());
		System.out.println(b.getBalance());
	}
	
	public static void testTransferC() {
		Account a = new Account();
		a.setBalance(1000.0);
		Account b = new Account();
		b.setBalance(1000.0);
		
		ATMtransferC task1 = new ATMtransferC(a, b, 100.0);
		ATMtransferC task2 = new ATMtransferC(b, a, 100.0);
		
		Thread thread1 = new Thread(task1);
		Thread thread2 = new Thread(task2);
		thread1.start();
		thread2.start();
		System.out.println(a.getBalance());
		System.out.println(b.getBalance());
	}

}
