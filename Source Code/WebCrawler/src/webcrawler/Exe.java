/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler;

import java.util.ArrayList;

/**
 *
 * @author Thai
 */
public abstract class Exe {
        public abstract void Execution(String url);
        
        public abstract void hotNews(String url);
        
        public abstract void mainNews (String url);
        
        //public abstract void mainNews_2(String url);
    
    public void pushDataToServer(ArrayList<Data> datas){
        
    }
    
}
