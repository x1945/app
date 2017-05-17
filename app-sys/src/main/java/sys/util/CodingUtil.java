package sys.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sys.constant.SysConfig;

/**
 * 編碼轉換
 * 
 * @author Fantasy
 * 
 */
public final class CodingUtil {

	private static final Logger LOG = LoggerFactory.getLogger(CodingUtil.class);

	public static final String UTF8 = "UTF8";
	public static final String BIG5 = "BIG5";
	public static final String ISO8859_1 = "ISO-8859-1";

	public static final String USER_AGENT = "Mozilla/5.0";

	/**
	 * UTF8轉BIG5
	 * 
	 * @param source
	 * @return
	 */
	public static String UTF8toBIG5(String source) {
		return transformChinese(source, UTF8, BIG5);
	}

	/**
	 * BIG5轉UTF8
	 * 
	 * @param source
	 * @return
	 */
	public static String BIG5toUTF8(String source) {
		return transformChinese(source, BIG5, UTF8);
	}

	/**
	 * UTF8轉ISO8859-1
	 * 
	 * @param source
	 * @return
	 */
	public static String UTF8toISO(String source) {
		return transformChinese(source, UTF8, ISO8859_1);
	}

	/**
	 * ISO8859-1轉UTF8
	 * 
	 * @param source
	 * @return
	 */
	public static String ISOtoUTF8(String source) {
		return transformChinese(source, ISO8859_1, UTF8);
	}

	/**
	 * BIG5轉ISO8859-1
	 * 
	 * @param source
	 * @return
	 */
	public static String BIG5toISO(String source) {
		return transformChinese(source, BIG5, ISO8859_1);
	}

	/**
	 * ISO8859-1轉BIG5
	 * 
	 * @param source
	 * @return
	 */
	public static String ISOtoBIG5(String source) {
		return transformChinese(source, ISO8859_1, BIG5);
	}

	/**
	 * 中文字轉換
	 * 
	 * @param source
	 * @param fromFormat
	 * @param toFormat
	 * @return
	 */
	public static String transformChinese(String source, String fromFormat, String toFormat) {
		try {
			return new String(source.getBytes(fromFormat), toFormat);
		} catch (UnsupportedEncodingException e) {
			LOG.error("transformChinese error[{}]", e.getMessage());
		}
		return source;
	}

	/**
	 * 檔案名稱編碼
	 * 
	 * @param fileName
	 * @return
	 */
	public static String encodingFileName(String fileName) {
		String returnFileName = "";
		try {
			returnFileName = URLEncoder.encode(fileName, UTF8);
			// returnFileName.replaceAll(regex, replacement);
			returnFileName = StringUtils.replace(returnFileName, "+", "%20");
			if (returnFileName.length() > 150) {
				returnFileName = ISOtoUTF8(fileName);
				returnFileName = StringUtils.replace(returnFileName, " ", "%20");
			}
		} catch (UnsupportedEncodingException e) {
			LOG.error("encodingFileName error[{}]", e.getMessage());
		}
		return returnFileName;
	}

	/**
	 * MD5編碼再轉HexCode
	 * 
	 * @param origin
	 * @return
	 */
	public static String md5(String origin) {
		String result = encodeCode(origin, "MD5");
		if (result != null)
			return result.toLowerCase(); // 配合原COA系統,所以轉小寫
		return null;
	}

	/**
	 * 編碼再轉HexCode
	 * 
	 * @param origin
	 * @return
	 */
	public static String encodeCode(String origin, String codeType) {
		String result = null;
		try {
			// MessageDigest md = MessageDigest.getInstance("MD5");
			MessageDigest md = MessageDigest.getInstance(codeType);
			result = getBytesHexStr(md.digest(new String(origin).getBytes()));
		} catch (Exception ex) {
		}
		return result;
	}

	/**
	 * String轉成hex String
	 * 
	 * @param input
	 * @return
	 */
	public static String getHexStr(String input) {
		if (input != null) {
			return getBytesHexStr(input.getBytes());
		}
		return null;
	}

	/**
	 * bytes轉成hex String
	 * 
	 * @param bArray
	 * @return
	 */
	public static String getBytesHexStr(byte[] bArray) {
		StringBuffer sbuff = new StringBuffer();
		for (int i = 0; i < bArray.length; i++) {
			sbuff.append(toHexString(bArray[i]));
		}
		return sbuff.toString();
	}

	/**
	 * bytes轉成hex String
	 * 
	 * @param bArray
	 * @return
	 */
	public static String toHexString(int b) {
		String s = Integer.toHexString(b & 0xff);
		if (s.length() < 2) {
			s = "0" + s;
		}
		return s.toUpperCase();
	}

	/**
	 * 中翻英(google translate)-有字數限制,如需全翻譯還是要透過api,但要收費
	 * 
	 * @param text
	 * @return
	 */
	public static String translate(String text) {
		return translate(text, "UTF-8");
	}

	/**
	 * 中翻英(google translate)
	 * 
	 * @param text
	 * @param coding(編碼)
	 * @return
	 */
	public static String translate(String text, String coding) {
		String result = null;
		StringBuilder sb = new StringBuilder();
		try {
			sb.append("https://translate.google.com.tw/translate_a/single");
			sb.append("?client=t");
			sb.append("&hl=zh-TW");
			sb.append("&sl=zh-TW");
			sb.append("&tl=en");
			sb.append("&ie=").append(coding);
			sb.append("&oe=").append(coding);
			sb.append("&text=").append(URLEncoder.encode(text, coding));
			// sb.append("&multires=1");
			// sb.append("&otf=1");
			// sb.append("&pc=1");
			// sb.append("&it=srcd_gms.1378");
			// sb.append("&ssel=4");
			// sb.append("&tsel=6");
			// sb.append("&sc=1");

			URL url = new URL(sb.toString());
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			// optional default is GET
			con.setRequestMethod("GET");
			// add request header
			con.setRequestProperty("User-Agent", USER_AGENT);

			int responseCode = con.getResponseCode();
			// LOG.info("responseCode:{}", responseCode);
			if (responseCode == 200) {
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();

				result = response.toString();
			} else {
				LOG.error("responseCode:{}", responseCode);
			}
		} catch (Exception e) {
			LOG.error("Exception:{}", e);
		}
		return result == null ? "" : parseTranslate(result);
	}

	/**
	 * parseTranslate
	 * 
	 * @param text
	 * @return
	 */
	private static String parseTranslate(String text) {
		// \u003cSpan\u003e
		if (text != null) {
			LOG.info("parseTranslate:{}", text);
			text = text.replaceAll("\\[\\[\\[\"", "").replaceAll("\",\".*", "");
			return text.replaceAll("(\\\\u003c)", "＜").replaceAll("(\\\\u003e)", "＞");
		}
		return null;
	}

	/**
	 * 反解 base64編碼
	 * 
	 * @param input
	 * @return
	 */
	public static String decode64(String input) {
		if (Util.isNotEmpty(input)) {
			try {
				Decoder coder = Base64.getDecoder();
				return new String(coder.decode(input), UTF8);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return SysConfig.Empty;
	}

	/**
	 * base64編碼
	 * 
	 * @param str
	 * @return
	 */
	public static String encode64(String input) {
		if (Util.isNotEmpty(input)) {
			try {
				Encoder coder = Base64.getEncoder();
				return new String(coder.encode(input.getBytes(UTF8)), UTF8);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return SysConfig.Empty;
	}
}
