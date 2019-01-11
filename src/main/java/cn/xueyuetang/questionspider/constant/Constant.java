package cn.xueyuetang.questionspider.constant;

import java.util.HashMap;
import java.util.Map;

public class Constant {
	public final static Map<String, String> SUBJECTMAP = new HashMap<String, String>();;

	static {
		SUBJECTMAP.put("社会学", "社会科学");
		SUBJECTMAP.put("心理学", "社会科学");
		SUBJECTMAP.put("历史", "社会科学");
		SUBJECTMAP.put("考古学", "社会科学");
		SUBJECTMAP.put("人类学", "社会科学");
		SUBJECTMAP.put("人口统计学", "社会科学");
		SUBJECTMAP.put("商业", "社会科学");
		SUBJECTMAP.put("地理学", "自然科学");
		SUBJECTMAP.put("生态学", "自然科学");
		SUBJECTMAP.put("天文学", "自然科学");
		SUBJECTMAP.put("气象学", "自然科学");
		SUBJECTMAP.put("农业", "自然科学");
		SUBJECTMAP.put("化学", "自然科学");
		SUBJECTMAP.put("植物学", "生命科学");
		SUBJECTMAP.put("生物学", "生命科学");
		SUBJECTMAP.put("艺术", "文化艺术");
	}
}
