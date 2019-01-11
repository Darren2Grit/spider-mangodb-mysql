package cn.xueyuetang.questionspider.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import cn.xueyuetang.questionspider.App;
import cn.xueyuetang.questionspider.bean.QuestionEntity;
import cn.xueyuetang.questionspider.bean.QuestionReadingItem;
import cn.xueyuetang.questionspider.constant.Constant;
import cn.xueyuetang.questionspider.constant.MangoCollection;
import cn.xueyuetang.questionspider.dao.TmCourseResDao;
import cn.xueyuetang.questionspider.dao.TmKnowledgeDao;
import cn.xueyuetang.questionspider.dao.TmQuestionDao;
import cn.xueyuetang.questionspider.entity.TmCourseRes;
import cn.xueyuetang.questionspider.entity.TmKnowledge;
import cn.xueyuetang.questionspider.entity.TmQuestion;
import cn.xueyuetang.questionspider.mango.MangoUtils;
import cn.xueyuetang.questionspider.service.ReadingService;
import cn.xueyuetang.questionspider.utils.CodeGEnum;
import cn.xueyuetang.questionspider.utils.CodeGenerator;
import cn.xueyuetang.questionspider.utils.GsonUtil;
import cn.xueyuetang.questionspider.utils.QuestionUtil;

@Service
public class ReadingServiceImpl implements ReadingService {
	private static final Logger logger = LogManager.getLogger(ReadingServiceImpl.class);
	@Autowired
	private MangoUtils mangoUtils;

	@Autowired
	private TmKnowledgeDao knowledgeDao;

	@Autowired
	private TmCourseResDao courseResDao;

	@Autowired
	private TmQuestionDao questionDao;

	public Map<String, List<QuestionReadingItem>> getArticleList(Integer type) {

		Map<String, List<QuestionReadingItem>> questionListOfArticleMap = new HashMap<String, List<QuestionReadingItem>>();
		MongoCollection<Document> collection = mangoUtils.mongoCollection(MangoCollection.Reading.getName());
		Document sub_match = new Document();
		sub_match.put("question_module_type", type);
		Document sub_group = new Document();
		sub_group.put("_id", "$question_article_id");
		sub_group.put("question", new Document("$push", "$$ROOT"));
		Document match = new Document("$match", sub_match);
		Document group = new Document("$group", sub_group);
		List<Document> aggregateList = new ArrayList<Document>();
		aggregateList.add(match);
		aggregateList.add(group);
		AggregateIterable<Document> resultset = collection.aggregate(aggregateList);
		MongoCursor<Document> cursor = resultset.iterator();
		while (cursor.hasNext()) {
			List<QuestionReadingItem> questionListData = new ArrayList<QuestionReadingItem>();
			Document item_doc = cursor.next();
			List<Map<String, Object>> questionList = (List<Map<String, Object>>) item_doc.get("question");
			for (Map<String, Object> question : questionList) {
				String questionStr = GsonUtil.GsonString(question);
				QuestionReadingItem questionItem = GsonUtil.GsonToBean(questionStr, QuestionReadingItem.class);
				questionItem.setId(UUID.randomUUID().toString());
				questionListData.add(questionItem);
			}
			if (questionListData.size() > 0) {
				Collections.sort(questionListData);
				questionListOfArticleMap.put(item_doc.get("_id", UUID.class).toString(), questionListData);
			}
		}
		return questionListOfArticleMap;
	}

