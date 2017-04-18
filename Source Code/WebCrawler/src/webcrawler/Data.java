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
public class Data {
    private String _address;
    private String _title;
    private String _image;
    private String _content;
    private String _topic;
    private String _topicNews;
    
    public Data (String _address, String _title, String _image, String _content, String _topic, String _topicNews) {
        this._address =_address;
        this._title = _title;
        this._image = _image;
        this._content = _content;
        this._topic = _topic;
        this._topicNews = _topicNews;
    }
    
     public Data(String _address, String _title, String _image, String _content, String _topicNews) {
        this._address =_address;
        this._title = _title;
        this._image = _image;
        this._content = _content;
        this._topicNews = _topicNews;
    }

    public String getAddress(){
        return _address;
    }
    
    public void setAddress(String _address) {
        this._address = _address;
    }
    
    public String getTitle (){
       return _title;
    }
    
    public void setTitle (String _title){
        this._title = _title;
    }
    
    public String getImage () {
        return _image;
    }
    
    public void setImage (String _image){
        this._image = _image;
    }
    
    public String getContent (){
        return _content;
    }
    
    public void setContent (String _content){
        this._content = _content;
    }
    
    public String getTopic (){
        return _topic;
    }
    
    public void setTopic (String _topic) {
        this._topic = _topic;
    }
    
    public String getTopicNews(){
        return _topicNews;
    }
    
    public void setTopicNews (String _topicNews){
         this._topicNews = _topicNews;
    }
}
