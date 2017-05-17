package sys.mybatis;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sys.beans.AppBean;
import sys.util.ReflectUtil;
import sys.util.StringUtil;
import sys.util.Util;

/**
 * BaseSqlBuilder
 * 
 * @author Fantasy
 * 
 */
public final class BaseDaoSql {

	private static final Logger LOG = LoggerFactory.getLogger(BaseDaoSql.class);

	private final static String PREFIX = "BaseDao.";

	public final static String COUNT = PREFIX + "count";
	public final static String LIST = PREFIX + "list";
	public final static String QUERY = PREFIX + "query";
	public final static String QUERY_BY_PK = PREFIX + "queryByPk";
	public final static String SAVE = PREFIX + "save";
	public final static String INSERT = PREFIX + "insert";
	public final static String UPDATE = PREFIX + "update";
	public final static String DELETE = PREFIX + "delete";
	public final static String DELETE_BY_PK = PREFIX + "deleteByPk";
	public final static String DELETE_BY_MAP = PREFIX + "deleteByMap";
	public final static String BATCH_INSERT = PREFIX + "batchInsert";

	public final static String[] fieldIgnore = { "serialVersionUID" };

	public final static String[] listOrderByFields = { "seq" };

	private final static HashMap<String, HashMap<String, String>> sqlCache = new HashMap<String, HashMap<String, String>>();

	public static boolean isBaseDaoSql(String baseDaoSql) {
		return baseDaoSql.startsWith(PREFIX);
	}

	/**
	 * 取得SQL
	 * 
	 * @param type
	 * @param baseDaoSql
	 * @return
	 */
	public static String getSql(Class<?> type, String baseDaoSql) {
		String result = null;
		if (type != null && baseDaoSql != null) {
			String daoName = type.getSimpleName();
			// LOG.debug("getSql daoName:{}", daoName);
			baseDaoSql = baseDaoSql.trim();
			HashMap<String, String> sqlMap = sqlCache.get(daoName);
			if (sqlMap == null) {
				sqlMap = new HashMap<String, String>();
				sqlCache.put(daoName, sqlMap);
			}
			result = sqlMap.get(baseDaoSql);
			if (result == null) {
				// LOG.debug("getSql SQL parse");
				result = parseSql(type, baseDaoSql);
				sqlMap.put(baseDaoSql, result);
			} else {
				// LOG.debug("getSql SQL from cache");
			}
		}
		return result;
	}

	public static String parseSql(Class<?> daoClass, String baseDaoSql) {
		String result = null;
		// LOG.debug("baseDaoSql:{}", baseDaoSql);
		Class<?> entityClass = MybatisUtil.getrResolveType(daoClass);
		if (entityClass != null) {
			switch (baseDaoSql) {
			case COUNT:
				result = countSql(entityClass);
				break;
			case LIST:
				result = listSql(entityClass);
				break;
			case QUERY:
				result = querySql(entityClass);
				break;
			case QUERY_BY_PK:
				result = queryByPkSql(entityClass);
				break;
			case SAVE:
				break;
			case INSERT:
				result = insertSQL(entityClass);
				break;
			case UPDATE:
				result = updateSql(entityClass);
				break;
			case DELETE:
				result = deleteSql(entityClass);
				break;
			case DELETE_BY_PK:
				result = deleteByPkSql(entityClass);
				break;
			case DELETE_BY_MAP:
				result = deleteByMapSql(entityClass);
				break;
			case BATCH_INSERT:
				result = batchInsertSql(entityClass);
				break;
			}
		}
		// LOG.debug("parseSql:{}", result);
		return result;
	}

	public static String countSql(Class<?> entityClass) {
		StringBuffer sb = new StringBuffer();
		if (entityClass.isAnnotationPresent(Table.class)) {
			Table bdt = entityClass.getAnnotation(Table.class);
			sb.append(" select count(1) from ").append(parseTable(entityClass, bdt));
			sb.append(" <where>").append(parseWhere(entityClass, bdt)).append(" </where>");
		}

		return sb.toString();
	}

