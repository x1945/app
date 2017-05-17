package app.test;

import java.util.Locale;

import sys.util.DateUtil;

public class test {

	public static void main(String[] args) {
		System.out.println(DateUtil.getTime());
		System.out.println(Locale.TRADITIONAL_CHINESE.toString());
		System.out.println(Locale.ENGLISH);
		System.out.println(Locale.TAIWAN);
	}

}
