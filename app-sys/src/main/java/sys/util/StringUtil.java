package sys.util;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * StringUtil
 * 
 * @author Fantasy
 *
 */
public final class StringUtil {

	public static final Pattern unicodePattern = Pattern.compile("[\\000]*");

	public static final String specialString = new String(new byte[] { -17, -69, -65 });

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

	/**
	 * 處理Unicode<br/>
	 * An invalid XML character (Unicode: 0x0) was found in the element content
	 * of the document.
	 * 
	 * @param value
	 * @return
	 */
	public static String processUnicode(String value) {
		if (value != null) {
			Matcher matcher = unicodePattern.matcher(value);
			if (matcher.find())
				return matcher.replaceAll("");
		}
		return value;
	}

	/**
	 * 處理Unicode<br/>
	 * An invalid XML character (Unicode: 0x1a) was found in the element content
	 * of the document.
	 * 
	 * @param value
	 * @return
	 */
	public static String processUnicode2(String in) {
		if (in == null || ("".equals(in)))
			return in;
		StringBuffer out = new StringBuffer(in);
		for (int i = 0; i < out.length(); i++) {
			// 0xC2 0xA0
			if (out.charAt(i) == 0x1a) {
				// out.setCharAt(i, '-');
				out.setCharAt(i, '\0');
			}
		}
		return out.toString();
	}

	public static String processSpecial(String value) {
		if (value != null)
			return value.replaceAll(specialString, "");
		return value;
	}

	/**
	 * 比較二段文字的差異
	 * 
	 * @param in1
	 * @param in2
	 */
	public static void comparing(String in1, String in2) {
		if (in1 != null && in2 != null) {
			StringBuilder t11 = new StringBuilder();
			StringBuilder t12 = new StringBuilder();
			StringBuilder t21 = new StringBuilder();
			StringBuilder t22 = new StringBuilder();
			StringBuilder t31 = new StringBuilder();
			StringBuilder t32 = new StringBuilder();
			byte[] b1 = in1.getBytes();
			byte[] b2 = in2.getBytes();
			StringBuffer out = new StringBuffer(in2);

			int len = b1.length > b2.length ? b2.length : b1.length;
			for (int i = 0; i < len; i++) {
				byte c1 = b1[i];
				byte c2 = b2[i];
				t31.append(c1);
				t32.append(c2);
				if (c1 != c2) {
					System.out.println(i);
					t12.append(c1);
					t22.append(c2);
				} else {
					t11.append(c1);
					t21.append(c2);
				}
				if (i == 68) {
					// 0xFE 0xFF
					char c3 = out.charAt(i);
					System.out.println(">>>[" + in2.charAt(i) + "]");
					System.out.println(c3 == 0xFE);
					System.out.println(c3 == 0xFF);
					System.out.println(c3 == 0x00);
					System.out.println(c3 == '\0');
					System.out.println(b2[i]);
					byte[] bs = { -17, -69, -65 };
					System.out.println(new String(bs));
				}
			}
			System.out.println("t11:" + t11.toString());
			System.out.println("t21:" + t21.toString());
			System.out.println("t12:" + t12.toString());
			System.out.println("t22:" + t22.toString());
			System.out.println("t31:" + t31.toString());
			System.out.println("t32:" + t32.toString());
			// System.out.println("in1>68:" + in1.charAt(68));
			// System.out.println("in2>68:" + in2.charAt(68));

			// int len = in1.length() > in2.length() ? in2.length() : in1.length();
			// for (int i = 0; i < len; i++) {
			// char c1 = in1.charAt(i);
			// char c2 = in2.charAt(i);
			// t31.append(c1);
			// t32.append(c2);
			// if (c1 != c2) {
			// t12.append(c1);
			// t22.append(c2);
			// } else {
			// t11.append(c1);
			// t21.append(c2);
			// }
			// }
			// System.out.println("t11:" + t11.toString());
			// System.out.println("t21:" + t21.toString());
			// System.out.println("t12:" + t12.toString());
			// System.out.println("t22:" + t22.toString());
			// System.out.println("t31:" + t31.toString());
			// System.out.println("t32:" + t32.toString());
			// System.out.println("in1>68:" + in1.charAt(68));
			// System.out.println("in2>68:" + in2.charAt(68));
		}
	}
}
