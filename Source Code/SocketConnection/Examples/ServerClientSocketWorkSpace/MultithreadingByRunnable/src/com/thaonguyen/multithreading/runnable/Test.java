package com.thaonguyen.multithreading.runnable;

public class Test {

	public static void main(String[] args) {
		RunnableDemo R1 = new RunnableDemo("Thread-1");
		RunnableDemo R2 = new RunnableDemo("Thread-2");
		
		R1.start();
		R2.start();
	}
}
