
package NewsCrawler;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.thaonguyen.mongodbconnector.MongoDBConnectorForCrawler;

/**
 *
 * @author Thai
 */
public class Baomoi extends News {
	private static final String URL = "http://www.baomoi.com";
	private MongoDBConnectorForCrawler conn;
	public String _topicNews; // name of newspaper

	@Override
	public void Execution() {
		String url = URL;
		conn = new MongoDBConnectorForCrawler("localhost", 27017, DATABASE_NAME);
		System.out.println("Crawling: " + URL);
		try {
			System.setProperty("http.proxyHost", "127.0.0.1");
			System.setProperty("http.proxyPort", "8182");
//			System.out.println("*********************Báo mới*******************");
			Document doc = Jsoup.connect(url).timeout(TIMEOUT_PERIOD).get();
			Elements baoMoi = doc.select("div[class=header-wrap]").select("li[class=parent]");
			for (Element _baoMoi : baoMoi) {
				Elements temp = _baoMoi.select("ul[class=child]");
				// child link solution
				if (!temp.isEmpty()) {
					temp = temp.select("li").select("a");
					_baoMoi.select("ul[class=child").remove();
					_topic = _baoMoi.select("li[class=parent").select("a").text();
					for (Element temp1 : temp) {
						for (int i = 1; i <= 1; i++) {
							String _newUrl = temp1.select("a").attr("href");
							_newUrl = _newUrl.substring(0, _newUrl.indexOf('.'));
							_newUrl = url + _newUrl + "/trang" + i + ".epi";
//							System.out.println("Topic: " + _topic);

//							System.out.println();
							hotNews(_newUrl);
//							System.out.println();
							mainNews(_newUrl);
						}
					}
				} else { // parents link solution
					_topic = _baoMoi.text();
					for (int i = 1; i <= 1; i++) {
//						System.out.println("Topic: " + _topic);

						String _newUrl = _baoMoi.select("a").attr("href");
						_newUrl = _newUrl.substring(0, _newUrl.indexOf('.'));
						_newUrl = url + _newUrl + "/trang" + i + ".epi";

//						System.out.println();
						hotNews(_newUrl);
//						System.out.println();
						mainNews_2(_newUrl);

					}
				}
			}
		} catch (Exception e) {
//			System.out.println(e);
		}
		conn.close();
	}

	@Override
	public void hotNews(String url) {
		try {
			Document doc;
			doc = Jsoup.connect(url).timeout(TIMEOUT_PERIOD).get();
			Elements link = doc.select("div[class=main]").select("article");
			Elements name;
			for (Element _hotNews : link) {
				_address = url + _hotNews.select("a").attr("href");
				_title = _hotNews.select("a").attr("title");
				_image = _hotNews.select("img").attr("src");

				name = _hotNews.select("p[class=meta]");
				_topicNews = name.select("a").first().text();

//				System.out.println("        Address: " + _address);
//				System.out.println("        Title: " + _title);
//				System.out.println("        Image: " + _image);
//				System.out.println("        Address's name: " + _topicNews);
				
				if (_address.isEmpty() || _title.isEmpty() || _image.isEmpty() || _topic.isEmpty() || _topicNews.isEmpty()) {
//					System.out.println("NOT ADD");
				} else {
//					System.out.println("ADD");
					try {
						_tmpImage = getImage(_image);
						conn.AddNews(_address, _title, _image, _tmpImage.getHeight(null), _tmpImage.getWidth(null), _topic, "", _topicNews);
					} catch (Exception e) {
						e.printStackTrace();
//						System.out.println("ADD FAIL");
					}
				}
			}
		} catch (IOException ex) {
			Logger.getLogger(Baomoi.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void mainNews(String url) {
		try {
			Document doc = Jsoup.connect(url).timeout(TIMEOUT_PERIOD).get();
			Elements link = doc.select("div[class=cat-content]").select("article");
			Elements name;

			for (Element _mainNews : link) {
				_address = url + _mainNews.select("a").attr("href");
				_image = _mainNews.select("img").attr("src");
				_title = _mainNews.select("h2").text();
				_content = _mainNews.select("p[class=summary]").text();

				name = _mainNews.select("p[class=meta]");
				_topicNews = name.select("a").first().text();

//				System.out.println("        Address: " + _address);
//				System.out.println("        Title: " + _title);
//				System.out.println("        Image: " + _image);
//				System.out.println("        Content: " + _content);
//				System.out.println("        Address's name: " + _topicNews);
				
				if (_address.isEmpty() || _title.isEmpty() || _image.isEmpty() || _topic.isEmpty() || _topicNews.isEmpty()) {
//					System.out.println("NOT ADD");
				} else {
//					System.out.println("ADD");
					try {
						_tmpImage = getImage(_image);
						conn.AddNews(_address, _title, _image, _tmpImage.getHeight(null), _tmpImage.getWidth(null), _topic, "", _topicNews);
					} catch (Exception e) {
						e.printStackTrace();
//						System.out.println("ADD FAIL");
					}
				}
			}
		} catch (IOException ex) {
			Logger.getLogger(Baomoi.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void mainNews_2(String url) throws IOException {
		Document doc;
		try {
			doc = Jsoup.connect(url).timeout(TIMEOUT_PERIOD).get();
			Elements link = doc.select("section[class=content-list]").select("article");
			Elements name;
			for (Element _mainNews : link) {
				_address = url + _mainNews.select("a").attr("href");
				_image = _mainNews.select("img").attr("src");
				_title = _mainNews.select("h2").text();
				_content = _mainNews.select("p[class=summary]").text();

				name = _mainNews.select("header").select("p[class=meta]");
				;
				_topicNews = name.select("a").first().text();

//				System.out.println("        Address: " + _address);
//				System.out.println("        Title: " + _title);
//				System.out.println("        Image: " + _image);
//				System.out.println("        Content: " + _content);
//				System.out.println("        Address's name: " + _topicNews);
				
				if (_address.isEmpty() || _title.isEmpty() || _image.isEmpty() || _topic.isEmpty() || _topicNews.isEmpty()) {
//					System.out.println("NOT ADD");
				} else {
//					System.out.println("ADD");
					try {
						_tmpImage = getImage(_image);
						conn.AddNews(_address, _title, _image, _tmpImage.getHeight(null), _tmpImage.getWidth(null), _topic, "", _topicNews);
					} catch (Exception e) {
						e.printStackTrace();
//						System.out.println("ADD FAIL");
					}
				}
			}
		} catch (IOException ex) {
			Logger.getLogger(Baomoi.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	protected String getBigImage(String url) {
		// TODO Auto-generated method stub
		return null;
	}
}
