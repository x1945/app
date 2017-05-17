package sys.util;

import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.context.ContextLoader;

/**
 * 統一存取Spring的ApplicationContext<br/>
 * 避免自行操作ApplicatoinContext，可用來取得Spring管理的所有Bean
 * 
 * @author JC Software Inc.
 */
@SuppressWarnings("unchecked")
public abstract class SpringUtil {

	private static ApplicationContext appContext;
	private static final Stack<TransactionStatus> TX_STACK = new Stack<TransactionStatus>();
	private static final TransactionDefinition DEFAULT_DEFINITION = new DefaultTransactionDefinition();
	private static PlatformTransactionManager txManager;

	private static final Logger log = LoggerFactory.getLogger(SpringUtil.class);

	/**
	 * 設定目前使用的Application Context
	 */
	public static void setApplicationContext(ApplicationContext context) {
		appContext = context;
	}

	/**
	 * 取得Spring的ApplicationContext
	 * 
	 * @return ApplicationContext
	 */
	public static ApplicationContext getApplicationContext() {
		if (appContext == null) {
			appContext = ContextLoader.getCurrentWebApplicationContext();
		}
		if (appContext == null) {
			throw new RuntimeException("Spring not loaded.");
		}
		return appContext;
	}

	/**
	 * 取得Spring管理的Bean
	 * 
	 * @param name
	 *            beanName
	 * @param type
	 *            beanType
	 * @return managed bean
	 */
	public static <T> T get(String name, Class<T> type) {
		return getApplicationContext().getBean(name, type);
	}

	/**
	 * 取得Spring管理的Bean
	 * 
	 * @param name
	 *            beanName
	 * @return managed bean
	 */
	public static <T> T get(String name) {
		return (T) getApplicationContext().getBean(name);
	}

	/**
	 * 取得Spring管理的Bean
	 * 
	 * @return managed bean
	 */
	public static <T> T get(Class<T> type) {
		ApplicationContext ctx = getApplicationContext();
		String[] names = ctx.getBeanNamesForType(type);
		if (names.length > 0) {
			return (T) ctx.getBean(names[0]);
		}
		return null;
	}

	/**
	 * 注入資源
	 * 
	 * @param obj
	 */
	public static void inject(Object obj) {
		getApplicationContext().getAutowireCapableBeanFactory().autowireBean(
				obj);
	}

	/* **************************與交易相關的功能************************** */
	protected static PlatformTransactionManager getTxManager() {
		if (txManager == null) {
			txManager = get(PlatformTransactionManager.class);
		}
		if (txManager == null) {
			throw new RuntimeException("找不到交易管理員。");
		}
		return txManager;
	}

	/** 開始一個新交易 **/
	public static void begin() {
		TX_STACK.push(getTxManager().getTransaction(DEFAULT_DEFINITION));
	}

	/** 送出目前交易 **/
	public static void commit() {
		if (TX_STACK.size() == 0) {
			throw new RuntimeException("沒有正在執行的交易。");
		}
		getTxManager().commit(TX_STACK.pop());
	}

	/** 回覆目前交易 **/
	public static void rollback() {
		if (TX_STACK.size() == 0) {
			throw new RuntimeException("沒有正在執行的交易。");
		}
		getTxManager().rollback(TX_STACK.pop());
	}

	/** 顯示所有的註冊的Bean **/
	public static void showAllBeanName() {
		String[] all = getApplicationContext().getBeanDefinitionNames();
		for (String n : all) {
			log.info("bean[{}]", n);
		}
	}
}
