package cn.xueyuetang.questionspider.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.xueyuetang.questionspider.bean.QuestionEntity;
import cn.xueyuetang.questionspider.bean.QuestionReadingItem;
import cn.xueyuetang.questionspider.bean.QuestionWritingItem;
import cn.xueyuetang.questionspider.constant.MangoCollection;
import cn.xueyuetang.questionspider.constant.QuestionModule;
import cn.xueyuetang.questionspider.entity.TmKnowledge;
import cn.xueyuetang.questionspider.service.ListeningService;
import cn.xueyuetang.questionspider.service.ReadingService;
import cn.xueyuetang.questionspider.service.SpokingService;
import cn.xueyuetang.questionspider.service.WritingService;

@Component
public class Main {
	private static final Logger logger = LogManager.getLogger(Main.class);

	@Autowired
	private ReadingService readingService;

	@Autowired
	private WritingService writingService;

	@Autowired
	private ListeningService listeningService;

	@Autowired
	private SpokingService spokingService;

	public void parseTofelReading() {
		logger.debug("开始解析托福阅读");
		String courseId = "c2077e3d-2eeb-4c8a-a94a-cf3372f0db5c";
		String qdbId = "93a1375f-d5ae-4820-8d71-13f97ab5195a";
		String orgId = "c6fb64aa-d38d-426f-9f65-ee0b491e6755";

		logger.debug("从mangodb获取数据");
		Map<String, List<QuestionReadingItem>> questionListOfArticleMap = readingService
				.getArticleList(QuestionModule.ToeflReading.getTypeId());
		logger.debug("从mangodb获取数据完成");
		Map<String, TmKnowledge> knowledgeMap = readingService.getKnowledgeMap(courseId);
		for (Entry<String, List<QuestionReadingItem>> questionListEntry : questionListOfArticleMap.entrySet()) {
			List<QuestionReadingItem> questionList = questionListEntry.getValue();
			readingService.parseTofelArticle(courseId, qdbId, orgId, questionList, knowledgeMap);
		}
		logger.debug("解析托福阅读完成");
	}

	public void parseIeltsReading() {
		logger.debug("开始解析雅思阅读");
		String courseId = "71088362-aeda-4434-8682-9cce8d1db1ad";
		String qdbId = "6b2c2a53-684f-488f-aea0-99304750331c";
		String orgId = "c6fb64aa-d38d-426f-9f65-ee0b491e6755";

		logger.debug("从mangodb获取数据");
		Map<String, List<QuestionReadingItem>> questionListOfArticleMap = readingService
				.getArticleList(QuestionModule.IeltsReading.getTypeId());
		logger.debug("从mangodb获取数据完成");

		Map<String, TmKnowledge> knowledgeMap = readingService.getKnowledgeMap(courseId);
		for (Entry<String, List<QuestionReadingItem>> questionListEntry : questionListOfArticleMap.entrySet()) {
			List<QuestionReadingItem> questionList = questionListEntry.getValue();
			readingService.parseIetlsArticle(courseId, qdbId, orgId, questionList, knowledgeMap);
		}
		logger.debug("解析雅思福阅读完成");
	}

	public void parseTofelWriting() {
		logger.debug("开始解析托福写作");
		String courseId = "c7e31823-8db4-48a9-9690-523cd3cc4a83";
		String qdbId = "6ddb46ef-ed79-4ca8-b85d-fceabcf3a205";
		String orgId = "c6fb64aa-d38d-426f-9f65-ee0b491e6755";

		logger.debug("从mangodb获取数据");
		List<QuestionWritingItem> questionList = writingService.getArticleList(QuestionModule.ToeflWriting.getTypeId());
		logger.debug("从mangodb获取数据完成");

		Map<String, TmKnowledge> knowledgeMap = writingService.getKnowledgeMap(courseId);
		for (QuestionWritingItem question : questionList) {
			writingService.parseTofel(courseId, qdbId, orgId, question, knowledgeMap);
		}
		logger.debug("解析托福写作完成");
	}

