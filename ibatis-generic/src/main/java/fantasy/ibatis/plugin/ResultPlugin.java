package fantasy.ibatis.plugin;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fantasy.ibatis.util.SimpleUtil;

/**
 * MyBatis Result Plugin 解析BaseDao query data
 * 
 * @author Fantasy
 * 
 */
@Intercepts({ @Signature(method = "handleResultSets", type = ResultSetHandler.class, args = { Statement.class }) })
public class ResultPlugin implements Interceptor {

	private static final Logger LOG = LoggerFactory.getLogger(ResultPlugin.class);

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		// LOG.debug("ResultPlugin---->>>>>>> ---------------");
		ResultSetHandler resultSetHandler = (ResultSetHandler) invocation.getTarget();
		MappedStatement ms = (MappedStatement) SimpleUtil.getFieldValue(resultSetHandler, "mappedStatement");
		// BoundSql boundsql = (BoundSql)
		// ReflectUtil.getFieldValue(resultSetHandler, "boundSql");
		SqlCommandType sct = ms.getSqlCommandType();

		if (SqlCommandType.SELECT.equals(sct)) {
			// String sql = boundsql.getSql();
			String id = ms.getId().trim();
			// LOG.info("ResultPlugin SQL: {}", sql);
			if (id.endsWith(".count") || id.endsWith(".getSeqByMap")) {
				List<Integer> result = new ArrayList<Integer>();
				Statement statement = (Statement) invocation.getArgs()[0]; // 取得方法的參數Statement
				ResultSet rs = statement.getResultSet(); // 取得結果集
				Integer count = 0;
				if (rs.next()) {
					count = rs.getInt(1);
				}
				result.add(count);
				return result;
			}

		}

		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		// 當目標類是ResultSetHandler類型時，才包裝目標類，否者直接返回目標本身,減少目標被代理的次數
		if (target instanceof ResultSetHandler) {
			// LOG.info("ResultPlugin ResultSetHandler");
			return Plugin.wrap(target, this);
		} else {
			return target;
		}
	}

	@Override
	public void setProperties(Properties properties) {
		LOG.info("ResultPlugin Properties init");
	}

}
