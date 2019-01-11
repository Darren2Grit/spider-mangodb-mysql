package cn.xueyuetang.questionspider.service;

import java.util.List;
import java.util.Map;

import cn.xueyuetang.questionspider.bean.QuestionEntity;
import cn.xueyuetang.questionspider.bean.QuestionReadingItem;
import cn.xueyuetang.questionspider.bean.QuestionWritingItem;
import cn.xueyuetang.questionspider.entity.TmKnowledge;

public interface SpokingService {
	public List<QuestionWritingItem> getArticleList(Integer type);
	
	public List<QuestionEntity> getQuestionEntityArticleList(String collectionName,Integer type);

	public boolean parseTofel(String courseId, String dbId, String orgId,
			QuestionWritingItem question, Map<String, TmKnowledge> knowledgeMap);

	public boolean parseIetls(String courseId, String dbId, String orgId,
			QuestionWritingItem question, Map<String, TmKnowledge> knowledgeMap);
	
	public boolean parseNoResQUestion(String courseId, String dbId, String orgId,
			QuestionEntity question, Map<String, TmKnowledge> knowledgeMap,Long index);
	
	

	public Map<String, TmKnowledge> getKnowledgeMap(String courseId);
}
