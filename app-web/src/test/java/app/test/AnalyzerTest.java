package app.test;

import java.util.Iterator;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class AnalyzerTest {

	public static void main(String[] args) throws Exception {
		System.out.println("testSmartChineseAnalyzer =============");
		testSmartChineseAnalyzer();
		System.out.println("testMySmartChineseAnalyzer =============");
		testMySmartChineseAnalyzer();
	}

	private static void testSmartChineseAnalyzer() throws Exception {
		SmartChineseAnalyzer smartChineseAnalyzer = new SmartChineseAnalyzer();
		print(smartChineseAnalyzer);
	}

	private static void testMySmartChineseAnalyzer() throws Exception {
		CharArraySet charArraySet = new CharArraySet(0, true);

		// 系統默認停用詞
		Iterator<Object> iterator = SmartChineseAnalyzer.getDefaultStopSet().iterator();
		while (iterator.hasNext()) {
			charArraySet.add(iterator.next());
		}
		// 自定義停用詞
		String[] myStopWords = { "彰化", "银行" };
		for (String stopWord : myStopWords) {
			charArraySet.add(stopWord);
		}
		System.out.println(charArraySet);

		SmartChineseAnalyzer smartChineseAnalyzer = new SmartChineseAnalyzer(charArraySet);
		print(smartChineseAnalyzer);
	}

	private static void print(Analyzer analyzer) throws Exception {
		// String text = "彰化銀行人民幣存款優惠方案熱烈實施中【優惠期間：自107年5月1日起至107年5月31日止】";
		String text = "彰化银行人民币存款优惠方案热烈实施中【优惠期间：自107年5月1日起至107年5月31日止】";
		// String text = "Lucene自帶多種分詞器，其中對中文分詞支持比較好的是smartcn。";
		TokenStream tokenStream = analyzer.tokenStream("content", text);
		CharTermAttribute attribute = tokenStream.addAttribute(CharTermAttribute.class);
		tokenStream.reset();
		StringBuilder sb = new StringBuilder();
		while (tokenStream.incrementToken()) {
			if (sb.length() > 0)
				sb.append("|");
			sb.append(new String(attribute.toString()));
			// System.out.println(new String(attribute.toString()));
		}
		System.out.println(sb.toString());
	}
}
