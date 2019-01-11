package cn.xueyuetang.questionspider.mango;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

@Component
public class MangoUtils {
	@Autowired
	private MongoDatabase mongoDatabase;

	public MongoCollection<Document> mongoCollection(String collectionName) {
		MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(collectionName);
		return mongoCollection;
	}

	public FindIterable<Document> findIterable(MongoCollection<Document> mongoCollection, Bson bsonFilter) {
		return mongoCollection.find(bsonFilter);
	}

}
