package cn.xueyuetang.questionspider.utils;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {
	public static String parseParagraph(String orgStr) {
		if (StringUtils.isNotEmpty(orgStr)) {
			return "<p>" + orgStr.trim() + "</p>";
		}
		return "";
	} 
}
