package fantasy.ibatis.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.GenericTypeResolver;

import fantasy.ibatis.annotation.Table;
import fantasy.ibatis.dao.GenericDao;

/**
 * BuildUtil
 * 
 * @author Fantasy
 * 
 */
public final class BuildUtil {

	private static final Logger LOG = LoggerFactory.getLogger(BuildUtil.class);

	public final static int MAPPED_STATEMENT_INDEX = 0;
	public final static int PARAMETER_INDEX = 1;
	public final static int ROWBOUNDS_INDEX = 2;
	public final static int RESULT_HANDLER_INDEX = 3;

	private final static HashMap<String, Class<?>> classCache = new HashMap<String, Class<?>>();

	private final static XMLLanguageDriver XMLDriver = new XMLLanguageDriver();

	/**
	 * copyFromNewSql
	 * 
	 * @param ms
	 * @param boundSql
	 * @param sql
	 * @return
	 */
	public static MappedStatement copyFromNewSql(MappedStatement ms, BoundSql boundSql, String sql) {
		BoundSql newBoundSql = copyFromBoundSql(ms, boundSql, sql);
		return copyFromMappedStatement(ms, new BoundSqlSqlSource(newBoundSql));
	}

	public static class BoundSqlSqlSource implements SqlSource {

		BoundSql boundSql;

		public BoundSqlSqlSource(BoundSql boundSql) {
			this.boundSql = boundSql;
		}

		public BoundSql getBoundSql(Object parameterObject) {
			return boundSql;
		}
	}

	public static BoundSql copyFromBoundSql(MappedStatement ms, BoundSql boundSql, String sql) {
		BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), sql, boundSql.getParameterMappings(),
				boundSql.getParameterObject());
		for (ParameterMapping mapping : boundSql.getParameterMappings()) {
			String prop = mapping.getProperty();
			if (boundSql.hasAdditionalParameter(prop)) {
				newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
			}
		}
		return newBoundSql;
	}

	/**
	 * copyFromNewSql
	 * 
	 * @param ms
	 * @param boundSql
	 * @param sql
	 * @param entryClass
	 * @return
	 */
	public static MappedStatement copyFromNewSql(MappedStatement ms, BoundSql boundSql, String sql,
			Class<?> entryClass) {

		Object parameterObject = boundSql.getParameterObject();
		sql = "<script>" + sql + "</script>";

		// XMLLanguageDriver XMLDriver = new XMLLanguageDriver();
		SqlSource sqlSource = XMLDriver.createSqlSource(ms.getConfiguration(), sql, parameterObject.getClass());
		// BoundSql bs = sqlSource.getBoundSql(parameterObject);
		// LOG.debug("XMLDriver parse sql:{}", bs.getSql());

		return copyFromMappedStatement(ms, sqlSource, getResultMap(ms, entryClass));
	}

	/**
	 * copyFromMappedStatement
	 * 
	 * @param ms
	 * @param newSqlSource
	 * @return
	 */
	public static MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
		return copyFromMappedStatement(ms, newSqlSource, ms.getResultMaps());
	}

	/**
	 * copyFromMappedStatement
	 * 
	 * @param ms
	 * @param newSqlSource
	 * @return
	 */
	public static MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource,
			List<ResultMap> resultMap) {

		Builder builder = new Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());

		builder.resource(ms.getResource());
		builder.fetchSize(ms.getFetchSize());
		builder.statementType(ms.getStatementType());
		builder.keyGenerator(ms.getKeyGenerator());
		if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
			StringBuffer keyProperties = new StringBuffer();
			for (String keyProperty : ms.getKeyProperties()) {
				keyProperties.append(keyProperty).append(",");
			}
			keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
			builder.keyProperty(keyProperties.toString());
		}

		// setStatementTimeout()
		builder.timeout(ms.getTimeout());

		// setStatementResultMap()
		builder.parameterMap(ms.getParameterMap());

		// setStatementResultMap()
		// builder.resultMaps(ms.getResultMaps());
		builder.resultMaps(resultMap);
		builder.resultSetType(ms.getResultSetType());

		// setStatementCache()
		builder.cache(ms.getCache());
		builder.flushCacheRequired(ms.isFlushCacheRequired());
		builder.useCache(ms.isUseCache());

		return builder.build();
	}

	/**
	 * 取得ResultMap
	 * 
	 * @param ms
	 * @param entryClass
	 * @return
	 */
	public static List<ResultMap> getResultMap(MappedStatement ms, Class<?> entryClass) {
		List<ResultMap> list = ms.getResultMaps();
		if (entryClass != null) {
			List<ResultMap> result = new ArrayList<ResultMap>();
			for (ResultMap rm : list) {
				ResultMap.Builder builders = new ResultMap.Builder(ms.getConfiguration(), rm.getId(), entryClass,
						rm.getResultMappings(), rm.getAutoMapping());
				result.add(builders.build());
			}
			return result;
		}
		return list;
	}

	/**
	 * 取得Dao Class
	 */
	public static Class<?> getDaoClass(String id) {
		Class<?> result = null;
		if (id != null) {
			result = classCache.get(id);
			if (result == null) {
				String[] ids = id.trim().split("\\.");
				int len = ids.length - 1;
				if (len >= 1) {
					String methodName = ids[len];
					String cName = id.replace("." + methodName, "");
					try {
						result = Class.forName(cName);
						classCache.put(id, result);
					} catch (ClassNotFoundException e) {
						LOG.error("getDaoClass error:{}", e);
					}
				}
			}
		}
		return result;
	}

	/**
	 * 取得DAO繼承BaseDao所指定T class
	 * 
	 * @param clazz
	 * @return
	 */
	public static Class<?> getrResolveType(Class<?> clazz) {
		Class<?>[] types = GenericTypeResolver.resolveTypeArguments(clazz, GenericDao.class);
		// if (types != null && types.length >= 2) {
		if (types != null && types.length >= 1) {
			return types[0];
		}
		return null;
	}

	/**
	 * getBaseDaoTable
	 * 
	 * @param entityClass
	 * @return
	 */
	public static Table getBaseDaoTable(Class<?> entityClass) {
		if (entityClass != null) {
			if (entityClass.isAnnotationPresent(Table.class)) {
				return entityClass.getAnnotation(Table.class);
			}
		}
		return null;
	}

	/**
	 * 取得所需參數
	 * 
	 * @param boundSql
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String parseParameter(BoundSql boundSql) {
		StringBuilder result = new StringBuilder();
		Object parameterObject = boundSql.getParameterObject();
		if (parameterObject instanceof HashMap) {
			HashMap<String, Object> tempMap = (HashMap<String, Object>) parameterObject;

			List<ParameterMapping> ParameterList = boundSql.getParameterMappings();
			for (ParameterMapping pm : ParameterList) {
				String key = SimpleUtil.trim(pm.getProperty());
				String value = SimpleUtil.trim(tempMap.get(key));
				result.append("&").append(key).append("=").append(value);
			}
		} else if (parameterObject instanceof String) {
			List<ParameterMapping> ParameterList = boundSql.getParameterMappings();
			if (ParameterList.size() == 0) {
				result.append("&primaryKey").append("=").append(SimpleUtil.trim(parameterObject));
			} else {
				for (ParameterMapping pm : ParameterList) {
					String key = SimpleUtil.trim(pm.getProperty());
					String value = SimpleUtil.trim(parameterObject);
					result.append("&").append(key).append("=").append(value);
				}
			}
		}
		return result.toString();
	}
}
