package sys.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

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
			return StringUtil.trim(input).matches("^(\\+|-)?\\d+$");
		}
		return false;
		// Pattern pattern = Pattern.compile("[0-9]*");
		// return pattern.matcher(str).matches();
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
			return (int) Math.floor(Math.random() * new Integer(end - start + 1).doubleValue()) + start;
		}
		return 0;
	}

	/**
	 * 檢查是不是數字
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isNumeric(Object value) {
		if (value == null) {
			return false;
		} else if (value instanceof Number) {
			return true;
		} else {
			String v = value.toString();
			if (v == null)
				return false;
			return v.trim().replaceAll(",", "").matches("(\\+|-)?\\d+(\\.\\d+)?");
		}
	}

	/**
	 * 功能說明：將數字欄位加上Comma(即,)。
	 * 
	 * @param input
	 * @return
	 */
	public static String addComma(Object input) {
		return addComma(input, -1);
	}

	/**
	 * 功能說明：將數字欄位加上Comma(即,)。
	 * 
	 * @param input
	 * @param showScale
	 * @return
	 */
	public static String addComma(Object input, int showScale) {
		return addComma(input, showScale, true);
	}

	/**
	 * 功能說明：將數字欄位加上Comma(即,)。.
	 * 
	 * 1.可指定小數長度,如指定length=-1,則依據實際輸入數字之小數位數來做切割.
	 * 2.假如指定的小數長度小於原小數長度,捨去部分可指定是否則採四捨五入方式處裡. EX :
	 * 4121324.4567(flo_length=-1,flag=true) => 4,121,324.4567
	 * 4121324.4567(flo_length=2,flag=true) => 4,121,324.46
	 * 4121324.4567(flo_length=2,flag=false) => 4,121,324.45
	 * 
	 * @param input
	 *            輸入數字
	 * @param showScale
	 *            指定小數長度
	 * @param flag
	 *            是否則採四捨五入方式處理
	 * @return String
	 */
	public static String addComma(Object input, int showScale, boolean flag) {
		if (!isNumeric(input))
			return "";
		try {
			StringBuffer sb = new StringBuffer(",##0");
			BigDecimal bd = parseBigDecimal(input);
			if (showScale == -1)
				showScale = bd.scale();
			if (showScale > 0) {
				sb.append(".");
				for (int i = 1; i <= showScale; i++)
					sb.append("0");
			}
			DecimalFormat dollarFormat = new DecimalFormat(sb.toString());
			return dollarFormat.format(bd.setScale(showScale, flag ? BigDecimal.ROUND_HALF_UP : BigDecimal.ROUND_DOWN));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return "";
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 傳入帶有comma的文字，去掉Comma，並傳回字串.
	 * 
	 * @param number
	 *            String 帶有comma的字串。
	 * @return String 傳出浮點數型態的數字(小數點2位，四捨五入)。
	 */
	public static String delComma(Object input) {
		if (input instanceof Double) {
			BigDecimal bd = BigDecimal.valueOf((Double) input);
			return bd.toPlainString();
		} else if (input instanceof Long) {
			BigDecimal bd = BigDecimal.valueOf((Long) input);
			return bd.toPlainString();
		} else if (isNumeric(input)) {
			return String.valueOf(input).trim().replaceAll(",", "");
		}
		return "";
	}

	/**
	 * 解析為整數
	 * 
	 * @param input
	 * @return
	 */
	public static int parseInt(Object input) {
		return parseBigDecimal(input).intValue();
	}

	/**
	 * 解析為Double
	 * 
	 * @param input
	 * @return
	 */
	public static double parseDouble(Object input) {
		return parseBigDecimal(input).doubleValue();
	}

	/**
	 * 解析為Double(四拾五入)
	 * 
	 * @param input
	 * @param scale
	 *            (小數點位置)
	 * @return
	 */
	public static double parseDouble(Object input, int scale) {
		BigDecimal result = BigDecimal.ZERO;
		if (isNumeric(input))
			result = new BigDecimal(delComma(input)).setScale(scale, RoundingMode.HALF_UP);
		return result.doubleValue();
	}

	/**
	 * 解析為長整數
	 * 
	 * @param input
	 * @return
	 */
	public static long parseLong(Object input) {
		return parseBigDecimal(input).longValue();
	}

	/**
	 * 解析為BigDecimal
	 * 
	 * @param input
	 * @return
	 */
	public static BigDecimal parseBigDecimal(Object input) {
		if (input instanceof BigDecimal) {
			return (BigDecimal) input;
		} else if (isNumeric(input)) {
			return new BigDecimal(delComma(input));
		}
		return BigDecimal.ZERO;
	}
}
