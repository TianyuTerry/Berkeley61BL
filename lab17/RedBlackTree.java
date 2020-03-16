public class RedBlackTree<T extends Comparable<T>> {

    /* Root of the tree. */
    RBTreeNode<T> root;

    /* Creates an empty RedBlackTree. */
    public RedBlackTree() {
        root = null;
    }

    /* Creates a RedBlackTree from a given BTree (2-3-4) TREE. */
    public RedBlackTree(BTree<T> tree) {
        Node<T> btreeRoot = tree.root;
        root = buildRedBlackTree(btreeRoot);
    }

    /* Builds a RedBlackTree that has isometry with given 2-3-4 tree rooted at
       given node R, and returns the root node. */
    RBTreeNode<T> buildRedBlackTree(Node<T> r) {
        if (r == null) {
            return null;
        } else if (r.getItemCount() == 1) {
            RBTreeNode n = new RBTreeNode(true, r.getItemAt(0));
            n.left = buildRedBlackTree(r.getChildAt(0));
            n.right = buildRedBlackTree(r.getChildAt(1));
            return n;
        } else if (r.getItemCount() == 2) {
            RBTreeNode parent = new RBTreeNode(true, r.getItemAt(0));
            parent.left = buildRedBlackTree(r.getChildAt(0));
            parent.right = null;
            RBTreeNode child = new RBTreeNode(false, r.getItemAt(1));
            child.left = buildRedBlackTree(r.getChildAt(1));
            child.right = buildRedBlackTree(r.getChildAt(2));
            parent.right = child;
            return parent;
        } else if (r.getItemCount() == 3) {
            RBTreeNode parent = new RBTreeNode(true, r.getItemAt(1), null, null);
            RBTreeNode leftChild = new RBTreeNode(false, r.getItemAt(0));
            leftChild.left = buildRedBlackTree(r.getChildAt(0));
            leftChild.right = buildRedBlackTree(r.getChildAt(1));
            RBTreeNode rightChild = new RBTreeNode(false, r.getItemAt(2));
            rightChild.left = buildRedBlackTree(r.getChildAt(2));
            rightChild.right = buildRedBlackTree(r.getChildAt(3));
            parent.left = leftChild;
            parent.right = rightChild;
            return parent;
        }
        return null;
    }

    /* Flips the color of NODE and its children. Assume that NODE has both left
       and right children. */
    void flipColors(RBTreeNode<T> node) {
        node.isBlack = !node.isBlack;
        node.left.isBlack = !node.left.isBlack;
        node.right.isBlack = !node.right.isBlack;
    }

    /* Rotates the given node NODE to the right. Returns the new root node of
       this subtree. */
    RBTreeNode<T> rotateRight(RBTreeNode<T> node) {
        if (node.left != null) {
            node.left.isBlack = node.isBlack;
            node.isBlack = false;

            RBTreeNode a = node.left.left;
            RBTreeNode b = node.left.right;
            RBTreeNode c = node.right;
            RBTreeNode child = new RBTreeNode(node.isBlack, node.item, b, c);
            return new RBTreeNode(node.left.isBlack, node.left.item, a, child);
        }
        return node;
    }

    /* Rotates the given node NODE to the left. Returns the new root node of
       this subtree. */
    RBTreeNode<T> rotateLeft(RBTreeNode<T> node) {
        if (node.right != null) {
            node.right.isBlack = node.isBlack;
            node.isBlack = false;

            RBTreeNode a = node.left;
            RBTreeNode b = node.right.left;
            RBTreeNode c = node.right.right;
            RBTreeNode child = new RBTreeNode(node.isBlack, node.item, a, b);
            return new RBTreeNode(node.right.isBlack, node.right.item, child, c);
        }
        return node;
    }

    /* Insert ITEM into the red black tree, rotating
       it accordingly afterwards. */
    void insert(T item) {
        if (root == null) {
            root = new RBTreeNode(true, item);
        } else {
            root = addHelper(root, item);
            root.isBlack = true;
        }
    }

    public static void main(String[] args) {
        RedBlackTree<Integer> sampleTree = new RedBlackTree();
        sampleTree.insert(4);
        sampleTree.insert(5);
        sampleTree.insert(3);
        sampleTree.insert(2);
        sampleTree.insert(1);
        sampleTree.insert(7);
    }

    private RBTreeNode balanceHelper(RBTreeNode node) {
        if (node.left != null && node.right != null) {
            if (node.isBlack && !node.left.isBlack && !node.right.isBlack) {
                flipColors(node);
                return node;
            }
        }
        if (node.left != null) {
            if (node.left.left != null) {
                if (node.isBlack && !node.left.isBlack && !node.left.left.isBlack) {
                    return balanceHelper(rotateRight(node));
                }
            }
            if (node.left.right != null) {
                if (node.isBlack && !node.left.isBlack && !node.left.right.isBlack) {
                    node.left = rotateLeft(node.left);
                    return balanceHelper(node);
                }
            }
        }
        if (node.right != null) {
            if (node.isBlack && !node.right.isBlack && node.left == null) {
                return rotateLeft(node);
            }
        }
        return node;
    }

    private RBTreeNode addHelper(RBTreeNode t, T key) {
        if (key.compareTo((T) t.item) < 0) {
            if (t.left == null) {
                t.left = new RBTreeNode(false, key);
            } else {
                t.left = addHelper(t.left, key);
            }
        } else if (key.compareTo((T) t.item) > 0) {
            if (t.right == null) {
                t.right = new RBTreeNode(false, key);
            } else {
                t.right = addHelper(t.right, key);
            }
        }
        return balanceHelper(t);
    }

    /* Returns whether the given node NODE is red. Null nodes (children of leaf
       nodes are automatically considered black. */
    private boolean isRed(RBTreeNode<T> node) {
        return node != null && !node.isBlack;
    }

    static class RBTreeNode<T> {

        final T item;
        boolean isBlack;
        RBTreeNode<T> left;
        RBTreeNode<T> right;

        /* Creates a RBTreeNode with item ITEM and color depending on ISBLACK
           value. */
        RBTreeNode(boolean isBlack, T item) {
            this(isBlack, item, null, null);
        }

        /* Creates a RBTreeNode with item ITEM, color depending on ISBLACK
           value, left child LEFT, and right child RIGHT. */
        RBTreeNode(boolean isBlack, T item, RBTreeNode<T> left,
                   RBTreeNode<T> right) {
            this.isBlack = isBlack;
            this.item = item;
            this.left = left;
            this.right = right;
        }
    }

}