	@Transactional
	public boolean parseTofelArticle(String courseId, String dbId, String orgId,
			List<QuestionReadingItem> articleQuestionList, Map<String, TmKnowledge> knowledgeMap) {
		String knowledgeNameTemp = articleQuestionList.get(0).getQuestionKnowledgeName().trim();
		String knowledgeName = Constant.SUBJECTMAP.get(knowledgeNameTemp) + "/" + knowledgeNameTemp;
		String knowledgeId = insertKnowledge(knowledgeName, courseId, knowledgeMap);
		Map<Integer, String> paragraphMap = insertRes(articleQuestionList, courseId, knowledgeId, knowledgeName);
		Long index = 0L;
		for (QuestionReadingItem questionReadingItem : articleQuestionList) {
			TmQuestion question = QuestionUtil.buildQuestion(questionReadingItem, index);
			question.setQDbid(dbId);

			if ("6".equals(questionReadingItem.getQuestionType())) {
				TmCourseRes courseRes = new TmCourseRes();
				String resId = UUID.randomUUID().toString();
				courseRes.setResId(resId);
				courseRes.setResName(questionReadingItem.getName() + "-" + questionReadingItem.getQuestionArticleTitle()
						+ "#" + questionReadingItem.getQuestionBelongParagraph());
				courseRes.setCourseId(courseId);
				courseRes.setResStatus(Integer.valueOf(0));
				courseRes.setbCreatedate(LocalDateTime.now());
				courseRes.setbModifydate(LocalDateTime.now());
				courseRes.setFileType(Integer.valueOf(7));
				courseRes.setKnowage(knowledgeId);
				courseRes.setKnowagename(knowledgeName);
				courseRes.setWikicontent(questionReadingItem.getQuestionInsertContent());
				if (courseResDao.insert(courseRes) > 0) {
					question.setResId(resId);
				}
			} else {
				if (paragraphMap != null) {
					String resId = paragraphMap.get(Integer.parseInt(questionReadingItem.getQuestionBelongParagraph()));
					if (StringUtils.isEmpty(resId)) {
						resId = paragraphMap.get(questionReadingItem.getQuestionArticleContent().size());
					}
					question.setResId(resId);
				}
			}
			question.setTag(questionReadingItem.getName().trim().replace("阅读", ""));
			question.setQFrom(questionReadingItem.getName().trim().replace("阅读", ""));
			question.setOrgId(orgId);
			question.setQStatus(Integer.valueOf(1));
			question.setQKnowages(knowledgeId);
			if (StringUtils.isNotEmpty(questionReadingItem.getQuestionOrder())) {
				question.setQOrder(Integer.parseInt(questionReadingItem.getQuestionOrder()));
			} else {
				question.setQOrder(Integer.valueOf(999));
			}
			if (questionDao.insert(question) > 0) {

			}
			index++;
		}

		return true;

	}

	private Map<Integer, String> insertRes(List<QuestionReadingItem> articleQuestionList, String courseId,
			String knowledgeId, String knowledgeName) {
		List<String> articlePageList = articleQuestionList.get(0).getQuestionArticleContent();
		String ariticleName = articleQuestionList.get(0).getQuestionArticleTitle();
		String name = articleQuestionList.get(0).getName();
		Map<Integer, String> paragraphMap = new HashMap<Integer, String>();
		for (int i = 0; i < articlePageList.size(); i++) {
			String resId = UUID.randomUUID().toString();
			TmCourseRes courseRes = new TmCourseRes();
			courseRes.setResId(resId);
			courseRes.setResName(name + "-" + ariticleName + "@" + (i + 1));
			courseRes.setCourseId(courseId);
			courseRes.setResStatus(Integer.valueOf(0));
			courseRes.setbCreatedate(LocalDateTime.now());
			courseRes.setbModifydate(LocalDateTime.now());
			courseRes.setFileType(Integer.valueOf(7));
			courseRes.setKnowage(knowledgeId);
			courseRes.setKnowagename(knowledgeName);
			courseRes.setWikicontent(articlePageList.get(i).trim());
			if (courseResDao.insert(courseRes) > 0) {
				paragraphMap.put((i + 1), resId);
			}
		}
		/*
		 * if(courseResService.saveOrUpdateBatch(insertData)){ return paragraphMap; }
		 */
		return paragraphMap;

	}

	private String insertKnowledge(String knowledgeName, String courseId, Map<String, TmKnowledge> knowledgeMap) {
		if (!knowledgeMap.isEmpty()) {
			if (knowledgeMap.containsKey(knowledgeName)) {
				return knowledgeMap.get(knowledgeName).getResId();
			}
		}
		String id = UUID.randomUUID().toString();
		TmKnowledge knowledge = new TmKnowledge();
		knowledge.setResId(id);
		knowledge.setCode(CodeGenerator.getNextCode(CodeGEnum.KN));
		knowledge.setCourseId(courseId);
		knowledge.setName(knowledgeName);
		knowledge.setStatus(Integer.valueOf(2));
		knowledge.setDegree(Integer.valueOf(0));
		knowledge.setbCreatedate(LocalDateTime.now());
		knowledge.setbModifydate(LocalDateTime.now());
		if (knowledgeDao.insert(knowledge) > 0) {
			knowledgeMap.put(knowledgeName, knowledge);
			return id;
		}
		return null;
	}

