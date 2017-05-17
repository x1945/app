package sys.mybatis;

import java.util.HashMap;
import java.util.Map;

public class SQLParams extends HashMap<String, Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SQLParams() {
		super();
	}

	public SQLParams(Map<String, Object> map) {
		super(map);
	}

	/**
	 * 加入排序Key
	 * 
	 * @param key
	 */
	public void orderBy(String key) {
		this.put("sortKey", key);
	}
}
