package app.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import app.svm.SvmUtil;

public class KeywordsParse {

	// static String[] files = { "人力發展處", "人事室", "主計室", "經發處", "資管處", "管考處" };
	static String[] files = { "08_綜合規劃處_測試建模用sample", "9A_檔管局_測試建模用sample", "10_產發處_測試建模用sample",
			"12_國土區域離島發展處_測試建模用sample", "13_社會發展處_測試建模用sample", "18_秘書室_測試建模用sample", "19_政風室_測試建模用sample",
			"20_法協中心_測試建模用sample", "30_媒體溝通小組_測試建模用sample", "32_國會及新聞聯絡中心_測試建模用sample" };

	public static void main(String[] args) throws Exception {
		jiebaAnalysisTest.init();
		System.out.println("start");
		// loadExcelKeywords("33_中興新村活化專案辦公室_測試用sample.xlsx");

		for (String file : files) {
			String filePath = "d:\\jieba\\" + file + ".xlsx";
			System.out.println(filePath);
			loadExcelKeywords(filePath);
		}

		System.out.println("end");
	}

	/**
	 * 讀取EXCEL檔的關鍵字
	 * 
	 * @param filePath
	 * @return
	 */
	public static Map<Integer, List<String>> loadExcelKeywords(String filePath) {
		Map<Integer, List<String>> result = new HashMap<Integer, List<String>>();
		Workbook wb = null;
		FileOutputStream fileOut = null;
		InputStream in = null;
		File file = new File(filePath);
		try {
			in = new FileInputStream(file);
			wb = WorkbookFactory.create(in);
			close(in);

			fileOut = new FileOutputStream(file);
			Sheet sheet = wb.getSheetAt(0);

			CellStyle style = wb.createCellStyle();
			style.setWrapText(true);
			style.setVerticalAlignment(VerticalAlignment.TOP);
			// sheet.setColumnWidth(columnIndex, width);
			// sheet.autoSizeColumn(4);
			// sheet.autoSizeColumn(5);
			// sheet.autoSizeColumn(6);
			// System.out.println(sheet.getDefaultColumnWidth());
			// sheet.setColumnWidth(4, 20);	
			sheet.setColumnWidth(4, 8000);
			sheet.setColumnWidth(5, 8000);
			sheet.setColumnWidth(6, 8000);
			sheet.setColumnWidth(7, 8000);

			Row titleRow = sheet.getRow(0);
			Cell cell = titleRow.createCell(4);
			cell.setCellValue("關鍵字");
			cell.setCellStyle(style);
			//
			cell = titleRow.createCell(5);
			cell.setCellValue("jieba斷詞");
			cell.setCellStyle(style);
			//
			cell = titleRow.createCell(6);
			cell.setCellValue("jieba關鍵字");
			cell.setCellStyle(style);
			//
			cell = titleRow.createCell(7);
			cell.setCellValue("jieba關鍵字(排除1個字)");
			cell.setCellStyle(style);
			
			
			for (Row row : sheet) {
				int rowIndex = row.getRowNum();
				// 排除第一行tilte
				if (rowIndex > 0) {
					List<String> list = new ArrayList<String>();
					StringBuffer sb = new StringBuffer();
					// keywords
					String keywords = SvmUtil.getCellValue(row.getCell(2)).replaceAll("　", "、");
					// System.out.println(keywords);
					String[] keys = SvmUtil.split(keywords, "、");
					for (String key : keys) {
						if (SvmUtil.trim(key).length() > 0) {
							String s = key.replaceAll("\\(.*", "");
							if (jiebaAnalysisTest.filter(s)) {
								list.add(s);
								if (sb.length() > 0)
									sb.append("、");
								sb.append(s);
							}
						}
					}
//					 System.out.println(list);
					result.put(rowIndex, list);
					cell = row.createCell(4);
					cell.setCellValue(sb.toString());
					cell.setCellStyle(style);

					// content
					String content = SvmUtil.getCellValue(row.getCell(1));
					//
					String content1 = jiebaAnalysisTest.jiebaAnalysis(content);
					cell = row.createCell(5);
					cell.setCellValue(content1);
					cell.setCellStyle(style);
					//
					String content2 = jiebaAnalysisTest.jiebaAnalysis(content, true);
					cell = row.createCell(6);
					cell.setCellValue(content2);
					cell.setCellStyle(style);
					//
					String content3 = jiebaAnalysisTest.jiebaAnalysis(content, true, 2);
					cell = row.createCell(7);
					cell.setCellValue(content3);
					cell.setCellStyle(style);
				}
			}

			// 回寫
			wb.write(fileOut);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(wb);
			close(fileOut);
		}
		return result;
	}

	/**
	 * 關閉
	 * 
	 * @param in
	 * @param wb
	 */
	public static void close(InputStream in) {
		try {
			if (in != null) {
				in.close();
				in = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void close(Workbook wb) {
		try {
			if (wb != null) {
				wb.close();
				wb = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void close(FileOutputStream fileOut) {
		try {
			if (fileOut != null) {
				fileOut.close();
				fileOut = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