	@Transactional
	public boolean parseIetlsArticle(String courseId, String dbId, String orgId,
			List<QuestionReadingItem> articleQuestionList, Map<String, TmKnowledge> knowledgeMap) {
		String knowledgeNameTemp = articleQuestionList.get(0).getQuestionKnowledgeName().trim();
		if (knowledgeNameTemp.contains("-")) {
			knowledgeNameTemp = knowledgeNameTemp.substring(knowledgeNameTemp.indexOf("-") + 1);
		}
		String knowledgeName = knowledgeNameTemp;
		if (StringUtils.isNotEmpty(Constant.SUBJECTMAP.get(knowledgeNameTemp))) {
			knowledgeName = Constant.SUBJECTMAP.get(knowledgeNameTemp) + "/" + knowledgeName;
		}
		String knowledgeId = insertKnowledge(knowledgeName, courseId, knowledgeMap);
		String resId = insertIetlsRes(articleQuestionList, courseId, knowledgeId, knowledgeName);
		Long index = 0L;
		for (QuestionReadingItem questionReadingItem : articleQuestionList) {
			TmQuestion question = QuestionUtil.buildQuestion(questionReadingItem, index);
			question.setQDbid(dbId);
			question.setResId(resId);
			question.setTag(questionReadingItem.getName().trim().replace("阅读", ""));
			question.setQFrom(questionReadingItem.getName().trim().replace("阅读", ""));
			question.setOrgId(orgId);
			question.setQStatus(Integer.valueOf(1));
			if (StringUtils.isNotEmpty(questionReadingItem.getQuestionOrder())) {
				question.setQOrder(Integer.parseInt(questionReadingItem.getQuestionOrder()));
			} else {
				question.setQOrder(Integer.valueOf(999));
			}
			if (questionDao.insert(question) > 0) {

			}
			index++;
		}

		return true;

	}

	private String insertIetlsRes(List<QuestionReadingItem> articleQuestionList, String courseId, String knowledgeId,
			String knowledgeName) {
		List<String> articlePageList = articleQuestionList.get(0).getQuestionArticleContent();
		String name = articleQuestionList.get(0).getName().trim();
		String ariticleName = articleQuestionList.get(0).getQuestionArticleTitle();
		String resId = UUID.randomUUID().toString();
		StringBuilder articleContent = new StringBuilder();
		for (int i = 0; i < articlePageList.size(); i++) {
			articleContent.append(articlePageList.get(i).trim());

		}
		TmCourseRes courseRes = new TmCourseRes();
		courseRes.setResId(resId);
		courseRes.setResName(name + "-" + ariticleName);
		courseRes.setCourseId(courseId);
		courseRes.setResStatus(Integer.valueOf(0));
		courseRes.setbCreatedate(LocalDateTime.now());
		courseRes.setbModifydate(LocalDateTime.now());
		courseRes.setFileType(Integer.valueOf(7));
		courseRes.setKnowage(knowledgeId);
		courseRes.setKnowagename(knowledgeName);
		courseRes.setWikicontent(articleContent.toString());
		if (courseResDao.insert(courseRes) > 0) {
			return resId;
		}
		return null;

	}

