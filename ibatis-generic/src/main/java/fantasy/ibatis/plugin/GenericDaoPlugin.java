package fantasy.ibatis.plugin;

import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fantasy.ibatis.util.BuildUtil;
import fantasy.ibatis.util.SqlUtil;

/**
 * MyBatis BaseDao Plugin 攔截BaseDao,產生出對應之SQL
 * 
 * @author Fantasy
 * 
 */
@Intercepts({
		@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
				RowBounds.class, ResultHandler.class }),
		@Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }) })
public class GenericDaoPlugin implements Interceptor {

	private final Logger LOG = LoggerFactory.getLogger(GenericDaoPlugin.class);

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		// LOG.debug("GenericDaoPlugin---->>>>>>> ---------------");
		Object[] queryArgs = invocation.getArgs();
		MappedStatement ms = (MappedStatement) queryArgs[BuildUtil.MAPPED_STATEMENT_INDEX];
		Object parameter = queryArgs[BuildUtil.PARAMETER_INDEX];
		BoundSql boundSQL = ms.getBoundSql(parameter);
		String mapperSQL = boundSQL.getSql();
		SqlCommandType sct = ms.getSqlCommandType();
		// LOG.debug("mapperSQL:{}", mapperSQL);
		// 判斷是否為BaseDao
		if (SqlUtil.isGenericDaoSql(mapperSQL)) {
			Class<?> daoClass = BuildUtil.getDaoClass(ms.getId());
			Class<?> entityClass = BuildUtil.getrResolveType(daoClass);
			Object result = null;
			// SELECT
			if (SqlCommandType.SELECT.equals(sct)) {
				String sql = SqlUtil.getSql(daoClass, mapperSQL);
				// LOG.debug("SQL:{}", sql);
				MappedStatement newMs = BuildUtil.copyFromNewSql(ms, boundSQL, sql, entityClass);
				queryArgs[BuildUtil.MAPPED_STATEMENT_INDEX] = newMs;
				result = invocation.proceed();

				return result;
			}
			// UPDATE,INSERT,DELETE
			else if (SqlCommandType.UPDATE.equals(sct) || SqlCommandType.INSERT.equals(sct)
					|| SqlCommandType.DELETE.equals(sct)) {

				String sql = SqlUtil.getSql(daoClass, mapperSQL.replace("save", "update"));
				// LOG.debug("SQL:{}", sql);
				MappedStatement newMs = BuildUtil.copyFromNewSql(ms, boundSQL, sql, null);
				queryArgs[BuildUtil.MAPPED_STATEMENT_INDEX] = newMs;
				result = invocation.proceed();
				// save
				if ((Integer) result == 0 && ms.getId().endsWith("save")) {
					// do insert
					sql = SqlUtil.getSql(daoClass, mapperSQL.replace("save", "insert"));
					// LOG.debug("SQL:{}", sql);
					newMs = BuildUtil.copyFromNewSql(ms, boundSQL, sql, null);
					queryArgs[BuildUtil.MAPPED_STATEMENT_INDEX] = newMs;
					result = invocation.proceed();
				}
				return result;
			}
		}

		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		// 當目標類是Executor類型時，才包裝目標類，否者直接返回目標本身,減少目標被代理的次數
		if (target instanceof Executor) {
			// LOG.info("GenericDaoPlugin Executor");
			return Plugin.wrap(target, this);
		} else {
			return target;
		}
	}

	@Override
	public void setProperties(Properties properties) {
		LOG.info("GenericDaoPlugin Properties init");
	}
}
