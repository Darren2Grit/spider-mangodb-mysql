package cn.xueyuetang.questionspider.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import cn.xueyuetang.questionspider.bean.QuestionEntity;
import cn.xueyuetang.questionspider.bean.QuestionReadingItem;
import cn.xueyuetang.questionspider.bean.QuestionWritingItem;
import cn.xueyuetang.questionspider.constant.Constant;
import cn.xueyuetang.questionspider.constant.MangoCollection;
import cn.xueyuetang.questionspider.dao.TmCourseResDao;
import cn.xueyuetang.questionspider.dao.TmKnowledgeDao;
import cn.xueyuetang.questionspider.dao.TmQuestionDao;
import cn.xueyuetang.questionspider.entity.TmCourseRes;
import cn.xueyuetang.questionspider.entity.TmKnowledge;
import cn.xueyuetang.questionspider.entity.TmQuestion;
import cn.xueyuetang.questionspider.mango.MangoUtils;
import cn.xueyuetang.questionspider.service.ITmCourseResService;
import cn.xueyuetang.questionspider.service.ITmQuestionService;
import cn.xueyuetang.questionspider.service.SpokingService;
import cn.xueyuetang.questionspider.service.WritingService;
import cn.xueyuetang.questionspider.utils.CodeGEnum;
import cn.xueyuetang.questionspider.utils.CodeGenerator;
import cn.xueyuetang.questionspider.utils.GsonUtil;
import cn.xueyuetang.questionspider.utils.QuestionUtil;

@Service
public class SpokingServiceImpl implements SpokingService {
	@Autowired
	private MangoUtils mangoUtils;

	@Autowired
	private TmKnowledgeDao knowledgeDao;

	@Autowired
	private TmCourseResDao courseResDao;

	@Autowired
	private TmQuestionDao questionDao;

	public List<QuestionWritingItem> getArticleList(Integer type) {
		List<QuestionWritingItem> questionList = new ArrayList<QuestionWritingItem>();
		MongoCollection<Document> collection = mangoUtils.mongoCollection(MangoCollection.Spoking.getName());
		BasicDBObject gt = new BasicDBObject("$eq", type);
		BasicDBObject query = new BasicDBObject("question_module_type", gt);
		FindIterable<Document> resultList = collection.find(query);
		MongoCursor<Document> cursor = resultList.iterator();
		while (cursor.hasNext()) {
			Document questionItem = cursor.next();
			QuestionWritingItem question = GsonUtil.GsonToBean(GsonUtil.GsonString(questionItem),
					QuestionWritingItem.class);
			question.setId(UUID.randomUUID().toString());
			questionList.add(question);
		}
		return questionList;
	}

	@Transactional
	public boolean parseTofel(String courseId, String dbId, String orgId, QuestionWritingItem question,
			Map<String, TmKnowledge> knowledgeMap) {
		String knowledgeNameTemp = question.getQuestionKnowledgeName();
		if (StringUtils.isNotEmpty(knowledgeNameTemp)) {
			knowledgeNameTemp = knowledgeNameTemp.trim();
		}
		String knowledgeName = knowledgeNameTemp;
		if (Constant.SUBJECTMAP.containsKey(knowledgeNameTemp)) {
			knowledgeName = Constant.SUBJECTMAP.get(knowledgeNameTemp) + "/" + knowledgeNameTemp;
		}
		String knowledgeId = insertKnowledge(knowledgeName, courseId, knowledgeMap);

		question.setQuestionContent(parseParagraph(question.getQuestionContent()));

		TmQuestion questionItem = QuestionUtil.buildSpokingQuestion(question);

		if (StringUtils.isNotEmpty(question.getQuestionArticleContent())) {
			String resId = insertRes(question.getName()+" "+question.getQuestionTitle(),question.getQuestionArticleContent(), 7, courseId, knowledgeId,
					knowledgeName, null);
			questionItem.setResId(resId);
		}

		if (StringUtils.isNotEmpty(question.getQuestionAudioUrl())) {
			String resId2 = insertRes(question.getName()+question.getQuestionTitle() + "(题目音频)", question.getQuestionAudioUrl(), 6,
					courseId, knowledgeId, knowledgeName,question.getQuestionAudioContent());
			questionItem.setRes2Id(resId2);
		}
		questionItem.setTag(question.getName().replace("口语", "").trim()+" "+question.getQuestionTitle().trim());
		questionItem.setQFrom(question.getName().replace("口语", "").trim()+" "+question.getQuestionTitle().trim());
		questionItem.setOrgId(orgId);
		questionItem.setQKnowages(knowledgeId);
		questionItem.setQDbid(dbId);
		if (questionDao.insert(questionItem) > 0) {
			return true;
		}
		return false;
	}

	private String parseParagraph(String orgStr) {
		if (StringUtils.isNotEmpty(orgStr)) {
			return "<p>" + orgStr.trim() + "</p>";
		}
		return "";
	}

