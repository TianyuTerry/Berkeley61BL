import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

public class CodingChallenges {

    /**
     * Return the missing number from an array of length N - 1 containing all
     * the values from 0 to N except for one missing number.
     */
    public static int missingNumber(int[] values) {
        Set<Integer> set = new HashSet<>();
        for (int el : values) {
            set.add(el);
        }
        for (int i = 0; i <= values.length + 1; i++) {
            if (!set.contains(i)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns true if and only if two integers in the array sum up to n.
     */
    public static boolean sumTo(int[] values, int n) {

        Set<Integer> set = new HashSet<>();

        for (int i = 0; i < values.length; i++) {
            for (int j = i + 1; j < values.length; j++) {
                set.add(values[i] + values[j]);
            }
        }
        return set.contains(n);
    }

    /**
     * Returns true if and only if s1 is a permutation of s2. s1 is a
     * permutation of s2 if it has the same number of each character as s2.
     */
    public static boolean isPermutation(String s1, String s2) {
        Map<Character, Integer> map1 = new HashMap<>();
        Map<Character, Integer> map2 = new HashMap<>();
        int i = 0;
        int j = 0;
        while (i < s1.length()) {
            if (!map1.containsKey(s1.charAt(i))) {
                map1.put(s1.charAt(i), 1);
            } else {
                map1.put(s1.charAt(i), map1.get(s1.charAt(i)) + 1);
            }
            i++;
        }
        while (j < s2.length()) {
            if (!map2.containsKey(s2.charAt(j))) {
                map2.put(s2.charAt(j), 1);
            } else {
                map2.put(s2.charAt(j), map2.get(s2.charAt(j)) + 1);
            }
            j++;
        }
        if (!(map1.keySet().equals(map2.keySet()))) {
            return false;
        }
        for (char keys : map1.keySet()) {
            if (map1.get(keys) != map2.get(keys)) {
                int i1 = map1.get(keys);
                int i2 = map2.get(keys);
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        String s1 = "sbsbsb";
        String s2 = "sbsbsb";
        System.out.println(isPermutation(s1, s2));
        System.out.println("3");
    }
}
