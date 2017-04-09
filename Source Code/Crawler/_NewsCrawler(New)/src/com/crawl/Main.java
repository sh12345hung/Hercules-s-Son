package com.crawl;

import java.io.IOException;
import java.util.ArrayList;
import com.mongodb.BasicDBObject;

public class Main {
	   public static void main(String[] args) throws IOException {
	     //VnExpress();
	      ZingNews();
	}

public static void VnExpress(){
	String web = "http://vnexpress.net/";
	ArrayList<Data> list = new ArrayList<Data>();
	VnExpress a = new VnExpress(web);
	list = a.Process_Web();
	for(int i = 0 ; i< list.size(); i++)
	{
		
	}
	
	
}
public static void ZingNews(){
	String web = "http://news.zing.vn/";
	ArrayList<Data> list = new ArrayList<Data>();
	ZingNews a = new ZingNews(web);
	list = a.Process_Web();
	for(int i = 0 ; i< list.size(); i++)
	{
		/*
		 * Lưu vào DataBase
		 */
	}
}
}