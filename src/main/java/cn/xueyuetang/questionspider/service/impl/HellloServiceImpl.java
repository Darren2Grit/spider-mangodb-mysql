package cn.xueyuetang.questionspider.service.impl;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import cn.xueyuetang.questionspider.dao.HelloDao;
import cn.xueyuetang.questionspider.dao.TmQuestionDao;
import cn.xueyuetang.questionspider.service.HelloService;

@Service
public class HellloServiceImpl implements HelloService {

	@Autowired
	private HelloDao helloDao;
	
	@Autowired
	private TmQuestionDao questionDao;
	
	@Autowired
	private MongoClient mongoClient;
	
	@Autowired
	private MongoDatabase mongoDatabase;

	public void Hello() {
		helloDao.hello();
		System.out.println(questionDao.selectList(null));
		MongoCollection<Document> collection = mongoDatabase.getCollection("writing");
		FindIterable<Document> findIterable = collection.find();  
        MongoCursor<Document> mongoCursor = findIterable.iterator();  
        while(mongoCursor.hasNext()){  
           System.out.println(mongoCursor.next());  
        }  
	}

}