	public static String listSql(Class<?> entityClass) {
		StringBuffer sb = new StringBuffer();
		if (entityClass.isAnnotationPresent(Table.class)) {
			Table bdt = entityClass.getAnnotation(Table.class);
			// 加入 top 100, 為安全機制, 若超過100請用page
			// sb.append(" select <if test='isLimit == null or isLimit == true'
			// >top 100</if> * from ")
			// .append(parseTable(entityClass, bdt));
			sb.append(" select  * from ").append(parseTable(entityClass, bdt));
			sb.append(" <where>").append(parseWhere(entityClass, bdt)).append(" </where>");
			// sb.append(parseOrder(entityClass, bdt));
			sb.append(" <if test='sortKey == null or sortKey == \"\" '>");
			sb.append(parseOrder(entityClass, bdt));
			sb.append(" </if>");
			sb.append(" <if test='sortKey != null and sortKey != \"\" '>");
			sb.append(" order by ${sortKey} ");
			sb.append(" </if>");
		}

		return sb.toString();
	}

	public static String querySql(Class<?> entityClass) {
		StringBuffer sb = new StringBuffer();
		if (entityClass.isAnnotationPresent(Table.class)) {
			Table bdt = entityClass.getAnnotation(Table.class);
			sb.append(" select * from ").append(parseTable(entityClass, bdt));
			// sb.append(" where ").append(parseWherePks(bdt));
			sb.append(" where ").append(parseWhere(entityClass, bdt));
			// 只取一筆
			// PostgreSQL
			sb.append(" LIMIT 1 ");
		}

		return sb.toString();
	}

	public static String queryByPkSql(Class<?> entityClass) {
		StringBuffer sb = new StringBuffer();
		if (entityClass.isAnnotationPresent(Table.class)) {
			Table bdt = entityClass.getAnnotation(Table.class);
			sb.append(" select * from ").append(parseTable(entityClass, bdt));
			// sb.append(" where ").append(parseWherePk(bdt));
			sb.append(" where ").append(parseWherePks(bdt));
		}

		return sb.toString();
	}

	public static String insertSQL(Class<?> entityClass) {
		StringBuffer sb = new StringBuffer();
		if (entityClass.isAnnotationPresent(Table.class)) {
			Table bdt = entityClass.getAnnotation(Table.class);
			// Field[] fields = entityClass.getDeclaredFields();
			Field[] fields = ReflectUtil.getAllFields(entityClass, AppBean.class);
			sb.append(" insert into ").append(parseTable(entityClass, bdt)).append(" (");
			for (Field field : fields) {
				String fName = field.getName();
				if (!ArrayUtils.contains(bdt.ignore(), fName) && !ArrayUtils.contains(fieldIgnore, fName)) {
					sb.append(bdt.upperCase() ? fName.toUpperCase() : fName).append(",");
				}
			}
			int len = sb.length();
			sb.delete(len - 1, sb.length());
			sb.append(" ) values ( ");
			for (Field field : fields) {
				String fName = field.getName();
				if (!ArrayUtils.contains(bdt.ignore(), fName) && !ArrayUtils.contains(fieldIgnore, fName)) {
					sb.append("#{").append(fName).append("},");
				}
			}
			len = sb.length();
			sb.delete(len - 1, sb.length());
			sb.append(" )");
		}

		return sb.toString();
	}

	public static String updateSql(Class<?> entityClass) {
		StringBuffer sb = new StringBuffer();
		if (entityClass.isAnnotationPresent(Table.class)) {
			Table bdt = entityClass.getAnnotation(Table.class);
			sb.append(" update ").append(parseTable(entityClass, bdt));
			sb.append(" set ").append(parseSet(entityClass, bdt));
			sb.append(" where ").append(parseWherePks(bdt));
		}

		return sb.toString();
	}

