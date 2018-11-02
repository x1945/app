package app.test;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class test {

	public static void main(String[] args) {
		// test obj = new test();
		// System.out.println(obj.loadFile("scripts/term.json"));

		String test = "中華民國,中華民國,中華民國";

		System.out.println(SvmUtil.frequency(test, "中"));

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

}
