//import edu.princeton.cs.algs4.BST;

public class BinarySearchTree<T extends Comparable<T>> extends BinaryTree<T> {

    /* Creates an empty BST. */
    public BinarySearchTree() {
        super();
    }

    /* Creates a BST with root as ROOT. */
    public BinarySearchTree(TreeNode root) {
        super(root);
    }

    /* Returns true if the BST contains the given KEY. */
    public boolean contains(T key) {
        return containsHelper(root, key);
    }

    private boolean containsHelper(TreeNode t, T key) {
        if (t == null) {
            return false;
        } else if (key.compareTo(t.item) == 0) {
            return true;
        } else if (key.compareTo(t.item) < 0) {
            return containsHelper(t.left, key);
        } else {
            return containsHelper(t.right, key);
        }
    }

    /* Adds a node for KEY iff KEY isn't in the BST already. */
    public void add(T key) {
        if (root == null) {
            root = new TreeNode(key);
        } else {
            addHelper(root, key);
        }
    }

    private void addHelper(TreeNode t, T key) {
        if (key.compareTo(t.item) < 0) {
            if (t.left == null) {
                t.left = new TreeNode(key);
            } else {
                addHelper(t.left, key);
            }
        } else if (key.compareTo(t.item) > 0) {
            if (t.right == null) {
                t.right = new TreeNode(key);
            } else {
                addHelper(t.right, key);
            }
        }
    }

    public static void main(String[] args) {
        BinarySearchTree<String> x = new BinarySearchTree<String>();
        x.add("C");
        x.add("A");
        x.add("E");
        x.add("B");
        x.add("D");
        x.add("A");
        x.add("B");
        x.printInorder();
        x.printPreorder();
        System.out.println(x.contains("C"));
        System.out.println(x.contains("A"));
        System.out.println(x.contains("E"));
        System.out.println(x.contains("B"));
        System.out.println(x.contains("D"));
        System.out.println(x.contains("F"));
    }



    /* Deletes a node from the BST. */
    public T delete(T key) {
        TreeNode parent = null;
        TreeNode curr = root;
        TreeNode delNode = null;
        TreeNode replacement = null;
        boolean rightSide = false;

        while (curr != null && !curr.item.equals(key)) {
            if (((Comparable<T>) curr.item).compareTo(key) > 0) {
                parent = curr;
                curr = curr.left;
                rightSide = false;
            } else {
                parent = curr;
                curr = curr.right;
                rightSide = true;
            }
        }
        delNode = curr;
        if (curr == null) {
            return null;
        }

        if (delNode.right == null) {
            if (root == delNode) {
                root = root.left;
            } else {
                if (rightSide) {
                    parent.right = delNode.left;
                } else {
                    parent.left = delNode.left;
                }
            }
        } else {
            curr = delNode.right;
            replacement = curr.left;
            if (replacement == null) {
                replacement = curr;
            } else {
                while (replacement.left != null) {
                    curr = replacement;
                    replacement = replacement.left;
                }
                curr.left = replacement.right;
                replacement.right = delNode.right;
            }
            replacement.left = delNode.left;
            if (root == delNode) {
                root = replacement;
            } else {
                if (rightSide) {
                    parent.right = replacement;
                } else {
                    parent.left = replacement;
                }
            }
        }
        return delNode.item;
    }

}
