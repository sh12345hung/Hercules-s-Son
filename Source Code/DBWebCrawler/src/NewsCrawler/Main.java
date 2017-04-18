package NewsCrawler;

import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		List<News> list = new ArrayList<>();
		list.add(new VNExpress());
                list.add(new Baomoi());

		for (News news : list) {
			news.Execution();
		}
	}
}
