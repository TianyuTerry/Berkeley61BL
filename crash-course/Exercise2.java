public class Exercise2 {
    /** Returns the maximum value from arr. */
    public static int max(int[] arr) {
    	int index = 0;
    	int maximum = 0;
    	while (index < arr.length){
    		if (arr[index] > maximum){
    			maximum = arr[index];
    		}
    		index = index + 1;
    	}
        return maximum;
    }
    public static void main(String[] args) {
        int[] numbers = new int[]{9, 2, 15, 2, 22, 10, 6};
        System.out.println(max(numbers));
    }
}