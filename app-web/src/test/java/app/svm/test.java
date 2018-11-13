package app.svm;

public class test {

	public static void main(String[] args) {
		System.out.println("start");
		DocModel docModel = new DocModel();
		// 訓練
		// docModel.training();
		// 測試
		// docModel.testing();
		// 辨識
		String typeName = docModel.identify(
				"部107年2月8日勞動發管字第10605218321號令發布「勞動部受理外國專業人才延攬及僱用法第十七條申請案件審查作業要點」第6點規定，申請人所附相關文件係外文者，應檢附中文譯本，但其他法令另有規定者，不在此限。");
		System.out.println("辨識:" + typeName);
		System.out.println("end");
	}
}
