package com.crawl;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.jws.WebParam;
import com.mongodb.BasicDBObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
public class ZingNews extends List_Crawler{
	   public String LinkTitle;
	    public String Title;
	    public ZingNews(String web)
	    {
	    	super(web);
	    }
	    
	    
	    public ArrayList<Data> Process_Web(){
	    	 ArrayList<Data> list = new ArrayList<Data>();
	    	 System.out.println("*********************ZingNews Crawler*****************: ");
	         System.setProperty("http.proxyHost", "127.0.0.1");
	         System.setProperty("http.proxyPort", "8182");
	         try {
	         Document doc = Jsoup.connect(web).get();
	         Elements links = doc.select("nav[class=categories] ").select("li");
	         int i =0;
	         Element links1 = links.first();
	             for (Element link: links){
	            	 Title = link.text();
	    	         
	            	 if(!link.text().equals(links1.text()))
	            	 {
	            		 LinkTitle = link.select("a").attr("abs:href");
	            		 Document doc1 = Jsoup.connect(link.select("a").attr("abs:href")).get();
                         Elements links2 = doc1.select("div[class=content-wrap]").select("section");  
                         /* Tin Chính */
                         for(Element link1: links2){
                             String url = link1.select("a").attr("abs:href");
			                 if(!(url.substring(url.indexOf('#')+1, url.length()).equals("home_cate|tinchinh"))){
                                     String _address = link1.select("article").select("p[class=title]").select("a").attr("abs:href");
                                   //  String _title = link1.select("a").text();
                                     String _image = link1.select("a").select("img").attr("src");
                                   //  String _content = link1.select("p[class=sumary").text();
                                     System.out.println("------------------------Tin chinh-----------------------------");
                                     System.out.println("                Address: "+ _address);
                                    // System.out.println("                Title: "+ _title);
                                     System.out.println("                Image: "+ _image);
                                   //  System.out.println("                Content: " + _content);
                                     i++;
                             }            
                             
                                 
                                
                             }
                         
	            		 }
	            	 }
	             System.out.println(i);
	         } 
	             
	                   catch (IOException e) {
	         System.out.println("ERROR Crawler processPage");
	                            }               
			return list;
	    }
}
