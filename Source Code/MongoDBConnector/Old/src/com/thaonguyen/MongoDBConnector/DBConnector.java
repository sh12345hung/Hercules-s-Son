package com.thaonguyen.MongoDBConnector;

import java.util.ArrayList;
import java.util.Iterator;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;

import java.util.Date;

public class DBConnector {
	public static final String DEFAULT_STANDARD_MONGODB_URI = "mongodb://admin:admin@ds157278.mlab.com:57278/thaonguyentestdb";
	
	private MongoClientURI  _uri;
	private MongoClient     _client;
	private MongoDatabase   _db;
	private MongoCollection<Document> _collection;
	
	public DBConnector(String CollectionName) {
		super();
		
		_uri = new MongoClientURI(DEFAULT_STANDARD_MONGODB_URI);
		_client = new MongoClient(_uri);
		_db = _client.getDatabase(_uri.getDatabase());
		_collection = _db.getCollection(CollectionName);
	}
	
	public DBConnector(String URI, String CollectionName) {
		super();
		
		_uri = new MongoClientURI(URI);
		_client = new MongoClient(_uri);
		_db = _client.getDatabase(_uri.getDatabase());
		_collection = _db.getCollection(CollectionName);
	}

	public static String getDefaultStandardMongodbUri() {
		return DEFAULT_STANDARD_MONGODB_URI;
	}

	public MongoClientURI getURI() {
		return _uri;
	}

	public MongoClient getClient() {
		return _client;
	}

	public MongoDatabase getDatabase() {
		return _db;
	}

	public MongoCollection<Document> getCollection() {
		return _collection;
	}
	
	public void close() {
		/* Close client */
		_client.close();
	}
	
	public int getCount() {
		return (int) _collection.count();
	}
	
	public boolean changeCollection(String CollectionName) {
		boolean result = false;
		
		/* Check if collection is exists */
		Iterator<String> collectionNames = _db.listCollectionNames().iterator();
		while (collectionNames.hasNext()) {
			if (CollectionName.equals(collectionNames.next())) {
				result = true;
			}
		}
		
		/* Change collection */
		if (result) {
			_collection = _db.getCollection(CollectionName);
		}
		
		return result;
	}
	
	public boolean checkURLExists(String NewsURL) {
		boolean result = false;
		
		/* Query to database */
		FindIterable<Document> doc = _collection.find(eq("URL", NewsURL));
		if (doc.first() != null) {
			result = true;
		}
		
		return result;
	}
	
	public ObjectId addNews(News News) {
		ObjectId newsID = null;
		
		/* Check if News is exists in database */
		if (!checkURLExists(News.getURL())) {
			Document doc = News.toDocument();
			_collection.insertOne(doc);
			newsID = (ObjectId)doc.get( "_id" );
		}
		
		return newsID;
	}
	
	public ArrayList<ObjectId> addNewsList(NewsList NewsList) {
		ArrayList<ObjectId> listID = new ArrayList<ObjectId>();
		ArrayList<News> listNews = NewsList.toArrayList();
		
		Iterator<News> iterator = listNews.iterator();
		while (iterator.hasNext()) {
			listID.add(this.addNews(iterator.next()));
		}
		
		return listID;
	}
	
	public NewsList getNews(int Start, int MaxCount) {
		NewsList result = new NewsList();
		Document tempDoc;
		News tempNews;
		FindIterable<Document> obj = _collection.find().skip(Start).limit(MaxCount);
		
		Iterator<Document> i = obj.iterator();
		while (i.hasNext()) {
			tempDoc = i.next();
			tempNews = new News(tempDoc.getString("Title"), tempDoc.getString("URL"), tempDoc.getString("HeaderText"), tempDoc.getString("ImageURL"), (Date) tempDoc.get("Modified"));
			tempNews.setID(((ObjectId)tempDoc.get("_id")).toHexString());
			result.addNews(tempNews);
		}
		
		return result;
	}
}
