
package webcrawler;

/**
 *
 * Config By Phuc
 */
import java.io.IOException;
import java.util.Map;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
public class ZingNews {
    
    private String _address;
    private String _title;
    private String _image;
    private String _description;
    public String _topic;
    
    public void Execution (String url) throws IOException {
        System.out.println ("*********************ZingNews*******************");
      
        Document doc = Jsoup.connect(url).get();
        Elements NewsZing = doc.select("nav[class=categories] ").select("ul").select("li").select("div[class=subcate]").remove().select("li");	        
        for (Element Zing : NewsZing){
                Elements temp = Zing.select("li").select("a");  
                if(!temp.isEmpty()){                	
                    for (Element temp1 : temp) {                    	     
                    _topic = temp1.select("a").text(); 
                   CheckTopic(_topic);
                    String _newUrl = temp1.select("a").attr("abs:href");  	              
                    System.out.println(_newUrl);
              //      hotNews(_newUrl);
               //     mainNews(_newUrl); 
                    System.out.println(_topic);
                    }
                }
        	}
    }
    public void mainNews (String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements link = doc.select("section[class=cate_content]").select("article");
        for (Element _mainNews : link){
            _address = _mainNews.select("a").attr("abs:href");
            _image = _mainNews.select("img").attr("src");
            _title = _mainNews.select("header").select("p[class=title]").text();
            _description = _mainNews.select("p[class=summary]").text();
            System.out.println("Address: " + _address);
            System.out.println("Title: " + _title);
            System.out.println("Image: " + _image);
            System.out.println("Description: " + _description);
            System.out.println("*************************");
            System.out.println(" ");
        }
        }
    public void hotNews (String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements link = doc.select("section[class=cate_sidebar]").select("section[class=mostread]").select("article");
        for ( Element _hotNews : link){
            _address =  _hotNews.select("a").attr("abs:href");
            _title = _hotNews.select("header").select("p[class=title]").select("a").text();
            _image = _hotNews.select("img").attr("src");
            System.out.println("        Address: " + _address);
            System.out.println("        Title: " + _title);
            System.out.println("        Image: " + _image);
        }
        }
    private  String CheckTopic(String _topic){
    	String a = "";
   	 switch(_topic){
       /* Thời Sự */
       case "Đời sống":
       case "Đô thị":
       case "Giao thông":
       case "Quốc phòng":  a = "Thế Giới";System.out.println("Topic: " + a);
	   							 break;
	   		/* Thế giới  */     		   						 
       case "Quân sự":
       case "Tư liệu":
       case "Người Việt 4 phương":	              	
       case "Phân tích":  a = "Thời Sự";System.out.println("Topic: " + a);
	   							break;		
	   		/*  Kinh Tế */   
       case "Tài chính":
       case "Chứng khoán":
       case "Bất động sản":
       case "Thông tin doanh nghiệp":
       case "Doanh nhân":  a = "Kinh Doanh";System.out.println("Topic: " + a);
	   							break;
	   		/*  Pháp Luật */   
       case "Pháp đình":
       case "Vụ án":	a = "Pháp Luật";System.out.println("Topic: " + a);
								break;
			/* Xuất Bản */   
       case "Tin tức xuất bản":
       case "Sách hay":
       case "Tác giả":	a = "Xuất Bản";System.out.println("Topic: " + a);
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
       case "Hậu trường thể thao": a = "Thể thao";System.out.println("Topic: " + a);
									break;
			/* Khoa Học - Công Nghệ */
       case "Điện thoại":
       case "Máy tính bảng":
       case "Ứng dụng di động":a = "Khoa Học - Công Nghệ";System.out.println("Topic: " + a);
									break;
			/* Xe */						
       case "Xe máy":
       case "Ô-tô":
       case "Xe độ":
       case "Siêu xe": a = "Xe";System.out.println("Topic: " + a);
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
       case "Game Show	":	a = "Giải Trí";System.out.println("Topic: " + a);
									break;
				/* Giáo Dục */				
       case "Tuyển sinh 2017":
       case "Tư vấn":
       case "Du học":	a = "Giáo Dục";System.out.println("Topic: " + a);
								break;
			/* Sức Khỏe */
			case	"Khỏe đẹp":
			case	"Dinh dưỡng":
			case	"Mẹ và Bé":
			case	"Bệnh thường gặp":a = "Sức Khỏe";System.out.println("Topic: " + a);
								 break;
			/* Du Lịch */
			case	"Địa điểm du lịch":
			case	"Kinh nghiệm du lịch":
			case	"Phượt":
			case	"Địa điểm ăn uống":
			case	"Món ngon":a = "Du Lịch";System.out.println("Topic: " + a);
								break;
       }
   	 return this._topic = a;
   }
    public static void main (String[] arg) throws IOException{
        ZingNews _Zing = new ZingNews();
        _Zing.Execution("http://www.news.zing.vn");
    }
    
}