
package Data;

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
public class Kenh14 {
    
    private String _address;
    private String _title;
    private String _image;
    private String _description;
    public String _topic;
    
    public void Execution (String url) throws IOException {
        System.out.println ("*********************Kenh14*******************");
        Document doc = Jsoup.connect(url).get();
        Elements Kenh14_ = doc.select("div[id=k14-main-menu-wrapper]").select("ul").select("li");
        for (Element K14 : Kenh14_){
                Elements temp = K14.select("li").select("a");
                

                    for (Element temp1 : temp) {
                    _topic = temp1.text();
                    System.out.println("Topic: " + _topic);
                    String _newUrl = temp1.select("a").attr("abs:href");
                    System.out.println(_newUrl);
                 //   hotNews(_newUrl);
                  mainNews(_newUrl);
                    System.out.println();
                }                 
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
    
    public void mainNews (String url) throws IOException {
    Document doc = Jsoup.connect(url).get();
    Elements link = doc.select("div[class=kbw-content]").select("ul");
    for (Element _mainNews : link){
        _address = _mainNews.select("a").attr("abs:href");
        _image = _mainNews.select("div[class=knswli-left fl").select("img").attr("src");
        _title = _mainNews.select("div[class=knswli-right]").select("h3").select("a").attr("title");
        _description = _mainNews.select("div[class=knswli-right]").select("span[class=knswli-sapo sapo-need-trim]").text();
        System.out.println("Address: " + _address);
        System.out.println("Title: " + _title);
        System.out.println("Image: " + _image);
        System.out.println("Description: " + _description);
        System.out.println(" ");
        System.out.println(" ");
    }
    }
    
   
    
    public static void main (String[] arg) throws IOException{
        Kenh14 _Kenh14 = new Kenh14();
        _Kenh14.Execution("http://www.kenh14.vn");
    }
    
}
