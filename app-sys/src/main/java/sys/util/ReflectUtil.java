package sys.util;

import java.lang.reflect.Field;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 反射工具類
 * 
 * @author Fantasy
 *
 */
public class ReflectUtil {

	private static final Logger LOG = LoggerFactory
			.getLogger(ReflectUtil.class);

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
		Field field = ReflectUtil.getField(obj, fieldName);
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
		for (Class<?> clazz = obj.getClass(); clazz != Object.class; clazz = clazz
				.getSuperclass()) {
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
	public static void setFieldValue(Object obj, String fieldName,
			String fieldValue) {
		Field field = ReflectUtil.getField(obj, fieldName);
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
