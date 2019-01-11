package cn.xueyuetang.questionspider.bean;

import lombok.Data;

@Data
public class QuestionWritingItem {
	private String id;
	private String name;
	private String questionTitle;
	private String questionKnowledgeName;
	private String questionAnswer;
	private String questionContent;
	private String questionType;
	private String questionResolveContent;
	private String questionArticleId;
	private String questionArticleContent;
	private String questionAudioContent;
	private String questionAudioUrl;
	private String questionAudioRefer;
	private String questionModuleType;
}
