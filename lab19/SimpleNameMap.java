import java.util.LinkedList;

public class SimpleNameMap {

    /* Instance variables here? */
    private LinkedList<Entry>[] array;
    private int length;
    private int size;
    private static double loadFactor = 0.75;


    public SimpleNameMap() {
        size = 0;
        length = 10;
        array = (LinkedList<Entry>[]) new Object[length];
    }

    /* Returns true if the given KEY is a valid name that starts with A - Z. */
    private static boolean isValidName(String key) {
        return 'A' <= key.charAt(0) && key.charAt(0) <= 'Z';
    }

    /* Returns true if the map contains the KEY. */
    public boolean containsKey(String key) {
        if (isValidName(key)) {
            int index = getIndex(key, length);
            LinkedList<Entry> list = array[index];
            for (Entry e : list) {
                if (e.key.equals(key)) {
                    return true;
                }
            }
        }
        return false;
    }

    /* Returns the value for the specified KEY. If KEY is not found, return
       null. */
    public String get(String key) {
        if (isValidName(key)) {
            int index = getIndex(key, length);
            LinkedList<Entry> list = array[index];
            for (Entry e : list) {
                if (e.key.equals(key)) {
                    return e.value;
                }
            }
        }
        return null;
    }

    public int size() {
        return size;
    }

    /* Puts a (KEY, VALUE) pair into this map. If the KEY already exists in the
       SimpleNameMap, replace the current corresponding value with VALUE. */
    void put(String key, String value) {
        if (isValidName(key)) {
            if (((double) (size + 1)) / length > loadFactor) {
                resize();
            }
            int index = getIndex(key, length);
            LinkedList<Entry> list = array[index];
            Boolean found = false;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).key.equals(key)) {
                    found = true;
                    list.remove(i);
                    list.add(i, new Entry(key, value));
                    break;
                }
            }
            if (!found) {
                list.add(new Entry(key, value));
                size += 1;
            }
        }
    }

    /* Removes a single entry, KEY, from this table and return the VALUE if
       successful or NULL otherwise. */
    public String remove(String key) {
        if (isValidName(key)) {
            int index = getIndex(key, length);
            LinkedList<Entry> list = array[index];
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).key.equals(key)) {
                    String toReturn = list.get(i).value;
                    list.remove(i);
                    size -= 1;
                    return toReturn;
                }
            }
        }
        return null;
    }

    private static int getIndex(String key, int divisor) {
        return Math.floorMod(key.hashCode(), divisor);
    }

    private void resize() {
        LinkedList<Entry>[] newArray = (LinkedList<Entry>[]) new Object[length * 2];
        for (int i = 0; i < length; i++) {
            LinkedList<Entry> list = array[i];
            for (int j = 0; j < list.size(); j++) {
                int index = getIndex(list.get(j).key, length * 2);
                newArray[index].add(list.get(j));
            }
        }
        array = newArray;
        length *= 2;
    }

    private static class Entry {

        private String key;
        private String value;

        Entry(String key, String value) {
            this.key = key;
            this.value = value;
        }

        /* Returns true if this key matches with the OTHER's key. */
        public boolean keyEquals(Entry other) {
            return key.equals(other.key);
        }

        /* Returns true if both the KEY and the VALUE match. */
        @Override
        public boolean equals(Object other) {
            return (other instanceof Entry
                    && key.equals(((Entry) other).key)
                    && value.equals(((Entry) other).value));
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }
        /*public int hashCode() {
            return (this.key.charAt(0) - 'A');
        }*/
    }

    public static void main(String[] args) {
        System.out.println("st".hashCode());
    }
}
