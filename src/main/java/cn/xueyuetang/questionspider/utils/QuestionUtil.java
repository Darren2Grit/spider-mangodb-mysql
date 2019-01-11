package cn.xueyuetang.questionspider.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;

import com.exam.model.paper.Option;
import com.exam.model.paper.Question;
import com.exam.model.paper.QuestionBlankFill;
import com.exam.model.paper.QuestionEssay;
import com.exam.model.paper.QuestionJudgment;
import com.exam.model.paper.QuestionMultipleChoice;
import com.exam.model.paper.QuestionSingleChoice;
import com.thoughtworks.xstream.XStream;

import cn.xueyuetang.questionspider.bean.QuestionEntity;
import cn.xueyuetang.questionspider.bean.QuestionReadingItem;
import cn.xueyuetang.questionspider.bean.QuestionWritingItem;
import cn.xueyuetang.questionspider.entity.TmQuestion;

public class QuestionUtil {
	public static TmQuestion buildQuestion(QuestionReadingItem questionReading,Long index) {
		XStream xstream = new XStream();
		TmQuestion questionResult = new TmQuestion();
		String result = "";
		int qType = 1;
		if ("1".equals(questionReading.getQuestionType())) {
			String answer = questionReading.getQuestionAnswer();
			if (answer.length() == 1) {
				qType = 1;
				QuestionSingleChoice question = new QuestionSingleChoice();
				if (questionReading.getQuestionContent() != null) {
					char alisa = 'A';
					for (String option : questionReading.getQuestionContent()) {
						question.addOption(new Option(String.valueOf(alisa), option));
						alisa = (char) (alisa + '\001');
					}
				}

				question.setId(questionReading.getId());
				question.setContent(questionReading.getQuestionTitle().trim());
				question.setKey(questionReading.getQuestionAnswer().trim());
				question.setExt(questionReading.getQuestionResolveContent().trim());
				result = xstream.toXML(question);
			} else {
				qType = 2;
				QuestionMultipleChoice question = new QuestionMultipleChoice();
				if (questionReading.getQuestionContent() != null) {
					char alisa = 'A';
					for (String option : questionReading.getQuestionContent()) {
						question.addOption(new Option(String.valueOf(alisa), option));
						alisa = (char) (alisa + '\001');
					}
				}
				question.setId(questionReading.getId());
				question.setContent(questionReading.getQuestionTitle().trim());
				question.setKey(questionReading.getQuestionAnswer().trim());
				question.setExt(questionReading.getQuestionResolveContent().trim());
				result = xstream.toXML(question);
			}

		} else if ("2".equals(questionReading.getQuestionType())) { // 判断 填空
			qType = 4;
			QuestionBlankFill question = new QuestionBlankFill();
			String[] questionAnswer = questionReading.getQuestionAnswer().split(",");
			for (int i = 0; i < questionAnswer.length; i++) {
				question.addBlank((i + 1), "BLANK" + (i + 1), questionAnswer[i]);
			}
			question.setId(questionReading.getId());
			question.setContent(
					questionReading.getQuestionTitle() + questionReading.getQuestionContent().get(0).trim());
			question.setKey(questionReading.getQuestionAnswer().trim());
			question.setExt(questionReading.getQuestionResolveContent().trim());

			result = xstream.toXML(question);
		} else if ("3".equals(questionReading.getQuestionType())) {
			qType = 4;
			QuestionBlankFill question = new QuestionBlankFill();
			String[] questionAnswer = questionReading.getQuestionAnswer().split(",");
			for (int i = 0; i < questionAnswer.length; i++) {
				question.addBlank((i + 1), "BLANK" + (i + 1), questionAnswer[i]);
			}
			question.setId(questionReading.getId());
			question.setContent(
					questionReading.getQuestionTitle() + questionReading.getQuestionContent().get(0).trim());
			question.setKey(questionReading.getQuestionAnswer().trim());
			question.setExt(questionReading.getQuestionResolveContent().trim());

			result = xstream.toXML(question);
		} else if ("4".equals(questionReading.getQuestionType())) {
			qType = 2;
			QuestionMultipleChoice question = new QuestionMultipleChoice();
			if (questionReading.getQuestionContent() != null) {
				char alisa = 'A';
				for (String option : questionReading.getQuestionContent()) {
					question.addOption(new Option(String.valueOf(alisa), option));
					alisa = (char) (alisa + '\001');
				}
			}
			question.setId(questionReading.getId());
			question.setContent(questionReading.getQuestionTitle().trim());
			question.setKey(questionReading.getQuestionAnswer().trim());
			question.setExt(questionReading.getQuestionResolveContent().trim());
			result = xstream.toXML(question);
		} else if ("5".equals(questionReading.getQuestionType())) {
			qType = 5;
			QuestionJudgment question = new QuestionJudgment();
			question.setId(questionReading.getId());
			question.setExt(questionReading.getQuestionResolveContent().trim());
			question.setKey(questionReading.getQuestionAnswer());
			question.setType("3");
			question.setContent(
					questionReading.getQuestionTitle() + questionReading.getQuestionContent().get(0).trim());
			result = xstream.toXML(question);
		} else if ("6".equals(questionReading.getQuestionType())) {
			qType = 1;
			QuestionSingleChoice question = new QuestionSingleChoice();
			if (questionReading.getQuestionContent() != null) {
				char alisa = 'A';
				for (String option : questionReading.getQuestionContent()) {
					question.addOption(new Option(String.valueOf(alisa), option));
					alisa = (char) (alisa + '\001');
				}
			}

			question.setId(questionReading.getId());
			question.setContent(questionReading.getQuestionTitle().trim());
			question.setKey(questionReading.getQuestionAnswer().trim());
			question.setExt(questionReading.getQuestionResolveContent().trim());
			result = xstream.toXML(question);
		}
		questionResult.setQId(questionReading.getId());
		questionResult.setQType(qType);
		questionResult.setQLevel(Integer.valueOf(3));
		if (qType == 4 || qType == 5) {
			questionResult.setQContent(
					questionReading.getQuestionTitle() + questionReading.getQuestionContent().get(0).trim());
		} else {
			questionResult.setQContent(questionReading.getQuestionTitle().trim());
		}
		questionResult.setQKey(questionReading.getQuestionAnswer().trim());
		questionResult.setQResolve(questionReading.getQuestionResolveContent().trim());
		questionResult.setQCreatedate(LocalDateTime.now().plusSeconds(index));
		questionResult.setQData(result);
		return questionResult;
	}

