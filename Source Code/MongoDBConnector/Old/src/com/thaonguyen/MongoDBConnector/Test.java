package com.thaonguyen.MongoDBConnector;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;

public class Test {

	public static void main(String[] args) {
		System.out.println("MongoDBConnector test start");
		
/*		News n = new News("title1", "url1", "headertext1", "imageurl1", new Date());
		
		System.out.println("Title: " + n.getTitle());
		System.out.println("URL: " + n.getTitle());
		System.out.println("HeaderText: " + n.getHeaderText());
		System.out.println("Image URL: "+ n.getImageURL());
		System.out.println("Modified: " + n.getModifiedDate());*/
		
		/*NewsList l = new NewsList();
		News n = new News("title1", "url1", "headertext1", "imageurl1", new Date());
		
		l.addNews(n);
		ArrayList<String> IDs = new ArrayList<String>();
		IDs.add("id1");
		
		if (l.SetNewsID(IDs)) {
			System.out.println("Set id ok, count = " + l.count());
		}
		
		ArrayList<News> arr = l.toArrayList();
		
		Iterator<News> i = arr.iterator();
		while (i.hasNext()) {
			System.out.println(i.next().getID());
		}
		
		List<Document> docs = l.toArrayListDocument();*/
		
		/* DBConnector conn = new DBConnector("News"); // Not check with unavailable collection name yet
		
		System.out.println("Document count: " + conn.getCount());
		
		if (conn.changeCollection("News")) {
			System.out.println("Change OK");
		}
		
		System.out.println("Document count: " + conn.getCount());
		
		News n1 = new News("title1", "url1", "headertext1", "imageurl1", new Date());
		
		if (conn.checkURLExists(n1.getURL())) {
			System.out.println("true");
		}
		else {
			System.out.println("false");
		}
		conn.addNews(n1);
		
		NewsList l = new NewsList();
		
		l.addNews(n1);
		
		News n2 = new News("title2", "url2", "headertext2", "imageurl2", new Date());
		l.addNews(n2);
		
		News n3 = new News("title3", "url3", "headertext3", "imageurl3", new Date());
		l.addNews(n3);
		
		News n4 = new News("title4", "url4", "headertext4", "imageurl4", new Date());
		l.addNews(n4);
		
		News n5 = new News("title5", "url5", "headertext5", "imageurl5", new Date());
		l.addNews(n5);
		
		System.out.println("Added: " + conn.addNewsList(l));*/
		
		DBConnector conn = new DBConnector("News"); // Not check with unavailable collection name yet
		
		System.out.println("Document count: " + conn.getCount());
		
		NewsList l = conn.getNews(0, 15);
		
		ArrayList<News> aln = l.toArrayList();
		Iterator<News> i = aln.iterator();
		while (i.hasNext()) {
			System.out.println(i.next().getTitle());
		}
		
		conn.close();
		System.out.println("MongoDBConnector test stop");
	}

}
