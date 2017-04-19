package NewsCrawler;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.thaonguyen.mongodbconnector.MongoDBConnectorForCrawler;

public class ZingNews implements News {
	private MongoDBConnectorForCrawler conn;
	public String _currentTopic;
	public String url = "http://news.zing.vn/";

	@Override
	public void Execution() {
		conn = new MongoDBConnectorForCrawler("localhost", 27017, DATABASE_NAME);
		System.out.println("Crawling: " + url);
		try {
			Document doc = Jsoup.connect(url).get();
			Elements NewsZing = doc.select("nav[class=categories] ").select("ul").select("li")
					.select("div[class=subcate]").remove().select("li");
			for (Element Zing : NewsZing) {
				Elements temp = Zing.select("li").select("a");
				if (!temp.isEmpty()) {
					for (Element temp1 : temp) {
						_currentTopic = temp1.select("a").text();
						CheckTopic(_currentTopic);
						String _newUrl = temp1.select("a").attr("abs:href");
//						System.out.println(_newUrl);
						hotNews(_newUrl);
						mainNews(_newUrl);
//						System.out.println(_currentTopic);
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
			Document doc = Jsoup.connect(url).get();
			Elements medias = doc.select("div[class=block_mid_new]").select("li");
			for (Element media : medias) {
				String _address = media.select("a").attr("href");
				String _title = media.select("a").text();
				String _image = media.select("a").select("img").attr("src");
				String _content = media.select(".news_lead").text();
//				System.out.println("------------------------Tin chinh-----------------------------");
//				System.out.println("                Address: " + _address);
//				System.out.println("                Title: " + _title);
//				System.out.println("                Image: " + _image);
//				System.out.println("                Content: " + _content);
//				System.out.println("                Topic: " + this._currentTopic);
				
				if (_address.isEmpty() || _title.isEmpty() || _image.isEmpty() || _content.isEmpty() || this._currentTopic.isEmpty()) {
//					System.out.println("NOT ADD");
				} else {
//					System.out.println("ADD");
					try {
						conn.AddNews(_address, _title, _image, _currentTopic, _content, "ZingNews");
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
	public void mainNews(String url) {
		try {
			Document doc = Jsoup.connect(url).get();
			Elements link = doc.select("section[class=cate_content]").select("article");
			for (Element _mainNews : link) {
				String _address = _mainNews.select("a").attr("abs:href");
				String _image = _mainNews.select("img").attr("src");
				String _title = _mainNews.select("header").select("p[class=title]").text();
				String _description = _mainNews.select("p[class=summary]").text();
//				System.out.println("Address: " + _address);
//				System.out.println("Title: " + _title);
//				System.out.println("Image: " + _image);
//				System.out.println("Description: " + _description);
//				System.out.println("*************************");
//				System.out.println(" ");
//				System.out.println("Topic: " + _currentTopic);
				
				if (_address.isEmpty() || _title.isEmpty() || _image.isEmpty() || _description.isEmpty() || this._currentTopic.isEmpty()) {
//					System.out.println("NOT ADD");
				} else {
//					System.out.println("ADD");
					try {
						conn.AddNews(_address, _title, _image, _currentTopic, _description, "ZingNews");
					} catch (Exception e) {
						e.printStackTrace();
//						System.out.println("ADD FAIL");
					}
				}
			}

		} catch (IOException e) {

		}
	}

	private String CheckTopic(String _currentTopic) {
		String a = "";
		switch (_currentTopic) {
		/* Thời Sự */
		case "Đời sống":
		case "Đô thị":
		case "Giao thông":
		case "Quốc phòng":
			a = "Thế Giới";
//			System.out.println("Topic: " + a);
			break;
		/* Thế giới */
		case "Quân sự":
		case "Tư liệu":
		case "Người Việt 4 phương":
		case "Phân tích":
			a = "Thời Sự";
//			System.out.println("Topic: " + a);
			break;
		/* Kinh Tế */
		case "Tài chính":
		case "Chứng khoán":
		case "Bất động sản":
		case "Thông tin doanh nghiệp":
		case "Doanh nhân":
			a = "Kinh Doanh";
//			System.out.println("Topic: " + a);
			break;
		/* Pháp Luật */
		case "Pháp đình":
		case "Vụ án":
			a = "Pháp Luật";
//			System.out.println("Topic: " + a);
			break;
		/* Xuất Bản */
		case "Tin tức xuất bản":
		case "Sách hay":
		case "Tác giả":
			a = "Xuất Bản";
//			System.out.println("Topic: " + a);
			break;
		/* Thể Thao */
		case "Thể thao Việt Nam":
		case "Cup Châu Âu":
		case "Thể thao Thế giới":
		case "Bóng đá":
		case "Bóng đá Anh":
		case "Bóng đá Việt Nam":
		case "Bóng rổ":
		case "Video bóng đá":
		case "Hậu trường thể thao":
			a = "Thể thao";
//			System.out.println("Topic: " + a);
			break;
		/* Khoa Học - Công Nghệ */
		case "Điện thoại":
		case "Máy tính bảng":
		case "Ứng dụng di động":
			a = "Khoa Học - Công Nghệ";
//			System.out.println("Topic: " + a);
			break;
		/* Xe */
		case "Xe máy":
		case "Ô-tô":
		case "Xe độ":
		case "Siêu xe":
			a = "Xe";
//			System.out.println("Topic: " + a);
			break;
		/* Giải Trí */
		case "Sao Việt":
		case "Sao Châu Á":
		case "Sao Hollywood":
		case "Nhạc Việt":
		case "Nhạc Hàn":
		case "Nhạc Âu Mỹ":
		case "Phim chiếu rạp":
		case "Phim truyền hình":
		case "Thời trang sao":
		case "Mặc đẹp":
		case "Làm đẹp":
		case "Gương mặt trẻ":
		case "Cộng đồng mạng":
		case "Sự kiện":
		case "Cười":
		case "Game Show	":
			a = "Giải Trí";
//			System.out.println("Topic: " + a);
			break;
		/* Giáo Dục */
		case "Tuyển sinh 2017":
		case "Tư vấn":
		case "Du học":
			a = "Giáo Dục";
//			System.out.println("Topic: " + a);
			break;
		/* Sức Khỏe */
		case "Khỏe đẹp":
		case "Dinh dưỡng":
		case "Mẹ và Bé":
		case "Bệnh thường gặp":
			a = "Sức Khỏe";
//			System.out.println("Topic: " + a);
			break;
		/* Du Lịch */
		case "Địa điểm du lịch":
		case "Kinh nghiệm du lịch":
		case "Phượt":
		case "Địa điểm ăn uống":
		case "Món ngon":
			a = "Du Lịch";
//			System.out.println("Topic: " + a);
			break;
		}
		return this._currentTopic = a;
	}
}
