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
import java.util.Map;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
public class Baomoi {
    
    private String _address;
    private String _title;
    private String _image;
    private String _description;
    public String _topic;
    public String _topicNews;   //name of newspaper
    
    public void Execution (String url) throws IOException {
        System.setProperty("http.proxyHost", "127.0.0.1");
        System.setProperty("http.proxyPort", "8182");
        System.out.println ("*********************Báo mới*******************");
        Document doc = Jsoup.connect(url).get();
        Elements baoMoi = doc.select("div[class=header-wrap]").select("li[class=parent]");
            for (Element _baoMoi : baoMoi){
                Elements temp = _baoMoi.select("ul[class=child]");
                if(!temp.isEmpty()){
                    temp = temp.select("li").select("a");
                    _baoMoi.select("ul[class=child").remove();
                    _topic = _baoMoi.select("li[class=parent").select("a").text();
                    for (Element temp1 : temp) {
                        for (int i = 1; i <= 1; i ++){
                            String _newUrl = temp1.select("a").attr("href");
                            _newUrl = _newUrl.substring(0,_newUrl.indexOf('.'));
                            _newUrl = url + _newUrl + "/trang" + i + ".epi";
                            System.out.println("Topic: " + _topic);
                            
                            System.out.println();
                            hotNews(_newUrl);
                            System.out.println();
                            mainNews(_newUrl);  
                        }  
                    }
                }else{
                    _topic = _baoMoi.text();
                    for (int i = 1; i <= 1; i++){
                    System.out.println("Topic: " + _topic);
                    
                    String _newUrl = _baoMoi.select("a").attr("href");
                    _newUrl = _newUrl.substring(0,_newUrl.indexOf('.'));
                    _newUrl = url + _newUrl + "/trang" + i + ".epi";
                    
                    System.out.println();
                    hotNews(_newUrl);
                    System.out.println();
                    mainNews_2(_newUrl);
                    
                }
            }        
        }
    }
    
    public void hotNews (String url) throws IOException {
    Document doc = Jsoup.connect(url).get();
    Elements link = doc.select("div[class=main]").select("article");
    Elements name;
    for ( Element _hotNews : link){
        _address = url + _hotNews.select("a").attr("href");
        _title = _hotNews.select("a").attr("title");
        _image = _hotNews.select("img").attr("src");
        
        name = _hotNews.select("p[class=meta]");
        _topicNews = name.select("a").first().text();
        
        System.out.println("        Address: " + _address);
        System.out.println("        Title: " + _title);
        System.out.println("        Image: " + _image);
        System.out.println("        Address's name: " + _topicNews);
    }
    }
    
    public void mainNews (String url) throws IOException {
    Document doc = Jsoup.connect(url).get();
    Elements link = doc.select("div[class=cat-content]").select("article");
    Elements name;

    for (Element _mainNews : link){
        _address = url + _mainNews.select("a").attr("href");
        _image = _mainNews.select("img").attr("src");
        _title = _mainNews.select("h2").text();
        _description = _mainNews.select("p[class=summary]").text();
        
        name = _mainNews.select("p[class=meta]");
        _topicNews = name.select("a").first().text();

        
        
        System.out.println("Address: " + _address);
        System.out.println("Title: " + _title);
        System.out.println("Image: " + _image);
        System.out.println("Description: " + _description);
        System.out.println("Address's name: " + _topicNews);
    }
    }
    
    public void mainNews_2 (String url) throws IOException {
    Document doc = Jsoup.connect(url).get();
    Elements link = doc.select("section[class=content-list]").select("article");
    Elements name;
    for (Element _mainNews : link){
        _address = url + _mainNews.select("a").attr("href");
        _image = _mainNews.select("img").attr("src");
        _title = _mainNews.select("h2").text();
        _description = _mainNews.select("p[class=summary]").text();
        
        name = _mainNews.select("header").select("p[class=meta]");;
        _topicNews = name.select("a").first().text();

        
        System.out.println("Address: " + _address);
        System.out.println("Title: " + _title);
        System.out.println("Image: " + _image);
        System.out.println("Description: " + _description);
        System.out.println("Address's name: " + _topicNews);
    }
    }
    
    public static void main (String[] arg) throws IOException{
        Baomoi _bM = new Baomoi();
        _bM.Execution("http://www.baomoi.com");
    }
    
}
