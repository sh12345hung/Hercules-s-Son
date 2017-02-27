package thaonguyen.MongoDBConnector;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import static com.mongodb.client.model.Filters.*;

import java.util.ArrayList;
import java.util.List;

public class Connector {

	public static void main(String[] args) {
		try {
			MongoClientURI uri = new MongoClientURI("mongodb://admin:admin@ds157278.mlab.com:57278/thaonguyentestdb");
			MongoClient client = new MongoClient(uri);
			MongoDatabase database = client.getDatabase(uri.getDatabase());
			MongoCollection<Document> collection = database.getCollection("test");
			
//			List<Document> doc = new ArrayList<Document>();
//			
//			for (int i = 0; i < 100; i++) {
//				doc.add(new Document("i", i));
//			}
//			
//			collection.insertMany(doc);
			
//			System.out.println(collection.count());
			
//			Document doc = collection.find().first();
//			System.out.println(doc.toJson());
			
//			MongoCursor<Document> cursor = collection.find().iterator();
//			try {
//				while (cursor.hasNext()) {
//					System.out.println(cursor.next().toJson());
//				}
//			} finally {
//				cursor.close();
//			}
//
//			System.out.println("OK");
//			client.close();
			
//			for (Document doc : collection.find()) {
//				System.out.println(doc.toJson());
//			}
			
//			for (Document doc : collection.find(lte("i", 69))) {
//				System.out.println(doc.toJson());
//			}
			
//			Block<Document> printBlock = new Block<Document>() {
//			     @Override
//			     public void apply(final Document document) {
//			         System.out.println(document.toJson());
//			     }
//			};
//			
//			collection.find(gt("i", 50)).forEach(printBlock);
			
//			collection.updateOne(eq("i", 50), new Document("$set", new Document("i", 10)));
			
//			UpdateResult updateResult = collection.updateMany(lte("i", 10), new Document("$inc", new Document("i", 100)));
//			System.out.println(updateResult.getModifiedCount());
			
//			collection.deleteOne(eq("i", 69));
			
			DeleteResult deleteResult = collection.deleteMany(gt("i", 50));
			System.out.println(deleteResult.getDeletedCount());
			
			System.out.println("OK");
		}
		catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

}
