public class Exercise0 {
	public static void main(String[] arg) {
		int row = 1;
		int SIZE = 5;
		while (row <= SIZE){
			int col = 1;
			while (col < row){
				System.out.print('*');
				System.out.print(' ');
				col = col + 1;
			}
			System.out.println('*');
			row = row + 1;
		}
	}
}