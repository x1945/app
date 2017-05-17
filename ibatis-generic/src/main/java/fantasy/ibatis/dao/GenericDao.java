package fantasy.ibatis.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Options.FlushCachePolicy;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import fantasy.ibatis.constant.Config;

public interface GenericDao<T> {

	@Select(Config.SQL.COUNT)
	Integer count(Map<String, Object> map);

	@Select(Config.SQL.LIST)
	List<T> list(Map<String, Object> map);

	@Select(Config.SQL.QUERY)
	T query(Map<String, Object> map);

	@Select(Config.SQL.QUERY_BY_PK)
	T queryByPk(Map<String, Object> map);

	@Update(Config.SQL.SAVE)
	int save(T bean);

	@Insert(Config.SQL.INSERT)
	int insert(T bean);

	@Update(Config.SQL.UPDATE)
	int update(T bean);

	@Delete(Config.SQL.DELETE)
	int delete(T bean);

	@Delete(Config.SQL.DELETE_BY_PK)
	int deleteByPk(Map<String, Object> map);

	@Delete(Config.SQL.DELETE_BY_MAP)
	int deleteByMap(Map<String, Object> map);

	@Insert(Config.SQL.BATCH_INSERT)
	int batchInsert(List<T> list);

	@Select("select 1")
	@Options(flushCache = FlushCachePolicy.TRUE)
	int flushCache();
}
