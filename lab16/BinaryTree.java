import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;


public class BinaryTree<T> {

    protected TreeNode root;

    public BinaryTree() {
        root = null;
    }

    public BinaryTree(TreeNode t) {
        root = t;
    }

    /* Constructs a binary tree based on a given preorder traversal PRE and an
       inorder traversal IN. */
    public BinaryTree(ArrayList<T> pre,  ArrayList<T> in) {
        root = listHelper(pre, in);
    }

    public static void main(String[] args) {
        BinaryTree s = new BinaryTree();
        char[] char1 = {'A', 'B', 'C', 'D', 'E', 'F'};
        ArrayList<Character> listpre = new ArrayList<>();
        for (char c : char1) {
            listpre.add(c);
        }
        char[] char2 = {'B', 'A', 'E', 'D', 'F', 'C'};
        ArrayList<Character> listin = new ArrayList<>();
        for (char c : char2) {
            listin.add(c);
        }


        s.listHelper(listpre, listin).printInorder();
        System.out.println();
        s.listHelper(listpre, listin).printPreorder();
        /*System.out.println(s.getIndex(listpre, listin));
        System.out.println(s.sliceLeft(listpre, 1));
        System.out.println(s.sliceRight(listpre, 1));*/

        BinaryTree t = new BinaryTree();
        char[] char3 = {'A', 'B', 'D', 'H', 'E', 'I', 'C', 'F', 'J', 'G', 'K'};
        ArrayList<Character> listpre2 = new ArrayList<>();
        for (char c : char3) {
            listpre2.add(c);
        }
        char[] char4 = {'H', 'D', 'B', 'E', 'I', 'A', 'F', 'J', 'C', 'K', 'G'};
        ArrayList<Character> listin2 = new ArrayList<>();
        for (char c : char4) {
            listin2.add(c);
        }

        System.out.println();
        t.listHelper(listpre2, listin2).printInorder();
        System.out.println();
        t.listHelper(listpre2, listin2).printPreorder();
    }

    private TreeNode listHelper(ArrayList<T> pre,  ArrayList<T> in) {
        int index = getIndex(pre, in);

        ArrayList<T> leftListPre = sliceLeft(pre, index);
        ArrayList<T> leftTreePre = sliceRight(leftListPre, 0);

        ArrayList<T> leftListIn = sliceLeft(in, index);
        ArrayList<T> leftTreeIn = sliceLeft(leftListIn, leftListIn.size() - 2);

        ArrayList<T> rightTreePre = sliceRight(pre, index);
        ArrayList<T> rightTreeIn = sliceRight(in, index);
        TreeNode tempRoot = new TreeNode(leftListPre.get(0));
        if (leftTreePre.size() == 0) {
            tempRoot.left = null;
        } else {
            tempRoot.left = listHelper(leftTreePre, leftTreeIn);
        }
        if (rightTreePre.size() == 0) {
            tempRoot.right = null;
        } else {
            tempRoot.right = listHelper(rightTreePre, rightTreeIn);
        }
        return tempRoot;
    }

    private int getIndex(ArrayList<T> list1, ArrayList<T> list2) {
        Set<T> set1 = new HashSet<>();
        Set<T> set2 = new HashSet<>();
        for (int i = 0; i < list1.size(); i++) {
            set1.add(list1.get(i));
            set2.add(list2.get(i));
            if (set1.equals(set2)) {
                return i;
            }
        }
        return 0;
    }

    private ArrayList<T> sliceLeft(ArrayList<T> list, int i) {
        ArrayList<T> answer = new ArrayList<>();
        for (int j = 0; j <= i; j++) {
            answer.add(list.get(j));
        }
        return answer;
    }

    private ArrayList<T> sliceRight(ArrayList<T> list, int i) {
        ArrayList<T> answer = new ArrayList<>();
        for (int j = i + 1; j <= list.size() - 1; j++) {
            answer.add(list.get(j));
        }
        return answer;
    }

    /* Print the values in the tree in preorder. */
    public void printPreorder() {
        if (root == null) {
            System.out.println("(empty tree)");
        } else {
            root.printPreorder();
            System.out.println();
        }
    }

    /* Print the values in the tree in inorder. */
    public void printInorder() {
        if (root == null) {
            System.out.println("(empty tree)");
        } else {
            root.printInorder();
            System.out.println();
        }
    }

    /* Prints the BinaryTree in preorder or in inorder. Used for testing. */
    protected static void print(BinaryTree t, String description) {
        System.out.println(description + " in preorder");
        t.printPreorder();
        System.out.println(description + " in inorder");
        t.printInorder();
        System.out.println();
    }

    protected class TreeNode {

        T item;
        TreeNode left;
        TreeNode right;
        int size = 0;

        public TreeNode(T item) {
            this.item = item; left = right = null;
        }

        public TreeNode(T item, TreeNode left, TreeNode right) {
            this.item = item;
            this.left = left;
            this.right = right;
        }

        /* Prints the nodes of the BinaryTree in preorder. Used for testing. */
        private void printPreorder() {
            System.out.print(item + " ");
            if (left != null) {
                left.printPreorder();
            }
            if (right != null) {
                right.printPreorder();
            }
        }

        /* Prints the nodes of the BinaryTree in inorder. Used for testing. */
        private void printInorder() {
            if (left != null) {
                left.printInorder();
            }
            System.out.print(item + " ");
            if (right != null) {
                right.printInorder();
            }
        }


    }
}
