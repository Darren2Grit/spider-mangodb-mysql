package cn.xueyuetang.questionspider.service;

import java.util.List;
import java.util.Map;

import cn.xueyuetang.questionspider.bean.QuestionReadingItem;
import cn.xueyuetang.questionspider.entity.TmKnowledge;

public interface ListeningService {
	public Map<String, List<QuestionReadingItem>> getArticleList(Integer type);

	public boolean parseTofelArticle(String courseId, String dbId, String orgId,
			List<QuestionReadingItem> articleQuestionList, Map<String, TmKnowledge> knowledgeMap);

	public boolean parseIetlsArticle(String courseId, String dbId, String orgId,
			List<QuestionReadingItem> articleQuestionList, Map<String, TmKnowledge> knowledgeMap);

	public Map<String, TmKnowledge> getKnowledgeMap(String courseId);
}
