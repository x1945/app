package app.es;

public class QueryTest2 {

	public static void main(String[] args) throws Exception {
		int count = 0;
		System.out.println(count);
		for (int i = 1; i <= 9876; i++) {
			count = ++count % 1000;
			if (count == 0) {
				System.out.println("count is zero");
			}
		}
		System.out.println(count);
	}
}
