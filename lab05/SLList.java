import java.util.Objects;

/**
  * An SLList is a list of integers, which hides the terrible truth of the
  * nakedness within.
  */
public class SLList {
    private static class IntNode {
        public int item;
        public IntNode next;

        @Override
        public String toString() {
            return "IntNode{"
                    + "item="
                    + item
                    + ", next="
                    + next
                    + '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            IntNode intNode = (IntNode) o;
            return item == intNode.item
                    && Objects.equals(next, intNode.next);
        }

        public IntNode(int i, IntNode n) {
            item = i;
            next = n;
        }
    }

    /* The first item (if it exists) is at sentinel.next. */
    private IntNode sentinel;
    private int size;

    /** Creates an empty SLList. */
    public SLList() {
        sentinel = new IntNode(63, null);
        size = 0;
    }

    public SLList(int x) {
        sentinel = new IntNode(63, null);
        sentinel.next = new IntNode(x, null);
        size = 1;
    }

    /** Returns an SLList consisting of the given values. */
    public static SLList of(int... values) {
        SLList list = new SLList();
        for (int i = values.length - 1; i >= 0; i -= 1) {
            list.addFirst(values[i]);
        }
        return list;
    }

    /** Returns the size of the list. */
    public int size() {
        return size;
    }

    /** Adds x to the front of the list. */
    public void addFirst(int x) {
        sentinel.next = new IntNode(x, sentinel.next);
        size += 1;
    }

    /** Returns the first item in the list. */
    public int getFirst() {
        return sentinel.next.item;
    }

    /** Return the value at the given index. */
    public int get(int index) {
        IntNode p = sentinel.next;
        while (index > 0) {
            p = p.next;
            index -= 1;
        }
        return p.item;
    }

    /** Adds x to the end of the list. */
    public void addLast(int x) {
        size += 1;
        IntNode p = sentinel;
        while (p.next != null) {
            p = p.next;
        }
        p.next = new IntNode(x, null);
    }

    /** Adds x to the list at the specified index. */
    public void add(int index, int x) {
        // TODO
        if (index < 0) {
            return;
        } else if (size <= index) {
            size += 1;
            IntNode p = sentinel;
            while (p.next != null) {
                p = p.next;
            }
            p.next = new IntNode(x, null);
        } else {
            size += 1;
            IntNode p = sentinel;
            while (index > 0) {
                p = p.next;
                index--;
            }
            IntNode p2 = new IntNode(x, p.next);
            p.next = p2;
            return;
        }
    }

    /** Returns the reverse of this list. This method is destructive. */
    public void reverse() {
        // TODO
        if (sentinel.next == null || sentinel.next.next == null) {
            return;
        } else {
            IntNode p = sentinel.next;
            IntNode x = new IntNode(p.item, null);
            p = p.next;
            while (p != null) {
                x = new IntNode(p.item, x);
                p = p.next;
            }
            sentinel.next = x;
        }

    }

    @Override
    public String toString() {
        /*return "SLList{" +
                "sentinel=" + sentinel +
                ", size=" + size +
                '}';*/
        IntNode p = sentinel.next;
        String answer = "";
        while (p != null) {
            answer += Integer.toString(p.item) + ' ';
            p = p.next;
        }
        return answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SLList slList = (SLList) o;
        return size == slList.size
                && Objects.equals(sentinel, slList.sentinel);
    }

    public static void main(String[] args) {
        SLList test1 = SLList.of(1, 3, 5);
        System.out.println(test1);
        test1.add(1, 2);
        test1.add(3, 4);
        System.out.println(test1);
        test1.reverse();
        System.out.println(test1);
        SLList test2 = SLList.of(1, 2, 3);
        test2.reverse();
        System.out.println(test2);
        SLList test3 = SLList.of(5);
        test3.reverse();
        System.out.println(test3);
        SLList test4 = new SLList();
        test4.reverse();
        System.out.println(test4);
    }

}