	public void parseIetlsWriting() {
		logger.debug("解析雅思写作");
		String courseId = "571da38c-32e1-470b-88ff-55b508ff3538";
		String qdbId = "44306044-6139-4add-b188-e81a9b4781e1";
		String orgId = "c6fb64aa-d38d-426f-9f65-ee0b491e6755";

		logger.debug("从mangodb获取数据");
		List<QuestionWritingItem> questionList = writingService.getArticleList(QuestionModule.IeltsWriting.getTypeId());
		logger.debug("从mangodb获取数据完成");

		Map<String, TmKnowledge> knowledgeMap = writingService.getKnowledgeMap(courseId);
		for (QuestionWritingItem question : questionList) {
			writingService.parseIetls(courseId, qdbId, orgId, question, knowledgeMap);
		}
		logger.debug("解析雅思写作完成");
	}

	public void parseTofelListening() {
		logger.debug("解析托福听力");
		String courseId = "9910ecbd-e9c7-468f-8e22-5ca9e1b48cf0";
		String qdbId = "49d8d4fd-5125-4b15-8033-8aa90d0ed3ad";
		String orgId = "c6fb64aa-d38d-426f-9f65-ee0b491e6755";

		logger.debug("从mangodb获取数据");
		Map<String, List<QuestionReadingItem>> questionListOfArticleMap = listeningService
				.getArticleList(QuestionModule.TofelListening.getTypeId());
		logger.debug("从mangodb获取数据完成");

		Map<String, TmKnowledge> knowledgeMap = listeningService.getKnowledgeMap(courseId);
		for (Entry<String, List<QuestionReadingItem>> questionListEntry : questionListOfArticleMap.entrySet()) {
			List<QuestionReadingItem> questionList = questionListEntry.getValue();
			listeningService.parseTofelArticle(courseId, qdbId, orgId, questionList, knowledgeMap);
		}
		logger.debug("解析托福听力完成");
	}

	public void parseIetlsListening() {
		logger.debug("解析雅思听力");
		String courseId = "565639e8-f7e6-4380-8dda-01ba528d2c11";
		String qdbId = "505a3d02-0429-4b78-a578-8b4ab8349f7e";
		String orgId = "c6fb64aa-d38d-426f-9f65-ee0b491e6755";

		logger.debug("从mangodb获取数据");
		Map<String, List<QuestionReadingItem>> questionListOfArticleMap = listeningService
				.getArticleList(QuestionModule.IetlsListening.getTypeId());
		logger.debug("从mangodb获取数据完成");

		Map<String, TmKnowledge> knowledgeMap = listeningService.getKnowledgeMap(courseId);
		for (Entry<String, List<QuestionReadingItem>> questionListEntry : questionListOfArticleMap.entrySet()) {
			List<QuestionReadingItem> questionList = questionListEntry.getValue();
			listeningService.parseIetlsArticle(courseId, qdbId, orgId, questionList, knowledgeMap);
		}
		logger.debug("解析雅思听力完成");
	}

	public void parseTofelSpoking() {
		logger.debug("解析托福口语");
		String courseId = "febe86d7-0800-42ab-8715-e03d5ba8bdd7";
		String qdbId = "78e384a6-c90c-49a7-b014-5c59243b3937";
		String orgId = "c6fb64aa-d38d-426f-9f65-ee0b491e6755";

		logger.debug("从mangodb获取数据");
		List<QuestionWritingItem> questionList = spokingService.getArticleList(QuestionModule.TofelSpoking.getTypeId());
		logger.debug("从mangodb获取数据完成");

		Map<String, TmKnowledge> knowledgeMap = spokingService.getKnowledgeMap(courseId);
		for (QuestionWritingItem question : questionList) {
			spokingService.parseTofel(courseId, qdbId, orgId, question, knowledgeMap);
		}
		logger.debug("解析托福口语完成");
	}

	public void parseIetlsSpoking() {
		logger.debug("解析雅思口语");
		String courseId = "ea69c873-a8b6-4321-9607-fcf94f89050a";
		String qdbId = "6caee054-2833-4529-8120-669e49ab3a57";
		String orgId = "c6fb64aa-d38d-426f-9f65-ee0b491e6755";

		logger.debug("从mangodb获取数据");
		List<QuestionWritingItem> questionList = spokingService.getArticleList(QuestionModule.IetlsSpoking.getTypeId());
		logger.debug("从mangodb获取数据完成");

		Map<String, TmKnowledge> knowledgeMap = spokingService.getKnowledgeMap(courseId);
		for (QuestionWritingItem question : questionList) {
			spokingService.parseIetls(courseId, qdbId, orgId, question, knowledgeMap);
		}
		logger.debug("解析雅思口语完成");
	}

