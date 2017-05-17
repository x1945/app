package sys.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Options.FlushCachePolicy;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface BaseDao<T> {

	@Select(BaseDaoSql.COUNT)
	Integer count(Map<String, Object> map);

	@Select(BaseDaoSql.LIST)
	List<T> list(Map<String, Object> map);

	@Select(BaseDaoSql.QUERY)
	T query(Map<String, Object> map);

	@Select(BaseDaoSql.QUERY_BY_PK)
	T queryByPk(Map<String, Object> map);

	@Update(BaseDaoSql.SAVE)
	int save(T bean);

	@Insert(BaseDaoSql.INSERT)
	int insert(T bean);

	@Update(BaseDaoSql.UPDATE)
	int update(T bean);

	@Delete(BaseDaoSql.DELETE)
	int delete(T bean);

	@Delete(BaseDaoSql.DELETE_BY_PK)
	int deleteByPk(Map<String, Object> map);

	@Delete(BaseDaoSql.DELETE_BY_MAP)
	int deleteByMap(Map<String, Object> map);

	@Insert(BaseDaoSql.BATCH_INSERT)
	int batchInsert(List<T> list);

	@Select("select 1")
	@Options(flushCache=FlushCachePolicy.TRUE)
	int flushCache();
}
