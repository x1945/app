package sys.util;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Properties;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 取得system.properties
 * 
 * @author ice
 * 
 */
public class PropUtil {

	private static final Logger LOG = LoggerFactory.getLogger(PropUtil.class);

	private static Properties properties = new Properties();

	private static String environment;

	private static String path;

	private static String org_uuid;

	/**
	 * 取得目前系統使用模式
	 * 
	 * @return
	 */
	public static String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String env) {
		environment = env;
	}

	/**
	 * 初始路徑
	 * 
	 * @return
	 */
	public static String getPath() {
		return StringUtil.trim(path);
	}

	public void setPath(String value) {
		path = value;
	}

	/**
	 * 判斷系統使用模式
	 * 
	 * @param env
	 * @return
	 */
	public static boolean isEnvironment(String env) {
		return StringUtil.trim(env).equals(environment);
	}

	/**
	 * 
	 * @param env
	 * @return
	 */
	public static boolean isNotEnvironment(String env) {
		return !isEnvironment(env);
	}

	/**
	 * 判斷是否為正式環境
	 */
	public static boolean isProd() {
		return isEnvironment("prod");
	}

	/**
	 * 初始化
	 */
	public void init() {
		LOG.info("=====================================================================");
		ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);

		StringBuilder sb = new StringBuilder();
		sb.append(getPath()); // for EJB
		sb.append("properties/system-").append(getEnvironment()).append(".properties");
		LOG.info("初始化PropUtil[{}] Environment[{}]", sb.toString(), getEnvironment());

		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(sb.toString());
		try {
			properties.load(is);
		} catch (IOException e) {
			LOG.error(e.toString());
		}

		LOG.info("=====================================================================");
	}

	/**
	 * getProperty
	 * 
	 * @param key
	 * @return
	 */
	public static String getProperty(String key) {
		return PropUtil.getProperty(key, "");
	}

	/**
	 * getProperty
	 * 
	 * @param key
	 * @param dfValue
	 * @return
	 */
	public static String getProperty(String key, String dfValue) {
		return properties.getProperty(key, dfValue);
	}

	/**
	 * getProperties
	 * 
	 * @param propPath
	 * @return
	 */
	public static Properties getProperties(String location) {
		Properties result = new Properties();
		InputStream is = null;
		try {
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream(location);
			result.load(is);
		} catch (IOException e) {
			LOG.error(e.toString());
		} finally {
			try {
				if (is != null) {
					is.close();
					is = null;
				}
			} catch (IOException e) {
				LOG.error(e.toString());
			}
		}
		return result;
	}

	public static String getOrg_uuid() {
		return org_uuid;
	}

	public static void setOrg_uuid(String org_uuid) {
		PropUtil.org_uuid = org_uuid;
	}

}
