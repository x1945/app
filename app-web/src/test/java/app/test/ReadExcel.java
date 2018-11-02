package app.test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

public class ReadExcel {

	svm_parameter _param;
	String _model_file = "jcs_svm_model.txt";
	String[] files = { "人力發展處", "人事室", "主計室" };
	// String[] files = { "人事室", "主計室" };
	// String[] files = { "人力發展處" };

	public static void main(String[] args) throws IOException {
		System.out.println("start");
		ReadExcel readExcel = new ReadExcel();
		List<SvmModelData> list = readExcel.read(readExcel.files);
		// readExcel.output(list);
		readExcel.svm(list);
		System.out.println("end");
	}

	public ReadExcel() {
		// default values
		_param = new svm_parameter();

		_param.svm_type = svm_parameter.C_SVC;
		_param.kernel_type = svm_parameter.LINEAR;
		_param.degree = 3;
		_param.gamma = 1.0; // 1/num_features
		_param.coef0 = 0;
		_param.nu = 0.5;
		_param.cache_size = 100;
		_param.C = 1;
		_param.eps = 1e-3;
		_param.p = 0.1;
		_param.shrinking = 1;
		_param.probability = 0;
		_param.nr_weight = 0;
		_param.weight_label = new int[0];
		_param.weight = new double[0];
	}

