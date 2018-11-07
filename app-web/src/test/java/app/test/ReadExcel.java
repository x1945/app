package app.test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
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
	// String[] files = { "人力發展處", "人事室", "主計室" };
	// String[] files = { "人力發展處" };
//	String[] files = { "人力發展處", "人事室", "主計室", "經發處", "資管處", "管考處" };
	// String[] files = { "人事室", "主計室" };
	 String[] files = { "主計室" };

	Vector<String> vs = new Vector<String>();
	Map<String, List<String>> KeyWordMap = new HashMap<String, List<String>>();

	public static void main(String[] args) throws IOException {
		System.out.println("start");
		SvmUtil.init();
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
		_param.shrinking = 1; // default 1
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
				smd.setLabel(SvmUtil.nameToLabel(file));
				smd.setName(file);
				System.out.println("--------------------------------0");
				System.out.println(smd.getKeywords());
				System.out.println(smd.getKeywords().size());
				System.out.println(smd.getContentMap().size());

				System.out.println("--------------------------------1");
				Map<String, Map<String, Double>> tfs = SvmUtil.tf(smd.getContentMap(), smd.getKeywords());
				System.out.println("--------------------------------2");
				Map<String, Double> idfs = SvmUtil.idf(smd.getContentMap(), smd.getKeywords(), smd.getName());
				System.out.println("--------------------------------3");
				Map<String, Map<String, Double>> tfidfs = SvmUtil.tf_idf(tfs, idfs, smd.getKeywords());
				smd.setTfidfs(tfidfs);
				System.out.println("--------------------------------4");
				List<Map.Entry<String, Double>> list = SvmUtil.tf_idf_sort(tfidfs, smd.getKeywords());
				smd.setKeyList(list);
				System.out.println("--------------------------------5");
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
		Map<String, String> testContentMap = new TreeMap<String, String>();
		Workbook wb = null;
		try {
			wb = WorkbookFactory.create(in);
			Sheet sheet = wb.getSheetAt(0);
			for (Row row : sheet) {
				// 排除第一行tilte
				if (row.getRowNum() > 0) {
					// id
					String id = SvmUtil.trim(SvmUtil.getCellValue(row.getCell(0)));
					// contents
					String content = SvmUtil.trim(SvmUtil.getCellValue(row.getCell(1)));
					if (row.getRowNum() <= SvmUtil.max) {
						contentMap.put(id, content);
					} else {
						testContentMap.put(id, content);
					}
					// keywords
					String[] keys = SvmUtil.split(SvmUtil.getCellValue(row.getCell(3)), "、");
					for (String key : keys) {
						if (SvmUtil.trim(key).length() > 0)
							keywords.add(key);
					}
					// keywords
					// if (keywords.size() <= 500)
					// keywords.addAll(SvmUtil.jiebaAnalysis(content));
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
		result.setTestContentMap(testContentMap);
		return result;
	}

	private class SvmModelData {

		Double label = 0d;

		String name;

		Set<String> keywords = new HashSet<String>();

		Map<String, String> contentMap = new TreeMap<String, String>();

		Map<String, String> testContentMap = new TreeMap<String, String>();

		Map<String, Map<String, Double>> tfidfs = null;

		List<Map.Entry<String, Double>> keyList = null;

		public Double getLabel() {
			return label;
		}

		public void setLabel(Double label) {
			this.label = label;
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

		public Map<String, Map<String, Double>> getTfidfs() {
			return tfidfs;
		}

		public void setTfidfs(Map<String, Map<String, Double>> tfidfs) {
			this.tfidfs = tfidfs;
		}

		public List<Map.Entry<String, Double>> getKeyList() {
			return keyList;
		}

		public void setKeyList(List<Map.Entry<String, Double>> keyList) {
			this.keyList = keyList;
		}

		public Map<String, String> getTestContentMap() {
			return testContentMap;
		}

		public void setTestContentMap(Map<String, String> testContentMap) {
			this.testContentMap = testContentMap;
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
				Map<String, Map<String, Double>> map = smd.getTfidfs();
				List<Map.Entry<String, Double>> list = smd.getKeyList();
				System.out.println("Sheet:" + smd.getName());
				Sheet sheet = wb.createSheet(smd.getName());
				sheet.createFreezePane(0, 1); // 凍結表格
				// 宣告列物件
				Row row = sheet.createRow(0);
				// 宣告表格物件
				Cell cell = null;
				int cellCount = 0, rowCount = 0;
				// title
				for (Map.Entry<String, Double> keyMap : list) {
					cell = row.createCell(++cellCount);
					cell.setCellValue(keyMap.getKey());
				}
				// data
				for (String key : map.keySet()) {
					Map<String, Double> fMap = map.get(key);
					// doc name
					row = sheet.createRow(++rowCount);
					cellCount = 0;
					cell = row.createCell(cellCount);
					cell.setCellValue(key);
					//
					for (Map.Entry<String, Double> keyMap : list) {
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
				for (Map.Entry<String, Double> keyMap : list) {
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
	 * 
	 * @param SvmModelList
	 */
	public void svm(List<SvmModelData> SvmModelList) {
		List<String> keywords = getKeyWords(SvmModelList, SvmUtil.KeyWordDepth);
		System.out.println("keywords.size():" + keywords.size());
		System.out.println(keywords);

		svm_problem _problem = parseVector(SvmModelList, keywords);
		// 訓練
		training(_problem);
		// // 測試
		// Map<String, Map<String, String>> testResultData = testing(_problem);
		Map<String, Map<String, String>> testResultData = testing(SvmModelList, keywords);
		// 輸出結果
		outputResult(SvmModelList, testResultData);
	}

	public List<String> getKeyWords(List<SvmModelData> SvmModelList, int d) {
		List<String> result = new ArrayList<String>();

		// 排除出現在其他類別的關鍵字
		Map<String, Set<String>> ignoreMap = new HashMap<String, Set<String>>();
		for (String file : files) {
			Set<String> ignoreSet = new HashSet<String>();
			for (SvmModelData smd : SvmModelList) {
				if (!file.equals(smd.getName())) {
					for (String key : smd.getKeywords())
						ignoreSet.add(key);
				}
			}
			ignoreMap.put(file, ignoreSet);
		}

		for (SvmModelData smd : SvmModelList) {
			Set<String> ignoreSet = ignoreMap.get(smd.getName());
			List<String> KeyWordList = new ArrayList<String>();
			List<Map.Entry<String, Double>> list = smd.getKeyList();
			int count = 1, index = 0, size = list.size();
			while (count <= d && index < size) {
				Map.Entry<String, Double> keyMap = list.get(index++);
				String key = keyMap.getKey();
//				if (!result.contains(key) && !ignoreSet.contains(key)) {
				if (!result.contains(key) && !ignoreSet.contains(key)) {
					System.out.println(smd.getName() + ":" + key + "[" + keyMap.getValue() + "][" + count + "]");
					result.add(key);
					KeyWordList.add(key);
					count++;
				}
			}
			KeyWordMap.put(smd.getName(), KeyWordList);
			System.out.println("---------------------------------------");
		}
		return result;
	}

	public svm_problem parseVector(List<SvmModelData> SvmModelList, List<String> keywords) {
		svm_problem _prob = new svm_problem();
		vs = new Vector<String>();
		Vector<Double> vy = new Vector<Double>();
		Vector<svm_node[]> vx = new Vector<svm_node[]>();

		for (SvmModelData smd : SvmModelList) {
			Map<String, String> contentMap = new TreeMap<String, String>();
			for (String key : smd.getContentMap().keySet()) {
				contentMap.put(key, smd.getContentMap().get(key));
			}
//			for (String key : smd.getTestContentMap().keySet()) {
//				contentMap.put(key, smd.getTestContentMap().get(key));
//			}
			for (String key : contentMap.keySet()) {
				String content = contentMap.get(key);
				svm_node[] x = SvmUtil.parseSvmNode(content, keywords);
				vx.addElement(x);
				vy.addElement(smd.getLabel());
				vs.addElement(key);
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

	public Map<String, Map<String, String>> testing(svm_problem _prob) {
		System.out.println("Testing...");
		Map<String, Map<String, String>> result = new HashMap<String, Map<String, String>>();

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
				result.put(SvmUtil.labelToName(model.label[i]), new TreeMap<String, String>());
			}

			//
			for (int i = 0; i < _prob.l; i++) {
				svm_node[] x = _prob.x[i];
				double v = svm.svm_predict(model, x);
				total++;
				// System.out.println(i + ". [" + _prob.y[i] + "], v[" + v + "]");
				if (v == _prob.y[i])
					correct++;
				//
				for (int j = 0; j < modelLength; j++) {
					if (model.label[j] == _prob.y[i]) {
						modelTotal[j]++;
						if (v == _prob.y[i]) {
							modelCount[j]++;
						} else {
							String contentId = vs.elementAt(i);
							// System.out.println(SvmUtil.labelToName(_prob.y[i]) + "[" + contentId + "] >> " + SvmUtil
							// .labelToName(v));
							result.get(SvmUtil.labelToName(_prob.y[i])).put(contentId, SvmUtil.labelToName(v));
						}
					}
				}
			}

			double accuracy = (double) correct / total * 100;
			System.out.println("Total Accuracy = " + accuracy + "% (" + correct + "/" + total + ")");
			for (int i = 0; i < modelLength; i++) {
				accuracy = (double) modelCount[i] / modelTotal[i] * 100;
				System.out.println(SvmUtil.labelToName(model.label[i]) + " Accuracy = " + accuracy + "% ("
						+ modelCount[i] + "/"
						+ modelTotal[i] + ")");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public Map<String, Map<String, String>> testing(List<SvmModelData> SvmModelList, List<String> keywords) {
		System.out.println("Testing...");
		Map<String, Map<String, String>> result = new HashMap<String, Map<String, String>>();

		svm_model model;
		int correct = 0, total = 0;
		try {
			model = svm.svm_load_model(_model_file);

			for (SvmModelData smd : SvmModelList) {
				Map<String, String> errorMap = new TreeMap<String, String>();
				int modelCount = 0, modelTotal = 0;

				
				Map<String, String> contentMap = new TreeMap<String, String>();
				for (String key : smd.getContentMap().keySet()) {
					contentMap.put(key, smd.getContentMap().get(key));
				}
//				for (String key : smd.getTestContentMap().keySet()) {
//					contentMap.put(key, smd.getTestContentMap().get(key));
//				}

				for (String key : contentMap.keySet()) {
					total++;
					modelTotal++;
					String content = contentMap.get(key);
					svm_node[] x = SvmUtil.parseSvmNode(content, keywords);
					double v = svm.svm_predict(model, x);
					if (v == smd.getLabel()) {
						correct++;
						modelCount++;
					} else {
						errorMap.put(key, SvmUtil.labelToName(v));
					}
				}
				result.put(smd.getName(), errorMap);
				double accuracy = (double) modelCount / modelTotal * 100;
				System.out.println(smd.getName() + " Accuracy = " + accuracy + "% (" + modelCount + "/" + modelTotal
						+ ")");
			}

			double accuracy = (double) correct / total * 100;
			System.out.println("Total Accuracy = " + accuracy + "% (" + correct + "/" + total + ")");

		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 輸出結果
	 * 
	 * @param SvmModelList
	 * @param data
	 */
	public void outputResult(List<SvmModelData> SvmModelList, Map<String, Map<String, String>> data) {
		System.out.println("匯出Excel Start");
		FileOutputStream fileOut = null;
		Workbook wb = null;
		try {
			// 設定檔案輸出串流到指定位置
			fileOut = new FileOutputStream("result.xlsx");
			// 宣告XSSFWorkbook
			wb = new XSSFWorkbook();
			// 建立分頁
			for (String sheetName : data.keySet()) {
				System.out.println("Sheet:" + sheetName);
				Sheet sheet = wb.createSheet(sheetName);
				sheet.createFreezePane(0, 1); // 凍結表格
				// 宣告列物件
				Row row = sheet.createRow(0);
				// 宣告表格物件
				Cell cell = null;
				int cellCount = 0, rowCount = 0;
				// title
				String[] titles = { "文件ID", "文件內容", "SVM判定" };
				for (String title : titles) {
					cell = row.createCell(cellCount++);
					cell.setCellValue(title);
				}

				for (SvmModelData smd : SvmModelList) {
					if (sheetName.equals(smd.getName())) {
						Map<String, String> dataMap = data.get(sheetName);
						for (String id : dataMap.keySet()) {
							row = sheet.createRow(++rowCount);
							cell = row.createCell(0);
							cell.setCellValue(id);
							cell = row.createCell(1);
							cell.setCellValue(smd.getContentMap().get(id));
							cell = row.createCell(2);
							cell.setCellValue(dataMap.get(id));
						}
					}
				}
			}

			// SVM建模關鍵字
			Sheet sheet = wb.createSheet("關鍵字" + SvmUtil.KeyWordDepth);
			sheet.createFreezePane(1, 0); // 凍結表格
			int rowCount = 0;
			for (String key : KeyWordMap.keySet()) {
				int cellCount = 0;
				Row row = sheet.createRow(rowCount++);
				Cell cell = row.createCell(cellCount++);
				cell.setCellValue(key);

				List<String> list = KeyWordMap.get(key);
				for (String s : list) {
					cell = row.createCell(cellCount++);
					cell.setCellValue(s);
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
			System.out.println("匯出Excel End");
		}
	}
}
