package app.test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.JiebaSegmenter.SegMode;
import com.huaban.analysis.jieba.SegToken;
import com.huaban.analysis.jieba.WordDictionary;

import libsvm.svm_node;

public class SvmUtil {

	static Map<Double, String> L2N = new HashMap<Double, String>();
	static Map<String, Double> N2L = new HashMap<String, Double>();
	static final int KeyWordDepth = 30;
	static final int max = 300;
	static boolean weights = false;

	private enum Classification {
		人力發展處(1d), 人事室(2d), 主計室(3d), 經發處(4d), 資管處(5d), 管考處(6d);

		private double value;

		// 構造方法
		private Classification(double value) {
			this.value = value;
		}

		public double getValue() {
			return value;
		}
	}

	public static void init() {
		System.out.println("SvmUtil init start");
		//
		for (Classification c : Classification.values()) {
			L2N.put(c.getValue(), c.name());
			N2L.put(c.name(), c.getValue());
		}

		//
		WordDictionary dictAdd = WordDictionary.getInstance();
		Path path = Paths.get("dict.txt");
		dictAdd.loadUserDict(path);
		System.out.println("SvmUtil init end");
	}

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
		// if (input == null || s == null || s.length() == 0)
		// return 0;
		// int result = 0;
		// Matcher matcher = Pattern.compile(s).matcher(input);
		// while (matcher.find())
		// result++;
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
	public static Map<String, Map<String, Double>> tf(Map<String, String> ContentMap, Set<String> keywords) {
		Map<String, Map<String, Double>> allTF = new TreeMap<String, Map<String, Double>>();

		System.out.println("TF for Every file is :");
		for (String key : ContentMap.keySet()) {
			Map<String, Double> dict = new TreeMap<String, Double>();
			dict = tf(ContentMap.get(key), keywords);
			// System.out.println(key + " >> " + dict);
			allTF.put(key, dict);
		}
		return allTF;
	}

	// term frequency in a file, frequency of each word
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

	public static Map<String, Double> idf(Map<String, String> ContentMap, Set<String> keywords, String name) {
		Map<String, Double> result = new TreeMap<String, Double>();
		int docNum = ContentMap.size();
		System.out.println("IDF for every word is:");
		for (String word : keywords) {
			int docCount = 1; // 不為0
			for (String content : ContentMap.values()) {
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

	public static Map<String, Map<String, Double>> tf_idf(Map<String, Map<String, Double>> all_tf,
			Map<String, Double> idfs, Set<String> keywords) {
		Map<String, Map<String, Double>> result = new TreeMap<String, Map<String, Double>>();

		System.out.println("TF-IDF for Every file is :");
		for (String key : all_tf.keySet()) {
			TreeMap<String, Double> tfidf = new TreeMap<String, Double>();
			Map<String, Double> temp = all_tf.get(key);
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
		// for (Map.Entry<String, Double> map : list) {
		// System.out.println(map.getKey() + " : " + map.getValue());
		// }
		return list;
	}

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

	public static double nameToLabel(String name) {
		return N2L.get(name);
	}

	public static String labelToName(double v) {
		return L2N.get(v);
	}

	static JiebaSegmenter segmenter = new JiebaSegmenter();

	public static Set<String> jiebaAnalysis(String input) {
		Set<String> result = new HashSet<String>();
		if (input != null) {
			List<SegToken> list = segmenter.process(input, SegMode.SEARCH);
			for (SegToken st : list) {
				// if (st.word.length() == 2 && st.word.matches("[\\u4e00-\\u9fa5]+"))
				// result.add(st.word);
				String s = parseWord(st.word);
				if (!"".equals(s))
					result.add(s);
			}
		}
		return result;
	}

	public static String parseWord(String input) {
		if (input != null && input.matches("[\\u4e00-\\u9fa5]+")) {
			if (input.length() == 2) {
				String result = input.replaceAll("[你妳您我牠他它她和及與的自當於在從向被把給讓用以拿靠除比零一二三四五六七八九十]", "");
				if (result.length() >= 2)
					return result;
			}
		}
		return "";
	}

	public static svm_node[] parseSvmNode(String content, List<String> keywords) {
		int size = keywords.size();
		svm_node[] result = new svm_node[keywords.size()];
		for (int i = 0; i < size; i++) {
			String keyword = keywords.get(i);
			result[i] = new svm_node();
			result[i].index = i;
			// result[i].value = frequency(content, keyword) > 0 ? 1 : 0;
			if (weights) {
				result[i].value = (frequency(content, keyword) > 0 ? 1 : 0) * (((size - i) % KeyWordDepth) * 0.3 + 1);
				// result[i].value = (frequency(content, keyword) > 0 ? 1 : 0) * ((size - i) % KeyWordDepth) + 1;
			} else {
				result[i].value = frequency(content, keyword) > 0 ? 1 : 0;
			}
		}
		return result;
	}
}
