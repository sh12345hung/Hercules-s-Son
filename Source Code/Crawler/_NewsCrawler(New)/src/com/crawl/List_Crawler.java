package com.crawl;
import java.util.ArrayList;
	
public abstract class List_Crawler {  
	
		public abstract ArrayList<Data> Process_Web();
		/*
		 *  Sử dụng abstract để ta chỉ cần định nghĩa lại hàm Process_web 
		 *  khi mỗi lần sử dụng.
		 */
}
