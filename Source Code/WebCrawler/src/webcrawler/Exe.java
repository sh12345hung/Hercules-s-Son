/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler;
/**
 *
 * @author Thai
 */
public interface Exe {
        public static final String DATABASE_NAME = "test";
        
        public  void Execution(String url);
        
        public  void hotNews(String url);
        
        public  void mainNews (String url);   
}
