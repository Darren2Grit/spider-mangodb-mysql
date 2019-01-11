package cn.xueyuetang.questionspider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


import cn.xueyuetang.questionspider.controller.Main;

/**
 * Hello world!
 *
 */
public class App {
	private static final Logger logger = LogManager.getLogger(App.class);
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
		Main main = (Main) context.getBean("main");
		logger.debug("-------开始解析-----");
		/*main.parseTofelReading();
		main.parseIeltsReading();
		main.parseTofelWriting();
		main.parseIetlsWriting();
		main.parseTofelListening();
		main.parseIetlsListening();
		main.parseTofelSpoking();
		main.parseIetlsSpoking();
		main.parseGreBlank();
		main.parseGreMath();
		main.parseGreWriting();
		main.parseGreReading();
		main.parseGreWord();
		main.parseSatGrammar();
		main.parseSatMath();
		main.parseSatReading();
		main.parseSATWord();
		main.parseSatWriting();*/
		main.parseGmatWriting();
		main.parseGmatIR();
		main.parseGmatMath();
		main.parseGmatReading();
		main.parseGmatCR();
		main.parseGmatSc();
		main.parseGmatWord();
		logger.debug("-------解析完成-----");
	}
}
