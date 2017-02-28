package com.thaonguyen.MongoDBConnector;

import java.util.Date;

import org.bson.Document;


public class News {
	private String _id;
	private String _title;
	private String _url;
	private String _headerText;
	private String _imageUrl;
	private Date   _modified;

	public News(String Title, String URL) {
		super();

		this._id = "";
		this._title = Title;
		this._url = URL;
		this._headerText = "";
		this._imageUrl = "";
		this._modified = null;
	}

	public News(String Title, String URL, String HeaderText, String ImageURL, Date ModifiedDate) {
		super();

		this._id = "";
		this._title = Title;
		this._url = URL;
		this._headerText = HeaderText;
		this._imageUrl = ImageURL;
		this._modified = ModifiedDate;
	}

	public News(News Obj) {
		super();

		this._id = Obj._id;
		this._title = Obj._title;
		this._url = Obj._url;
		this._headerText = Obj._headerText;
		this._imageUrl = Obj._imageUrl;
		this._modified = Obj._modified;
	}

	public String getID() {
		return _id;
	}

	public void setID(String _id) {
		this._id = _id;
	}

	public String getTitle() {
		return _title;
	}

	public void setTitle(String _title) {
		this._title = _title;
	}

	public String getURL() {
		return _url;
	}

	public void setURL(String _url) {
		this._url = _url;
	}

	public String getHeaderText() {
		return _headerText;
	}

	public void setHeaderText(String _headerText) {
		this._headerText = _headerText;
	}

	public String getImageURL() {
		return _imageUrl;
	}

	public void setImageURL(String _imageUrl) {
		this._imageUrl = _imageUrl;
	}

	public Date getModifiedDate() {
		return _modified;
	}

	public void setModifiedDate(Date _modified) {
		this._modified = _modified;
	}
	
	public Document toDocument() {
		Document doc = new Document("Title", this._title)
					       .append("URL", this._url)
					       .append("HeaderText", this._headerText)
					       .append("ImageURL", this._imageUrl)
					       .append("Modified", this._modified);
		return doc;
	}
}
