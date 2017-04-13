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
    
    public void Execution (String url) throws IOException {
        System.out.println ("*********************Báo mới*******************");
        Document doc = Jsoup.connect(url).get();
        Elements baoMoi = doc.select("div[class=header-wrap]").select("li[class=parent]");
        for (Element _baoMoi : baoMoi){
                Elements test = _baoMoi.select("ul[class=child]");
                if(!test.isEmpty()){
                    test = test.select("li").select("a");
                    for (Element test1 : test) {
                            String _newUrl = url + test1.attr("href");
                            System.out.println(_newUrl);
                            hotNews(_newUrl);
                            mainNews(_newUrl);
                            System.out.println();
                        }
                        System.out.println();
                }else{
                    System.out.println("else");
                    String _newUrl = url + _baoMoi.select("a").attr("href");
                    System.out.println(_newUrl);
                    hotNews(_newUrl);
                    mainNews_2(_newUrl);
                    System.out.println("else");
                    System.out.println();
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
        //System.out.println(_mainNews.select("p[class=summary]").text());
        System.out.println("Address: " + _address);
        System.out.println("Title: " + _title);
        System.out.println("Image: " + _image);
        System.out.println("Description: " + _description);
    }
    }
    
    public static void main (String[] arg) throws IOException{
        Baomoi _bM = new Baomoi();
        _bM.Execution("http://www.baomoi.com");
    }
    
}