	public void parseSatReading() {
		logger.debug("解析SAT阅读");
		String courseId = "187eaea5-4325-416c-8b3e-1c73f6673012";
		String qdbId = "eb191d0f-2954-453a-aa27-90219a14dfac";
		String orgId = "4796e996-a9ac-423e-adfc-9f47ed74f5eb";

		logger.debug("从mangodb获取数据");
		Map<String, List<QuestionEntity>> questionListOfArticleMap = readingService
				.getQuestionEntityArticleList(MangoCollection.Sat.getName(), QuestionModule.SatReading.getTypeId());
		logger.debug("从mangodb获取数据完成");

		Map<String, TmKnowledge> knowledgeMap = readingService.getKnowledgeMap(courseId);
		for (Entry<String, List<QuestionEntity>> questionListEntry : questionListOfArticleMap.entrySet()) {
			List<QuestionEntity> questionList = questionListEntry.getValue();
			readingService.parseSatArticle(courseId, qdbId, orgId, questionList, knowledgeMap);
		}
		logger.debug("解析SAT阅读完成");
	}

	public void parseSatGrammar() {
		logger.debug("解析SAT语法");
		String courseId = "ff8a7b6f-f85e-4454-a877-2b19ce15c274";
		String qdbId = "de33ccd4-2470-41ee-8f65-d1803793ec8b";
		String orgId = "4796e996-a9ac-423e-adfc-9f47ed74f5eb";

		logger.debug("从mangodb获取数据");
		Map<String, List<QuestionEntity>> questionListOfArticleMap = readingService
				.getQuestionEntityArticleList(MangoCollection.Sat.getName(), QuestionModule.SatGrammar.getTypeId());
		logger.debug("从mangodb获取数据完成");

		Map<String, TmKnowledge> knowledgeMap = readingService.getKnowledgeMap(courseId);
		for (Entry<String, List<QuestionEntity>> questionListEntry : questionListOfArticleMap.entrySet()) {
			List<QuestionEntity> questionList = questionListEntry.getValue();
			readingService.parseSatArticle(courseId, qdbId, orgId, questionList, knowledgeMap);
		}
		logger.debug("解析SAT语法完成");
	}

	public void parseGreReading() {
		logger.debug("解析Gre阅读");
		String courseId = "48020ca1-9a43-44ee-b8a8-20bb5534cb23";
		String qdbId = "b5c27378-b0b9-4fb6-bc54-f284c8d29b71";
		String orgId = "c6fb64aa-d38d-426f-9f65-ee0b491e6755";

		logger.debug("从mangodb获取数据");
		Map<String, List<QuestionEntity>> questionListOfArticleMap = readingService
				.getQuestionEntityArticleList(MangoCollection.GRE.getName(), QuestionModule.GreReading.getTypeId());
		logger.debug("从mangodb获取数据完成");

		Map<String, TmKnowledge> knowledgeMap = readingService.getKnowledgeMap(courseId);
		for (Entry<String, List<QuestionEntity>> questionListEntry : questionListOfArticleMap.entrySet()) {
			List<QuestionEntity> questionList = questionListEntry.getValue();
			readingService.parseSatArticle(courseId, qdbId, orgId, questionList, knowledgeMap);
		}
		logger.debug("解析Gre阅读完成");
	}

	public void parseSatWriting() {
		logger.debug("解析SAT写作");
		String courseId = "c5551849-83e9-46f9-bcc4-752c8bb05cae";
		String qdbId = "2dbd5a71-a2c7-4417-9148-c2394343e5a2";
		String orgId = "4796e996-a9ac-423e-adfc-9f47ed74f5eb";

		logger.debug("从mangodb获取数据");
		List<QuestionEntity> questionList = writingService.getQuestionEntityArticleList(MangoCollection.Sat.getName(),
				QuestionModule.SatWriting.getTypeId());
		logger.debug("从mangodb获取数据完成");

		Map<String, TmKnowledge> knowledgeMap = writingService.getKnowledgeMap(courseId);
		Long index = 0L;
		for (QuestionEntity question : questionList) {
			writingService.parseWriting(courseId, qdbId, orgId, question, knowledgeMap, index++);
		}
		logger.debug("解析SAT写作完成");
	}

