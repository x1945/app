package sys.spring;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;

import sys.beans.User;
import sys.service.SystemService;
import sys.system.ControllerContext;
import sys.util.Util;

public class CustomJdbcTokenRepositoryImpl extends JdbcTokenRepositoryImpl {

	// public static final String DEF_REMOVE_USER_TOKENS_SQL =
	// "delete from persistent_logins where last_used=(select * from (select
	// MIN(last_used) from persistent_logins where username=?)as t) and
	// username=?;";
	// private String removeUserTokensSql = DEF_REMOVE_USER_TOKENS_SQL;
	//
	// public void createNewToken(PersistentRememberMeToken token) {
	// getJdbcTemplate().update(insertTokenSql, token.getUsername(),
	// token.getSeries(),
	// token.getTokenValue(), token.getDate());
	// }

	// ~ Static fields/initializers
	// =====================================================================================

	@Autowired
	SystemService systemService;

	private static final Logger LOG = LoggerFactory.getLogger(CustomJdbcTokenRepositoryImpl.class);

	/** Default SQL for creating the database table to store the tokens */
	public static final String CREATE_TABLE_SQL = "create table persistent_logins (username varchar(64) not null, series varchar(64) primary key, "
			+ "token varchar(64) not null, last_used timestamp not null)";
	/** The default SQL used by the <tt>getTokenBySeries</tt> query */
	public static final String DEF_TOKEN_BY_SERIES_SQL = "select username,series,token,last_used from persistent_logins where series = ?";
	/** The default SQL used by <tt>createNewToken</tt> */
	public static final String DEF_INSERT_TOKEN_SQL = "insert into persistent_logins (username, series, token, last_used) values(?,?,?,?)";
	/** The default SQL used by <tt>updateToken</tt> */
	public static final String DEF_UPDATE_TOKEN_SQL = "update persistent_logins set token = ?, last_used = ? where series = ?";
	/** The default SQL used by <tt>removeUserTokens</tt> */
	public static final String DEF_REMOVE_USER_TOKENS_SQL = "delete from persistent_logins where username = ?";

	public static final String DEF_REMOVE_USER_TOKENS_SQL2 = "delete from persistent_logins where username = ? and series = ?";
	// ~ Instance fields
	// ================================================================================================

	private String tokensBySeriesSql = DEF_TOKEN_BY_SERIES_SQL;
	private String insertTokenSql = DEF_INSERT_TOKEN_SQL;
	private String updateTokenSql = DEF_UPDATE_TOKEN_SQL;
	private String removeUserTokensSql = DEF_REMOVE_USER_TOKENS_SQL;
	private String removeUserTokensSql2 = DEF_REMOVE_USER_TOKENS_SQL2;
	private boolean createTableOnStartup;

	// @Override
	// protected void initDao() {
	// if (createTableOnStartup) {
	// getJdbcTemplate().execute(CREATE_TABLE_SQL);
	// }
	// }

	@Override
	public void createNewToken(PersistentRememberMeToken token) {
		User user = systemService.getLoginUser();
		user.setSeries(token.getSeries());
		LOG.debug("createNewToken user : {}", user);
		LOG.debug("createNewToken username[{}] Series[{}] Token[{}] Date[{}]", token.getUsername(), token.getSeries(),
				token.getTokenValue(), token.getDate());
		getJdbcTemplate().update(insertTokenSql, token.getUsername(), token.getSeries(), token.getTokenValue(),
				token.getDate());
	}

	@Override
	public void updateToken(String series, String tokenValue, Date lastUsed) {
		ControllerContext.setSeries(series);
		LOG.debug("updateToken series[{}] tokenValue[{}] lastUsed[{}]", series, tokenValue, lastUsed);
		getJdbcTemplate().update(updateTokenSql, tokenValue, lastUsed, series);
	}

	/**
	 * Loads the token data for the supplied series identifier.
	 * 
	 * If an error occurs, it will be reported and null will be returned (since
	 * the result should just be a failed persistent login).
	 * 
	 * @param seriesId
	 * @return the token matching the series, or null if no match found or an
	 *         exception occurred.
	 */
	// @Override
	// public PersistentRememberMeToken getTokenForSeries(String seriesId) {
	// try {
	// return getJdbcTemplate().queryForObject(tokensBySeriesSql,
	// new RowMapper<PersistentRememberMeToken>() {
	//
	// public PersistentRememberMeToken mapRow(ResultSet rs, int rowNum)
	// throws SQLException {
	// return new PersistentRememberMeToken(rs.getString(1), rs
	// .getString(2), rs.getString(3), rs.getTimestamp(4));
	// }
	// }, seriesId);
	// } catch (EmptyResultDataAccessException zeroResults) {
	// if (logger.isDebugEnabled()) {
	// logger.debug("Querying token for series '" + seriesId
	// + "' returned no results.", zeroResults);
	// }
	// } catch (IncorrectResultSizeDataAccessException moreThanOne) {
	// logger.error("Querying token for series '" + seriesId
	// + "' returned more than one value. Series" + " should be unique");
	// } catch (DataAccessException e) {
	// logger.error("Failed to load token for series " + seriesId, e);
	// }
	//
	// return null;
	// }

	@Override
	public void removeUserTokens(String username) {
		String series = systemService.getSeries(username);
		// String series = ControllerContext.getSeries();
		LOG.debug("removeUserTokens series[{}]", series);
		if (Util.isNotEmpty(series)) {
			getJdbcTemplate().update(removeUserTokensSql2, username, series);
			systemService.removeSeries(username);
		} else {
			getJdbcTemplate().update(removeUserTokensSql, username);
		}
	}

	/**
	 * Intended for convenience in debugging. Will create the persistent_tokens
	 * database table when the class is initialized during the initDao method.
	 * 
	 * @param createTableOnStartup
	 *            set to true to execute the
	 */
	// @Override
	// public void setCreateTableOnStartup(boolean createTableOnStartup) {
	// this.createTableOnStartup = createTableOnStartup;
	// }

}
