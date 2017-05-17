package sys.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Util {

	private static final Logger log = LoggerFactory.getLogger(Util.class);

	/**
	 * 取得UUID
	 * 
	 * @return
	 */
	public static String getUUID() {
		return getUUID(32);
	}

	/**
	 * 取得UUID
	 * 
	 * @param len
	 * @return
	 */
	public static String getUUID(int len) {
		String result = StringUtil.trim(UUID.randomUUID()).replaceAll("\\W", "");
		if (result.length() > len) {
			return result.substring(0, len);
		}
		return result;
	}

	/**
	 * 判斷是否為空
	 * 
	 * @param str
	 */
	public static boolean isEmpty(Object input) {
		return "".equals(StringUtil.zip(input));
	}

	/**
	 * 判斷是否不為空
	 * 
	 * @param str
	 */
	public static boolean isNotEmpty(Object input) {
		return !isEmpty(input);
	}

	/**
	 * 檢查二者是否相同
	 * 
	 * @param Object
	 * @param Object
	 */
	public static boolean equals(Object a, Object b) {
		return equals(a, b, true);
	}

	/**
	 * 檢查二者是否相同
	 * 
	 * @param Object
	 * @param Object
	 * @param boolean
	 *            不分大小寫
	 */
	public static boolean equals(Object a, Object b, boolean c) {
		if (c) {
			return StringUtil.trim(a).equalsIgnoreCase(StringUtil.trim(b));
		} else {
			return StringUtil.trim(a).equals(StringUtil.trim(b));
		}
	}

	/**
	 * 檢查二者是否不相同
	 * 
	 * @param Object
	 * @param Object
	 * @param boolean
	 *            不分大小寫
	 */
	public static boolean notEquals(Object a, Object b) {
		return !equals(a, b);
	}

	/**
	 * 檢查二者是否不相同
	 * 
	 * @param Object
	 * @param Object
	 * @param boolean
	 *            不分大小寫
	 */
	public static boolean notEquals(Object a, Object b, boolean c) {
		return !equals(a, b, c);
	}

	/**
	 * 將Map的值置入Bean
	 * 
	 * @param map
	 * @param bean
	 * @return
	 */
	public static <T> boolean mapToBean(Map<String, ?> map, T bean) {
		boolean result = false;
		if (map != null && bean != null) {
			try {
				BeanUtils.populate(bean, map);
				result = true;
			} catch (IllegalAccessException e) {
				log.error("mapToBean IllegalAccessException: {}", e);
			} catch (InvocationTargetException e) {
				log.error("mapToBean InvocationTargetException: {}", e);
			}
		}
		return result;
	}
}
