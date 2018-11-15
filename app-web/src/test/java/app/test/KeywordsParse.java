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

	public static void main(String[] args) throws Exception {
		jiebaAnalysisTest.init();
		System.out.println("start");
		loadExcelKeywords("33_中興新村活化專案辦公室_測試用sample.xlsx");
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
			for (Row row : sheet) {
				int rowIndex = row.getRowNum();
				// 排除第一行tilte
				if (rowIndex > 0) {
					List<String> list = new ArrayList<String>();
					StringBuffer sb = new StringBuffer();
					// keywords
					String keywords = SvmUtil.getCellValue(row.getCell(3)).replaceAll("　", "、");
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
					// System.out.println(list);
					result.put(rowIndex, list);
					Cell cell = row.createCell(4);
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