	public void parseGreWriting() {
		logger.debug("解析GRE写作");
		String courseId = "b255c63b-eaec-4aa0-8ecc-72a4a7722e15";
		String qdbId = "f2c00b85-e312-4848-9792-482f80519400";
		String orgId = "c6fb64aa-d38d-426f-9f65-ee0b491e6755";

		logger.debug("从mangodb获取数据");
		List<QuestionEntity> questionList = writingService.getQuestionEntityArticleList(MangoCollection.GRE.getName(),
				QuestionModule.GreWriting.getTypeId());
		logger.debug("从mangodb获取数据完成");

		Map<String, TmKnowledge> knowledgeMap = writingService.getKnowledgeMap(courseId);
		Long index = 0L;
		for (QuestionEntity question : questionList) {
			writingService.parseWriting(courseId, qdbId, orgId, question, knowledgeMap, index++);
		}
		logger.debug("解析GRE写作完成");
	}

	public void parseSatMath() {
		logger.debug("解析SAT数学");
		String courseId = "cf4578af-18af-4a5c-862b-aa6e26d04e66";
		String qdbId = "97d47d96-2080-4c55-9a30-38cdfb5937ec";
		String orgId = "4796e996-a9ac-423e-adfc-9f47ed74f5eb";

		logger.debug("从mangodb获取数据");
		List<QuestionEntity> questionList = spokingService.getQuestionEntityArticleList(MangoCollection.Sat.getName(),
				QuestionModule.SatMath.getTypeId());
		logger.debug("从mangodb获取数据完成");

		Map<String, TmKnowledge> knowledgeMap = spokingService.getKnowledgeMap(courseId);
		Long index = 0L;
		for (QuestionEntity questionListEntry : questionList) {
			spokingService.parseNoResQUestion(courseId, qdbId, orgId, questionListEntry, knowledgeMap, index++);
		}
		logger.debug("解析SAT数学完成");
	}

	public void parseGreMath() {
		logger.debug("解析GRE数学");
		String courseId = "e0f62c74-b107-4ff5-b944-dd42c4017e0f";
		String qdbId = "97d47d96-2080-4c55-9a30-38cdfb5937ec";
		String orgId = "c6fb64aa-d38d-426f-9f65-ee0b491e6755";

		logger.debug("从mangodb获取数据");
		List<QuestionEntity> questionList = spokingService.getQuestionEntityArticleList(MangoCollection.GRE.getName(),
				QuestionModule.GreMath.getTypeId());
		logger.debug("从mangodb获取数据完成");

		Map<String, TmKnowledge> knowledgeMap = spokingService.getKnowledgeMap(courseId);
		Long index = 0L;
		for (QuestionEntity questionListEntry : questionList) {
			spokingService.parseNoResQUestion(courseId, qdbId, orgId, questionListEntry, knowledgeMap, index++);
		}
		logger.debug("解析GRE数学完成");
	}

	public void parseGreBlank() {
		logger.debug("解析GRE填空");
		String courseId = "e2f5615a-77be-4b61-80d7-90a32a662651";
		String qdbId = "977fd6e5-460e-4a79-b383-d3523158d8d6";
		String orgId = "c6fb64aa-d38d-426f-9f65-ee0b491e6755";

		logger.debug("从mangodb获取数据");
		List<QuestionEntity> questionList = spokingService.getQuestionEntityArticleList(MangoCollection.GRE.getName(),
				QuestionModule.GreBlank.getTypeId());
		logger.debug("从mangodb获取数据完成");

		Map<String, TmKnowledge> knowledgeMap = spokingService.getKnowledgeMap(courseId);
		Long index = 0L;
		for (QuestionEntity questionListEntry : questionList) {
			spokingService.parseNoResQUestion(courseId, qdbId, orgId, questionListEntry, knowledgeMap, index++);
		}
		logger.debug("解析GRE填空完成");
	}

	public void parseGreWord() {
		logger.debug("解析GRE单词");
		String courseId = "c078a8ea-57be-4774-b508-47119e2a923d";
		String qdbId = "936c3f31-b7ca-4d60-9297-9e6b8360ea00";
		String orgId = "c6fb64aa-d38d-426f-9f65-ee0b491e6755";

		logger.debug("从mangodb获取数据");
		List<QuestionEntity> questionList = spokingService.getQuestionEntityArticleList(MangoCollection.GRE.getName(),
				QuestionModule.GreWord.getTypeId());
		logger.debug("从mangodb获取数据完成");

		Map<String, TmKnowledge> knowledgeMap = spokingService.getKnowledgeMap(courseId);
		Long index = 0L;
		for (QuestionEntity questionListEntry : questionList) {
			spokingService.parseNoResQUestion(courseId, qdbId, orgId, questionListEntry, knowledgeMap, index++);
		}
		logger.debug("解析GRE单词完成");
	}

