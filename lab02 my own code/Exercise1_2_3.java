public class Exercise1_2_3 {
	public static void main(String[] args) {
		int sum = 0;
		for (int i = 0; i < args.length; i += 1) {
			sum += Integer.parseInt(args[i]);
		}
		System.out.println(sum);
	}
}