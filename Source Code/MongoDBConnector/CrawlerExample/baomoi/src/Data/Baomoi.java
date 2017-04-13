/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

/**
 *
 * @author Thai
 */
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.thaonguyen.mongodbconnector.MongoDBConnectorForCrawler;
public class Baomoi {
	private static final String DATABASE_NAME = "test";
    
    private String _address;
    private String _title;
    private String _image;
    private String _description;
    public String _topic;
    private MongoDBConnectorForCrawler conn;
    
    public Baomoi() {
    	conn = new MongoDBConnectorForCrawler("localhost", 27017, DATABASE_NAME);
    }
    
    public void close() {
    	conn.close();
    }
    
    public void Execution (String url) throws IOException {
        System.out.println ("*********************Báo mới*******************");
        Document doc = Jsoup.connect(url).get();
        Elements baoMoi = doc.select("div[class=header-wrap]").select("li[class=parent]");
        for (Element _baoMoi : baoMoi){
                Elements temp = _baoMoi.select("ul[class=child]");
                if(!temp.isEmpty()){
                    temp = temp.select("li").select("a");

                    for (Element temp1 : temp) {
                            _topic = temp1.select("a").text();
                            //System.out.println("Topic: " + _topic);
                            String _newUrl = url + temp1.attr("href");
                            //System.out.println(_newUrl);
                            hotNews(_newUrl);
                            mainNews(_newUrl);
                            //System.out.println();
                        }
                        System.out.println();
                }else{
                    _topic = _baoMoi.text();
                    //System.out.println("Topic: " + _topic);
                    String _newUrl = url + _baoMoi.select("a").attr("href");
                    //System.out.println(_newUrl);
                    hotNews(_newUrl);
                    mainNews_2(_newUrl);
                    //System.out.println();
                }
                    
        }
    }
    
    public void hotNews (String url) throws IOException {
    Document doc = Jsoup.connect(url).get();
    Elements link = doc.select("div[class=main]");
    for ( Element _hotNews : link){
        _address = url + _hotNews.select("a").attr("href");
        _title = _hotNews.select("a").attr("title");
        _image = _hotNews.select("img").attr("src");
        System.out.println("        Address: " + _address);
        System.out.println("        Title: " + _title);
        System.out.println("        Image: " + _image);
        System.out.println("        Topic: " + _topic);
        
        if (_address.isEmpty() || _title.isEmpty() || _image.isEmpty()) {
        	System.out.println("NOT ADD");
        }
        else {
        	System.out.println("ADD");
        	try {
				conn.AddNews(_address, _title, _image, _topic, "", "BaoMoi");
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("ADD FAIL");
			}
        }
    }
    }
    
    public void mainNews (String url) throws IOException {
    Document doc = Jsoup.connect(url).get();
    Elements link = doc.select("div[class=cat-content]").select("article");
    for (Element _mainNews : link){
        _address = url + _mainNews.select("a").attr("href");
        _image = _mainNews.select("img").attr("src");
        _title = _mainNews.select("h2").text();
        _description = _mainNews.select("p[class=summary]").text();
        System.out.println("Address: " + _address);
        System.out.println("Title: " + _title);
        System.out.println("Image: " + _image);
        System.out.println("Description: " + _description);
        System.out.println("Topic: " + _topic);
        
        if (_address.isEmpty() || _title.isEmpty() || _image.isEmpty()) {
        	System.out.println("NOT ADD");
        }
        else {
        	System.out.println("ADD");
        	try {
				conn.AddNews(_address, _title, _image, _topic, _description, "BaoMoi");
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("ADD FAIL");
			}
        }
    }
    }
    
    public void mainNews_2 (String url) throws IOException {
    Document doc = Jsoup.connect(url).get();
    Elements link = doc.select("section[class=content-list]").select("article");
    for (Element _mainNews : link){
        _address = url + _mainNews.select("a").attr("href");
        _image = _mainNews.select("img").attr("src");
        _title = _mainNews.select("h2").text();
        _description = _mainNews.select("p[class=summary]").text();
        System.out.println("Address: " + _address);
        System.out.println("Title: " + _title);
        System.out.println("Image: " + _image);
        System.out.println("Description: " + _description);
        System.out.println("Topic: " + _topic);
        
        if (_address.isEmpty() || _title.isEmpty() || _image.isEmpty()) {
        	System.out.println("NOT ADD");
        }
        else {
        	System.out.println("ADD");
        	try {
				conn.AddNews(_address, _title, _image, _topic, _description, "BaoMoi");
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("ADD FAIL");
			}
        }
    }
    }
    
    public static void main (String[] arg) throws IOException{
        Baomoi _bM = new Baomoi();
        _bM.Execution("http://www.baomoi.com");
    }
    
}