	public void parseSATWord() {
		logger.debug("解析SAT单词");
		String courseId = "31a621ae-b343-452e-80dc-d46b9daa913b";
		String qdbId = "62d579c8-ea1e-47d4-a2b3-e8544644c70a";
		String orgId = "4796e996-a9ac-423e-adfc-9f47ed74f5eb";

		logger.debug("从mangodb获取数据");
		List<QuestionEntity> questionList = spokingService.getQuestionEntityArticleList(MangoCollection.Sat.getName(),
				QuestionModule.SatWord.getTypeId());
		logger.debug("从mangodb获取数据完成");

		Map<String, TmKnowledge> knowledgeMap = spokingService.getKnowledgeMap(courseId);
		Long index = 0L;
		for (QuestionEntity questionListEntry : questionList) {
			spokingService.parseNoResQUestion(courseId, qdbId, orgId, questionListEntry, knowledgeMap, index++);
		}
		logger.debug("解析SAT单词完成");
	}

	/**
	 * 解析GMAT阅读
	 */
	public void parseGmatReading() {
		logger.debug("解析Gmat阅读");
		String courseId = "7756d39b-c92c-4fd3-959d-adb74c99ddb1";
		String qdbId = "a3a2d6bb-eec1-48e5-a78a-d78825cd5a54";
		String orgId = "4796e996-a9ac-423e-adfc-9f47ed74f5eb";

		logger.debug("从mangodb获取数据");
		Map<String, List<QuestionEntity>> questionListOfArticleMap = readingService
				.getQuestionEntityArticleList(MangoCollection.GMAT.getName(), QuestionModule.GmatRc.getTypeId());
		logger.debug("从mangodb获取数据完成");

		Map<String, TmKnowledge> knowledgeMap = readingService.getKnowledgeMap(courseId);
		for (Entry<String, List<QuestionEntity>> questionListEntry : questionListOfArticleMap.entrySet()) {
			List<QuestionEntity> questionList = questionListEntry.getValue();
			readingService.parseSatArticle(courseId, qdbId, orgId, questionList, knowledgeMap);
		}
		logger.debug("解析Gmat阅读完成");
	}

	public void parseGmatMath() {
		logger.debug("解析GMAT数学");
		String courseId = "4c5c8a7b-4e45-49b5-badc-f491e241a6aa";
		String qdbId = "7aab2107-f1b1-46d6-a64f-caf8479bcafd";
		String orgId = "4796e996-a9ac-423e-adfc-9f47ed74f5eb";

		logger.debug("从mangodb获取数据");
		List<QuestionEntity> questionList = spokingService.getQuestionEntityArticleList(MangoCollection.GMAT.getName(),
				QuestionModule.GmatMath.getTypeId());
		logger.debug("从mangodb获取数据完成");

		Map<String, TmKnowledge> knowledgeMap = spokingService.getKnowledgeMap(courseId);
		Long index = 0L;
		for (QuestionEntity questionListEntry : questionList) {
			spokingService.parseNoResQUestion(courseId, qdbId, orgId, questionListEntry, knowledgeMap, index++);
		}
		logger.debug("解析GMAT数学完成");
	}

	/**
	 * gmat语法
	 */
	public void parseGmatSc() {
		logger.debug("解析GMAT语法");
		String courseId = "94a3be41-53c0-4915-8e35-6fb6a16bcdc6";
		String qdbId = "cb5bbbbc-39e5-461b-9236-0da4bb0bdcfc";
		String orgId = "4796e996-a9ac-423e-adfc-9f47ed74f5eb";

		logger.debug("从mangodb获取数据");
		List<QuestionEntity> questionList = spokingService.getQuestionEntityArticleList(MangoCollection.GMAT.getName(),
				QuestionModule.GmatSc.getTypeId());
		logger.debug("从mangodb获取数据完成");

		Map<String, TmKnowledge> knowledgeMap = spokingService.getKnowledgeMap(courseId);
		Long index = 0L;
		for (QuestionEntity questionListEntry : questionList) {
			spokingService.parseNoResQUestion(courseId, qdbId, orgId, questionListEntry, knowledgeMap, index++);
		}
		logger.debug("解析GMAT语法完成");
	}

