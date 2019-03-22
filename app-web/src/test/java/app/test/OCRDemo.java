package app.test;

import java.io.File;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.Tesseract1;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;

public class OCRDemo {

	public static void main(String[] args) throws TesseractException {
		System.out.println("start");
		System.setProperty("Dfile.encoding", "UTF8 ");
//		chs();
//		eng();
		eng4();
		
    	System.out.println("-------------------------------");
    	System.out.println("LibName:"+LoadLibs.getTesseractLibName());
    	System.out.println("java.io.tmpdir:"+System.getProperty("java.io.tmpdir"));
    	System.out.println("jna.library.path:"+System.getProperty("jna.library.path"));
		
		System.out.println("end");
	}
	
	public static void chs () throws TesseractException{
		ITesseract instance = new Tesseract();
		// 如果未將tessdata放在根目錄下需要指定絕對路徑
		instance.setDatapath("D:/Tess4J/tessdata");
//		instance.setDatapath("D:/Tess4J/langdata");

		// 如果需要識別英文之外的語種，需要指定識別語種，並且需要將對應的語言包放進項目中
		instance.setLanguage("chi_tra");
		// 指定識別圖片
//		File imageFile = new File("D:/test-data/OCR-TEST.png");
//		File imageFile = new File("D:/test-data/分機一覽表.pdf");
//		File imageFile = new File("D:/test-data/註解 2018-11-22 104313.jpg");
//		File imageFile = new File("D:/test-data/註解 2018-11-22 104323.png");
//		File imageFile = new File("D:/test-data/註解 2018-11-22 104622.png");
//		File imageFile = new File("D:/test-data/註解 2018-11-22 105027.png");
//		File imageFile = new File("D:/test-data/註解 2018-11-22 105027X2.png");
//		File imageFile = new File("D:/test-data/註解 2018-11-23 090307.png");
//		File imageFile = new File("D:/test-data/註解 2018-11-23 090525.png");
		File imageFile = new File("D:/test-data/2018-11-23 09.47.01.jpg");
		long startTime = System.currentTimeMillis();
//		instance.setOcrEngineMode(TessAPI.TessOcrEngineMode.OEM_CUBE_ONLY);
		System.out.println("識別中...");
		String ocrResult = instance.doOCR(imageFile);

		// 輸出識別結果
		System.out.println("OCR Result: \n" + ocrResult + "\n 耗時：" + (System.currentTimeMillis() - startTime) + "ms");
	}
	
	public static void eng () throws TesseractException{
		ITesseract instance = new Tesseract();
//		System.setProperty("jna.library.path", "32".equals(System.getProperty("sun.arch.data.model")) ? "D:/tess4j-4.3.0-sources/win32-x86" : "D:/tess4j-4.3.0-sources/win32-x86-64");
//	
//		System.out.println("jna.library.path:"+System.getProperty("jna.library.path"));
//		System.out.println("sun.arch.data.model:"+System.getProperty("sun.arch.data.model"));
		// 如果未將tessdata放在根目錄下需要指定絕對路徑
		instance.setDatapath("D:\\tess4j-4.3.0-sources\\tessdata");
//		instance.setDatapath("D:/Tess4J/tessdata");

		// 如果需要識別英文之外的語種，需要指定識別語種，並且需要將對應的語言包放進項目中
//		instance.setLanguage("eng");
		System.out.println(System.getProperty("Dfile.encoding"));
//		System.setProperty("jna.encoding", "UTF8");
//		System.setProperty("Dfile.encoding", "UTF8 ");
//		Dfile.encoding=UTF8 
		
		// 指定識別圖片
		File imageFile = new File("D:\\test-data\\eurotext.png");
		System.out.println(imageFile.exists());
		long startTime = System.currentTimeMillis();
//		instance.setOcrEngineMode(TessAPI.TessOcrEngineMode.OEM_TESSERACT_ONLY);
		String ocrResult = instance.doOCR(imageFile);

		// 輸出識別結果
		System.out.println("OCR Result: \n" + ocrResult + "\n 耗時：" + (System.currentTimeMillis() - startTime) + "ms");
	}
	
	public static void eng4 () throws TesseractException{
		ITesseract instance = new Tesseract();
		
		
		System.setProperty("jna.library.path", "D:\\tess4j-4.3.0-sources");
//		System.setProperty("jna.library.path", "C:\\Users\\AP4-Fantasy\\AppData\\Local\\Temp\\tess4j");
//		System.setProperty("jna.library.path", "32".equals(System.getProperty("sun.arch.data.model")) ? "D:/tess4j-4.3.0-sources/win32-x86" : "D:/tess4j-4.3.0-sources/win32-x86-64");
//	
//		System.out.println("jna.library.path:"+System.getProperty("jna.library.path"));
//		System.out.println("sun.arch.data.model:"+System.getProperty("sun.arch.data.model"));
		// 如果未將tessdata放在根目錄下需要指定絕對路徑
		instance.setDatapath("D:\\tess4j-4.3.0-sources\\tessdata");

		// 如果需要識別英文之外的語種，需要指定識別語種，並且需要將對應的語言包放進項目中
//		instance.setLanguage("eng");
		System.out.println(System.getProperty("Dfile.encoding"));
//		System.setProperty("jna.encoding", "UTF8");
//		System.setProperty("Dfile.encoding", "UTF8 ");
//		Dfile.encoding=UTF8 
		
		// 指定識別圖片
		File imageFile = new File("D:\\test-data\\eurotext.png");
		System.out.println(imageFile.exists());
		long startTime = System.currentTimeMillis();
//		instance.setOcrEngineMode(TessAPI.TessOcrEngineMode.OEM_TESSERACT_ONLY);
		String ocrResult = instance.doOCR(imageFile);

		// 輸出識別結果
		System.out.println("OCR Result: \n" + ocrResult + "\n 耗時：" + (System.currentTimeMillis() - startTime) + "ms");
	}
}
