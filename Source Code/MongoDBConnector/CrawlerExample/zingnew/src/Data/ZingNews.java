
package Data;

/**
 *
 * Config By Phuc
 */
import java.io.IOException;
import java.util.Map;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.thaonguyen.mongodbconnector.MongoDBConnectorForCrawler;
public class ZingNews {
	private static final String DATABASE_NAME = "test";
    
    private String _address;
    private String _title;
    private String _image;
    private String _description;
    public String _topic;
    private MongoDBConnectorForCrawler conn;
    
    public ZingNews() {
    	conn = new MongoDBConnectorForCrawler("localhost", 27017, DATABASE_NAME);
    }
    
    public void close() {
    	conn.close();
    }
    
    public void Execution (String url) throws IOException {
        System.out.println ("*********************ZingNews*******************");
        Document doc = Jsoup.connect(url).get();
        Elements NewsZing = doc.select("nav[class=categories] ").select("li");
        for (Element Zing : NewsZing){
                Elements temp = Zing.select("ul[class=child]");
                if(!temp.isEmpty()){
                    temp = temp.select("li").select("a");

                    for (Element temp1 : temp) {
                            _topic = temp1.select("a").text();
//                            System.out.println("Topic: " + _topic);
                            String _newUrl =temp1.attr("abs:href");
//                            System.out.println(_newUrl);
                            hotNews(_newUrl);
                            mainNews(_newUrl);
                            System.out.println();
                        }
                        System.out.println();
                }else{
                    _topic = Zing.text();
//                    System.out.println("Topic: " + _topic);
                    String _newUrl = Zing.select("a").attr("abs:href");
//                    System.out.println(_newUrl);
                    hotNews(_newUrl);
                    mainNews_2(_newUrl);
//                    System.out.println();
                }
                    
        }
    }
    
    public void hotNews (String url) throws IOException {
    Document doc = Jsoup.connect(url).get();
    Elements link = doc.select("section[class=cate_sidebar]").select("section[class=mostread]").select("article");
    for ( Element _hotNews : link){
        _address =  _hotNews.select("a").attr("abs:href");
        _title = _hotNews.select("header").select("p[class=title]").select("a").text();
        _image = _hotNews.select("img").attr("src");
        System.out.println("        Address: " + _address);
        System.out.println("        Title: " + _title);
        System.out.println("        Image: " + _image);
        System.out.println("        Topic: " + _topic);
        
        if (_address.isEmpty() || _title.isEmpty() || _image.isEmpty() || (_topic.length() > 10)) {
        	System.out.println("NOT ADD");
        }
        else {
        	System.out.println("ADD");
        	try {
				conn.AddNews(_address, _title, _image, _topic, "", "ZingNews");
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("ADD FAIL");
			}
        }
    }
    }
    
    public void mainNews (String url) throws IOException {
    Document doc = Jsoup.connect(url).get();
    Elements link = doc.select("div[class=wrapper]");
    for (Element _mainNews : link){
        _address = _mainNews.select("a").attr("abs:href");
        _image = _mainNews.select("img").attr("src");
        _title = _mainNews.select("header").select("p[class=title]").text();
        _description = _mainNews.select("p[class=summary]").text();
        System.out.println("Address: " + _address);
        System.out.println("Title: " + _title);
        System.out.println("Image: " + _image);
        System.out.println("Description: " + _description);
        System.out.println("Topic: " + _topic);
        
        if (_address.isEmpty() || _title.isEmpty() || _image.isEmpty() || (_topic.length() > 10)) {
        	System.out.println("NOT ADD");
        }
        else {
        	System.out.println("ADD");
        	try {
				conn.AddNews(_address, _title, _image, _topic, _description, "ZingNews");
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("ADD FAIL");
			}
        }
    }
    }
    
    public void mainNews_2 (String url) throws IOException {
    Document doc = Jsoup.connect(url).get();
    Elements link = doc.select("section[class=cate_content]").select("article");
    for (Element _mainNews : link){
        _address = _mainNews.select("a").attr("abs:href");
        _image = _mainNews.select("img").attr("src");
        _title = _mainNews.select("header").select("p[class=title]").text();
        _description = _mainNews.select("p[class=summary]").text();
        System.out.println("Address: " + _address);
        System.out.println("Title: " + _title);
        System.out.println("Image: " + _image);
        System.out.println("Description: " + _description);
        System.out.println("Topic: " + _topic);
        
        if (_address.isEmpty() || _title.isEmpty() || _image.isEmpty() || (_topic.length() > 10)) {
        	System.out.println("NOT ADD");
        }
        else {
        	System.out.println("ADD");
        	try {
				conn.AddNews(_address, _title, _image, _topic, _description, "ZingNews");
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("ADD FAIL");
			}
        }
        System.out.println("*************************");
        System.out.println(" ");
    }
    }
    
    public static void main (String[] arg) throws IOException{
        ZingNews _Zing = new ZingNews();
        _Zing.Execution("http://www.news.zing.vn");
        _Zing.close();
    }
    
}
