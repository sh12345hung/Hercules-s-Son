package webcrawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class VnExpress extends Exe{
    private ArrayList<Data> dataList;
    private String _address;
    private String _title;
    private String _image;
    private String _description;
    
    @Override
    public void Execution(String url){ 
        System.setProperty("http.proxyHost", "127.0.0.1");
        System.setProperty("http.proxyPort", "8182");
        System.out.println("*********************VNExpress*****************: ");
        try {
                Document doc = Jsoup.connect(url).get();
                Elements links = doc.select("ul[class=list_menu_header]").select("li");
                Element links1 = links.first();
                for (Element link: links){
                if(!link.text().equals(links1.text())){
                System.out.println(link.text());
                    if(link.select("a").attr("href").substring(0,link.select("a").attr("href").indexOf('p')+1).equals("http")){
                        Document doc1 = Jsoup.connect(link.select("a").attr("href")).get();
                        Elements links2 = doc1.select("div[class=mid_header width_common]").select("ul[id=breakumb_web]").select("li");
                        for(Element link1: links2){
                                url = link1.select("a").attr("href");
                                if(!(url.substring(0,url.indexOf('p')+1).equals("http"))){
                                        url = link.select("a").attr("href") + url;
                                        System.out.println();
                                        hotNews(url);
                                        System.out.println();
                                        mainNews(url);
                                }
                                else {
                                        System.out.println();
                                        hotNews(url);
                                        System.out.println();
                                        mainNews(url);
                                }
                        }
                }else{
                        Document doc1 = Jsoup.connect("http://vnexpress.net" + link.select("a").attr("href")).get();
                        Elements links2 = doc1.select("div[class=mid_header width_common]").select("ul[id=breakumb_web]").select("li");   
                        for(Element link1: links2){
                                url = link1.select("a").attr("href");
                                if(!(url.substring(0, url.indexOf("/")).equals("http:"))){
                                        url = "http://vnexpress.net" + url;
                                        System.out.println();
                                        hotNews(url);
                                        System.out.println();
                                        mainNews(url);
                                }
                                else {
                                        System.out.println();
                                        hotNews(url);
                                        System.out.println();
                                        mainNews(url);
                                }
                        }
                 }
            }
        }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    ///////////////////////////////////
    
    
    @Override
    public void hotNews (String url){
        dataList = new ArrayList<>();
        try {
            /*Connection.Response res = Jsoup.connect(url)
                    .method(Method.POST)
                    .userAgent("Mozilla/5.0 (Windows NT 6.0) Chrome/19.0.1084.46 Safari/536.5")
                    .timeout(100 * 1000)
                    .ignoreHttpErrors(true)
                    .execute();
       
            Map<String, String> cookies = res.cookies();
            Document doc = Jsoup.connect(url).method(Method.POST)
                .method(Method.POST)
                .userAgent("Mozilla/5.0 (Windows NT 6.0) Chrome/19.0.1084.46 Safari/536.5")
                .timeout(500 * 1000)
                .ignoreHttpErrors(true)
                .cookies(cookies)
                .get();*/
            Document doc = Jsoup.connect(url).get();
            Elements hotNews = doc.select("div[class=box_hot_news]");
            for(Element hot:hotNews){
                _address = hot.select(".block_news_big").select("a").attr("href");
                _title = hot.select("a").text();
                _image = hot.select(".block_news_big").select("a").select("img").attr("src");
                _description = hot.select("h4[class=news_lead]").text();
                
                Data data = new Data(_address, _title, _image, _description);
                dataList.add(data);
                
            }
            for(Data data: dataList){
                System.out.println("Address: " + data.getAddress());
                System.out.println("Title: " + data.getTitle());
                System.out.println("Image: " + data.getImage());
                System.out.println("Description: " + data.getDescription());
            }
        } catch (IOException ex) {
            Logger.getLogger(VnExpress.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    @Override
    public void mainNews (String url){
        dataList = new ArrayList<>();
        try {
            /*Connection.Response res = Jsoup.connect(url)
                    .method(Method.POST)
                    .userAgent("Mozilla/5.0 (Windows NT 6.0) Chrome/19.0.1084.46 Safari/536.5")
                    .timeout(100 * 1000)
                    .ignoreHttpErrors(true)
                    .execute();
       
            Map<String, String> cookies = res.cookies();
            Document doc = Jsoup.connect(url).method(Method.POST)
                    .method(Method.POST)
                    .userAgent("Mozilla/5.0 (Windows NT 6.0) Chrome/19.0.1084.46 Safari/536.5")
                    .timeout(500 * 1000)
                    .ignoreHttpErrors(true)
                    .cookies(cookies)
                    .get();*/
            Document doc = Jsoup.connect(url).get();
            Elements medias = doc.select("div[class=block_mid_new]").select ("li");
            for(Element media:medias){
                _address = media.select("a").attr("href");
                _title = media.select("a").text();
                _image = media.select("a").select("img").attr("src");
                _description = media.select(".news_lead").text();
                
                Data data = new Data(_address, _title, _image, _description);
                dataList.add(data);
            }
            for(Data data: dataList){
                System.out.println("Address: " + data.getAddress());
                System.out.println("Title: " + data.getTitle());
                System.out.println("Image: " + data.getImage());
                System.out.println("Description: " + data.getDescription());
            }
        } catch (IOException ex) {
            Logger.getLogger(VnExpress.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
  
    public void mainVNE () throws IOException {
        Execution("http://vnexpress.net/");
	}
}