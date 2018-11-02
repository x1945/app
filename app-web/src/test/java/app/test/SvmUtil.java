package app.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SvmUtil {

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
		int result = 0;
		// Matcher matcher = Pattern.compile(s).matcher(input);
		// while (matcher.find())
		// result++;

		int index = -1;
		do {
			index = input.indexOf(s, ++index);
			if (index >= 0)
				result++;
		} while (index >= 0);

		return result;
	}

	// ====================================================================================
	public static Map<String, Map<String, Float>> tf(Map<String, String> ContentMap, Set<String> keywords)
			throws IOException {
		Map<String, Map<String, Float>> allTF = new TreeMap<String, Map<String, Float>>();

		System.out.println("TF for Every file is :");
		for (String key : ContentMap.keySet()) {
			Map<String, Float> dict = new TreeMap<String, Float>();
			dict = tf(ContentMap.get(key), keywords);
			// System.out.println(key + " >> " + dict);
			allTF.put(key, dict);
		}
		return allTF;
	}

	// term frequency in a file, frequency of each word
	public static Map<String, Float> tf(String content, Set<String> keywords) {
		Map<String, Float> result = new TreeMap<String, Float>();
		Map<String, Integer> temp = new TreeMap<String, Integer>();
		int totalFrequency = 0;
		for (String word : keywords) {
			int frequency = frequency(content, word);
			totalFrequency += frequency;
			temp.put(word, frequency);
		}
		for (String word : keywords) {
			float value = 0f;
			if (totalFrequency > 0)
				value = (float) temp.get(word) / totalFrequency;
			result.put(word, value);
		}
		return result;
	}

	public static Map<String, Float> idf(Map<String, String> ContentMap, Set<String> keywords) {
		Map<String, Float> result = new TreeMap<String, Float>();
		int docNum = ContentMap.size();
		System.out.println("IDF for every word is:");
		for (String word : keywords) {
			int docCount = 0;
			for (String content : ContentMap.values()) {
				if (frequency(content, word) > 0)
					docCount++;
			}
			float value = 0f;
			if (docCount > 0)
				value = (float) Math.log(docNum / docCount);
			System.out.println(word + " = " + value);
			result.put(word, value);
		}
		return result;
	}

	public static Map<String, Map<String, Float>> tf_idf(Map<String, Map<String, Float>> all_tf,
			Map<String, Float> idfs, Set<String> keywords) {
		Map<String, Map<String, Float>> result = new TreeMap<String, Map<String, Float>>();

		for (String key : all_tf.keySet()) {
			TreeMap<String, Float> tfidf = new TreeMap<String, Float>();
			Map<String, Float> temp = all_tf.get(key);
			for (String word : keywords) {
				Float f1 = temp.get(word);
				if (f1.isNaN())
					f1 = 0f;
				Float f2 = idfs.get(word);
				if (f2.isNaN())
					f2 = 0f;
				Float value = (float) f1 * f2;
				tfidf.put(word, value);
			}
			result.put(key, tfidf);
		}

		System.out.println("TF-IDF for Every file is :");
		for (String key : result.keySet()) {
			Map<String, Float> temp = result.get(key);
			// System.out.println(key + " >> " + temp);
		}
		return result;
	}

	public static List<Map.Entry<String, Float>> tf_idf_sort(Map<String, Map<String, Float>> tf_idfs,
			Set<String> keywords) {
		Map<String, Float> temp = new TreeMap<String, Float>();
		for (String word : keywords) {
			Float f = 0f;
			for (Map<String, Float> fMap : tf_idfs.values()) {
				Float value = fMap.get(word);
				if (!value.isNaN())
					f = Float.sum(f, value);
			}
			temp.put(word, f);
		}
		// System.out.println(temp);

		// 升序比較器
		Comparator<Map.Entry<String, Float>> valueComparator = new Comparator<Map.Entry<String, Float>>() {

			@Override
			public int compare(Entry<String, Float> o1, Entry<String, Float> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		};

		// map轉換成list進行排序
		List<Map.Entry<String, Float>> list = new ArrayList<Map.Entry<String, Float>>(temp.entrySet());

		// 排序
		Collections.sort(list, valueComparator);
		for (Map.Entry<String, Float> map : list) {
			System.out.println(map.getKey() + " : " + map.getValue());
		}
		return list;
	}

	// ====================================================================================

	// tf for all file
	public static Map<String, Map<String, Float>> tfAllFiles(Map<String, List<String>> map) throws IOException {
		Map<String, Map<String, Float>> allTF = new TreeMap<String, Map<String, Float>>();

		System.out.println("TF for Every file is :");
		for (String key : map.keySet()) {
			Map<String, Float> dict = new TreeMap<String, Float>();
			dict = tf(map.get(key));
			System.out.println(key + " >> " + dict);
			allTF.put(key, dict);
		}
		return allTF;
	}

	// term frequency in a file, frequency of each word
	public static Map<String, Float> tf(List<String> cutwords) {
		Map<String, Float> resTF = new TreeMap<String, Float>();
		int wordLen = cutwords.size();
		Map<String, Integer> intTF = normalTF(cutwords);
		for (String key : intTF.keySet()) {
			float value = (float) Float.parseFloat(intTF.get(key).toString()) / wordLen;
			resTF.put(key, value);
		}
		return resTF;
	}

	// term frequency in a file, times for each word
	public static Map<String, Integer> normalTF(List<String> cutwords) {
		Map<String, Integer> resTF = new TreeMap<String, Integer>();

		for (String word : cutwords) {
			if (resTF.get(word) == null) {
				resTF.put(word, 1);
			} else {
				resTF.put(word, resTF.get(word) + 1);
			}
		}
		return resTF;
	}

	public static Map<String, Float> idf(Map<String, Map<String, Float>> all_tf) {
		Map<String, Float> resIdf = new TreeMap<String, Float>();
		Map<String, Integer> dict = new TreeMap<String, Integer>();
		int docNum = all_tf.size();

		for (Map<String, Float> map : all_tf.values()) {
			for (String word : map.keySet()) {
				if (dict.get(word) == null) {
					dict.put(word, 1);
				} else {
					dict.put(word, dict.get(word) + 1);
				}
			}
		}

		System.out.println("IDF for every word is:");
		for (String key : dict.keySet()) {
			if (dict.get(key) != null) {
				float value = (float) Math.log(docNum / Float.parseFloat(dict.get(key).toString()));
				resIdf.put(key, value);
				System.out.println(key + " = " + value);
			}
		}
		return resIdf;
	}

	public static void tf_idf(Map<String, Map<String, Float>> all_tf, Map<String, Float> idfs) {
		Map<String, Map<String, Float>> resTfIdf = new TreeMap<String, Map<String, Float>>();

		for (String key : all_tf.keySet()) {
			TreeMap<String, Float> tfidf = new TreeMap<String, Float>();
			Map<String, Float> temp = all_tf.get(key);
			for (String word : temp.keySet()) {
				Float value = (float) Float.parseFloat(temp.get(word).toString()) * idfs.get(word);
				tfidf.put(word, value);
			}
			resTfIdf.put(key, tfidf);
		}

		System.out.println("TF-IDF for Every file is :");
		for (String key : resTfIdf.keySet()) {
			Map<String, Float> temp = resTfIdf.get(key);
			System.out.println(key + " >> " + temp);
		}
	}
}
