package fantasy.ibatis.util;

import java.lang.reflect.Field;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SimpleUtil
 * 
 * @author Fantasy
 *
 */
public final class SimpleUtil {

	private static final Logger LOG = LoggerFactory.getLogger(SimpleUtil.class);

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

	/**
	 * 判斷是否為空
	 * 
	 * @param str
	 */
	public static boolean isEmpty(Object input) {
		return "".equals(zip(input));
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
			return trim(a).equalsIgnoreCase(trim(b));
		} else {
			return trim(a).equals(trim(b));
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
	 * 利用反射獲取指定對象的指定屬性
	 * 
	 * @param obj
	 *            目標對象
	 * @param fieldName
	 *            目標屬性
	 * @return 目標屬性的值
	 */
	public static Object getFieldValue(Object obj, String fieldName) {
		Object result = null;
		Field field = getField(obj, fieldName);
		if (field != null) {
			field.setAccessible(true);
			try {
				result = field.get(obj);
			} catch (IllegalArgumentException e) {
				LOG.error("IllegalArgumentException:{}", e);
			} catch (IllegalAccessException e) {
				LOG.error("IllegalAccessException:{}", e);
			}
		}
		return result;
	}

	/**
	 * 利用反射獲取指定對象裡面的指定屬性
	 * 
	 * @param obj
	 *            目標對象
	 * @param fieldName
	 *            目標屬性
	 * @return 目標字段
	 */
	private static Field getField(Object obj, String fieldName) {
		Field field = null;
		for (Class<?> clazz = obj.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
			try {
				field = clazz.getDeclaredField(fieldName);
				break;
			} catch (NoSuchFieldException e) {
				// 這裡不用做處理，子類沒有該字段可能對應的父類有，都沒有就返回null。
			}
		}
		return field;
	}

	/**
	 * 利用反射設置指定對象的指定屬性為指定的值
	 * 
	 * @param obj
	 *            目標對象
	 * @param fieldName
	 *            目標屬性
	 * @param fieldValue
	 *            目標值
	 */
	public static void setFieldValue(Object obj, String fieldName, String fieldValue) {
		Field field = getField(obj, fieldName);
		if (field != null) {
			try {
				field.setAccessible(true);
				field.set(obj, fieldValue);
			} catch (IllegalArgumentException e) {
				LOG.error("IllegalArgumentException:{}", e);
			} catch (IllegalAccessException e) {
				LOG.error("IllegalAccessException:{}", e);
			}
		}
	}

	public static Field[] getAllFields(Class<?> clazz) {
		return getAllFields(clazz, null);
	}

	public static Field[] getAllFields(Class<?> startClass, Class<?> endClass) {
		Field[] result = null;
		boolean next = true;
		if (startClass != null) {
			result = startClass.getDeclaredFields();
			Class<?> superClass = startClass.getSuperclass();
			while (superClass != null && next) {
				result = ArrayUtils.addAll(result, superClass.getDeclaredFields());

				if (superClass == endClass)
					next = false;

				superClass = superClass.getSuperclass();
			}
		}
		return result;
	}
}
