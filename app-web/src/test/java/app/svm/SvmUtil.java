package app.svm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import libsvm.svm_node;
import libsvm.svm_problem;

/**
 * SVM Util
 * 
 * @author AP4-Fantasy
 *
 */
public class SvmUtil {

	static final int KeyWordDepth = 70;
	static final int _max = 9999;
	/**
	 * 特殊字元(空格)
	 */
	private static final String specialString = new String(new byte[] { -17, -69, -65 });

	/**
	 * 去頭尾空白和特殊字完(空格)
	 * 
	 * @param input
	 * @return
	 */
	public static String trim(String input) {
		if (input == null)
			return "";
		return input.trim().replaceAll(specialString, "");
	}

	/**
	 * 分割字串
	 * 
	 * @param input
	 * @param sign
	 * @return
	 */
	public static String[] split(String input, String sign) {
		if (sign == null)
			return new String[] { input };
		return trim(input).split(sign);
	}

	/**
	 * 取得次數
	 * 
	 * @param input
	 * @param sign
	 * @return
	 */
	public static int frequency(String input, String s) {
		if (input == null || s == null || s.length() == 0)
			return 0;
		int result = -1, index = -1;
		do {
			index = input.indexOf(s, ++index);
			result++;
		} while (index >= 0);
		return result;
	}

	// ====================================================================================
	/**
	 * TF
	 * 
	 * @param contentMap
	 * @param keywords
	 * @return
	 */
	public static Map<String, Map<String, Double>> tf(Map<String, String> contentMap, Set<String> keywords) {
		Map<String, Map<String, Double>> result = new TreeMap<String, Map<String, Double>>();
		System.out.println("TF for Every file is :");
		for (String key : contentMap.keySet()) {
			Map<String, Double> dict = new TreeMap<String, Double>();
			dict = tf(contentMap.get(key), keywords);
			// System.out.println(key + " >> " + dict);
			result.put(key, dict);
		}
		return result;
	}

	/**
	 * TF
	 * 
	 * @param content
	 * @param keywords
	 * @return
	 */
	public static Map<String, Double> tf(String content, Set<String> keywords) {
		Map<String, Double> result = new TreeMap<String, Double>();
		Map<String, Integer> temp = new TreeMap<String, Integer>();
		int totalFrequency = 0;
		for (String word : keywords) {
			int frequency = frequency(content, word);
			totalFrequency += frequency;
			temp.put(word, frequency);
		}
		for (String word : keywords) {
			Double value = 0d;
			if (totalFrequency > 0)
				value = (double) temp.get(word) / totalFrequency;
			result.put(word, value);
		}
		return result;
	}

	/**
	 * IDF
	 * 
	 * @param contentMap
	 * @param keywords
	 * @return
	 */
	public static Map<String, Double> idf(Map<String, String> contentMap, Set<String> keywords) {
		Map<String, Double> result = new TreeMap<String, Double>();
		int docNum = contentMap.size();
		System.out.println("IDF for every word is:");
		for (String word : keywords) {
			int docCount = 1; // 不為0
			for (String content : contentMap.values()) {
				if (frequency(content, word) > 0)
					docCount++;
			}
			Double value = 0d;
			if (docCount > 0)
				value = Math.log((double) docNum / docCount);
			result.put(word, value);
		}
		return result;
	}

	/**
	 * TF-IDF
	 * 
	 * @param tfs
	 * @param idfs
	 * @param keywords
	 * @return
	 */
	public static Map<String, Map<String, Double>> tf_idf(Map<String, Map<String, Double>> tfs,
			Map<String, Double> idfs, Set<String> keywords) {
		Map<String, Map<String, Double>> result = new TreeMap<String, Map<String, Double>>();

		System.out.println("TF-IDF for Every file is :");
		for (String key : tfs.keySet()) {
			TreeMap<String, Double> tfidf = new TreeMap<String, Double>();
			Map<String, Double> temp = tfs.get(key);
			for (String word : keywords) {
				Double f1 = temp.get(word);
				if (f1.isNaN())
					f1 = 0d;
				Double f2 = idfs.get(word);
				if (f2.isNaN())
					f2 = 0d;
				Double value = f1 * f2;
				tfidf.put(word, value);
			}
			result.put(key, tfidf);
			// System.out.println(key + " >> " + tfidf);
		}
		return result;
	}

