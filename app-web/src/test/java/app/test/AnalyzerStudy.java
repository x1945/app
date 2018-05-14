package app.test;

import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class AnalyzerStudy {

	public static void main(String[] args) throws Exception {
		// 需要處理的測試字符串
		String str = "這是一個分詞器測試程序，希望大家繼續關注我的個人系列博客：基於Lucene的案例開發，這裡加一點帶空格的標簽 LUCENE java 分詞器";
		Analyzer analyzer = null;
		// 標准分詞器，如果用來處理中文，和ChineseAnalyzer有一樣的效果，這也許就是之後的版本棄用ChineseAnalyzer的一個原因
		// 第三方中文分詞器，有下面2中構造方法。
		analyzer = new SmartChineseAnalyzer();
		// 空格分詞器，對字符串不做如何處理
		analyzer = new WhitespaceAnalyzer();
		// 簡單分詞器，一段一段話進行分詞
		analyzer = new SimpleAnalyzer();
		// 二分法分詞器，這個分詞方式是正向退一分詞(二分法分詞)，同一個字會和它的左邊和右邊組合成一個次，每個人出現兩次，除了首字和末字
		analyzer = new CJKAnalyzer();
		// 關鍵字分詞器，把處理的字符串當作一個整體
		analyzer = new KeywordAnalyzer();
		// 被忽略的詞分詞器
		analyzer = new StopAnalyzer();

		// 使用分詞器處理測試字符串
		StringReader reader = new StringReader(str);
		TokenStream tokenStream = analyzer.tokenStream("", reader);
		tokenStream.reset();
		CharTermAttribute term = tokenStream.getAttribute(CharTermAttribute.class);
		int l = 0;
		// 輸出分詞器和處理結果
		System.out.println(analyzer.getClass());
		while (tokenStream.incrementToken()) {
			System.out.print(term.toString() + "|");
			l += term.toString().length();
			// 如果一行輸出的字數大於30，就換行輸出
			if (l > 30) {
				System.out.println();
				l = 0;
			}
		}
	}
}