	public static TmQuestion buildQuestion(QuestionWritingItem questionReading) {
		XStream xstream = new XStream();
		TmQuestion questionResult = new TmQuestion();

		QuestionEssay question = new QuestionEssay();
		question.setId(questionReading.getId());
		question.setKey(questionReading.getQuestionAnswer().trim());
		question.setType("5");
		question.setExt(questionReading.getQuestionAudioContent());
		String result = xstream.toXML(question);

		questionResult.setQId(questionReading.getId());
		questionResult.setQType(Integer.valueOf(5));
		questionResult.setQLevel(Integer.valueOf(3));
		questionResult.setQStatus(Integer.valueOf(1));
		questionResult.setQKey(questionReading.getQuestionAnswer().trim());
		questionResult.setQResolve(questionReading.getQuestionAudioContent());
		questionResult.setQCreatedate(LocalDateTime.now());
		questionResult.setQData(result);
		return questionResult;
	}

	public static TmQuestion buildSpokingQuestion(QuestionWritingItem questionReading) {
		XStream xstream = new XStream();
		TmQuestion questionResult = new TmQuestion();

		QuestionEssay question = new QuestionEssay();
		question.setId(questionReading.getId());
		question.setContent(questionReading.getQuestionContent());
		question.setType("6");
		question.setKey(questionReading.getQuestionAnswer().trim());
		question.setExt(questionReading.getQuestionAnswer().trim());
		String result = xstream.toXML(question);

		questionResult.setQId(questionReading.getId());
		questionResult.setQType(Integer.valueOf(6));
		questionResult.setQLevel(Integer.valueOf(3));
		questionResult.setQStatus(Integer.valueOf(1));
		questionResult.setQContent(questionReading.getQuestionContent());
		questionResult.setQKey(questionReading.getQuestionAnswer().trim());
		questionResult.setQResolve(questionReading.getQuestionAnswer().trim());
		questionResult.setQCreatedate(LocalDateTime.now());
		questionResult.setQData(result);
		return questionResult;
	}