	/**
	 * TF-IDF降冪排序
	 * 
	 * @param tf_idfs
	 * @param keywords
	 * @return
	 */
	public static List<Map.Entry<String, Double>> tf_idf_sort(Map<String, Map<String, Double>> tf_idfs,
			Set<String> keywords) {
		Map<String, Double> temp = new TreeMap<String, Double>();
		for (String word : keywords) {
			Double f = 0d;
			for (Map<String, Double> fMap : tf_idfs.values()) {
				Double value = fMap.get(word);
				if (!value.isNaN())
					f = Double.sum(f, value);
			}
			temp.put(word, f);
		}
		// System.out.println(temp);
		return mapsort(temp);
	}

	public static List<Map.Entry<String, Double>> mapsort(Map<String, Double> map) {
		// 升序比較器
		Comparator<Map.Entry<String, Double>> valueComparator = new Comparator<Map.Entry<String, Double>>() {

			@Override
			public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		};
		// map轉換成list進行排序
		List<Map.Entry<String, Double>> list = new ArrayList<Map.Entry<String, Double>>(map.entrySet());
		// 排序
		Collections.sort(list, valueComparator);
		return list;
	}

	/**
	 * 取得CELL值
	 * 
	 * @param cell
	 * @return
	 */
	public static String getCellValue(Cell cell) {
		if (cell != null) {
			if (cell.getCellType() == CellType.NUMERIC) {
				return String.valueOf(cell.getNumericCellValue());
			} else {
				return cell.getStringCellValue();
			}
		}
		return "";
	}

	/**
	 * 將模型學習資料轉換為向量點
	 * 
	 * @param svmModel
	 * @return
	 */
	public static svm_problem parseSvmProblem(SvmModel svmModel) {
		svm_problem result = new svm_problem();
		Vector<Double> vy = new Vector<Double>();
		Vector<svm_node[]> vx = new Vector<svm_node[]>();

		Map<String, Map<String, String>> data = svmModel.getTrainData();
		for (String typeName : data.keySet()) {
			Map<String, String> contentMap = data.get(typeName);
			for (String key : contentMap.keySet()) {
				String content = contentMap.get(key);
				svm_node[] x = parseSvmNode(content, svmModel.getKeywords());
				vx.addElement(x);
				vy.addElement(svmModel.nameToLabel(typeName));
			}
		}

		result.l = vy.size();
		result.x = new svm_node[result.l][];
		for (int i = 0; i < result.l; i++)
			result.x[i] = vx.elementAt(i);
		result.y = new double[result.l];
		for (int i = 0; i < result.l; i++)
			result.y[i] = vy.elementAt(i);

		return result;
	}

	/**
	 * 將文章和關鍵字轉換為SVM向量點
	 * 
	 * @param content
	 * @param keywords
	 * @return
	 */
	public static svm_node[] parseSvmNode(String content, List<String> keywords) {
		int size = keywords.size();
		svm_node[] result = new svm_node[keywords.size()];
		for (int i = 0; i < size; i++) {
			String keyword = keywords.get(i);
			result[i] = new svm_node();
			result[i].index = i;
			result[i].value = frequency(content, keyword) > 0 ? 1 : 0;
		}
		return result;
	}

	/**
	 * 讀取EXCEL(學習&測試資料)
	 * 
	 * @param filePath
	 * @return
	 */
	public static Map<String, String> loadExcel(String filePath) {
		return loadExcel(filePath, 1, _max);
	}

