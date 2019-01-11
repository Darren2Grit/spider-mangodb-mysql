package cn.xueyuetang.questionspider.bean;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;

@Data
public class QuestionEntity implements Comparable<QuestionEntity> {
	private String id;
	private String moduleName;
	private String moduleType;
	private String questionTagName;
	private String articleId;
	private String articleContent;
	private String questionKnowledge;
	private String questionTitle;
	private List<String> questionOption;
	private String questionAnswer;
	private String questionResolve;
	private String questionType;
	private String questionOrder;
	private List<Object> questionList;

	public int compareTo(QuestionEntity o) {
		if (StringUtils.isEmpty(this.questionOrder)) {
			return 1;
		}

		if (StringUtils.isEmpty(o.getQuestionOrder())) {
			return -1;
		}
		return Integer.parseInt(this.getQuestionOrder()) - Integer.parseInt(o.getQuestionOrder());

	}
}
