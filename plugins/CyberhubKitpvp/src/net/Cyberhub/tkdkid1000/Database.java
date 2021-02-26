package net.Cyberhub.tkdkid1000;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Database {

	public void set(String username, String password, String db, String coll, String title, Map<String, Object> values) {
		MongoClient client = new MongoClient();
		MongoCredential credential; 
	      credential = MongoCredential.createCredential(username, db, 
	         password.toCharArray()); 
	    System.out.println("Credentials: " + credential);
	    System.out.println("Connected to the database successfully");  
	    MongoDatabase database = client.getDatabase(db);
	    MongoCollection<Document> collection = database.getCollection(coll);
	    Document doc = new Document("title", title);
	    for (Map.Entry<String, Object> value : values.entrySet()) {
	    	doc.append(value.getKey(), value.getValue());
	    }
	    collection.insertOne(doc);
	    client.close();
	}
	
	public List<Document> get(String username, String password, String db, String coll) {
		MongoClient client = new MongoClient();
		MongoCredential credential; 
	      credential = MongoCredential.createCredential(username, password, 
	         db.toCharArray()); 
	    System.out.println("Credentials: " + credential);
	    System.out.println("Connected to the database successfully");  
	    MongoDatabase database = client.getDatabase(db);
	    List<Document> list = new ArrayList<Document>();
	    MongoCollection<Document> collection = database.getCollection(coll);
	    FindIterable<Document> iterDoc = collection.find();
		Iterator<Document> it = iterDoc.iterator();
		while (it.hasNext()) {
			list.add(it.next());
		}
		client.close();
		return list;
	}
}
