package app.svm;

import java.io.File;

/**
 * 測試公文 Model
 * 
 * @author AP4-Fantasy
 *
 */
public class DocModel extends SvmModel {

	@Override
	String[] types() {
		return new String[] { "人力發展處", "人事室", "主計室", "經發處", "資管處", "管考處" };
	}

	@Override
	String path() {
		return new File("doc").getAbsolutePath() + "\\";
	}
}
