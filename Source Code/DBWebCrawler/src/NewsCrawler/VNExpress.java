package NewsCrawler;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class VNExpress implements News {
    public static final  String URL = "http://vnexpress.net/";
    private String _address;
    private String _title;
    private String _image;
    private String _content;
    public String _topic;
    
    @Override
    public void Execution() {
        String url = URL;
        System.out.println("*********************VNExpress Crawler*****************: ");
        System.setProperty("http.proxyHost", "127.0.0.1");
	System.setProperty("http.proxyPort", "8182");
	try {
            Document doc = Jsoup.connect(url).get();
            Elements links = doc.select("ul[class=list_menu_header]").select("li");
            Element links1 = links.first();
            for (Element link : links) {
                if (!link.text().equals(links1.text())) {
                    System.out.println(link.text());
                    _topic = link.text();
                    if (link.select("a").attr("href").substring(0, link.select("a").attr("href").indexOf('p') + 1).equals("http")) {
                        Document doc1 = Jsoup.connect(link.select("a").attr("href")).get();
                        Elements links2 = doc1.select("div[class=mid_header width_common]").select("ul[id=breakumb_web]").select("li");
                        for (Element link1 : links2) {
                            url = link1.select("a").attr("href");
                            if (!(url.substring(0, url.indexOf('p') + 1).equals("http"))) {
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
                    } else {
                        Document doc1 = Jsoup.connect("http://vnexpress.net" + link.select("a").attr("href")).get();
			Elements links2 = doc1.select("div[class=mid_header width_common]").select("ul[id=breakumb_web]").select("li");
			for (Element link1 : links2) {
                            url = link1.select("a").attr("href");
                            if (!(url.substring(0, url.indexOf("/")).equals("http:"))) {
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

    @Override
    public void hotNews(String url) {
	try {
            Document doc = Jsoup.connect(url).get();
            Elements hotNews = doc.select("div[class=box_hot_news]");
            for (Element hot : hotNews) {
                _address = hot.select(".block_news_big").select("a").attr("href");
                _title = hot.select("a").text();
                _image = hot.select(".block_news_big").select("a").select("img").attr("src");
                _content = hot.select("h4[class=news_lead]").text();
                System.out.println("----------------------------Hot News-------------------------");
                System.out.println("Address: " + _address);
                System.out.println("Title: " + _title);
                System.out.println("Image: " + _image);
                System.out.println("Content: " + _content);
                System.out.println("Topic: " + _topic);
            }
        } catch (IOException e) {
                System.out.println(e);
        }
    }

    @Override
    public void mainNews(String url) {
	try {
            Document doc = Jsoup.connect(url).get();
            Elements medias = doc.select("div[class=block_mid_new]").select("li");
            for (Element media : medias) {
		_address = media.select("a").attr("href");
		_title = media.select("a").text();
		_image = media.select("a").select("img").attr("src");
		_content = media.select(".news_lead").text();
		System.out.println("------------------------Tin chinh-----------------------------");
		System.out.println("                Address: " + _address);
		System.out.println("                Title: " + _title);
		System.out.println("                Image: " + _image);
		System.out.println("                Content: " + _content);
		System.out.println("                Topic: " + _topic);
            }
	} catch (IOException e) {
            System.out.println(e);
        }
    }
}