	public static String updateSeqSql(Class<?> entityClass) {
		StringBuffer sb = new StringBuffer();
		if (entityClass.isAnnotationPresent(Table.class)) {
			Table bdt = entityClass.getAnnotation(Table.class);
			String seq = bdt.upperCase() ? bdt.seq().toUpperCase() : bdt.seq();
			sb.append(" update ").append(parseTable(entityClass, bdt));
			// sb.append(" set seq = #{seq} ");
			sb.append(" set ").append(seq).append(" = #{").append(bdt.seq()).append("} ");
			sb.append(" where ").append(parseWherePks(bdt));
		}

		return sb.toString();
	}

	public static String deleteSql(Class<?> entityClass) {
		StringBuffer sb = new StringBuffer();
		if (entityClass.isAnnotationPresent(Table.class)) {
			Table bdt = entityClass.getAnnotation(Table.class);
			sb.append(" delete from  ").append(parseTable(entityClass, bdt));
			sb.append(" where ").append(parseWherePks(bdt));
		}

		return sb.toString();
	}

	public static String deleteByPkSql(Class<?> entityClass) {
		StringBuffer sb = new StringBuffer();
		if (entityClass.isAnnotationPresent(Table.class)) {
			Table bdt = entityClass.getAnnotation(Table.class);
			sb.append(" delete from  ").append(parseTable(entityClass, bdt));
			// sb.append(" where ").append(parseWherePk(bdt));
			sb.append(" where ").append(parseWherePks(bdt));
		}

		return sb.toString();
	}

	public static String deleteByMapSql(Class<?> entityClass) {
		StringBuffer sb = new StringBuffer();
		if (entityClass.isAnnotationPresent(Table.class)) {
			Table bdt = entityClass.getAnnotation(Table.class);
			sb.append(" delete from  ").append(parseTable(entityClass, bdt));
			sb.append(" where ").append(parseWhere(entityClass, bdt));
		}

		return sb.toString();
	}

	public static String batchInsertSql(Class<?> entityClass) {
		StringBuffer sb = new StringBuffer();
		if (entityClass.isAnnotationPresent(Table.class)) {
			Table bdt = entityClass.getAnnotation(Table.class);
			// Field[] fields = entityClass.getDeclaredFields();
			Field[] fields = ReflectUtil.getAllFields(entityClass, AppBean.class);
			sb.append(" insert into ").append(parseTable(entityClass, bdt)).append(" (");
			for (Field field : fields) {
				String fName = field.getName();
				if (!ArrayUtils.contains(bdt.ignore(), fName) && !ArrayUtils.contains(fieldIgnore, fName)) {
					sb.append(bdt.upperCase() ? fName.toUpperCase() : fName).append(",");
				}
			}
			int len = sb.length();
			sb.delete(len - 1, sb.length());
			sb.append(" ) values ");
			sb.append("<foreach item='data' collection='list' open='(' close=')' separator='),('>");
			for (Field field : fields) {
				String fName = field.getName();
				if (!ArrayUtils.contains(bdt.ignore(), fName) && !ArrayUtils.contains(fieldIgnore, fName)) {
					sb.append("#{data.").append(fName).append("},");
				}
			}
			len = sb.length();
			sb.delete(len - 1, sb.length());
			sb.append("</foreach>");
		}

		return sb.toString();
	}

	/**
	 * table name
	 * 
	 * @param type
	 * @param bdt
	 * @return
	 */
	private static String parseTable(Class<?> type, Table bdt) {
		StringBuffer sb = new StringBuffer();
		sb.append(bdt.schema());
		if (Util.isNotEmpty(bdt.schema())) {
			sb.append(".");
		}
		// sb.append(bdt.name().length() > 0 ? bdt.name() :
		// type.getSimpleName());
		if (bdt.name().length() > 0) {
			sb.append(bdt.name());
		} else {
			String name = StringUtil.trim(type.getSimpleName());
			sb.append(bdt.upperCase() ? name.toUpperCase() : name.toLowerCase());
		}
		return sb.toString();
	}

