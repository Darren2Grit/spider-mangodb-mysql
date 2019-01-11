package cn.xueyuetang.questionspider.bean;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;

@Data
public class QuestionReadingItem implements Comparable<QuestionReadingItem> {
	private String id;
	private String module;
	private String name;
	private String questionTitle;
	private String questionKnowledgeName;
	private String questionAnswer;
	private List<String> questionContent;
	private String questionModuleType;
	private String questionType;
	private String questionResolveContent;
	private String questionBelongParagraph;
	private String questionArticleTitle;
	private String questionArticleId;
	private List<String> questionArticleContent;
	private List<String> questionArticleContentTranslation;
	private String questionAudioUrl;
	private String questionAudioArticleContent;
	private List<String> questionContentFileUrlList;
	private String questionAudioRefer;
	private String questionOrder;
	private String questionInsertContent;

	public int compareTo(QuestionReadingItem o) {
		if (StringUtils.isEmpty(o.getQuestionOrder())) {
			return -1;
		}
		
		if (StringUtils.isEmpty(this.questionOrder)) {
			return 1;
		}
		return Integer.parseInt(this.questionOrder)-Integer.parseInt(o.getQuestionOrder());
	}
}
