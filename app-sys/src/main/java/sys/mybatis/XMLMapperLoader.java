package sys.mybatis;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.ibatis.builder.annotation.MapperAnnotationBuilder;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.NestedIOException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ClassUtils;

/**
 * Mybatis SQL Scanner
 * 
 * 監聽mappers/*.xml是否異動,有異動時自動重新載入 (修改SQL無需重啟Server)
 * 
 * @author Fantasy
 * 
 *         2014/09/22 調整為只reload修改的檔案,加快重新載入的速度
 */
public class XMLMapperLoader implements DisposableBean, InitializingBean, ApplicationContextAware {

	private static final Logger LOG = LoggerFactory.getLogger(XMLMapperLoader.class);

	private static StringBuffer workRoot = new StringBuffer();

	private ConfigurableApplicationContext context = null;
	@SuppressWarnings("unused")
	private transient String basePackage = null;
	private HashMap<String, String> fileMapping = new HashMap<String, String>();
	private Scanner scanner = null;
	private ScheduledExecutorService service = null;
	private Set<String> changeSet = new HashSet<String>();

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = (ConfigurableApplicationContext) applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			service = Executors.newScheduledThreadPool(1);
			// 獲取xml所在包
			MapperScannerConfigurer config = context.getBean(MapperScannerConfigurer.class);
			Field field = config.getClass().getDeclaredField("basePackage");
			field.setAccessible(true);
			basePackage = (String) field.get(config);
			// 觸發文件監聽事件
			scanner = new Scanner();
			scanner.scan();