	/**
	 * update set
	 * 
	 * @param type
	 * @param bdt
	 * @return
	 */
	private static String parseSet(Class<?> type, Table bdt) {
		StringBuffer sb = new StringBuffer();
		// Field[] fields = type.getDeclaredFields();
		Field[] fields = ReflectUtil.getAllFields(type, AppBean.class);
		for (Field field : fields) {
			String fName = field.getName();
			if (!ArrayUtils.contains(bdt.ignore(), fName) && !ArrayUtils.contains(fieldIgnore, fName)) {
				sb.append(bdt.upperCase() ? fName.toUpperCase() : fName);
				sb.append("=#{").append(fName).append("},");
			}
		}
		int len = sb.length();
		sb.delete(len - 1, sb.length());
		return sb.toString();
	}

	/**
	 * 解析where
	 * 
	 * @param entityClass
	 * @param bdt
	 * @return
	 */

	private static String parseWhere(Class<?> entityClass, Table bdt) {
		// return parseWhere(entityClass, bdt, false);
		return parseWhere(entityClass, bdt, true);
	}

	/**
	 * 解析where
	 * 
	 * @param entityClass
	 * @param bdt
	 * @param determineEmpty
	 *            (是否判斷空白)
	 * @return
	 */
	private static String parseWhere(Class<?> entityClass, Table bdt, boolean determineEmpty) {
		StringBuffer sb = new StringBuffer();
		sb.append("<trim prefix=' ' prefixOverrides='and|or'>");

		// Field[] fields = entityClass.getDeclaredFields();
		Field[] fields = ReflectUtil.getAllFields(entityClass, AppBean.class);
		// int count = 0;
		for (Field field : fields) {
			String fName = field.getName();
			if (!ArrayUtils.contains(bdt.ignore(), fName) && !ArrayUtils.contains(fieldIgnore, fName)) {
				if (determineEmpty) {
					sb.append("<if test='").append(fName).append(" != null and ").append(fName).append(" != \"\" '>");
				} else {
					sb.append("<if test='").append(fName).append("!= null'>");
				}
				// if (count++ >= 1) {
				sb.append(" and ");
				// }
				sb.append(bdt.upperCase() ? fName.toUpperCase() : fName);
				sb.append("=#{").append(field.getName()).append("}");
				sb.append("</if>");
			}
		}
		sb.append("</trim>");

		return sb.toString();
	}

	/**
	 * 解析where build with pks
	 * 
	 * @param type
	 * @param bdt
	 * @return
	 */
	private static String parseWherePks(Table bdt) {
		StringBuffer sb = new StringBuffer();
		String[] pks = bdt.pk();
		for (String pk : pks) {
			if (sb.length() > 0)
				sb.append(" and ");

			sb.append(bdt.upperCase() ? pk.toUpperCase() : pk);
			sb.append("=#{").append(pk).append("}");
		}
		return sb.toString();
	}

	/**
	 * 解析Order
	 * 
	 * @param entityClass
	 * @param bdt
	 * @return
	 */
	private static String parseOrder(Class<?> entityClass, Table bdt) {
		StringBuffer sb = new StringBuffer();
		// Field[] fields = entityClass.getDeclaredFields();
		Field[] fields = ReflectUtil.getAllFields(entityClass, AppBean.class);
		for (Field field : fields) {
			String fName = field.getName();
			if (bdt.seq().equalsIgnoreCase(fName)) {
				if (sb.length() == 0) {
					sb.append(" order by ");
				} else {
					sb.append(",");
				}
				sb.append(bdt.upperCase() ? fName.toUpperCase() : fName);
			}

			// if (ArrayUtils.contains(listOrderByFields, fName)) {
			// if (sb.length() == 0) {
			// sb.append(" order by ");
			// } else {
			// sb.append(",");
			// }
			// sb.append(bdt.upperCase() ? fName.toUpperCase() : fName);
			// }
		}

		return sb.toString();
	}

}
