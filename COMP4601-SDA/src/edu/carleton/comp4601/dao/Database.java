package edu.carleton.comp4601.dao;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


public class Database {
	private MongoClient client;
	private MongoDatabase database;
	private MongoCollection<org.bson.Document> collection;
	
	public Database() throws UnknownHostException {
		System.out.println("DATABASE CONSTRUCT");
		client = new MongoClient("127.0.0.1", 27017);
		database = client.getDatabase("sda");
		collection = database.getCollection("documents");
	}
	
	
	public void insertDocument(Integer id, String text, String name, ArrayList<String> tags, ArrayList<String> links) {
		
		System.out.println("ADDING ACCOUNT TO DB" + id);
		Document document = new Document();
		document.put("_id", id);
		document.put("name", text);
		document.put("text", name);
		document.put("tags", tags);
		document.put("links", links);
		
		collection.insertOne(document);
	}
	
	@SuppressWarnings("unchecked")
	public edu.carleton.comp4601.dao.Document getDocument(Integer id) {
		Document doc = collection.find(new Document("_id", id)).first();
		edu.carleton.comp4601.dao.Document a = new edu.carleton.comp4601.dao.Document();
		a.setId(doc.getInteger("_id"));
		a.setName(doc.getString("name"));
		a.setText(doc.getString("text"));
		a.setTags((ArrayList<String>) doc.get("tags"));
		a.setLinks((ArrayList<String>) doc.get("links"));
		
		return a;
	}
	
	public void update(Integer id, ArrayList<String> tags, ArrayList<String> links ) {
		System.out.println("UPDATING ACCOUNT IN DB" + id);
		
		Document doc = new Document("tags",tags)
				.append("links", links);
		collection.updateOne(new Document("_id", id), new Document("$set", doc));
	}
	

	public ConcurrentHashMap<Integer, edu.carleton.comp4601.dao.Document> getDocuments() {
		ConcurrentHashMap<Integer, edu.carleton.comp4601.dao.Document> documents = new ConcurrentHashMap<Integer, edu.carleton.comp4601.dao.Document>();
		
		Block<Document> map = new Block<Document>() {
			@SuppressWarnings("unchecked")
			@Override
			public void apply(final Document doc) {
				edu.carleton.comp4601.dao.Document a = new edu.carleton.comp4601.dao.Document();
				
				a.setId(doc.getInteger("_id"));
				a.setName(doc.getString("name"));
				a.setText(doc.getString("text"));
				a.setTags((ArrayList<String>) doc.get("tags"));
				a.setLinks((ArrayList<String>) doc.get("links"));
				
				documents.put(doc.getInteger("_id"), a);
			}
		};
		
		collection.find().forEach(map);
		
		return documents;
		
	}
	
	
	public void remove(Integer id) {
		System.out.println("REMOVING ACCOUNT FROM DB " + id);

		collection.deleteOne(new Document("_id", id));
	}

	public void remove(ArrayList<String> tags) {
		
		Document in = new Document("$in", tags);
		Document query = new Document("tags", in);
		
		collection.deleteMany(query);
		
		
	}

	
	
	

}