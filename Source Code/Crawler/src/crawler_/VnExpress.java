package crawler_;

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
public  class VnExpress extends Crawl_Arraylist{
    public String LinkTitle;
    public String Title;
    
   
    
    public ArrayList<Data> Process_Web(){
    	 ArrayList<Data> list = new ArrayList<Data>();
    	
        System.out.println("*********************VNExpress Crawler*****************: ");
        System.setProperty("http.proxyHost", "127.0.0.1");
        System.setProperty("http.proxyPort", "8182");
        try {
        Document doc = Jsoup.connect("http://vnexpress.net").get();
        Elements links = doc.select("ul[class=list_menu_header]").select("li");
        int i =0;
	Element links1 = links.first();
            for (Element link: links){
                    if(!link.text().equals(links1.text())){
                    System.out.println(link.text());
                    Title = link.text();
                    if(link.select("a").attr("href").substring(0,link.select("a").attr("href").indexOf('p')+1).equals("http")){
                            System.out.println("Link:" + link.select("a").attr("href"));
                            
                            LinkTitle = link.select("a").attr("href");
                        
                            Document doc1 = Jsoup.connect(link.select("a").attr("href")).get();
                            Elements links2 = doc1.select("div[class=mid_header width_common]").select("ul[id=breakumb_web]").select("li");
                            for(Element link1: links2){
                                    String url = link1.select("a").attr("href");
                                    if(!(url.substring(0,url.indexOf('p')+1).equals("http"))){
                                        System.out.println("------------" + url);
                                        url = link.select("a").attr("href") + url;
                                    }
                                    String title = link1.text();
                                    System.out.println("        Link child: " + url);
                                    System.out.println("        Title child: "+ title);
             
                                    Document doc2 = Jsoup.connect(url).get();
                                    Elements medias = doc2.select("div[class=block_mid_new]").select ("li");
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
                                    i++;
                                    }
                                    
                                    Document doc3 = Jsoup.connect(url).get();
            //hot news
                                    Elements hotNews = doc3.select("div[class=box_hot_news]");
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
                                    
                                    //Xem nhieu nhat
                                    Elements sHotNews = doc.select("div[id=xemnhieunhat]").select("h2[class=txt_main_category]").select("a");
                                    System.out.println(sHotNews.text());
                                    sHotNews = doc.select("div[class=content_box_category width_common]").select("li");
                                    for(Element sHot:sHotNews){
                                            String _address = sHot.select("a").attr("href");
                                            String _title = sHot.select("a").text();
                                            String _image = sHot.select("a").select("img").attr("src");
                                            String _content = sHot.select(".news_lead").text();
                                            System.out.println("------------------------Xem nhieu nhat-----------------------------");
                                            System.out.println("        Address: "+ _address);
                                            System.out.println("        Title: "+ _title);
                                            System.out.println("        Image: "+ _image);
                                            System.out.println("        Content: " + _content);
                                }
                            }
                    }else{
                            System.out.println("Link: http://vnexpress.net"+ link.select("a").attr("href"));
                            
                            Document doc1 = Jsoup.connect("http://vnexpress.net" + link.select("a").attr("href")).get();
                            Elements links2 = doc1.select("div[class=mid_header width_common]").select("ul[id=breakumb_web]").select("li");   
                            for(Element link1: links2){
                                    String url = link1.select("a").attr("href");
                                    if(!(url.substring(0, url.indexOf("/")).equals("http:"))){
                                        url = "http://vnexpress.net" + url;
                                    }
                                    String title = link1.text();
                                    System.out.println("        Link child: " + url);
                                    System.out.println("        Title child: "+ title);
                                    //Main news
                                    Document doc2 = Jsoup.connect(url).get();
                                    Elements medias = doc2.select("div[class=block_mid_new]").select ("li");
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
                                    i++;
                                    }
                                    //Hot news
                                    Document doc3 = Jsoup.connect(url).get();
                                    Elements hotNews = doc3.select("div[class=box_hot_news]");
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
                                    
                                    //Xem nhieu nhat
                                    Elements sHotNews = doc.select("div[id=xemnhieunhat]").select("h2[class=txt_main_category]").select("a");
                                    System.out.println(sHotNews.text());
                                    sHotNews = doc.select("div[class=content_box_category width_common]").select("li");
                                    for(Element sHot:sHotNews){
                                            String _address = sHot.select("a").attr("href");
                                            String _title = sHot.select("a").text();
                                            String _image = sHot.select("a").select("img").attr("src");
                                            String _content = sHot.select(".news_lead").text();
                                            System.out.println("------------------------Xem nhieu nhat-----------------------------");
                                            System.out.println("        Address: "+ _address);
                                            System.out.println("        Title: "+ _title);
                                            System.out.println("        Image: "+ _image);
                                            System.out.println("        Content: " + _content);
                                }
                            }
                    }       
                }              
            }
            System.out.println(i);
    } catch (IOException e) {
        System.out.println("ERROR Crawler processPage");
}
            
			return list;
    }
}
	