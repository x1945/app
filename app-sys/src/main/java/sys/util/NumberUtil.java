package sys.util;

/**
 * NumberUtil
 * 
 * @author Fantasy
 *
 */
public final class NumberUtil {

	/**
	 * 判斷是否為整數
	 * 
	 * @param input
	 * @return
	 */
	public static boolean isInteger(Object input) {
		if (Util.isNotEmpty(input)) {
			return StringUtil.trim(input).matches("[0-9]*");
		}
		return false;
		// Pattern pattern = Pattern.compile("[0-9]*");
		// return pattern.matcher(str).matches();
	}

	/**
	 * 解析整數
	 * 
	 * @param input
	 * @return
	 */
	public static int parseInt(Object input) {
		return parseInt(input, 0);
	}

	/**
	 * 解析整數
	 * 
	 * @param input
	 * @return
	 */
	public static int parseInt(Object input, int defaulValue) {
		if (isInteger(input)) {
			return Integer.parseInt(StringUtil.trim(input));
		}
		return defaulValue;
	}

	/**
	 * 區間亂數
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static int random(Integer start, Integer end) {
		if (start <= end) {
			return (int) Math.floor(Math.random()
					* new Integer(end - start + 1).doubleValue())
					+ start;
		}
		return 0;
	}
}
