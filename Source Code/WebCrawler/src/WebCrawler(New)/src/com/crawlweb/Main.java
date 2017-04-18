package com.crawlweb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
	public static void main(String[] args) throws InterruptedException, IOException {
		List<News> list = new ArrayList<News>();
		list.add(new ZingNews());

		for (News news : list) {
			news.Execution();
		}
	}
}
