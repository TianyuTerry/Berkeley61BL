public class Exercise1 {
	public static void drawTriangle(int n){
		int row = 1;
		while (row <= n){
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
	public static void main(String[] args) {
      drawTriangle(10);
   }
}