	/**
	 * gmat逻辑
	 */
	public void parseGmatCR() {
		logger.debug("解析GMAT逻辑");
		String courseId = "8727725f-295a-45e1-a43e-f8ecaecdd779";
		String qdbId = "2160c052-4407-4cbf-95a3-6167ceeb1ea7";
		String orgId = "4796e996-a9ac-423e-adfc-9f47ed74f5eb";

		logger.debug("从mangodb获取数据");
		List<QuestionEntity> questionList = spokingService.getQuestionEntityArticleList(MangoCollection.GMAT.getName(),
				QuestionModule.GmatCr.getTypeId());
		logger.debug("从mangodb获取数据完成");

		Map<String, TmKnowledge> knowledgeMap = spokingService.getKnowledgeMap(courseId);
		Long index = 0L;
		for (QuestionEntity questionListEntry : questionList) {
			spokingService.parseNoResQUestion(courseId, qdbId, orgId, questionListEntry, knowledgeMap, index++);
		}
		logger.debug("解析GMAT逻辑完成");
	}

	/**
	 * GMAT 写作
	 */
	public void parseGmatWriting() {
		logger.debug("解析GMAT写作");
		String courseId = "b9f90697-f3bf-4394-974b-4ba250100563";
		String qdbId = "a00054af-44ff-4c3d-94b0-c4ca2b134b9c";
		String orgId = "4796e996-a9ac-423e-adfc-9f47ed74f5eb";

		logger.debug("从mangodb获取数据");
		List<QuestionEntity> questionList = writingService.getQuestionEntityArticleList(MangoCollection.GMAT.getName(),
				QuestionModule.GmatWriting.getTypeId());
		logger.debug("从mangodb获取数据完成");

		Map<String, TmKnowledge> knowledgeMap = writingService.getKnowledgeMap(courseId);
		Long index = 0L;
		for (QuestionEntity question : questionList) {
			writingService.parseGmatWriting(courseId, qdbId, orgId, question, knowledgeMap, index++);
		}
		logger.debug("解析GMAT写作完成");
	}

	/**
	 * GMAT 综合推理
	 */
	public void parseGmatIR() {
		logger.debug("解析GMAT综合推理");
		String courseId = "7c32456c-dc0a-4db8-8632-9bb8c4a289ef";
		String qdbId = "8c1ef993-53b2-4619-8215-d7ec719c1fdc";
		String orgId = "4796e996-a9ac-423e-adfc-9f47ed74f5eb";

		logger.debug("从mangodb获取数据");
		List<QuestionEntity> questionList = writingService.getQuestionEntityArticleList(MangoCollection.GMAT.getName(),
				QuestionModule.GmatIr.getTypeId());
		logger.debug("从mangodb获取数据完成");

		Map<String, TmKnowledge> knowledgeMap = readingService.getKnowledgeMap(courseId);
		for (QuestionEntity question : questionList) {
			readingService.parseGmatArticle(courseId, qdbId, orgId, question, knowledgeMap);
		}
		logger.debug("解Gmat综合完成");
	}

	/**
	 * Gmat单词
	 */
	public void parseGmatWord() {
		logger.debug("解析GMAT单词");
		String courseId = "397b8ea8-4bc0-435c-9df0-20543dd4a521";
		String qdbId = "3bc03e4c-2b66-431b-9899-1064b5016980";
		String orgId = "4796e996-a9ac-423e-adfc-9f47ed74f5eb";

		logger.debug("从mangodb获取数据");
		List<QuestionEntity> questionList = spokingService.getQuestionEntityArticleList(MangoCollection.GMAT.getName(),
				QuestionModule.GmatWord.getTypeId());
		logger.debug("从mangodb获取数据完成");

		Map<String, TmKnowledge> knowledgeMap = spokingService.getKnowledgeMap(courseId);
		Long index = 0L;
		for (QuestionEntity questionListEntry : questionList) {
			spokingService.parseNoResQUestion(courseId, qdbId, orgId, questionListEntry, knowledgeMap, index++);
		}
		logger.debug("解析GMAT单词完成");
	}

}
