package NewsCrawler;

import java.awt.Image;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.ImageIcon;

public interface News {
	public static final String DATABASE_NAME = "test";

	public void Execution();

	public void hotNews(String url);

	public void mainNews(String url);
        
        public void getImage (String url);
        
    /**
     *
     * @param urlImage
     * @throws MalformedURLException
     */
    public static void Size(String urlImage) throws MalformedURLException {
                URL url = new URL(urlImage);
            
		Image image = new ImageIcon(url).getImage();
		int imgWidth = image.getWidth(null);
		int imgHeight = image.getHeight(null);
		
		System.out.println("Height " + imgHeight);
		System.out.println("Widght " + imgWidth);
	}
            
}
