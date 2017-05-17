package sys.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateUtil {

	private static final SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMddHHmmss");

	/**
	 * 取得當前日期
	 * 
	 * @return
	 */
	public static Date getDate() {
		return new Date();
	}

	/**
	 * 取得當前時間
	 * 
	 * @return
	 */
	public static Timestamp getTimestamp() {
		return new Timestamp(getDate().getTime());
	}

	/**
	 * 取得當前時間
	 * 
	 * @return
	 */
	public static long getTime() {
		return Long.parseLong(sdFormat.format(getDate()));
	}
}
