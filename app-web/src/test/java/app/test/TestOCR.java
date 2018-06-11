package app.test;

import java.io.File;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class TestOCR {

	public static void main(String[] args) {
		System.out.println("doOCR on a PNG image");
//		File imageFile = new File("D:/yahoo.png");
		File imageFile = new File("D:/test-data/eurotext.tif");
		try {
			ITesseract instance = new Tesseract();
//			instance.setDatapath("C:/Program Files (x86)/Tesseract-OCR/tessdata");
			System.out.println(imageFile.exists());
			String result = instance.doOCR(imageFile);
			System.out.println(result);
		} catch (TesseractException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