	private String insertSatRes(List<QuestionEntity> articleQuestionList, String courseId, String knowledgeId,
			String knowledgeName) {
		String articleContent = articleQuestionList.get(0).getArticleContent();
		String ariticleName = articleQuestionList.get(0).getQuestionTagName();
		String resId = UUID.randomUUID().toString();
		TmCourseRes courseRes = new TmCourseRes();
		courseRes.setResId(resId);
		courseRes.setResName(ariticleName);
		courseRes.setCourseId(courseId);
		courseRes.setResStatus(Integer.valueOf(0));
		courseRes.setbCreatedate(LocalDateTime.now());
		courseRes.setbModifydate(LocalDateTime.now());
		courseRes.setFileType(Integer.valueOf(7));
		courseRes.setKnowage(knowledgeId);
		courseRes.setKnowagename(knowledgeName);
		courseRes.setWikicontent(articleContent);
		if (courseResDao.insert(courseRes) > 0) {
			return resId;
		}
		return null;

	}
	
	
	private String insertGmatRes(List<QuestionEntity> articleQuestionList, String courseId, String knowledgeId,
			String knowledgeName) {
		String articleContent = articleQuestionList.get(0).getArticleContent();
		String ariticleName = articleQuestionList.get(0).getQuestionTagName()+"-1";
		String resId = UUID.randomUUID().toString();
		TmCourseRes courseRes = new TmCourseRes();
		courseRes.setResId(resId);
		courseRes.setResName(ariticleName);
		courseRes.setCourseId(courseId);
		courseRes.setResStatus(Integer.valueOf(0));
		courseRes.setbCreatedate(LocalDateTime.now());
		courseRes.setbModifydate(LocalDateTime.now());
		courseRes.setFileType(Integer.valueOf(7));
		courseRes.setKnowage(knowledgeId);
		courseRes.setKnowagename(knowledgeName);
		courseRes.setWikicontent(articleContent);
		if (courseResDao.insert(courseRes) > 0) {
			return resId;
		}
		return null;

	}

	private String insertGmatRes(QuestionEntity articleQuestionList, String courseId, String knowledgeId,
			String knowledgeName) {
		String articleContent = articleQuestionList.getArticleContent();
		String ariticleName = articleQuestionList.getQuestionTagName()+"-1";
		String resId = UUID.randomUUID().toString();
		TmCourseRes courseRes = new TmCourseRes();
		courseRes.setResId(resId);
		courseRes.setResName(ariticleName);
		courseRes.setCourseId(courseId);
		courseRes.setResStatus(Integer.valueOf(0));
		courseRes.setbCreatedate(LocalDateTime.now());
		courseRes.setbModifydate(LocalDateTime.now());
		courseRes.setFileType(Integer.valueOf(7));
		courseRes.setKnowage(knowledgeId);
		courseRes.setKnowagename(knowledgeName);
		courseRes.setWikicontent(articleContent);
		if (courseResDao.insert(courseRes) > 0) {
			return resId;
		}
		return null;

	}

	public Map<String, TmKnowledge> getKnowledgeMap(String courseId) {
		Map<String, TmKnowledge> result = new HashMap<String, TmKnowledge>();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("course_id", courseId);
		List<TmKnowledge> listData = knowledgeDao.selectByMap(param);
		if (listData != null && listData.size() > 0) {
			for (TmKnowledge knowledge : listData) {
				result.put(knowledge.getName().trim(), knowledge);
			}
		}
		return result;
	}

	public boolean parseSatArticle(String courseId, String dbId, String orgId, List<QuestionEntity> articleQuestionList,
			Map<String, TmKnowledge> knowledgeMap) {
		String knowledgeNameTemp = articleQuestionList.get(0).getQuestionKnowledge();
		if (knowledgeNameTemp.contains("-")) {
			knowledgeNameTemp = knowledgeNameTemp.substring(knowledgeNameTemp.indexOf("-") + 1);
		}
		String knowledgeName = knowledgeNameTemp;
		if (StringUtils.isNotEmpty(Constant.SUBJECTMAP.get(knowledgeNameTemp))) {
			knowledgeName = Constant.SUBJECTMAP.get(knowledgeNameTemp) + "/" + knowledgeName;
		}
		String knowledgeId = insertKnowledge(knowledgeName, courseId, knowledgeMap);
		String resId = insertGmatRes(articleQuestionList, courseId, knowledgeId, knowledgeName);
		Long index = 0L;
		for (QuestionEntity questionReadingItem : articleQuestionList) {
			TmQuestion question = QuestionUtil.buildQUestion(questionReadingItem, index);
			question.setQDbid(dbId);
			question.setResId(resId);
			question.setQFrom(questionReadingItem.getQuestionTagName());
			question.setTag(questionReadingItem.getQuestionTagName());
			question.setOrgId(orgId);
			question.setQStatus(Integer.valueOf(1));
			if (StringUtils.isNotEmpty(questionReadingItem.getQuestionOrder())) {
				question.setQOrder(Integer.parseInt(questionReadingItem.getQuestionOrder()));
			} else {
				question.setQOrder(Integer.valueOf(999));
			}
			if (questionDao.insert(question) > 0) {

			}
			index++;
		}

		return false;
	}

