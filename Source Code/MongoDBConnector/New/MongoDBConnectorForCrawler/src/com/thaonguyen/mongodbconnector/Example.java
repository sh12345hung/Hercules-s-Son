package com.thaonguyen.mongodbconnector;

import java.util.Date;

public class Example {

	public static void main(String[] args) {
		/* Open connection do database */
		MongoDBConnectorForCrawler conn = new MongoDBConnectorForCrawler("localhost", 27017, "test");
		
		/* Add news 1 */
		String URL = "http://baotintuc.vn/the-gioi/nguoi-anh-ram-ro-bieu-tinh-phan-doi-chia-tay-chau-au-20170325224125086.htm";
		String Title = "Người Anh rầm rộ biểu tình phản đối chia tay châu Âu";
		String ImageURL = "http://media.baotintuc.vn/2017/03/25/22/38/bieu-tinh-o-anh.jpg";
		String Description = "Hàng nghìn người dân Anh ngày 25/3 tuần hành trên các đường phố lớn tại thủ đô London của nước Anh, phản đối Brexit (Anh rời Liên minh châu Âu).";
		String Topic = "THEGIOI";
		
		try {
			conn.AddNews(URL, Title, ImageURL, Topic, Description, "VNExpress", (new Date()).toString());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		/* Add news 2 */
		URL = "http://eva.vn/lang-sao/hh-thu-thao-ke-chuyen-qua-khu-trong-ngay-nhan-bang-tot-nghiep-dai-hoc-c20a304022.html";
		Title = "HH Thu Thảo kể chuyện quá khứ trong ngày nhận bằng tốt nghiệp Đại học";
		ImageURL = "http://eva-img.24hstatic.com/upload/1-2017/images/2017-03-26/hh-thu-thao-ke-chuyen-qua-khu-trong-ngay-nhan-bang-tot-nghiep-dai-hoc-1-1490461664-width500height625.jpg";
		Description = "Hoa hậu Việt Nam 2012 từng bị bố mẹ cấm cản đi học vì gia đình không có đủ tiền cho con gái theo học.";
		String TOPIC = "THEGIOI";
		
		try {
			conn.AddNews(URL, Title, ImageURL, TOPIC, Description, "VNExpress", (new Date()).toString());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		/* Close connection do database */
		conn.close();
	}
}
