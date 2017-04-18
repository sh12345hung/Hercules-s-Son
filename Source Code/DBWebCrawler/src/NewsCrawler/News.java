package NewsCrawler;

public interface News {
	public static final String DATABASE_NAME = "test";

	public void Execution();

	public void hotNews(String url);

	public void mainNews(String url);
}
