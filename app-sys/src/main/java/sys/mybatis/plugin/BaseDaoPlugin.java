package sys.mybatis.plugin;

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

import sys.mybatis.BaseDaoSql;
import sys.mybatis.MybatisUtil;

/**
 * MyBatis BaseDao Plugin 攔截BaseDao,產生出對應之SQL
 * 
 * @author Fantasy
 * 
 */
@Intercepts({
		@Signature(type = Executor.class, method = "query", args = {
				MappedStatement.class, Object.class, RowBounds.class,
				ResultHandler.class }),
		@Signature(type = Executor.class, method = "update", args = {
				MappedStatement.class, Object.class }) })
public class BaseDaoPlugin implements Interceptor {

	private final Logger LOG = LoggerFactory.getLogger(BaseDaoPlugin.class);

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		// LOG.debug("BaseDaoPlugin---->>>>>>> ---------------");
		Object[] queryArgs = invocation.getArgs();
		MappedStatement ms = (MappedStatement) queryArgs[MybatisUtil.MAPPED_STATEMENT_INDEX];
		Object parameter = queryArgs[MybatisUtil.PARAMETER_INDEX];
		BoundSql boundSQL = ms.getBoundSql(parameter);
		String mapperSQL = boundSQL.getSql();
		SqlCommandType sct = ms.getSqlCommandType();
		// LOG.debug("mapperSQL:{}", mapperSQL);
		// 判斷是否為BaseDao
		if (BaseDaoSql.isBaseDaoSql(mapperSQL)) {
			Class<?> daoClass = MybatisUtil.getDaoClass(ms.getId());
			Class<?> entityClass = MybatisUtil.getrResolveType(daoClass);
			Object result = null;
			// SELECT
			if (SqlCommandType.SELECT.equals(sct)) {
				String sql = BaseDaoSql.getSql(daoClass, mapperSQL);
				// LOG.debug("SQL:{}", sql);
				MappedStatement newMs = MybatisUtil.copyFromNewSql(ms,
						boundSQL, sql, entityClass);
				queryArgs[MybatisUtil.MAPPED_STATEMENT_INDEX] = newMs;
				result = invocation.proceed();

				return result;
			}
			// UPDATE,INSERT,DELETE
			else if (SqlCommandType.UPDATE.equals(sct)
					|| SqlCommandType.INSERT.equals(sct)
					|| SqlCommandType.DELETE.equals(sct)) {

				String sql = BaseDaoSql.getSql(daoClass,
						mapperSQL.replace("save", "update"));
				// LOG.debug("SQL:{}", sql);
				MappedStatement newMs = MybatisUtil.copyFromNewSql(ms,
						boundSQL, sql, null);
				queryArgs[MybatisUtil.MAPPED_STATEMENT_INDEX] = newMs;
				result = invocation.proceed();
				// save
				if ((Integer) result == 0 && ms.getId().endsWith("save")) {
					// do insert
					sql = BaseDaoSql.getSql(daoClass,
							mapperSQL.replace("save", "insert"));
					// LOG.debug("SQL:{}", sql);
					newMs = MybatisUtil.copyFromNewSql(ms, boundSQL, sql, null);
					queryArgs[MybatisUtil.MAPPED_STATEMENT_INDEX] = newMs;
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
			// LOG.info("BaseDaoPlugin Executor");
			return Plugin.wrap(target, this);
		} else {
			return target;
		}
	}

	@Override
	public void setProperties(Properties properties) {
		LOG.info("BaseDaoPlugin Properties init");
	}

}
