package com.thaonguyen.mongodbconnector;

import org.bson.Document;
import java.util.List;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;

/**
 * <h1>MongoDBConnectorForCrawler</h1>
 * The library to connect to MongoDB database
 * for NewsCrawler
 * 
 * 
 * @author ThaoNguyenManh
 * @version 1.0
 * @since 2017-03-26
 */

public class MongoDBConnectorForCrawler {
	/* ----- Constants ----- */
	private static final String NEWS_COLLECTION_NAME = "News";
	private static final int NUM_OF_CHAR_IN_SHORTDESC = 30;
	
	/* ----- Variables ----- */
	private MongoClientURI  _uri = null;
	private MongoClient     _client = null;
	private MongoDatabase   _db = null;
	private MongoCollection<Document> _news = null;
	
	/* ----- Constructors ----- */
	/**
	 * This is class constructor.
	 * 
	 * @param URI MongoDB standard URI to create connection to database.
	 */
	public MongoDBConnectorForCrawler(String URI) {
		super();
		
		try {
			_uri = new MongoClientURI(URI);
			_client = new MongoClient(_uri);
			_db = _client.getDatabase(_uri.getDatabase());
			_news = _db.getCollection(NEWS_COLLECTION_NAME);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * This is class constructor.
	 * 
	 * @param Host MongoDB host's name.
	 * @param Port MongoDB port.
	 * @param DatabaseName name of database to connect.
	 */
	public MongoDBConnectorForCrawler(String Host, int Port, String DatabaseName) {
		super();
		
		try {
			_client = new MongoClient(Host, Port);
			_db = _client.getDatabase(DatabaseName);
			_news = _db.getCollection(NEWS_COLLECTION_NAME);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/* ----- Methods ----- */
	/**
	 * This function is used for checking
	 * whether the article is existed in database.
	 * 
	 * @param URL URL of the article to be check.
	 * @return true if the article is not in the database, false in the other hand.
	 * @throws Exception On connection to database is not established.
	 * @see Exception 
	 */
	public boolean CheckAvailable(String URL) throws Exception {
		if (_news != null) { /* Check if database is accessed */
			/* Query to database */
			FindIterable<Document> doc = _news.find(eq("URL", URL));
			if (doc.first() != null) {
				return false; /* Not available */
			}
			else {
				return true; /* Available */
			}
		}
		else {
			throw (new Exception("_news is not initialized."));
		}
	}
	
	/**
	 * Add a new article to database (it's not need to check URL first)
	 * 
	 * @param URL URL of the article.
	 * @param Title Title of the article.
	 * @param ImageURL ImageURL of the article.
	 * @param Topic Topic of the article.
	 * @param Description Description of the article.
	 * @return It add successfully.
	 * @throws Exception On connection to database is not established.
	 * @see Exception 
	 */
	public boolean AddNews(String URL, String Title, String ImageURL, String Topic, String Description)  throws Exception {
		if (_news != null) { /* Check if database is accessed */
			if (this.CheckAvailable(URL)) { /* If URL is available */
				/* Create new news */
				Document doc = new Document();
				
				doc.put("URL", URL);
				doc.put("TITLE", Title);
				doc.put("IMAGEURL", ImageURL);
				doc.put("TOPIC", Topic);
				int len = Description.length();
				doc.put("SHORTDESC", Description.substring(0, len>NUM_OF_CHAR_IN_SHORTDESC?NUM_OF_CHAR_IN_SHORTDESC:len));
				doc.put("FULLDESC", Description);
				
				/* Add to collection */
				_news.insertOne(doc);
				
				/* Success */
				return true; /* News is inserted */
			}
			else { /* URL is existed in database */
				return false; /* News is not inserted */
			}
		}
		else{
			throw (new Exception("_news is not initialized."));
		}
	}
	
	/**
	 * Add a new article to database (it's not need to check URL first)
	 * 
	 * @param URL URL of the article.
	 * @param Title Title of the article.
	 * @param ImageURL ImageURL of the article.
	 * @param Topic List of topic of the article.
	 * @param Description Description of the article.
	 * @return It add successfully.
	 * @throws Exception On connection to database is not established.
	 * @see Exception
	 */
	public boolean AddNews(String URL, String Title, String ImageURL, List<String> Topic, String Description)  throws Exception {
		if (_news != null) { /* Check if database is accessed */
			if (this.CheckAvailable(URL)) { /* If URL is available */
				/* Create new news */
				Document doc = new Document();
				
				doc.put("URL", URL);
				doc.put("TITLE", Title);
				doc.put("IMAGEURL", ImageURL);
				doc.put("TOPIC", Topic);
				int len = Description.length();
				doc.put("SHORTDESC", Description.substring(0, len>NUM_OF_CHAR_IN_SHORTDESC?NUM_OF_CHAR_IN_SHORTDESC:len));
				doc.put("FULLDESC", Description);
				
				/* Add to collection */
				_news.insertOne(doc);
				
				/* Success */
				return true; /* News is inserted */
			}
			else { /* URL is existed in database */
				return false; /* News is not inserted */
			}
		}
		else{
			throw (new Exception("_news is not initialized."));
		}
	}
	
	/**
	 * This is method that close connection
	 * between client and MongoDB. This should be called
	 * at the end of program.
	 */
	public void close() {
		/* Close client */
		_client.close();
	}
}
