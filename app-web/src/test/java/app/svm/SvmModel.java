package app.svm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

/**
 * SVM Model
 * 
 * @author AP4-Fantasy
 *
 */
public abstract class SvmModel {

	final String keywordsFile = "keywords.xlsx";
	final String svmModelFile = "svm_model.txt";

	//
	Map<Double, String> L2N = new HashMap<Double, String>();
	Map<String, Double> N2L = new HashMap<String, Double>();

	svm_parameter param;
	Map<String, Map<String, String>> trainData;
	Map<String, Map<String, String>> testData;
	List<String> keywords;

	/**
	 * 建構子
	 */
	SvmModel() {
		// param default values
		if (param == null) {
			param = new svm_parameter();
			param.svm_type = svm_parameter.C_SVC;
			param.kernel_type = svm_parameter.LINEAR;
			param.degree = 3;
			param.gamma = 1.0; // 1/num_features
			param.coef0 = 0;
			param.nu = 0.5;
			param.cache_size = 200;
			param.C = 1;
			param.eps = 1e-3;
			param.p = 0.1;
			param.shrinking = 1; // default 1
			param.probability = 0;
			param.nr_weight = 0;
			param.weight_label = new int[0];
			param.weight = new double[0];
		}
		// parse types
		int size = 0;
		for (String type : types()) {
			L2N.put((double) ++size, type);
			N2L.put(type, (double) size);
		}
	}

	// get & set =================================================================

	public Map<String, Map<String, String>> getTrainData() {
		return trainData;
	}

	public svm_parameter getParam() {
		return param;
	}

	public void setParam(svm_parameter param) {
		this.param = param;
	}

	public void setTrainData(Map<String, Map<String, String>> trainData) {
		this.trainData = trainData;
	}

	public Map<String, Map<String, String>> getTestData() {
		return testData;
	}

