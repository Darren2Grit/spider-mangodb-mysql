package cn.xueyuetang.questionspider.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;



public class CodeGenerator {

	 private CodeGenerator() {
	 }
	
	/***
	 * 功能：生成编号
	 * @param CodeGEnum cenum 通用字段属性
	 * @return 编号
	 * */
	public static String getNextCode(CodeGEnum cenum){
		// 把初始编号存在表里，考虑在redis读取的序号？?
		String nextCode = null ; 
		try {
				Calendar calendar = new GregorianCalendar();
	
				String year = "" + calendar.get(GregorianCalendar.YEAR);
	
				String month = "" + (calendar.get(GregorianCalendar.MONTH) + 1);
				if (month.length() == 1) {
					month = "0" + month;
				}
	
				String day = "" + calendar.get(GregorianCalendar.DAY_OF_MONTH);
				if (day.length() == 1) {
					day = "0" + day;
				}
	
				String hour = "" + calendar.get(GregorianCalendar.HOUR_OF_DAY);
				if (hour.length() == 1) {
					hour = "0" + hour;
				}
				String minute = "" + calendar.get(GregorianCalendar.MINUTE);
				if (minute.length() == 1) {
					minute = "0" + minute;
				}
				String second = "" + calendar.get(GregorianCalendar.SECOND);
	
				if (second.length() == 1) {
					second = "0" + second;
				}
				nextCode = cenum+year.substring(2, 4) + month + day + hour + minute + second;
		} catch(Exception ex) {
			
		} finally {
			
		}
		return nextCode;
	}

	public static void main(String[]args) {

		//CodeGenerator code = new CodeGenerator();
		//System.out.println("订单编号生成："+code.getNextCode(CodeGEnum.PL));

	}

}
