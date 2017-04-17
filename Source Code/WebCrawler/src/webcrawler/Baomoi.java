/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler;

/**
 *
 * @author Thai
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Baomoi extends Exe {

    private ArrayList<Data> dataList;
    private String _address;
    private String _title;
    private String _image;
    private String _description;
    public String _topic;
    public String _topicNews;   //name of newspaper

    @Override
    public void Execution(String url) {
        try {
            System.setProperty("http.proxyHost", "127.0.0.1");
            System.setProperty("http.proxyPort", "8182");
            System.out.println("*********************Báo mới*******************");
            Document doc = Jsoup.connect(url).get();
            Elements baoMoi = doc.select("div[class=header-wrap]").select("li[class=parent]");
            for (Element _baoMoi : baoMoi) {
                Elements temp = _baoMoi.select("ul[class=child]");
                //child link solution
                if (!temp.isEmpty()) { 
                    temp = temp.select("li").select("a");
                    _baoMoi.select("ul[class=child").remove();
                    _topic = _baoMoi.select("li[class=parent").select("a").text();
                    for (Element temp1 : temp) {
                        for (int i = 1; i <= 1; i++) {
                            String _newUrl = temp1.select("a").attr("href");
                            _newUrl = _newUrl.substring(0, _newUrl.indexOf('.'));
                            _newUrl = url + _newUrl + "/trang" + i + ".epi";
                            System.out.println("Topic: " + _topic);

                            System.out.println();
                            hotNews(_newUrl);
                            System.out.println();
                            mainNews(_newUrl);
                        }
                    }
                } else { //parents link solution
                    _topic = _baoMoi.text();
                    for (int i = 1; i <= 1; i++) {
                        System.out.println("Topic: " + _topic);

                        String _newUrl = _baoMoi.select("a").attr("href");
                        _newUrl = _newUrl.substring(0, _newUrl.indexOf('.'));
                        _newUrl = url + _newUrl + "/trang" + i + ".epi";

                        System.out.println();
                        hotNews(_newUrl);
                        System.out.println();
                        mainNews_2(_newUrl);

                    }
                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void hotNews(String url) {
        dataList = new ArrayList<>(); 
        try {
            Document doc; doc = Jsoup.connect(url).get();
            Elements link = doc.select("div[class=main]").select("article");
            Elements name;
            for (Element _hotNews : link) {
                _address = url + _hotNews.select("a").attr("href");
                _title = _hotNews.select("a").attr("title");
                _image = _hotNews.select("img").attr("src");
            
                name = _hotNews.select("p[class=meta]");
                _topicNews = name.select("a").first().text();
                
                Data data = new Data(_address, _title, _image, "", _topicNews);
                dataList.add(data);
            }
            for(Data data : dataList){
                System.out.println("        Address: " + data.getAddress());
                System.out.println("        Title: " + data.getTitle());
                System.out.println("        Image: " + data.getImage());
                System.out.println("        Address's name: " + data.getTopicNews());
            }
        } catch (IOException ex) {
            Logger.getLogger(Baomoi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void mainNews(String url) {
        
        dataList = new ArrayList<>();
        Document doc;
        try {
            doc = Jsoup.connect(url).get();
            Elements link = doc.select("div[class=cat-content]").select("article");
            Elements name;

            for (Element _mainNews : link) {
                _address = url + _mainNews.select("a").attr("href");
                _image = _mainNews.select("img").attr("src");
                _title = _mainNews.select("h2").text();
                _description = _mainNews.select("p[class=summary]").text();

                name = _mainNews.select("p[class=meta]");
                _topicNews = name.select("a").first().text();
                Data  data = new Data(_address, _title, _image, _description, _topicNews);
                dataList.add(data);
            }
            for (Data data: dataList){
                System.out.println("        Address: " + data.getAddress());
                System.out.println("        Title: " + data.getTitle());
                System.out.println("        Image: " + data.getImage());
                System.out.println("        Description: " + data.getDescription());
                System.out.println("        Address's name: " + data.getTopicNews());
            }
        } catch (IOException ex) {
            Logger.getLogger(Baomoi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

 
    public void mainNews_2(String url) throws IOException {
        dataList = new ArrayList<>();
        Document doc;
        try {
            doc = Jsoup.connect(url).get();
            Elements link = doc.select("section[class=content-list]").select("article");
            Elements name;
            for (Element _mainNews : link) {
                _address = url + _mainNews.select("a").attr("href");
                _image = _mainNews.select("img").attr("src");
                _title = _mainNews.select("h2").text();
                _description = _mainNews.select("p[class=summary]").text();

                name = _mainNews.select("header").select("p[class=meta]");;
                _topicNews = name.select("a").first().text();
            
                Data data = new Data(_address, _title, _image, _description, _topicNews);
                dataList.add(data);
            }
            for (Data data: dataList){
                System.out.println("        Address: " + data.getAddress());
                System.out.println("        Title: " + data.getTitle());
                System.out.println("        Image: " + data.getImage());
                System.out.println("        Description: " + data.getDescription());
                System.out.println("        Address's name: " + data.getTopicNews());
            }
        } catch (IOException ex) {
            Logger.getLogger(Baomoi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mainBM () throws IOException {
           Execution("http://www.baomoi.com");
    }
}
