package app.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.JiebaSegmenter.SegMode;
import com.huaban.analysis.jieba.SegToken;
import com.huaban.analysis.jieba.WordDictionary;

public class jiebaAnalysisTest {

	static JiebaSegmenter segmenter;
	static Set<String> stopWords;

	public static void main(String[] args) {
		init();
		jiebaAnalysis();
	}

	public static void init() {
		System.out.println("jieba Analysis init start...");
		WordDictionary dictAdd = WordDictionary.getInstance();
		// Path path = Paths.get("d:\\jieba\\dict.tw.txt");
		Path path = Paths.get("d:\\jieba\\dict.big.txt");
		dictAdd.loadUserDict(path);

		// stopWords = readStopWords("d:\\jieba\\stop_words.txt");
		stopWords = readStopWords("d:\\jieba\\stop_words_20181116.txt");
		//
		segmenter = new JiebaSegmenter();
		System.out.println("jieba Analysis init end ...");
	}

	public static void jiebaAnalysis() {
		// JiebaSegmenter segmenter = new JiebaSegmenter();

		String[] sentences = new String[] { "這是一個伸手不見五指的黑夜。", "我叫孫悟空，我愛北京，我愛Python和C++。", "我不喜歡日本和服。", "雷猴回歸人間。",
				"工信處女干事每月經過下屬科室都要親口交代24口交換機等技術性器件的安裝工作", "結果婚的和尚未結過婚的", "新竹的交通大學在新竹的大學路上",
				"主旨：貴會科長謝宏利前於本部資訊中心任職期間，辦理該中心105年度三級管制施政計畫管考業務，平均評核成績為優等，請查照並惠予獎勵。_x005F_x000D_說明：依本部考績委員會106年第12次會議決議，獎勵額度為嘉奬二次。" };

		for (String sentence : sentences) {
			// System.out.println(segmenter.process(sentence, SegMode.INDEX).toString());
			System.out.println("==================================================");
			System.out.println(sentence);
			System.out.println("--------------------------------------------------");
			List<SegToken> list = segmenter.process(sentence, SegMode.SEARCH);
			// List<SegToken> list = segmenter.process(sentence, SegMode.INDEX);
			for (SegToken st : list) {
				// if (!stopWords.contains(st.word))
				System.out.println(st.word);
				// System.out.println(st.toString());
			}
			System.out.println("--------------------------------------------------");
		}
	}

	public static String jiebaAnalysis(String input) {
		return jiebaAnalysis(input, false);
	}

	public static String jiebaAnalysis(String input, boolean stopWord) {
		return jiebaAnalysis(input, stopWord, 1);
	}

	public static String jiebaAnalysis(String input, boolean stopWord, int len) {
		StringBuilder sb = new StringBuilder();
		List<SegToken> list = segmenter.process(input, SegMode.SEARCH);
		// List<SegToken> list = segmenter.process(sentence, SegMode.INDEX);
		for (SegToken st : list) {
			String s = SvmUtil.trim(st.word);
			if (s.matches("[\\u4e00-\\u9fa5]+") && s.length() > 0) {
				if (stopWord) {
					if (filter(s, len)) {
						if (sb.length() > 0)
							sb.append("、");
						sb.append(s);
					}
				} else {
					if (sb.length() > 0)
						sb.append("、");
					sb.append(s);
				}
			}
		}
		return sb.toString();
	}

	public static boolean filter(String input) {
		return filter(input, 1);
	}

	public static boolean filter(String input, int len) {
		if (input.matches("[\\u4e00-\\u9fa5]+") && input.length() >= len) {
			if (!stopWords.contains(input))
				return true;
		}
		return false;
	}

	// read file
	public static Set<String> readStopWords(String file) {
		Set<String> result = new HashSet<String>();
		InputStreamReader inStrR = null;
		BufferedReader br = null;
		try {
			inStrR = new InputStreamReader(new FileInputStream(file), "utf8");
			br = new BufferedReader(inStrR);
			String line = br.readLine();
			while (line != null) {
				result.add(SvmUtil.trim(line));
				line = br.readLine();
			}
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
					br = null;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if (inStrR != null) {
					inStrR.close();
					inStrR = null;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
}