	public List<SvmModelData> read(String[] files) {
		List<SvmModelData> result = new ArrayList<SvmModelData>();
		for (String file : files) {
			InputStream in = null;
			try {
				in = this.getClass().getClassLoader().getResourceAsStream(file + ".xlsx");
				SvmModelData smd = read(in);
				smd.setLabel(file);
				smd.setName(file);
				System.out.println("--------------------------------");
				System.out.println(smd.getKeywords());
				System.out.println(smd.getKeywords().size());
				System.out.println("--------------------------------");
				Map<String, Map<String, Float>> tfs = SvmUtil.tf(smd.getContentMap(), smd.getKeywords());
				System.out.println("--------------------------------");
				Map<String, Float> idfs = SvmUtil.idf(smd.getContentMap(), smd.getKeywords());
				System.out.println("--------------------------------");
				Map<String, Map<String, Float>> tfidfs = SvmUtil.tf_idf(tfs, idfs, smd.getKeywords());
				smd.setTfidfs(tfidfs);
				System.out.println("--------------------------------");
				List<Map.Entry<String, Float>> list = SvmUtil.tf_idf_sort(tfidfs, smd.getKeywords());
				smd.setKeyList(list);
				System.out.println("--------------------------------");
				// output(tfidfs, smd.getKeywords());
				// output(tfidfs, list);
				System.out.println("--------------------------------");
				result.add(smd);

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (in != null) {
						in.close();
						in = null;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public SvmModelData read(InputStream in) {
		SvmModelData result = new SvmModelData();

		Set<String> keywords = new HashSet<String>();
		Map<String, String> contentMap = new TreeMap<String, String>();
		Workbook wb = null;
		try {
			wb = WorkbookFactory.create(in);
			Sheet sheet = wb.getSheetAt(0);
			for (Row row : sheet) {
				// 排除第一行tilte
				if (row.getRowNum() > 0) {
					// id
					String id = SvmUtil.trim(row.getCell(0).getStringCellValue());
					// contents
					contentMap.put(id, SvmUtil.trim(row.getCell(1).getStringCellValue()));
					// keywords
					String[] keys = SvmUtil.split(row.getCell(3).getStringCellValue(), "、");
					for (String key : keys)
						keywords.add(key);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (wb != null) {
					wb.close();
					wb = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		result.setKeywords(keywords);
		result.setContentMap(contentMap);
		return result;
	}

	private class SvmModelData {

		Double label = 0d;

		String name;

		Set<String> keywords = new HashSet<String>();

		Map<String, String> contentMap = new TreeMap<String, String>();

		Map<String, Map<String, Float>> tfidfs = null;

		List<Map.Entry<String, Float>> keyList = null;

		public Double getLabel() {
			return label;
		}

		public void setLabel(Double label) {
			this.label = label;
		}

		public void setLabel(String name) {
			switch (name) {
			case "人力發展處":
				setLabel(1d);
				break;
			case "人事室":
				setLabel(2d);
				break;
			case "主計室":
				setLabel(3d);
				break;
			}
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Set<String> getKeywords() {
			return keywords;
		}

		public void setKeywords(Set<String> keywords) {
			this.keywords = keywords;
		}

		public Map<String, String> getContentMap() {
			return contentMap;
		}

		public void setContentMap(Map<String, String> contentMap) {
			this.contentMap = contentMap;
		}

		public Map<String, Map<String, Float>> getTfidfs() {
			return tfidfs;
		}

		public void setTfidfs(Map<String, Map<String, Float>> tfidfs) {
			this.tfidfs = tfidfs;
		}

		public List<Map.Entry<String, Float>> getKeyList() {
			return keyList;
		}

		public void setKeyList(List<Map.Entry<String, Float>> keyList) {
			this.keyList = keyList;
		}

	}

	public void output(List<SvmModelData> SvmModelList) {
		System.out.println("匯出Excel Start");
		FileOutputStream fileOut = null;
		Workbook wb = null;
		try {
			// 設定檔案輸出串流到指定位置
			fileOut = new FileOutputStream("report.xlsx");
			// 宣告XSSFWorkbook
			wb = new XSSFWorkbook();
			// 建立分頁
			for (SvmModelData smd : SvmModelList) {
				Map<String, Map<String, Float>> map = smd.getTfidfs();
				List<Map.Entry<String, Float>> list = smd.getKeyList();
				System.out.println("Sheet:" + smd.getName());
				Sheet sheet = wb.createSheet(smd.getName());
				sheet.createFreezePane(0, 1); // 凍結表格
				// 宣告列物件
				Row row = sheet.createRow(0);
				// 宣告表格物件
				Cell cell = null;
				int cellCount = 0, rowCount = 0;
				// title
				for (Map.Entry<String, Float> keyMap : list) {
					cell = row.createCell(++cellCount);
					cell.setCellValue(keyMap.getKey());
				}
				// data
				for (String key : map.keySet()) {
					Map<String, Float> fMap = map.get(key);
					// doc name
					row = sheet.createRow(++rowCount);
					cellCount = 0;
					cell = row.createCell(cellCount);
					cell.setCellValue(key);
					//
					for (Map.Entry<String, Float> keyMap : list) {
						String word = keyMap.getKey();
						cell = row.createCell(++cellCount);
						cell.setCellValue(fMap.get(word));
					}
				}
				// total
				cellCount = 0;
				row = sheet.createRow(++rowCount);
				cell = row.createCell(cellCount);
				cell.setCellValue("total");
				for (Map.Entry<String, Float> keyMap : list) {
					cell = row.createCell(++cellCount);
					cell.setCellValue(keyMap.getValue());
				}
			}
			// fileOut.close();
			wb.write(fileOut);
			// wb.close();
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
			System.out.println("匯出Excel End");
		}
	}

	public void outputX(List<SvmModelData> SvmModelList) {
		System.out.println("匯出Excel Start");
		FileOutputStream fileOut = null;
		Workbook wb = null;
		try {
			// 設定檔案輸出串流到指定位置
			fileOut = new FileOutputStream("report.xlsx");
			// 宣告XSSFWorkbook
			wb = new XSSFWorkbook();
			// 建立分頁
			for (SvmModelData smd : SvmModelList) {
				Map<String, Map<String, Float>> map = smd.getTfidfs();
				List<Map.Entry<String, Float>> list = smd.getKeyList();
				System.out.println("Sheet:" + smd.getName());
				Sheet sheet = wb.createSheet(smd.getName());
				sheet.createFreezePane(1, 0); // 凍結表格
				// 宣告列物件
				Row row = sheet.createRow(0);
				// 宣告表格物件
				Cell cell = null;
				int cellCount = 0, rowCount = 0;
				// title
				for (String key : map.keySet()) {
					cell = row.createCell(++cellCount);
					cell.setCellValue(key);
				}
				cell = row.createCell(++cellCount);
				cell.setCellValue("total");
				//
				for (Map.Entry<String, Float> keyMap : list) {
					String word = keyMap.getKey();
					// word
					row = sheet.createRow(++rowCount);
					cellCount = 0;
					cell = row.createCell(cellCount);
					cell.setCellValue(word);
					//
					for (String key : map.keySet()) {
						Map<String, Float> fMap = map.get(key);
						cell = row.createCell(++cellCount);
						cell.setCellValue(fMap.get(word));
					}
					cell = row.createCell(++cellCount);
					cell.setCellValue(keyMap.getValue());
				}
			}
			// fileOut.close();
			wb.write(fileOut);
			// wb.close();
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
			System.out.println("匯出Excel End");
		}
	}

	/**
	 * 輸出之Excel
	 * 
	 * @param map
	 * @param keywords
	 */
	public void output(Map<String, Map<String, Float>> map, List<Map.Entry<String, Float>> list) {
		System.out.println("匯出Excel Start");
		FileOutputStream fileOut = null;
		Workbook wb = null;
		try {
			// 設定檔案輸出串流到指定位置
			fileOut = new FileOutputStream("report.xlsx");
			// 宣告XSSFWorkbook
			wb = new XSSFWorkbook();
			// 建立分頁
			Sheet sheet = wb.createSheet("工作表");
			sheet.createFreezePane(1, 0); // 凍結表格
			// 宣告列物件
			Row row = sheet.createRow(0);
			// 宣告表格物件
			Cell cell = null;
			int cellCount = 0, rowCount = 0;
			// title
			for (String key : map.keySet()) {
				cell = row.createCell(++cellCount);
				cell.setCellValue(key);
			}
			cell = row.createCell(++cellCount);
			cell.setCellValue("total");
			//
			for (Map.Entry<String, Float> keyMap : list) {
				String word = keyMap.getKey();
				// word
				row = sheet.createRow(++rowCount);
				cellCount = 0;
				cell = row.createCell(cellCount);
				cell.setCellValue(word);
				//
				for (String key : map.keySet()) {
					Map<String, Float> fMap = map.get(key);
					cell = row.createCell(++cellCount);
					cell.setCellValue(fMap.get(word));
				}
				cell = row.createCell(++cellCount);
				cell.setCellValue(keyMap.getValue());
			}
			// fileOut.close();
			wb.write(fileOut);
			// wb.close();
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
			System.out.println("匯出Excel End");
		}
	}

	/**
	 * 輸出之Excel
	 * 
	 * @param map
	 * @param keywords
	 */
	public void output(Map<String, Map<String, Float>> map, Set<String> keywords) {
		System.out.println("匯出Excel Start");
		FileOutputStream fileOut = null;
		Workbook wb = null;
		try {
			// 設定檔案輸出串流到指定位置
			fileOut = new FileOutputStream("report.xlsx");
			// 宣告XSSFWorkbook
			wb = new XSSFWorkbook();
			// 建立分頁
			Sheet sheet = wb.createSheet("工作表");
			// 宣告列物件
			Row row = sheet.createRow(0);
			// 宣告表格物件
			Cell cell = null;
			int cellCount = 0, rowCount = 0;
			// title
			for (String key : map.keySet()) {
				cell = row.createCell(++cellCount);
				cell.setCellValue(key);
			}
			//
			for (String word : keywords) {
				// word
				row = sheet.createRow(++rowCount);
				cellCount = 0;
				cell = row.createCell(cellCount);
				cell.setCellValue(word);
				//
				for (String key : map.keySet()) {
					Map<String, Float> fMap = map.get(key);
					cell = row.createCell(++cellCount);
					cell.setCellValue(fMap.get(word));
				}
			}
			// fileOut.close();
			wb.write(fileOut);
			// wb.close();
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
			System.out.println("匯出Excel End");
		}
	}

	/**
	 * 
	 * @param SvmModelList
	 */
	public void svm(List<SvmModelData> SvmModelList) {
		List<String> keywords = getKeyWords(SvmModelList, 30);
		System.out.println("keywords.size():" + keywords.size());
		System.out.println(keywords);

		svm_problem _problem = parseVector(SvmModelList, keywords);
		training(_problem);
		testing(_problem);
	}

	public List<String> getKeyWords(List<SvmModelData> SvmModelList, int d) {
		List<String> result = new ArrayList<String>();
		for (SvmModelData smd : SvmModelList) {
			List<Map.Entry<String, Float>> list = smd.getKeyList();
			int count = 1, index = 0, size = list.size();
			while (count <= d && index < size) {
				Map.Entry<String, Float> keyMap = list.get(index++);
				String key = keyMap.getKey();
				if (!result.contains(key)) {
					System.out.println(smd.getName() + ":" + key + "[" + keyMap.getValue() + "][" + count + "]");
					result.add(key);
					count++;
				}
			}
			System.out.println("---------------------------------------");
		}
		return result;
	}

	public svm_problem parseVector(List<SvmModelData> SvmModelList, List<String> keywords) {
		svm_problem _prob = new svm_problem();
		Vector<Double> vy = new Vector<Double>();
		Vector<svm_node[]> vx = new Vector<svm_node[]>();

		int size = keywords.size();
		for (SvmModelData smd : SvmModelList) {
			Map<String, String> contentMap = smd.getContentMap();
			for (String content : contentMap.values()) {
				svm_node[] x = new svm_node[size];
				for (int i = 0; i < size; i++) {
					String keyword = keywords.get(i);
					x[i] = new svm_node();
					x[i].index = i;
					x[i].value = SvmUtil.frequency(content, keyword) > 0 ? 1 : 0;
				}
				vx.addElement(x);
				vy.addElement(smd.getLabel());
			}
			System.out.println("---------------------------------------");
		}

		_prob.l = vy.size();
		System.out.println("Vector.size = " + _prob.l);
		_prob.x = new svm_node[_prob.l][];
		for (int i = 0; i < _prob.l; i++)
			_prob.x[i] = vx.elementAt(i);
		_prob.y = new double[_prob.l];
		for (int i = 0; i < _prob.l; i++)
			_prob.y[i] = vy.elementAt(i);

		return _prob;
	}

	public void training(svm_problem _prob) {
		System.out.print("Training...");
		try {
			svm_model model = svm.svm_train(_prob, _param);
			System.out.println("Done!!");
			svm.svm_save_model(_model_file, model);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testing(svm_problem _prob) {
		System.out.println("Training...");

		svm_model model;
		int correct = 0, total = 0;
		try {
			model = svm.svm_load_model(_model_file);
			//
			int modelLength = model.label.length;
			int modelCount[] = new int[modelLength];
			int modelTotal[] = new int[modelLength];
			for (int i = 0; i < modelLength; i++) {
				modelCount[i] = 0;
				modelTotal[i] = 0;
			}

			//
			for (int i = 0; i < _prob.l; i++) {
				double v;
				svm_node[] x = _prob.x[i];
				v = svm.svm_predict(model, x);
				total++;
				// System.out.println(i + ". [" + _prob.y[i] + "], v[" + v + "]");
				if (v == _prob.y[i])
					correct++;

				//
				for (int j = 0; j < modelLength; j++) {
					if (model.label[j] == _prob.y[i]) {
						modelTotal[j]++;
						if (v == _prob.y[i])
							modelCount[j]++;
					}
				}
			}

			double accuracy = (double) correct / total * 100;
			System.out.println("Total Accuracy = " + accuracy + "% (" + correct + "/" + total + ")");
			for (int i = 0; i < modelLength; i++) {
				accuracy = (double) modelCount[i] / modelTotal[i] * 100;
				System.out.println(files[i] + " Accuracy = " + accuracy + "% (" + modelCount[i] + "/"
						+ modelTotal[i] + ")");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
