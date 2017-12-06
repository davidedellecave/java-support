package ddc.support.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtil {

	public static String getMsg(Throwable e) {
		String info = "";
		if (e != null) {
			info = e.getClass().getSimpleName();
			if (e.getMessage() != null && e.getMessage().length() > 0)
				info += " " + e.getMessage();
			else if (e.getCause() != null && e.getCause().getMessage() != null)
				info += " " + e.getCause().getMessage();
		} else {
			info = "unknown error";
		}
		return info;
	}

	public static String getStackTrace(Throwable e) {		
        final StringWriter writer = new StringWriter();
        final PrintWriter pw = new PrintWriter(writer, true);
        e.printStackTrace(pw);
        return writer.getBuffer().toString();
	}
}
