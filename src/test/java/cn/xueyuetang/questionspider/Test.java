package cn.xueyuetang.questionspider;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Test {

	public static void main(String[] args) {
		String content = "<com.exam.model.paper.QuestionMultipleChoice>\r\n" + 
				"  <id>e9ceefd6-3d5b-46b5-b100-a646f9c9db1d</id>\r\n" + 
				"  <type>2</type>\r\n" + 
				"  <content>&lt;p&gt;&lt;strong&gt;13&lt;/strong&gt;&lt;/p&gt;&lt;br/&gt;&lt;p&gt; For the following question, consider each of the choices separately and select all that apply.&lt;/p&gt;&lt;br/&gt;&lt;p&gt;The passage mentions which of the following as being a characteristic of seasonal ice?\r\n" + 
				"&#x2; &lt;/p&gt;</content>\r\n" + 
				"  <key>A C</key>\r\n" + 
				"  <score>0</score>\r\n" + 
				"  <ext>&lt;p&gt;&lt;strong&gt;Choice A and C&lt;/strong&gt; are correct.\r\n" + 
				"Choice A is correct: the passage states that “to the untrained eye, all sea ice looks similar” (line 2-3).\r\n" + 
				"Choice B is incorrect: it is clear that perennial ice contains fine, veinlike channels, but the passage does not mention whether seasonal ice contains them.\r\n" + 
				"Choice C is correct: in lines 6-8, the passage establishes that first-year ice tastes salty but eventually gets fresher if the ice survives.&lt;/p&gt;</ext>\r\n" + 
				"  <options>\r\n" + 
				"    <com.exam.model.paper.Option>\r\n" + 
				"      <alisa>A</alisa>\r\n" + 
				"      <text>It is similar in appearance to perennial ice.</text>\r\n" + 
				"    </com.exam.model.paper.Option>\r\n" + 
				"    <com.exam.model.paper.Option>\r\n" + 
				"      <alisa>B</alisa>\r\n" + 
				"      <text>It is typically filled with fine, veinlike channels.</text>\r\n" + 
				"    </com.exam.model.paper.Option>\r\n" + 
				"    <com.exam.model.paper.Option>\r\n" + 
				"      <alisa>C</alisa>\r\n" + 
				"      <text>It tastes saltier than perennial ice.</text>\r\n" + 
				"    </com.exam.model.paper.Option>\r\n" + 
				"  </options>\r\n" + 
				"</com.exam.model.paper.QuestionMultipleChoice>";
		String s = content.replaceAll("&#x2;", "");
		System.out.println(s);
	}

}
