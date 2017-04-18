package com.crawlweb;

import java.io.IOException;

public interface News {

	public void Execution() throws IOException;

	public void hotNews(String url);

	public void mainNews(String url);
}