	public void setTestData(Map<String, Map<String, String>> testData) {
		this.testData = testData;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	// method =================================================================

	public String name() {
		return this.getClass().getSimpleName();
	};

	public double nameToLabel(String name) {
		return N2L.get(name);
	}

	public String labelToName(double v) {
		return L2N.get(v);
	}

	/**
	 * 依TF-IDF解析關鍵字
	 */
	public void parseKeywords(int depth) {
		System.out.println("TF-IDF解析關鍵字開始...");
		// data
		if (trainData == null) {
			trainData = new HashMap<String, Map<String, String>>();
			for (String type : types()) {
				Map<String, String> map = SvmUtil.loadExcel(path() + type + ".xlsx", 1, 300);
				trainData.put(type, map);
			}
		}
		// parseKeywords
		Map<String, List<String>> keywordMap = new HashMap<String, List<String>>();
		for (String type : types()) {
			System.out.println("type:" + type);
			Set<String> set = SvmUtil.loadExcelKeywords(path() + type + ".xlsx");
			Map<String, String> contentMap = trainData.get(type);
			System.out.println("TF.......");
			Map<String, Map<String, Double>> tfs = SvmUtil.tf(contentMap, set);
			System.out.println("IDF......");
			Map<String, Double> idfs = SvmUtil.idf(contentMap, set);
			System.out.println("TF-IDF...");
			Map<String, Map<String, Double>> tfidfs = SvmUtil.tf_idf(tfs, idfs, set);
			System.out.println("依TF-IDF取前" + depth + "關鍵字");
			List<Map.Entry<String, Double>> list = SvmUtil.tf_idf_sort(tfidfs, set);
			//
			List<String> keywordList = new ArrayList<String>();
			int count = 1, index = 0, size = list.size();
			while (count <= depth && index < size) {
				Map.Entry<String, Double> keyMap = list.get(index++);
				String key = keyMap.getKey();
				keywordList.add(key);
				count++;
			}
			keywordMap.put(type, keywordList);
		}
		//
		System.out.println("解析關鍵字完成,輸出" + keywordsFile);
		SvmUtil.outputKeywords(this, keywordMap, depth);
		System.out.println("TF-IDF解析關鍵字結束...");
	}

	/**
	 * 學習
	 * 
	 */
	public void training() {
		System.out.println("學習開始...");
		// data
		if (trainData == null) {
			trainData = new HashMap<String, Map<String, String>>();
			for (String type : types()) {
				Map<String, String> map = SvmUtil.loadExcel(path() + type + ".xlsx", 1, 300);
				trainData.put(type, map);
			}
		}
		// keywords
		if (keywords == null)
			keywords = SvmUtil.loadKeywords(path() + keywordsFile);

		// parse svm_problem
		svm_problem svmProblem = SvmUtil.parseSvmProblem(this);
		try {
			svm_model model = svm.svm_train(svmProblem, getParam());
			System.out.println("學習完成,產生模型檔:" + path() + svmModelFile);
			svm.svm_save_model(path() + svmModelFile, model);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("學習結束...");
		}
	}

	/**
	 * 測試
	 */
	public void testing() {
		System.out.println("測試開始...");
		if (testData == null) {
			testData = new HashMap<String, Map<String, String>>();
			for (String type : types()) {
				Map<String, String> map = SvmUtil.loadExcel(path() + type + ".xlsx", 1, 300);
				testData.put(type, map);
			}
		}
		// keywords
		if (keywords == null)
			keywords = SvmUtil.loadKeywords(path() + keywordsFile);

		// testing
		int correct = 0, total = 0;
		try {
			svm_model model = svm.svm_load_model(path() + svmModelFile);

			Map<String, Map<String, String>> data = getTestData();
			for (String typeName : data.keySet()) {
				Map<String, String> errorMap = new TreeMap<String, String>();
				Map<Double, Integer> statistics = new TreeMap<Double, Integer>();
				int modelCount = 0, modelTotal = 0;

				Map<String, String> contentMap = data.get(typeName);

				for (String key : contentMap.keySet()) {
					total++;
					modelTotal++;
					String content = contentMap.get(key);
					svm_node[] x = SvmUtil.parseSvmNode(content, keywords);
					double v = svm.svm_predict(model, x);
					if (v == nameToLabel(typeName)) {
						correct++;
						modelCount++;
					} else {
						errorMap.put(key, labelToName(v));

						Integer statisticsNumber = statistics.get(v);
						if (statisticsNumber == null)
							statisticsNumber = 0;
						statisticsNumber++;
						statistics.put(v, statisticsNumber);
					}
				}
				double accuracy = (double) modelCount / modelTotal * 100;
				System.out.println(typeName + " Accuracy = " + accuracy + "% (" + modelCount + "/" + modelTotal
						+ ")");
				StringBuffer sb = new StringBuffer();
				for (Double key : statistics.keySet()) {
					sb.append(labelToName(key)).append("[").append(statistics.get(key)).append("] ");
				}
				// System.out.println(sb.toString());
			}

			double accuracy = (double) correct / total * 100;
			System.out.println("Total Accuracy = " + accuracy + "% (" + correct + "/" + total + ")");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("測試結束...");
		}
	}

	/**
	 * 辨識
	 * 
	 * @param input
	 * @return
	 */
	public String identify(String content) {
		String result = "";
		// keywords
		if (keywords == null)
			keywords = SvmUtil.loadKeywords(path() + keywordsFile);
		//
		try {
			svm_model model = svm.svm_load_model(path() + svmModelFile);
			svm_node[] x = SvmUtil.parseSvmNode(content, keywords);
			double v = svm.svm_predict(model, x);
			result = this.labelToName(v);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// abstract method =================================================================

	/**
	 * 模型路徑
	 * 
	 * @return
	 */
	abstract String path();

	/**
	 * 模型分類
	 * 
	 * @return
	 */
	abstract String[] types();
}
