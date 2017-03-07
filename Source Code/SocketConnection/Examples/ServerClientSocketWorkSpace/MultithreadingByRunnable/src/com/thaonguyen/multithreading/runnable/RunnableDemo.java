package com.thaonguyen.multithreading.runnable;

public class RunnableDemo implements Runnable {
	private Thread t;
	private String threadName;
	
	public RunnableDemo(String name) {
		threadName = name;
		System.out.println("Creating " + threadName);
	}

	@Override
	public void run() {
		System.out.println("Running " + threadName);
		try {
			for (int i = 4; i > 0; i--) {
				System.out.println("Thread: " + threadName + ", " + i);
				// Let thread sleep for a while.
				Thread.sleep(50);
			}
		}
		catch (InterruptedException e) {
			System.out.println("Thread " + threadName + " exitting.");
		}
	}
	
	public void start() {
		System.out.println("Starting " + threadName);
		if (t == null) {
			t = new Thread(this, threadName);
			t.start();
		}
	}

}
