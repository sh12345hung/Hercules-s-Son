/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thai
 */
public class WebCrawler {
        public static void main(String[] args) throws IOException{
        Baomoi bm = new Baomoi();
        VnExpress vnE = new VnExpress();
        try {
            //bm.mainBM();
            vnE.mainVNE();
        } catch (IOException ex) {
            Logger.getLogger(WebCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       
        
    }
}
