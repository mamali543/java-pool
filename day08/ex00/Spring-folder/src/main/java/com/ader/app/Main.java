package com.ader.app;

import com.ader.interfaces.Printer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
		Printer printer = context.getBean("printerWithPrefixErrToUpper", Printer.class);
		printer.displayMessage("Hello!");
		printer = context.getBean("printerWithDateTimeOutToLower", Printer.class);
		printer.displayMessage("Hello!");
	}
}
