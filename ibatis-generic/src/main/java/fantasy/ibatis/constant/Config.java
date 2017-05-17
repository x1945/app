package fantasy.ibatis.constant;

public interface Config {
	static final String Empty = "";
	static final int PageSize = 10;
	static final int PageInterval = 3;
	static final int BUFFER_SIZE = 1024;
	static final String PREFIX = "GenericDao.";

	// 快取
	interface CACHE {
		long FOREVER = 0;
		long TIME = 60000 * 5; // 60秒*5 = 5分鐘
		long ONE_MINUTE = 60000; // 1分鐘
		long TWO_MINUTE = 60000 * 2; // 1分鐘
		long THREE_MINUTE = 60000 * 3; // 3分鐘
		int SIZE = 1024; // 1024筆
	}

	// SQL NAME
	interface SQL {
		String COUNT = PREFIX + "count";
		String LIST = PREFIX + "list";
		String QUERY = PREFIX + "query";
		String QUERY_BY_PK = PREFIX + "queryByPk";
		String SAVE = PREFIX + "save";
		String INSERT = PREFIX + "insert";
		String UPDATE = PREFIX + "update";
		String DELETE = PREFIX + "delete";
		String DELETE_BY_PK = PREFIX + "deleteByPk";
		String DELETE_BY_MAP = PREFIX + "deleteByMap";
		String BATCH_INSERT = PREFIX + "batchInsert";
	}
}
