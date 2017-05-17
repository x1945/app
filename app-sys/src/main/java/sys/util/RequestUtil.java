package sys.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import sys.constant.SysConfig;

/**
 * RequestUtil
 * 
 * @author Fantasy
 * 
 */
public final class RequestUtil {

	private static final Logger LOG = LoggerFactory.getLogger(RequestUtil.class);

	/**
	 * get Request
	 * 
	 * @return
	 */
	public static HttpServletRequest getRequest() {
		HttpServletRequest request = null;
		try {
			request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		} catch (Exception e) {
			LOG.warn("getRequest is not found");
		}
		return request;
	}

	/**
	 * isGet
	 * 
	 * @return
	 */
	public static boolean isGet() {
		return isGet(getRequest());
	}

	/**
	 * isGet
	 * 
	 * @return
	 */
	public static boolean isGet(HttpServletRequest request) {
		if (request != null) {
			return "GET".equalsIgnoreCase(request.getMethod());
		}
		return false;
	}

	/**
	 * 轉換 request params map
	 * 
	 * @return
	 */
	public static Map<String, String> transformParams(Map<String, String[]> requestMap) {
		Map<String, String> result = new HashMap<String, String>();
		if (requestMap != null) {
			Set<String> keys = requestMap.keySet();
			for (String key : keys) {
				result.put(key, getValue(requestMap.get(key)));
			}
		}
		return result;
	}

	/**
	 * 取得request 參數
	 * 
	 * @return
	 */
	public static Map<String, String> getParams() {
		HttpServletRequest request = getRequest();
		if (request != null)
			return transformParams(request.getParameterMap());
		return new HashMap<String, String>();
	}

	/**
	 * get Value
	 * 
	 * @return
	 */
	public static String getValue(String[] values) {
		if (values != null) {
			if (values.length > 0)
				return StringUtil.trim(values[0]);
		}
		return null;
	}

	/**
	 * getValue
	 * 
	 * @param request
	 * @param key
	 * @return
	 */
	public static String getValue(HttpServletRequest request, String key) {
		if (request != null) {
			String[] s = request.getParameterValues(key);
			return getValue(s);
		}
		return null;
	}

	/**
	 * 判斷是否為ajax請求
	 * 
	 * @param request
	 * @return true為ajax語法，false為其它。
	 */
	public static boolean isAjax() {
		return isAjax(getRequest());
	}

	/**
	 * 判斷是否為ajax請求
	 * 
	 * @param request
	 * @return true為ajax語法，false為其它。
	 */
	public static boolean isAjax(HttpServletRequest request) {
		if (request != null) {
			String requestType = request.getHeader("X-Requested-With");
			// LOG.debug("requestType[{}]", requestType);
			if (requestType != null && "XMLHttpRequest".equalsIgnoreCase(requestType)) {
				return true;
			}
			// LOG.debug("request.getParameter(downloadMode)[{}]",
			// request.getParameter("downloadMode"));
			// if (Util.parseBoolean(request.getParameter("downloadMode"))) {
			// return true;
			// }

		}
		return false;
	}

	/**
	 * 取得IP
	 * 
	 * @return
	 */
	public static String getClientAddr() {
		return getClientAddr(getRequest());
	}

	/**
	 * 取得IP
	 * 
	 * @return
	 */
	public static String getClientAddr(HttpServletRequest request) {
		if (request != null) {
			return request.getRemoteAddr();
		}
		return SysConfig.Empty;
	}

	/**
	 * 取得IP
	 * 
	 * @return
	 */
	public static String getClientHost(HttpServletRequest request) {
		if (request != null) {
			return request.getRemoteHost();
		}
		return SysConfig.Empty;
	}
}
