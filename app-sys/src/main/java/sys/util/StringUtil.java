package sys.util;

import java.math.BigDecimal;

/**
 * StringUtil
 * 
 * @author Fantasy
 *
 */
public final class StringUtil {
	/**
	 * 去除空白
	 * 
	 * @param str
	 */
	public static String trim(Object input) {
		if (input == null) {
			return "";
		} else if (input instanceof Double) {
			BigDecimal bd = BigDecimal.valueOf((Double) input);
			return bd.toPlainString();
		} else if (input instanceof Long) {
			BigDecimal bd = BigDecimal.valueOf((Long) input);
			return bd.toPlainString();
		} else {
			String string = input.toString();
			char[] chars = string.toCharArray();
			int begin = 0;
			while (begin < chars.length && (chars[begin] <= ' ' || chars[begin] == '　')) {
				++begin;
			}
			int end = chars.length - 1;
			while (end > begin && (chars[end] <= ' ' || chars[end] == '　')) {
				--end;
			}
			if (begin == 0 && end == chars.length - 1) {
				return string;
			}
			return new String(chars, begin, end - begin + 1);
		}
	}

	/**
	 * 壓縮字串
	 * 
	 * @param input
	 * @return
	 */
	public static String zip(Object input) {
		return trim(input).replaceAll("[\\s\\t\\r\\n]", "");
	}

}
