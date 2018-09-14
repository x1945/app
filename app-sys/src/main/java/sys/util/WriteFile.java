package sys.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * 寫檔 2013/08/08
 * 
 * @author Fantasy
 * 
 */

public class WriteFile {

	public WriteFile() {
		initWriteFile();
	}

	public void initWriteFile() {

	}

	/**
	 * 寫入文字檔(使用FileWriter 寫檔編碼為預設的iso-8859-1)，
	 * 因此此method使用OutputStreamWriter寫檔，可自行指定格式
	 * 
	 * @param text
	 *            將整個String寫入指定的檔案
	 * @param filename
	 *            可用相對路徑或絕對路徑
	 * @param format
	 *            寫入檔案的編碼格式
	 * @param append
	 *            true 將此次寫檔串在原本檔案最後面 | false 將此次寫檔蓋掉原本的文字檔內容
	 * @return true 寫檔成功 | false 寫檔失敗
	 */
	public static boolean writeText(String text, String filename, String format,
			boolean append) {
		if (text.equals("")) {
			return false;
		}
		// 建立檔案，準備寫檔
		File file = new File(filename);
		try {
			BufferedWriter bufWriter = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(file, append),
							format));
			bufWriter.write(text);
			bufWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(filename + "寫檔發生錯誤");
			return false;
		}
		return true;
	}

	/**
	 * 寫入檔案使用utf8格式寫檔，並且復蓋原本檔案內容
	 * 
	 * @param text
	 * @param filename
	 * @return
	 */
	public static boolean writeTextUTF8(String text, String filename) {
		return writeText(text, filename, "utf8", false);
	}

	/**
	 * 寫入檔案使用big5格式寫檔，並且復蓋原本檔案內容
	 * 
	 * @param text
	 * @param filename
	 * @return
	 */
	public static boolean writeTextBIG5(String text, String filename) {
		return writeText(text, filename, "big5", false);
	}

	/**
	 * 寫入檔案使用utf8格式寫檔，串在原本檔案內容後面
	 * 
	 * @param text
	 * @param filename
	 * @return
	 */
	public static boolean writeTextUTF8Apend(String text, String filename) {
		return writeText(text, filename, "utf8", true);
	}

	/**
	 * 寫入檔案使用big5格式寫檔，串在原本檔案內容後面
	 * 
	 * @param text
	 * @param filename
	 * @return
	 */
	public static boolean writeTextBIG5Apend(String text, String filename) {
		return writeText(text, filename, "big5", true);
	}

	/**
	 * 檢查檔案是否存在
	 * 
	 * @param filename
	 * @return true 檔案已存在 | false 檔案不存在
	 */
	public static boolean exists(String path) {
		return new File(path).exists();
	}

	/**
	 * 建立新檔(檔案已存在會刪除舊檔並建新檔)
	 * 
	 * @param path
	 */
	public static void createNewFile(String path) {
		try {
			File file = new File(path);
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 刪檔案
	 * 
	 * @param path
	 */
	public static void deleteFile(String path) {

		File file = new File(path);
		file.delete();

	}

	/**
	 * 建立資料夾(可建多層資料夾)
	 * 
	 * @param path
	 * @param 最後一層的資料夾
	 */
	public static String mkDir(String path) {
		int location = path.lastIndexOf("\\");
		System.out.println("location=" + location);
		if (location >= 0) {
			String paths = path.substring(0, location);
			String[] pathAry = paths.split("[/]|\\\\");

			StringBuffer list = new StringBuffer();
			for (int i = 0; i < pathAry.length; i++) {
				if (!pathAry[i].equals("")) {
					list.append(pathAry[i] + "/");
					File dir = new File(list.toString());
					if (!dir.isDirectory()) {
						dir.mkdir();
					}
				}
			}
			return list.toString();
		}
		return null;
	}

	/**
	 * 建立資料夾(可建多層資料夾)
	 * 
	 * @param path
	 * @param 最後一層的資料夾
	 */
	public static void write(String fileName, String text) {
		write(fileName, text, false);
	}

	public static void write(String fileName, String text, boolean isAppend) {
		// if (!exists(fileName)) {
		// mkDir(fileName);
		// createNewFile(fileName);
		// }
		if (isAppend) {
			writeTextUTF8Apend(text, fileName);
		} else {
			writeTextUTF8(text, fileName);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(WriteFile.exists("./bb.txt"));

	}

}
