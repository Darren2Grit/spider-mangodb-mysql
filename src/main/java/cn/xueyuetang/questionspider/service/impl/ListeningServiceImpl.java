package cn.xueyuetang.questionspider.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

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
import cn.xueyuetang.questionspider.service.ListeningService;
import cn.xueyuetang.questionspider.utils.CodeGEnum;
import cn.xueyuetang.questionspider.utils.CodeGenerator;
import cn.xueyuetang.questionspider.utils.GsonUtil;
import cn.xueyuetang.questionspider.utils.QuestionUtil;

@Service
public class ListeningServiceImpl implements ListeningService {
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
		MongoCollection<Document> collection = mangoUtils.mongoCollection(MangoCollection.Listening.getName());
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
		String knowledgeNameTemp = articleQuestionList.get(0).getQuestionKnowledgeName();
		String knowledgeName = knowledgeNameTemp;
		if (StringUtils.isNotEmpty(knowledgeNameTemp)) {
			knowledgeName = knowledgeNameTemp.replace("-", "/").trim();
		}
		String knowledgeId = insertKnowledge(knowledgeName, courseId, knowledgeMap);
		String resId = insertRes(articleQuestionList, courseId, knowledgeId, knowledgeName);
		Long index=0L;
		for (QuestionReadingItem questionReadingItem : articleQuestionList) {
			TmQuestion question = QuestionUtil.buildQuestion(questionReadingItem,index);
			question.setQDbid(dbId);
			question.setResId(resId);
			question.setQFrom(questionReadingItem.getName().trim().replace("听力", ""));
			question.setTag(questionReadingItem.getName().trim().replace("听力", ""));
			question.setOrgId(orgId);
			question.setQStatus(Integer.valueOf(1));
			question.setQKnowages(knowledgeId);
			question.setQResolve(parseParagraph(questionReadingItem.getQuestionResolveContent())
					+ parseParagraph("<p>听力原文</p>" + questionReadingItem.getQuestionArticleContent().get(0)));
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

	private String insertKnowledge(String knowledgeName, String courseId, Map<String, TmKnowledge> knowledgeMap) {
		if (StringUtils.isEmpty(knowledgeName)) {
			return null;
		}
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
		String knowledgeNameTemp = articleQuestionList.get(0).getQuestionKnowledgeName();
		if (StringUtils.isNotEmpty(knowledgeNameTemp)) {
			knowledgeNameTemp = knowledgeNameTemp.trim();
		}
		if (knowledgeNameTemp.contains("-")) {
			knowledgeNameTemp = knowledgeNameTemp.substring(knowledgeNameTemp.indexOf("-") + 1);
		}
		String knowledgeName = knowledgeNameTemp;
		if (StringUtils.isNotEmpty(Constant.SUBJECTMAP.get(knowledgeNameTemp))) {
			knowledgeName = Constant.SUBJECTMAP.get(knowledgeNameTemp) + "/" + knowledgeName;
		}
		String knowledgeId = insertKnowledge(knowledgeName, courseId, knowledgeMap);
		String resId = insertRes(articleQuestionList, courseId, knowledgeId, knowledgeName);
		Long index=0L;
		for (QuestionReadingItem questionReadingItem : articleQuestionList) {
			TmQuestion question = QuestionUtil.buildQuestion(questionReadingItem,index);
			question.setQDbid(dbId);
			question.setResId(resId);
			question.setQFrom(questionReadingItem.getName().trim().replace("听力", ""));
			question.setTag(questionReadingItem.getName().trim().replace("听力", ""));
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

	private String insertRes(List<QuestionReadingItem> articleQuestionList, String courseId, String knowledgeId,
			String knowledgeName) {
		String ariticleName = articleQuestionList.get(0).getQuestionArticleTitle();
		if (StringUtils.isNotEmpty(ariticleName)&&StringUtils.isNoneEmpty(articleQuestionList.get(0).getName())) {
			ariticleName = articleQuestionList.get(0).getName().trim()+" "+ariticleName.trim();
		} else {
			ariticleName = articleQuestionList.get(0).getName().trim()+" "
					+ articleQuestionList.get(0).getQuestionKnowledgeName().trim();
		}
		String resId = UUID.randomUUID().toString();
		TmCourseRes courseRes = new TmCourseRes();
		courseRes.setResId(resId);
		courseRes.setResName(ariticleName);
		courseRes.setCourseId(courseId);
		courseRes.setResStatus(Integer.valueOf(0));
		courseRes.setbCreatedate(LocalDateTime.now());
		courseRes.setbModifydate(LocalDateTime.now());
		courseRes.setFileType(Integer.valueOf(6));
		courseRes.setKnowage(knowledgeId);
		courseRes.setKnowagename(knowledgeName);
		courseRes.setWikicontent(articleQuestionList.get(0).getQuestionArticleContent().get(0));
		String fileUrl = "upload/audio/" + articleQuestionList.get(0).getQuestionAudioUrl();
		courseRes.setFileUrl(fileUrl.replace("\\", "/"));
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

	private static String parseParagraph(String orgStr) {
		if (StringUtils.isNotEmpty(orgStr)) {
			return "<p>" + orgStr.trim() + "</p>";
		}
		return "";
	}

}
