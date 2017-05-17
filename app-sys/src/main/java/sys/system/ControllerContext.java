package sys.system;

import java.util.HashMap;
import java.util.Map;

import sys.util.StringUtil;

public final class ControllerContext {

	public static final String SERIES = "Series";

	private static final ThreadLocal<Map<String, Object>> LOCAL = new ThreadLocal<Map<String, Object>>();

	private ControllerContext() {
	}

	public static void init() {
		LOCAL.set(new HashMap<String, Object>());
	}

	public static void cleanup() {
		LOCAL.remove();
	}

	public static Object get(String name) {
		return getLocal().get(name);
	}

	public static void put(String name, Object value) {
		getLocal().put(name, value);
	}

	private static Map<String, Object> getLocal() {
		Map<String, Object> map = LOCAL.get();
		if (map == null) {
			map = new HashMap<String, Object>();
			LOCAL.set(map);
		}
		return map;
	}

	public static void setSeries(String value) {
		getLocal().put(SERIES, value);
	}

	public static String getSeries() {
		return StringUtil.trim(get(SERIES));
	}
}