	public Map<String, List<QuestionEntity>> getQuestionEntityArticleList(String collectionType, Integer type) {
		Map<String, List<QuestionEntity>> questionListOfArticleMap = new HashMap<String, List<QuestionEntity>>();
		MongoCollection<Document> collection = mangoUtils.mongoCollection(collectionType);
		Document sub_match = new Document();
		sub_match.put("module_type", type);
		Document sub_group = new Document();
		sub_group.put("_id", "$article_id");
		sub_group.put("question", new Document("$push", "$$ROOT"));
		Document match = new Document("$match", sub_match);
		Document group = new Document("$group", sub_group);
		List<Document> aggregateList = new ArrayList<Document>();
		aggregateList.add(match);
		aggregateList.add(group);
		AggregateIterable<Document> resultset = collection.aggregate(aggregateList);
		MongoCursor<Document> cursor = resultset.iterator();
		while (cursor.hasNext()) {
			List<QuestionEntity> questionListData = new ArrayList<QuestionEntity>();
			Document item_doc = cursor.next();
			List<Map<String, Object>> questionList = (List<Map<String, Object>>) item_doc.get("question");
			for (Map<String, Object> question : questionList) {
				String questionStr = GsonUtil.GsonString(question);
				QuestionEntity questionItem = GsonUtil.GsonToBean(questionStr, QuestionEntity.class);
				questionItem.setId(UUID.randomUUID().toString());
				questionListData.add(questionItem);
			}
			if (questionListData.size() > 0) {
				Collections.sort(questionListData);
				questionListOfArticleMap.put(item_doc.get("_id", UUID.class).toString(), questionListData);
			}
		}
		return questionListOfArticleMap;
	}

	@Transactional
	public boolean parseGmatArticle(String courseId, String dbId, String orgId, QuestionEntity articleQuestionList,
			Map<String, TmKnowledge> knowledgeMap) {
		String knowledgeNameTemp = articleQuestionList.getQuestionKnowledge();
		if (knowledgeNameTemp.contains("-")) {
			knowledgeNameTemp = knowledgeNameTemp.substring(knowledgeNameTemp.indexOf("-") + 1);
		}
		String knowledgeName = knowledgeNameTemp;
		if (StringUtils.isNotEmpty(Constant.SUBJECTMAP.get(knowledgeNameTemp))) {
			knowledgeName = Constant.SUBJECTMAP.get(knowledgeNameTemp) + "/" + knowledgeName;
		}
		String knowledgeId = insertKnowledge(knowledgeName, courseId, knowledgeMap);
		String resId = insertGmatRes(articleQuestionList, courseId, knowledgeId, knowledgeName);
		List<Object> questionList = articleQuestionList.getQuestionList();
		Long index = 0L;
		for (Object questionObj : questionList) {
			QuestionEntity questionTemp = GsonUtil.GsonToBean(GsonUtil.GsonString(questionObj), QuestionEntity.class);
			String qid=UUID.randomUUID().toString();
			String questionType = questionTemp.getQuestionType();
			questionType = questionType.substring(0, questionType.lastIndexOf("."));
			questionTemp.setQuestionType(questionType);
			questionTemp.setId(qid);
			TmQuestion question = QuestionUtil.buildQUestion(questionTemp, index);
			question.setQId(qid);
			question.setQDbid(dbId);
			question.setResId(resId);
			question.setQFrom(questionTemp.getQuestionTagName());
			question.setTag(questionTemp.getQuestionTagName());
			question.setOrgId(orgId);
			question.setQStatus(Integer.valueOf(1));
			if (StringUtils.isNotEmpty(questionTemp.getQuestionOrder())) {
				String questionTempOrder = questionTemp.getQuestionOrder();
				questionTempOrder = questionTempOrder.substring(0, questionTempOrder.lastIndexOf("."));
				question.setQOrder(Integer.parseInt(questionTempOrder));
			} else {
				question.setQOrder(Integer.valueOf(999));
			}
			if (questionDao.insert(question) > 0) {

			}
			index++;

		}

		return false;
	}

}
