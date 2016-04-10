# Deadlock-Example-In-Java
同步与死锁的例子

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

/*
 * 
 * 提示：
 * 创建一个ATM取钱类，这是取款机的模型。这个类实现Runnnable接口作为线程来执行。
 * 为这个类增加账户类Account对象，用构造器初始化这个对象。
 * 实现run方法，它将调用subtactAccount()对账户余额进行扣除，并且执行循环100次。
 * 解释：从ATM取钱后，该银行账户的余额减少。
 * 
 * */
public class ATMget {

}

/*
 * 
 * 提示：
 * 创建一个ATM存钱类，这是存款机的模型。这个类实现Runnnable接口作为线程来执行。
 * 为这个类增加账户类Account对象，用构造器初始化这个对象。
 * 实现run方法，它将调用addAccount()对账户余额进行充值，并且执行循环100次。
 * 解释：在ATM存钱后，该银行账户的余额增加。
 * 
 * */
public class ATMput implements Runnable {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}


从这个例子，我们应该知道synchronized的使用，它是修饰符，可以用来修饰方法来实现同步方法。
另外也可以作为同步代码块，来保护代码块。当作为同步代码块使用时，必须把对象引入作为传入参数。通常我们使用this关键字来引用正在执行的方法的对象（也就是说使用一把锁）。
其实从这个例子，我想到了一个危险的更有考察意义的样例。同步之后导致的死锁问题。在这里，我们设计时，一个线程永远都只有一把锁，所以绝不会出现死锁的问题。
我们考虑这样一个情形，转账问题。
我们实现一笔转账时，必须实现两个账户的更新，所以我们在设计ATM转账类时，如果使用两把锁会怎么样（也就是说我们不用this把ATM对象锁住，而是锁住转账的两个账户对象）。
设计如下：

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
	}

}

因为addAccount和subtractAccount是两把锁，
所以在transfer方法里面我们没有直接使用account的addAccount和subtractAccount的方法，写法作用是一样的，这样能够把两把锁看得清晰一些。
在银行每秒钟可能有上亿次的转账记录，我们不能排除一亿次中可能出现，两个账户同时向对方转账的情况。
当线程A锁住from，尝试锁住to，线程B锁住to，尝试锁住from。这里线程A和B的from和to恰好对调。
用main方法模拟这个过程。此时，线程A和线程B都将永久地等待下去。

public class Main {
	
	public static void main(String[] args) {
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
	}

}

我们应该如何解决这个问题呢？解决问题的要点是无论在何时，都要保证锁顺序的一致性。我们这里利用的对象的hash值来处理。

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

我们通过模拟这个过程，正如我们所预想的，程序使用了4秒钟，完成了转账。死锁问题不再发生。

public class Main {
	
	public static void main(String[] args) {
		testTransferA();
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
	}

}

大家可以在我的github下载项目代码来体验Java中的同步与死锁问题。
