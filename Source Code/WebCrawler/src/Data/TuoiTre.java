/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

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


public class TuoiTre {
    public String LinkTitle;
    public String Title;
    
    public void tuoiTre() throws IOException{                                                  
        System.out.println("*********************TuoiTre.vn Crawl*****************: ");
        System.setProperty("http.proxyHost", "127.0.0.1");
        System.setProperty("http.proxyPort", "8182");
        Document doc = Jsoup.connect("http://tuoitre.vn/").get();
        Elements links = doc.select("nav[class=nav-main]").select("li");
        links = links.select("div[class=mega-menu]").remove();
        
	Element links1 = links.first();
        int i = 1;
        for (Element link: links){
                if(!link.text().equals(links1.text())){
                    String title = link.select("a").attr("title");
                    System.out.println(title);
                    System.out.println("Link: " + link.select("a").attr("href"));
                    System.out.println("-----------------if1");
                    if(link.select("a").attr("href").substring(0,link.select("a").attr("href").indexOf('p') + 1).equals("http")){
                        Document doc1 = Jsoup.connect(links.select("a").attr("href")).get();
                        Elements links2 = doc1.select("ul[class=clearfix]").select("li:eq("+i+")").select("div[class=mega-menu]").select("li");
                        i++;
                        for(Element link1: links2){
                            if(!link1.select("a").attr("href").equals(link.select("a").attr("href"))){
                                String url = link1.select("a").attr("href");
                                title = link1.text();
                                System.out.println("        Link: " + url);
                                System.out.println("        Title: "+ title);
                                System.out.println("---------------if2");
                            }
                        }
                        
                    }
                    
                }else{
                    String title = link.select("a").attr("title");
                    System.out.println(title);
                    System.out.println(link.select("a").attr("href"));
                    if(link.select("a").attr("href").substring(0,link.select("a").attr("href").indexOf('p') + 1).equals("http")){
                        Document doc1 = Jsoup.connect(link.select("a").attr("href")).get();
                        Elements links2 = doc1.select("ul[class=clearfix]").select("li:eq("+i+")").select("div[class='mega-menu']").select("li");
                        i++;
                        for(Element link1: links2){
                            if(!link1.select("a").attr("href").equals(link.select("a").attr("href"))){
                                String url = link1.select("a").attr("href");
                                title = link1.text();
                                System.out.println("        Link: " + url);
                                System.out.println("        Title: "+ title);
                                System.out.println("---------------esle2");
                            }
                        }
                    }
                }
            }
        }
    
    ///////////////////////////////////
    
    public void hotNews (String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements hotNews = doc.select("div[class=box_hot_news]");
        for(Element hot:hotNews){
            String _address = hot.select(".block_news_big").select("a").attr("href");
            String _title = hot.select("a").text();
            String _image = hot.select(".block_news_big").select("a").select("img").attr("src");
            String _content = hot.select("h4[class=news_lead]").text();
            System.out.println("----------------------------Hot News-------------------------");
            System.out.println("Address: "+ _address);
            System.out.println("Title: "+ _title);
            System.out.println("Image: "+ _image);
            System.out.println("Content: " + _content);
        }
    }

    
    
    public void mainNews (String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements medias = doc.select("div[class=block_mid_new]").select ("li");
        for(Element media:medias){
            String _address = media.select("a").attr("href");
            String _title = media.select("a").text();
            String _image = media.select("a").select("img").attr("src");
            String _content = media.select(".news_lead").text();
            System.out.println("------------------------Tin chinh-----------------------------");
            System.out.println("                Address: "+ _address);
            System.out.println("                Title: "+ _title);
            System.out.println("                Image: "+ _image);
            System.out.println("                Content: " + _content);
        }
    }
  
    public static void main (String[] args) throws IOException {
    	TuoiTre news = new TuoiTre();
        news.tuoiTre();
	}
}