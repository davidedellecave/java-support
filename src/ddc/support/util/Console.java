package ddc.support.util;

import org.apache.commons.lang3.StringUtils;

public class Console {
	private static int INDENT = 4;
	final static private int MAX_LINE = 160;

	public static void title(String message) {
		p(message, 0);
	}

	public static void message(String message) {
		p(message, 1);
	}

	public static void details(String message) {
		p(message, 2);
	}

	public static void error(String message, Throwable t) {
		p(message, 1);
		doSeparator('>',2);
		p("ERROR [" + t.getMessage() + "]", 2);
		doSeparator('<',2);
	}

	private static void p(String message, int indent) {
		if (indent == 0) {
			doSeparator('-',0);
			doPrint(message, 0);
			doSeparator('-',0);
		} else {
			doPrint(message, INDENT * indent);
		}
	}
	
	private static void doSeparator(char ch, int indent) {
		doPrint(StringUtils.repeat(ch, MAX_LINE-indent*INDENT), indent*INDENT);
	}
	
	private static void doPrint(String line, int spaces) {
		if (spaces + line.length() > MAX_LINE) {
			String[] toks = StringUtil.splitByChunk(line, MAX_LINE-spaces);
			for (int i = 0; i < toks.length; i++) {
				System.out.print("|");
				System.out.println(StringUtils.repeat(' ', spaces) + toks[i].trim());
			}
		} else {
			System.out.print("|");
			System.out.println(StringUtils.repeat(' ', spaces) + line);
		}
	}
}
