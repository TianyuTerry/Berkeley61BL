import java.util.Iterator;
import java.util.LinkedList;

public class HashMap<K, V> implements Map61BL<K, V> {

    private LinkedList<Entry<K, V>>[] array;
    private int length;
    private int size;
    private float loadFactor;


    public HashMap() {
        loadFactor = 0.75f;
        size = 0;
        length = 16;
        array = new LinkedList[length];
        for (int i = 0; i < array.length; i++) {
            array[i] = new LinkedList<>();
        }
    }

    public HashMap(int initialCapacity) {
        loadFactor = 0.75f;
        size = 0;
        length = initialCapacity;
        array = new LinkedList[length];
        for (int i = 0; i < array.length; i++) {
            array[i] = new LinkedList<>();
        }
    }

    public HashMap(int initialCapacity, float loadFactor) {
        this.loadFactor = loadFactor;
        size = 0;
        length = initialCapacity;
        array = new LinkedList[length];
        for (int i = 0; i < array.length; i++) {
            array[i] = new LinkedList<>();
        }
    }

    public int capacity() {
        return length;
    }


    /* Returns true if the map contains the KEY. */
    public boolean containsKey(K key) {

        int index = getIndex(key, length);
        LinkedList<Entry<K, V>> list = array[index];
        for (Entry e : list) {
            if (e.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    /* Returns the value for the specified KEY. If KEY is not found, return
       null. */
    public V get(K key) {
        int index = getIndex(key, length);
        LinkedList<Entry<K, V>> list = array[index];
        for (Entry e : list) {
            if (e.key.equals(key)) {
                return (V) e.value;
            }
        }
        return null;
    }

    public int size() {
        return size;
    }

    /* Puts a (KEY, VALUE) pair into this map. If the KEY already exists in the
       SimpleNameMap, replace the current corresponding value with VALUE. */
    public void put(K key, V value) {
        if (((float) (size + 1)) / length > loadFactor) {
            resize();
        }
        int index = getIndex(key, length);
        LinkedList<Entry<K, V>> list = array[index];
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

    /* Removes a single entry, KEY, from this table and return the VALUE if
       successful or NULL otherwise. */
    public V remove(K key) {
        int index = getIndex(key, length);
        LinkedList<Entry<K, V>> list = array[index];
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).key.equals(key)) {
                V toReturn = list.get(i).value;
                list.remove(i);
                size -= 1;
                return toReturn;
            }
        }
        return null;
    }

    public boolean remove(K key, V value) {
        int index = getIndex(key, length);
        LinkedList<Entry<K, V>> list = array[index];
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).key.equals(key)) {
                list.remove(i);
                size -= 1;
                return true;
            }
        }
        return false;
    }

    public void clear() {
        array = new LinkedList[length];
        for (int i = 0; i < array.length; i++) {
            array[i] = new LinkedList<>();
        }
        size = 0;
    }

    public Iterator<K> iterator() {
        return new HashMapIterator();
    }

    private class HashMapIterator implements  Iterator<K> {
        private int i = 0;
        private int j = 0;

        public K next() {
            if (j < array[i].size()) {
                K toReturn = array[i].get(j).key;
                j += 1;
                return toReturn;
            } else {
                i += 1;
                j = 0;
                return next();
            }
        }

        public boolean hasNext() {
            Boolean isEmpty = true;
            for (int k = i + 1; k < array.length; k++) {
                if (!array[k].isEmpty()) {
                    isEmpty = false;
                    break;
                }
            }
            return j < array[i].size() || !isEmpty;
        }
    }

    private int getIndex(K key, int divisor) {
        return Math.floorMod(key.hashCode(), divisor);
    }

    private void resize() {
        LinkedList<Entry<K, V>>[] newArray = new LinkedList[length * 2];
        for (int i = 0; i < newArray.length; i++) {
            newArray[i] = new LinkedList<>();
        }
        for (int i = 0; i < length; i++) {
            LinkedList<Entry<K, V>> list = array[i];
            for (int j = 0; j < list.size(); j++) {
                int index = getIndex(list.get(j).key, length * 2);
                newArray[index].add(list.get(j));
            }
        }
        array = newArray;
        length *= 2;
    }

    private static class Entry<K, V> {

        private K key;
        private V value;

        Entry(K key, V value) {
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
    }

    public static void main(String[] args) {
        System.out.println("st".hashCode());
    }
}

