package edu.carleton.comp4601.dao;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.annotation.XmlRootElement;

import com.mongodb.BasicDBObject;

import java.util.List;
import java.util.Map;

@XmlRootElement
public class Documents {
	
	private static Documents instance;
	
	private ConcurrentHashMap<Integer, Document> documents;
	private Database db;
	
	Documents() {
		try {
			db = new Database();
			documents = db.getDocuments();
			System.out.println(documents.get(new Integer(1)));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static Documents getInstance() {
		if (instance == null) {
			instance = new Documents();
		}
		
		return instance;
	}
	
	
	public boolean add(Document doc) {
		if (documents.get(doc.getId()) == null) {
			documents.put(doc.getId(), doc);
			db.insertDocument(
					doc.getId(),
					doc.getText(),
					doc.getName(),
					doc.getTags(),
					doc.getLinks());
			
			return true;
		}
		
		return false;
	}
	
	public Document get(Integer id) {
		if (documents.get(id) != null) {
			return documents.get(id);
		}
		
		return db.getDocument(id);
	}
	
	public Collection<Document> getValues(){
		Collection<Document> d = documents.values();
		return d;
	}
	
	
	
	
	public boolean update(Integer id, Document doc) {
		if (documents.get(id) != null) {
			documents.put(id,doc);
			db.update(id,doc.getTags(), doc.getLinks());
			return true;
		}
		return false;
	}
	
	public boolean remove(Integer id) {
		if (get(id) != null) {
			Integer n = id;
			documents.remove(n);
			db.remove(id);
			return true;
		}
		else
			return false;
	}
	//removes all documents with given tags(assumes tags are given tag1,tag2,tag3)
	public boolean remove(String tags) {
		
		boolean check = false;
		System.out.println(tags.split(",").length);
		List<String> t = Arrays.asList(tags.split(","));
		ArrayList<String> al = new ArrayList<>();
		al.addAll(t);
		//iterating through documents instance and removing each document containing
		//specified tags
		for(Map.Entry<Integer, Document> entry : documents.entrySet()) {
			Integer key = entry.getKey();
		    Document value = entry.getValue();
		    for(int i = 0; i < al.size(); i++){
		    	if(value.getTags().contains(al.get(i))){
		    		documents.remove(key);
		    		check = true;
		    	}
		    }
		}
		
		db.remove(al);
		return check;
	}
	
	
	
	
}