	private String insertRes(String articleName, String content, Integer fileType, String courseId, String knowledgeId,
			String knowledgeName, String audioContent) {
		String resId = UUID.randomUUID().toString();
		TmCourseRes courseRes = new TmCourseRes();
		courseRes.setResId(resId);
		courseRes.setResName(articleName);
		courseRes.setCourseId(courseId);
		courseRes.setResStatus(Integer.valueOf(0));
		courseRes.setbCreatedate(LocalDateTime.now());
		courseRes.setbModifydate(LocalDateTime.now());
		courseRes.setFileType(fileType);
		if (StringUtils.isNotEmpty(knowledgeId)) {
			courseRes.setKnowage(knowledgeId);
			courseRes.setKnowagename(knowledgeName);
		}
		if (fileType.intValue() == 7) {
			courseRes.setWikicontent(content);
		} else if (fileType.intValue() == 6) {
			content = "upload/audio/" + content;
			courseRes.setFileUrl(content.replace("\\", "/"));
			courseRes.setWikicontent(audioContent);
		}
		if (courseResDao.insert(courseRes) > 0) {
			return resId;
		}
		return null;

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
	public boolean parseIetls(String courseId, String dbId, String orgId, QuestionWritingItem question,
			Map<String, TmKnowledge> knowledgeMap) {
		String knowledgeNameTemp = question.getQuestionKnowledgeName();
		if (StringUtils.isNotEmpty(knowledgeNameTemp)) {
			knowledgeNameTemp = knowledgeNameTemp.trim();
		}
		String knowledgeName = knowledgeNameTemp;
		if (Constant.SUBJECTMAP.containsKey(knowledgeNameTemp)) {
			knowledgeName = Constant.SUBJECTMAP.get(knowledgeNameTemp) + "/" + knowledgeNameTemp;
		}
		String knowledgeId = insertKnowledge(knowledgeName, courseId, knowledgeMap);

		TmQuestion questionItem = QuestionUtil.buildSpokingQuestion(question);
		questionItem.setTag(question.getName().replace("口语", "").trim()+" "+question.getQuestionTitle().trim());
		questionItem.setQFrom(question.getName().replace("口语", "").trim()+" "+question.getQuestionTitle().trim());
		questionItem.setOrgId(orgId);
		questionItem.setQKnowages(knowledgeId);
		questionItem.setQDbid(dbId);
		if (questionDao.insert(questionItem) > 0) {
			return true;
		}
		return false;
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

	public List<QuestionEntity> getQuestionEntityArticleList(String collectionName, Integer type) {
		List<QuestionEntity> questionList = new ArrayList<QuestionEntity>();
		MongoCollection<Document> collection = mangoUtils.mongoCollection(collectionName);
		BasicDBObject gt = new BasicDBObject("$eq", type);
		BasicDBObject query = new BasicDBObject("module_type", gt);
		FindIterable<Document> resultList = collection.find(query);
		MongoCursor<Document> cursor = resultList.iterator();
		while (cursor.hasNext()) {
			Document questionItem = cursor.next();
			QuestionEntity question = GsonUtil.GsonToBean(GsonUtil.GsonString(questionItem),
					QuestionEntity.class);
			question.setId(UUID.randomUUID().toString());
			questionList.add(question);
		}
		Collections.sort(questionList);
		return questionList;
	}

	public boolean parseNoResQUestion(String courseId, String dbId, String orgId, QuestionEntity question,
			Map<String, TmKnowledge> knowledgeMap,Long index) {
		String knowledgeNameTemp = question.getQuestionKnowledge();
		if (StringUtils.isNotEmpty(knowledgeNameTemp)) {
			knowledgeNameTemp = knowledgeNameTemp.trim();
		}
		String knowledgeName = knowledgeNameTemp;
		if (Constant.SUBJECTMAP.containsKey(knowledgeNameTemp)) {
			knowledgeName = Constant.SUBJECTMAP.get(knowledgeNameTemp) + "/" + knowledgeNameTemp;
		}
		if(StringUtils.isEmpty(knowledgeName)||"暂无".equals(knowledgeName)) {
			knowledgeName=question.getModuleName();
		}
		String knowledgeId = insertKnowledge(knowledgeName, courseId, knowledgeMap);
		
		TmQuestion questionItem = QuestionUtil.buildQUestion(question,index);
		questionItem.setTag(question.getQuestionTagName());
		questionItem.setQFrom(question.getQuestionTagName());
		questionItem.setOrgId(orgId);
		questionItem.setQKnowages(knowledgeId);
		questionItem.setQDbid(dbId);
		questionItem.setQOrder(Integer.parseInt(question.getQuestionOrder()));
		if (questionDao.insert(questionItem) > 0) {
			return true;
		}
		return false;
	}
	
	

}