			// 每3秒scan 一次
			service.scheduleAtFixedRate(new Task(), 3, 3, TimeUnit.SECONDS);

		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	class Task implements Runnable {

		@Override
		public void run() {
			try {
				if (scanner.isChanged()) {
					LOG.info("================== mappers/*.xml文件改變,重新載入! ==============");
					scanner.reloadXML();
					LOG.info("================== 重新載入完成 ! ==========================");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@SuppressWarnings({ "rawtypes" })
	class Scanner {

		private String[] basePackages;
		// private static final String XML_RESOURCE_PATTERN = "**/*.xml";
		private static final String XML_RESOURCE_PATTERN = "mappers/*.xml";
		// private static final String XML_RESOURCE_PATTERN = "META-INF/mappers/*.xml";
		private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

		public Scanner() {
			basePackages = new String[] { "" };
			// basePackages =
			// StringUtils.tokenizeToStringArray(XMLMapperLoader.this.basePackage,
			// ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
			LOG.info("===================================================");
			LOG.info("=============== Mybatis SQL Scanner init ===================");
			LOG.info("===================================================");
		}

		public Resource[] getResource(String basePackage, String pattern) throws IOException {
			// WEB
			String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
					+ ClassUtils.convertClassNameToResourcePath(context.getEnvironment().resolveRequiredPlaceholders(
							basePackage)) + "/" + pattern;
			// System.out.println("packageSearchPath:"+packageSearchPath);

			// EJB
			// openEJB (file:///D:/workspace/jcs2/jcs-ejb/ejbModule)
			// String packageSearchPath = getWorkRoot()
			// + ClassUtils.convertClassNameToResourcePath(context.getEnvironment().resolveRequiredPlaceholders(
			// basePackage)) + "/" + pattern;
			Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
			return resources;
		}

		public Resource getResource(String name) throws IOException {
			// EJB
			// String resourceName = getWorkRoot() + "/META-INF/mappers/" + name + ".xml";
			// WEB
			String resourceName = getWorkRoot() + "/mappers/" + name + ".xml";

			return resourcePatternResolver.getResource(resourceName);
		}

		public void reloadXML() throws Exception {
			SqlSessionFactory factory = context.getBean(SqlSessionFactory.class);
			Configuration configuration = factory.getConfiguration();
			// 移除加載項
			removeConfig(configuration);
			// 重新載入
			for (String name : changeSet) {
				Resource r = getResource(name);
				Class<?> clazz = getDaoClass(name);
				if (clazz != null) {
					try {
						// parse java annotation
						MapperAnnotationBuilder mapperAnnotationBuilder = new MapperAnnotationBuilder(
								configuration, clazz);
						mapperAnnotationBuilder.parse();

						// parse xml
						XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(r.getInputStream(),
								configuration, r.toString(), configuration.getSqlFragments());
						xmlMapperBuilder.parse();

					} catch (Exception e) {
						throw new NestedIOException("Failed to parse mapping resource: '" + r + "'",
								e);
					} finally {
						ErrorContext.instance().reset();
					}
				}
			}

			changeSet.clear();
		}

		private void removeConfig(Configuration configuration) throws Exception {
			Class<?> classConfig = configuration.getClass();
			clearMap(classConfig, configuration, "mappedStatements");
			clearMap(classConfig, configuration, "caches");
			clearMap(classConfig, configuration, "resultMaps");
			clearMap(classConfig, configuration, "parameterMaps");
			clearMap(classConfig, configuration, "keyGenerators");
			clearMap(classConfig, configuration, "sqlFragments");

			clearSet(classConfig, configuration, "loadedResources");

		}

		@SuppressWarnings("unchecked")
		private void clearMap(Class<?> classConfig, Configuration configuration, String fieldName) throws Exception {
			Set<String> removeKeys = new HashSet<String>();
			// System.out.println("clearMap fieldName:" + fieldName);
			Field field = classConfig.getDeclaredField(fieldName);
			field.setAccessible(true);
			Map mapConfig = (Map) field.get(configuration);
			Set<String> keys = mapConfig.keySet();
			for (String key : keys) {
				for (String change : changeSet) {
					if (key.indexOf(change) != -1) {
						// System.out.println("clearMap key:" + key);
						removeKeys.add(key);
					}
				}
			}

			for (String key : removeKeys) {
				mapConfig.remove(key);
			}

			// mapConfig.clear();
		}

		@SuppressWarnings("unchecked")
		private void clearSet(Class<?> classConfig, Configuration configuration, String fieldName) throws Exception {
			Set<String> removeKeys = new HashSet<String>();

			Field field = classConfig.getDeclaredField(fieldName);
			field.setAccessible(true);
			Set setConfig = (Set) field.get(configuration);
			// System.out.println("clearSet fieldName:" + fieldName);
			Set<String> keys = (Set) field.get(configuration);
			for (String key : keys) {
				for (String change : changeSet) {
					if (key.indexOf(change) != -1) {
						// System.out.println("clearSet key:" + key);
						removeKeys.add(key);
					}
				}
			}

			for (String key : removeKeys) {
				setConfig.remove(key);
			}

			// setConfig.clear();
		}

		public void scan() throws IOException {
			if (!fileMapping.isEmpty()) {
				return;
			}
			for (String basePackage : basePackages) {
				Resource[] resources = getResource(basePackage, XML_RESOURCE_PATTERN);
				if (resources != null) {
					for (Resource r : resources) {
						String multi_key = getValue(r);
						fileMapping.put(r.getFilename(), multi_key);
					}
				}
			}
		}

		private String getValue(Resource resource) throws IOException {
			String contentLength = String.valueOf((resource.contentLength()));
			String lastModified = String.valueOf((resource.lastModified()));
			return new StringBuilder(contentLength).append(lastModified).toString();
		}

		public boolean isChanged() throws IOException {
			boolean isChanged = false;
			for (String basePackage : basePackages) {
				Resource[] resources = getResource(basePackage, XML_RESOURCE_PATTERN);
				if (resources != null) {
					for (Resource r : resources) {
						String name = r.getFilename();
						String value = fileMapping.get(name);
						String multi_key = getValue(r);
						// if ("Table01Dao.xml".equals(name))
						// LOG.info("name:{} value:{} multi_key:{} Filename:{} rLen:{}", name, value, multi_key, r
						// .getFilename(), resources.length);
						if (!multi_key.equals(value)) {
							LOG.info("===================================================");
							LOG.info(">> " + name + " 已修改 (" + multi_key + ")");
							LOG.info("===================================================");
							isChanged = true;
							fileMapping.put(name, multi_key);

							changeSet.add(name.replace(".xml", ""));
						}
					}
				}
			}
			return isChanged;
		}
	}

	@Override
	public void destroy() throws Exception {
		if (service != null) {
			service.shutdownNow();
		}
	}

	/**
	 * 取得工作目錄
	 */
	public String getWorkRoot() {
		// EJB
		// if (workRoot.length() <= 0) {
		// workRoot.append("file://");
		// workRoot.append(Thread.currentThread().getContextClassLoader()
		// .getResource("").getPath().replace("/apache-openejb-4.7.1/lib", ""));
		// workRoot.append("coa-ejb/ejbModule");
		// LOG.info("workRoot:{}", workRoot);
		// }

		// WEB
		// workRoot.delete(0, workRoot.length());
		if (workRoot.length() <= 0) {
			workRoot.append("file://");
			workRoot.append(Thread.currentThread().getContextClassLoader()
					.getResource("").getPath());
			LOG.info("workRoot:{}", workRoot);
		}
		return workRoot.toString();
	}

	/**
	 * 取得Dao Class
	 * 
	 * @param className
	 * @return
	 */
	public static Class<?> getDaoClass(String className) {
		Class<?> result = null;
		try {
			result = Class.forName("app.dao." + className);
		} catch (ClassNotFoundException e) {
			LOG.error("取得不到Class [app.dao.{}]", className);
		}
		return result;
	}
}
