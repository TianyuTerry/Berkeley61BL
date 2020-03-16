import java.util.ArrayList;

/* A MinHeap class of Comparable elements backed by an ArrayList. */
public class MinHeap<E extends Comparable<E>> {

    /* An ArrayList that stores the elements in this MinHeap. */
    private ArrayList<E> contents;
    //private int size;

    /* Initializes an empty MinHeap. */
    public MinHeap() {
        contents = new ArrayList<>();
        contents.add(null);
    }

    /* Returns the element at index INDEX, and null if it is out of bounds. */
    private E getElement(int index) {
        if (index >= contents.size()) {
            return null;
        } else {
            return contents.get(index);
        }
    }

    /* Sets the element at index INDEX to ELEMENT. If the ArrayList is not big
       enough, add elements until it is the right size. */
    private void setElement(int index, E element) {
        while (index >= contents.size()) {
            contents.add(null);
        }
        contents.set(index, element);
    }

    /* Swaps the elements at the two indices. */
    private void swap(int index1, int index2) {
        E element1 = getElement(index1);
        E element2 = getElement(index2);
        setElement(index2, element1);
        setElement(index1, element2);
    }

    /* Prints out the underlying heap sideways. Use for debugging. */
    @Override
    public String toString() {
        return toStringHelper(1, "");
    }

    /* Recursive helper method for toString. */
    private String toStringHelper(int index, String soFar) {
        if (getElement(index) == null) {
            return "";
        } else {
            String toReturn = "";
            int rightChild = getRightOf(index);
            toReturn += toStringHelper(rightChild, "        " + soFar);
            if (getElement(rightChild) != null) {
                toReturn += soFar + "    /";
            }
            toReturn += "\n" + soFar + getElement(index) + "\n";
            int leftChild = getLeftOf(index);
            if (getElement(leftChild) != null) {
                toReturn += soFar + "    \\";
            }
            toReturn += toStringHelper(leftChild, "        " + soFar);
            return toReturn;
        }
    }

    /* Returns the index of the left child of the element at index INDEX. */
    private int getLeftOf(int index) {
        return 2 * index;
    }

    /* Returns the index of the right child of the element at index INDEX. */
    private int getRightOf(int index) {
        return 2 * index + 1;
    }

    /* Returns the index of the parent of the element at index INDEX. */
    private int getParentOf(int index) {
        return index / 2;
    }

    /* Returns the index of the smaller element. At least one index has a
       non-null element. */
    private int min(int index1, int index2) {
        if (getElement(index1) == null) {
            return index2;
        } else if (getElement(index2) == null) {
            return index1;
        } else if (getElement(index1).compareTo(getElement(index2)) < 0) {
            return index1;
        } else {
            return index2;
        }
    }

    /* Returns but does not remove the smallest element in the MinHeap. */
    public E peek() {
        return contents.get(1);
    }

    /* Bubbles up the element currently at index INDEX. */
    private void bubbleUp(int index) {
        while (index != 1 && min(index, getParentOf(index)) == index) {
            swap(index, getParentOf(index));
            index = getParentOf(index);
        }
    }

    /* Bubbles down the element currently at index INDEX. */
    private void bubbleDown(int index) {
        if (min(index, getRightOf(index)) == getRightOf(index)
                && min(index, getLeftOf(index)) == getLeftOf(index)) {
            int minIndex = min(getLeftOf(index), getRightOf(index));
            swap(index, minIndex);
            bubbleDown(minIndex);
        } else if (min(index, getRightOf(index)) == getRightOf(index)) {
            int rightIndex = getRightOf(index);
            swap(index, rightIndex);
            bubbleDown(rightIndex);
        } else if (min(index, getLeftOf(index)) == getLeftOf(index)) {
            int leftIndex = getLeftOf(index);
            swap(index, leftIndex);
            bubbleDown(leftIndex);
        }
    }

    /* Inserts element into the MinHeap. */
    public void insert(E element) {
        setElement(size() + 1, element);
        bubbleUp(size());
    }

    /* Returns the number of elements in the MinHeap. */
    public int size() {
        return contents.size() - 1;
    }

    /* Returns the smallest element. */
    public E removeMin() {
        E toReturn = getElement(1);
        swap(1, size());
        contents.remove(size());
        if (getElement(1) != null) {
            bubbleDown(1);
        }
        return toReturn;
    }

    /* Updates the position of ELEMENT inside the MinHeap, which may have been
       mutated since the inital insert. If a copy of ELEMENT does not exist in
       the MinHeap, do nothing.*/
    public void update(E element) {
        int index = contents.indexOf(element);
        if (index >= 1) {
            bubbleDown(index);
            bubbleUp(index);
        }
    }

    public void replace(E element) {
        for (int i = 1; i <= size(); i++) {
            if (contents.get(i).equals(element)) {
                contents.remove(i);
                contents.add(i, element);
                break;
            }
        }
    }

    public static void main(String[] args) {
        MinHeap<Integer> mhp = new MinHeap<>();
        mhp.insert(9);
        System.out.println(mhp.size());
        mhp.removeMin();
    }
}
