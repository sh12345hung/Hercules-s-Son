package com.thaonguyen.multithreading.thread;

public class TestThread {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ThreadDemo R1 = new ThreadDemo("Thread-1");
		ThreadDemo R2 = new ThreadDemo("Thread-2");
		
		R1.start();
		R2.start();
	}

}