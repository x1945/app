package app.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.JiebaSegmenter.SegMode;
import com.huaban.analysis.jieba.SegToken;
import com.huaban.analysis.jieba.WordDictionary;

public class test {

	public static void main(String[] args) {
		// test obj = new test();
		// System.out.println(obj.loadFile("scripts/term.json"));

		System.out.println(180%30);
		System.out.println(12%30);
		System.out.println(179%30);
		System.out.println(30%30);
		int docNum= 10, docCount=10;
		System.out.println("Math.log docNum / docCount:"+ Math.log((double) docNum / docCount));
		System.out.println(SvmUtil.parseWord("你愛我嗎"));
		String test = "中華民國,中華民國,中華民國,中華民國,中華民國,中華民國,中華民國,中華民國,中華民國,中華民國";
		String ssss = "中華民國";
		System.out.println(ssss.matches("[\\u4e00-\\u9fa5]+"));

		SvmUtil.init();
		System.out.println(SvmUtil.labelToName(3d));
		System.out.println(SvmUtil.nameToLabel("資管處"));
		System.out.println("frequency:" + SvmUtil.frequency(test, "中華"));
		long startTime = System.currentTimeMillis();
		for (int i = 0; i <= 1000; i++)
			SvmUtil.frequency(test, "中");
		System.out.println("search Time:" + (double) (System.currentTimeMillis() - startTime) / 1000d + " s");

		Set<String> testSet = new HashSet<String>();
		testSet.add("中華民國");
		testSet.add("中華");
		testSet.add("中華民國");
		System.out.println(testSet);

		jiebaAnalysis();
		// float value = (float) Math.log(987654 / 999);
		// System.out.println(value);
		// int i=100;
		// long l =9999;
		// System.out.println(String.format("% 5d", i));
		// System.out.println(String.format("% 5d", l));
	}

	private String loadFile(String fileName) {
		StringBuilder result = new StringBuilder("");
		// Get file from resources folder
		ClassLoader classLoader = this.getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());

		try (Scanner scanner = new Scanner(file)) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				result.append(line).append("\n");
			}
			scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	private static void jiebaAnalysis() {
		System.out.println("jieba Analysis -------------------");
		WordDictionary dictAdd = WordDictionary.getInstance();
		Path path = Paths.get("d:\\jieba\\dict.tw.txt");
		dictAdd.loadUserDict(path);

		JiebaSegmenter segmenter = new JiebaSegmenter();

		String[] sentences = new String[] { "這是一個伸手不見五指的黑夜。我叫孫悟空，我愛北京，我愛Python和C++。", "我不喜歡日本和服。", "雷猴回歸人間。",
				"工信處女干事每月經過下屬科室都要親口交代24口交換機等技術性器件的安裝工作", "結果婚的和尚未結過婚的", "新竹的交通大學在新竹的大學路上",
				"主旨：貴會科長謝宏利前於本部資訊中心任職期間，辦理該中心105年度三級管制施政計畫管考業務，平均評核成績為優等，請查照並惠予獎勵。_x005F_x000D_說明：依本部考績委員會106年第12次會議決議，獎勵額度為嘉奬二次。" };

		for (String sentence : sentences) {
			// System.out.println(segmenter.process(sentence, SegMode.INDEX).toString());
			System.out.println("--------------------------------------------------");
			System.out.println(sentence);
			List<SegToken> list = segmenter.process(sentence, SegMode.SEARCH);
			for (SegToken st : list) {
				System.out.println(st.word);
			}
		}
	}

}
