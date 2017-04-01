package crawler_;

public class Data {
	private String _url; // Child Link and child child link
	private String _title; // Title 
	private String _address; // News Address
	private String _content; // Decripstion
	private String _image; // Image's link
	
	public Data(String _url , String _address , String _title, String _image , String _content){
		this._address = _address;
		this._url = _url;
		this._title = _title;
		this._image = _image;
		this._content = _content;
	}
	public String getUrl(){
		return _url;
	}
	public String getImage(){
		return _image;
	}
	public String getTitle(){
		return _title;
	}
	public String getaddress(){
		return _address;
	}
	public String getcontent(){
		return _content;
	}
	
}