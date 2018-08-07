package app.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;

import net.sourceforge.tess4j.ITessAPI.TessPageIteratorLevel;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.Word;

public class TestOCR {

	public static final char space = (char) 12288;

	public static void main(String[] args) {
		TestOCR ocr = new TestOCR();
		// ocr.testDoOCR_File();
		long startTime = 0l;
		String s = "123測試;:[{}]|+_-=abc!@#$%^&*()";
		StringBuffer sb = new StringBuffer();
		int count = 100000; // 迴圈 100000次
		System.out.println(s);
		startTime = System.currentTimeMillis();
		for (int i = 0; i <= count; i++) {
			sb.append(ocr.toChanisesFullChar3(s));
		}
		System.out.println("Using Time1:" + (double) (System.currentTimeMillis() - startTime) / 1000d + " s");
		sb.delete(0, sb.length());
		startTime = System.currentTimeMillis();
		for (int i = 0; i <= count; i++) {
			sb.append(ocr.toChanisesFullChar2(s));
		}
		System.out.println("Using Time2:" + (double) (System.currentTimeMillis() - startTime) / 1000d + " s");
		sb.delete(0, sb.length());
		startTime = System.currentTimeMillis();
		for (int i = 0; i <= count; i++) {
			sb.append(ocr.toChanisesFullChar(s));
		}
		System.out.println("Using Time3:" + (double) (System.currentTimeMillis() - startTime) / 1000d + " s");
	}

	public void testDoOCR_File() {
		System.out.println("doOCR on a PNG image");
		// File imageFile = new File("D:/test-data/yahoo.png");
		File imageFile = new File("D:/test-data/OCR-TEST.png");
		// File imageFile = new File("D:/test-data/img001.jpg");

		// File imageFile = new File("D:/test-data/分機一覽表.pdf");
		// File imageFile = new File("D:/test-data/eurotext.tif");
		try {
			// ITesseract instance = new Tesseract();
			Tesseract instance = new Tesseract();
			instance.setDatapath("D:/Tess4J/tessdata");
			instance.setLanguage("chi_tra");
			// instance.setDatapath("C:/Program Files
			// (x86)/Tesseract-OCR/tessdata");
			System.out.println(imageFile.exists());

			BufferedImage bi = ImageIO.read(imageFile);
			String result = instance.doOCR(bi);
			// String result = instance.doOCR(imageFile);
			System.out.println(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("end");
	}

	public void testGetWords() {
		System.out.println("doOCR on a PNG image");
		// File imageFile = new File("D:/test-data/yahoo.png");
		File imageFile = new File("D:/test-data/OCR-TEST.png");
		// File imageFile = new File("D:/test-data/img001.jpg");

		// File imageFile = new File("D:/test-data/分機一覽表.pdf");
		// File imageFile = new File("D:/test-data/eurotext.tif");
		try {
			// ITesseract instance = new Tesseract();
			Tesseract instance = new Tesseract();
			instance.setDatapath("D:/Tess4J/tessdata");
			instance.setLanguage("chi_tra");

			System.out.println(imageFile.exists());

			int pageIteratorLevel = TessPageIteratorLevel.RIL_SYMBOL;

			BufferedImage bi = ImageIO.read(imageFile);
			List<Word> result = instance.getWords(bi, pageIteratorLevel);

			// print the complete result
			for (Word word : result) {
				System.out.println(word.toString());
			}

			System.out.println(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("end");
	}

	/**
	 * (2005/7/28) 說明：轉成中文全型字
	 * 
	 * @author jonz
	 * @param s
	 * @return
	 */
	public String toChanisesFullChar(String s) {
		if (s == null || s.equals(""))
			return "";
		char[] ca = s.toCharArray();
		for (int i = 0; i < ca.length; i++) {
			// 超過這個應該都是中文字了
			if (ca[i] > '\200') {
			}
			// 半型空白轉成全型空白
			else if (ca[i] == 32) {
				ca[i] = (char) 12288;
				;
			}
			// 是有定義的字、數字及符號
			else if (Character.isLetterOrDigit(ca[i])) {
				ca[i] = (char) (ca[i] + 65248);
			}
			// 其它不合要求的，全部轉成全型空白。
			else {
				ca[i] = (char) 12288;
				;
			}
		}
		return String.valueOf(ca);
	}

	/**
	 * (2005/7/28) 說明：轉成中文全型字
	 * 
	 * @author jonz
	 * @param s
	 * @return
	 */
	public String toChanisesFullChar2(String s) {
		if (s == null || s.equals(""))
			return "";
		char[] ca = s.toCharArray();
		for (int i = 0; i < ca.length; i++) {
			if (ca[i] > '\200') {
				continue;
			}      // 超過這個應該都是中文字了…
			if (ca[i] == 32) {
				ca[i] = (char) 12288;
				continue;
			}  // 半型空白轉成全型空白
			if (Character.isLetterOrDigit(ca[i])) {
				ca[i] = (char) (ca[i] + 65248);
				continue;
			}  // 是有定義的字、數字及符號
			ca[i] = (char) 12288;  // 其它不合要求的，全部轉成全型空白。
		}
		return String.valueOf(ca);
	}

	/**
	 * (2005/7/28) 說明：轉成中文全型字
	 * 
	 * @author jonz
	 * @param s
	 * @return
	 */
	public String toChanisesFullChar3(String s) {
		if (s == null || s.equals(""))
			return "";
		char[] ca = s.toCharArray();
		for (int i = 0; i < ca.length; i++) {
			if (ca[i] > '\200') {
				continue;
			}      // 超過這個應該都是中文字了…
			if (ca[i] == 32) {
				ca[i] = (char) 12288;
				continue;
			}  // 半型空白轉成全型空白
			if (Character.isLetterOrDigit(ca[i])) {
				ca[i] = (char) (ca[i] + 65248);
				continue;
			}  // 是有定義的字、數字及符號
			ca[i] = (char) 12288;  // 其它不合要求的，全部轉成全型空白。
		}
		return String.valueOf(ca);
	}
}
