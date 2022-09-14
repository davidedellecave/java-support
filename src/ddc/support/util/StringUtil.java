package ddc.support.util;

import java.nio.charset.Charset;

public class StringUtil {
	// private static final int INDEX_NOT_FOUND=-1;

	public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
	public static final Charset UTF_8 = Charset.forName("UTF-8");
	public static final Charset UTF_16 = Charset.forName("UTF-16");
	public static final Charset ASCII = Charset.forName("ASCII");

	public static String fromUTF8toISO88591(String source) {
		return toCharset(source, UTF_8, ISO_8859_1);
	}

	public static String toCharset(String source, Charset fromCharset, Charset toCharset) {
		return new String(source.getBytes(fromCharset), toCharset);
	}

	public static String left(final String str, final int len) {
		if (str == null) {
			return null;
		}
		if (len < 0) {
			return "";
		}
		if (str.length() <= len) {
			return str;
		}
		return str.substring(0, len);
	}

	public static boolean isEmpty(final CharSequence cs) {
		return cs == null || cs.length() == 0;
	}

	public static boolean isBlank(String s) {
		return s == null || s.length() == 0 || s.trim().length() == 0;
	}

	public static StringBuilder removeEnd(final StringBuilder str, final char remove) {
		if (str == null || str.length() == 0)
			return str;
		int l = str.length() - 1;
		int size = 0;
		while (l >= 0 && str.charAt(l) == remove) {
			size++;
			l--;
		}
		if (size > 0)
			return str.delete(str.length() - size, str.length());
		return str;
	}

	public static StringBuilder removeChar(final StringBuilder str, final char remove) {
		if (str == null || str.length() == 0)
			return str;

		StringBuilder out = new StringBuilder(str.length());
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			if (ch != remove)
				out.append(ch);
		}
		return out;
	}

	public static String noAsciiCharsToUnicode(String s) {
		StringBuilder b = new StringBuilder();
		for (char c : s.toCharArray()) {
			if (c > 128) {
				b.append("\\u").append(Integer.toHexString(c));
			} else {
				b.append(c);
			}
		}
		return b.toString();
	}

	// &#xhhhh;
	public static String noAsciiCharsToXml(String s) {
		StringBuilder b = new StringBuilder();
		for (char c : s.toCharArray()) {
			if (c > 128 || c == '>' || c == '<' || c == '&' || c == '"' || c == '\\') {
				b.append("&#x").append(Integer.toHexString(c));
				b.append(";");
			} else {
				b.append(c);
			}
		}
		return b.toString();
	}

	// Remove all chars minus or equal than ASCII (decimal) 31
	public static String removeNonPrintable(final CharSequence cs) {
		if (cs == null || cs.length() == 0)
			return "";
		StringBuilder out = new StringBuilder(cs.length());
		for (int i = 0; i < cs.length(); i++) {
			char ch = cs.charAt(i);
			if (ch > 31)
				out.append(ch);
		}
		return out.toString();
	}

	public static String removeChar(final CharSequence cs, int asciiStart, int asciiEnd) {
		if (cs == null || cs.length() == 0)
			return "";
		StringBuilder out = new StringBuilder(cs.length());
		for (int i = 0; i < cs.length(); i++) {
			char ch = cs.charAt(i);
			if (ch >= asciiStart && ch<=asciiEnd)
				out.append(ch);
		}
		return out.toString();
	}
	
	public static String[] splitByChunk(String s, int chunkSize) {
		int chunkCount = (s.length() / chunkSize) + (s.length() % chunkSize == 0 ? 0 : 1);
		String[] returnVal = new String[chunkCount];
		for (int i = 0; i < chunkCount; i++) {
			returnVal[i] = s.substring(i * chunkSize, Math.min((i + 1) * (chunkSize), s.length()));
		}
		return returnVal;
	}
	
	//Match string using wildcards ? (any 1 char) and * (any chars)
	public static boolean wildcardsMatches(String text, String glob) {
	    String rest = null;
	    int pos = glob.indexOf('*');
	    if (pos != -1) {
	        rest = glob.substring(pos + 1);
	        glob = glob.substring(0, pos);
	    }

	    if (glob.length() > text.length())
	        return false;

	    // handle the part up to the first *
	    for (int i = 0; i < glob.length(); i++)
	        if (glob.charAt(i) != '?' 
	                && !glob.substring(i, i + 1).equalsIgnoreCase(text.substring(i, i + 1)))
	            return false;

	    // recurse for the part after the first *, if any
	    if (rest == null) {
	        return glob.length() == text.length();
	    } else {
	        for (int i = glob.length(); i <= text.length(); i++) {
	            if (wildcardsMatches(text.substring(i), rest))
	                return true;
	        }
	        return false;
	    }
	}
}
