package com.thaonguyen.mongodbconnector;

import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

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
	private static final int NUM_CHAR_IN_DESC = 35;
	private static final int NUM_WORD_IN_TITLE = 25;
	private static final String TIMEZONE = "VST"; /* Asia/Ho_Chi_Minh */
	private static final String DATEFORMAT = "yyyy/MM/dd HH:mm:ss";
	private static final String NEWS_COLLECTION_NAME = "News";
	
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
	 * @param Source Source of news.
	 * @param Time Time that release the new.
	 * @return It add successfully.
	 * @throws Exception On connection to database is not established.
	 * @see Exception 
	 */
	public boolean AddNews(String URL, String Title, String ImageURL, String Topic, String Description, String Source)  throws Exception {
		if (_news != null) { /* Check if database is accessed */
			if (this.CheckAvailable(URL)) { /* If URL is available */
				/* Create new news */
				Document doc = new Document();
				
				doc.put("URL", URL);
				doc.put("TITLE", TitleTrim(Title));
				doc.put("IMAGEURL", ImageURL);
				doc.put("TOPIC", Topic);
				doc.put("DESC", Description.substring(0, ((Description.length() < NUM_CHAR_IN_DESC)?Description.length():NUM_CHAR_IN_DESC)) + ((Description.length() > NUM_CHAR_IN_DESC)?"...":""));
				doc.put("SOURCE", Source);
				doc.put("COMMENTCOUNT", 0);
				doc.put("TIME", this.getCurrentDateTime());
				
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
	
	private String TitleTrim(String Title) {
		String result = "";
		
		/* Trim the title if it has more than limit word number */
		String arr[] = Title.split(" ");
		if (arr.length < NUM_WORD_IN_TITLE) {
			result = Title;
		}
		else {
			for (int i = 0; i < NUM_WORD_IN_TITLE - 1; i++) {
				result += arr[i] + " ";
			}
			result += arr[NUM_WORD_IN_TITLE - 1] + "...";
		}
		
		return result;
	}
	
	public String getCurrentDateTime() {
		DateFormat dateFormat = new SimpleDateFormat(DATEFORMAT);
		dateFormat.setTimeZone(TimeZone.getTimeZone(TIMEZONE));
		Date date = new Date();
		return dateFormat.format(date);
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
