package NewsCrawler;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.thaonguyen.mongodbconnector.*;

public class VNExpress extends News {
	public String LinkTitle;
	public String Title;
	public String _currentTopic;
	private MongoDBConnectorForCrawler conn;

	@Override
	public void Execution() {
		conn = new MongoDBConnectorForCrawler("localhost", 27017, DATABASE_NAME);
		System.out.println("Crawling: http://vnexpress.net/");
//		System.out.println("*********************VNExpress Crawler*****************: ");
		System.setProperty("http.proxyHost", "127.0.0.1");
		System.setProperty("http.proxyPort", "8182");
		try {
			Document doc = Jsoup.connect("http://vnexpress.net/").timeout(TIMEOUT_PERIOD).get();
			Elements links = doc.select("ul[class=list_menu_header]").select("li");
			Element links1 = links.first();
			for (Element link : links) {
				if (!link.text().equals(links1.text())) {
//					System.out.println(link.text());
					_currentTopic = link.text();
					if (link.select("a").attr("href").substring(0, link.select("a").attr("href").indexOf('p') + 1)
							.equals("http")) {
						Document doc1 = Jsoup.connect(link.select("a").attr("href")).timeout(TIMEOUT_PERIOD).get();
						Elements links2 = doc1.select("div[class=mid_header width_common]")
								.select("ul[id=breakumb_web]").select("li");
						for (Element link1 : links2) {
							String url = link1.select("a").attr("href");
							if (!(url.substring(0, url.indexOf('p') + 1).equals("http"))) {
								url = link.select("a").attr("href") + url;
//								System.out.println();
								hotNews(url);
//								System.out.println();
								mainNews(url);
							}
						}
					} else {
						Document doc1 = Jsoup.connect("http://vnexpress.net" + link.select("a").attr("href")).timeout(TIMEOUT_PERIOD).get();
						Elements links2 = doc1.select("div[class=mid_header width_common]")
								.select("ul[id=breakumb_web]").select("li");
						for (Element link1 : links2) {
							String url = link1.select("a").attr("href");
							if (!(url.substring(0, url.indexOf("/")).equals("http:"))) {
								url = "http://vnexpress.net" + url;
//								System.out.println();
								hotNews(url);
//								System.out.println();
								mainNews(url);
							}
						}
					}
				}
			}
		} catch (Exception e) {
//			System.out.println(e);
		}

		conn.close();
	}

	public void hotNews(String url) {
		try {
			Document doc = Jsoup.connect(url).timeout(TIMEOUT_PERIOD).get();
			Elements hotNews = doc.select("div[class=box_hot_news]");
			for (Element hot : hotNews) {
				_address = hot.select(".block_news_big").select("a").attr("href");
				_title = hot.select("a").text();
				_content = hot.select("h4[class=news_lead]").text();
				
				_image = getBigImage(_address);
				if (_image.isEmpty()) {
					_image = hot.select(".block_news_big").select("a").select("img").attr("src");
				}
//				System.out.println("----------------------------Hot News-------------------------");
//				System.out.println("Address: " + _address);
//				System.out.println("Title: " + _title);
//				System.out.println("Image: " + _image);
//				System.out.println("Content: " + _content);
//				System.out.println("Topic: " + _currentTopic);

				if (_address.isEmpty() || _title.isEmpty() || _image.isEmpty() || _content.isEmpty()) {
//					System.out.println("NOT ADD");
				} else {
//					System.out.println("ADD");
					try {
						_tmpImage = getImage(_image);
						conn.AddNews(_address, _title, _image, _tmpImage.getHeight(null), _tmpImage.getWidth(null), _currentTopic, _content, "VNExpress");
					} catch (Exception e) {
						e.printStackTrace();
//						System.out.println("ADD FAIL");
					}
				}
			}
		} catch (IOException e) {

		}
	}

	public void mainNews(String url) {
		try {
			Document doc = Jsoup.connect(url).timeout(TIMEOUT_PERIOD).get();
			Elements medias = doc.select("div[class=block_mid_new]").select("li");
			for (Element media : medias) {
				_address = media.select("a").attr("href");
				_title = media.select("a").text();
				_content = media.select(".news_lead").text();
				
				_image = getBigImage(_address);
				if (_image.isEmpty()) {
					_image = media.select("a").select("img").attr("src");
				}
				
//				System.out.println("------------------------Tin chinh-----------------------------");
//				System.out.println("                Address: " + _address);
//				System.out.println("                Title: " + _title);
//				System.out.println("                Image: " + _image);
//				System.out.println("                Content: " + _content);
//				System.out.println("                Topic: " + _currentTopic);

				if (_address.isEmpty() || _title.isEmpty() || _image.isEmpty() || _content.isEmpty()) {
//					System.out.println("NOT ADD");
				} else {
//					System.out.println("ADD");
					try {
						_tmpImage = getImage(_image);
						conn.AddNews(_address, _title, _image, _tmpImage.getHeight(null), _tmpImage.getWidth(null), _currentTopic, _content, "VNExpress");
					} catch (Exception e) {
						e.printStackTrace();
//						System.out.println("ADD FAIL");
					}
				}
			}
		} catch (IOException e) {

		}
	}

	@Override
	protected String getBigImage(String url) {
		String resultURL = "";

		try {
			Document doc = Jsoup.connect(url).timeout(TIMEOUT_PERIOD).get();
			Elements image = doc.select("div[id=detail_page],div[class=main_content_detail width_common]").select(
					"div[class=fck_detail width_common block_ads_connect],div[class=block_thumb_slide_show],div[class=fck_detail width_common]");
			resultURL = image.select("img").attr("src").toString();
		} catch (IOException e) {
			System.out.println(e);
		}

		return resultURL;
	}
}
