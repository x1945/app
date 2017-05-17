package sys.constant;

public interface SysConfig {
	static final String Empty = "";
	static final int PageSize = 10;
	static final int PageInterval = 3;
	static final int BUFFER_SIZE = 1024;

	// 帳號狀況
	interface Account_status {
		String 尚未開通 = "0";
		String 正常使用 = "1";
		String 封鎖 = "2";
	}
}