	/**
	 * 讀取EXCEL(學習&測試資料)
	 * 
	 * @param filePath
	 * @param start
	 * @param end
	 * @return
	 */
	public static Map<String, String> loadExcel(String filePath, int start, int end) {
		Map<String, String> result = new TreeMap<String, String>();
		Workbook wb = null;
		InputStream in = null;
		File file = new File(filePath);
		try {
			in = new FileInputStream(file);
			wb = WorkbookFactory.create(in);
			Sheet sheet = wb.getSheetAt(0);
			for (Row row : sheet) {
				int rowIndex = row.getRowNum();
				// 排除第一行tilte
				if (rowIndex > 0) {
					if (rowIndex >= start && rowIndex <= end) {
						// id
						String id = trim(getCellValue(row.getCell(0)));
						// contents
						String content = trim(getCellValue(row.getCell(1)));
						result.put(id, content);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(in, wb);
		}
		return result;
	}

	/**
	 * 讀取關鍵字
	 * 
	 * @param filePath
	 * @return
	 */
	public static List<String> loadKeywords(String filePath) {
		List<String> result = new ArrayList<String>();
		Workbook wb = null;
		InputStream in = null;
		File file = new File(filePath);
		try {
			in = new FileInputStream(file);
			wb = WorkbookFactory.create(in);
			Sheet sheet = wb.getSheetAt(0);
			Map<Short, List<String>> map = new TreeMap<Short, List<String>>();
			for (Row row : sheet) {
				// 排除第一行
				if (row.getRowNum() > 0) {
					short firstNum = row.getFirstCellNum();
					short lastNum = row.getLastCellNum();
					for (short x = firstNum; x < lastNum; x++) {
						Cell cell = row.getCell(x);
						if (cell != null) {
							List<String> list = map.get(x);
							if (list == null) {
								list = new ArrayList<String>();
								map.put(x, list);
							}
							String value = trim(getCellValue(cell));
							list.add(value);
						}
					}
				}
			}

			for (List<String> list : map.values())
				result.addAll(list);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(in, wb);
		}
		return result;
	}

	/**
	 * 讀取EXCEL檔的關鍵字
	 * 
	 * @param filePath
	 * @return
	 */
	public static Set<String> loadExcelKeywords(String filePath) {
		Set<String> result = new HashSet<String>();
		Workbook wb = null;
		InputStream in = null;
		File file = new File(filePath);
		try {
			in = new FileInputStream(file);
			wb = WorkbookFactory.create(in);
			Sheet sheet = wb.getSheetAt(0);
			for (Row row : sheet) {
				int rowIndex = row.getRowNum();
				// 排除第一行tilte
				if (rowIndex > 0) {
					// keywords
					String[] keys = SvmUtil.split(SvmUtil.getCellValue(row.getCell(3)), "、");
					for (String key : keys) {
						if (SvmUtil.trim(key).length() > 0)
							result.add(key);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(in, wb);
		}
		return result;
	}

	/**
	 * 輸出結果
	 * 
	 * @param SvmModelList
	 * @param data
	 */
	public static void outputKeywords(SvmModel svmModel, Map<String, List<String>> data, int depth) {
		FileOutputStream fileOut = null;
		Workbook wb = null;
		try {
			// 設定檔案輸出串流到指定位置
			fileOut = new FileOutputStream(svmModel.path() + svmModel.keywordsFile);
			// 宣告XSSFWorkbook
			wb = new XSSFWorkbook();
			// 建立分頁
			Sheet sheet = wb.createSheet("keywords");
			sheet.createFreezePane(0, 1); // 凍結表格
			// 宣告列物件
			Row row = sheet.createRow(0);
			// 宣告表格物件
			Cell cell = null;
			int cellCount = 0, rowCount = 0;
			// title
			for (String type : svmModel.types()) {
				cell = row.createCell(cellCount++);
				cell.setCellValue(type);
			}

			for (int y = 0; y < depth; y++) {
				row = sheet.createRow(++rowCount);
				cellCount = 0;
				for (String type : svmModel.types()) {
					List<String> list = data.get(type);
					cell = row.createCell(cellCount++);
					cell.setCellValue(list.get(y));
				}
			}

			wb.write(fileOut);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (wb != null) {
					wb.close();
					wb = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (fileOut != null) {
					fileOut.close();
					fileOut = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 關閉
	 * 
	 * @param in
	 * @param wb
	 */
	public static void close(InputStream in, Workbook wb) {
		try {
			if (wb != null) {
				wb.close();
				wb = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			if (in != null) {
				in.close();
				in = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
