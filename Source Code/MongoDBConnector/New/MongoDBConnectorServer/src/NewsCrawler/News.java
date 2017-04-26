package NewsCrawler;

import java.awt.Image;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;

public abstract class News {
	public static final String DATABASE_NAME = "test";
	public static final int TIMEOUT_PERIOD = 20000; /* 20s */
	
	protected String _address;
	protected String _title;
	protected String _image;
	protected String _content;
	protected String _topic;
	protected Image _tmpImage;

	public abstract void Execution();

	protected abstract void hotNews(String url);

	protected abstract void mainNews(String url);

	protected abstract String getBigImage(String url);

	protected Image getImage(String urlImage) throws MalformedURLException {
		URL url = new URL(urlImage);
		Image image = new ImageIcon(url).getImage();
		return image;
	}
}
