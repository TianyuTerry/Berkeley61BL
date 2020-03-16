/**
 * A data structure to represent a Linked List of Integers.
 * Each IntList represents one node in the overall Linked List.
 *
 * This is a dummy implementation to allow IntListTest to compile. Replace this
 * file with your own IntList class.
 */
public class IntList {
    int first;
    IntList rest;

    public IntList(int f, IntList r) {
        first = f;
        rest = r;
    }

    /**
     * Returns an IntList consisting of the given values.
     */
    public static IntList of(int... values) {
        if (values.length == 0) {
            return null;
        }
        IntList p = new IntList(values[0],  null);
        IntList front = p;
        for (int i = 1; i < values.length; i += 1) {
            p.rest = new IntList(values[i], null);
            p = p.rest;
        }
        return front;
    }

    /**
     * Returns the size of the list.
     */
    public int size() {
        if (rest == null) {
            return 1;
        }
        return 1 + rest.size();
    }

    /**
     * Returns [position]th value in this list.
     */
    public int get(int position) {
        if (position == 0) {
            return first;
        } else {
            return rest.get(position - 1);
        }
    }

    public void add(int value) {
        IntList p = this;
        while (p.rest != null) {
            p = p.rest;
        }
        p.rest = new IntList(value, null);
    }

    public int smallest() {
        int x = this.first;
        IntList p = this;
        while (p != null) {
            if (p.first <= x) {
                x = p.first;
            }
            p = p.rest;
        }
        return x;
    }

    public int squaredSum() {
        IntList p = this;
        int sum = 0;
        while (p != null) {
            sum += p.first * p.first;
            p = p.rest;
        }
        return sum;
    }

    public static void dSquareList(IntList L) {
        while (L != null) {
            L.first = L.first * L.first;
            L = L.rest;
        }
    }

    public static IntList catenate(IntList A, IntList B) {
        if (A != null) {
            IntList l = new IntList(A.first, null);
            IntList front = l;
            while (A.rest != null) {
                l.rest = new IntList(A.rest.first, null);
                A = A.rest;
                l = l.rest;
            }
            while (B != null) {
                l.rest = new IntList(B.first, null);
                B = B.rest;
                l = l.rest;
            }
            return front;
        } else if (A == null && B != null) {
            IntList l = new IntList(B.first, null);
            IntList front = l;
            while (B.rest != null) {
                l.rest = new IntList(B.rest.first, null);
                B = B.rest;
                l = l.rest;
            }
            return front;
        }
        return null;
    }

    public static IntList dcatenate(IntList A, IntList B) {

        IntList p = A;
        if (B == null) {
            return A;
        }
        while (p.rest != null) {
            p = p.rest;
        }
        p.rest = B;
        return A;
    }


    public String toString() {
        if (rest == null) {
            return Integer.toString(first);
        }
        IntList p = this;
        return Integer.toString(p.first) + " " + this.rest.toString();
    }

    /** Returns whether this and the given list or object are equal. */
    public boolean equals(Object o) {
        IntList other = (IntList) o;
        IntList p = this;
        int i = 0;
        int size = p.size();
        if (other == null) {
            return false;
        }
        if (size != other.size()) {
            return false;
        }
        while (i < size) {
            if (p.first != other.first) {
                return false;
            }
            p = p.rest;
            other = other.rest;
            i++;
        }
        return true;
    }

    public static void main(String [] args) {
        IntList l1 = IntList.of(3, 24, 5, 7, 4, 5, 0);
        IntList l2 = IntList.of(4, 5, 6, 78, 0);
        IntList l = IntList.catenate(l1, l2);
        System.out.println(l.toString());
        System.out.println(l1.toString());
        IntList.dcatenate(l1, l2);
        System.out.println(l1.toString());
    }


}






















