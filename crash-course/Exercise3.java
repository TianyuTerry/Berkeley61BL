public class Exercise3 {
    /** Returns the maximum value from arr. */
    public static int max(int[] arr) {
    	int maximum = 0;
    	for (int i = 0; i < arr.length; i += 1){
            if (arr[i] > maximum){
                maximum = arr[i];
            }
        }
        return maximum;
    }
    public static void main(String[] args) {
        int[] numbers = new int[]{9, 2, 15, 2, 22, 10, 6};
        System.out.println(max(numbers));
    }
}