	public static TmQuestion buildQUestion(QuestionEntity questionEntity,Long index) {
		XStream xstream = new XStream();
		TmQuestion questionResult = new TmQuestion();
		String result = "";
		int qType = 1;
		if ("1".equals(questionEntity.getQuestionType())) {
			QuestionSingleChoice question = new QuestionSingleChoice();
			if (questionEntity.getQuestionOption() != null) {
				char alisa = 'A';
				for (String option : questionEntity.getQuestionOption()) {
					question.addOption(new Option(String.valueOf(alisa), option));
					alisa = (char) (alisa + '\001');
				}
			}

			question.setId(questionEntity.getId());
			question.setContent(questionEntity.getQuestionTitle());
			question.setKey(questionEntity.getQuestionAnswer());
			if(StringUtils.isNotBlank(questionEntity.getQuestionResolve())){
				question.setExt(questionEntity.getQuestionResolve());
			}
			result = xstream.toXML(question);
		}else if("2".equals(questionEntity.getQuestionType())){
			qType = 2;
			QuestionMultipleChoice question = new QuestionMultipleChoice();
			if (questionEntity.getQuestionOption() != null) {
				char alisa = 'A';
				for (String option : questionEntity.getQuestionOption()) {
					question.addOption(new Option(String.valueOf(alisa), option));
					alisa = (char) (alisa + '\001');
				}
			}
			question.setId(questionEntity.getId());
			question.setContent(questionEntity.getQuestionTitle());
			question.setKey(questionEntity.getQuestionAnswer());
			question.setExt(questionEntity.getQuestionResolve());
			result = xstream.toXML(question);
		}else if("3".equals(questionEntity.getQuestionType())){
			qType = 4;
			QuestionBlankFill question = new QuestionBlankFill();
			String[] questionAnswer = questionEntity.getQuestionAnswer().split(",");
			for (int i = 0; i < questionAnswer.length; i++) {
				question.addBlank((i + 1), "BLANK" + (i + 1), questionAnswer[i]);
			}
			question.setId(questionEntity.getId());
			question.setContent(questionEntity.getQuestionTitle().trim());
			question.setKey(questionEntity.getQuestionAnswer());
			question.setExt(questionEntity.getQuestionResolve());

			result = xstream.toXML(question);
		}else if("4".equals(questionEntity.getQuestionType())){
			
		}else if("5".equals(questionEntity.getQuestionType())){
			qType = 5;
			QuestionEssay question = new QuestionEssay();
			question.setId(questionEntity.getId());
			question.setKey(questionEntity.getQuestionAnswer());
			question.setType("5");
			question.setExt(questionEntity.getQuestionResolve());
			result = xstream.toXML(question);
		}
		
		
		questionResult.setQId(questionEntity.getId());
		questionResult.setQKey(questionEntity.getQuestionAnswer());
		questionResult.setQResolve(questionEntity.getQuestionResolve());
		questionResult.setQCreatedate(LocalDateTime.now().plusSeconds(index));
		questionResult.setQData(result.replaceAll("&#x2;", ""));
		questionResult.setQType(Integer.valueOf(qType));
		questionResult.setQLevel(Integer.valueOf(3));
		questionResult.setQStatus(Integer.valueOf(1));
		questionResult.setQContent(questionEntity.getQuestionTitle());
		return questionResult;
	}
}
