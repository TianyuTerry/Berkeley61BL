import java.util.Arrays;

public class DistributionSorts {

    /* Destructively sorts ARR using counting sort. Assumes that ARR contains
       only 0, 1, ..., 9. */
    /**
     *  code here
     */
    public static void countingSort(int[] arr) {
        int[] counts = new int[10];
        for (int i = 0; i < 10; i++) {
            counts[i] = 0;
        }
        for (int i : arr) {
            counts[i]++;
        }
        int[] starts = new int[10];
        starts[0] = 0;
        for (int i = 1; i < 10; i++) {
            starts[i] = starts[i - 1] + counts[i - 1];
        }
        int[] sorted = new int[arr.length];
        for (int i : arr) {
            sorted[starts[i]] = i;
            starts[i]++;
        }
        for (int j = 0; j < arr.length; j++) {
            arr[j] = sorted[j];
        }
    }

    /* Destructively sorts ARR using LSD radix sort. */
    public static void lsdRadixSort(int[] arr) {
        int maxDigit = mostDigitsIn(arr);
        for (int d = 0; d < maxDigit; d++) {
            countingSortOnDigit(arr, d);
        }
    }

    /* A helper method for radix sort. Modifies ARR to be sorted according to
       DIGIT-th digit. When DIGIT is equal to 0, sort the numbers by the
       rightmost digit of each number. */
    /**
     *  code here
     */
    private static void countingSortOnDigit(int[] arr, int digit) {
        int[] counts = new int[10];
        for (int i = 0; i < 10; i++) {
            counts[i] = 0;
        }
        for (int i : arr) {
            counts[extractNumber(i, digit)]++;
        }
        int[] starts = new int[10];
        starts[0] = 0;
        for (int i = 1; i < 10; i++) {
            starts[i] = starts[i - 1] + counts[i - 1];
        }
        int[] sorted = new int[arr.length];
        for (int i : arr) {
            int number = extractNumber(i, digit);
            sorted[starts[number]] = i;
            starts[number]++;
        }
        for (int j = 0; j < arr.length; j++) {
            arr[j] = sorted[j];
        }
    }

    private static int extractNumber(int i, int digit) {
        String number = Integer.toString(i);
        if (digit >= number.length()) {
            return 0;
        } else {
            return Character.getNumericValue(number.charAt(number.length() - digit - 1));
        }
    }

    /* Returns the largest number of digits that any integer in ARR has. */
    private static int mostDigitsIn(int[] arr) {
        int maxDigitsSoFar = 0;
        for (int num : arr) {
            int numDigits = (int) (Math.log10(num) + 1);
            if (numDigits > maxDigitsSoFar) {
                maxDigitsSoFar = numDigits;
            }
        }
        return maxDigitsSoFar;
    }

    /* Returns a random integer between 0 and 9999. */
    private static int randomInt() {
        return (int) (10000 * Math.random());
    }

    /* Returns a random integer between 0 and 9. */
    private static int randomDigit() {
        return (int) (10 * Math.random());
    }

    /* Returns a random integer between 0 and 99. */
    private static int randomDigitNew() {
        return (int) (100 * Math.random());
    }

    private static void runCountingSort(int len) {
        int[] arr1 = new int[len];
        for (int i = 0; i < arr1.length; i++) {
            arr1[i] = randomDigit();
        }
        System.out.println("Original array: " + Arrays.toString(arr1));
        countingSort(arr1);
        if (arr1 != null) {
            System.out.println("Should be sorted: " + Arrays.toString(arr1));
        }
    }

    private static void runLSDRadixSort(int len) {
        int[] arr2 = new int[len];
        for (int i = 0; i < arr2.length; i++) {
            arr2[i] = randomDigitNew();
        }
        System.out.println("Original array: " + Arrays.toString(arr2));
        lsdRadixSort(arr2);
        System.out.println("Should be sorted: " + Arrays.toString(arr2));

    }

    public static void main(String[] args) {
        runCountingSort(20);
        runLSDRadixSort(3);
        runLSDRadixSort(30);
    }
}
