public class ArrayOperations {
    /**
     * Delete the value at the given position in the argument array, shifting
     * all the subsequent elements down, and storing a 0 as the last element of
     * the array.
     */
    public static void delete(int[] values, int pos) {
        if (pos < 0 || pos >= values.length) {
            return;
        }
        // TODO: YOUR CODE HERE
        else {
            int i = pos;
            while (i < values.length - 1) {
                values[i] = values[i + 1];
                i++;
            }
            values[i] = 0;
        }
    }

    /**
     * Insert newInt at the given position in the argument array, shifting all
     * the subsequent elements up to make room for it. The last element in the
     * argument array is lost.
     */
    public static void insert(int[] values, int pos, int newInt) {
        if (pos < 0 || pos >= values.length) {
            return;
        }
        // TODO: YOUR CODE HERE
        else {
            int i = values.length - 1;
            while (i != pos) {
                values[i] = values[i - 1];
                i--;
            }
            values[i] = newInt; 
        }
    }
}
