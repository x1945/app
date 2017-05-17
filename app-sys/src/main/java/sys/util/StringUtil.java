package sys.util;

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
		} else {
			return input.toString().trim();
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
