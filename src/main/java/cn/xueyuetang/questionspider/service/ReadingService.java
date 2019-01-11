package cn.xueyuetang.questionspider.service;

import java.util.List;
import java.util.Map;

import cn.xueyuetang.questionspider.bean.QuestionEntity;
import cn.xueyuetang.questionspider.bean.QuestionReadingItem;
import cn.xueyuetang.questionspider.entity.TmKnowledge;

public interface ReadingService {
	public Map<String, List<QuestionReadingItem>> getArticleList(Integer type);
	
	public Map<String, List<QuestionEntity>> getQuestionEntityArticleList(String collectionType,Integer type);

	public boolean parseTofelArticle(String courseId, String dbId, String orgId,
			List<QuestionReadingItem> articleQuestionList, Map<String, TmKnowledge> knowledgeMap);

	public boolean parseIetlsArticle(String courseId, String dbId, String orgId,
			List<QuestionReadingItem> articleQuestionList, Map<String, TmKnowledge> knowledgeMap);
	
	public boolean parseSatArticle(String courseId, String dbId, String orgId,
			List<QuestionEntity> articleQuestionList, Map<String, TmKnowledge> knowledgeMap);
	
	public boolean parseGmatArticle(String courseId, String dbId, String orgId,
			QuestionEntity question, Map<String, TmKnowledge> knowledgeMap);
	

	public Map<String, TmKnowledge> getKnowledgeMap(String courseId);
}
